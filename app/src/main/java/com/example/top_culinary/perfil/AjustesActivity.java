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

public class AjustesActivity extends AppCompatActivity {
    // Declaracion de los widgets
    ImageButton buttonAtras;
    TextView textViewAjustes;
    Button buttonCambiarNombre;
    Button buttonCambiarContrasena;
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
    }
    // Metodo para iniciar el perfil
    private void iniciarPerfil(String nombreFormateado) {
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
    // Metodo para cambiar el nombre de usuario
    private void iniciarCambiarNombre(String nombreFormateado) {
        mostrarToast("Implementar la funcionalidad de cambio de nombre");
    }
    // Metodo para cambiar la contraseña del usuario
    private void iniciarCambiarContrasena(String nombreFormateado) {
        mostrarToast("Implementar la funcionalidad de cambio de contraseña");
    }
    // Metodo para mostrar un toast con un mensaje indicado
    private void mostrarToast(String mensaje) {
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
}