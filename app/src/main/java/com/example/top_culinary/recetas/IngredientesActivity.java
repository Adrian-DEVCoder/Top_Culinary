package com.example.top_culinary.recetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.top_culinary.R;
import com.example.top_culinary.adapter.UnidadSpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IngredientesActivity extends AppCompatActivity {
    // Declaración de las variables
    private String nombreReceta;
    private String nombreFormateado;
    private String imagenReceta;
    private List<String> ingredientes = new ArrayList<>();

    // Declaración de los widgets
    private ScrollView scrollViewIngredientes;
    private LinearLayout linearLayoutIngredientes;
    private EditText editTextCantidad;
    private Spinner spinnerUnidad;
    private EditText editTextIngrediente;
    private TextView textViewIngredientesAnadidos;
    private ImageButton buttonAgregarIngrediente;
    private ImageButton buttonSiguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientes);

        // Obtención de datos del intent
        Intent intent = getIntent();
        nombreFormateado = intent.getStringExtra("nombreFormateado");
        nombreReceta = intent.getStringExtra("nombreReceta");
        imagenReceta = intent.getStringExtra("imagenReceta");

        // Inicialización de los widgets
        initWidgets();

        // Inicialización del spinner de unidades
        inicializarSpinner();

        // Actualizar vista de ingredientes añadidos
        actualizarTextViewIngredientes();

        // Configuración de los listeners
        setupListeners();
    }

    private void initWidgets() {
        scrollViewIngredientes = findViewById(R.id.scrollViewIngredientes);
        linearLayoutIngredientes = findViewById(R.id.linearLayoutIngredientes);
        editTextCantidad = findViewById(R.id.editTextCantidad);
        spinnerUnidad = findViewById(R.id.spinnerUnidad);
        editTextIngrediente = findViewById(R.id.editTextIngrediente);
        textViewIngredientesAnadidos = findViewById(R.id.textViewIngredientesAnadidos);
        buttonAgregarIngrediente = findViewById(R.id.imageButtonAgregarIngrediente);
        buttonSiguiente = findViewById(R.id.imageButtonSiguiente);
    }

    private void inicializarSpinner() {
        List<String> unidades = Arrays.asList("Selecciona una unidad - Opcional", "Kilogramo (kg)", "Gramo (g)", "Mililitro (ml)", "Litro (l)", "Cucharada (cda)", "Cucharadita (cdita)", "Unidades");
        UnidadSpinnerAdapter adapter = new UnidadSpinnerAdapter(this, android.R.layout.simple_spinner_item, unidades);
        spinnerUnidad.setAdapter(adapter);
    }

    private void setupListeners() {
        buttonAgregarIngrediente.setOnClickListener(v -> agregarIngrediente());

        buttonSiguiente.setOnClickListener(v -> iniciarAnadirPasos());
    }

    private void agregarIngrediente() {
        String cantidad = editTextCantidad.getText().toString().trim();
        String nombre = editTextIngrediente.getText().toString().trim();
        String unidad = spinnerUnidad.getSelectedItem().toString();

        if (cantidad.isEmpty()) {
            mostrarToast("Por favor, introduce una cantidad.");
            editTextCantidad.setError("Introduce una cantidad");
            editTextCantidad.requestFocus();
            return;
        }

        if (nombre.isEmpty()) {
            mostrarToast("Por favor, introduce un ingrediente.");
            editTextIngrediente.setError("Introduce un ingrediente");
            editTextIngrediente.requestFocus();
            return;
        }

        anadirFormatearIngrediente(cantidad, unidad, nombre);
        limpiarCampos();
    }

    private void anadirFormatearIngrediente(String cantidad, String unidad, String nombre) {
        String unidadFormateada = unidad;
        if (!unidadFormateada.equalsIgnoreCase("Selecciona una unidad - Opcional") && !unidadFormateada.equalsIgnoreCase("Unidades")) {
            int primerParentesis = unidadFormateada.indexOf("(");
            int segundoParentesis = unidadFormateada.indexOf(")", primerParentesis);
            if (primerParentesis != -1 && segundoParentesis != -1) {
                unidadFormateada = unidadFormateada.substring(primerParentesis + 1, segundoParentesis);
            }
        }

        String ingredienteFormateado = unidadFormateada.equalsIgnoreCase("Selecciona una unidad - Opcional") || unidadFormateada.equalsIgnoreCase("Unidades")
                ? cantidad + " " + nombre
                : cantidad + " " + unidadFormateada + " de " + nombre;

        ingredientes.add(ingredienteFormateado);
        actualizarTextViewIngredientes();
    }

    private void actualizarTextViewIngredientes() {
        StringBuilder stbIngredientes = new StringBuilder();
        for (String ingrediente : ingredientes) {
            stbIngredientes.append(ingrediente).append("\n");
        }
        textViewIngredientesAnadidos.setText(stbIngredientes.length() > 0 ? stbIngredientes.toString() : "No hay ingredientes añadidos");
    }

    private void limpiarCampos() {
        editTextCantidad.setText("");
        editTextIngrediente.setText("");
        spinnerUnidad.setSelection(0);
        editTextCantidad.requestFocus();
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void iniciarAnadirPasos() {
        Intent intent = new Intent(IngredientesActivity.this, PasosActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        intent.putExtra("nombreReceta", nombreReceta);
        intent.putExtra("imagenReceta", imagenReceta);
        intent.putStringArrayListExtra("listaIngredientes", new ArrayList<>(ingredientes));
        startActivity(intent);
        finish();
    }
}
