package com.example.comprameste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class HistorialActivity extends AppCompatActivity {

    ListView lvCompras;

    ArrayList<Compra> listaCompras = new ArrayList();
    CustomAdapterCompras adapter;
    BDTransations conexion = new BDTransations();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        lvCompras = (ListView) findViewById(R.id.lvCompras);
        adapter= new CustomAdapterCompras(getApplicationContext(), listaCompras);
        lvCompras.setAdapter(adapter);

        buscarCompras(getApplicationContext());


    }

    public void buscarCompras(Context context){

        listaCompras = conexion.buscarCompras(context);
        adapter= new CustomAdapterCompras(getApplicationContext(), listaCompras);
        lvCompras.setAdapter(adapter);
    }
}