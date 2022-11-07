package com.example.comprameste;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ScrollView miScrollView;
    EditText txtProducto,txtCantidad, txtValorUni;
    TextView txtTotalFinal, txtTotal;
    int lbId = 0;
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
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtTotalFinal = (TextView) findViewById(R.id.txtTotalFinal);
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
                    if (!txtTotal.getText().toString().equals("$0")){
                        if (lbId==0) agregarProducto();
                            else {
                                int index = buscarProductoById(lbId);
                                if (index != -1) editarProducto(index);
                                    else Toast.makeText(getApplicationContext(),"No se encuentra producto con id "+lbId,Toast.LENGTH_SHORT).show();
                        }

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
                txtTotal.setText("$0");
                listaProductos.clear();
                txtTotalFinal.setText("$0");
                adapter.notifyDataSetChanged();

            }
        });

        lvProductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Producto "+ listaProductos.get(position).getId() + " Eliminado" , Toast.LENGTH_LONG).show();
                eliminarProducto(position);
                adapter.notifyDataSetChanged();
                calcularTotalFinal();
                return true;
            }
        });

        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lbId = listaProductos.get(position).getId();
                txtProducto.setText(listaProductos.get(position).getNombre());
                txtCantidad.setText(String.valueOf(listaProductos.get(position).getCantidad()));
                txtValorUni.setText(String.valueOf(listaProductos.get(position).getValorUnitario()));
                //txtTotal.setText(String.valueOf(listaProductos.get(position).getTotal()));
                txtProducto.requestFocusFromTouch();
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
        /*SwipeListViewTouchListener touchListener =new SwipeListViewTouchListener(lvProductos,new SwipeListViewTouchListener.OnSwipeCallback() {
            @Override
            public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
                //Aqui ponemos lo que hara el programa cuando deslizamos un item ha la izquierda
                eliminarProducto(reverseSortedPositions[0], listView.getChildAt(reverseSortedPositions[0]));
                adapter.notifyDataSetChanged();
                calcularTotalFinal();
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
        lvProductos.setOnScrollListener(touchListener.makeScrollListener());*/

    }

    @Override
    public void onBackPressed() {

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
        txtTotal.setText("$0");
    }

    public double calcularTotalFinal(){
        double total = 0.0;
        for (int i = 0; i< listaProductos.size(); i++){
            total += listaProductos.get(i).getTotal();
        }

        txtTotalFinal.setText("$"+formatea.format(total));

        return total;
    }

    public int buscarProductoById(int id){
        for (int i = 0; i < listaProductos.size(); i++){
            if (listaProductos.get(i).getId() == id) return i;
        }

        return -1;
    }

    public void agregarProducto(){

        listId.add(listId.size()+1);
        Producto newProd = new Producto(listId.get(listId.size()-1),
                txtProducto.getText().toString(),
                Integer.parseInt(txtCantidad.getText().toString()),
                Double.parseDouble(txtValorUni.getText().toString()),
                Double.parseDouble(txtTotal.getText().toString()));
        listaProductos.add(newProd);
    }

    public void editarProducto(int i){
        lbId = 0;
        listaProductos.get(i).setNombre(txtProducto.getText().toString());
        listaProductos.get(i).setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
        listaProductos.get(i).setValorUnitario(Double.parseDouble(txtValorUni.getText().toString()));
        listaProductos.get(i).setTotal(Double.parseDouble(txtTotal.getText().toString()));
        Toast.makeText(getApplicationContext(),"Producto "+listaProductos.get(i).getId()+" editado correctamente.", Toast.LENGTH_SHORT);

    }

    public void eliminarProducto(int position){
        listaProductos.get(position).setCantidad(0);
        listaProductos.get(position).setValorUnitario(0);
        listaProductos.get(position).setTotal(0);
    }



}