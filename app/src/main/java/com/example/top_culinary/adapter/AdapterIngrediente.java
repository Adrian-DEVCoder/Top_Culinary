package com.example.top_culinary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class AdapterIngrediente extends RecyclerView.Adapter<AdapterIngrediente.ItemViewHolder> {
    // Inicializamos las variables
    private List<Ingrediente> ingredientesList;
    private OnItemSelectListener onItemSelectListener;

    public AdapterIngrediente(List<Ingrediente> ingredientesList, OnItemSelectListener listener) {
        this.ingredientesList = ingredientesList;
        this.onItemSelectListener = listener;
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @NonNull
    @Override
    public AdapterIngrediente.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingrediente_cesta,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterIngrediente.ItemViewHolder holder, int position) {
        Ingrediente ingrediente = ingredientesList.get(position);
        String urlImagen = ingrediente.getImagen();
        Glide.with(holder.itemView.getContext())
                .load(urlImagen)
                .into(holder.imageViewIngrediente);
        holder.textViewNombreIngrediente.setText(ingrediente.getNombre());
        holder.textViewPrecioIngrediente.setText(ingrediente.getPrecio()+"€");
        // Establece un onClickListener al boton de añadir al carrito para añadir el ingrediente seleccionado al carrito
        holder.buttonComprarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(ingredientesList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingredientesList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        // Declaracion de las variables
        private DBHandler dbHandler;
        // Declaracion de los widgets
        CardView cardViewIngrediente;
        LinearLayout linearLayoutIngrediente;
        ImageView imageViewIngrediente;
        TextView textViewNombreIngrediente;
        TextView textViewPrecioIngrediente;
        Button buttonComprarIngrediente;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializacion de las variables
            dbHandler = new DBHandler(itemView.getContext());
            // Inicializacion de los widgets
            cardViewIngrediente = itemView.findViewById(R.id.cardViewIngrediente);
            linearLayoutIngrediente = itemView.findViewById(R.id.linearLayoutIngrediente);
            imageViewIngrediente = itemView.findViewById(R.id.imageViewIngrediente);
            textViewNombreIngrediente = itemView.findViewById(R.id.textViewNombreIngrediente);
            textViewPrecioIngrediente = itemView.findViewById(R.id.textViewPrecioIngrediente);
            buttonComprarIngrediente = itemView.findViewById(R.id.buttonComprarIngrediente);
        }

    }
}
