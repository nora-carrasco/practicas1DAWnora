//Clases DAO para realizar operaciones CRUD en las tablas producto y clientes
import com.akihabara.market.dao.ClienteDAO;
import com.akihabara.market.dao.ProductoDAO;
//Clases modelo de producto y cliente
import com.akihabara.market.model.ClienteOtaku;
import com.akihabara.market.model.ProductoOtaku;
//Clase para interacción con el usuario por consola
import com.akihabara.market.view.InterfazConsola;
//Errores con la base de datos
import java.sql.SQLException;
//Manejar fechas y listas (colecciones)
import java.util.Date;
import java.util.List;

//Servicio de sugerencias
import com.akihabara.market.service.LlmService;

//Clase Main que actúa como controlador del sistema, es la interacción entre la vista, el modelo y el acceso a datos
public class Main {
    public static void main(String[] args) {
    	
        //Instancia de la vista para interacción con el usuario
        InterfazConsola vista = new InterfazConsola();
        //DAO para la comunicación con la base de datos
        ProductoDAO productoDAO = new ProductoDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        //Instancia de LlmService para sugerir nombres de productos 
        LlmService llmService;
        
        try {
        	//Inicializar el servicio de IA
            llmService = new LlmService(); 
        } catch (Exception e) {
        	//Manejar errores con el inicio de la IA
            vista.mostrarMensaje("Error al inicializar el servicio de IA: " + e.getMessage());
            llmService = null; 
        }
        
        //Controlar el bucle del menú principal
        boolean salir = false; 

        //Bucle principal del menú (mostrar hasta elegir salir)
        try {
            while (!salir) {
                //Mostrar el menú principal al usuario
                vista.mostrarMenuPrincipal();
                //Solicitar y leer opción
                int opcion = vista.leerOpcion();

                switch (opcion) {
                	//Gestión de Productos
                    case 1: 
                    	//Llamar al método para gestionar productos con servicio IA incluido
                        gestionarProductos(vista, productoDAO, llmService); 
                        break;
                     //Gestión de Clientes
                    case 2: 
                    	//Llamar al método para gestionar clientes
                        gestionarClientes(vista, clienteDAO);
                        break;
                    //Salir del menú principal
                    case 3: 
                    	//Cerrar conexiones con la base de datos y salir
                        productoDAO.getDbConnection().cerrarConexion();
                        clienteDAO.getDbConnection().cerrarConexion();
                        vista.cerrarScanner();
                        salir = true;
                        vista.mostrarMensaje("Programa finalizado.");
                        break;
                    //En caso de que se elija una opción distinta a las anteriores, mostrar error y seguir solicitando
                    default:
                        vista.mostrarMensaje("Opción inválida. Intente de nuevo.");
                }
            }
            
         //Manejar errores durante la ejecución del programa
        } catch (Exception e) {
            vista.mostrarMensaje("Error inesperado en el programa: " + e.getMessage());
            e.printStackTrace();
            //Cerrar las conexiones y el objeto Scanner
            productoDAO.getDbConnection().cerrarConexion();
            clienteDAO.getDbConnection().cerrarConexion();
            vista.cerrarScanner();
        }
    }

