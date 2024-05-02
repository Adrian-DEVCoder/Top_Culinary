package com.example.top_culinary.registro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.top_culinary.R;
import com.example.top_culinary.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class RegistroActivity extends AppCompatActivity {
    /**
     * Widgets
     */
    ImageView imageViewFondo;
    ImageView imageViewTitulo;
    TextView textViewNuevoChef;
    TextView textViewEmail;
    EditText editTextEmail;
    TextView textViewContrasenia;
    EditText editTextContrasenia;
    TextView textViewEligeTema;
    LinearLayout linearLayoutTemas;
    ImageView imageViewNaranja;
    ImageView imageViewVerde;
    ImageView imageViewRojo;
    ImageView imageViewMarron;
    ImageView imageViewAzul;
    Button buttonRegistro;
    TextView textViewYaChef;
    Button buttonInicioSesion;

    /**
     * Variables
     */
    private FirebaseAuth firebaseAuth;
    /**
     * Constantes
     */
    private static final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        // Inicializacion de Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        // Inicializacion de los Widgets
        imageViewFondo = findViewById(R.id.imgFondo);
        imageViewTitulo = findViewById(R.id.imgTituloFondo);
        textViewNuevoChef = findViewById(R.id.txvNuevoChef);
        textViewEmail = findViewById(R.id.txvEmail);
        editTextEmail = findViewById(R.id.edtEmail);
        textViewContrasenia = findViewById(R.id.txvContrasenia);
        editTextContrasenia = findViewById(R.id.edtContrasenia);
        textViewEligeTema = findViewById(R.id.txvEligeTema);
        linearLayoutTemas = findViewById(R.id.llTemas);
        imageViewNaranja = findViewById(R.id.imgTemaNaranja);
        imageViewVerde = findViewById(R.id.imgTemaVerde);
        imageViewRojo = findViewById(R.id.imgTemaRojo);
        imageViewMarron = findViewById(R.id.imgTemaMarron);
        imageViewAzul = findViewById(R.id.imgTemaAzul);
        buttonRegistro = findViewById(R.id.btnRegistro);
        textViewYaChef = findViewById(R.id.txvYaChef);
        buttonInicioSesion = findViewById(R.id.btnIniciarSesion);
        // Listener del boton para registrarse
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextEmail.getText().toString().trim();
                String password = editTextContrasenia.getText().toString().trim();
                if(email.isEmpty() && password.isEmpty()) {
                    editTextEmail.setError("Por favor, introduce las credenciales.");
                } else if(!email.contains("@")){
                    editTextEmail.setError("El email introducido no es valido.");
                    editTextEmail.requestFocus();
                } else if(password.length() <= 8){
                    editTextContrasenia.setError("La contraseña debe contener mas de 8 caracteres. Además, debe incluir mayúsculas, minúsculas y números.");
                    editTextContrasenia.requestFocus();
                } else if(!comprobarContrasenia(password)){
                    editTextContrasenia.setError("La contraseña debe contener mas de 8 caracteres. Además, debe incluir mayúsculas, minúsculas y números.");
                    editTextContrasenia.requestFocus();
                } else {
                    registro(email, password);
                }

            }
        });
        // Listener del boton para ir al inicio de sesion
        buttonInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicioSesion();
            }
        });
    }

    /**
     * Registramos al usuario con los datos indicados
     * @param email del usuario
     * @param password del usuario
     */
    private void registro(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("REGISTRO","Registro Satisfactorio");
                            // Reinicia la sesión de usuario
                            firebaseAuth.signOut();
                            // Luego, inicia la actividad de inicio de sesión
                            Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w("REGISTRO","Registro Incorrecto");
                            Toast.makeText(RegistroActivity.this,"Registro Erroneo",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Redirigimos al usuario al inicio de sesion
     */
    private void inicioSesion(){
        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
        startActivity(intent);
        super.finish();
    }

    /**
     * Comprobamos la contraseña introducida por el usuario
     * @param pass del usuario
     * @return true o false dependiendo si la contraseña pasa el pattern
     */
    private boolean comprobarContrasenia(String pass){
        return passwordPattern.matcher(pass).matches();
    }
}