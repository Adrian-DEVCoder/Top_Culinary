package com.example.top_culinary.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.R;
import com.example.top_culinary.model.Dialogo;
import com.example.top_culinary.registro.RegistroActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    // Declaracion de las constantes
    private static final int RC_SIGN_IN = 9001;
    // Declaracion de las Variables
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    // Declaracion de los widgets
    private EditText editTextEmail;
    private EditText editTextContrasenia;
    private Button buttonIniciarSesion;
    private Button buttonRegistro;
    private ImageButton imageButtonGoogle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Inicialización de Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        // Comprobar si el usuario ya ha iniciado sesión
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            redirigirACocina();
        }
        // Inicialización de los widgets
        editTextEmail = findViewById(R.id.edtEmail);
        editTextContrasenia = findViewById(R.id.edtContrasenia);
        buttonIniciarSesion = findViewById(R.id.btnIniciarSesion);
        buttonRegistro = findViewById(R.id.btnRegistro);
        imageButtonGoogle = findViewById(R.id.iBtnGoogle);
        // Configurar listeners
        buttonIniciarSesion.setOnClickListener(v -> validarYIniciarSesion());
        buttonRegistro.setOnClickListener(v -> redirigirARegistro());
        imageButtonGoogle.setOnClickListener(v -> inicioSesionGoogle());
        // Configurar opciones de inicio de sesión de Google
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

    private void validarYIniciarSesion() {
        String email = editTextEmail.getText().toString().trim();
        String contrasenia = editTextContrasenia.getText().toString().trim();
        if (email.isEmpty()) {
            editTextEmail.setError("Por favor, introduzca su correo electrónico.");
            editTextEmail.requestFocus();
        } else if (!email.contains("@")) {
            editTextEmail.setError("Por favor, introduzca un correo electrónico válido.");
            editTextEmail.requestFocus();
        } else if (contrasenia.length() <= 8) {
            editTextContrasenia.setError("Por favor, introduzca su contraseña.");
            editTextContrasenia.requestFocus();
        } else {
            iniciarSesion(email, contrasenia);
        }
    }

    private void iniciarSesion(String email, String contrasenia) {
        firebaseAuth.signInWithEmailAndPassword(email, contrasenia)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        verificarUsuarioEnFirestore();
                    } else {
                        mostrarDialogo("Error de inicio de sesión", "Inicio de sesión incorrecto.");
                    }
                });
    }

    private void verificarUsuarioEnFirestore() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
            firestoreDB.collection("usuarios")
                    .document(currentUser.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                redirigirACocina();
                            } else {
                                redirigirANombreUsuario("Custom");
                            }
                        } else {
                            Log.d("Firestore", "Error al verificar el documento del usuario", task.getException());
                        }
                    });
        }
    }

    private void redirigirARegistro() {
        Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
        startActivity(intent);
    }

    private void inicioSesionGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Log.d("Google", "Error al iniciar sesión con Google");
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        verificarUsuarioEnFirestore();
                    } else {
                        mostrarDialogo("Error","Error en el inicio de sesión con Google.");
                    }
                });
    }

    private void redirigirACocina() {
        Intent intent = new Intent(LoginActivity.this, CocinaActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirigirANombreUsuario(String metodoInicioSesion) {
        Intent intent = new Intent(LoginActivity.this, NombreUsuarioActivity.class);
        intent.putExtra("metodoInicioSesion", metodoInicioSesion);
        startActivity(intent);
        finish();
    }

    private void mostrarDialogo(String titulo, String contenido) {
        Dialogo.showDialog(this, titulo, contenido);
    }
}
