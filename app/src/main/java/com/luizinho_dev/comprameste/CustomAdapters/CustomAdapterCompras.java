package com.luizinho_dev.comprameste.CustomAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.comprameste.R;
import com.luizinho_dev.comprameste.Compra;
import com.luizinho_dev.comprameste.Entities.Compras;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomAdapterCompras extends BaseAdapter {
    Context context;
    ArrayList<Compras> listaCompras;
    DecimalFormat formatterDecimal = new DecimalFormat("###,###.##");

    public CustomAdapterCompras(Context context, ArrayList<Compras> listaCompras) {
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
        TextView txtIdCompra, txtFecha, txtCantProdCompra, txtTotalCompra, txtNombreCompraHis, lbIdCompra, lbFecha, lbCantProdCompra, lbTotalCompra;

        Compras compra = listaCompras.get(position);

        //Verificamos si la vista de ese item ya existe, sino la "inflamos" para mostrar. Si está actualizamos sus datos
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_compras, null);
            txtIdCompra = convertView.findViewById(R.id.txtIdCompra);
            txtFecha = convertView.findViewById(R.id.txtFecha);
            txtCantProdCompra = convertView.findViewById(R.id.txtCantProdCompra);
            txtTotalCompra = convertView.findViewById(R.id.txtTotalCompra);
            txtNombreCompraHis = convertView.findViewById(R.id.txtNombreCompraHis);

            lbIdCompra = convertView.findViewById(R.id.lbIdCompra);
            lbFecha = convertView.findViewById(R.id.lbFecha);
            lbCantProdCompra = convertView.findViewById(R.id.lbCantProdCompra);
            lbTotalCompra = convertView.findViewById(R.id.lbTotalCompra);

        } else {

            txtIdCompra = convertView.findViewById(R.id.txtIdCompra);
            txtFecha = convertView.findViewById(R.id.txtFecha);
            txtCantProdCompra = convertView.findViewById(R.id.txtCantProdCompra);
            txtTotalCompra = convertView.findViewById(R.id.txtTotalCompra);
            txtNombreCompraHis = convertView.findViewById(R.id.txtNombreCompraHis);

            lbIdCompra = convertView.findViewById(R.id.lbIdCompra);
            lbFecha = convertView.findViewById(R.id.lbFecha);
            lbCantProdCompra = convertView.findViewById(R.id.lbCantProdCompra);
            lbTotalCompra = convertView.findViewById(R.id.lbTotalCompra);

        }

        txtNombreCompraHis.setText(String.valueOf(compra.getNombre()));
        txtIdCompra.setText(String.valueOf(compra.getId()));
        txtFecha.setText(compra.getFecha());
        txtCantProdCompra.setText(String.valueOf(compra.getCantProductos()));
        txtTotalCompra.setText("$"+ formatterDecimal.format(compra.getTotal()));


        return convertView;
    }
}
