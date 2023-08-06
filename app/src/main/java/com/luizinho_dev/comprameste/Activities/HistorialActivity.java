package com.luizinho_dev.comprameste.Activities;

import androidx.appcompat.app.AppCompatActivity;

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
import com.luizinho_dev.comprameste.Logica.HistorialLogica;

import java.util.ArrayList;

public class HistorialActivity extends AppCompatActivity {

    HistorialLogica historialLogica = new HistorialLogica();

    ListView lvCompras;

    CustomAdapterCompras adapter;
    BDTransations conexion = new BDTransations();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        cargarElementos();

        historialLogica.cargarBD(getApplicationContext());

        cargarCompras();

        asignarEventos();

    }

    private void asignarEventos() {

        //#region ListView lvCompras
        lvCompras.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("idCompra",historialLogica.listaCompras.get(position).getId());
                intent.putExtra("nombreCompra",historialLogica.listaCompras.get(position).getNombre());
                startActivity(intent);
                return true;
            }
        });
        //#endregion
    }

    public void cargarCompras(){

        historialLogica.buscarCompras();
        actualizarAdapter();
    }

    public void actualizarAdapter(){
        adapter= new CustomAdapterCompras(getApplicationContext(), historialLogica.listaCompras);
        lvCompras.setAdapter(adapter);
    }

    public void cargarElementos(){
        lvCompras = findViewById(R.id.lvCompras);
    }
}