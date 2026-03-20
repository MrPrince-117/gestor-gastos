package com.example.migestordegastos.models;

public class Categoria {

    private int id;
    private String nombre;
    private String icono;

    public Categoria(int id, String nombre, String icono) {
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIcono() {
        return icono;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}
