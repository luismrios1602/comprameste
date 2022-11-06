package com.example.comprameste;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ScrollView miScrollView;
    EditText txtProducto,txtCantidad, txtValorUni, txtTotal, txtTotalFinal;
    Button btnAgregar,btnCalcular, btnNuevaCompra;
    ListView lvProductos;

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    ArrayList<Producto> productos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        miScrollView = (ScrollView) findViewById(R.id.miScrollView);

        txtProducto = (EditText) findViewById(R.id.txtProducto);
        txtCantidad = (EditText) findViewById(R.id.txtCantidad);
        txtValorUni = (EditText) findViewById(R.id.txtValorUnitario);
        txtTotal = (EditText) findViewById(R.id.txtTotal);
        txtTotalFinal = (EditText) findViewById(R.id.txtTotalFinal);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnCalcular = (Button) findViewById(R.id.btnCalcular);
        btnNuevaCompra = (Button) findViewById(R.id.btnNuevaCompra);
        lvProductos = (ListView) findViewById(R.id.lvProductos);

        CustomAdapter adapter = new CustomAdapter(getApplicationContext(),productos);
        //ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,product);
        lvProductos.setAdapter(adapter);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validar()){
                    if (!txtTotal.getText().toString().isEmpty()){
                        Producto newProd = new Producto(txtProducto.getText().toString(),
                                Integer.parseInt(txtCantidad.getText().toString()),
                                Double.parseDouble(txtValorUni.getText().toString()),
                                Double.parseDouble(txtTotal.getText().toString()));
                        productos.add(newProd);

                        limpiarCampos();
                        txtTotalFinal.setText("$"+formatea.format(calcularTotalFinal()));

                    } else {
                        Toast.makeText(getApplicationContext(),"Primero de calcular el total" , Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Campos vacíos" , Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }
        });

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validar()){
                    int cant = Integer.parseInt(txtCantidad.getText().toString());
                    Double valUni = Double.parseDouble(txtValorUni.getText().toString());

                    txtTotal.setText(calcularTotalUnd(cant,valUni).toString());
                }
            }
        });

        btnNuevaCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtProducto.setText("");
                txtCantidad.setText("");
                txtValorUni.setText("");
                txtTotal.setText("");
                productos.clear();
                txtTotalFinal.setText("");
                adapter.notifyDataSetChanged();

            }
        });

        miScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.lvProductos).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        lvProductos.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    public boolean validar(){
        try {

            String nombre = txtProducto.getText().toString();
            int cant = Integer.parseInt(txtCantidad.getText().toString());
            Double valUni = Double.parseDouble(txtValorUni.getText().toString());

            if (!nombre.isEmpty() && cant > 0 && valUni > 0) return true;
                else return false;


        } catch (Exception e){
            return false;
        }

    }

    public Double calcularTotalUnd(int cant, Double valorUni){
        return cant * valorUni;
    }

    public void limpiarCampos(){
        txtProducto.setText("");
        txtCantidad.setText("");
        txtValorUni.setText("");
        txtTotal.setText("");
    }

    public Double calcularTotalFinal(){
        Double total = 0.0;
        for (int i=0; i<productos.size(); i++){
            total += productos.get(i).getTotal();
        }

        return total;
    }


}