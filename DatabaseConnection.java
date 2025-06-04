package com.akihabara.market.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Clase para gestionar la conexión a la base de datos MySQL akihabara_db
public class DatabaseConnection {

    //Variables constantes con los datos de conexión
    private static final String DB_URL = "jdbc:mysql://localhost:3306/akihabara_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "userAkihabara";
    private static final String PASSWORD = "curso";

    //Propiedad privada para almacenar la conexión
    private Connection conexion;

    //Constructor que carga el driver de MySQL y establece la conexión a la base de datos
    public DatabaseConnection() {
    	
        try {
            //Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Mostrar mensaje de éxito
            System.out.println("Se ha cargado en memoria el driver de MySQL.");

            //Establecer la conexión a la base de datos con sus datos
            conexion = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            //Mostrar mensaje de éxito
            System.out.println("Se ha establecido con éxito la conexión a la base de datos.");
        
        //En caso de error con el driver, mostrar mensaje
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se pudo cargar el driver de MySQL. Causa: " + e.getMessage());
            e.printStackTrace();
          
        //En caso de error con la base de datos, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error: No se pudo establecer la conexión a la base de datos. Causa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Obtener la conexión a la base de datos
    public Connection getConexion() {
    	
        try {
        	//Si la conexión no está activa o está cerrada
            if (conexion == null || conexion.isClosed()) {
            	//Volver a conectar
                conexion = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                //Mostrar mensaje de éxito
                System.out.println("Se ha reabierto la conexión a la base de datos.");
            }
            
        //En caso de error al abrir la nueva conexión, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al intentar reabrir la conexión: " + e.getMessage());
            e.printStackTrace();
        }
        
        //Devolver la nueva conexión
        return conexion;
    }

    //Cerrar la conexión a la base de datos si está activa
    public void cerrarConexion() {
    	
    	//Si la conexiín está activa
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                	//Cerrar la conexión y mostrar mensaje de éxito
                    conexion.close();
                    System.out.println("Se ha cerrado la conexión con la base de datos.");
                //Si no, mostrar mensaje informativo
                } else {
                    System.out.println("La conexión ya estaba cerrada.");
                }
            
            //En caso de error al cerrar la conexión, mostrar mensaje
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
            
        //En caso de no haber ninguna conexión, mostrar mensaje
        } else {
            System.out.println("No hay ninguna conexión activa para cerrar.");
        }
    }
}

