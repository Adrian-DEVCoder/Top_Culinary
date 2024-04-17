package com.example.top_culinary.recetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.top_culinary.R;
import com.example.top_culinary.cesta.CestaActivity;
import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.foro.ForoActivity;
import com.example.top_culinary.perfil.PerfilActivity;

public class AniadirRecetasActivity extends AppCompatActivity {
    // Declaracion de los widgets
    ImageButton buttonCesta;
    ImageButton buttonCocina;
    ImageButton buttonForo;
    ImageButton buttonPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_recetas);
        // Botones de la botonera inferior
        buttonCesta = findViewById(R.id.imgBCesta);
        buttonCocina = findViewById(R.id.imgBCocina);
        buttonForo = findViewById(R.id.imgBForo);
        buttonPerfil = findViewById(R.id.imgBPerfil);
        // Listener de los botones inferiores
        buttonCesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCesta();
            }
        });
        buttonCocina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCocina();
            }
        });
        buttonForo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarForo();
            }
        });
        buttonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarPerfil();
            }
        });
    }

    /**
     * Inicia la actividad de Añadir Recetas
     */
    private void iniciarCesta(){
        Intent intent = new Intent(this, CestaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

    /**
     * Inicia la actividad de Cesta
     */
    private void iniciarCocina(){
        Intent intent = new Intent(this, CocinaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

    /**
     * Inicia la actividad de Foro
     */
    private void iniciarForo(){
        Intent intent = new Intent(this, ForoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

    /**
     * Inicia la actividad de Perfil
     */
    private void iniciarPerfil(){
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

}