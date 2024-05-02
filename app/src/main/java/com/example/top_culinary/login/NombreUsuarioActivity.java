package com.example.top_culinary.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NombreUsuarioActivity extends AppCompatActivity {
    // Declaracion de las variables
    private FirebaseFirestore firestoreDB;
    // Declaracion de los widgets
    private TextView textViewNombre;
    private EditText editTextNombre;
    private ImageButton buttonConfirmar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre_usuario);
        // Obtenemos el tipo de inicio de sesion
        Intent intent = getIntent();
        String inicioDeSesion = intent.getStringExtra("inicioDeSesion");
        // Inicializacion de la BD Online
        firestoreDB = FirebaseFirestore.getInstance();
        // Inicializacion de los widgets
        textViewNombre = findViewById(R.id.textViewIntroduceNombre);
        editTextNombre = findViewById(R.id.editTextNomUsuario);
        buttonConfirmar = findViewById(R.id.imageButtonConfirmar);
        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = editTextNombre.getText().toString().trim();
                if(!"".equals(nombreUsuario)){
                    buttonConfirmar.setImageResource(R.drawable.confirmar);
                    verificarNombreUsuario(inicioDeSesion,nombreUsuario);
                } else {
                    buttonConfirmar.setImageResource(R.drawable.no_confirmar);
                    Toast.makeText(NombreUsuarioActivity.this, "Por favor, introduce tu nombre de usuario.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Metodo para verificar si existe el nombre de usuario, en caso contrario agregamos al usuario
    private void verificarNombreUsuario(String inicioDeSesion, String nombreUsuario) {
        firestoreDB.collection("usuarios")
                .whereEqualTo("display_name", nombreUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Verifica si la consulta encontró algún documento
                            if (!task.getResult().isEmpty()) {
                                // El nombre de usuario ya existe
                                Toast.makeText(NombreUsuarioActivity.this, "El nombre de usuario ya existe, introduce otro por favor", Toast.LENGTH_SHORT).show();
                            } else {
                                // El nombre de usuario no existe, procede a agregar al usuario
                                agregarUsuarioFirestore(inicioDeSesion, nombreUsuario);
                            }
                        } else {
                            // Maneja el caso de fallo de la consulta
                            Log.e("Firestore", "Error al verificar el nombre de usuario", task.getException());
                            Toast.makeText(NombreUsuarioActivity.this, "Error al verificar el nombre de usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    // Metodo para agregar el usuario a firestore
    private void agregarUsuarioFirestore(String inicioDeSesion, String nombreUsuario) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!= null) {
            String uid = currentUser.getUid();
            String displayName = nombreUsuario;
            String email = currentUser.getEmail();
            String tipoInicioDeSesion = "Custom";
            // Comprobamos el tipo de inicio de sesion
            if(inicioDeSesion == null){
                tipoInicioDeSesion = "Google";
            }
            String temaFavorito = "Default"; // Hay que implementar esta funcionalidad
            // Insertamos los datos del usuario
            Map<String,Object> usuario = new HashMap<>();
            usuario.put("display_name", displayName);
            usuario.put("email", email);
            usuario.put("tipo_inicio_de_sesion", tipoInicioDeSesion);
            usuario.put("tema_favorito", temaFavorito);
            // Creamos un array vacio tanto para los seguidores como los seguidos
            List<String> seguidores = new ArrayList<>();
            List<String> seguidos = new ArrayList<>();
            // Creamos un array vacio tanto para las recetas como las recetas publicadas
            List<String> recetas = new ArrayList<>();
            List<String> recetasPublicadas = new ArrayList<>();
            // Insertamos las diferentes listas
            usuario.put("seguidores", seguidores);
            usuario.put("seguidos", seguidos);
            usuario.put("recetas", recetas);
            usuario.put("recetasPublicadas", recetasPublicadas);
            // Verificamos si el documento ya existe
            firestoreDB.collection("usuarios").document(uid).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("Firestore", "El usuario ya existe en Firestore");
                                    // El usuario ya existe, no necesitas hacer nada más
                                } else {
                                    // El usuario no existe, procedemos a crear el documento
                                    firestoreDB.collection("usuarios")
                                            .document(uid)
                                            .set(usuario)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("Firestore", "Usuario agregado correctamente");
                                                    Intent intent = new Intent(NombreUsuarioActivity.this, CocinaActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("Firestore", "Error al agregar al usuario",e);
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