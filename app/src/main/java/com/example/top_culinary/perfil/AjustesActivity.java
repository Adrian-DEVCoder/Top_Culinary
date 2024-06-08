package com.example.top_culinary.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.top_culinary.R;
import com.example.top_culinary.login.LoginActivity;
import com.example.top_culinary.model.Dialogo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AjustesActivity extends AppCompatActivity {
    // Declaración de las variables
    private FirebaseFirestore firestoreDB;
    private String tipoInicioSesion;
    private String nombreFormateado;

    // Declaración de los widgets
    private ImageButton buttonAtras;
    private Button buttonCambiarNombre;
    private Button buttonCambiarContrasena;
    private Button buttonCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        // Inicialización de Firestore
        firestoreDB = FirebaseFirestore.getInstance();

        // Obtención del usuario actual
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            redirigirALogin();
            return;
        }

        // Obtención del nombre del intent
        nombreFormateado = getIntent().getStringExtra("nombreFormateado");

        // Inicialización de los widgets
        inicializarWidgets();

        // Configuración de los listeners
        configurarListeners();

        // Comprobamos el tipo de inicio de sesión
        comprobarInicioSesion(currentUser.getUid());
    }

    private void inicializarWidgets() {
        buttonAtras = findViewById(R.id.imageButtonAtras);
        buttonCambiarNombre = findViewById(R.id.buttonCambiarNombre);
        buttonCambiarContrasena = findViewById(R.id.buttonCambiarContrasena);
        buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion);
    }

    private void configurarListeners() {
        buttonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarPerfil();
            }
        });

        buttonCambiarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCambiarNombre();
            }
        });

        buttonCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCambiarContrasena();
            }
        });

        buttonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
    }

    private void comprobarInicioSesion(String uidUsuario) {
        firestoreDB.collection("usuarios")
                .document(uidUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                tipoInicioSesion = document.getString("tipo_inicio_de_sesion");
                                actualizarUI();
                            }
                        } else {
                            mostrarDialogo("Error","Error al obtener el tipo de inicio de sesión.");
                        }
                    }
                });
    }

    private void actualizarUI() {
        if (tipoInicioSesion != null && tipoInicioSesion.equalsIgnoreCase("Google")) {
            buttonCambiarContrasena.setVisibility(View.GONE);
        } else {
            buttonCambiarContrasena.setVisibility(View.VISIBLE);
        }
    }

    private void iniciarPerfil() {
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void iniciarCambiarNombre() {
        Intent intent = new Intent(AjustesActivity.this, ModificacionNombreActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        finish();
    }

    private void iniciarCambiarContrasena() {
        Intent intent = new Intent(AjustesActivity.this, ModificacionContrasenaActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        finish();
    }

    private void cerrarSesion() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        redirigirALogin();
    }

    private void redirigirALogin() {
        Intent intent = new Intent(AjustesActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void mostrarDialogo(String titulo, String contenido) {
        Dialogo.showDialog(this, titulo, contenido);
    }
}
