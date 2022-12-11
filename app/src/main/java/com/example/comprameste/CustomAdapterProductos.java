package com.example.comprameste;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomAdapterProductos extends BaseAdapter {

    Context context;
    ArrayList<Producto> listaProd;

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    public CustomAdapterProductos(Context context, ArrayList<Producto> listaProd) {
        this.context = context;
        this.listaProd = listaProd;
    }

    @Override
    public int getCount() {
        return listaProd.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtNomProd, txtCantidad, txtValorUni, txtTotal, lbCantidad, lbValorUnitario, lbTotal;

        Producto prod = listaProd.get(position);

        //Verificamos si la vista de ese item ya existe, sino la "inflamos" para mostrar. Si está actualizamos sus datos
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_products,null);
            txtNomProd = convertView.findViewById(R.id.txtNomProd);
            txtCantidad = convertView.findViewById(R.id.txtCantProd);
            txtValorUni = convertView.findViewById(R.id.txtValorProd);
            txtTotal = convertView.findViewById(R.id.txtTotalProd);

            lbCantidad = convertView.findViewById(R.id.lbCant);
            lbValorUnitario = convertView.findViewById(R.id.lbValUni);
            lbTotal = convertView.findViewById(R.id.lblTotal);

            //Si los datos de cantidad y valor unitario vienen en 0 es porque se eliminó el producto
            if (prod.getCantidad() == 0 && prod.getValorUnitario() == 0){

                //Colocamos las letras en blanco pues el fondo será rojo
                convertView.setBackgroundColor(Color.RED);
                lbCantidad.setTextColor(Color.WHITE);
                lbValorUnitario.setTextColor(Color.WHITE);
                lbTotal.setTextColor(Color.WHITE);
                txtNomProd.setTextColor(Color.WHITE);
                txtCantidad.setTextColor(Color.WHITE);
                txtValorUni.setTextColor(Color.WHITE);
                txtTotal.setTextColor(Color.WHITE);
            } else {

                //Si trae estos datos colocamos las letras en negro por si estábamos reactivando una eliminada
                convertView.setBackgroundColor(Color.WHITE);
                lbCantidad.setTextColor(Color.BLACK);
                lbValorUnitario.setTextColor(Color.BLACK);
                lbTotal.setTextColor(Color.BLACK);
                txtNomProd.setTextColor(Color.BLACK);
                txtCantidad.setTextColor(Color.BLACK);
                txtValorUni.setTextColor(Color.BLACK);
                txtTotal.setTextColor(Color.BLACK);

            }

        } else {
            txtNomProd = convertView.findViewById(R.id.txtNomProd);
            txtCantidad = convertView.findViewById(R.id.txtCantProd);
            txtValorUni = convertView.findViewById(R.id.txtValorProd);
            txtTotal = convertView.findViewById(R.id.txtTotalProd);

            lbCantidad = convertView.findViewById(R.id.lbCant);
            lbValorUnitario = convertView.findViewById(R.id.lbValUni);
            lbTotal = convertView.findViewById(R.id.lblTotal);

            //Si los datos de cantidad y valor unitario vienen en 0 es porque se eliminó el producto
            if (prod.getCantidad() == 0 && prod.getValorUnitario() == 0){

                //Colocamos las letras en blanco pues el fondo será rojo
                convertView.setBackgroundColor(Color.RED);
                lbCantidad.setTextColor(Color.WHITE);
                lbValorUnitario.setTextColor(Color.WHITE);
                lbTotal.setTextColor(Color.WHITE);
                txtNomProd.setTextColor(Color.WHITE);
                txtCantidad.setTextColor(Color.WHITE);
                txtValorUni.setTextColor(Color.WHITE);
                txtTotal.setTextColor(Color.WHITE);
            } else {

                //Si trae estos datos colocamos las letras en negro por si estábamos reactivando una eliminada
                convertView.setBackgroundColor(Color.WHITE);
                lbCantidad.setTextColor(Color.BLACK);
                lbValorUnitario.setTextColor(Color.BLACK);
                lbTotal.setTextColor(Color.BLACK);
                txtNomProd.setTextColor(Color.BLACK);
                txtCantidad.setTextColor(Color.BLACK);
                txtValorUni.setTextColor(Color.BLACK);
                txtTotal.setTextColor(Color.BLACK);

            }

        }

        txtNomProd.setText(prod.getNombre());
        txtCantidad.setText(String.valueOf(prod.getCantidad()));
        txtValorUni.setText("$"+formatea.format(prod.getValorUnitario()));
        txtTotal.setText("$"+formatea.format(prod.getTotal()));

        return convertView;
    }
}
