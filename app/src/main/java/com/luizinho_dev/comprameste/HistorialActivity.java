package com.luizinho_dev.comprameste;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.comprameste.R;

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

        lvCompras.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("idCompra",listaCompras.get(position).getId());
                intent.putExtra("nombreCompra",listaCompras.get(position).getNombre());
                startActivity(intent);
                return true;
            }
        });

    }
    
    public void buscarCompras(Context context){

        listaCompras = conexion.buscarCompras(context);
        adapter= new CustomAdapterCompras(getApplicationContext(), listaCompras);
        lvCompras.setAdapter(adapter);
    }
}