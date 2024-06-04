package com.example.top_culinary.perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.top_culinary.R;
import com.example.top_culinary.cesta.CestaActivity;
import com.example.top_culinary.chat.MensajesActivity;
import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.chat.ChatActivity;
import com.example.top_culinary.model.Usuario;
import com.example.top_culinary.recetas.AniadirRecetasActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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
    // Declaracion de las variables
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private boolean esUsuarioActual = true;
    private String uidUsuarioActividad;
    private String uidUsuarioSesion;
    private String nombreFormateado;
    private Usuario usuario;
    private ListenerRegistration listenerRegistration;
    private ListenerRegistration seguidorListenerRegistration;

    // Declaracion de los widgets
    TextView textViewNomUsuario;
    ImageButton buttonAjustes;
    ImageView imageViewAvatar;
    TextView textViewSeguidores;
    TextView textViewNumSeguidores;
    View viewLineaDivisora;
    TextView textViewSeguidos;
    TextView textViewNumSeguidos;
    TextView textViewNumRecetas;
    TextView textViewRecetasPublicadas;
    Button buttonSeguir;
    ImageButton buttonEnviarMensaje;
    ImageButton buttonAniadirRecetas;
    ImageButton buttonCesta;
    ImageButton buttonCocina;
    ImageButton buttonForo;
    LinearLayout linearLayoutBotoneraInferior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // Obtencion del usuario actualmente en la sesion
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        uidUsuarioSesion = currentUser.getUid();

        // Obtenemos el nombre del usuario del intent
        if (getIntent().getParcelableExtra("usuario") != null) {
            usuario = getIntent().getParcelableExtra("usuario");
            nombreFormateado = usuario.getDisplay_name();
        } else {
            nombreFormateado = getIntent().getStringExtra("nombreFormateado");
        }

        // Inicializacion de los widgets
        textViewNomUsuario = findViewById(R.id.textViewNomUsuario);
        buttonAjustes = findViewById(R.id.imageButtonAjustes);
        textViewNomUsuario.setText(nombreFormateado);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        imageViewAvatar.setImageResource(R.drawable.avatar);
        textViewSeguidores = findViewById(R.id.textViewSeguidores);
        textViewNumSeguidores = findViewById(R.id.textViewNumSeguidores);
        viewLineaDivisora = findViewById(R.id.viewLineaDivisora);
        textViewSeguidos = findViewById(R.id.textViewSeguidos);
        textViewNumSeguidos = findViewById(R.id.textViewNumSeguidos);
        textViewNumRecetas = findViewById(R.id.textViewNumRecetas);
        textViewRecetasPublicadas = findViewById(R.id.textViewRecetas);
        buttonSeguir = findViewById(R.id.buttonSeguir);
        buttonEnviarMensaje = findViewById(R.id.imageButtonEnviarMensaje);
        buttonAniadirRecetas = findViewById(R.id.imgBRecetas);
        buttonCesta = findViewById(R.id.imgBCesta);
        buttonCocina = findViewById(R.id.imgBCocina);
        buttonForo = findViewById(R.id.imgBForo);
        linearLayoutBotoneraInferior = findViewById(R.id.linearLayoutBotoneraInferior);

        // Obtencion del uid del usuario actual
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
                                        List<String> seguidores = (List<String>) snapshot.get("seguidores");
                                        List<String> seguidos = (List<String>) snapshot.get("seguidos");
                                        List<String> recetasPublicadas = (List<String>) snapshot.get("recetasPublicadas");

                                        textViewNumSeguidores.setText(seguidores != null ? String.valueOf(seguidores.size()) : "0");
                                        textViewNumSeguidos.setText(seguidos != null ? String.valueOf(seguidos.size()) : "0");
                                        textViewNumRecetas.setText(recetasPublicadas != null ? String.valueOf(recetasPublicadas.size()) : "0");
                                    }
                                }
                            });
                }
            }
        });

        // Listener de los botones inferiores
        buttonAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarAjustes(nombreFormateado);
            }
        });
        buttonAniadirRecetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarAniadirRecetas(nombreFormateado);
            }
        });
        buttonCesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCesta(nombreFormateado);
            }
        });
        buttonCocina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCocina(nombreFormateado);
            }
        });
        buttonForo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarForo(nombreFormateado);
            }
        });
        buttonSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uidUsuarioSesionActual = uidUsuarioSesion;
                String uidUsuarioSeguido = uidUsuarioActividad;
                firestoreDB.collection("usuarios")
                        .document(uidUsuarioSesionActual)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        List<String> seguidos = (List<String>) document.get("seguidos");
                                        if (seguidos != null) {
                                            if (seguidos.contains(uidUsuarioSeguido)) {
                                                firestoreDB.collection("usuarios")
                                                        .document(uidUsuarioSesionActual)
                                                        .update("seguidos", FieldValue.arrayRemove(uidUsuarioSeguido))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                buttonSeguir.setText("Seguir");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e("PerfilActivity", "Error al dejar de seguir al usuario", e);
                                                            }
                                                        });
                                                firestoreDB.collection("usuarios")
                                                        .document(uidUsuarioSeguido)
                                                        .update("seguidores", FieldValue.arrayRemove(uidUsuarioSesionActual));
                                            } else {
                                                firestoreDB.collection("usuarios")
                                                        .document(uidUsuarioSesionActual)
                                                        .update("seguidos", FieldValue.arrayUnion(uidUsuarioSeguido))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                buttonSeguir.setText("Siguiendo");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e("PerfilActivity", "Error al seguir al usuario", e);
                                                            }
                                                        });
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
                            }
                        });
            }
        });
        buttonEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarEnviarMensajes(uidUsuarioActividad);
            }
        });
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
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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
                    }
                });
    }

    private Task<String> obtenerUidActividad(String nombreUsuario) {
        return firestoreDB.collection("usuarios")
                .whereEqualTo("display_name", nombreUsuario)
                .get()
                .continueWith(new Continuation<QuerySnapshot, String>() {
                    @Override
                    public String then(@NonNull Task<QuerySnapshot> task) throws Exception {
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
                    }
                });
    }

    private void iniciarEnviarMensajes(String uidUsuarioActividad) {
        Intent intent = new Intent(this, MensajesActivity.class);
        intent.putExtra("uidUsuarioActividad", uidUsuarioActividad);
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
