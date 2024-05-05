package com.example.top_culinary.perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.QuerySnapshot;

public class ModificacionNombreActivity extends AppCompatActivity {
    // Declaracion de las variables
    private FirebaseFirestore firestoreDB;
    private String nombreFormateado;
    // Declaracion de los widgets
    ImageButton buttonAtras;
    TextView textViewModificarNombre;
    TextView textViewIntroduceNombre;
    EditText editTextNombreUsuario;
    ImageButton buttonConfirmar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificacion_nombre);
        // Inicializacion del Firestore
        firestoreDB = FirebaseFirestore.getInstance();
        // Obtencion del nombre del usuario a traves del intent
        Intent intent = getIntent();
        nombreFormateado = intent.getStringExtra("nombreFormateado");
        // Inicializacion de los widgets
        buttonAtras = findViewById(R.id.imageButtonAtras);
        textViewModificarNombre = findViewById(R.id.textViewModificarNombre);
        textViewIntroduceNombre = findViewById(R.id.textViewNombreUsuario);
        editTextNombreUsuario = findViewById(R.id.editTextNombreUsuario);
        buttonConfirmar = findViewById(R.id.imageButtonConfirmar);
        // Listener de los diferentes botones
        buttonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModificacionNombreActivity.this, AjustesActivity.class);
                intent.putExtra("nombreFormateado", nombreFormateado);
                startActivity(intent);
            }
        });
        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarActualizarNombreUsuario();
            }
        });
    }

    /**
     * Valida si el nombre de usuario introducido es vacio
     */
    private void validarActualizarNombreUsuario() {
        String nuevoNomUsuario = editTextNombreUsuario.getText().toString().trim();
        if(nuevoNomUsuario.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce un nombre de usuario válido.", Toast.LENGTH_SHORT).show();
            buttonConfirmar.setImageResource(R.drawable.no_confirmar);
            return;
        }
        verificarNombreUsuarioRegistrado(nuevoNomUsuario, nombreFormateado);
    }

    /**
     * Verifica si el nombre de usuario ya esta registrado
     */
    private void verificarNombreUsuarioRegistrado(String nuevoNomUsuario, String nombreFormateado) {
        firestoreDB.collection("usuarios")
                .whereEqualTo("display_name",nuevoNomUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            if(!task.getResult().isEmpty()) {
                                Toast.makeText(ModificacionNombreActivity.this, "El nombre de usuario ya esta registrado",Toast.LENGTH_SHORT).show();
                                buttonConfirmar.setImageResource(R.drawable.no_confirmar);
                            } else {
                                actualizarNombreUsuario(nuevoNomUsuario);
                            }
                        } else {
                            Toast.makeText(ModificacionNombreActivity.this, "Error al verificar el nombre de usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Actualiza el nombre de usuario, una vez pasadas las validaciones
     */
    private void actualizarNombreUsuario(String nuevoNomUsuario) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            firestoreDB.collection("usuarios")
                    .document(currentUser.getUid())
                    .update("display_name",nuevoNomUsuario)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(ModificacionNombreActivity.this, "Nombre de Usuario actualizado correctamente.", Toast.LENGTH_SHORT).show();
                                // Redirigimos al usuario a los ajustes de nuevo
                                Intent intent = new Intent(ModificacionNombreActivity.this,AjustesActivity.class);
                                intent.putExtra("nombreFormateado",nuevoNomUsuario);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ModificacionNombreActivity.this, "Error al actualizar el nombre de usuario.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
