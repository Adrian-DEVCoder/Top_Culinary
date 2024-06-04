package com.example.top_culinary.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.top_culinary.R;
import com.example.top_culinary.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class ModificacionContrasenaActivity extends AppCompatActivity {
    // Declaración de las constantes
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");

    // Declaración de las variables
    private boolean esVisible = false;
    private String nombreFormateado;

    // Declaración de los widgets
    private EditText editTextContrasena;
    private ImageButton buttonVerContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificacion_contrasena);

        // Obtención del nombre de usuario a través del intent
        nombreFormateado = getIntent().getStringExtra("nombreFormateado");

        // Inicialización de los widgets
        inicializarWidgets();

        // Configuración de los listeners
        configurarListeners();
    }

    private void inicializarWidgets() {
        editTextContrasena = findViewById(R.id.editTextContrasena);
        buttonVerContrasena = findViewById(R.id.imageButtonVerContrasena);

        findViewById(R.id.imageButtonAtras).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresarAjustes();
            }
        });

        findViewById(R.id.buttonConfirmar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarActualizarContrasena();
            }
        });
    }

    private void configurarListeners() {
        buttonVerContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleVerContrasena();
            }
        });
    }

    private void toggleVerContrasena() {
        if (esVisible) {
            buttonVerContrasena.setImageResource(R.drawable.ojo_cerrado);
            editTextContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            esVisible = false;
        } else {
            buttonVerContrasena.setImageResource(R.drawable.ojo);
            editTextContrasena.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            esVisible = true;
        }
    }

    private void validarActualizarContrasena() {
        String nuevaContrasena = editTextContrasena.getText().toString().trim();

        if (nuevaContrasena.isEmpty()) {
            mostrarToast("Por favor, introduce una contraseña válida.");
            return;
        }

        if (nuevaContrasena.length() < 8 || !PASSWORD_PATTERN.matcher(nuevaContrasena).matches()) {
            mostrarToast("La contraseña debe contener más de 8 caracteres, incluyendo mayúsculas, minúsculas y números.");
            return;
        }

        actualizarContrasena(nuevaContrasena);
    }

    private void actualizarContrasena(String nuevaContrasena) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUser.updatePassword(nuevaContrasena).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mostrarToast("Contraseña actualizada correctamente.");
                        regresarAjustes();
                    } else {
                        mostrarToast("Error al actualizar la contraseña del usuario.");
                    }
                }
            });
        }
    }

    private void regresarAjustes() {
        Intent intent = new Intent(ModificacionContrasenaActivity.this, AjustesActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        finish();
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
