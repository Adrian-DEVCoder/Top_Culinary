package com.example.top_culinary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.top_culinary.R;
import com.example.top_culinary.model.Chat;
import com.google.api.Context;

import java.util.List;

public class AdapterChatsUsuario extends RecyclerView.Adapter<AdapterChatsUsuario.ViewHolder> {
    private Context context;
    private List<Chat> chatList;

    public AdapterChatsUsuario(Context context, List<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        String urlImagenUsuario = chat.getAvatarUrl();
        Glide.with(holder.itemView.getContext())
                .load(urlImagenUsuario)
                .into(holder.imageViewAvatar);
        holder.textViewNombreUsuario.setText(chat.getNombreUsuario());
        holder.textViewUltimoMensaje.setText(chat.getUltimoMensaje());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    // Agregar un nuevo usuario al adaptador
    public void agregarUsuario(Chat chat) {
        chatList.add(chat);
        notifyItemInserted(chatList.size() - 1);
    }

    // Limpia el adaptador
    public void clear() {
        int size = chatList.size();
        if(size > 0) {
            chatList.clear();
            notifyItemRangeRemoved(0,size);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewAvatar;
        public TextView textViewNombreUsuario;
        public TextView textViewUltimoMensaje;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.imageViewAvatar);
            textViewNombreUsuario = itemView.findViewById(R.id.textViewNombreUsuario);
            textViewUltimoMensaje = itemView.findViewById(R.id.textViewUltimoMensaje);
        }
    }
}