    //Método para gestionar operaciones de productos
    private static void gestionarProductos(InterfazConsola vista, ProductoDAO productoDAO, LlmService llmService) {
    	
    	//Controlar el bucle del menú de productos
        boolean volver = false; 

        //Mientras no se elija salir
        while (!volver) {
        	//Mostrar menú de productos al usuario
            vista.mostrarMenuProductos();
            //Solicitar y leer opción
            int opcion = vista.leerOpcion();

            switch (opcion) {
            	//Añadir producto
                case 1: 
                    try {
                        //Preguntar si desea una sugerencia de nombre de la IA
                        String usarSugerencia = vista.leerTexto("¿Desea una sugerencia de nombre para el producto? (s/n): ").trim().toLowerCase();
                        String nombre;
                        if (usarSugerencia.equals("s") && llmService != null) {
                            //Solicitar datos para la sugerencia
                            String tipo = vista.leerTexto("Tipo de producto (ej. figura, camiseta): ");
                            String franquicia = vista.leerTexto("Franquicia (ej. Naruto, One Piece): ");
                            //Validar entradas del usuario
                            if (tipo.isEmpty() || franquicia.isEmpty()) {
                                vista.mostrarMensaje("El tipo y la franquicia no pueden estar vacíos.");
                                continue;
                            }
                            //Obtener sugerencia de la IA
                            String nombreSugerido = llmService.sugerirNombreProducto(tipo, franquicia);
                            //En caso de error al obtenerla, solicitar datos manuales
                            if (nombreSugerido == null) {
                                vista.mostrarMensaje("No se pudo obtener una sugerencia. Ingrese el nombre manualmente.");
                                nombre = vista.leerTexto("Nombre: ");
                            } else {
                                vista.mostrarMensaje("Nombre sugerido: " + nombreSugerido);
                                String confirmacion = vista.leerTexto("¿Desea usar este nombre? (s/n): ").trim().toLowerCase();
                                if (confirmacion.equals("s")) {
                                    nombre = nombreSugerido;
                                } else {
                                    nombre = vista.leerTexto("Ingrese un nombre para el producto: ");
                                }
                            }
                            
                        //Si no desea sugerencia, pedir nombre manualmente
                        } else {
                            nombre = vista.leerTexto("Nombre: ");
                        }
                        
                        //Validar que el nombre no esté vacío
                        if (nombre.isEmpty()) {
                            vista.mostrarMensaje("El nombre no puede estar vacío.");
                            continue;
                        }
                        
                        //Solicitar el resto de los datos del producto
                        String categoria = vista.leerTexto("Categoría (Figura, Manga, Póster, Llavero, Ropa): ");
                        double precio = vista.leerDouble("Precio: ");
                        int stock = vista.leerInt("Stock: ");
                        
                        //Crear y agregar el producto
                        productoDAO.agregarProducto(new ProductoOtaku(0, nombre, categoria, precio, stock));
                        vista.mostrarMensaje("Producto agregado exitosamente.");
                        
                    //Manejar errores al agregar el producto
                    } catch (IllegalArgumentException | SQLException e) {
                        vista.mostrarMensaje("Error al agregar producto: " + e.getMessage());
                    }
                    break;
                    
                 //Consultar producto por su ID
                case 2: 
                    try {
                    	//Solicitar ID del producto
                        int id = vista.leerInt("Ingrese ID del producto: ");
                        ProductoOtaku producto = productoDAO.obtenerProductoPorId(id);
                        //Si no existe ningún producto con ese ID, mostrar inexistencia
                        if (producto != null) vista.mostrarMensaje(producto.toString());
                        else vista.mostrarMensaje("Producto no encontrado.");
                        
                    //Manejar errores al consultar productos
                    } catch (SQLException e) {
                        vista.mostrarMensaje("Error al consultar producto: " + e.getMessage());
                    }
                    break;
                    
                 //Listar todos los productos
                case 3: 
                    try {
                    	//Obtener todos los productos y almacenar en una lista
                        List<ProductoOtaku> productos = productoDAO.obtenerTodosLosProductos();
                        //Si no hay productos, mostrar inexistencia
                        if (productos.isEmpty()) {
                            vista.mostrarMensaje("No hay productos registrados.");
                        } else {
                            productos.forEach(p -> vista.mostrarMensaje(p.toString()));
                        }
                        
                    //Manejar errores al listar productos
                    } catch (SQLException e) {
                        vista.mostrarMensaje("Error al listar productos: " + e.getMessage());
                    }
                    break;
                    
                 //Buscar productos por nombre
                case 4: 
                    try {
                    	//Solicitar nombre
                        String nombreB = vista.leerTexto("Ingrese nombre o parte del nombre: ");
                        List<ProductoOtaku> productos = productoDAO.buscarProductosPorNombre(nombreB);
                        //Si no hay productos, mostrar inexistencia
                        if (productos.isEmpty()) {
                            vista.mostrarMensaje("No se encontraron productos con ese nombre.");
                        } else {
                            productos.forEach(p -> vista.mostrarMensaje(p.toString()));
                        }
                        
                    //Manejar errores al buscar productos
                    } catch (SQLException e) {
                        vista.mostrarMensaje("Error al buscar productos por nombre: " + e.getMessage());
                    }
                    break;
                    
                //Buscar productos por categoría
                case 5:
                    try {
                    	//Solicitar categoría
                        String categoriaB = vista.leerTexto("Ingrese categoría: ");
                        List<ProductoOtaku> productos = productoDAO.buscarProductoPorCategoria(categoriaB);
                        //Si no se encuentran productos, mostrar inexistencia
                        if (productos.isEmpty()) {
                            vista.mostrarMensaje("No se encontraron productos en esa categoría.");
                        } else {
                            productos.forEach(p -> vista.mostrarMensaje(p.toString()));
                        }
                        
                    //Manejar errores al buscar productos
                    } catch (SQLException e) {
                        vista.mostrarMensaje("Error al buscar productos por categoría: " + e.getMessage());
                    }
                    break;
                    
                 //Actualizar producto
                case 6: 
                    try {
                    	//Solicitar ID del producto
                        int idU = vista.leerInt("ID del producto a actualizar: ");
                        ProductoOtaku producto = productoDAO.obtenerProductoPorId(idU);
                        if (producto != null) {
                        	//Solicitar nuevos datos
                            producto.setNombre(vista.leerTexto("Nuevo nombre: "));
                            producto.setCategoria(vista.leerTexto("Nueva categoría (Figura, Manga, Póster, Llavero, Ropa): "));
                            producto.setPrecio(vista.leerDouble("Nuevo precio: "));
                            producto.setStock(vista.leerInt("Nuevo stock: "));
                            if (productoDAO.actualizarProducto(producto)) {
                                vista.mostrarMensaje("Producto actualizado exitosamente.");
                              //Si no hay productos con ese ID, mostrar inexistencia
                            } else {
                                vista.mostrarMensaje("No se encontró el producto con ID: " + idU);
                            }
                            
                        //Si no hay productos con ese ID, mostrar inexistencia
                        } else {
                            vista.mostrarMensaje("Producto no encontrado.");
                        }
                        
                    //Manejar errores al actualizar productos
                    } catch (IllegalArgumentException | SQLException e) {
                        vista.mostrarMensaje("Error al actualizar producto: " + e.getMessage());
                    }
                    break;
                    
                 //Eliminar producto
                case 7: 
                    try {
                    	//Solicitar ID del producto
                        int idE = vista.leerInt("ID del producto a eliminar: ");
                        if (productoDAO.eliminarProducto(idE)) {
                            vista.mostrarMensaje("Producto eliminado exitosamente.");
                        //Si no hay productos con ese ID, mostrar inexistencia
                        } else {
                            vista.mostrarMensaje("No se encontró el producto con ID: " + idE);
                        }
                        
                    //Manejar errores al eliminar productos 
                    } catch (SQLException e) {
                        vista.mostrarMensaje("Error al eliminar producto: " + e.getMessage());
                    }
                    break;
                    
                //Volver al menú principal
                case 8: 
                    volver = true;
                    break;
                
                //En caso de opción distinta a las anteriores, mostrar error y seguir solicitando
                default:
                    vista.mostrarMensaje("Opción inválida. Intente de nuevo.");
            }
        }
    }

