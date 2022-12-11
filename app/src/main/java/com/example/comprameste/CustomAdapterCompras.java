package com.example.comprameste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomAdapterCompras extends BaseAdapter {
    Context context;
    ArrayList<Compra> listaCompras;

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    public CustomAdapterCompras(Context context, ArrayList<Compra> listaCompras) {
        this.context = context;
        this.listaCompras = listaCompras;
    }

    @Override
    public int getCount() {
        return listaCompras.size();
    }

    @Override
    public Object getItem(int position) {
        return listaCompras.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView txtIdCompra, txtFecha, txtCantProdCompra, txtTotalCompra, lbIdCompra, lbFecha, lbCantProdCompra, lbTotalCompra;

        Compra compra = listaCompras.get(position);

        //Verificamos si la vista de ese item ya existe, sino la "inflamos" para mostrar. Si está actualizamos sus datos
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_compras, null);
            txtIdCompra = convertView.findViewById(R.id.txtIdCompra);
            txtFecha = convertView.findViewById(R.id.txtFecha);
            txtCantProdCompra = convertView.findViewById(R.id.txtCantProdCompra);
            txtTotalCompra = convertView.findViewById(R.id.txtTotalCompra);

            lbIdCompra = convertView.findViewById(R.id.lbIdCompra);
            lbFecha = convertView.findViewById(R.id.lbFecha);
            lbCantProdCompra = convertView.findViewById(R.id.lbCantProdCompra);
            lbTotalCompra = convertView.findViewById(R.id.lbTotalCompra);

        } else {

            txtIdCompra = convertView.findViewById(R.id.txtIdCompra);
            txtFecha = convertView.findViewById(R.id.txtFecha);
            txtCantProdCompra = convertView.findViewById(R.id.txtCantProdCompra);
            txtTotalCompra = convertView.findViewById(R.id.txtTotalCompra);

            lbIdCompra = convertView.findViewById(R.id.lbIdCompra);
            lbFecha = convertView.findViewById(R.id.lbFecha);
            lbCantProdCompra = convertView.findViewById(R.id.lbCantProdCompra);
            lbTotalCompra = convertView.findViewById(R.id.lbTotalCompra);

        }

        txtIdCompra.setText(String.valueOf(compra.getId()));
        txtFecha.setText(compra.getFecha());
        txtCantProdCompra.setText(String.valueOf(compra.getCantProductos()));
        txtTotalCompra.setText("$"+formatea.format(compra.getTotal()));

        return convertView;
    }
}
