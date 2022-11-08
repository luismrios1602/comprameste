package com.example.comprameste;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final  String DATABASE_NAME = "DBComprameste";

    //Constructor para crear la base de datos
    public AdminSQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Compras(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fecha String NOT NULL)");

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

    }
}
