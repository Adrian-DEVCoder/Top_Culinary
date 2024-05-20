    package com.example.top_culinary.recetas;

    import android.Manifest;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.graphics.Bitmap;
    import android.graphics.PorterDuff;
    import android.net.Uri;
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
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.core.content.ContextCompat;
    import androidx.loader.app.LoaderManager;
    import androidx.loader.content.Loader;

    import com.bumptech.glide.Glide;
    import com.example.top_culinary.R;
    import com.example.top_culinary.cocina.RecetacionActivity;
    import com.example.top_culinary.database.DBHandler;
    import com.example.top_culinary.model.Receta;

    import org.jetbrains.annotations.Nullable;

    public class DetallesRecetaUsuarioActivity extends AppCompatActivity {
        // Declaracion de las variables
        private DBHandler dbHandler;
        // Declaracion de los widgets
        ImageButton buttonAtras;
        ImageButton buttonFavoritos;
        ImageView imageViewReceta;
        ScrollView scrollViewDetalles;
        LinearLayout linearLayoutDetalles;
        TextView textViewTituloReceta;
        LinearLayout linearLayoutSeparacion;
        TextView textViewIngredientes;
        TextView textViewListaIngredientes;
        TextView textViewPasos;
        TextView textViewListaPasos;
        Button buttonComenzarReceta;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detalles_receta_usuario);
            // Inicializacion de la DB Local
            dbHandler = new DBHandler(this);
            // Obtencion del nombre de usuario a traves del intent
            Intent intent = getIntent();
            String nombreReceta = intent.getStringExtra("nombreReceta");
            // Inicialización de los widgets
            buttonAtras = findViewById(R.id.imageButtonBack);
            buttonFavoritos = findViewById(R.id.imageButtonFavorito);
            imageViewReceta = findViewById(R.id.imageViewRecetaUsuario);
            scrollViewDetalles = findViewById(R.id.scrollViewDetalles);
            linearLayoutDetalles = findViewById(R.id.llDetallesReceta);
            textViewTituloReceta = findViewById(R.id.textviewTituloReceta);
            linearLayoutSeparacion = findViewById(R.id.llSeparacion);
            textViewIngredientes = findViewById(R.id.textviewIngredientes);
            textViewListaIngredientes = findViewById(R.id.textviewListaIngredientes);
            textViewPasos = findViewById(R.id.textviewPasos);
            textViewListaPasos = findViewById(R.id.textviewListaPasos);
            // Boton para comenzar a realizar la receta con una animacion
            buttonComenzarReceta = findViewById(R.id.buttonComenzarReceta);
            Animation pulseAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse_animation);
            buttonComenzarReceta.startAnimation(pulseAnimation);
            // Listener de los botones de atras y favoritos
            buttonAtras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetallesRecetaUsuarioActivity.this,AniadirRecetasActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            buttonFavoritos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleFavorito(buttonFavoritos);
                }
            });
            // Obtenemos la receta y sus detalles
            Receta receta = dbHandler.obtenerRecetaUsuarioPorNombre(nombreReceta);
            // Cargamos la imagen dentro del imageView
            String urlImagen = receta.getImagen();
            Log.d("Imagen Receta Usuario",urlImagen);
            Glide.with(this)
                    .load(receta.getImagen())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(imageViewReceta);
            // Cargamos el tituto de la receta
            textViewTituloReceta.setText(receta.getTitulo());
            // Cargamos los ingredientes
            StringBuilder stbIngredientes = new StringBuilder();
            for(String ingrediente : receta.getIngredientes()) {
                stbIngredientes.append("- ").append(ingrediente).append("\n");
            }
            textViewListaIngredientes.setText(stbIngredientes.toString());
            // Cargamos los pasos
            StringBuilder stbPasos = new StringBuilder();
            for(int i = 0; i < receta.getPasos().size(); i++) {
                stbPasos.append(i+1).append(". ").append(receta.getPasos().get(i)).append("\n");
            }
            textViewListaPasos.setText(stbPasos.toString());
            // Listener del boton de comenzar receta
            buttonComenzarReceta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetallesRecetaUsuarioActivity.this, RecetacionActivity.class);
                    intent.putExtra("nombreRecetaUsuario",receta.getTitulo());
                    startActivity(intent);
                    finish();
                }
            });
        }

        // Animacion del boton de favoritos
        private void toggleFavorito(ImageButton imageButton) {
            int currentImageResource = imageButton.getDrawable().getConstantState().equals(ContextCompat.getDrawable(imageButton.getContext(), R.drawable.favorito).getConstantState()) ? R.drawable.sin_favorito : R.drawable.favorito;
            imageButton.setImageResource(currentImageResource);
            // Aplica un ColorFilter para cambiar el color de la imagen
            int color = ContextCompat.getColor(imageButton.getContext(), currentImageResource == R.drawable.favorito ? R.color.naranja_more : R.color.black);
            imageButton.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            // Aplica una animacion de desvanecimiento
            imageButton.animate().alpha(0.5f).setDuration(300).withEndAction(new Runnable() {
                @Override
                public void run() {
                    imageButton.animate().alpha(1f).setDuration(300);
                }
            });
        }
    }