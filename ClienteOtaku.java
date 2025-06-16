package com.akihabara.market.model;

import java.util.Date;

public class ClienteOtaku {
	
    //Atributos privados
    private String dni;
    private String nombre;
    private String email;
    private String telefono;
    private Date fechaRegistro;

    //Constructor vacío
    public ClienteOtaku() {
    }

    //Constructor para inicializar todos los parámetros
    public ClienteOtaku(String dni, String nombre, String email, String telefono, Date fechaRegistro) {
        this.dni = dni;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fechaRegistro = fechaRegistro;
    }

    //Métodos getters y setters de todos los atributos
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    //Método toString para mostrar los datos de los clientes
    @Override
    public String toString() {
        return "ClienteOtaku -> " +
                "DNI: " + dni +
                ", Nombre: " + nombre +
                ", Email: " + email +
                ", Teléfono: " + telefono +
                ", Fecha Registro: " + fechaRegistro;
    }
}