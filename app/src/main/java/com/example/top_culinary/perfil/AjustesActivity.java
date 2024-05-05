package com.example.top_culinary.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.top_culinary.R;
import com.example.top_culinary.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AjustesActivity extends AppCompatActivity {
    // Declaracion de los widgets
    ImageButton buttonAtras;
    TextView textViewAjustes;
    Button buttonCambiarNombre;
    Button buttonCambiarContrasena;
    Button buttonCerrarSesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
        // Obtencion del nombre del intent
        Intent intent = getIntent();
        String nombreFormateado = intent.getStringExtra("nombreFormateado");
        // Inicializacion de los widgets
        buttonAtras = findViewById(R.id.imageButtonAtras);
        textViewAjustes = findViewById(R.id.textViewAjustes);
        buttonCambiarNombre = findViewById(R.id.buttonCambiarNombre);
        buttonCambiarContrasena = findViewById(R.id.buttonCambiarContrasena);
        buttonCerrarSesion = findViewById(R.id.buttonCerrarSesion);
        buttonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarPerfil(nombreFormateado);
            }
        });
        buttonCambiarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCambiarNombre(nombreFormateado);
            }
        });
        buttonCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCambiarContrasena(nombreFormateado);
            }
        });
        buttonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });
    }
    // Inicia el perfil del usuario actual
    private void iniciarPerfil(String nombreFormateado) {
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
    // Modifica el nombre de usuario del usuario actual
    private void iniciarCambiarNombre(String nombreFormateado) {
        mostrarToast("Implementar la funcionalidad de cambio de nombre");
    }
    // Cambia la contraseña del usuario actual
    private void iniciarCambiarContrasena(String nombreFormateado) {
        mostrarToast("Implementar la funcionalidad de cambio de contraseña");
    }
    // Muestra un toast con un mensaje introducido por parametro
    private void mostrarToast(String mensaje) {
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
    // Cierra la sesion del usuario actual y nos redirige al inicio de sesion
    private void cerrarSesion() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signOut();
        Intent intent = new Intent(AjustesActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}