package com.example.top_culinary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.top_culinary.R;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.interfaces.OnItemSelectListener;
import com.example.top_culinary.model.Ingrediente;

import java.util.List;

public class AdapterIngredienteBuscador extends RecyclerView.Adapter<AdapterIngredienteBuscador.ItemViewHolder> {
    // Inicializacion de las variables
    private List<Ingrediente> ingredientesBuscadorList;
    private OnItemSelectListener onItemSelectListener;

    public AdapterIngredienteBuscador(List<Ingrediente> ingredientesBuscadorList, OnItemSelectListener listener) {
        this.ingredientesBuscadorList = ingredientesBuscadorList;
        this.onItemSelectListener = listener;
    }

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        this.onItemSelectListener = listener;
    }

    public void setListaIngredientesFiltrados(List<Ingrediente> ingredienteList) {
        this.ingredientesBuscadorList = ingredienteList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterIngredienteBuscador.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingrediente_buscador, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIngredienteBuscador.ItemViewHolder holder, int position) {
        Ingrediente ingrediente = ingredientesBuscadorList.get(position);
        String urlImagen = ingrediente.getImagen();
        Glide.with(holder.itemView.getContext())
                .load(urlImagen)
                .into(holder.imageViewIngredienteBuscador);
        holder.textViewIngredienteBuscador.setText(ingrediente.getNombre());
        // Agregando OnClickListener para cerrar el teclado
        holder.itemView.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) holder.itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm!= null) {
                imm.hideSoftInputFromWindow(holder.itemView.getWindowToken(), 0);
            }
        });
        // Agregando OnClickListener a los botones de compra de los ingredientes y ocultamos el teclado
        holder.buttonAnadirCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) holder.itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null) {
                    imm.hideSoftInputFromWindow(holder.itemView.getWindowToken(), 0);
                }
                if(onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(ingredientesBuscadorList.get(position));
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        recyclerView.setOnTouchListener((v, event) -> {
            // Comprueba si el evento es el inicio de una interacción táctil
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                // Oculta el teclado
                InputMethodManager imm = (InputMethodManager) recyclerView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm!= null) {
                    imm.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return ingredientesBuscadorList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        // Declaracion de las variables
        private DBHandler dbHandler;
        // Declaracion de los widgets
        CardView cardViewIngredienteBuscador;
        ImageView imageViewIngredienteBuscador;
        TextView textViewIngredienteBuscador;
        ImageButton buttonAnadirCarrito;

        public ItemViewHolder (@NonNull View itemView) {
            super(itemView);
            // Inicializacion de las variables
            dbHandler = new DBHandler(itemView.getContext());
            // Inicializacion de los widgets
            cardViewIngredienteBuscador = itemView.findViewById(R.id.cardViewIngredienteBuscador);
            imageViewIngredienteBuscador = itemView.findViewById(R.id.imageViewIngredienteBuscador);
            textViewIngredienteBuscador = itemView.findViewById(R.id.textViewNombreIngredienteBuscador);
            buttonAnadirCarrito = itemView.findViewById(R.id.imageButtonAnadirCarrito);
        }
    }
}
