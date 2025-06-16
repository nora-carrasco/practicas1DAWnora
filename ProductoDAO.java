package com.akihabara.market.dao;

import com.akihabara.market.model.ProductoOtaku;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Clase DAO para realizar operaciones CRUD sobre la tabla producto en la base de datos akihabara_db
public class ProductoDAO {
    //Propiedad privada para conexión a la base de datos
    private final DatabaseConnection dbConnection;

    //Constructor para inicializar la conexión a la base de datos
    public ProductoDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    //Obtener la instancia de DatabaseConnection
    public DatabaseConnection getDbConnection() {
        return dbConnection;
    }

    //Agregar un nuevo producto a la base de datos
    public void agregarProducto(ProductoOtaku producto) throws SQLException {
        //Sólo poder elegir entre estas categorías
        String[] categoriasValidas = {"Figura", "Manga", "Póster", "Llavero", "Ropa"};
        //Si se elije una distinta, mostrar mensaje de error
        if (!List.of(categoriasValidas).contains(producto.getCategoria())) {
            throw new IllegalArgumentException("Categoría inválida: " + producto.getCategoria());
        }
        //Sentencia SQL para agregar el nuevo producto
        String sql = "INSERT INTO producto (nombre, categoria, precio, stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getCategoria());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.executeUpdate();
            System.out.println("Producto agregado exitosamente: " + producto.getNombre()); // Corrección: Mensaje de éxito
        //Manejar errores al agregar productos
        } catch (SQLException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
            throw e; 
        }
    }

    //Obtener un producto por su ID
    public ProductoOtaku obtenerProductoPorId(int id) throws SQLException {
        //Sentencia SQL para seleccionar un producto por su ID
        String sql = "SELECT * FROM producto WHERE id = ?";
        try (Connection conn = dbConnection.getConexion();
        	//Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            //Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ProductoOtaku(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                    );
                }
            }
        //Manejar errores al obtener productos
        } catch (SQLException e) {
            System.out.println("Error al obtener producto por ID: " + e.getMessage());
            throw e; 
        }
        return null;
    }

    //Obtener todos los productos de la base de datos
    public List<ProductoOtaku> obtenerTodosLosProductos() throws SQLException {
        //Lista para almacenar los productos
        List<ProductoOtaku> productos = new ArrayList<>();
        //Sentencia SQL para obtener todos los productos
        String sql = "SELECT * FROM producto";
        try (Connection conn = dbConnection.getConexion();
        	//Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql);
        	//Ejecutar la consulta
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                productos.add(new ProductoOtaku(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("categoria"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                ));
            }
            System.out.println("Se obtuvieron " + productos.size() + " productos."); 
        //Manejar errores al obtener productos
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los productos: " + e.getMessage());
            throw e; 
        }
        return productos;
    }

    //Actualizar un producto existente en la base de datos
    public boolean actualizarProducto(ProductoOtaku producto) throws SQLException {
        //Lista de categorías válidas
        String[] categoriasValidas = {"Figura", "Manga", "Póster", "Llavero", "Ropa"};
        //Si se elije una categoría inválida, mostrar mensaje de error
        if (!List.of(categoriasValidas).contains(producto.getCategoria())) {
            throw new IllegalArgumentException("Categoría inválida: " + producto.getCategoria());
        }
        //Sentencia SQL para actualizar el producto
        String sql = "UPDATE producto SET nombre = ?, categoria = ?, precio = ?, stock = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConexion();
        	//Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getCategoria());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            stmt.setInt(5, producto.getId());
            //Ejecutar la consulta
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Producto actualizado exitosamente: " + producto.getNombre()); // Corrección: Mensaje de éxito
                return true;
            //En caso no no existir un producto con ID introducido, mostrar error
            } else {
                System.out.println("No se encontró el producto con ID: " + producto.getId());
                return false;
            }
        //Manejar errores al actualizar productos
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            throw e; 
        }
    }

    //Eliminar un producto por su ID
    public boolean eliminarProducto(int id) throws SQLException {
        //Sentencia SQL para eliminar un producto por su ID
        String sql = "DELETE FROM producto WHERE id = ?";
        try (Connection conn = dbConnection.getConexion();
        	 //Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            //Ejecutar la consulta
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Producto eliminado exitosamente con ID: " + id); // Corrección: Mensaje de éxito
                return true;
            //En caso no no existir un producto con ID introducido, mostrar error
            } else {
                System.out.println("No se encontró el producto con ID: " + id);
                return false;
            }
        //Manejar errores al eliminar productos
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            throw e; 
        }
    }

    //Buscar productos por nombre
    public List<ProductoOtaku> buscarProductosPorNombre(String nombre) throws SQLException {
        //Lista para almacenar productos
        List<ProductoOtaku> productos = new ArrayList<>();
        //Sentencia SQL para buscar productos por su nombre
        String sql = "SELECT * FROM producto WHERE nombre LIKE ?";
        try (Connection conn = dbConnection.getConexion();
        	//Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            //Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos.add(new ProductoOtaku(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                    ));
                }
                System.out.println("Se encontraron " + productos.size() + " productos con nombre: " + nombre); // Corrección: Mensaje de éxito
            }
        //Manejar errores al buscar productos
        } catch (SQLException e) {
            System.out.println("Error al buscar productos por nombre: " + e.getMessage());
            throw e; 
        }
        //Devolver lista de productos
        return productos;
    }

    //Buscar productos por categoría
    public List<ProductoOtaku> buscarProductoPorCategoria(String categoria) throws SQLException {
        //Lista para almacenar productos
        List<ProductoOtaku> productos2 = new ArrayList<>();
        //Sentencia SQL para obtener productos de una categoría
        String sql = "SELECT * FROM producto WHERE categoria = ?";
        try (Connection conn = dbConnection.getConexion();
        	//Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria);
            //Ejecutar la consulta
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    productos2.add(new ProductoOtaku(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("categoria"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                    ));
                }
                System.out.println("Se encontraron " + productos2.size() + " productos en la categoría: " + categoria); // Corrección: Mensaje de éxito
            }
        //Manejar errores al buscar productos
        } catch (SQLException e) {
            System.out.println("Error al buscar productos por categoría: " + e.getMessage());
            throw e; // Corrección: Propagar excepción
        }
        //Devolver lista de productos
        return productos2;
    }
}