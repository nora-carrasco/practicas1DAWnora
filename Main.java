import com.akihabara.market.dao.DatabaseConnection;
import java.sql.Connection;

public class Main {

	public static void main(String[] args) {
		
        //Crear nueva instancia de conexión
        DatabaseConnection db = new DatabaseConnection();

        //Obtener y verificar la conexión
        Connection conexion = db.getConexion();
        
        //En caso de que sea correcta, mostrar mensaje de éxito
        if (conexion != null) {
            System.out.println("Conexión verificada correctamente.");
        //En caso de error al establecer la conexión, mostrar mensaje de error
        } else {
            System.out.println("No se pudo establecer la conexión.");
        }

        //Cerrar la conexión
        db.cerrarConexion();
    }

}
