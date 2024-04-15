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

import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.R;
import com.example.top_culinary.registro.RegistroActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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
    private GoogleSignInClient mGoogleSignInClient;

    // Constantes
    private static final int RC_SIGN_IN = 9001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Inicializacion del Firebase
        firebaseAuth = FirebaseAuth.getInstance();

        // Implementar el que compruebe si el usuario actual ya ha iniciado sesion a traves del token

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

        // Establecemos las opciones del inicio de sesion de google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        // Listener del boton para redirigir al registro
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registro();
            }
        });

        // Listenre del boton para iniciar sesion con google
        imageButtonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inicioSesionGoogle();
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
                            Intent intent = new Intent(LoginActivity.this, CocinaActivity.class);
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

    private void inicioSesionGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            mostrarToast(e.getMessage());
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            mostrarToast("Inicio de Sesion satisfactorio con Google");
                            startActivity(new Intent(LoginActivity.this, CocinaActivity.class));
                            finish();
                        } else {
                            mostrarToast("Error en el Inicio de Sesion con Google");
                        }
                    }
                });
    }

    /**
     * Mostramos un toast al usuario
     * @param mensaje del toast
     */
    private void mostrarToast(String mensaje){
        Toast.makeText(LoginActivity.this, mensaje, Toast.LENGTH_SHORT).show();
    }
}