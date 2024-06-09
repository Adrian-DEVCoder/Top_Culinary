package com.example.top_culinary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.top_culinary.R;
import com.example.top_culinary.model.Chat;

import java.util.List;

public class AdapterChatsUsuario extends RecyclerView.Adapter<AdapterChatsUsuario.ChatViewHolder> {

    private Context context;
    private List<Chat> chats;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Chat chat);
    }

    public AdapterChatsUsuario(Context context, List<Chat> chats, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.chats = chats;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Chat chat = chats.get(position);
        holder.nombreUsuario.setText(chat.getNombreUsuario());
        holder.ultimoMensaje.setText(chat.getUltimoMensaje());
        if (chat.getAvatarUrl() != null && !chat.getAvatarUrl().isEmpty()) {
            Glide.with(context).load(chat.getAvatarUrl()).into(holder.avatar);
        } else {
            holder.avatar.setImageResource(R.drawable.avatar);
        }
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(chat));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void filterList(List<Chat> filteredChats) {
        this.chats = filteredChats;
        notifyDataSetChanged();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView nombreUsuario, ultimoMensaje;
        ImageView avatar;
        public ChatViewHolder(View itemView) {
            super(itemView);
            nombreUsuario = itemView.findViewById(R.id.textViewNombreUsuario);
            ultimoMensaje = itemView.findViewById(R.id.textViewUltimoMensaje);
            avatar = itemView.findViewById(R.id.imageViewAvatar);
        }
    }
}
