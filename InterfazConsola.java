package com.akihabara.market.view;

//Scanner para leer entradas desde la consola
import java.util.Scanner;

//Clase que representa la interfaz de usuario en consola
//Encapsula la interacción con el usuario: mostrar menús, leer entradas y mostrar mensajes
public class InterfazConsola {
    //Scanner privado para leer entradas del usuario
    private final Scanner scanner;
    
    //Constructor que inicializa el Scanner
    public InterfazConsola() {
        scanner = new Scanner(System.in);
    }
    
    //Mostrar menú principal
    public void mostrarMenuPrincipal() {
        System.out.println("\n=== Menú Principal ===");
        System.out.println("1. Gestión de Productos");
        System.out.println("2. Gestión de Clientes");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opción: ");
    }
    
    //Mostrar menú de productos
    public void mostrarMenuProductos() {
        System.out.println("\n=== Gestión de Productos ===");
        System.out.println("1. Añadir producto");
        System.out.println("2. Consultar producto por ID");
        System.out.println("3. Listar todos los productos");
        System.out.println("4. Buscar productos por nombre");
        System.out.println("5. Buscar productos por categoría");
        System.out.println("6. Actualizar producto");
        System.out.println("7. Eliminar producto");
        System.out.println("8. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
    }
    
    //Mostrar menú de clientes
    public void mostrarMenuClientes() {
        System.out.println("\n=== Gestión de Clientes ===");
        System.out.println("1. Añadir cliente");
        System.out.println("2. Consultar cliente por DNI");
        System.out.println("3. Listar todos los clientes");
        System.out.println("4. Buscar clientes por nombre");
        System.out.println("5. Actualizar cliente");
        System.out.println("6. Eliminar cliente");
        System.out.println("7. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
    }
    
    //Leer opción del menú introducida por el usuario
    public int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
            
        //En caso de ser inválida, devolver -1
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    //Mostrar mensaje y leer cadena de texto introducida por el usuario
    public String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }
    
    //Mostrar mensaje y leer número decimal introducido por el usuario
    public double leerDouble(String mensaje) {
        //Mostrar mensaje solicitando hasta que el número sea válido
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(scanner.nextLine());
                
            //En caso de ser inválido, mostrar mensaje de error y volver a solicitar
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
    }
    
    //Mostrar mensaje y leer número entero introducido por el usuario
    public int leerInt(String mensaje) {
        //Mostrar mensaje solicitando hasta que el número sea válido
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(scanner.nextLine());
                
            //En caso de ser inválido, mostrar mensaje de error y volver a solicitar
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número entero válido.");
            }
        }
    }
    
    //Mostrar mensaje al usuario
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
    
    //Cerrar el scanner
    public void cerrarScanner() {
        scanner.close();
    }
}