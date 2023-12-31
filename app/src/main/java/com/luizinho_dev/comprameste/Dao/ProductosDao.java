package com.luizinho_dev.comprameste.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.luizinho_dev.comprameste.Entities.Productos;

import java.util.List;

@Dao
public interface ProductosDao {

    @Insert
    long createProducto(Productos producto);

    @Update
    void updateProducto(Productos producto);

    //Permite eliminar varios productos
    @Delete
    int deleteProducto(Productos...productos);

    @Query("SELECT * FROM Productos WHERE id_compra = :idCompra")
    List<Productos> findProductosByIdCompra(int idCompra);

    @Query("SELECT * FROM Productos WHERE id = :id LIMIT 1")
    Productos findProductoById(long id);


}
