package com.luizinho_dev.comprameste.Logica;

import static com.luizinho_dev.comprameste.Database.AppDatabase.MIGRATION_1_2;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.room.Room;

import com.luizinho_dev.comprameste.Activities.HistorialActivity;
import com.luizinho_dev.comprameste.Activities.MainActivity;
import com.luizinho_dev.comprameste.Compra;
import com.luizinho_dev.comprameste.CustomAdapters.CustomAdapterProductos;
import com.luizinho_dev.comprameste.Dao.ComprasDao;
import com.luizinho_dev.comprameste.Database.AppDatabase;
import com.luizinho_dev.comprameste.Entities.Compras;
import com.luizinho_dev.comprameste.Entities.Productos;

import java.security.spec.ECField;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainLogica {

    public ArrayList<Productos> listaProductos = new ArrayList<>();
    public Compras compraActu = new Compras();

    SimpleDateFormat formatterFecha = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

    public AppDatabase db;

    public MainLogica(){

    }

    public void cargarBD(Context context){
        try {

            db = Room.databaseBuilder(context, AppDatabase.class, "compramesteDB").addMigrations(MIGRATION_1_2).allowMainThreadQueries().build();
            System.out.println("Conexion creada.");

        } catch (Exception e){
            System.out.println("Error al crear la conexion");
        }
    }

    public void cargarUltimaCompra(){

        try {
            //Consultamos en la base de datos la última compra
            Compras ultCompra = db.comprasDao().findUltimaCompra();

            System.out.println("Ultima compra: " + ultCompra);

            //Si no hay ultima compra el resultado es null, por tanto si hay un objeto, buscamos los productos de dicha compra
            if (ultCompra != null)  {
                listaProductos.clear();
                ArrayList<Productos> productos = (ArrayList<Productos>) db.productosDao().findProductosByIdCompra(ultCompra.getId());
                listaProductos.addAll(productos);
            }
            //Si está null entonces mandamos una compra nueva (con 0)
            else ultCompra = new Compras(0);

            //Asignamos la compraAct para que en el front se pueda utilizar esta directamente
            compraActu = ultCompra;

        } catch (Exception e){
            System.err.println("Error al consultar ultima compra");
            compraActu = new Compras(0);
        }

    }

    /**
     * @descption Método para cargar en el front la información de la última compra o la compra seleccionada desde el historial
     * según corresponda
     */
    public void cargarCompraActual(Bundle bundle){

        //Si no venimos desde otra vista (O se le da atrás en historial) el bundle viene null
        if(bundle != null){
            System.out.println("idCompra seleccionada: "+bundle.getInt("idCompra"));
            //Si el id que viene es diferente de 0 entonces le asignamos lo que haya en idCompra (Pero si viene 0 va 0, esa validacion para que? - buena pregunta :v)
            int idCompraSelect = (bundle.getInt("idCompra")!=0) ? bundle.getInt("idCompra") : 0;

            //Ya con el id seleccionado, consultamos los datos de la compra
            Compras compraEnc = db.comprasDao().findCompraById(idCompraSelect);
            System.out.println("Compra encontrada: "+ compraEnc);

            //Le asignamos los datos al objeto global de la compra si la compra no es null
            if (compraEnc != null ){
                compraActu = compraEnc;

            } else {
                //Si viene null entonces creamos un objeto nuevo para que no haya peos despues
                compraActu.setId(0);
                compraActu.setNombre("");
                compraActu.setCantProductos(0);
                compraActu.setFecha("");
                compraActu.setTotal(0.0);
            }

            //Una vez consultada la compra, vamos a consultar sus productos
            cargarProductosByCompra(compraActu.getId());

        } else {
            //Si el bundle es null es porque apenas estamos entrando a la app, así que cargamos la ultma compra
            cargarUltimaCompra();
        }

    }

    public Compras buscarCompraById(int idCompra){
        try {
            Compras compra = db.comprasDao().findCompraById(idCompra);
            return compra;

        } catch (Exception e){
            System.out.println("Error al consultar compra.");
            return null;
        }
    }

    public void cargarProductosByCompra(int idCompra){
        listaProductos.clear();
        ArrayList<Productos> productos = (ArrayList<Productos>) db.productosDao().findProductosByIdCompra(idCompra);
        listaProductos.addAll(productos);
    }

    public void cargarProductosCompraActual(){
        listaProductos.clear();
        ArrayList<Productos> productos = (ArrayList<Productos>) db.productosDao().findProductosByIdCompra(compraActu.getId());
        listaProductos.addAll(productos);
    }

    public Productos buscarProductoById(long idProd){

        try {

            Productos nuevoProd = db.productosDao().findProductoById(idProd);
            return nuevoProd;

        } catch (Exception e){

            System.out.println("Error al consultar el producto: "+e);
            return null;

        }
    }

    public Compras crearCompra(int cantProductos, double total, String nombre){
        try {
            Date fecha = new Date();
            String fechaFormat = formatterFecha.format(fecha);

            Compras compra = new Compras(fechaFormat, cantProductos, total, nombre);
            db.comprasDao().createCompra(compra);

            //Después de crear la compra vamos a la BD a consultar la ultima compra creada
            compra = db.comprasDao().findUltimaCompra();
            return compra;

        } catch (Exception e){
            System.err.println("Error creando la compra "+ e);
            return null;

        }
    }

    public Compras crearCompra(String nombre){
        try {
            Date fecha = new Date();
            String fechaFormat = formatterFecha.format(fecha);

            nombre = (nombre == null) ? "NN" : nombre;

            Compras compra = new Compras(fechaFormat, nombre);
            long id = db.comprasDao().createCompra(compra);

            //Después de crear la compra vamos a la BD a consultar la compra con el id creado
            compra = db.comprasDao().findCompraById(id);
            return compra;

        } catch (Exception e){
            System.err.println("Error creando la compra "+ e);
            return null;

        }
    }

    public boolean actualizarCompra(int idCompra, int cantProductos, double total, String nombre){

        try {
            //Si la compra es 0 es porque directamente no la hemos guardado así que no actualizamos
            if (idCompra != 0){
                //Nos traemos la compra para no perder la fecha
                Compras compra = buscarCompraById(idCompra);
                if (compra != null){
                    compra.setCantProductos(cantProductos);
                    compra.setTotal(total);
                    compra.setNombre(nombre);

                    db.comprasDao().updateCompra(compra);
                    compraActu = compra;
                    return true;
                } else {
                    System.out.println("Compra " + idCompra + " no encontrada.");
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e){
            System.out.println("Error al actualizar compra.");
            return false;
        }
    }

    public Productos crearProducto(String nombre, int cantidad, double valorUnitario, double porcDesc, double total, int idCompra){

        try{

            //Si el idCompra es 0 entonces le creamos la compra con la que se va a guardar y de paso se la asignamos a la compra actual
            if (idCompra == 0){
                Compras compra = crearCompra(compraActu.getNombre());
                //Validamos que se haya guardado correctamente
                if (compra != null){
                    //Con este dato actualizamos la compra actual
                    compraActu = compra;
                    idCompra = compra.getId();
                    System.out.println("Compra creada: "+ compra.getId());
                } else {
                    System.err.println("Error al crear la compra principal.");
                    return null;
                }
            }

            Productos producto = new Productos(nombre, cantidad, valorUnitario, porcDesc, total, idCompra);
            long idProdCreado = db.productosDao().createProducto(producto);
            producto.setId(idProdCreado);
            System.out.println("Producto "+ producto + " creado exitosamente.");

            return producto;
        } catch (Exception e){
            System.out.println("Se ha presentado un error creando los productos");
            return null;
        }

    }

    public boolean actualizarProducto(long idProd, String nombre, int cantidad, double valorUnitario, double porcDesc, double total, int idCompra){
        try {

            Productos prod = buscarProductoById(idProd);
            if (prod != null){
                prod.setNombre(nombre);
                prod.setCantidad(cantidad);
                prod.setValorUnitario(valorUnitario);
                prod.setPorcDesc(porcDesc);
                prod.setTotal(total);
                prod.setIdCompra(idCompra);

                db.productosDao().updateProducto(prod);
                System.out.println("Producto actualizado!");
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            System.err.println("Error al actualizar producto: "+e);
            return false;
        }


    }

    public void eliminarProducto(Context context, Productos producto){

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Borrar Producto...").
                    setMessage(MessageFormat.format("¿Está seguro que desea borrar el producto {0}?",producto.getNombre())).
                    setCancelable(true).
                    setPositiveButton("Sí",(dialog, which) -> {
                        try {
                            System.out.println("Eliminando producto...");
                            //Validamos que haya una conexion, sino creamos una
                            if (db == null) {
                                cargarBD(context);
                            }

                            int deleted = db.productosDao().deleteProducto(producto);
                            System.out.println(deleted);
                            System.out.println("Producto eliminado exitosamente");

                            // Mandamos a actualizar a adapter del MainActivity para que no se quede pegado
                            if (context instanceof MainActivity) {
                                //Calculamos el total de la compra que esto actualiza la lista, calcula y actualiza la compra
                                ((MainActivity) context).calcularTotalFinal();
                                //Actualizamos el recycler de la vista
                                ((MainActivity) context).actualizarRecyclerView();
                                //Limpiamos la vista del formulario por si le dieron editar y luego eliminar
                                ((MainActivity) context).limpiarCampos();
                            }

                        } catch (Exception e){
                            System.err.println("Error al eliminar producto: "+ e);

                        }

                    }).
                    setNegativeButton("No",(dialog, which) -> {

                        System.out.println("No eliminar compra");
                        dialog.cancel();

                        //Validamos que haya una conexion, sino creamos una (Ni sé por qué pero bueno :v)
                        if (db == null) {
                            cargarBD(context);
                        }

                        // Mandamos a actualizar a adapter del MainActivity para que no se quede pegado
                        if (context instanceof MainActivity) {
                            //Cargamos los productos para actualizar la lista
                            cargarProductosByCompra(producto.getIdCompra());
                            ((MainActivity) context).actualizarRecyclerView();
                        }

                    }).show();


        } catch (Exception e){
            System.out.println("Error al eliminar producto "+producto.getId()+": "+e);
        }

    }

}
