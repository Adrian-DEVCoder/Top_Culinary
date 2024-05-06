package com.example.top_culinary.perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.top_culinary.R;
import com.example.top_culinary.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class ModificacionContrasenaActivity extends AppCompatActivity {
    // Declaracion de las constantes
    private static final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$");
    // Declaracion de las variables
    private FirebaseFirestore firestoreDB;
    private String nombreFormateado;
    private boolean esVisible = false;
    // Declaracion de los widgets
    ImageButton buttonAtras;
    TextView textViewModificarContrasena;
    TextView textViewIntroduceContrasena;
    EditText editTextContrasena;
    ImageButton buttonVerContrasena;
    ImageButton buttonConfirmar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificacion_contrasena);
        // Inicializacion del Firestore
        firestoreDB = FirebaseFirestore.getInstance();
        // Obtencion del nombre de usuario a traves del intent
        Intent intent = getIntent();
        nombreFormateado = intent.getStringExtra("nombreFormateado");
        // Inicializacion de los widgets
        buttonAtras = findViewById(R.id.imageButtonAtras);
        textViewModificarContrasena = findViewById(R.id.textViewModificarContrasena);
        textViewIntroduceContrasena = findViewById(R.id.textViewIntroduceContrasena);
        editTextContrasena = findViewById(R.id.editTextContrasena);
        buttonVerContrasena = findViewById(R.id.imageButtonVerContrasena);
        buttonConfirmar = findViewById(R.id.imageButtonConfirmar);
        // Listener de los diferentes botones
        buttonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModificacionContrasenaActivity.this,AjustesActivity.class);
                intent.putExtra("nombreFormateado",nombreFormateado);
                startActivity(intent);
                finish();
            }
        });
        buttonVerContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(esVisible) {
                    buttonVerContrasena.setImageResource(R.drawable.ojo_cerrado);
                    editTextContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    esVisible = false;
                } else {
                    buttonVerContrasena.setImageResource(R.drawable.ojo);
                    editTextContrasena.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    esVisible = true;
                }
            }
        });
        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarActualizarContrasena();
            }
        });
    }

    /**
     * Valida si la contraseña introducida es valida
     */
    private void validarActualizarContrasena() {
        String nuevaContrasena = editTextContrasena.getText().toString().trim();
        if(nuevaContrasena.isEmpty()) {
            editTextContrasena.requestFocus();
            Toast.makeText(this, "Por favor, introduce una contraseña válida.", Toast.LENGTH_SHORT).show();
            buttonConfirmar.setImageResource(R.drawable.no_confirmar);
            return;
        } else if (nuevaContrasena.length() <= 8) {
            editTextContrasena.requestFocus();
            Toast.makeText(this, "La contraseña debe contener mas de 8 caracteres. Ademas, debe de incluir mayúsculas, minúsculas y números.", Toast.LENGTH_SHORT).show();
            buttonConfirmar.setImageResource(R.drawable.no_confirmar);
            return;
        } else if (!comprobarContrasena(nuevaContrasena)) {
            Toast.makeText(this, "La contraseña debe contener mas de 8 caracteres. Ademas, debe de incluir mayúsculas, minúsculas y números.", Toast.LENGTH_SHORT).show();
            editTextContrasena.requestFocus();
            return;
        } else {
            actualizarContrasena(nuevaContrasena);
        }
    }

    /**
     * Verifica si la contraseña cumple con el patron minimo de seguridad
     * @param nuevaContrasena del usuario
     * @return true/false dependiendo si cumple con el patron o no
     */
    private boolean comprobarContrasena(String nuevaContrasena) {
        return passwordPattern.matcher(nuevaContrasena).matches();
    }

    /**
     * Actualiza la contraseña en caso de pasar las validaciones previas
     */
    private void actualizarContrasena(String nuevaContrasena) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            currentUser.updatePassword(nuevaContrasena)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(ModificacionContrasenaActivity.this, "Contraseña actualizada correctamente.", Toast.LENGTH_SHORT).show();
                                // Redirigimos al usuario a los ajustes de nuevo
                                Intent intent = new Intent(ModificacionContrasenaActivity.this,AjustesActivity.class);
                                intent.putExtra("nombreFormateado",nombreFormateado);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ModificacionContrasenaActivity.this, "Error al actualizar la contraseña del usuario.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

}
