package com.example.top_culinary.adapter;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.top_culinary.R;
import com.example.top_culinary.cocina.DetallesRecetaActivity;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Receta;

import java.util.List;

public class AdapterRecetaUsuario extends RecyclerView.Adapter<AdapterRecetaUsuario.ItemViewHolder> {
    // Inicializacion de las variables
    private List<Receta> recetaUsuarioList;

    // Constructor
    public AdapterRecetaUsuario(List<Receta> recetaUsuarioList) {this.recetaUsuarioList = recetaUsuarioList;}

    public void setRecetaUsuarioList(List<Receta> recetaUsuarioList) {
        this.recetaUsuarioList = recetaUsuarioList;
    }

    @NonNull
    @Override
    public AdapterRecetaUsuario.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receta_usuario, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecetaUsuario.ItemViewHolder holder, int position) {
        Receta receta = recetaUsuarioList.get(position);
        holder.imageViewReceta.setImageResource(R.drawable.placeholder);
        holder.textViewNombre.setText(receta.getTitulo());
    }

    @Override
    public int getItemCount() { return recetaUsuarioList.size(); }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Declaracion de las variables
        private DBHandler dbHandler;
        // Declaracion de los widgets
        CardView cardViewReceta;
        LinearLayout linearLayoutReceta;
        LinearLayout linearLayoutDetalles;
        ImageView imageViewReceta;
        TextView textViewNombre;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializacion de la DB
            dbHandler = new DBHandler(itemView.getContext());
            // Inicializacion de los widgets
            cardViewReceta = itemView.findViewById(R.id.cardReceta);
            linearLayoutReceta = itemView.findViewById(R.id.llReceta);
            linearLayoutDetalles = itemView.findViewById(R.id.llDetalles);
            imageViewReceta = itemView.findViewById(R.id.imgReceta);
            textViewNombre = itemView.findViewById(R.id.txvNombreReceta);
            // Listener de los clicks
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if(pos != RecyclerView.NO_POSITION) {
                String nomReceta = textViewNombre.getText().toString();
                obtenerReceta(nomReceta);
            }
        }

        private void obtenerReceta(String nomReceta) {
            Intent intent = new Intent(itemView.getContext(), DetallesRecetaActivity.class);
            intent.putExtra("nombreReceta", nomReceta);
            itemView.getContext().startActivity(intent);
        }
    }
}
