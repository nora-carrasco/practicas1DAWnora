package com.akihabara.market.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	//Constantes para establecer la conexión a la base de datos
    private static final String DB_URL = "jdbc:mysql://localhost:3306/akihabara_db"; 
    private static final String USER = "userAkihabara";
    private static final String PASSWORD = "curso";

    //Propiedad para almacenar la conexión
    private Connection conexion;

    //Constructor para cargar el driver y establecer la conexión con la base de datos
    public DatabaseConnection() {
        try {
        	
            //Cargar el driver de MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Mostrar mensaje de éxito
            System.out.println("Se ha cargado en memoria el driver de MySQL.");

            //Establecer la conexión
            conexion = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            //Mostrar mensaje de éxito
            System.out.println("Se ha establecido con éxito la conexión a la base de datos.");
            
        //En caso de error al cargar el driver, mostrar mensaje de error
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver de MySQL: " + e.getMessage());
            
        //En caso de error al establecer la conexión, mostrar mensaje de error
        } catch (SQLException e) {
            System.out.println("Error al establecer la conexión: " + e.getMessage());
        }
    }

    //Método para obtener la conexión actual
    public Connection getConexion() {
        return conexion;
    }

    //Método para cerrar la conexión a la base de datos
    public void cerrarConexion() {
    	//Comprobar que haya una conexión activa
        if (conexion != null) {
            try {
            	//Cerrar la conexión y mostrar mensaje de éxito
                conexion.close();
                System.out.println("Se ha cerrado la conexión con la base de datos.");
            
            //En caso de error al cerrar la conexión, mostrar mensaje de error
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

}
