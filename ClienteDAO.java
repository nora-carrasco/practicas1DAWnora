package com.akihabara.market.dao;

import com.akihabara.market.model.ClienteOtaku;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Clase DAO para realizar operaciones CRUD sobre la tabla clientes en la base de datos akihabara_db
public class ClienteDAO {
    //Propiedad privada para conexión a la base de datos
    private final DatabaseConnection dbConnection;

    //Constructor para inicializar la conexión a la base de datos
    public ClienteDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    //Obtener la instancia de DatabaseConnection
    public DatabaseConnection getDbConnection() {
        return dbConnection;
    }

    //Agregar un nuevo cliente a la base de datos
    public void agregarCliente(ClienteOtaku cliente) throws SQLException {
        // Corrección: Validar formato del DNI
        if (!cliente.getDni().matches("^[0-9]{8}[A-Za-z]$")) {
            throw new IllegalArgumentException("DNI inválido: debe tener 8 dígitos y una letra.");
        }
        //Sentencia SQL para agregar el nuevo cliente
        String sql = "INSERT INTO clientes (dni, nombre, email, telefono, fecha_registro) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConexion();
             //Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getDni());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefono());
            stmt.setDate(5, new java.sql.Date(cliente.getFechaRegistro().getTime()));
            //Ejecutar la consulta
            stmt.executeUpdate();
            System.out.println("Cliente agregado exitosamente: " + cliente.getNombre());
        //En caso de error al agregar, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al agregar cliente: " + e.getMessage());
            throw e; // Corrección: Propagar excepción
        }
    }

    //Obtener un cliente por su DNI
    public ClienteOtaku obtenerClientePorDni(String dni) throws SQLException {
        // Corrección: Validar formato del DNI
        if (!dni.matches("^[0-9]{8}[A-Za-z]$")) {
            throw new IllegalArgumentException("DNI inválido: debe tener 8 dígitos y una letra.");
        }
        //Sentencia SQL para seleccionar un cliente por su DNI
        String sql = "SELECT * FROM clientes WHERE dni = ?";
        try (Connection conn = dbConnection.getConexion();
             //Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            //Ejecutar consulta y almacenar resultados
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ClienteOtaku(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getDate("fecha_registro")
                    );
                }
            }
        //En caso de error al obtener el cliente, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al obtener cliente por DNI: " + e.getMessage());
            throw e; // Corrección: Propagar excepción
        }
        return null;
    }

    //Obtener todos los clientes de la base de datos
    public List<ClienteOtaku> obtenerTodosLosClientes() throws SQLException {
        //Lista para almacenar los clientes
        List<ClienteOtaku> clientes = new ArrayList<>();
        //Sentencia SQL para obtener todos los clientes
        String sql = "SELECT * FROM clientes";
        try (Connection conn = dbConnection.getConexion();
             //Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql);
             //Ejecutar la consulta y obtener resultados
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                clientes.add(new ClienteOtaku(
                    rs.getString("dni"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getDate("fecha_registro")
                ));
            }
            System.out.println("Se obtuvieron " + clientes.size() + " clientes.");
        //En caso de error al obtener los clientes, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los clientes: " + e.getMessage());
            throw e; // Corrección: Propagar excepción
        }
        //Devolver la lista de clientes
        return clientes;
    }

    //Actualizar un cliente existente en la base de datos
    public boolean actualizarCliente(ClienteOtaku cliente) throws SQLException {
        // Corrección: Validar formato del DNI
        if (!cliente.getDni().matches("^[0-9]{8}[A-Za-z]$")) {
            throw new IllegalArgumentException("DNI inválido: debe tener 8 dígitos y una letra.");
        }
        //Sentencia SQL para actualizar el cliente
        String sql = "UPDATE clientes SET nombre = ?, email = ?, telefono = ?, fecha_registro = ? WHERE dni = ?";
        try (Connection conn = dbConnection.getConexion();
             //Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            stmt.setDate(4, new java.sql.Date(cliente.getFechaRegistro().getTime()));
            stmt.setString(5, cliente.getDni());
            int rowsAffected = stmt.executeUpdate();
            //En caso de haberse actualizado, mostrar mensaje de éxito
            if (rowsAffected > 0) {
                System.out.println("Cliente actualizado exitosamente: " + cliente.getNombre());
                return true;
            //En caso de no encontrar ningún cliente con ese DNI, mostrar mensaje de error
            } else {
                System.out.println("No se encontró el cliente con DNI: " + cliente.getDni());
                return false;
            }
        //En caso de error al actualizar el cliente, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
            throw e; // Corrección: Propagar excepción
        }
    }

    //Eliminar un cliente por su DNI
    public boolean eliminarCliente(String dni) throws SQLException {
        // Corrección: Validar formato del DNI
        if (!dni.matches("^[0-9]{8}[A-Za-z]$")) {
            throw new IllegalArgumentException("DNI inválido: debe tener 8 dígitos y una letra.");
        }
        //Sentencia SQL para eliminar un cliente por su DNI
        String sql = "DELETE FROM clientes WHERE dni = ?";
        try (Connection conn = dbConnection.getConexion();
             //Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dni);
            //Ejecutar la consulta
            int rowsAffected = stmt.executeUpdate();
            //En caso de haberse eliminado, mostrar mensaje de éxito
            if (rowsAffected > 0) {
                System.out.println("Cliente eliminado exitosamente con DNI: " + dni);
                return true;
            //En caso de no existir ningún cliente con ese DNI, mostrar mensaje de error
            } else {
                System.out.println("No se encontró el cliente con DNI: " + dni);
                return false;
            }
        //En caso de error al eliminar un cliente, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
            throw e; // Corrección: Propagar excepción
        }
    }

    //Buscar clientes por nombre
    public List<ClienteOtaku> buscarClientesPorNombre(String nombre) throws SQLException {
        //Lista para almacenar clientes
        List<ClienteOtaku> clientes = new ArrayList<>();
        //Sentencia SQL para buscar clientes por su nombre
        String sql = "SELECT * FROM clientes WHERE nombre LIKE ?";
        try (Connection conn = dbConnection.getConexion();
             //Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            //Ejecutar la consulta y obtener resultados
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(new ClienteOtaku(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getDate("fecha_registro")
                    ));
                }
                System.out.println("Se encontraron " + clientes.size() + " clientes con nombre: " + nombre);
            }
        //En caso de error al buscar clientes, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al buscar clientes por nombre: " + e.getMessage());
            throw e; // Corrección: Propagar excepción
        }
        //Devolver lista de clientes
        return clientes;
    }
}