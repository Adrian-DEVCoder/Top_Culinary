package com.example.top_culinary.cocina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.top_culinary.R;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Receta;

import java.util.List;

public class DetallesRecetaActivity extends AppCompatActivity {
    // Declaracion de las variables
    private DBHandler dbHandler;
    // Declaracion de los widgets
    private ImageButton imageButtonAtras;
    private ImageView imageViewReceta;
    private TextView textViewTitulo;
    private TextView textViewDescripcion;
    private TextView textViewListaIngredientes;
    private TextView textViewListaEquipamiento;
    private TextView textViewListaPasos;
    private Button buttonComenzarReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_receta);

        // Inicializar la base de datos y los widgets
        dbHandler = new DBHandler(this);
        initWidgets();

        // Obtener el nombre de la receta del intent
        Intent intent = getIntent();
        String nombreReceta = intent.getStringExtra("nombreReceta");
        String nombreFormateado = intent.getStringExtra("nombreFormateado");

        // Cargar los datos de la receta
        Receta receta = dbHandler.obtenerRecetaPorNombre(nombreReceta);
        cargarDatosReceta(receta);

        // Configurar los listeners
        setupListeners(nombreFormateado, receta);
    }

    private void initWidgets() {
        imageButtonAtras = findViewById(R.id.imageButtonBack);
        imageViewReceta = findViewById(R.id.imageViewReceta);
        textViewTitulo = findViewById(R.id.textviewTituloReceta);
        textViewDescripcion = findViewById(R.id.textviewDescripcion);
        textViewListaIngredientes = findViewById(R.id.textviewListaIngredientes);
        textViewListaEquipamiento = findViewById(R.id.textviewListaEquipamiento);
        textViewListaPasos = findViewById(R.id.textviewListaPasos);
        buttonComenzarReceta = findViewById(R.id.buttonComenzarReceta);
    }

    private void cargarDatosReceta(Receta receta) {
        if (receta != null) {
            Glide.with(this)
                    .load(receta.getImagen())
                    .into(imageViewReceta);

            textViewTitulo.setText(receta.getTitulo());
            textViewDescripcion.setText(receta.getDescripcion());

            textViewListaIngredientes.setText(construirLista(receta.getIngredientes()));
            textViewListaEquipamiento.setText(construirLista(receta.getEquipamiento()));
            textViewListaPasos.setText(construirListaPasos(receta.getPasos()));
        }
    }

    private String construirLista(List<String> items) {
        StringBuilder stb = new StringBuilder();
        for (String item : items) {
            stb.append("- ").append(item).append("\n");
        }
        return stb.toString();
    }

    private String construirListaPasos(List<String> pasos) {
        StringBuilder stb = new StringBuilder();
        for (int i = 0; i < pasos.size(); i++) {
            stb.append(i + 1).append(". ").append(pasos.get(i)).append("\n");
        }
        return stb.toString();
    }

    private void setupListeners(String nombreFormateado, Receta receta) {
        imageButtonAtras.setOnClickListener(v -> volverAtras(nombreFormateado));
        buttonComenzarReceta.setOnClickListener(v -> comenzarReceta(receta.getTitulo()));
        animarBotonComenzar();
    }

    private void volverAtras(String nombreFormateado) {
        Intent intent = new Intent(DetallesRecetaActivity.this, CocinaActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        finish();
    }

    private void comenzarReceta(String nombreReceta) {
        Intent intent = new Intent(DetallesRecetaActivity.this, RecetacionActivity.class);
        intent.putExtra("nombreReceta", nombreReceta);
        startActivity(intent);
        finish();
    }

    private void animarBotonComenzar() {
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_animation);
        buttonComenzarReceta.startAnimation(pulseAnimation);
    }
}
