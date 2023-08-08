package com.luizinho_dev.comprameste.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.MessageFormat;

@Entity(tableName = "Compras")
public class Compras {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "fecha")
    public String fecha;

    @ColumnInfo(name = "cant_prod")
    public int cantProductos;

    @ColumnInfo(name = "total")
    public double total;

    @ColumnInfo(name = "nombre")
    public String nombre;

    public Compras() {
    }

    public Compras(int id) {
        this.id = id;
    }

    public Compras(String fecha, String nombre) {
        this.fecha = fecha;
        this.nombre = nombre;
    }

    public Compras(String fecha, int cantProductos, double total, String nombre) {
        this.fecha = fecha;
        this.cantProductos = cantProductos;
        this.total = total;
        this.nombre = nombre;
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

    @Override
    public String toString(){
        return MessageFormat.format("Id: {0}, Nombre: {1}, Cant. Prod.: {2}, Total: {3}",
                this.id, this.nombre, this.cantProductos, this.total);
    }
}
