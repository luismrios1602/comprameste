package com.luizinho_dev.comprameste.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Productos",
        foreignKeys = @ForeignKey(
        entity = Compras.class,
        parentColumns = "id",
        childColumns = "id_compra",
        onDelete = ForeignKey.CASCADE,
        deferred = true
))
public class Productos {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "cantidad")
    public int cantidad;

    @ColumnInfo(name = "val_uni")
    public double valorUnitario;

    @ColumnInfo(name = "total")
    public double total;

    //No se coloca como foránea porque mucho texto
    @ColumnInfo(name = "id_compra")
    public int idCompra;

    public Productos() {
    }

    public Productos(String nombre, int cantidad, double valorUnitario, double total, int idCompra) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
        this.total = total;
        this.idCompra = idCompra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
    }
}