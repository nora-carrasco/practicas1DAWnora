public class Main {

	public static void main(String[] args) {	
		
		//Crear un objeto de ProductoOtaku
		com.akihabara.market.model.ProductoOtaku producto = new com.akihabara.market.model.ProductoOtaku(
	            "Figura de Goku", 
	            "Anime", 
	            49.99, 
	            10, 
	            "Figura coleccionable de Goku Super Saiyan"
	    );
	        
		//Mostrar información del producto con el método creado anteriormente
	    System.out.println(producto.toString());
	    
	}

}
