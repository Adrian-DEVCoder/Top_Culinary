package com.example.top_culinary.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.example.top_culinary.R;
import com.example.top_culinary.adapter.AdapterChatsUsuario;
import com.example.top_culinary.cesta.CestaActivity;
import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.model.Chat;
import com.example.top_culinary.model.Dialogo;
import com.example.top_culinary.perfil.PerfilActivity;
import com.example.top_culinary.recetas.AniadirRecetasActivity;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private List<Chat> chats = new ArrayList<>();
    private AdapterChatsUsuario adapterChatsUsuario;
    ImageButton buttonBuscarUsuarios;
    RecyclerView recyclerViewChats;
    ImageButton buttonAniadirRecetas;
    ImageButton buttonCesta;
    ImageButton buttonCocina;
    ImageButton buttonPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String nombreFormateado = intent.getStringExtra("nombreFormateado");

        buttonBuscarUsuarios = findViewById(R.id.imageButtonBuscarUsuarios);
        recyclerViewChats = findViewById(R.id.recyclerViewChats);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        cargarChats();

        buttonAniadirRecetas = findViewById(R.id.imgBRecetas);
        buttonCesta = findViewById(R.id.imgBCesta);
        buttonCocina = findViewById(R.id.imgBCocina);
        buttonPerfil = findViewById(R.id.imgBPerfil);

        buttonBuscarUsuarios.setOnClickListener(v -> iniciarBuscarUsuarios(nombreFormateado));
        buttonAniadirRecetas.setOnClickListener(v -> iniciarAniadirRecetas(nombreFormateado));
        buttonCesta.setOnClickListener(v -> iniciarCesta(nombreFormateado));
        buttonCocina.setOnClickListener(v -> iniciarCocina(nombreFormateado));
        buttonPerfil.setOnClickListener(v -> iniciarPerfil(nombreFormateado));
    }

    private void cargarChats() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Dialogo.showDialog(this, "Error", "No hay un usuario actual seleccionado.");
            return;
        }

        String currentUserId = currentUser.getUid();
        CollectionReference chatCollection = firebaseFirestore.collection("chats");
        Query query = chatCollection.whereArrayContains("participantIds", currentUserId);

        query.addSnapshotListener((snapshots, e) -> {
            if (e != null) {
                Log.e("ChatActivity", "Error al obtener chats de Firebase", e);
                Dialogo.showDialog(this, "Error", "No hemos podido recuperar los chats.");
                return;
            }

            if (snapshots != null && !snapshots.isEmpty()) {
                chats.clear();
                List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                for (DocumentSnapshot document : snapshots) {
                    Chat chat = document.toObject(Chat.class);
                    if (chat != null) {
                        chat.setId(document.getId());
                        String otherUserId = getOtherUserId(chat.getParticipantIds(), currentUserId);
                        if (otherUserId != null) {
                            Task<DocumentSnapshot> userDocTask = firebaseFirestore.collection("usuarios").document(otherUserId).get();
                            userDocTask.addOnSuccessListener(userDocument -> {
                                if (userDocument.exists()) {
                                    String otherUserName = userDocument.getString("display_name");
                                    chat.setNombreUsuario(otherUserName);
                                    chats.add(chat);
                                    Log.d("ChatActivity", "Chat añadido: " + chat.getNombreUsuario());
                                }
                            }).addOnFailureListener(ex -> {
                                Log.e("ChatActivity", "Error al obtener nombre de usuario", ex);
                            });
                            tasks.add(userDocTask);
                        }
                    }
                }

                Tasks.whenAllComplete(tasks).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        updateUI();
                    } else {
                        Log.e("ChatActivity", "Error en las tareas de carga de usuario", task.getException());
                    }
                });
            }
        });
    }

    private void updateUI() {
        if (adapterChatsUsuario == null) {
            adapterChatsUsuario = new AdapterChatsUsuario(ChatActivity.this, chats, chat -> {
                Intent intent = new Intent(ChatActivity.this, MensajesActivity.class);
                intent.putExtra("chatId", chat.getId());
                intent.putExtra("otherUserId", getOtherUserId(chat.getParticipantIds(), firebaseAuth.getCurrentUser().getUid()));
                startActivity(intent);
            });
            recyclerViewChats.setAdapter(adapterChatsUsuario);
        } else {
            adapterChatsUsuario.notifyDataSetChanged();
        }
    }

    private String getOtherUserId(List<String> participantIds, String currentUserId) {
        for (String id : participantIds) {
            if (!id.equals(currentUserId)) {
                return id;
            }
        }
        return null;
    }

    private void iniciarBuscarUsuarios(String nombreFormateado) {
        Intent intent = new Intent(this, BuscadorUsuariosActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void iniciarAniadirRecetas(String nombreFormateado) {
        Intent intent = new Intent(this, AniadirRecetasActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void iniciarCesta(String nombreFormateado) {
        Intent intent = new Intent(this, CestaActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void iniciarCocina(String nombreFormateado) {
        Intent intent = new Intent(this, CocinaActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void iniciarPerfil(String nombreFormateado) {
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
