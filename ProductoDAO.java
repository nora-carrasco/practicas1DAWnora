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

    //Agregar un nuevo producto a la base de datos.
    public void agregarProducto(ProductoOtaku producto) {
    	//Sentencia SQL para agregar el nuevo producto
        String sql = "INSERT INTO producto (nombre, categoria, precio, stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConexion();
        	//Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getCategoria());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setInt(4, producto.getStock());
            //Ejecutar la consulta
            stmt.executeUpdate();
            System.out.println("Producto agregado exitosamente: " + producto.getNombre());
        //En caso de error al agregar, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Obtener un producto por su ID
    public ProductoOtaku obtenerProductoPorId(int id) {
    	//Sentencia SQL para seleccionar un producto por su ID
        String sql = "SELECT * FROM producto WHERE id = ?";
        try (Connection conn = dbConnection.getConexion();
        	//Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            //Ejecutar consulta y almacenar resultados
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
        //En caso de error al obtener el producto, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al obtener producto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //Obtener todos los productos de la base de datos
    public List<ProductoOtaku> obtenerTodosLosProductos() {
    	//Lista para almacenar los productos
        List<ProductoOtaku> productos = new ArrayList<>();
        //Sentencia SQL para obtener todos los productos
        String sql = "SELECT * FROM producto";
        try (Connection conn = dbConnection.getConexion();
        	//Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql);
        	//Ejecutar la consulta y obtener resultados
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
        //En caso de error al obtener los productos, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los productos: " + e.getMessage());
            e.printStackTrace();
        }
        //Devolver la lista de productos
        return productos;
    }

    //Actualizar un producto existente en la base de datos
    public boolean actualizarProducto(ProductoOtaku producto) {
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
            int rowsAffected = stmt.executeUpdate();
            //En caso de haberse actualizado, mostrar mensaje de éxito
            if (rowsAffected > 0) {
                System.out.println("Producto actualizado exitosamente: " + producto.getNombre());
                return true;
            //En caso de no encontrar ningún producto con ese ID, mostrar mensaje de error
            } else {
                System.out.println("No se encontró el producto con ID: " + producto.getId());
                return false;
            }
        //En caso de error al actualizar el producto, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //Eliminar un producto por su ID
    public boolean eliminarProducto(int id) {
    	//Sentencia SQL para eliminar un producto por su ID
        String sql = "DELETE FROM producto WHERE id = ?";
        try (Connection conn = dbConnection.getConexion();
        	//Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            //Ejecutar la consulta
            int rowsAffected = stmt.executeUpdate();
            //En caso de haberse eliminado, mostrar mensaje de éxito
            if (rowsAffected > 0) {
                System.out.println("Producto eliminado exitosamente con ID: " + id);
                return true;
            //En caso de no existir ningún producto con ese ID, mostrar mensaje de error
            } else {
                System.out.println("No se encontró el producto con ID: " + id);
                return false;
            }
        //En caso de error al eliminar un producto, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //Buscar productos por nombre
    public List<ProductoOtaku> buscarProductosPorNombre(String nombre) {
    	//Lista para almacenar productos
        List<ProductoOtaku> productos = new ArrayList<>();
        //Sentencia SQL para buscar productos por su nombre
        String sql = "SELECT * FROM producto WHERE nombre LIKE ?";
        try (Connection conn = dbConnection.getConexion();
        	//Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nombre + "%");
            //Ejecutar la consulta y obtener resultados
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
                System.out.println("Se encontraron " + productos.size() + " productos con nombre: " + nombre);
            }
        //En caso de error al buscar productos, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al buscar productos por nombre: " + e.getMessage());
            e.printStackTrace();
        }
        //Devolver lista de productos
        return productos;
    }

    //Buscar productos por categoría
    public List<ProductoOtaku> buscarProductoPorCategoria(String categoria) {
        //Lista para almacenar productos
    	List<ProductoOtaku> productos = new ArrayList<>();
    	//Sentencia SQL para obtener productos de una categoría
        String sql = "SELECT * FROM producto WHERE categoria = ?";
        try (Connection conn = dbConnection.getConexion();
        	//Preparar la consulta
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria);
            //Ejecutar la consulta y obtener resultados
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
                System.out.println("Se encontraron " + productos.size() + " productos en la categoría: " + categoria);
            }
        //En caso de error al obtener productos, mostrar mensaje
        } catch (SQLException e) {
            System.out.println("Error al buscar productos por categoría: " + e.getMessage());
            e.printStackTrace();
        }
        //Devolver lista de productos
        return productos;
    }
}
