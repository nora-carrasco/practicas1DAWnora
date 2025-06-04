import com.akihabara.market.dao.DatabaseConnection;
import com.akihabara.market.dao.ProductoDAO;
import com.akihabara.market.model.ProductoOtaku;
import com.akihabara.market.view.InterfazConsola;
import java.sql.SQLException;
import java.util.List;

//Clase Main que actúa como Controlador del sistema
//Interacción entre la vista (consola), el modelo (ProductoOtaku) y el acceso a datos (ProductoDAO).
public class Main {
	public static void main(String[] args) {
		//Instancia de la vista para interacción con el usuario
        InterfazConsola vista = new InterfazConsola();
        //DAO para la comunicación con la base de datos
        ProductoDAO productoDAO = new ProductoDAO();

        boolean salir = false;
        
        //Bucle principal del menú (mostrar hasta elegir salir)
        while (!salir) {
        	//Mostrar el menú al usuario
            vista.mostrarMenuPrincipal();
            //Leer la opción introducida
            int opcion = vista.leerOpcion();

            //Estructura switch para manejar cada opción del menú
            switch (opcion) {
            	//Opción 1: Agregar nuevo producto
                case 1 -> {
                	//Solicitar datos 
                    String nombre = vista.leerTexto("Nombre: ");
                    String categoria = vista.leerTexto("Categoría: ");
                    double precio = vista.leerDouble("Precio: ");
                    int stock = vista.leerInt("Stock: ");
                    //Crear y guardar un nuevo producto
                    productoDAO.agregarProducto(new ProductoOtaku(0, nombre, categoria, precio, stock));
                }
                //Opción 2: Consultar producto por ID
                case 2 -> {
                	//Solicitar ID del producto
                    int id = vista.leerInt("Ingrese ID del producto: ");
                    //Obtener producto por su ID
                    ProductoOtaku producto = productoDAO.obtenerProductoPorId(id);
                    //Mostrar resultado
                    if (producto != null) vista.mostrarMensaje(producto.toString());
                    else vista.mostrarMensaje("Producto no encontrado.");
                }
                //Opción 3: Listar todos los productos
                case 3 -> {
                	//Lista de productos
                    List<ProductoOtaku> productos = productoDAO.obtenerTodosLosProductos();
                    productos.forEach(p -> vista.mostrarMensaje(p.toString()));
                }
                //Opción 4: Buscar productos por nombre
                case 4 -> {
                	//Solicitar nombre
                    String nombre = vista.leerTexto("Ingrese nombre o parte del nombre: ");
                    //Lista de productos
                    List<ProductoOtaku> productos = productoDAO.buscarProductosPorNombre(nombre);
                    productos.forEach(p -> vista.mostrarMensaje(p.toString()));
                }
                //Opción 5: Buscar productos por categoría
                case 5 -> {
                	//Solicitar categoría
                    String categoria = vista.leerTexto("Ingrese categoría: ");
                    //Lista de productos
                    List<ProductoOtaku> productos = productoDAO.buscarProductoPorCategoria(categoria);
                    productos.forEach(p -> vista.mostrarMensaje(p.toString()));
                }
                //Opción 6: Actualizar producto existente
                case 6 -> {
                	//Solicitar ID del producto
                    int id = vista.leerInt("ID del producto a actualizar: ");
                    ProductoOtaku producto = productoDAO.obtenerProductoPorId(id);
                    if (producto != null) {
                    	//Leer nuevos valores
                        producto.setNombre(vista.leerTexto("Nuevo nombre: "));
                        producto.setCategoria(vista.leerTexto("Nueva categoría: "));
                        producto.setPrecio(vista.leerDouble("Nuevo precio: "));
                        producto.setStock(vista.leerInt("Nuevo stock: "));
                        //Actualizar producto en la base de datos
                        productoDAO.actualizarProducto(producto);
                    } else {
                        vista.mostrarMensaje("Producto no encontrado.");
                    }
                }
                //Opción 7: Eliminar producto por ID
                case 7 -> {
                	//Solicitar ID del producto
                    int id = vista.leerInt("ID del producto a eliminar: ");
                    productoDAO.eliminarProducto(id);
                }
                //Opción 8: Salir del menú
                case 8 -> {
                	//Cerrar conexión
                    productoDAO.getDbConnection().cerrarConexion();
                    //Cerrar scanner
                    vista.cerrarScanner();
                    salir = true;
                    vista.mostrarMensaje("Programa finalizado.");
                }
                //Opción distinta a las anteriores, mostrar mensaje de error y volver a solicitar
                default -> vista.mostrarMensaje("Opción inválida. Intente de nuevo.");
            }
        }
    }
}

