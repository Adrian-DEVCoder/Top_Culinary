package com.example.top_culinary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.top_culinary.R;
import com.example.top_culinary.cesta.CarritoActivity;
import com.example.top_culinary.model.Ingrediente;

import java.util.List;

public class AdapterIngredientesComprados extends RecyclerView.Adapter<AdapterIngredientesComprados.ItemViewHolder> {
    // Declaracion de las variables
    private List<Ingrediente> ingredientesCompradosList;
    private CarritoActivity carritoActivity;
    private OnItemChangeListener onItemChangeListener;

    public AdapterIngredientesComprados(List<Ingrediente> ingredientesCompradosList, CarritoActivity carritoActivity) {
        this.ingredientesCompradosList = ingredientesCompradosList;
        this.carritoActivity = carritoActivity;
    }

    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener) {
        this.onItemChangeListener = onItemChangeListener;
    }

    @NonNull
    @Override
    public AdapterIngredientesComprados.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIngredientesComprados.ItemViewHolder holder, int position) {
        Ingrediente ingrediente = ingredientesCompradosList.get(position);
        String urlImagen = ingrediente.getImagen();
        Glide.with(holder.itemView.getContext())
                .load(urlImagen)
                .into(holder.imageViewItem);
        holder.textViewNombre.setText(ingrediente.getNombre());
        holder.textViewPrecio.setText(ingrediente.getPrecio() + "€");
        holder.textViewCantidad.setText(String.valueOf(ingrediente.getCantidad()));
        holder.buttonEliminar.setOnClickListener(v -> {
            ingredientesCompradosList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, ingredientesCompradosList.size());
            if (onItemChangeListener != null) {
                onItemChangeListener.onItemChange();
            }
        });
        // Listener del boton de incrementar la cantidad
        holder.buttonIncrementar.setOnClickListener(v -> {
            int index = holder.getAdapterPosition();
            if (index != RecyclerView.NO_POSITION) {
                Ingrediente ingredienteActualizado = ingredientesCompradosList.get(index);
                ingredienteActualizado.setCantidad(ingredienteActualizado.getCantidad() + 1);
                notifyItemChanged(index);
                if (onItemChangeListener != null) {
                    onItemChangeListener.onItemChange();
                }
            }
        });

        // Listener del boton de decrementar la cantidad
        holder.buttonDecrementar.setOnClickListener(v -> {
            int index = holder.getAdapterPosition();
            if (index != RecyclerView.NO_POSITION) {
                Ingrediente ingredienteActualizado = ingredientesCompradosList.get(index);
                if (ingredienteActualizado.getCantidad() > 1) {
                    ingredienteActualizado.setCantidad(ingredienteActualizado.getCantidad() - 1);
                    notifyItemChanged(index);
                    if (onItemChangeListener != null) {
                        onItemChangeListener.onItemChange();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientesCompradosList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        // Declaracion de las variables
        // private DBHandler dbHandler;  // No parece necesario
        // Declaracion de los widgets
        LinearLayout linearLayoutItem;
        ImageView imageViewItem;
        TextView textViewNombre;
        TextView textViewPrecio;
        ImageButton buttonEliminar;
        ImageButton buttonDecrementar;
        TextView textViewCantidad;
        ImageButton buttonIncrementar;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializacion de los widgets
            linearLayoutItem = itemView.findViewById(R.id.item_layout);
            imageViewItem = itemView.findViewById(R.id.imagen_item);
            textViewNombre = itemView.findViewById(R.id.nombre_item);
            textViewPrecio = itemView.findViewById(R.id.precio_item);
            buttonEliminar = itemView.findViewById(R.id.delete_button);
            buttonDecrementar = itemView.findViewById(R.id.decrement_button);
            textViewCantidad = itemView.findViewById(R.id.item_quantity);
            buttonIncrementar = itemView.findViewById(R.id.increment_button);
        }
    }

    public interface OnItemChangeListener {
        void onItemChange();
    }
}
