package com.luizinho_dev.comprameste.Logica;

import static com.luizinho_dev.comprameste.Database.AppDatabase.MIGRATION_1_2;
import static com.luizinho_dev.comprameste.Database.AppDatabase.MIGRATION_2_3;

import android.app.AlertDialog;
import android.content.Context;

import androidx.room.Room;

import com.luizinho_dev.comprameste.Activities.HistorialActivity;
import com.luizinho_dev.comprameste.Database.AppDatabase;
import com.luizinho_dev.comprameste.Entities.Compras;

import java.text.MessageFormat;
import java.util.ArrayList;

public class HistorialLogica {

    public ArrayList<Compras> listaCompras = new ArrayList();

    public AppDatabase db;

    public HistorialLogica() {

    }

    public void cargarBD(Context context){
        try {

            db = Room.databaseBuilder(context, AppDatabase.class, "compramesteDB")
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_3)
                    .build();
            System.out.println("Conexion exitosa.");

        } catch (Exception e){
            System.out.println("Error al crear la conexion");
        }
    }

    public void buscarCompras() {
        try {

            listaCompras.clear();
            ArrayList<Compras> compras =  (ArrayList<Compras>) db.comprasDao().findCompras();
            listaCompras.addAll(compras);

            System.out.println("Compras cargadas exitosamente");


        } catch (Exception e) {
            System.out.println("Error al cargar compras: "+ e);
        }
    }

    public void eliminarCompra(Context context, Compras compra){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Borrar Compra...").
                setMessage(MessageFormat.format("¿Está seguro que desea borrar la compra {0}?",compra.getNombre())).
                setCancelable(true).
                setPositiveButton("Sí",(dialog, which) -> {

                    try {

                        System.out.println("Eliminando compra...");
                        //Validamos que haya una conexion, sino creamos una
                        if (db == null) {
                            cargarBD(context);
                        }

                        int deleted = db.comprasDao().deleteCompra(compra);
                        System.out.println(deleted);
                        System.out.println("Compra eliminada exitosamente");

                        // Mandamos a actualizar a adapter del HistorialActivity para que no se quede pegado
                        if (context instanceof HistorialActivity) {
                            //Cargamos las compras para actualizar la lista
                            ((HistorialActivity) context).cargarCompras();
                        }

                    } catch (Exception e){
                        System.err.println("Error al eliminar compra: "+ e);

                    }



                }).
                setNegativeButton("No",(dialog, which) -> {

                    System.out.println("No eliminar compra");
                    dialog.cancel();

                    // Mandamos a actualizar a adapter del HistorialActivity para que no se quede pegado
                    if (context instanceof HistorialActivity) {
                        ((HistorialActivity) context).actualizarRecyclerView();
                    }

                }).show();

    }
}
