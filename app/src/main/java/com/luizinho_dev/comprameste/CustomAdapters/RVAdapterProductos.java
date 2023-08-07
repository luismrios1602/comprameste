package com.luizinho_dev.comprameste.CustomAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprameste.R;
import com.luizinho_dev.comprameste.Activities.MainActivity;
import com.luizinho_dev.comprameste.Entities.Productos;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class RVAdapterProductos extends RecyclerView.Adapter<RVAdapterProductos.ViewHolder> {
    private ArrayList<Productos> productos;

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    // Constructor para inicializar el adaptador con la lista de datos
    public RVAdapterProductos(ArrayList<Productos> productos) {
        this.productos = productos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el diseño de cada elemento del RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_products, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Vincular los datos con las vistas en cada elemento del RecyclerView
        Productos producto = productos.get(position);
        System.out.println(producto.toString());

        holder.txtNomProd.setText(producto.getNombre());
        holder.txtCantidad.setText(String.valueOf(producto.getCantidad()));
        holder.txtValorUni.setText(formatea.format(producto.getValorUnitario()));
        holder.txtTotal.setText(formatea.format(producto.getTotal()));
    }

    @Override
    public int getItemCount() {
        // Devuelve el número total de elementos en la lista de datos
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNomProd, txtCantidad, txtValorUni, txtTotal, lbCantidad, lbValorUnitario, lbTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Referenciar las vistas dentro de cada elemento del RecyclerView
            txtNomProd = itemView.findViewById(R.id.txtNomProd);
            txtCantidad = itemView.findViewById(R.id.txtCantProd);
            txtValorUni = itemView.findViewById(R.id.txtValorProd);
            txtTotal = itemView.findViewById(R.id.txtTotalProd);


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
                    Productos producto = productos.get(position);
                    MainActivity.editarProducto(producto);
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
