package com.luizinho_dev.comprameste.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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
}
