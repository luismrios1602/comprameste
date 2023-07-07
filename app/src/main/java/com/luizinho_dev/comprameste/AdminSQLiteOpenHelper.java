package com.luizinho_dev.comprameste;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "DBComprameste";

    //Constructor para crear la base de datos
    public AdminSQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Compras(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT DEFAULT NULL, " +
                "fecha String NOT NULL, " +
                "cant_prod INTEGER, " +
                "total DOUBLE)");

        db.execSQL("CREATE TABLE Productos(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL," +
                "cantidad INTEGER NOT NULL," +
                "valor_unitario REAL NOT NULL," +
                "valor_total REAL NOT NULL," +
                "id_compra INTEGER NOT NULL," +
                "FOREIGN KEY(id_compra) REFERENCES Compras(id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion > 2){

            if (oldVersion > 3){
                //Si la versiones la 4 o más es porque ya está actualizada
            } else {
                //Si es mayor que 2 pero no mayor que 3, actualizamos todo lo que haya atrasado
                db.execSQL("ALTER TABLE Compras ADD COLUMN nombre TEXT DEFAULT NULL");
            }
        } else {

            //Si la versión de la BD del cliente es 2 o menor, que cree la BD de nuevo
            db.execSQL("DROP TABLE IF EXISTS Productos");
            db.execSQL("DROP TABLE IF EXISTS Compras");
            onCreate(db);
        }

    }


}