    //Método para gestionar operaciones de clientes
    private static void gestionarClientes(InterfazConsola vista, ClienteDAO clienteDAO) {
        boolean volver = false;

        while (!volver) {
            vista.mostrarMenuClientes();
            int opcion = vista.leerOpcion();

            switch (opcion) {
            	//Añadir cliente
                case 1: 
                    try {
                    	//Solicitar datos del nuevo cliente
                        String dni = vista.leerTexto("DNI (8 dígitos + letra): ");
                        String nombre = vista.leerTexto("Nombre: ");
                        String email = vista.leerTexto("Email: ");
                        String telefono = vista.leerTexto("Teléfono: ");
                        //Crear y añadir nuevo cliente
                        ClienteOtaku cliente = new ClienteOtaku(dni, nombre, email, telefono, new Date());
                        clienteDAO.agregarCliente(cliente);
                        vista.mostrarMensaje("Cliente agregado exitosamente.");
                    
                    //Manejar errores al agregar clientes
                    } catch (IllegalArgumentException | SQLException e) {
                        vista.mostrarMensaje("Error al agregar cliente: " + e.getMessage());
                    }
                    break;
                    
                //Consultar cliente por DNI
                case 2: 
                    try {
                    	//Solicitar DNI del cliente
                        String dniC = vista.leerTexto("Ingrese DNI del cliente: ");
                        ClienteOtaku cliente = clienteDAO.obtenerClientePorDni(dniC);
                        //Si no existe ningún cliente con ese DNI, mostrar inexistencia
                        if (cliente != null) vista.mostrarMensaje(cliente.toString());
                        else vista.mostrarMensaje("Cliente no encontrado.");
                        
                    //Manejar errores al consultar clientes
                    } catch (IllegalArgumentException | SQLException e) {
                        vista.mostrarMensaje("Error al consultar cliente: " + e.getMessage());
                    }
                    break;
                    
                 //Listar todos los clientes
                case 3: 
                    try {
                    	//Obtener todos los clientes y almacenarlos en una lista
                        List<ClienteOtaku> clientes = clienteDAO.obtenerTodosLosClientes();
                        //Si no hay clientes, mostrar inexistencia
                        if (clientes.isEmpty()) {
                            vista.mostrarMensaje("No hay clientes registrados.");
                        } else {
                            clientes.forEach(c -> vista.mostrarMensaje(c.toString()));
                        }
                    //Manejar errores al listar clientes
                    } catch (SQLException e) {
                        vista.mostrarMensaje("Error al listar clientes: " + e.getMessage());
                    }
                    break;
                    
                //Buscar clientes por nombre
                case 4: 
                    try {
                    	//Solicitar nombre
                        String nombreB = vista.leerTexto("Ingrese nombre o parte del nombre: ");
                        List<ClienteOtaku> clientes = clienteDAO.buscarClientesPorNombre(nombreB);
                        //Si no hay clientes con ese nombre, mostrar inexistencia
                        if (clientes.isEmpty()) {
                            vista.mostrarMensaje("No se encontraron clientes con ese nombre.");
                        } else {
                            clientes.forEach(c -> vista.mostrarMensaje(c.toString()));
                        }
                    //Manejar errores al buscar clientes
                    } catch (SQLException e) {
                        vista.mostrarMensaje("Error al buscar clientes por nombre: " + e.getMessage());
                    }
                    break;
                
                //Actualizar cliente
                case 5: 
                    try {
                    	//Solicitar DNI
                        String dniU = vista.leerTexto("DNI del cliente a actualizar: ");
                        ClienteOtaku cliente = clienteDAO.obtenerClientePorDni(dniU);
                        //Si existe el cliente con ese DNI, solicitar nuevos datos 
                        if (cliente != null) {
                            cliente.setNombre(vista.leerTexto("Nuevo nombre: "));
                            cliente.setEmail(vista.leerTexto("Nuevo email: "));
                            cliente.setTelefono(vista.leerTexto("Nuevo teléfono: "));
                            cliente.setFechaRegistro(new Date());
                            if (clienteDAO.actualizarCliente(cliente)) {
                                vista.mostrarMensaje("Cliente actualizado exitosamente.");
                            //En caso de error al actualizar, mostrar mensaje
                            } else {
                                vista.mostrarMensaje("No se pudo actualizar el cliente.");
                            }
                        //Si no existe el cliente, mostrar inexistencia
                        } else {
                            vista.mostrarMensaje("Cliente no encontrado.");
                        }
                    //Manejar errores al actualizar clientes
                    } catch (IllegalArgumentException | SQLException e) {
                        vista.mostrarMensaje("Error al actualizar cliente: " + e.getMessage());
                    }
                    break;
                    
                //Eliminar cliente
                case 6: 
                    try {
                    	//Solicitar DNI
                        String dniE = vista.leerTexto("DNI del cliente a eliminar: ");
                        if (clienteDAO.eliminarCliente(dniE)) {
                            vista.mostrarMensaje("Cliente eliminado exitosamente.");
                        //Si no hay cliente con ese ID, mostrar inexistencia
                        } else {
                            vista.mostrarMensaje("No se encontró el cliente con DNI: " + dniE);
                        }
                    //Manejar errores al eliminar clientes
                    } catch (IllegalArgumentException | SQLException e) {
                        vista.mostrarMensaje("Error al eliminar cliente: " + e.getMessage());
                    }
                    break;
                    
                 //Volver al menú principal
                case 7: 
                    volver = true;
                    break;
                    
                //En caso de opción distinta a las anteriores, mostrar error y volver a solicitar
                default:
                    vista.mostrarMensaje("Opción inválida. Intente de nuevo.");
            }
        }
    }
}