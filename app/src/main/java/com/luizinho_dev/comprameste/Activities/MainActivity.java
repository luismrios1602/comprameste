package com.luizinho_dev.comprameste.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comprameste.R;
import com.luizinho_dev.comprameste.CustomAdapters.CustomAdapterProductos;
import com.luizinho_dev.comprameste.CustomAdapters.RVAdapterProductos;
import com.luizinho_dev.comprameste.Entities.Compras;
import com.luizinho_dev.comprameste.Entities.Productos;
import com.luizinho_dev.comprameste.Logica.MainLogica;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    static EditText txtProducto;
    static EditText txtCantidad;
    static EditText txtValorUni;
    EditText txtNombreCompra;
    TextView txtTotalFinal;
    static TextView txtTotal;
    Button btnAgregar, btnCancelar, btnNuevaCompra, btnHistorial, btnDuplicarCompra;
    static NestedScrollView nestedScroll;
    RecyclerView rvProductos;


    public static long lbId = 0;
    double totalFinal = 0.0;

    long exitTime = System.currentTimeMillis();

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    static RVAdapterProductos rvadapter;

    Bundle bundle;
    MainLogica mainLogica = new MainLogica();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cargamos los elementos de la vista al "back"
        cargarElementos();

        //Cargamos la base de datos desde ROOM (En la logica)
        mainLogica.cargarBD(getApplicationContext());

        //Llamamos el bundle por si venimos desde la actividad Historial
        bundle = getIntent().getExtras();
        System.out.println("bundle: " + bundle);

        if (bundle != null){
            //Cargamos la compra actual, que si se viene desde el historial será la seleccionada

            //Pero antes, debemos validar si el bundle trae idCompra porque sino el SO me está mamando gallo mandando un Extras que no tiene nada
            int idCompra = bundle.getInt("idCompra");
            System.out.println("idCompra del bundle: "+ idCompra);

            //Siendo así, si es 0 el idCompra (No sé por qué) entonces llamamos a cargar la ultima, sino es 0 entonces llamamos a esa
            if (idCompra != 0){
                mainLogica.cargarCompraActual(bundle);

            } else {
                cargarInfoUltimaCompra();
            }
        } else {
            //Cargamos la informacion de la ultima compra y seteamos los valores de nombre y total
            cargarInfoUltimaCompra();
        }

        //No importa qué compras cargue, actualizamos / creamos el recyclerview
        actualizarRecyclerView();


        //Verificamos sin el id de la compra actual no es 0, para así habilitar el duplicar compra
        boolean habilitar = (mainLogica.compraActu.getId() != 0);
        btnDuplicarCompra.setEnabled(habilitar);

        //Cargamos el txt del nombre de la compra con la compra cargada
        txtNombreCompra.setText(mainLogica.compraActu.getNombre());
        txtTotalFinal.setText("$" + formatea.format(mainLogica.compraActu.getTotal()));

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
     * @description Método para cargar todos los elementos del XML al java para manipularlos
     */
    private void cargarElementos(){

        //Llamamos todos los objetos del front que vamos a utilizar
        txtNombreCompra = findViewById(R.id.txtNombreCompra);
        txtProducto = findViewById(R.id.txtProducto);
        txtCantidad = findViewById(R.id.txtCantidad);
        txtCantidad.setText("");
        txtValorUni = findViewById(R.id.txtValorUnitario);
        txtValorUni.setText("");
        txtTotal = findViewById(R.id.txtTotal);
        txtTotalFinal = findViewById(R.id.txtTotalFinal);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnNuevaCompra = findViewById(R.id.btnNuevaCompra);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnDuplicarCompra = findViewById(R.id.btnDuplicarCompra);
        nestedScroll = findViewById(R.id.nestedScroll);
        rvProductos = findViewById(R.id.rvProductos);
    }

    /**
     * @description Método para asignar todos los eventos de cada uno de los elementos de la vista
     */
    @SuppressLint("ClickableViewAccessibility")
    private void asignarEventos(){

        //region Button btnAgregar
        btnAgregar.setOnClickListener(v -> {

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

        });
        //endregion

        //region Button btnCancelar
        btnCancelar.setOnClickListener(v -> {
            limpiarCampos();
            actualizarRecyclerView();
        });
        //endregion

        //region Button btnNuevaCompra
        btnNuevaCompra.setOnClickListener(v -> {
            //Limpiamos los campos de productos y los datos de la compra
            //Limpiamos la compra actual para que no me actualice los datos de la ultima compra
            mainLogica.compraActu = new Compras(0);
            limpiarCampos();
            txtTotalFinal.setText("$0");
            txtNombreCompra.setText("");
            btnDuplicarCompra.setEnabled(false);
            mainLogica.listaProductos.clear();
            rvadapter.notifyDataSetChanged();
            actualizarRecyclerView();

        });

        //endregion

        //region Button btnHistorial
        btnHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HistorialActivity.class);
            startActivity(intent);
        });
        //endregion

        //region Button btnDuplicarCompra
        btnDuplicarCompra.setOnClickListener(v -> {
            if (mainLogica.listaProductos.size() > 0){
                String nombreCompra = txtNombreCompra.getText().toString() + " duplicado";
                String nombre;
                int cantidad;
                double valorUnitario;
                double total;
                int idCompra;

                //Creamos la nueva compra para asignarla a los productos a crear
                Compras nuevaCompra = mainLogica.crearCompra(mainLogica.listaProductos.size(), mainLogica.compraActu.getTotal(), nombreCompra);

                if (nuevaCompra != null){
                    //Si no es null la compra es porque salió todo bien
                    idCompra = nuevaCompra.getId();
                    mainLogica.compraActu = nuevaCompra;
                    //Actualizamos el nombre en la vista
                    txtNombreCompra.setText(mainLogica.compraActu.getNombre());

                    for(int i = 0; i < mainLogica.listaProductos.size(); i ++){
                        nombre = mainLogica.listaProductos.get(i).getNombre();
                        cantidad = mainLogica.listaProductos.get(i).getCantidad();
                        valorUnitario = mainLogica.listaProductos.get(i).getValorUnitario();
                        total = mainLogica.listaProductos.get(i).getTotal();

                        mainLogica.crearProducto(nombre,cantidad, valorUnitario, total, idCompra);
                    }

                    mainLogica.cargarProductosByCompra(idCompra);
                    actualizarRecyclerView();
                    Toast.makeText(getApplicationContext(),"Compra duplicada exitosamente." , Toast.LENGTH_LONG).show();

                } else {
                    //Si viene null la compra es porque hubo un error
                    Toast.makeText(getApplicationContext(),"Error al duplicar la compra." , Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(),"Sin productos a duplicar." , Toast.LENGTH_SHORT).show();
            }
        });
        //endregion

        //region EditText txtNombreCompra
        txtNombreCompra.setOnFocusChangeListener((v, hasFocus) -> {
            String nombreCompra = txtNombreCompra.getText().toString();
            if(mainLogica.compraActu.getId() != 0){
                System.out.println("Actualizando compra...");
                mainLogica.actualizarCompra(mainLogica.compraActu.getId(),mainLogica.listaProductos.size(),mainLogica.compraActu.getTotal(),nombreCompra);
            } else {
                //Si el id de la compra actual es 0 entonces solo le asignamos el nombre pero no la guardamos en BD
                mainLogica.compraActu.setNombre(nombreCompra);
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
        mainLogica.cargarProductosByCompra(mainLogica.compraActu.getId());

        double total = 0.0;
        for (int i = 0; i< mainLogica.listaProductos.size(); i++){
            total += mainLogica.listaProductos.get(i).getTotal();
        }

        totalFinal = total;
        txtTotalFinal.setText("$"+formatea.format(total));
        //Actualizamos la compra actual cada vez que se calcule el totalFinal
        mainLogica.actualizarCompra(mainLogica.compraActu.getId(), mainLogica.listaProductos.size(), totalFinal, mainLogica.compraActu.getNombre());

    }

    public void crearProducto(){

        Productos prodCreado = mainLogica.crearProducto(txtProducto.getText().toString(),
                Integer.parseInt(txtCantidad.getText().toString()),
                Double.parseDouble(txtValorUni.getText().toString()),
                Double.parseDouble(txtTotal.getText().toString()),
                mainLogica.compraActu.getId());

        //Si se creó el producto exitosamente
        if (prodCreado != null){
            mainLogica.cargarProductosCompraActual();
            actualizarRecyclerView();
            lbId = 0;
            Toast.makeText(getApplicationContext(), "Producto creado exitosamente.", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(getApplicationContext(), "ERROR: No se pudo crear el Producto.", Toast.LENGTH_LONG).show();
        }
    }

    public void actualizarProducto(long idProd){
        String nombre = txtProducto.getText().toString();
        int cantidad = Integer.parseInt(txtCantidad.getText().toString());
        double valorUnitario = Double.parseDouble(txtValorUni.getText().toString());
        double total = Double.parseDouble(txtTotal.getText().toString());

        boolean prodActualizado = mainLogica.actualizarProducto(idProd, nombre, cantidad, valorUnitario, total, mainLogica.compraActu.getId());

        if (prodActualizado){
            //Si se actualizó correctamente tenemos que actualizar el adapter
            mainLogica.cargarProductosCompraActual();
            actualizarRecyclerView();

            Toast.makeText(getApplicationContext(), "Producto editado exitosamente.",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "ERROR: No se pudo editar el Producto", Toast.LENGTH_LONG).show();
        }

    }

//    public void actualizarAdapter(){
//        adapter= new CustomAdapterProductos(getApplicationContext(), mainLogica.listaProductos);
//        lvProductos.setAdapter(adapter);
//    }

    public void actualizarRecyclerView(){
        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        rvadapter = new RVAdapterProductos(mainLogica.listaProductos);
        rvProductos.setAdapter(rvadapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(rvadapter.itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(rvProductos);

    }
    public void cargarInfoUltimaCompra(){

        mainLogica.cargarUltimaCompra();
        txtNombreCompra.setText(mainLogica.compraActu.getNombre());
        System.out.println("totalCompra: "+mainLogica.compraActu.getTotal());
        txtTotalFinal.setText("$"+formatea.format(mainLogica.compraActu.getTotal()));

    }

    /**
     * @description Método utilizado por el Adapter para cargar desde allá los valores del producto a editar
     * @param prodEdit
     */
    public static void editarProducto(Productos prodEdit){
        System.out.println(prodEdit);
        //Cargamos los datos del producto seleccionado (Todos los campos se vuelven static por el metodo)
        lbId = prodEdit.getId();
        txtProducto.setText(prodEdit.getNombre());
        txtCantidad.setText(String.valueOf(prodEdit.getCantidad()));
        txtValorUni.setText(String.valueOf(prodEdit.getValorUnitario()));
        txtTotal.setText(String.valueOf(prodEdit.getTotal()));
        txtProducto.requestFocusFromTouch();
        //Le notificamos al adapter que tuvo un cambio (En realidad es para que me vuelva a mostrar el elemento porque lo borra :v)
        rvadapter.notifyDataSetChanged();
        nestedScroll.scrollTo(-20,-20);
    }




}