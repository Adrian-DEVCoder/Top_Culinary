package com.example.top_culinary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.top_culinary.R;
import com.example.top_culinary.cesta.CarritoActivity;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Ingrediente;

import java.util.List;

public class AdapterIngredientesComprados extends RecyclerView.Adapter<AdapterIngredientesComprados.ItemViewHolder> {
    // Declaracion de las variables
    private List<Ingrediente> ingredientesCompradosList;
    private CarritoActivity carritoActivity;

    public AdapterIngredientesComprados(List<Ingrediente> ingredientesCompradosList, CarritoActivity carritoActivity) {
        this.ingredientesCompradosList = ingredientesCompradosList;
        this.carritoActivity = carritoActivity;
    }

    @NonNull
    @Override
    public AdapterIngredientesComprados.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carrito,parent,false);
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
        holder.textViewPrecio.setText(ingrediente.getPrecio()+"€");
        holder.textViewCantidad.setText(String.valueOf(ingrediente.getCantidad() + 1));
        holder.buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientesCompradosList.remove(ingrediente);
                notifyItemRemoved(position);
            }
        });
        // Listener del boton de incrementar la cantidad
        holder.buttonIncrementar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = position;
                if (index != RecyclerView.NO_POSITION) {
                    Ingrediente ingredienteActualizado = ingredientesCompradosList.get(index);
                    ingredienteActualizado.setCantidad(ingredienteActualizado.getCantidad() + 1);
                    notifyItemChanged(index);
                    carritoActivity.actualizarPrecioTotal(ingredientesCompradosList);
                }
            }
        });

        // Listener del boton de decrementar la cantidad
        holder.buttonDecrementar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = position;
                if (index != RecyclerView.NO_POSITION) {
                    if(Integer.parseInt(holder.textViewCantidad.getText().toString()) > 0) {
                        Ingrediente ingredienteActualizado = ingredientesCompradosList.get(index);
                        ingredienteActualizado.setCantidad(ingredienteActualizado.getCantidad() - 1);
                        notifyItemChanged(index);
                        carritoActivity.actualizarPrecioTotal(ingredientesCompradosList);
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
        private DBHandler dbHandler;
        // Declaracion de los widgets
        LinearLayout linearLayoutItem;
        ImageView imageViewItem;
        TextView textViewNombre;
        TextView textViewPrecio;
        ImageButton buttonEliminar;
        ImageButton buttonDecrementar;
        TextView textViewCantidad;
        ImageButton buttonIncrementar;

        public ItemViewHolder (@NonNull View itemView) {
            super(itemView);
            // Inicializacion de las variables
            dbHandler = new DBHandler(itemView.getContext());
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
}
