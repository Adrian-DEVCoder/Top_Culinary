package com.example.top_culinary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.top_culinary.R;
import com.example.top_culinary.model.Mensaje;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<AdapterMensajes.ViewHolder> {

    private static final int VIEW_TYPE_ENVIADO = 1;
    private static final int VIEW_TYPE_RECIBIDO = 2;

    private List<Mensaje> mensajes;
    private String currentUserId;

    public AdapterMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
        this.currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public int getItemViewType(int position) {
        Mensaje mensaje = mensajes.get(position);
        if (mensaje.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_ENVIADO;
        } else {
            return VIEW_TYPE_RECIBIDO;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ENVIADO:
                View viewEnvio = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje_enviado, parent, false);
                return new ViewHolder(viewEnvio);
            case VIEW_TYPE_RECIBIDO:
                View viewRecibo = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje_recibido, parent, false);
                return new ViewHolder(viewRecibo);
            default:
                throw new IllegalArgumentException("Tipo de vista desconocido");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mensaje mensaje = mensajes.get(position);
        holder.bind(mensaje);
    }

    @Override
    public int getItemCount() {
        return mensajes.size();
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes.clear();
        this.mensajes.addAll(mensajes);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.text_message_body);
        }
        public void bind(Mensaje mensaje) {
            tvContent.setText(mensaje.getContent());
        }
    }
}
