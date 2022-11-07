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

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<Producto> listaProd;

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    public CustomAdapter(Context context, ArrayList<Producto> listaProd) {
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

        } else {
            txtNomProd = convertView.findViewById(R.id.txtNomProd);
            txtCantidad = convertView.findViewById(R.id.txtCantProd);
            txtValorUni = convertView.findViewById(R.id.txtValorProd);
            txtTotal = convertView.findViewById(R.id.txtTotalProd);

            if (prod.getCantidad() == 0 && prod.getValorUnitario() == 0){

                lbCantidad = convertView.findViewById(R.id.lbCant);
                lbValorUnitario = convertView.findViewById(R.id.lbValUni);
                lbTotal = convertView.findViewById(R.id.lblTotal);

                lbCantidad.setTextColor(Color.WHITE);
                lbValorUnitario.setTextColor(Color.WHITE);
                lbTotal.setTextColor(Color.WHITE);
                txtNomProd.setTextColor(Color.WHITE);
                txtCantidad.setTextColor(Color.WHITE);
                txtValorUni.setTextColor(Color.WHITE);
                txtTotal.setTextColor(Color.WHITE);
            }

        }

        txtNomProd.setText(prod.getNombre());
        txtCantidad.setText(String.valueOf(prod.getCantidad()));
        txtValorUni.setText("$"+formatea.format(prod.getValorUnitario()));
        txtTotal.setText("$"+formatea.format(prod.getTotal()));

        return convertView;
    }
}
