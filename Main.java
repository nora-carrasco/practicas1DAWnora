import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {
    public static void main(String[] args) {
    	
    	//Mi APIKEY
        String apiKey = "My API Key here";
        //Objeto Scanner para leer entradas del usuario por la consola
        Scanner scanner = new Scanner(System.in);
        //Lista para almacenar mensajes de las conversaciones
        List<String> conversationHistory = new ArrayList<>();
        
        //Crear HTTP client
        HttpClient client = HttpClient.newHttpClient();
        
        while (true) { 
            //Solicitar y leer entrada al usuario
            System.out.print("Enter your prompt (or 'FIN' to end): ");
            String prompt = scanner.nextLine();
            
            //Comprobar si se ha introducido FIN
            if (prompt.equalsIgnoreCase("FIN")) {
            	//Terminar el chat
                break;
            }
            
            //Guardar las entradas del usuario por consola en la lista de mensajes
            conversationHistory.add("User: " + prompt);
            
            try {
                //Constructor mensaje JSON 
                JsonObject message = new JsonObject();
                message.addProperty("role", "user");
                message.addProperty("content", prompt);

                //Añadir cada mensaje de la conversación
                JsonArray messages = new JsonArray();
                messages.add(message);

                JsonObject body = new JsonObject();
                body.addProperty("model", "mistralai/mistral-7b-instruct:free");
                body.add("messages", messages);

                //Crear HTTP POST request
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://openrouter.ai/api/v1/chat/completions"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

                //Enviar request y recibir respuesta
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                //Procesar respuesta JSON 
                JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
                String result = json
                    .getAsJsonArray("choices")
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content")
                    .getAsString();

                //Guardar respuestas del LLM en la lista
                System.out.println("LLM Response:\n" + result + "\n");
                conversationHistory.add("Assistant: " + result);
                
            //En caso de error de comunicación
            } catch (Exception e) {
            	//Mostrar mensaje de error y añadir a la lista
                String errorMsg = "Error communicating with OpenRouter: " + e.getMessage();
                System.out.println(errorMsg);
                conversationHistory.add(errorMsg);
            }
        }
        
        //Guardar la conversación en el archivo
        try {
            //Generar el nombre del archivo con la fecha y hora actual 
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String filename = "chat_" + now.format(formatter) + ".txt";
            
            //Pasar la conversación al archivo
            try (FileWriter writer = new FileWriter(filename)) {
                for (String entry : conversationHistory) {
                    writer.write(entry + "\n\n");
                }
                System.out.println("Conversation saved to " + filename);
            }
            
        //En caso de error al guardar la conversación en el archivo
        } catch (IOException e) {
        	//Mostrar mensaje de error
            System.out.println("Error saving conversation to file: " + e.getMessage());
        }
        
        //Cerrar el scanner
        scanner.close();
    }
}