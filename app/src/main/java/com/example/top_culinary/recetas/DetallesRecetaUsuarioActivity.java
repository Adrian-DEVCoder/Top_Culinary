package com.example.top_culinary.recetas;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.top_culinary.R;
import com.example.top_culinary.cocina.RecetacionActivity;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Receta;

import java.util.List;

public class DetallesRecetaUsuarioActivity extends AppCompatActivity {
    // Declaración de las variables
    private DBHandler dbHandler;

    // Declaración de los widgets
    private ImageButton buttonAtras;
    private ImageButton buttonFavoritos;
    private ImageView imageViewReceta;
    private TextView textViewTituloReceta;
    private TextView textViewListaIngredientes;
    private TextView textViewListaPasos;
    private Button buttonComenzarReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_receta_usuario);

        // Inicialización de la DB Local
        dbHandler = new DBHandler(this);

        // Inicialización de los widgets
        initWidgets();

        // Obtención del nombre de la receta a través del intent
        String nombreReceta = getIntent().getStringExtra("nombreReceta");

        // Configuración de los listeners
        setupListeners();

        // Obtención y visualización de los detalles de la receta
        displayRecetaDetails(nombreReceta);
    }

    private void initWidgets() {
        buttonAtras = findViewById(R.id.imageButtonBack);
        buttonFavoritos = findViewById(R.id.imageButtonFavorito);
        imageViewReceta = findViewById(R.id.imageViewRecetaUsuario);
        textViewTituloReceta = findViewById(R.id.textviewTituloReceta);
        textViewListaIngredientes = findViewById(R.id.textviewListaIngredientes);
        textViewListaPasos = findViewById(R.id.textviewListaPasos);
        buttonComenzarReceta = findViewById(R.id.buttonComenzarReceta);

        // Animación del botón de comenzar receta
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_animation);
        buttonComenzarReceta.startAnimation(pulseAnimation);
    }

    private void setupListeners() {
        buttonAtras.setOnClickListener(v -> {
            Intent intent = new Intent(DetallesRecetaUsuarioActivity.this, AniadirRecetasActivity.class);
            startActivity(intent);
            finish();
        });

        buttonFavoritos.setOnClickListener(v -> toggleFavorito(buttonFavoritos));

        buttonComenzarReceta.setOnClickListener(v -> {
            String nombreReceta = textViewTituloReceta.getText().toString();
            Intent intent = new Intent(DetallesRecetaUsuarioActivity.this, RecetacionActivity.class);
            intent.putExtra("nombreRecetaUsuario", nombreReceta);
            startActivity(intent);
            finish();
        });
    }

    private void displayRecetaDetails(String nombreReceta) {
        Receta receta = dbHandler.obtenerRecetaUsuarioPorNombre(nombreReceta);

        if (receta != null) {
            // Cargamos la imagen dentro del imageView
            Glide.with(this)
                    .load(receta.getImagen())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageViewReceta);

            // Cargamos el título de la receta
            textViewTituloReceta.setText(receta.getTitulo());

            // Cargamos los ingredientes
            textViewListaIngredientes.setText(formatList(receta.getIngredientes()));

            // Cargamos los pasos
            textViewListaPasos.setText(formatSteps(receta.getPasos()));
        } else {
            Log.e("DetallesRecetaUsuario", "No se pudo obtener los detalles de la receta");
        }
    }

    private String formatList(List<String> items) {
        StringBuilder formattedList = new StringBuilder();
        for (String item : items) {
            formattedList.append("- ").append(item).append("\n");
        }
        return formattedList.toString();
    }

    private String formatSteps(List<String> steps) {
        StringBuilder formattedSteps = new StringBuilder();
        for (int i = 0; i < steps.size(); i++) {
            formattedSteps.append(i + 1).append(". ").append(steps.get(i)).append("\n");
        }
        return formattedSteps.toString();
    }

    private void toggleFavorito(ImageButton imageButton) {
        int currentImageResource = imageButton.getDrawable().getConstantState().equals(ContextCompat.getDrawable(imageButton.getContext(), R.drawable.favorito).getConstantState()) ? R.drawable.sin_favorito : R.drawable.favorito;
        imageButton.setImageResource(currentImageResource);

        // Aplica un ColorFilter para cambiar el color de la imagen
        int color = ContextCompat.getColor(imageButton.getContext(), currentImageResource == R.drawable.favorito ? R.color.naranja_more : R.color.black);
        imageButton.setColorFilter(color, PorterDuff.Mode.SRC_IN);

        // Aplica una animación de desvanecimiento
        imageButton.animate().alpha(0.5f).setDuration(300).withEndAction(() -> imageButton.animate().alpha(1f).setDuration(300));
    }
}
