package com.example.top_culinary.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.top_culinary.R;
import com.example.top_culinary.adapter.AdapterMensajes;
import com.example.top_culinary.model.Chat;
import com.example.top_culinary.model.Mensaje;
import com.example.top_culinary.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MensajesActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore;
    private AdapterMensajes adapterMensajes;
    private String uidUsuarioRecibidor;
    private String uidUsuarioEnviador;
    private RecyclerView recyclerViewMensajes;
    private EditText editTextMensaje;
    private ImageButton buttonEnviarMensaje;
    private List<Mensaje> mensajes = new ArrayList<>();
    private String chatId;
    private String otherUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajes);

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        uidUsuarioEnviador = currentUser != null ? currentUser.getUid() : null;
        uidUsuarioRecibidor = getIntent().getStringExtra("uidUsuarioActividad");

        recyclerViewMensajes = findViewById(R.id.recyclerViewMensajes);
        recyclerViewMensajes.setLayoutManager(new LinearLayoutManager(this));

        adapterMensajes = new AdapterMensajes(mensajes);
        recyclerViewMensajes.setAdapter(adapterMensajes);

        editTextMensaje = findViewById(R.id.editTextMensaje);
        buttonEnviarMensaje = findViewById(R.id.buttonEnviarMensaje);

        Intent intent = getIntent();
        if (intent.getStringExtra("chatId") != null) {
            chatId = intent.getStringExtra("chatId");
            otherUserId = intent.getStringExtra("otherUserId");
        } else {
            chatId = construirChatId(uidUsuarioEnviador, uidUsuarioRecibidor);
        }

        chatExiste(exists -> {
            if (exists) {
                if(otherUserId != null) {
                    cargarDatosUsuario(otherUserId);
                }
                cargarMensajes();
            } else {
                crearNuevoChat();
            }
        });

        buttonEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contenidoMensaje = editTextMensaje.getText().toString();
                if (!contenidoMensaje.trim().isEmpty()) {
                    enviarNuevoMensaje(contenidoMensaje);
                    editTextMensaje.setText("");
                }
            }
        });
    }

    private void cargarMensajes() {
        if (chatId == null) return;

        CollectionReference mensajesRef = firebaseFirestore.collection("chats").document(chatId).collection("mensajes");
        mensajesRef.orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("MensajesActivity", "Error al cargar mensajes", e);
                return;
            }

            if (queryDocumentSnapshots != null) {
                mensajes.clear();
                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    Mensaje mensaje = doc.toObject(Mensaje.class);
                    mensajes.add(mensaje);
                }
                adapterMensajes.notifyDataSetChanged();
                if (!mensajes.isEmpty() && adapterMensajes.getItemCount() > 0) {
                    int targetPosition = adapterMensajes.getItemCount() - 1;
                    if (targetPosition >= 0 && targetPosition < adapterMensajes.getItemCount()) {
                        recyclerViewMensajes.smoothScrollToPosition(targetPosition);
                    } else {
                        Log.e("MensajesActivity", "Invalid target position: " + targetPosition);
                    }
                }
            }
        });
    }

    private void cargarDatosUsuario(String userId) {
        DocumentReference docRef = firebaseFirestore.collection("usuarios").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Usuario usuario = documentSnapshot.toObject(Usuario.class);
                if (usuario != null) {
                    TextView userName = findViewById(R.id.user_name);
                    ImageView userProfileImage = findViewById(R.id.user_profile_image);
                    userName.setText(usuario.getDisplay_name());
                }
            }
        }).addOnFailureListener(e -> Log.e("MensajesActivity", "Error al cargar datos del usuario", e));
    }

    private String construirChatId(String idUsuario1, String idUsuario2) {
        if (idUsuario1 != null && idUsuario2 != null) {
            return idUsuario1.compareTo(idUsuario2) < 0 ? idUsuario1 + "_" + idUsuario2 : idUsuario2 + "_" + idUsuario1;
        } else {
            return "";
        }
    }

    private void enviarNuevoMensaje(String contenido) {
        if (uidUsuarioEnviador == null) {
            Log.e("Mensajes", "Error: uidUsuarioEnviador es null");
            return;
        }

        Mensaje mensaje = new Mensaje(contenido, uidUsuarioEnviador, System.currentTimeMillis());
        Log.d("Mensaje","Timestamp del Mensaje enviado: " + System.currentTimeMillis());

        firebaseFirestore.collection("chats").document(chatId).collection("mensajes").add(mensaje)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Mensajes", "Mensaje enviado exitosamente.");
                    firebaseFirestore.collection("chats").document(chatId)
                            .update("ultimoMensaje", contenido, "timestamp", System.currentTimeMillis())
                            .addOnSuccessListener(aVoid -> Log.d("Mensajes", "Último mensaje actualizado."))
                            .addOnFailureListener(e -> Log.e("Mensajes", "Error al actualizar último mensaje", e));
                })
                .addOnFailureListener(e -> Log.e("Mensajes", "Error al enviar mensaje", e));
    }

    private void chatExiste(CheckChatExistsCallback callback) {
        DocumentReference docRef = firebaseFirestore.document("chats/" + chatId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                callback.onCallback(true);
            } else {
                callback.onCallback(false);
            }
        });
    }

    private interface CheckChatExistsCallback {
        void onCallback(boolean exists);
    }

    private void crearNuevoChat() {
        if (uidUsuarioEnviador == null || uidUsuarioRecibidor == null) {
            Log.e("Mensajes", "Error: uidUsuarioEnviador o uidUsuarioRecibidor es null");
            return;
        }

        List<String> participantIds = Arrays.asList(uidUsuarioEnviador, uidUsuarioRecibidor);

        Chat chat = new Chat();
        chat.setParticipantIds(participantIds);
        chat.setTimestamp(System.currentTimeMillis());

        firebaseFirestore.collection("chats").document(chatId).set(chat)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Mensajes", "Chat creado exitosamente");
                    cargarMensajes();
                })
                .addOnFailureListener(e -> Log.e("Mensajes", "Error al crear chat", e));
    }
}
