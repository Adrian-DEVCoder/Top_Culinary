package com.example.top_culinary.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.top_culinary.R;
import com.example.top_culinary.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ModificacionContrasenaActivity extends AppCompatActivity {
    // Declaracion de las variables
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
        // Obtencion del nombre de usuario a traves del intent
        Intent intent = getIntent();
        String nombreFormateado = intent.getStringExtra("nombreFormateado");
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
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent intent = new Intent(ModificacionContrasenaActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}