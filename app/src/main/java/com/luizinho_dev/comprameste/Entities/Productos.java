package com.luizinho_dev.comprameste.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.text.MessageFormat;

@Entity(tableName = "Productos",
        foreignKeys = @ForeignKey(
        entity = Compras.class,
        parentColumns = "id",
        childColumns = "id_compra",
        onDelete = ForeignKey.CASCADE,
        deferred = true
), indices = {@Index(name = "idx_id_compra", value = "id_compra")})
public class Productos {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public long id;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "cantidad")
    public int cantidad;

    @ColumnInfo(name = "val_uni")
    public double valorUnitario;

    @ColumnInfo(name = "porc_desc", defaultValue = "0")
    public double porcDesc;

    @ColumnInfo(name = "total")
    public double total;

    //No se coloca como foránea porque mucho texto
    @ColumnInfo(name = "id_compra")
    public int idCompra;

    @ColumnInfo(name = "comprado", defaultValue = "0")
    public boolean comprado;

    public Productos() {
    }

    @Ignore
    public Productos(String nombre, int cantidad, double valorUnitario, double total, int idCompra) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
        this.total = total;
        this.idCompra = idCompra;
    }

    @Ignore
    public Productos(String nombre, int cantidad, double valorUnitario, double porcDesc, double total, int idCompra) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
        this.porcDesc = porcDesc;
        this.total = total;
        this.idCompra = idCompra;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public double getPorcDesc() {
        return porcDesc;
    }

    public void setPorcDesc(double porcDesc) {
        this.porcDesc = porcDesc;
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

    public boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }

    @Override
    public String toString(){
        return MessageFormat.format("Id: {4}, Nombre: {0}, Cant: {1}, Valor Uni.: {2}, Desc: {5}, Total: {3}, Comprado: {6}",
                this.nombre, this.cantidad, this.valorUnitario, this.total, this.id, this.porcDesc, this.comprado);
    }
}
