package com.luizinho_dev.comprameste.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Productos")
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
}
