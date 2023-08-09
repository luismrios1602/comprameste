package com.luizinho_dev.comprameste.CustomAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprameste.R;
import com.luizinho_dev.comprameste.Activities.MainActivity;
import com.luizinho_dev.comprameste.Entities.Compras;
import com.luizinho_dev.comprameste.Logica.HistorialLogica;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class RVAdapterCompras extends RecyclerView.Adapter<RVAdapterCompras.ViewHolder> {
    private ArrayList<Compras> compras;
    private Drawable icon_delete;
    private Drawable icon_detail;

    AlertDialog.Builder builder;
    DecimalFormat formatea = new DecimalFormat("###,###.##");

    // Constructor para inicializar el adaptador con la lista de datos
    public RVAdapterCompras(Context context, ArrayList<Compras> compras) {
        this.compras = compras;
        //Cargamos el icono a mostrar al mostrar y eliminar
        icon_delete = ContextCompat.getDrawable(context, R.drawable.ic_icon_delete);
        icon_detail = ContextCompat.getDrawable(context, R.drawable.ic_icon_detail);
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
            int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
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
                    Intent intent = new Intent(viewHolder.itemView.getContext(), MainActivity.class);
                    intent.putExtra("idCompra",compra.getId());
                    intent.putExtra("nombreCompra",compra.getNombre());
                    viewHolder.itemView.getContext().startActivity(intent);

                } else {
                    HistorialLogica historialLogica = new HistorialLogica();
                    int position = viewHolder.getAdapterPosition();
                    historialLogica.eliminarCompra(viewHolder.itemView.getContext(), compras.get(position));

                }
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        @Override
        public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
            // Ajusta el umbral de deslizamiento
            return 0.90f; // Por ejemplo, deslizar al menos la mitad del ancho del elemento
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                View itemView = viewHolder.itemView;

                //Le asignamos el margen que van a tener los iconos (para que queden centrados del lado que les toca
                int iconMargin_delete = (itemView.getHeight() - icon_delete.getIntrinsicHeight()) / 2;
                int iconMargin_detail = (itemView.getHeight() - icon_delete.getIntrinsicHeight()) / 2;

                //Calculamos que el movimiento del item sea de al menos 20% para mostrar el icono (en ambos casos)
                if (Math.abs(dX) > itemView.getWidth() * 0.2f) {
                    if (dX > 0) {
                        //Si el movimiento horizontal es mayor a 0 es porque es hacia la derecha
                        icon_delete.setBounds(
                                itemView.getLeft() + iconMargin_delete,
                                itemView.getTop() + iconMargin_delete,
                                itemView.getLeft() + iconMargin_delete + icon_delete.getIntrinsicWidth(),
                                itemView.getBottom() - iconMargin_delete);
                        icon_delete.draw(c);
                    } else {

                        //Si el movimiento horizontal es menor a 0 es porque es hacia la izquierda
                        icon_detail.setBounds(
                                itemView.getRight() - iconMargin_detail - icon_detail.getIntrinsicWidth(),
                                itemView.getTop() + iconMargin_detail,
                                itemView.getRight() - iconMargin_detail,
                                itemView.getBottom() - iconMargin_detail);
                        icon_detail.draw(c);
                    }
                }

            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    };


}

