package com.akihabara.market.dao;

//Librerías para conectar con la base de datos
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    //Constantes para establecer la conexión a la base de datos
    private static final String DB_URL = "jdbc:mysql://localhost:3306/akihabara_db?useSSL=false&serverTimezone=UTC"; // Corrección: Agregar parámetros para compatibilidad
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
            
        //En caso de error al cargar el driver, mostrar mensaje de error
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver de MySQL: " + e.getMessage());
            throw new RuntimeException("Driver no encontrado", e); // Corrección: Lanzar excepción
        }
    }

    //Método para obtener la conexión actual
    public Connection getConexion() throws SQLException {
    	//Verificar si la conexión está cerrada
        if (conexion == null || conexion.isClosed()) { 
        	//Obtener la conexión
            conexion = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            System.out.println("Se ha establecido con éxito la conexión a la base de datos.");
        }
        return conexion;
    }

    //Método para cerrar la conexión a la base de datos
    public void cerrarConexion() {
        //Comprobar que haya una conexión activa
        if (conexion != null) {
            try {
                //Cerrar la conexión si no está ya cerrada y mostrar mensaje de éxito
                if (!conexion.isClosed()) { 
                    conexion.close();
                    System.out.println("Se ha cerrado la conexión con la base de datos.");
                }
            //En caso de error al cerrar la conexión, mostrar mensaje de error
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}