package com.akihabara.market.model;

public class ProductoOtaku {

	//Atributos privados
	private String nombre;
    private String categoria;
    private double precio;
    private int stock;
    private String descripcion;

    //Constructor vacío
    public ProductoOtaku() {
    }

    //Constructor para inicializar todos los parámetros
    public ProductoOtaku(String nombre, String categoria, double precio, int stock, String descripcion) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.descripcion = descripcion;
    }

    //Métodos getters y setters de todos los atributos
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    //Método toString para mostrar los datos de los productos
    @Override
    public String toString() {
        return "ProductoOtaku-> " +
                "Nombre: " + nombre +
                ", Categoría: " + categoria +
                ", Precio: " + precio +
                ", Stock: " + stock +
                ", Descripción: " + descripcion 
                ;
    }

}
