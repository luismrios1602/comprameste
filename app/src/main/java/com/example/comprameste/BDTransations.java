package com.example.comprameste;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BDTransations {

    SimpleDateFormat formatterFecha = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

    public BDTransations() {
    }

    public Producto agregarProducto(Context context, Producto newProd){

        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();
        int id;

        try {

            if(newProd.getIdCompra() > 0){

                ContentValues registro = new ContentValues();
                registro.put("nombre",newProd.getNombre());
                registro.put("cantidad",newProd.getCantidad());
                registro.put("valor_unitario",newProd.getValorUnitario());
                registro.put("valor_total",newProd.getTotal());
                registro.put("id_compra",newProd.getIdCompra());

                newProd.setId((int) db.insert("productos",null,registro));

            } else {

                ContentValues compra = new ContentValues();
                Date fecha = new Date();
                String fechaFormat = formatterFecha.format(fecha);
                compra.put("fecha",fechaFormat);
                //compra.put("cant_prod",cantProd);
                //compra.put("total",totalFinal);

                int idCompra = (int) db.insert("compras",null,compra);

                newProd.setIdCompra(idCompra);

                ContentValues registro = new ContentValues();
                registro.put("nombre",newProd.getNombre());
                registro.put("cantidad",newProd.getCantidad());
                registro.put("valor_unitario",newProd.getValorUnitario());
                registro.put("valor_total",newProd.getTotal());
                registro.put("id_compra",idCompra);

                newProd.setId((int) db.insert("productos",null,registro));
            }

            db.close();
            return newProd;

        } catch (Exception e){
            System.out.println(e.getMessage());
            db.close();
            return null;

        }


    }

    public Producto editarProducto(Context context, Producto prodEdit){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();

        try {
            ContentValues registro = new ContentValues();
            registro.put("nombre",prodEdit.getNombre());
            registro.put("cantidad",prodEdit.getCantidad());
            registro.put("valor_unitario",prodEdit.getValorUnitario());
            registro.put("valor_total",prodEdit.getTotal());
            registro.put("id_compra",prodEdit.getIdCompra());

            int filas = db.update("productos",registro,"id ="+prodEdit.getId(),null);

            System.out.println("Producto editado exitosamente.");
            db.close();

            return prodEdit;

        } catch (Exception e){
            System.out.println(e.getMessage());
            db.close();
            return null;
        }
    }

    public Producto eliminarProducto(Context context, Producto prodElim){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();

        try {
            ContentValues registro = new ContentValues();
            registro.put("nombre",prodElim.getNombre());
            registro.put("cantidad",0);
            registro.put("valor_unitario",0);
            registro.put("valor_total",0);
            registro.put("id_compra",prodElim.getIdCompra());

            int filas = db.update("productos",registro,"id ="+prodElim.getId(),null);

            System.out.println("Producto editado exitosamente.");
            db.close();

            return prodElim;

        } catch (Exception e){
            System.out.println(e.getMessage());
            db.close();
            return null;
        }
    }

    public ArrayList<Producto> buscarProductosByCompra(Context context, int idCompra){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();

        Producto prod = null;
        ArrayList<Producto> listaProductos = new ArrayList<>();

        try {

            Cursor registro = db.rawQuery("SELECT * FROM Productos WHERE id_compra = "+idCompra, null);
            if (registro.moveToFirst()){
                do{
                    int id = registro.getInt(0);
                    String nombre = registro.getString(1);
                    int cantidad = registro.getInt(2);
                    double valor_unitario = registro.getDouble(3);
                    double valor_total = registro.getDouble(4);
                    int id_compra = registro.getInt(5);

                    prod = new Producto(id, nombre, cantidad, valor_unitario,valor_total,id_compra);
                    listaProductos.add(prod);
                }while(registro.moveToNext());
            }

            registro.close();
            return listaProductos;

        } catch (Exception e){
            System.out.println(e.getMessage());
            return new ArrayList<Producto>();
        }

    }

    public Producto buscarProductoById(Context context, int id){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();

        Producto prod = null;
        try {

            Cursor registro = db.rawQuery("SELECT * FROM Productos WHERE id = "+id, null);
            if (registro.moveToFirst()){
                int idProd = registro.getInt(0);
                String nombre = registro.getString(1);
                int cantidad = registro.getInt(2);
                double valor_unitario = registro.getDouble(3);
                double valor_total = registro.getDouble(4);
                int id_compra = registro.getInt(5);

                prod = new Producto(id, nombre, cantidad, valor_unitario,valor_total,id_compra);
                return prod;

            } else {

                return prod;
            }

        } catch (Exception e){
            System.out.println(e.getMessage());
            return prod;
        }
    }

    public ArrayList<Producto> buscarProductosUltimaCompra(Context context){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();

        Producto prod = null;
        ArrayList<Producto> listaProductos = new ArrayList<>();

        try {

            int idUltimaCompra = buscarUltimaCompra(context);

            Cursor registro = db.rawQuery("SELECT * FROM Productos WHERE id_compra ="+idUltimaCompra, null);
            if (registro.moveToFirst()){
                do{
                    int id = registro.getInt(0);
                    String nombre = registro.getString(1);
                    int cantidad = registro.getInt(2);
                    double valor_unitario = registro.getDouble(3);
                    double valor_total = registro.getDouble(4);
                    int id_compra = registro.getInt(5);

                    prod = new Producto(id, nombre, cantidad, valor_unitario,valor_total,id_compra);
                    listaProductos.add(prod);
                }while(registro.moveToNext());
            }

            registro.close();
            return listaProductos;

        } catch (Exception e){
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }

    }

    private int buscarUltimaCompra(Context context){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();

        Cursor registro = db.rawQuery("SELECT * FROM Compras ORDER BY id DESC", null);
        if (registro.moveToFirst()){
            return registro.getInt(0);
        } else {
            return 0;
        }
    }

    public ArrayList<Compra> buscarCompras(Context context){
        System.out.println("Consultando compras");
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context);
        SQLiteDatabase db = admin.getWritableDatabase();

        Compra compra = null;
        ArrayList<Compra> listaCompras = new ArrayList<>();

        try {

            Cursor registro = db.rawQuery("SELECT * FROM Compras", null);
            if (registro.moveToFirst()){
                do{
                    int id = registro.getInt(0);
                    String fecha = registro.getString(1);
                    int cant_prod = registro.getInt(2);
                    double valor_total = registro.getDouble(3);

                    compra = new Compra(id, fecha, cant_prod, valor_total);
                    listaCompras.add(compra);
                }while(registro.moveToNext());
            }

            registro.close();
            return listaCompras;

        } catch (Exception e){
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }

    }

    public void actualizarCompra(Context context, int idCompra, int cantProd, double total){

        try {
            AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(context);
            SQLiteDatabase db = admin.getWritableDatabase();

            db.execSQL("UPDATE Compras SET cant_prod = "+cantProd+", total = "+total+" WHERE id ="+idCompra);
            db.close();
        } catch (Exception e){
            System.out.println(e);
        }


    }

}
