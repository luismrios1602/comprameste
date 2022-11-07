package com.example.comprameste;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ScrollView miScrollView;
    EditText txtProducto,txtCantidad, txtValorUni, txtTotal, txtTotalFinal;
    Button btnAgregar,btnCalcular, btnNuevaCompra;
    ListView lvProductos;

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    ArrayList<Producto> listaProductos = new ArrayList<>();
    ArrayList<Integer> listId = new ArrayList<>();


    @SuppressLint("ClickableViewAccessibility")
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

        CustomAdapter adapter = new CustomAdapter(this, listaProductos);
        lvProductos.setAdapter(adapter);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validar()){
                    if (!txtTotal.getText().toString().isEmpty()){
                        listId.add(listId.size()+1);
                        Producto newProd = new Producto(listId.get(listId.size()-1),
                                txtProducto.getText().toString(),
                                Integer.parseInt(txtCantidad.getText().toString()),
                                Double.parseDouble(txtValorUni.getText().toString()),
                                Double.parseDouble(txtTotal.getText().toString()));
                        listaProductos.add(newProd);

                        limpiarCampos();
                        calcularTotalFinal();

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
                listaProductos.clear();
                txtTotalFinal.setText("");
                adapter.notifyDataSetChanged();

            }
        });

        lvProductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Producto "+ listaProductos.get(position).getId() + " Eliminado" , Toast.LENGTH_LONG).show();
                eliminarProducto(position, view);
                adapter.notifyDataSetChanged();
                calcularTotalFinal();
                return true;
            }
        });

        //Métodos para realizar scroll del listview dentro del scroll original
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

        //Deslizar item para borrarlo
        SwipeListViewTouchListener touchListener =new SwipeListViewTouchListener(lvProductos,new SwipeListViewTouchListener.OnSwipeCallback() {
            @Override
            public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
                /*//Aqui ponemos lo que hara el programa cuando deslizamos un item ha la izquierda
                eliminarProducto(reverseSortedPositions[0], listView);
                adapter.notifyDataSetChanged();
                calcularTotalFinal();*/
            }

            @Override
            public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
                //Aqui ponemos lo que hara el programa cuando deslizamos un item ha la derecha
                eliminarProducto(reverseSortedPositions[0], listView.getChildAt(reverseSortedPositions[0]));
               adapter.notifyDataSetChanged();
               calcularTotalFinal();

            }
        },true, false);

        //Escuchadores del listView
        lvProductos.setOnTouchListener(touchListener);
        lvProductos.setOnScrollListener(touchListener.makeScrollListener());

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

    public double calcularTotalFinal(){
        double total = 0.0;
        for (int i = 0; i< listaProductos.size(); i++){
            total += listaProductos.get(i).getTotal();
        }

        txtTotalFinal.setText("$"+formatea.format(total));

        return total;
    }

    public void eliminarProducto(int position, View view){
        view.setBackgroundColor(Color.RED);
        listaProductos.get(position).setCantidad(0);
        listaProductos.get(position).setValorUnitario(0);
        listaProductos.get(position).setTotal(0);
    }


}