package com.example.top_culinary.cocina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
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

public class DetallesRecetaActivity extends AppCompatActivity {
    // Declaracion de las variables
    private DBHandler dbHandler;
    // Declaracion de los widgets
    ImageButton imageButtonAtras;
    ImageButton imageButtonFavorito;
    ImageView imageViewReceta;
    ScrollView scrollViewReceta;
    TextView textViewTitulo;
    TextView textViewDescripcion;
    TextView textViewIngredientes;
    TextView textViewListaIngredientes;
    TextView textViewEquipamiento;
    TextView textViewListaEquipamiento;
    TextView textViewPasos;
    TextView textViewListaPasos;
    Button buttonComenzarReceta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_receta);
        // Obtenemos el nombre de la receta a traves del intent
        Intent intent = getIntent();
        String nombreReceta = intent.getStringExtra("nombreReceta");
        dbHandler = new DBHandler(this);
        scrollViewReceta = findViewById(R.id.scrollViewDetalles);
        imageButtonAtras = findViewById(R.id.imageButtonBack);
        imageButtonFavorito = findViewById(R.id.imageButtonFavorito);
        imageViewReceta = findViewById(R.id.imageViewReceta);
        textViewTitulo = findViewById(R.id.textviewTituloReceta);
        textViewDescripcion = findViewById(R.id.textviewDescripcion);
        textViewIngredientes = findViewById(R.id.textviewIngredientes);
        textViewListaIngredientes = findViewById(R.id.textviewListaIngredientes);
        textViewEquipamiento = findViewById(R.id.textviewEquipamiento);
        textViewListaEquipamiento = findViewById(R.id.textviewListaEquipamiento);
        textViewPasos = findViewById(R.id.textviewPasos);
        textViewListaPasos = findViewById(R.id.textviewListaPasos);
        // Boton para comenzar a realizar la receta con una animacion
        buttonComenzarReceta = findViewById(R.id.buttonComenzarReceta);
        Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_animation);
        buttonComenzarReceta.startAnimation(pulseAnimation);

        // Listener para el boton de volver atras
        imageButtonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetallesRecetaActivity.this, CocinaActivity.class));
                finish();
            }
        });

        // Listener para el boton de favorito
        imageButtonFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorito(imageButtonFavorito);
            }
        });

        Receta receta = dbHandler.obtenerRecetaPorNombre(nombreReceta);

        String urlImagen = receta.getImagen();
        Glide.with(this)
                .load(urlImagen)
                .into(imageViewReceta);

        textViewTitulo.setText(receta.getTitulo());
        textViewDescripcion.setText(receta.getDescripcion());

        StringBuilder stbIngredientes = new StringBuilder();
        for(String ingrediente : receta.getIngredientes()){
            stbIngredientes.append(ingrediente+"\n");
        }
        textViewListaIngredientes.setText(stbIngredientes.toString());

        StringBuilder stbEquipamiento = new StringBuilder();
        for(String equipamiento : receta.getEquipamiento()){
            stbEquipamiento.append(equipamiento+"\n");
        }
        textViewListaEquipamiento.setText(stbEquipamiento.toString());

        StringBuilder stbPasos = new StringBuilder();
        for(int i=0;i<receta.getPasos().size();i++){
            stbPasos.append(receta.getPasos().get(i)+"\n");
        }
        textViewListaPasos.setText(stbPasos.toString());

        // Listener para el boton de comenzar recetas
        buttonComenzarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesRecetaActivity.this, RecetacionActivity.class);
                intent.putExtra("nombreReceta",receta.getTitulo());
                startActivity(intent);
                finish();
            }
        });
    }

    private void toggleFavorito(ImageButton imageButton) {
        int currentImageResource = imageButton.getDrawable().getConstantState().equals(ContextCompat.getDrawable(imageButton.getContext(), R.drawable.favorito).getConstantState()) ? R.drawable.sin_favorito : R.drawable.favorito;
        imageButton.setImageResource(currentImageResource);
        // Aplicar ColorFilter para cambiar el color de la imagen
        int color = ContextCompat.getColor(imageButton.getContext(), currentImageResource == R.drawable.favorito ? R.color.naranja_more : R.color.black);
        imageButton.setColorFilter(color, PorterDuff.Mode.SRC_IN);

        // Opcional: Agregar una animación de desvanecimiento si lo deseas
        imageButton.animate().alpha(0.5f).setDuration(300).withEndAction(new Runnable() {
            @Override
            public void run() {
                imageButton.animate().alpha(1f).setDuration(300);
            }
        });
    }

}