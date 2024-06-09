package com.example.top_culinary.perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.top_culinary.R;
import com.example.top_culinary.cesta.CestaActivity;
import com.example.top_culinary.chat.MensajesActivity;
import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.chat.ChatActivity;
import com.example.top_culinary.model.Usuario;
import com.example.top_culinary.recetas.AniadirRecetasActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class PerfilActivity extends AppCompatActivity {
    // Declaración de las variables
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private boolean esUsuarioActual = true;
    private String uidUsuarioActividad;
    private String uidUsuarioSesion;
    private String nombreFormateado;
    private Usuario usuario;
    private ListenerRegistration listenerRegistration;
    private ListenerRegistration seguidorListenerRegistration;

    // Declaración de los widgets
    private TextView textViewNomUsuario, textViewNumSeguidores, textViewNumSeguidos, textViewNumRecetas;
    private ImageButton buttonPerfil, buttonAjustes, buttonEnviarMensaje, buttonAniadirRecetas, buttonCesta, buttonCocina, buttonForo;
    private Button buttonSeguir;
    private RecyclerView recyclerViewRecetas;
    private ImageView imageViewPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        // Obtencion del usuario actualmente en la sesión
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        uidUsuarioSesion = currentUser != null ? currentUser.getUid() : null;

        // Obtiene el nombre del usuario del intent
        if (getIntent().getParcelableExtra("usuario") != null) {
            usuario = getIntent().getParcelableExtra("usuario");
            nombreFormateado = usuario.getDisplay_name();
        } else {
            nombreFormateado = getIntent().getStringExtra("nombreFormateado");
            if (nombreFormateado == null) {
                Log.e("PerfilActivity", "nombreFormateado no recibido.");
                return;
            }
        }
        // Inicializacion de los widgets
        textViewNomUsuario = findViewById(R.id.textViewNomUsuario);
        textViewNumSeguidores = findViewById(R.id.textViewNumSeguidores);
        textViewNumSeguidos = findViewById(R.id.textViewNumSeguidos);
        textViewNumRecetas = findViewById(R.id.textViewNumRecetas);
        buttonAjustes = findViewById(R.id.imageButtonAjustes);
        buttonAniadirRecetas = findViewById(R.id.imgBRecetas);
        buttonCesta = findViewById(R.id.imgBCesta);
        buttonCocina = findViewById(R.id.imgBCocina);
        buttonForo = findViewById(R.id.imgBForo);
        buttonPerfil = findViewById(R.id.imgBPerfil);
        buttonSeguir = findViewById(R.id.buttonSeguir);
        buttonEnviarMensaje = findViewById(R.id.imageButtonEnviarMensaje);
        recyclerViewRecetas = findViewById(R.id.recyclerViewRecetas);
        imageViewPerfil = findViewById(R.id.imageViewAvatar);
        // Obtención del uid del usuario actual
        obtenerUidActividad(nombreFormateado).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    uidUsuarioActividad = task.getResult();
                    if (uidUsuarioSesion.equals(uidUsuarioActividad)) {
                        buttonSeguir.setVisibility(View.GONE);
                        buttonEnviarMensaje.setVisibility(View.GONE);
                        buttonAjustes.setVisibility(View.VISIBLE);
                    } else {
                        buttonAjustes.setVisibility(View.GONE);
                        buttonEnviarMensaje.setVisibility(View.VISIBLE);
                        buttonSeguir.setVisibility(View.VISIBLE);
                    }
                    verificarSeguido();
                    listenerRegistration = firestoreDB.collection("usuarios").document(uidUsuarioActividad)
                            .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        Log.w("PerfilActivity", "Listen failed.", e);
                                        return;
                                    }
                                    if (snapshot != null && snapshot.exists()) {
                                        usuario = snapshot.toObject(Usuario.class);
                                        textViewNomUsuario.setText(nombreFormateado);
                                        List<String> seguidores = usuario.getSeguidores();
                                        List<String> seguidos = usuario.getSeguidos();
                                        List<String> recetasPublicadas = usuario.getRecetasPublicadas();
                                        textViewNumSeguidores.setText(seguidores != null ? String.valueOf(seguidores.size()) : "0");
                                        textViewNumSeguidos.setText(seguidos != null ? String.valueOf(seguidos.size()) : "0");
                                        textViewNumRecetas.setText(recetasPublicadas != null ? String.valueOf(recetasPublicadas.size()) : "0");
                                        String urlImagenUsuario = usuario.getUrlImagenUsuario();
                                        if (urlImagenUsuario != null && !urlImagenUsuario.isEmpty()) {
                                            Glide.with(PerfilActivity.this)
                                                    .load(urlImagenUsuario)
                                                    .placeholder(R.drawable.avatar)
                                                    .error(R.drawable.avatar)
                                                    .into(imageViewPerfil);
                                        } else {
                                            imageViewPerfil.setImageResource(R.drawable.avatar);
                                        }
                                    }
                                }
                            });
                }
            }
        });

        // Listener de los botones inferiores
        buttonAjustes.setOnClickListener(v -> iniciarAjustes(nombreFormateado));
        buttonAniadirRecetas.setOnClickListener(v -> iniciarAniadirRecetas(nombreFormateado));
        buttonCesta.setOnClickListener(v -> iniciarCesta(nombreFormateado));
        buttonCocina.setOnClickListener(v -> iniciarCocina(nombreFormateado));
        buttonForo.setOnClickListener(v -> iniciarForo(nombreFormateado));
        buttonSeguir.setOnClickListener(v -> seguirUsuario());
        buttonEnviarMensaje.setOnClickListener(v -> iniciarEnviarMensajes(usuario));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
        if (seguidorListenerRegistration != null) {
            seguidorListenerRegistration.remove();
        }
    }

    private void verificarSeguido() {
        String uidUsuarioSesionActual = uidUsuarioSesion;
        String uidUsuarioSeguido = uidUsuarioActividad;
        firestoreDB.collection("usuarios")
                .document(uidUsuarioSesionActual)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<String> seguidos = (List<String>) document.get("seguidos");
                            if (seguidos != null && seguidos.contains(uidUsuarioSeguido)) {
                                buttonSeguir.setText("Siguiendo");
                            } else {
                                buttonSeguir.setText("Seguir");
                            }
                        } else {
                            Log.e("PerfilActivity", "Error al obtener el documento del usuario", task.getException());
                        }
                    } else {
                        Log.e("PerfilActivity", "Error al obtener el documento del usuario", task.getException());
                    }
                });
    }

    private Task<String> obtenerUidActividad(String nombreUsuario) {
        return firestoreDB.collection("usuarios")
                .whereEqualTo("display_name", nombreUsuario)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            return document.getId();
                        } else {
                            return null;
                        }
                    } else {
                        throw new Exception("Error al obtener el uid del usuario");
                    }
                });
    }

    private void seguirUsuario() {
        String uidUsuarioSesionActual = uidUsuarioSesion;
        String uidUsuarioSeguido = uidUsuarioActividad;
        firestoreDB.collection("usuarios")
                .document(uidUsuarioSesionActual)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            List<String> seguidos = (List<String>) document.get("seguidos");
                            if (seguidos != null) {
                                if (seguidos.contains(uidUsuarioSeguido)) {
                                    firestoreDB.collection("usuarios")
                                            .document(uidUsuarioSesionActual)
                                            .update("seguidos", FieldValue.arrayRemove(uidUsuarioSeguido))
                                            .addOnSuccessListener(unused -> buttonSeguir.setText("Seguir"))
                                            .addOnFailureListener(e -> Log.e("PerfilActivity", "Error al dejar de seguir al usuario", e));
                                    firestoreDB.collection("usuarios")
                                            .document(uidUsuarioSeguido)
                                            .update("seguidores", FieldValue.arrayRemove(uidUsuarioSesionActual));
                                } else {
                                    firestoreDB.collection("usuarios")
                                            .document(uidUsuarioSesionActual)
                                            .update("seguidos", FieldValue.arrayUnion(uidUsuarioSeguido))
                                            .addOnSuccessListener(unused -> buttonSeguir.setText("Siguiendo"))
                                            .addOnFailureListener(e -> Log.e("PerfilActivity", "Error al seguir al usuario", e));
                                    firestoreDB.collection("usuarios")
                                            .document(uidUsuarioSeguido)
                                            .update("seguidores", FieldValue.arrayUnion(uidUsuarioSesionActual));
                                }
                            }
                        } else {
                            Log.e("PerfilActivity", "Error al obtener el documento del usuario", task.getException());
                        }
                    } else {
                        Log.e("PerfilActivity", "Error al obtener el documento del usuario", task.getException());
                    }
                });
    }

    private void iniciarEnviarMensajes(Usuario usuario) {
        Intent intent = new Intent(this, MensajesActivity.class);
        intent.putExtra("uidUsuarioActividad", usuario.getUid());
        intent.putExtra("nombreUsuario", usuario.getDisplay_name());
        intent.putExtra("urlImagenUsuario", usuario.getUrlImagenUsuario());
        startActivity(intent);
        finish();
    }

    private void iniciarAjustes(String nombreFormateado) {
        Intent intent = new Intent(this, AjustesActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    private void iniciarForo(String nombreFormateado) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}
