package com.luizinho_dev.comprameste.CustomAdapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.comprameste.R;
import com.luizinho_dev.comprameste.Entities.Productos;
import com.luizinho_dev.comprameste.Producto;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomAdapterProductos extends BaseAdapter {

    Context context;
    ArrayList<Productos> listaProd;

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    public CustomAdapterProductos(Context context, ArrayList<Productos> listaProd) {
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

        Productos prod = listaProd.get(position);

        //Verificamos si la vista de ese item ya existe, sino la "inflamos" para mostrar. Si está actualizamos sus datos
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_products,null);

        }

        txtNomProd = convertView.findViewById(R.id.txtNomProd);
        txtCantidad = convertView.findViewById(R.id.txtCantProd);
        txtValorUni = convertView.findViewById(R.id.txtValorProd);
        txtTotal = convertView.findViewById(R.id.txtTotalProd);

        txtNomProd.setText(prod.getNombre());
        txtCantidad.setText(String.valueOf(prod.getCantidad()));
        txtValorUni.setText("$ "+formatea.format(prod.getValorUnitario()));
        txtTotal.setText("$ "+formatea.format(prod.getTotal()));

        return convertView;
    }
}
