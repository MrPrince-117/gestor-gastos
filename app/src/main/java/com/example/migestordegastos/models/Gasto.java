package com.example.migestordegastos.models;

public class Gasto {

    private int id;
    private String concepto;
    private double cantidad;
    private String fecha;
    private String descripcion;
    private int idCategoria;
    private int idUsuario;

    public Gasto(int id, String concepto, double cantidad, String fecha, String descripcion, int idCategoria, int idUsuario) {
        this.id = id;
        this.concepto = concepto;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public String getConcepto() {
        return concepto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public String getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
