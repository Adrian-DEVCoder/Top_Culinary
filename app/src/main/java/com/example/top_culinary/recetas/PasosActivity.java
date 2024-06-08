package com.example.top_culinary.recetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.top_culinary.R;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Dialogo;

import java.util.ArrayList;
import java.util.List;

public class PasosActivity extends AppCompatActivity {
    // Declaración de las variables
    private DBHandler dbHandler;
    private String nombreReceta;
    private String nombreFormateado;
    private String imagenReceta;
    private ArrayList<String> listaIngredientes;
    private List<String> pasos = new ArrayList<>();

    // Declaración de los widgets
    private ScrollView scrollViewPasos;
    private LinearLayout linearLayoutPasos;
    private EditText editTextPaso;
    private ImageButton buttonAnadirPaso;
    private TextView textViewPasosAnadidos;
    private ImageButton buttonAnadirReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasos);

        // Inicialización de la DB
        dbHandler = new DBHandler(this);

        // Obtención de datos a través del intent
        obtenerDatosIntent();

        // Inicialización de los widgets
        initWidgets();

        // Configuración de los listeners
        setupListeners();
    }

    private void obtenerDatosIntent() {
        Intent intent = getIntent();
        nombreReceta = intent.getStringExtra("nombreReceta");
        imagenReceta = intent.getStringExtra("imagenReceta");
        nombreFormateado = intent.getStringExtra("nombreFormateado");
        listaIngredientes = intent.getStringArrayListExtra("listaIngredientes");
    }

    private void initWidgets() {
        scrollViewPasos = findViewById(R.id.scrollViewPasos);
        linearLayoutPasos = findViewById(R.id.linearLayoutPasos);
        editTextPaso = findViewById(R.id.editTextPaso);
        buttonAnadirPaso = findViewById(R.id.imageButtonAnadirPaso);
        textViewPasosAnadidos = findViewById(R.id.textViewPasosAnadidos);
        buttonAnadirReceta = findViewById(R.id.imageButtonAnadirReceta);

        actualizarTextViewPasos();
    }

    private void setupListeners() {
        buttonAnadirPaso.setOnClickListener(v -> {
            if (verificarPaso()) {
                limpiarCampos();
            }
        });

        buttonAnadirReceta.setOnClickListener(v -> anadirNuevaReceta());
    }

    private boolean verificarPaso() {
        String paso = editTextPaso.getText().toString().trim();
        if (paso.isEmpty()) {
            mostrarDialogo("Error","Por favor, ingresa un paso a realizar");
            editTextPaso.setError("Ingresa un paso a realizar");
            editTextPaso.requestFocus();
            return false;
        } else {
            anadirPaso(paso);
            return true;
        }
    }

    private void anadirPaso(String pasoIngresado) {
        pasos.add(pasoIngresado);
        actualizarTextViewPasos();
    }

    private void actualizarTextViewPasos() {
        StringBuilder stbPasos = new StringBuilder();
        for (int i = 0; i < pasos.size(); i++) {
            stbPasos.append(i + 1).append(". ").append(pasos.get(i)).append("\n");
        }
        String pasosActuales = stbPasos.toString();
        textViewPasosAnadidos.setText(pasosActuales.isEmpty() ? "No hay pasos añadidos" : pasosActuales);
    }

    private void anadirNuevaReceta() {
        String ingredientes = convertirListaAString(listaIngredientes);
        String pasosConcatenados = convertirListaAString(pasos);

        dbHandler.insertarRecetaUsuario(imagenReceta, nombreReceta, ingredientes, pasosConcatenados);
        mostrarDialogo("Operación Exitosa","Receta añadida con éxito");
        iniciarAnadirRecetas();
    }

    private String convertirListaAString(List<String> lista) {
        StringBuilder stb = new StringBuilder();
        for (String item : lista) {
            stb.append(item).append("\n");
        }
        return stb.toString();
    }

    private void iniciarAnadirRecetas() {
        Intent intent = new Intent(this, AniadirRecetasActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        finish();
    }

    private void limpiarCampos() {
        editTextPaso.setText("");
    }

    private void mostrarDialogo(String titulo, String contenido) {
        Dialogo.showDialog(this, titulo, contenido);
    }
}
