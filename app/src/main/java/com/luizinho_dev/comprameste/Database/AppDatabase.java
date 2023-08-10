package com.luizinho_dev.comprameste.Database;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.luizinho_dev.comprameste.Dao.ComprasDao;
import com.luizinho_dev.comprameste.Dao.ProductosDao;
import com.luizinho_dev.comprameste.Entities.Compras;
import com.luizinho_dev.comprameste.Entities.Productos;

@Database(version = 3, entities = {Productos.class, Compras.class}, exportSchema = true, autoMigrations = {
//        @AutoMigration(from = 1, to = 2),
})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ComprasDao comprasDao();
    public abstract ProductosDao productosDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Productos ADD COLUMN porc_desc REAL DEFAULT 0 NOT NULL");
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Productos ADD COLUMN comprado INTEGER DEFAULT 0 NOT NULL");
        }
    };

}


