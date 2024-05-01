package com.example.top_culinary.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.top_culinary.R;
import com.example.top_culinary.cocina.DetallesRecetaActivity;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Receta;

import java.util.List;

public class AdapterReceta extends RecyclerView.Adapter<AdapterReceta.ItemViewHolder> {
    // Inicializamos las variables
    private List<Receta> recetaList;
    /**
     * Constructor
     * @param recetaList lista de las recetas
     */
    public AdapterReceta(List<Receta> recetaList) {
        this.recetaList = recetaList;
    }

    public void setListaRecetasFiltradas(List<Receta> listaRecetas){
        this.recetaList = listaRecetas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterReceta.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receta, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReceta.ItemViewHolder holder, int position) {
        Receta receta = recetaList.get(position);
        String urlImagen = receta.getImagen();
        Glide.with(holder.itemView.getContext())
                .load(urlImagen)
                .into(holder.imageViewReceta);
        holder.textViewNombre.setText(receta.getTitulo());
        holder.textViewTiempo.setText(receta.getTiempoTotal());
        holder.textViewTipoPlato.setText(receta.getTipoPlato());
    }

    /**
     * Obtenemos el tamaño de la lista de recetas
     * @return Tamaño de la lista
     */
    @Override
    public int getItemCount() {
        return recetaList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Declaracion de las variables
        private DBHandler dbHandler;
        // Declaracion de los widgets
        CardView cardViewReceta;
        LinearLayout linearLayoutReceta;
        LinearLayout linearLayoutDetallesGeneral;
        LinearLayout linearLayoutDetalles;
        LinearLayout linearLayoutTiempo;
        LinearLayout linearLayoutTipoPlato;
        ImageView imageViewReceta;
        ImageView imageViewTiempo;
        ImageView imageViewTipoPlato;
        TextView textViewNombre;
        TextView textViewTiempo;
        TextView textViewTipoPlato;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewReceta = itemView.findViewById(R.id.cardReceta);
            linearLayoutReceta = itemView.findViewById(R.id.llReceta);
            linearLayoutDetalles = itemView.findViewById(R.id.llDetalles);
            linearLayoutDetallesGeneral = itemView.findViewById(R.id.llDetallesGeneral);
            linearLayoutTiempo = itemView.findViewById(R.id.llTiempo);
            linearLayoutTipoPlato = itemView.findViewById(R.id.llTipoPlato);
            imageViewReceta = itemView.findViewById(R.id.imgReceta);
            imageViewTiempo = itemView.findViewById(R.id.imgTiempo);
            imageViewTipoPlato = itemView.findViewById(R.id.imgTipoPlato);
            textViewNombre = itemView.findViewById(R.id.txvNombreReceta);
            textViewTiempo = itemView.findViewById(R.id.txvTiempo);
            textViewTipoPlato = itemView.findViewById(R.id.txvTipoPlato);
            dbHandler = new DBHandler(itemView.getContext());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION){
                String nomReceta = textViewNombre.getText().toString();
                obtenerReceta(nomReceta);
            }
        }

        private void obtenerReceta(String nombreReceta) {
            Intent intent = new Intent(itemView.getContext(), DetallesRecetaActivity.class);
            intent.putExtra("nombreReceta",nombreReceta);
            itemView.getContext().startActivity(intent);
        }
    }
}
