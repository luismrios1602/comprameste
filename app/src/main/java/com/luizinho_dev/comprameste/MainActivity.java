package com.luizinho_dev.comprameste;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comprameste.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ScrollView miScrollView;
    EditText txtProducto,txtCantidad, txtValorUni, txtNombreCompra;
    TextView txtTotalFinal, txtTotal;
    Button btnAgregar,btnCalcular, btnCancelar, btnNuevaCompra, btnHistorial, btnDuplicarCompra;
    ListView lvProductos;

    int lbId = 0;
    int idCompraGlobal = 0;
    double totalFinal = 0.0;

    long exitTime = System.currentTimeMillis();

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    ArrayList<Producto> listaProductos = new ArrayList<>();
    ArrayList<Integer> listId = new ArrayList<>();
    CustomAdapterProductos adapter;
    BDTransations conexion = new BDTransations();

    Bundle bundle;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        miScrollView = (ScrollView) findViewById(R.id.miScrollView);

        txtNombreCompra = (EditText) findViewById(R.id.txtNombreCompra);
        txtProducto = (EditText) findViewById(R.id.txtProducto);
        txtCantidad = (EditText) findViewById(R.id.txtCantidad);
        txtCantidad.setText("");
        txtValorUni = (EditText) findViewById(R.id.txtValorUnitario);
        txtValorUni.setText("");
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtTotalFinal = (TextView) findViewById(R.id.txtTotalFinal);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
//        btnCalcular = (Button) findViewById(R.id.btnCalcular);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnNuevaCompra = (Button) findViewById(R.id.btnNuevaCompra);
        btnHistorial = (Button) findViewById(R.id.btnHistorial);
        btnDuplicarCompra = (Button) findViewById(R.id.btnDuplicarCompra);
        lvProductos = (ListView) findViewById(R.id.lvProductos);

        adapter= new CustomAdapterProductos(getApplicationContext(), listaProductos);
        lvProductos.setAdapter(adapter);

        //Llamamos el bundle por si venimos desde la actividad Historial
        bundle = getIntent().getExtras();
        System.out.println(bundle);
        //Si no venimos desde allí (O se le da atrás) el bundle viene null
        if(bundle != null){
            System.out.println("idCompra seleccionada: "+bundle.getInt("idCompra"));
            txtNombreCompra.setText(bundle.getString("nombreCompra"));
            idCompraGlobal = (bundle.getInt("idCompra")!=0)?bundle.getInt("idCompra"):0;
            btnDuplicarCompra.setEnabled(true);

        } else {
            btnDuplicarCompra.setEnabled(false);
        }

        //Si no hay id de compra es porque apenas estoy entrando a la app así que cargamos la última compra
        if(idCompraGlobal == 0){
            cargarUltimaCompra(getApplicationContext());
        } else {
            buscarProductosByCompra(getApplicationContext(), idCompraGlobal);
        }

        txtNombreCompra.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(idCompraGlobal != 0){
                    String nombreCompra = txtNombreCompra.getText().toString();
                    System.out.println("Actualizando compra...");
                    conexion.actualizarCompra(getApplicationContext(), idCompraGlobal,listaProductos.size(),totalFinal, nombreCompra);
                }
            }
        });
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validar()){
                    //Si los valores están bien, agregamos el nombre de la nombre
                    String nombreCompra = txtNombreCompra.getText().toString();

                    //Revisamos que el id del producto actual sea diferente de 0 para saber si agregamos o editamos
                    if (lbId==0) {
                        Producto newProd = new Producto(txtProducto.getText().toString(),
                                Integer.parseInt(txtCantidad.getText().toString()),
                                Double.parseDouble(txtValorUni.getText().toString()),
                                Double.parseDouble(txtTotal.getText().toString()),
                                idCompraGlobal);

                        Producto prodCreado = conexion.agregarProducto(getApplicationContext(),newProd,nombreCompra);

                        if (prodCreado != null){

                            idCompraGlobal = prodCreado.getIdCompra();
                            listaProductos = conexion.buscarProductosByCompra(getApplicationContext(),prodCreado.getIdCompra());
                            adapter= new CustomAdapterProductos(getApplicationContext(), listaProductos);
                            lvProductos.setAdapter(adapter);
                            lbId = 0;
                            Toast.makeText(getApplicationContext(), "Producto creado exitosamente.", Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(getApplicationContext(), "ERROR: No se pudo crear el Producto.", Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        //Si tenemos un id de producto, entonces lo editamos
                        Producto prodEdit = conexion.buscarProductoById(getApplicationContext(), lbId);
                        if (prodEdit != null) {

                            prodEdit.setNombre(txtProducto.getText().toString());
                            prodEdit.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                            prodEdit.setValorUnitario(Double.parseDouble(txtValorUni.getText().toString()));
                            prodEdit.setTotal(Double.parseDouble(txtTotal.getText().toString()));

                            prodEdit = conexion.editarProducto(getApplicationContext(), prodEdit);
                            if (prodEdit != null){

                                listaProductos = conexion.buscarProductosByCompra(getApplicationContext(),prodEdit.getIdCompra());
                                adapter= new CustomAdapterProductos(getApplicationContext(), listaProductos);
                                lvProductos.setAdapter(adapter);
                                lbId = 0;
                                Toast.makeText(getApplicationContext(), "Producto editado exitosamente.",Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "ERROR: No se pudo editar el Producto", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(),"No se encuentra producto con id "+prodEdit.getId(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    limpiarCampos();
                    calcularTotalFinal();
                    //Al final actualizamos los datos de la compra
                    conexion.actualizarCompra(getApplicationContext(), idCompraGlobal,listaProductos.size(),totalFinal, nombreCompra);
                } else {
                    Toast.makeText(getApplicationContext(),"Campos vacíos" , Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }
        });

        /*btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validar()){
                    int cant = Integer.parseInt(txtCantidad.getText().toString());
                    Double valUni = Double.parseDouble(txtValorUni.getText().toString());

                    txtTotal.setText(calcularTotalUnd(cant,valUni).toString());
                }
            }
        });*/

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCampos();
            }
        });

        btnNuevaCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtProducto.setText("");
                txtCantidad.setText("");
                txtValorUni.setText("");
                txtTotal.setText("0");
                txtTotalFinal.setText("$0");
                txtNombreCompra.setText("");
                btnDuplicarCompra.setEnabled(false);
                idCompraGlobal = 0;
                listaProductos.clear();
                adapter.notifyDataSetChanged();

            }
        });

        lvProductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Producto prodElim = conexion.buscarProductoById(getApplicationContext(), listaProductos.get(position).getId());
                String nombreCompra = txtNombreCompra.getText().toString();

                if (prodElim != null) {

                    prodElim = conexion.eliminarProducto(getApplicationContext(), prodElim);

                    if (prodElim != null) {
                        listaProductos = conexion.buscarProductosByCompra(getApplicationContext(),prodElim.getIdCompra());
                        adapter= new CustomAdapterProductos(getApplicationContext(), listaProductos);
                        lvProductos.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(), "Producto eliminado exitosamente.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"ERROR: No se pudo eliminar el registro",Toast.LENGTH_LONG).show();
                    }
                }

                calcularTotalFinal();
                conexion.actualizarCompra(getApplicationContext(), idCompraGlobal,listaProductos.size(),totalFinal, nombreCompra);
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

        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
                startActivity(intent);
            }
        });

        btnDuplicarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaProductos.size() > 0){
                    String nombreCompra = txtNombreCompra.getText().toString() + " duplicado";
                    String nombre;
                    int cantidad;
                    double valorUnitario;
                    double total;
                    int idCompra = 0;

                    for(int i = 0; i < listaProductos.size(); i ++){
                        nombre = listaProductos.get(i).getNombre();
                        cantidad = listaProductos.get(i).getCantidad();
                        valorUnitario = listaProductos.get(i).getValorUnitario();
                        total = listaProductos.get(i).getTotal();

                        Producto newProd = new Producto(nombre,cantidad,valorUnitario,total);
                        //Al primer producto le creamos la nueva compra a duplicar con todo y nombre (Le colocamos un copia al lado)
                        if(i==0){
                            newProd = conexion.agregarProducto(getApplicationContext(),newProd,nombreCompra);
                            idCompra = newProd.getIdCompra();
                        } else {
                            newProd.setIdCompra(idCompra);
                            newProd = conexion.agregarProducto(getApplicationContext(),newProd,nombreCompra);
                        }
                    }

                    idCompraGlobal = idCompra;
                    buscarProductosByCompra(getApplicationContext(), idCompraGlobal);
                    conexion.actualizarCompra(getApplicationContext(),idCompraGlobal,listaProductos.size(),totalFinal, nombreCompra);

                    Toast.makeText(getApplicationContext(),"Compra duplicada exitosamente." , Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getApplicationContext(),"Sin productos a duplicar." , Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Este método se llama antes de que el texto cambie.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Este método se llama cuando el texto cambia.
                // Validamos que los valores no estén nulos
                if(validarValores()){
                    int cant = Integer.parseInt(txtCantidad.getText().toString());
                    Double valUni = Double.parseDouble(txtValorUni.getText().toString());

                    txtTotal.setText(calcularTotalUnd(cant,valUni).toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Este método se llama después de que el texto cambie.
            }
        });

        txtValorUni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Este método se llama antes de que el texto cambie.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Este método se llama cuando el texto cambia.
                // Validamos que los valores no estén nulos
                if(validarValores()){
                    int cant = Integer.parseInt(txtCantidad.getText().toString());
                    Double valUni = Double.parseDouble(txtValorUni.getText().toString());

                    txtTotal.setText(calcularTotalUnd(cant,valUni).toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Este método se llama después de que el texto cambie.
            }
        });

    }

    //Método para evitar que le den clic al botón de ir a atrás
    @Override
    public void onBackPressed() {
        /*//Antes de salir validar si han pasado hasta 5 segundos antes de cerrar de nuevo la app
        if ((this.exitTime + 5000) < System.currentTimeMillis()){
            Toast.makeText(getApplicationContext(),"Presionar nuevamente para salir." , Toast.LENGTH_SHORT).show();
            //Si no han pasado 5 segundos, entonces le asignamos el valor actual para que espere 5 segundos de cuando dio clic
            this.exitTime = System.currentTimeMillis();
        } else {
            finish();
        }*/
    }

    public boolean validar(){
        try {
            //Validamos primero el nombre y así nos ahorramos validar el resto si el nombre está vacío
            String nombre = txtProducto.getText().toString();
            if (nombre.isEmpty()) return false;

            //Validamos si la cantidad está vacía y la seteamos por defecto a 1 (Para evitar escribirla)
            String cantString = txtCantidad.getText().toString();
            if (cantString.equals("")) txtCantidad.setText("1");

            //Validamos si el valor unitario está vacío y lo seteamos por defecto a 0 (Para evitar escribirla)
            String valUniString = txtValorUni.getText().toString();
            if (valUniString.equals("")) txtValorUni.setText("0");

            int cant = Integer.parseInt(txtCantidad.getText().toString());
            Double valUni = Double.parseDouble(txtValorUni.getText().toString());

            if (!nombre.isEmpty() && cant >= 0 && valUni >= 0) return true;
                else return false;

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    /** @description Método para validar los valores de cantidad y valor unitario antes de calcular total unitario */
    public boolean validarValores(){
        try {

            int cant = Integer.parseInt(txtCantidad.getText().toString());
            System.err.println("cantiadd: |"+cant+"|");
            Double valUni = Double.parseDouble(txtValorUni.getText().toString());

            if (cant >= 0 && valUni >= 0) return true;
            else return false;


        } catch (Exception e){
            return false;
        }
    }

    public Double calcularTotalUnd(int cant, Double valorUni){
        return cant * valorUni;
    }

    public void limpiarCampos(){
        lbId = 0;
        txtProducto.setText("");
        txtCantidad.setText("");
        txtValorUni.setText("");
        txtTotal.setText("0");
    }

    public double calcularTotalFinal(){
        double total = 0.0;
        for (int i = 0; i< listaProductos.size(); i++){
            total += listaProductos.get(i).getTotal();
        }

        totalFinal = total;
        txtTotalFinal.setText("$"+formatea.format(total));

        return total;
    }

    public void cargarUltimaCompra(Context context){
        String nombreCompra;
        List<Compra> listaUltCompras;

        listaProductos = conexion.buscarProductosUltimaCompra(context);
        if (listaProductos.size() > 0){
            idCompraGlobal = listaProductos.get(0).getIdCompra();
            listaUltCompras = conexion.buscarCompraById(context,idCompraGlobal);
            System.out.println("idCompra "+ idCompraGlobal);

        } else {
            idCompraGlobal = 0;
            listaUltCompras = new ArrayList<>();
        }
        adapter= new CustomAdapterProductos(getApplicationContext(), listaProductos);
        lvProductos.setAdapter(adapter);
        calcularTotalFinal();

        //Asignar nombre de la compra
        if (listaUltCompras.size() > 0){
            txtNombreCompra.setText(listaUltCompras.get(0).getNombre());
        }
    }

    public void buscarProductosByCompra(Context context, int idCompra){
        listaProductos = conexion.buscarProductosByCompra(context, idCompra);
        if (listaProductos.size() > 0){
            idCompra = listaProductos.get(0).getIdCompra();
            System.out.println("idCompra "+ idCompra);
        } else {
            idCompra = 0;
        }

        adapter= new CustomAdapterProductos(getApplicationContext(), listaProductos);
        lvProductos.setAdapter(adapter);
        calcularTotalFinal();
    }

}