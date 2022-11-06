package com.example.comprameste;

public class Producto {
    public String nombre;
    public int cantidad;
    public double valorUnitario;
    public double total;

    public Producto(String nombre, int cantidad, double valorUnitario, double total) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
        this.total = total;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
