package com.luizinho_dev.comprameste.CustomAdapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comprameste.R;
import com.luizinho_dev.comprameste.Activities.MainActivity;
import com.luizinho_dev.comprameste.Entities.Productos;
import com.luizinho_dev.comprameste.Logica.MainLogica;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class RVAdapterProductos extends RecyclerView.Adapter<RVAdapterProductos.ViewHolder> {
    private ArrayList<Productos> productos;
    private Drawable icon_delete;
    private Drawable icon_edit;

    DecimalFormat formatea = new DecimalFormat("###,###,###.##");

    // Constructor para inicializar el adaptador con la lista de datos
    public RVAdapterProductos(Context context, ArrayList<Productos> productos) {
        this.productos = productos;
        this.icon_delete = ContextCompat.getDrawable(context, R.drawable.ic_icon_delete);
        this.icon_edit = ContextCompat.getDrawable(context, R.drawable.ic_icon_edit);
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
        holder.txtValorUni.setText("$ "+formatea.format(producto.getValorUnitario()));
        holder.txtPorcDesc.setText(String.valueOf(producto.getPorcDesc())+" %");
        holder.txtTotal.setText("$ "+formatea.format(producto.getTotal()));
    }

    @Override
    public int getItemCount() {
        // Devuelve el número total de elementos en la lista de datos
        return productos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNomProd, txtCantidad, txtValorUni, txtPorcDesc, txtTotal, lbCantidad, lbValorUnitario, lbTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Referenciar las vistas dentro de cada elemento del RecyclerView
            txtNomProd = itemView.findViewById(R.id.txtNomProd);
            txtCantidad = itemView.findViewById(R.id.txtCantProd);
            txtValorUni = itemView.findViewById(R.id.txtValorProd);
            txtPorcDesc = itemView.findViewById(R.id.txtPorcDesc);
            txtTotal = itemView.findViewById(R.id.txtTotalProd);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                }
            });


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
                //Si deslizó hacía la derecha realizamos la logica del editar
                if (direction == ItemTouchHelper.LEFT) {
                    // Obtén la posición del elemento que se deslizó
                    int position = viewHolder.getAdapterPosition();
                    Productos producto = productos.get(position);
                    MainActivity.editarProducto(producto);
                } else {
                    //Sino entonces la del eliminar
                    System.out.println("Eliminando producto...");
                    MainLogica mainLogica = new MainLogica();
                    int position = viewHolder.getAdapterPosition();
                    Productos producto = productos.get(position);

                    mainLogica.eliminarProducto(viewHolder.itemView.getContext(), producto);
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

        /**
         * @description             Evento que se ejecuta al mover un item a la derecha o izquierda
         * @param c                 The canvas which RecyclerView is drawing its children
         * @param recyclerView      The RecyclerView to which ItemTouchHelper is attached to
         * @param viewHolder        The ViewHolder which is being interacted by the User or it was
         *                          interacted and simply animating to its original position
         * @param dX                The amount of horizontal displacement caused by user's action
         * @param dY                The amount of vertical displacement caused by user's action
         * @param actionState       The type of interaction on the View. Is either ACTION_STATE_DRAG or ACTION_STATE_SWIPE.
         * @param isCurrentlyActive True if this view is currently being controlled by the user or
         *                          false it is simply animating back to its original state.
         */
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                View itemView = viewHolder.itemView;
                //Le asignamos el margen que van a tener los iconos (para que queden centrados del lado que les toca
                int iconMargin_delete = (itemView.getHeight() - icon_delete.getIntrinsicHeight()) / 2;
                int iconMargin_edit = (itemView.getHeight() - icon_edit.getIntrinsicHeight()) / 2;

                //Calculamos que el movimiento del item sea de al menos 20% para mostrar el icono (en ambos casos)
                if (Math.abs(dX) > itemView.getWidth() * 0.2f) {
                    //Si el movimiento horizontal es mayor a 0 es porque es hacia la derecha
                    if (dX > 0) {
                        icon_delete.setBounds(
                                itemView.getLeft() + iconMargin_delete,
                                itemView.getTop() + iconMargin_delete,
                                itemView.getLeft() + iconMargin_delete + icon_delete.getIntrinsicWidth(),
                                itemView.getBottom() - iconMargin_delete);
                        icon_delete.draw(c);
                    } else {
                        //Si el movimiento horizontal es menor a 0 es porque es hacia la izquierda
                        icon_edit.setBounds(
                                itemView.getRight() - iconMargin_edit - icon_edit.getIntrinsicWidth(),
                                itemView.getTop() + iconMargin_edit,
                                itemView.getRight() - iconMargin_edit,
                                itemView.getBottom() - iconMargin_edit);
                        icon_edit.draw(c);
                    }
                }

            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}
