package com.akihabara.market.service;

//Librerías necesarias para servicio LLM
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

//Clase que comunica con la API de OpenRouter para sugerir nombres de productos
public class LlmService {
    //Variable para almacenar la API Key leída desde el archivo de configuración
    private final String apiKey;

    //Cliente HTTP para realizar solicitudes a la API de OpenRouter
    private final HttpClient client;

    //Constructor de la clase
    public LlmService() {
        //Instancia de Properties para leer el archivo config.properties
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream("config.properties")) {
            //Cargar las propiedades del archivo en el objeto props
            props.load(fis);

            //Obtener la clave API desde las propiedades
            this.apiKey = props.getProperty("OPENROUTER_API_KEY");

            //Si la clave no está o está vacía, lanzar excepción (error)
            if (apiKey == null || apiKey.isEmpty()) {
                throw new IllegalStateException("API Key no encontrada en config.properties");
            }
            
        } catch (IOException e) {
            //En caso de error al leer el archivo, lanzar una excepción (error)
            throw new RuntimeException("Error al leer config.properties: " + e.getMessage(), e);
        }

        //Inicializar el cliente HTTP por defecto
        this.client = HttpClient.newHttpClient();
    }

    //Método que genera una sugerencia de nombre para un producto otaku, basado en su tipo y franquicia
    public String sugerirNombreProducto(String tipo, String franquicia) {
        //Prompt que se enviará al modelo de lenguaje
        String prompt = String.format(
            "Sugiere un nombre llamativo y original para un producto otaku del tipo '%s' basado en la franquicia '%s'.",
            tipo, franquicia
        );

        try {
            //Objeto JSON que representa el mensaje del usuario
            JsonObject message = new JsonObject();
            //Rol del mensaje: usuario
            message.addProperty("role", "user"); 
            //Contenido del mensaje: prompt
            message.addProperty("content", prompt); 

            //Array de mensajes
            JsonArray messages = new JsonArray();
            messages.add(message);

            //Cuerpo de la solicitud con el modelo y los mensajes
            JsonObject body = new JsonObject();
            body.addProperty("model", "mistralai/mistral-7b-instruct:free"); 
            //Mensajes a enviar al modelo
            body.add("messages", messages); 

            //Solicitud HTTP POST hacia la API de OpenRouter
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://openrouter.ai/api/v1/chat/completions")) 
                    .header("Authorization", "Bearer " + apiKey) 
                    .header("Content-Type", "application/json") 
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString())) 
                    .build();

            //Enviar la solicitud y capturar la respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //Parsear la respuesta JSON
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

            //Extraer el contenido del primer mensaje devuelto por la IA
            String result = json
                    .getAsJsonArray("choices") 
                    .get(0) 
                    .getAsJsonObject()
                    .getAsJsonObject("message") 
                    .get("content")
                    .getAsString(); 

            //Devolver el resultado sin espacios
            return result.trim();
            
        } catch (Exception e) {
            //En caso de error, imprimir mensaje y devolver null
            System.err.println("Error al sugerir nombre de producto: " + e.getMessage());
            return null;
        }
    }
}
