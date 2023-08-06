package com.luizinho_dev.comprameste.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.luizinho_dev.comprameste.Dao.ComprasDao;
import com.luizinho_dev.comprameste.Dao.ProductosDao;
import com.luizinho_dev.comprameste.Entities.Compras;
import com.luizinho_dev.comprameste.Entities.Productos;

@Database(version = 1, entities = {Productos.class, Compras.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ComprasDao comprasDao();
    public abstract ProductosDao productosDao();

}
