package com.luizinho_dev.comprameste.CustomAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprameste.R;
import com.luizinho_dev.comprameste.Activities.MainActivity;
import com.luizinho_dev.comprameste.Entities.Compras;
import com.luizinho_dev.comprameste.Entities.Productos;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class RVAdapterCompras extends RecyclerView.Adapter<RVAdapterCompras.ViewHolder> {
    private ArrayList<Compras> compras;

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    // Constructor para inicializar el adaptador con la lista de datos
    public RVAdapterCompras(ArrayList<Compras> compras) {
        this.compras = compras;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de cada elemento del RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_compras, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Vincular los datos con las vistas en cada elemento del RecyclerView
        Compras compra = compras.get(position);
        System.out.println(compra.toString());

        holder.txtIdCompra.setText(String.valueOf(compra.getId()));
        holder.txtNombreCompraHis.setText(compra.getNombre());
        holder.txtFecha.setText(String.valueOf(compra.getFecha()));
        holder.txtCantProdCompra.setText(String.valueOf(compra.getCantProductos()));
        holder.txtTotalCompra.setText("$ "+formatea.format(compra.getTotal()));

    }

    @Override
    public int getItemCount() {
        // Devuelve el número total de elementos en la lista de datos
        return compras.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtIdCompra, txtNombreCompraHis, txtFecha, txtCantProdCompra, txtTotalCompra;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Referenciar las vistas dentro de cada elemento del RecyclerView
            txtIdCompra = itemView.findViewById(R.id.txtIdCompra);
            txtNombreCompraHis = itemView.findViewById(R.id.txtNombreCompraHis);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtCantProdCompra = itemView.findViewById(R.id.txtCantProdCompra);
            txtTotalCompra = itemView.findViewById(R.id.txtTotalCompra);


        }
    }

    // Implementa ItemTouchHelper.Callback para el arrastrar y soltar
    public ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
//            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int dragFlags = 0;
            int swipeFlags = ItemTouchHelper.LEFT ;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder source, @NonNull RecyclerView.ViewHolder target) {
            try {
/*                // Aquí puedes manejar la lógica de reordenar los elementos en tu lista y notificar al adaptador
                // Por ejemplo, intercambiar elementos en la lista y notificar el cambio con notifyItemMoved
                int sourcePosition = source.getAdapterPosition();
                int targetPosition = target.getAdapterPosition();
                Collections.swap(productos, sourcePosition, targetPosition);
                notifyItemMoved(sourcePosition, targetPosition);*/
                return false;
            } catch (Exception e){
                System.out.println(e);
                return false;
            }
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            try {
                if (direction == ItemTouchHelper.LEFT) {
                    // Obtén la posición del elemento que se deslizó
                    int position = viewHolder.getAdapterPosition();
                    Compras compra = compras.get(position);
//                    MainActivity.editarProducto(compra);
                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        @Override
        public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
            // Ajusta el umbral de deslizamiento
            return 0.10f; // Por ejemplo, deslizar al menos la mitad del ancho del elemento
        }

    };


}

