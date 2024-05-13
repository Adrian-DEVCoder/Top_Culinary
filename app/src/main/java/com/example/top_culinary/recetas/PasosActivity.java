package com.example.top_culinary.recetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.top_culinary.R;
import com.example.top_culinary.database.DBHandler;

import java.util.ArrayList;
import java.util.List;

public class PasosActivity extends AppCompatActivity {
    // Declaracion de las variables
    private DBHandler dbHandler;
    private String nombreReceta;
    private String nombreFormateado;
    private String imagenReceta;
    private ArrayList<String> listaIngredientes;
    private String paso;
    private List<String> pasos = new ArrayList<>();
    // Declaracion de los widgets
    ScrollView scrollViewPasos;
    LinearLayout linearLayoutPasos;
    EditText editTextPaso;
    ImageButton buttonAnadirPaso;
    TextView textViewPasosAnadidos;
    ImageButton buttonAnadirReceta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasos);
        // Inicializacion de la DB
        dbHandler = new DBHandler(this);
        // Obtencion del nombre de usuario, nombre de receta e ingredientes
        Intent intent = getIntent();
        nombreReceta = intent.getStringExtra("nombreReceta");
        imagenReceta = intent.getStringExtra("imagenReceta");
        nombreFormateado = intent.getStringExtra("nombreFormateado");
        listaIngredientes = intent.getStringArrayListExtra("listaIngredientes");
        // Inicializacion de los widgets
        scrollViewPasos = findViewById(R.id.scrollViewPasos);
        linearLayoutPasos = findViewById(R.id.linearLayoutPasos);
        editTextPaso = findViewById(R.id.editTextPaso);
        buttonAnadirPaso = findViewById(R.id.imageButtonAnadirPaso);
        textViewPasosAnadidos = findViewById(R.id.textViewPasosAnadidos);
        actualizarTextViewPasos();
        buttonAnadirReceta = findViewById(R.id.imageButtonAnadirReceta);
        // Listener de los diferentes botones
        buttonAnadirPaso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarPaso();
                limpiarCampos();
            }
        });
        buttonAnadirReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anadirNuevaReceta();
            }
        });
    }

    // Verifica el contenido del paso y llama al metodo de añadir paso
    private void verificarPaso() {
        if(editTextPaso.getText().toString().trim().equals("")) {
            mostrarToast("Por favor, ingresa un paso a realizar");
            editTextPaso.setError("Ingresa un paso a realizar");
            editTextPaso.requestFocus();
        } else {
            paso = editTextPaso.getText().toString();
        }
        anadirPaso(paso);
    }

    // Añade el paso a la lista de pasos y llama al metodo de actualizar
    private void anadirPaso(String pasoIngresado) {
        pasos.add(paso);
        actualizarTextViewPasos();
    }

    // Actualiza el contenido del textview de los pasos
    private void actualizarTextViewPasos() {
        StringBuilder stbPasos = new StringBuilder();
        for(int i=0; i<pasos.size(); i++) {
            stbPasos.append(i+1).append(". ").append(pasos.get(i)).append("\n");
        }
        String pasosActuales = stbPasos.toString();
        if(pasosActuales != null && !pasosActuales.isEmpty()) {
            textViewPasosAnadidos.setText(pasosActuales);
        } else {
            textViewPasosAnadidos.setText("No hay pasos añadidos");
        }
    }

    /**
     * Ingresa la nueva receta del usuario
     */
    private void anadirNuevaReceta() {
        // Concatenamos los valores de la lista de ingredientes
        StringBuilder stbIngredientes = new StringBuilder();
        for(String ingrediente : listaIngredientes) {
            stbIngredientes.append(ingrediente).append("\n");
        }
        String ingredientes = stbIngredientes.toString();
        // Concatenamos los valores de la lista de pasos
        StringBuilder stbPasos = new StringBuilder();
        for(String p : pasos) {
            stbPasos.append(p).append("\n");
        }
        String pasos = stbPasos.toString();
        dbHandler.insertarRecetaUsuario(imagenReceta,nombreReceta,ingredientes,pasos);
        mostrarToast("Receta añadida con exito");
        iniciarAnadirRecetas();
    }

    private void iniciarAnadirRecetas() {
        Intent intent = new Intent(this, AniadirRecetasActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        finish();
    }

    // Limpia los diferentes campos
    private void limpiarCampos() {
        editTextPaso.setText("");
    }

    // Muestra un toast con el mensaje indicado
    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}