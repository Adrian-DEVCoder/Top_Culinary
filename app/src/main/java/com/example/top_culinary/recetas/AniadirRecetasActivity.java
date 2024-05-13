package com.example.top_culinary.recetas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.top_culinary.R;
import com.example.top_culinary.adapter.AdapterRecetaUsuario;
import com.example.top_culinary.cesta.CestaActivity;
import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.chat.ChatActivity;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Receta;
import com.example.top_culinary.perfil.PerfilActivity;

import java.util.List;

public class AniadirRecetasActivity extends AppCompatActivity {
    // Declaracion de las variables
    private DBHandler dbHandler;
    private List<Receta> recetaUsuarioList;
    private AdapterRecetaUsuario adapterRecetaUsuario;
    // Declaracion de los widgets
    Button buttonAnadirReceta;
    TextView textViewMisRecetas;
    RecyclerView recyclerViewRecetasAnadidas;
    ImageButton buttonCesta;
    ImageButton buttonCocina;
    ImageButton buttonForo;
    ImageButton buttonPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_recetas);
        // Inicializacion de la DB Local
        dbHandler = new DBHandler(this);
        // Obtencion del nombre del usuario
        Intent intent = getIntent();
        String nombreFormateado = intent.getStringExtra("nombreFormateado");
        // Botones de la botonera inferior
        buttonAnadirReceta = findViewById(R.id.buttonAnadirReceta);
        textViewMisRecetas = findViewById(R.id.textViewMisRecetas);
        recyclerViewRecetasAnadidas = findViewById(R.id.recyclerViewMisRecetas);
        recyclerViewRecetasAnadidas.setLayoutManager(new LinearLayoutManager(this));
        recetaUsuarioList = dbHandler.obtenerRecetasUsuario();
        adapterRecetaUsuario = new AdapterRecetaUsuario(recetaUsuarioList);
        recyclerViewRecetasAnadidas.setAdapter(adapterRecetaUsuario);
        buttonCesta = findViewById(R.id.imgBCesta);
        buttonCocina = findViewById(R.id.imgBCocina);
        buttonForo = findViewById(R.id.imgBForo);
        buttonPerfil = findViewById(R.id.imgBPerfil);
        // Listener del boton de añadir recetas
        buttonAnadirReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarAnadirRecetas(nombreFormateado);
            }
        });
        // Listener de los botones inferiores
        buttonCesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCesta(nombreFormateado);
            }
        });
        buttonCocina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCocina(nombreFormateado);
            }
        });
        buttonForo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarForo(nombreFormateado);
            }
        });
        buttonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarPerfil(nombreFormateado);
            }
        });
    }

    /**
     * Inicia la actividad Añadir Recetas
     * @param nombreFormateado
     */
    private void iniciarAnadirRecetas(String nombreFormateado) {
        Intent intent = new Intent(this, NombreImagenRecetaActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        finish();
    }

    /**
     * Inicia la actividad Cesta
     */
    private void iniciarCesta(String nombreFormateado){
        Intent intent = new Intent(this, CestaActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

    /**
     * Inicia la actividad Cocina
     */
    private void iniciarCocina(String nombreFormateado){
        Intent intent = new Intent(this, CocinaActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

    /**
     * Inicia la actividad Chat
     */
    private void iniciarForo(String nombreFormateado){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

    /**
     * Inicia la actividad de Perfil
     */
    private void iniciarPerfil(String nombreFormateado){
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

}