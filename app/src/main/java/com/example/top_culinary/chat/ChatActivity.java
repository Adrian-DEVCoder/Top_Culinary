package com.example.top_culinary.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.top_culinary.R;
import com.example.top_culinary.cesta.CestaActivity;
import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.perfil.PerfilActivity;
import com.example.top_culinary.recetas.AniadirRecetasActivity;

public class ChatActivity extends AppCompatActivity {
    // Declaracion de los widgets
    ImageButton buttonAniadirRecetas;
    ImageButton buttonCesta;
    ImageButton buttonCocina;
    ImageButton buttonPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // Obtencion del nombre del intent
        Intent intent = getIntent();
        String nombreFormateado = intent.getStringExtra("nombreFormateado");
        // Botones de la botonera inferior
        buttonAniadirRecetas = findViewById(R.id.imgBRecetas);
        buttonCesta = findViewById(R.id.imgBCesta);
        buttonCocina = findViewById(R.id.imgBCocina);
        buttonPerfil = findViewById(R.id.imgBPerfil);
        // Listener de los botones inferiores
        buttonAniadirRecetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarAniadirRecetas(nombreFormateado);
            }
        });
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
        buttonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarPerfil(nombreFormateado);
            }
        });
    }

    /**
     * Inicia la actividad de Añadir Recetas
     */
    private void iniciarAniadirRecetas(String nombreFormateado){
        Intent intent = new Intent(this, AniadirRecetasActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    /**
     * Inicia la actividad de Cesta
     */
    private void iniciarCesta(String nombreFormateado){
        Intent intent = new Intent(this, CestaActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    /**
     * Inicia la actividad de Foro
     */
    private void iniciarCocina(String nombreFormateado){
        Intent intent = new Intent(this, CocinaActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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