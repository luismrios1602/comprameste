package com.luizinho_dev.comprameste.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.comprameste.R;
import com.luizinho_dev.comprameste.BDTransations;
import com.luizinho_dev.comprameste.Compra;
import com.luizinho_dev.comprameste.CustomAdapters.CustomAdapterCompras;
import com.luizinho_dev.comprameste.CustomAdapters.RVAdapterCompras;
import com.luizinho_dev.comprameste.CustomAdapters.RVAdapterProductos;
import com.luizinho_dev.comprameste.Logica.HistorialLogica;

import java.util.ArrayList;

public class HistorialActivity extends AppCompatActivity {

    HistorialLogica historialLogica = new HistorialLogica();

    RecyclerView rvCompras;

    RVAdapterCompras rvadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        cargarElementos();

        asignarEventos();

        historialLogica.cargarBD(getApplicationContext());

        cargarCompras();


    }

    public void cargarElementos(){
        rvCompras = findViewById(R.id.rvCompras);

    }

    private void asignarEventos() {

    }

    public void actualizarRecyclerView(){
        rvCompras.setLayoutManager(new LinearLayoutManager(this));
        rvadapter = new RVAdapterCompras(getApplicationContext(),historialLogica.listaCompras);
        rvCompras.setAdapter(rvadapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(rvadapter.itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(rvCompras);

    }

    public void cargarCompras(){

        historialLogica.buscarCompras();
        actualizarRecyclerView();
    }



}