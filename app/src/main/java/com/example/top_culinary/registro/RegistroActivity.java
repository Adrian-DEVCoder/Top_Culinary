package com.example.top_culinary.registro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.top_culinary.R;
import com.example.top_culinary.login.LoginActivity;
import com.example.top_culinary.model.Dialogo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {
    // Widgets
    private TextInputEditText edtEmail, edtContrasenia;
    private TextInputLayout tilEmail, tilContrasenia;
    private Button btnRegistro, btnIniciarSesion;
    // Variables
    private FirebaseAuth firebaseAuth;
    // Constantes
    private static final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        // Inicialización de Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        // Inicialización de los Widgets
        edtEmail = findViewById(R.id.edtEmail);
        edtContrasenia = findViewById(R.id.edtContrasenia);
        tilEmail = findViewById(R.id.tilEmail);
        tilContrasenia = findViewById(R.id.tilContrasenia);
        btnRegistro = findViewById(R.id.btnRegistro);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        // Listener del botón para registrarse
        btnRegistro.setOnClickListener(v -> validarYRegistrar());
        // Listener del botón para ir al inicio de sesión
        btnIniciarSesion.setOnClickListener(v -> inicioSesion());
    }

    /**
     * Validamos los datos de entrada y registramos al usuario si son válidos
     */
    private void validarYRegistrar() {
        String email = edtEmail.getText().toString().trim();
        String password = edtContrasenia.getText().toString().trim();

        if (email.isEmpty()) {
            tilEmail.setError("Ingrese su email");
            edtEmail.requestFocus();
        } else if (!email.contains("@")) {
            tilEmail.setError("El email introducido no es válido.");
            edtEmail.requestFocus();
        } else if (password.length() <= 8) {
            tilContrasenia.setError("La contraseña debe contener más de 8 caracteres.");
            edtContrasenia.requestFocus();
        } else if (!comprobarContrasenia(password)) {
            tilContrasenia.setError("La contraseña debe contener mayúsculas, minúsculas y números.");
            edtContrasenia.requestFocus();
        } else {
            registro(email, password);
        }
    }

    /**
     * Registramos al usuario con los datos indicados
     * @param email del usuario
     * @param password del usuario
     */
    private void registro(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("REGISTRO", "Registro satisfactorio");
                        firebaseAuth.signOut(); // Cerrar la sesión del usuario recién registrado
                        inicioSesion(); // Redirigir al inicio de sesión
                    } else {
                        Log.w("REGISTRO", "Registro incorrecto", task.getException());
                        Dialogo.showDialog(this,"Error","Registro erroneo. Vuelve a introducir los datos.");
                    }
                });
    }

    /**
     * Redirigimos al usuario al inicio de sesión
     */
    private void inicioSesion() {
        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Comprobamos la contraseña introducida por el usuario
     * @param pass del usuario
     * @return true o false dependiendo si la contraseña pasa el pattern
     */
    private boolean comprobarContrasenia(String pass) {
        return passwordPattern.matcher(pass).matches();
    }
}
