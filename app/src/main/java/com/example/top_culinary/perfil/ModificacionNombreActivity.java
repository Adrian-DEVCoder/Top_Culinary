package com.example.top_culinary.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.top_culinary.R;
import com.example.top_culinary.model.Dialogo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ModificacionNombreActivity extends AppCompatActivity {
    // Declaración de las variables
    private FirebaseFirestore firestoreDB;
    private String nombreFormateado;

    // Declaración de los widgets
    private EditText editTextNombreUsuario;
    private Button buttonConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificacion_nombre);

        // Inicialización del Firestore
        firestoreDB = FirebaseFirestore.getInstance();

        // Obtención del nombre del usuario a través del intent
        nombreFormateado = getIntent().getStringExtra("nombreFormateado");

        // Inicialización de los widgets
        inicializarWidgets();

        // Configuración de los listeners
        configurarListeners();
    }

    private void inicializarWidgets() {
        editTextNombreUsuario = findViewById(R.id.editTextNombreUsuario);
        buttonConfirmar = findViewById(R.id.buttonConfirmar);

        findViewById(R.id.imageButtonAtras).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresarAjustes();
            }
        });
    }

    private void configurarListeners() {
        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarActualizarNombreUsuario();
            }
        });
    }

    private void validarActualizarNombreUsuario() {
        String nuevoNomUsuario = editTextNombreUsuario.getText().toString().trim();
        if (nuevoNomUsuario.isEmpty()) {
            mostrarDialogo("Error","Por favor, introduce un nombre de usuario válido.");
            return;
        }
        verificarNombreUsuarioRegistrado(nuevoNomUsuario);
    }

    private void verificarNombreUsuarioRegistrado(String nuevoNomUsuario) {
        firestoreDB.collection("usuarios")
                .whereEqualTo("display_name", nuevoNomUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                mostrarDialogo("Error","El nombre de usuario ya está registrado.");
                            } else {
                                actualizarNombreUsuario(nuevoNomUsuario);
                            }
                        } else {
                            mostrarDialogo("Error","Error al verificar el nombre de usuario.");
                        }
                    }
                });
    }

    private void actualizarNombreUsuario(String nuevoNomUsuario) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            firestoreDB.collection("usuarios")
                    .document(currentUser.getUid())
                    .update("display_name", nuevoNomUsuario)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mostrarDialogo("Operacion Exitosa","Nombre de usuario actualizado correctamente.");
                                regresarAjustes(nuevoNomUsuario);
                            } else {
                                mostrarDialogo("Error","Error al actualizar el nombre de usuario.");
                            }
                        }
                    });
        }
    }

    private void regresarAjustes() {
        Intent intent = new Intent(ModificacionNombreActivity.this, AjustesActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        finish();
    }

    private void regresarAjustes(String nuevoNomUsuario) {
        Intent intent = new Intent(ModificacionNombreActivity.this, AjustesActivity.class);
        intent.putExtra("nombreFormateado", nuevoNomUsuario);
        startActivity(intent);
        finish();
    }

    private void mostrarDialogo(String titulo, String contenido) {
        Dialogo.showDialog(this, titulo, contenido);
    }
}
