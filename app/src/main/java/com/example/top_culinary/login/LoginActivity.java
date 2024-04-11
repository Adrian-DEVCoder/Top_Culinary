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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.top_culinary.MainActivity;
import com.example.top_culinary.R;
import com.example.top_culinary.registro.RegistroActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    // Widgets
    ImageView imageViewFondo;
    ImageView imageViewTitulo;
    TextView textViewIniciarSesion;
    TextView textViewEmail;
    TextView textViewContrasenia;
    EditText editTextEmail;
    EditText editTextContrasenia;
    Button buttonIniciarSesion;
    TextView textViewRegistro;
    Button buttonRegistro;
    TextView textViewIniciarCon;
    ImageButton imageButtonGoogle;
    ImageButton imageButtonFacebook;

    // Variables
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Inicializacion del Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // Inicializacion de los widgets
        imageViewFondo = findViewById(R.id.imgFondo);
        imageViewTitulo = findViewById(R.id.imgTituloFondo);
        textViewIniciarSesion = findViewById(R.id.txvIniciarSesion);
        textViewEmail = findViewById(R.id.txvEmail);
        editTextEmail = findViewById(R.id.edtEmail);
        textViewContrasenia = findViewById(R.id.txvContrasenia);
        editTextContrasenia = findViewById(R.id.edtContrasenia);
        buttonIniciarSesion = findViewById(R.id.btnIniciarSesion);
        textViewRegistro = findViewById(R.id.txvRegistro);
        buttonRegistro = findViewById(R.id.btnRegistro);
        textViewIniciarCon = findViewById(R.id.txvIniciarSesionCon);
        imageButtonGoogle = findViewById(R.id.iBtnGoogle);
        imageButtonFacebook = findViewById(R.id.iBtnFacebook);
        
        // Listener del boton para iniciar sesion
        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String contrasenia = editTextContrasenia.getText().toString().trim();
                if(email.isEmpty() && contrasenia.isEmpty()){
                    editTextEmail.setError("Por favor, introduzca sus credenciales.");
                    editTextEmail.requestFocus();
                } else if (!email.contains("@")){
                    editTextEmail.setError("Por favor, introduzca un correo electrónico valido.");
                    editTextEmail.requestFocus();
                } else if (contrasenia.length() <= 8){
                    editTextContrasenia.setError("Por favor, introduzca su contraseña.");
                    editTextContrasenia.requestFocus();
                } else {
                    iniciarSesion(email, contrasenia);
                }
            }
        });

        // Listener del boton para redirigir al registro
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro();
            }
        });
    }

    /**
     * Iniciamos sesion con el usuario indicado
     * @param email del usuario
     * @param contrasenia del usuario
     */
    private void iniciarSesion(String email, String contrasenia){
        firebaseAuth.signInWithEmailAndPassword(email,contrasenia)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // Si el inicio de sesion ha sido correcto actualizamos con la informacion del usuario
                            Log.w("Sesion","signInUserGmailAndPassword:success");
                            FirebaseUser usuarioActual = firebaseAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Log.w("Sesion","signInUserGmailAndPassword:failed");
                            Toast.makeText(LoginActivity.this, "Inicio de Sesion erroneo.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Redirigimos al registro al usuario
      */
    private void registro(){
        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(intent);
    }
}