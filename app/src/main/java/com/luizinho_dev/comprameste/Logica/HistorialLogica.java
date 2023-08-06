package com.luizinho_dev.comprameste.Logica;

import android.content.Context;

import androidx.room.Room;

import com.luizinho_dev.comprameste.Database.AppDatabase;
import com.luizinho_dev.comprameste.Entities.Compras;

import java.util.ArrayList;

public class HistorialLogica {

    public ArrayList<Compras> listaCompras = new ArrayList();

    public AppDatabase db;

    public HistorialLogica() {

    }

    public void cargarBD(Context context){
        try {

            db = Room.databaseBuilder(context, AppDatabase.class, "compramesteDB").allowMainThreadQueries().build();
            System.out.println("Conexion creada.");

        } catch (Exception e){
            System.out.println("Error al crear la conexion");
        }
    }

    public void buscarCompras() {
        try {

            listaCompras = (ArrayList<Compras>) db.comprasDao().findCompras();

        } catch (Exception e) {
            System.out.println("Error al cargar compras: "+ e);
        }
    }
}
