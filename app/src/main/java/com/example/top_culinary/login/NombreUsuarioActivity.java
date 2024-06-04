package com.example.top_culinary.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.example.top_culinary.R;
import com.example.top_culinary.cocina.CocinaActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NombreUsuarioActivity extends AppCompatActivity {
    // Declaración de las variables
    private FirebaseFirestore firestoreDB;
    // Declaración de los widgets
    private TextInputEditText editTextNombre;
    private String inicioDeSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre_usuario);
        // Obtención del tipo de inicio de sesión
        inicioDeSesion = getIntent().getStringExtra("inicioDeSesion");
        // Inicialización de la BD Online
        firestoreDB = FirebaseFirestore.getInstance();
        // Inicialización de los widgets
        editTextNombre = findViewById(R.id.editTextNomUsuario);
        findViewById(R.id.imageButtonConfirmar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = editTextNombre.getText().toString().trim();
                if (!nombreUsuario.isEmpty()) {
                    verificarNombreUsuario(inicioDeSesion, nombreUsuario);
                } else {
                    Toast.makeText(NombreUsuarioActivity.this, "Por favor, introduce tu nombre de usuario.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para verificar si existe el nombre de usuario, en caso contrario agregamos al usuario
    private void verificarNombreUsuario(String inicioDeSesion, String nombreUsuario) {
        firestoreDB.collection("usuarios")
                .whereEqualTo("display_name", nombreUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                Toast.makeText(NombreUsuarioActivity.this, "El nombre de usuario ya existe, introduce otro por favor", Toast.LENGTH_SHORT).show();
                            } else {
                                agregarUsuarioFirestore(inicioDeSesion, nombreUsuario);
                            }
                        } else {
                            Log.e("Firestore", "Error al verificar el nombre de usuario", task.getException());
                            Toast.makeText(NombreUsuarioActivity.this, "Error al verificar el nombre de usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Método para agregar el usuario a Firestore
    private void agregarUsuarioFirestore(String inicioDeSesion, String nombreUsuario) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String email = currentUser.getEmail();
            String tipoInicioDeSesion = (inicioDeSesion == null) ? "Google" : "Custom";
            // Insertamos los datos del usuario
            Map<String, Object> usuario = new HashMap<>();
            usuario.put("display_name", nombreUsuario);
            usuario.put("email", email);
            usuario.put("tipo_inicio_de_sesion", tipoInicioDeSesion);
            usuario.put("tema_favorito", "Default");
            usuario.put("seguidores", new ArrayList<String>());
            usuario.put("seguidos", new ArrayList<String>());
            usuario.put("recetas", new ArrayList<String>());
            usuario.put("recetasPublicadas", new ArrayList<String>());
            usuario.put("chats", new ArrayList<String>());

            firestoreDB.collection("usuarios").document(uid).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("Firestore", "El usuario ya existe en Firestore");
                                } else {
                                    firestoreDB.collection("usuarios").document(uid).set(usuario)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("Firestore", "Usuario agregado correctamente");
                                                    startActivity(new Intent(NombreUsuarioActivity.this, CocinaActivity.class));
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("Firestore", "Error al agregar al usuario", e);
                                                }
                                            });
                                }
                            } else {
                                Log.d("Firestore", "Error al obtener el documento del usuario", task.getException());
                            }
                        }
                    });
        } else {
            Log.w("Firestore", "Usuario no autenticado");
        }
    }
}
