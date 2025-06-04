import com.akihabara.market.dao.DatabaseConnection;
import com.akihabara.market.dao.ProductoDAO;
import com.akihabara.market.model.ProductoOtaku;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //Crear una instancia de ProductoDAO
        ProductoDAO productoDAO = new ProductoDAO();

        //Agregar un nuevo producto
        System.out.println("Agregar Producto");
        //Agregar el nuevo producto con sus datos
        ProductoOtaku nuevoProducto = new ProductoOtaku(0, "Figura de Levi Ackerman", "Figura", 39.99, 8);
        productoDAO.agregarProducto(nuevoProducto);

        //Obtener un producto por ID
        System.out.println("Obtener Producto por ID");
        //Obtener el producto con ID 1
        ProductoOtaku producto = productoDAO.obtenerProductoPorId(1);
        //Si se encuentra, mostrar producto
        if (producto != null) {
            System.out.println("Producto encontrado: " + producto);
        //Si no, mostrar mensaje de inexistencia
        } else {
            System.out.println("Producto no encontrado con ID 1.");
        }

        //Obtener todos los productos
        System.out.println("Obtener Todos los Productos");
        //Obtener una lista con todos los productos
        List<ProductoOtaku> todosLosProductos = productoDAO.obtenerTodosLosProductos();
        //Para cada producto, mostrar sus datos
        for (ProductoOtaku p : todosLosProductos) {
            System.out.println(p);
        }

        //Actualizar un producto
        System.out.println("Actualizar Producto");
        //En caso de que exista, actualizar sus datos
        if (producto != null) {
            producto.setPrecio(49.99);
            producto.setStock(10);
            productoDAO.actualizarProducto(producto);
        }

        //Eliminar un producto
        System.out.println("Eliminar Producto");
        //Eliminar el producto con ID 2
        productoDAO.eliminarProducto(2); 

        //Buscar productos por nombre
        System.out.println("Buscar Productos por Nombre");
        //Obtener productos con nombre "Naruto"
        List<ProductoOtaku> productosPorNombre = productoDAO.buscarProductosPorNombre("Naruto");
        //Para cada producto encontrado
        for (ProductoOtaku p : productosPorNombre) {
        	//Mostrar sus datos
            System.out.println(p);
        }

        //Buscar productos por categoría
        System.out.println("Buscar Productos por Categoría");
        //Obtener productos de la categoría "Figura"
        List<ProductoOtaku> productosPorCategoria = productoDAO.buscarProductoPorCategoria("Figura");
        //Para cada producto en la lista, mostrar sus datos
        for (ProductoOtaku p : productosPorCategoria) {
            System.out.println(p);
        }

        //Cerrar la conexión
        productoDAO.getDbConnection().cerrarConexion();
    }
}

