package com.example.top_culinary.recetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    // Declaracion de las variables
    private String nombreReceta;
    private String nombreFormateado;
    private String imagenReceta;
    private String cantidadIngrediente;
    private String unidadIngrediente;
    private String nombreIngrediente;
    private List<String> ingredientes = new ArrayList<>();
    private ArrayList<String> listIngredientes;
    // Declaracion de los widgets
    ScrollView scrollViewIngredientes;
    LinearLayout linearLayoutIngredientes;
    TextView textViewCantidad;
    EditText editTextCantidad;
    TextView textViewUnidad;
    Spinner spinnerUnidad;
    TextView textViewIngrediente;
    EditText editTextIngrediente;
    ImageButton buttonAgregarIngrediente;
    TextView textViewIngredientesAnadidos;
    ImageButton buttonSiguiente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredientes);
        // Obtencion del nombre de usuario y del nombre de la receta a traves del intent
        Intent intent = getIntent();
        nombreFormateado = intent.getStringExtra("nombreFormateado");
        nombreReceta = intent.getStringExtra("nombreReceta");
        imagenReceta = intent.getStringExtra("imagenReceta");
        // Inicializacion de los widgets
        scrollViewIngredientes = findViewById(R.id.scrollViewIngredientes);
        linearLayoutIngredientes = findViewById(R.id.linearLayoutIngredientes);
        textViewCantidad = findViewById(R.id.textViewCantidad);
        editTextCantidad = findViewById(R.id.editTextCantidad);
        textViewUnidad = findViewById(R.id.textViewUnidad);
        spinnerUnidad = findViewById(R.id.spinnerUnidad);
        // Inicializamos el spinner con el valor de las unidades
        inicializarSpinner();
        textViewIngrediente = findViewById(R.id.textViewIngrediente);
        editTextIngrediente = findViewById(R.id.editTextIngrediente);
        buttonAgregarIngrediente = findViewById(R.id.imageButtonAgregarIngrediente);
        textViewIngredientesAnadidos = findViewById(R.id.textViewIngredientesAnadidos);
        actualizarTextViewIngredientes();
        buttonSiguiente = findViewById(R.id.imageButtonSiguiente);
        // Listener de los botones
        buttonAgregarIngrediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Asignacion y comprobacion de valores
                if(editTextCantidad.getText().toString().trim().equals("")) {
                    mostrarToast("Por favor, introduce una cantidad.");
                    editTextCantidad.setError("Introduce una cantidad");
                    editTextCantidad.requestFocus();
                } else {
                    cantidadIngrediente = editTextCantidad.getText().toString().trim();
                }
                if(editTextIngrediente.getText().toString().trim().equals("")) {
                    mostrarToast("Por favor, introduce un ingrediente.");
                    editTextIngrediente.setError("Introduce un ingrediente");
                    editTextIngrediente.requestFocus();
                } else {
                    nombreIngrediente = editTextIngrediente.getText().toString().trim();
                }
                unidadIngrediente = spinnerUnidad.getSelectedItem().toString();
                anadirFormatearIngrediente(cantidadIngrediente,unidadIngrediente,nombreIngrediente);
                limpiarCampos();
            }
        });
        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarAnadirPasos(nombreFormateado, nombreReceta,(ArrayList<String>) ingredientes);
            }
        });
    }

    // Inicializa el spinner y los valores de este
    private void inicializarSpinner() {
        List<String> unidades = Arrays.asList("Selecciona una unidad - Opcional","Kilogramo (kg)", "Gramo (g)", "Mililitro (ml)", "Litro (l)","Cucharada (cda)", "Cucharadita (cdita)", "Unidades");
        UnidadSpinnerAdapter adapter = new UnidadSpinnerAdapter(this, android.R.layout.simple_spinner_item, unidades);
        spinnerUnidad.setAdapter(adapter);
    }

    /**
     * Inicia la actividad de los pasos para continuar con la creacion de la receta
     * @param nombreFormateado
     * @param nombreReceta
     * @param listIngredientes
     */
    private void iniciarAnadirPasos(String nombreFormateado, String nombreReceta, ArrayList<String> listIngredientes) {
        Intent intent = new Intent(IngredientesActivity.this, PasosActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        intent.putExtra("nombreReceta", nombreReceta);
        intent.putExtra("imagenReceta", imagenReceta);
        intent.putStringArrayListExtra("listaIngredientes", listIngredientes);
        startActivity(intent);
        finish();
    }

    /**
     * Inserta el ingrediente dentro de la lista de ingredientes, ademas llama a metodos para limpiar los campos y mostrar un toast
     * @param cantidad
     * @param unidad
     * @param nombre
     */
    private void anadirFormatearIngrediente(String cantidad, String unidad, String nombre) {
        String ingredienteFormateado;
        String unidadFormateada = unidad;
        // Verificamos si la unidad no es "Selecciona una unidad" o "unidades"
        if (!unidadFormateada.equalsIgnoreCase("Selecciona una unidad - Opcional") && !unidadFormateada.equalsIgnoreCase("unidades")) {
            // Buscamos el índice del paréntesis de apertura
            int primerParantesis = unidadFormateada.indexOf("(");
            if (primerParantesis != -1) {
                // Buscamos el índice del paréntesis de cierre
                int segundoParentesis = unidadFormateada.indexOf(")", primerParantesis);
                if (segundoParentesis!= -1) {
                    // Extraemos el texto entre los paréntesis
                    unidadFormateada = unidadFormateada.substring(primerParantesis + 1, segundoParentesis);
                }
            }
        }
        if (cantidad!= null && nombre!= null) {
            if (unidadFormateada.equalsIgnoreCase("Selecciona una unidad - Opcional") || unidadFormateada.equalsIgnoreCase("unidades")) {
                ingredienteFormateado = cantidad + " " + nombre;
            } else {
                ingredienteFormateado = cantidad + " " + unidadFormateada + " de " + nombre;
            }
            ingredientes.add(ingredienteFormateado);
            actualizarTextViewIngredientes();
        } else {
            mostrarToast("Por favor, ingresa una cantidad, unidad y nombre para el ingrediente.");
        }
    }

    // Actualiza el contenido del text view de los ingredientes añadidos
    private void actualizarTextViewIngredientes() {
        StringBuilder stbIngredientes = new StringBuilder();
        for (String ingrediente : ingredientes) {
            stbIngredientes.append(ingrediente).append("\n");
        }
        String ingredientesActuales = stbIngredientes.toString();
        if (ingredientesActuales!= null &&!ingredientesActuales.isEmpty()) {
            textViewIngredientesAnadidos.setText(ingredientesActuales);
        } else {
            textViewIngredientesAnadidos.setText("No hay ingredientes añadidos");
        }
    }

    // Limpia los diferentes campos
    private void limpiarCampos() {
        editTextCantidad.setText("");
        editTextIngrediente.setText("");
        spinnerUnidad.setSelection(0);
        editTextCantidad.requestFocus();
    }

    // Muestra un toast con el mensaje indicado
    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}