package com.example.comprameste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<Producto> listaProd;

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    public CustomAdapter(Context context, List<Producto> listaProd) {
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
        TextView txtNomProd, txtCantidad, txtValorUni, txtTotal;

        Producto prod = listaProd.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_products,null);
            txtNomProd = convertView.findViewById(R.id.txtNomProd);
            txtCantidad = convertView.findViewById(R.id.txtCantProd);
            txtValorUni = convertView.findViewById(R.id.txtValorProd);
            txtTotal = convertView.findViewById(R.id.txtTotalProd);

            txtNomProd.setText(prod.getNombre());
            txtCantidad.setText(String.valueOf(prod.getCantidad()));
            txtValorUni.setText("$"+formatea.format(prod.getValorUnitario()));
            txtTotal.setText("$"+formatea.format(prod.getTotal()));

        }
        return convertView;
    }
}
