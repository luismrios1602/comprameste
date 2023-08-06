package com.luizinho_dev.comprameste.Logica;

import android.content.Context;
import android.os.Bundle;

import androidx.room.Room;

import com.luizinho_dev.comprameste.Compra;
import com.luizinho_dev.comprameste.CustomAdapters.CustomAdapterProductos;
import com.luizinho_dev.comprameste.Dao.ComprasDao;
import com.luizinho_dev.comprameste.Database.AppDatabase;
import com.luizinho_dev.comprameste.Entities.Compras;
import com.luizinho_dev.comprameste.Entities.Productos;

import java.security.spec.ECField;
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

            db = Room.databaseBuilder(context, AppDatabase.class, "compramesteDB").allowMainThreadQueries().build();
            System.out.println("Conexion creada.");

        } catch (Exception e){
            System.out.println("Error al crear la conexion");
        }
    }

    public void cargarUltimaCompra(){

        try {
            //Consultamos en la base de datos la última compra
            Compras ultCompra = db.comprasDao().findUltimaCompra();

            //Si no hay ultima compra el resultado es null, por tanto si hay un objeto, buscamos los productos de dicha compra
            if (ultCompra != null) listaProductos = (ArrayList<Productos>) db.productosDao().findProductosByIdCompra(ultCompra.id);
            //Si está null entonces mandamos una compra
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
            String nombreCompraSelect = bundle.getString("nombreCompra");
            //Si el id que viene es diferente de 0 entonces le asignamos lo que haya en idCompra (Pero si viene 0 va 0, esa validacion para que? - buena pregunta :v)
            int idCompraSelect = (bundle.getInt("idCompra")!=0) ? bundle.getInt("idCompra") : 0;

            //Le asignamos los datos al objeto global de la compra
            compraActu.setId(idCompraSelect);
            compraActu.setNombre(nombreCompraSelect);

            //Cargamos los productos de la compra seleccionada
            listaProductos = (ArrayList<Productos>) db.productosDao().findProductosByIdCompra(compraActu.getId());

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
        listaProductos = (ArrayList<Productos>) db.productosDao().findProductosByIdCompra(idCompra);
    }

    public Productos buscarProductoById(int idProd){

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

            Compras compra = new Compras(fechaFormat, nombre);
            db.comprasDao().createCompra(compra);

            //Después de crear la compra vamos a la BD a consultar la ultima compra creada
            compra = db.comprasDao().findUltimaCompra();
            return compra;

        } catch (Exception e){
            System.err.println("Error creando la compra "+ e);
            return null;

        }
    }

    public boolean actualizarCompra(int idCompra, int cantProductos, double total, String nombre){

        try {
            //Nos traemos la compra para no perder la fecha
            Compras compra = buscarCompraById(idCompra);
            if (compra != null){
                compra.setCantProductos(cantProductos);
                compra.setTotal(total);
                compra.setNombre(nombre);

                db.comprasDao().updateCompra(compra);
                return true;
            } else {
                System.out.println("Compra " + idCompra + " no encontrada.");
                return false;
            }
        } catch (Exception e){
            System.out.println("Error al actualizar compra.");
            return false;
        }
    }

    public boolean crearProducto(String nombre, int cantidad, double valorUnitario, double total, int idCompra){

        try{

            //Si el idCompra 0 entonces le creamos la compra con la que se va a guardar y de paso se la asignamos a la compra actual
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
                    return false;
                }
            }

            Productos producto = new Productos(nombre, cantidad, valorUnitario, total, idCompra);
            db.productosDao().createProducto(producto);
            System.out.println("Producto "+producto.getNombre()+ " creado exitosamente.");

            return true;
        } catch (Exception e){
            System.out.println("Se ha presentado un error creando los productos");
            return false;
        }

    }

    public boolean actualizarProducto(int idProd, String nombre, int cantidad, double valorUnitario, double total, int idCompra){
        try {

            Productos prod = buscarProductoById(idProd);
            if (prod != null){
                prod.setNombre(nombre);
                prod.setCantidad(cantidad);
                prod.setValorUnitario(valorUnitario);
                prod.setTotal(total);
                prod.setIdCompra(idCompra);

                db.productosDao().updateProducto(prod);
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            System.err.println("Error al actualizar producto: "+e);
            return false;
        }


    }

    public boolean eliminarProducto(int idProd){

        try {

            Productos prod = buscarProductoById(idProd);
            db.productosDao().deleteProducto(prod);
            System.out.println("Producto "+ idProd + " eliminado.");
            return true;

        } catch (Exception e){
            System.out.println("Error al eliminar producto "+idProd+": "+e);
            return false;
        }

    }

}
