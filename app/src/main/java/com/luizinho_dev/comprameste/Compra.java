package com.luizinho_dev.comprameste;

import java.io.Serializable;

public class Compra implements Serializable {
    public int id;
    public String fecha;
    public int cantProductos;
    public double total;
    private String nombre;

    public Compra() {

    }

    public Compra(int id, String fecha, int cantProductos, double total) {
        this.id = id;
        this.fecha = fecha;
        this.cantProductos = cantProductos;
        this.total = total;
    }

    public Compra(int id, String fecha, int cantProductos, double total, String nombre) {
        this.id = id;
        this.fecha = fecha;
        this.cantProductos = cantProductos;
        this.total = total;
        this.nombre = nombre;
    }

    public Compra(int id, String fecha) {
        this.id = id;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getCantProductos() {
        return cantProductos;
    }

    public void setCantProductos(int cantProductos) {
        this.cantProductos = cantProductos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
