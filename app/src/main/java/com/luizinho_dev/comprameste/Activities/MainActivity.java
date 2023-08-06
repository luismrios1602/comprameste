package com.luizinho_dev.comprameste.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import com.luizinho_dev.comprameste.BDTransations;
import com.luizinho_dev.comprameste.CustomAdapters.CustomAdapterProductos;
import com.luizinho_dev.comprameste.Entities.Compras;
import com.luizinho_dev.comprameste.Logica.MainLogica;
import com.luizinho_dev.comprameste.Producto;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ScrollView miScrollView;
    EditText txtProducto,txtCantidad, txtValorUni, txtNombreCompra;
    TextView txtTotalFinal, txtTotal;
    Button btnAgregar, btnCancelar, btnNuevaCompra, btnHistorial, btnDuplicarCompra;
    ListView lvProductos;

    int lbId = 0;
    int idCompraGlobal = 0;
    double totalFinal = 0.0;

    long exitTime = System.currentTimeMillis();

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    ArrayList<Producto> listaProductos = new ArrayList<>();
    CustomAdapterProductos adapter;
    BDTransations conexion = new BDTransations();

    Bundle bundle;
    MainLogica mainLogica = new MainLogica();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cargamos la base de datos desde ROOM (En la logica)
        mainLogica.cargarBD(getApplicationContext());
        mainLogica.cargarUltimaCompra();

        //Cargamos los elementos de la vista al "back"
        cargarElementos();

        //Usamos el objeto de Logica para usar su lista de productos y cargar el listview a la primera
        adapter= new CustomAdapterProductos(getApplicationContext(), mainLogica.listaProductos);
        lvProductos.setAdapter(adapter);

        //Llamamos el bundle por si venimos desde la actividad Historial
        bundle = getIntent().getExtras();
        System.out.println(bundle);

        //Cargamos la compra actual, que si se viene desde el historial será la seleccionada, sino será la ultima
        mainLogica.cargarCompraActual(bundle);

        //Verificamos sin el id de la compra actual no es 0, para así habilitar el duplicar compra
        boolean habilitar = (mainLogica.compraActu.getId() != 0);
        btnDuplicarCompra.setEnabled(habilitar);

        //Cargamos el txt del nombre de la compra con la compra cargada
        txtNombreCompra.setText(mainLogica.compraActu.getNombre());

        //Asignamos los eventos a los elemtos de la vista
        asignarEventos();

    }

    /**
     * @description Método para evitar que le den clic al botón de ir a atrás
     */
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

    /**
     * @description Método para validar que los datos en el formulario de producto sean correctos para poder guardar
     * @return true / false
     */
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
            double valUni = Double.parseDouble(txtValorUni.getText().toString());

            return !nombre.isEmpty() && cant >= 0 && valUni >= 0; //Si no cumple con las validaciones, directamente retornamos false y si sí, pues true :v

        } catch (Exception e) {
            System.out.println(""+e);
            return false;
        }

    }

    /** @description Método para validar los valores de cantidad y valor unitario antes de calcular total unitario */
    public boolean validarValores(){
        try {

            int cant = Integer.parseInt(txtCantidad.getText().toString());
            System.err.println("cantiadd: |"+cant+"|");
            double valUni = Double.parseDouble(txtValorUni.getText().toString());

            return cant >= 0 && valUni >= 0; //Si los valores no son mayores a 0 que retorne false


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

    @SuppressLint("SetTextI18n")
    public void calcularTotalFinal(){
        //Actualizamos la lista de productos del front
        mainLogica.cargarProductosByCompra(idCompraGlobal);

        double total = 0.0;
        for (int i = 0; i< mainLogica.listaProductos.size(); i++){
            total += mainLogica.listaProductos.get(i).getTotal();
        }

        totalFinal = total;
        txtTotalFinal.setText("$"+formatea.format(total));

    }



    /**
     * @description Método para cargar todos los elementos del XML al java para manipularlos
     */
    private void cargarElementos(){

        //Llamamos todos los objetos del front que vamos a utilizar
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
    }

    /**
     * @description Método para asignar todos los eventos de cada uno de los elementos de la vista
     */
    @SuppressLint("ClickableViewAccessibility")
    private void asignarEventos(){

        //region Button btnAgregar
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validar()){

                    //Revisamos que el id del producto actual sea diferente de 0 para saber si agregamos o editamos
                    if (lbId==0) {
                        crearProducto();
                    } else {
                        actualizarProducto(lbId);
                    }

                    limpiarCampos();
                    calcularTotalFinal();
                } else {
                    Toast.makeText(getApplicationContext(),"Campos vacíos" , Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
            }
        });
        //endregion

        //region Button btnCancelar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limpiarCampos();
            }
        });
        //endregion

        //region Button btnNuevaCompra
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

        //endregion

        //region Button btnHistorial
        btnHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
                startActivity(intent);
            }
        });
        //endregion

        //region Button btnDuplicarCompra
        btnDuplicarCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainLogica.listaProductos.size() > 0){
                    String nombreCompra = txtNombreCompra.getText().toString() + " duplicado";
                    String nombre;
                    int cantidad;
                    double valorUnitario;
                    double total;
                    int idCompra = 0;

                    //Creamos la nueva compra para asignarla a los productos a crear
                    Compras nuevaCompra = mainLogica.crearCompra(mainLogica.listaProductos.size(), totalFinal, nombreCompra);

                    if (nuevaCompra != null){
                        //Si no es null la compra es porque salió todo bien
                        idCompra = nuevaCompra.getId();
                        mainLogica.compraActu = nuevaCompra;

                        for(int i = 0; i < mainLogica.listaProductos.size(); i ++){
                            nombre = mainLogica.listaProductos.get(i).getNombre();
                            cantidad = mainLogica.listaProductos.get(i).getCantidad();
                            valorUnitario = mainLogica.listaProductos.get(i).getValorUnitario();
                            total = mainLogica.listaProductos.get(i).getTotal();

                            mainLogica.crearProducto(nombre,cantidad, valorUnitario, total, idCompra);
                        }

                        mainLogica.cargarProductosByCompra(idCompra);
                        Toast.makeText(getApplicationContext(),"Compra duplicada exitosamente." , Toast.LENGTH_LONG).show();

                    } else {
                        //Si viene null la compra es porque hubo un error
                        Toast.makeText(getApplicationContext(),"Error al duplicar la compra." , Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),"Sin productos a duplicar." , Toast.LENGTH_SHORT).show();
                }
            }
        });
        //endregion

        //region EditText txtNombreCompra
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
        //endregion

        //region EditText txtCantidad
        txtCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Este método se llama antes de que el texto cambie.
            }

            @SuppressLint("SetTextI18n")
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
        //endregion

        //region EditText txtValorUni
        txtValorUni.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Este método se llama antes de que el texto cambie.
            }

            @SuppressLint("SetTextI18n")
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
        //endregion

        //region ListView lvProductos
        lvProductos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                //Eliminamos el producto de la base de datos
                boolean prodElim = mainLogica.eliminarProducto(mainLogica.listaProductos.get(position).getId());

                if (prodElim) {

                    //Si se eliminó correctamente, lo eliminamos del array
                    mainLogica.listaProductos.remove(position);
                    adapter= new CustomAdapterProductos(getApplicationContext(), mainLogica.listaProductos);
                    lvProductos.setAdapter(adapter);

                    Toast.makeText(getApplicationContext(), "Producto eliminado exitosamente.", Toast.LENGTH_LONG).show();

                    //Calculamos el total de la compra y actualizamos los datos de esta
                    calcularTotalFinal();
                    mainLogica.actualizarCompra(idCompraGlobal, mainLogica.listaProductos.size(), totalFinal, txtNombreCompra.getText().toString());

                } else {
                    Toast.makeText(getApplicationContext(),"ERROR: No se pudo eliminar el registro",Toast.LENGTH_LONG).show();

                }

                return true;
            }
        });

        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lbId = mainLogica.listaProductos.get(position).getId();
                txtProducto.setText(mainLogica.listaProductos.get(position).getNombre());
                txtCantidad.setText(String.valueOf(mainLogica.listaProductos.get(position).getCantidad()));
                txtValorUni.setText(String.valueOf(mainLogica.listaProductos.get(position).getValorUnitario()));
                //txtTotal.setText(String.valueOf(listaProductos.get(position).getTotal()));
                txtProducto.requestFocusFromTouch();
            }
        });

        //Método para realizar scroll del listview dentro del scroll original
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

        //endregion

        //region ScrollView miScrollView

        //Método para realizar scroll del listview dentro del scroll original
        miScrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.lvProductos).getParent()
                        .requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        //endregion


    }

    public void crearProducto(){

        boolean prodCreado = mainLogica.crearProducto(txtProducto.getText().toString(),
                Integer.parseInt(txtCantidad.getText().toString()),
                Double.parseDouble(txtValorUni.getText().toString()),
                Double.parseDouble(txtTotal.getText().toString()),
                idCompraGlobal);

        //Si se creó el producto exitosamente
        if (prodCreado){

            mainLogica.cargarProductosByCompra(idCompraGlobal);
            adapter= new CustomAdapterProductos(getApplicationContext(), mainLogica.listaProductos);
            lvProductos.setAdapter(adapter);
            lbId = 0;
            Toast.makeText(getApplicationContext(), "Producto creado exitosamente.", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "ERROR: No se pudo crear el Producto.", Toast.LENGTH_LONG).show();
        }
    }

    public void actualizarProducto(int idProd){
        String nombre = txtProducto.getText().toString();
        int cantidad = Integer.parseInt(txtCantidad.getText().toString());
        double valorUnitario = Double.parseDouble(txtValorUni.getText().toString());
        double total = Double.parseDouble(txtTotal.getText().toString());

        boolean prodActualizado = mainLogica.actualizarProducto(idProd, nombre, cantidad, valorUnitario, total, idCompraGlobal);

        if (prodActualizado){
            Toast.makeText(getApplicationContext(), "Producto editado exitosamente.",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "ERROR: No se pudo editar el Producto", Toast.LENGTH_LONG).show();
        }

    }

}