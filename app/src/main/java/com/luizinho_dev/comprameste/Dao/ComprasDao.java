package com.luizinho_dev.comprameste.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.luizinho_dev.comprameste.Entities.Compras;

import java.util.List;

@Dao
public interface ComprasDao {

    @Insert
    long createCompra(Compras compra);

    @Update
    void updateCompra(Compras compra);

    @Query("SELECT * FROM Compras")
    List<Compras> findCompras();

    @Query("SELECT * FROM Compras ORDER BY id DESC LIMIT 1")
    Compras findUltimaCompra();

    @Query("SELECT * FROM Compras WHERE id = :id LIMIT 1")
    Compras findCompraById(long id);


}
