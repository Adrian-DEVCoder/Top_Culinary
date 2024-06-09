package com.example.top_culinary.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.top_culinary.R;
import com.example.top_culinary.model.Usuario;
import com.example.top_culinary.perfil.PerfilActivity;

import java.util.ArrayList;
import java.util.List;

public class AdapterBuscadorUsuarios extends RecyclerView.Adapter<AdapterBuscadorUsuarios.ViewHolder> {
    private List<Usuario> usuarios;
    private Context context;

    // Constructor
    public AdapterBuscadorUsuarios(List<Usuario> usuarios, Context context) {
        this.usuarios = usuarios != null ? usuarios : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buscar_usuario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Usuario usuario = usuarios.get(position);
        holder.textViewNombreUsuario.setText(usuario.getDisplay_name());
        if (usuario.getUrlImagenUsuario() != null && !usuario.getUrlImagenUsuario().isEmpty()) {
            Glide.with(context)
                    .load(usuario.getUrlImagenUsuario())
                    .placeholder(R.drawable.avatar)
                    .into(holder.imageViewUsuario);
        } else {
            holder.imageViewUsuario.setImageResource(R.drawable.avatar);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PerfilActivity.class);
            intent.putExtra("usuario", usuario);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    public void actualizarUsuarios(List<Usuario> usuariosNuevos, String nombreUsuario) {
        if (usuariosNuevos == null) {
            usuariosNuevos = new ArrayList<>();
        }
        List<Usuario> usuariosFiltrados = new ArrayList<>();
        for (Usuario usuario : usuariosNuevos) {
            if (usuario != null && !usuario.getDisplay_name().equalsIgnoreCase(nombreUsuario)) {
                usuariosFiltrados.add(usuario);
            }
        }
        this.usuarios.clear();
        for(Usuario usuario : usuariosFiltrados) {
            this.usuarios.add(usuario);
        }
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewUsuario;
        public TextView textViewNombreUsuario;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewUsuario = itemView.findViewById(R.id.imageViewFotoPerfil);
            textViewNombreUsuario = itemView.findViewById(R.id.textViewNombreUsuario);
        }
    }
}
