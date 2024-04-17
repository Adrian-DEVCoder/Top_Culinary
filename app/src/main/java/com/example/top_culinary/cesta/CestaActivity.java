package com.example.top_culinary.cesta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.top_culinary.R;
import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.foro.ForoActivity;
import com.example.top_culinary.perfil.PerfilActivity;
import com.example.top_culinary.recetas.AniadirRecetasActivity;

public class CestaActivity extends AppCompatActivity {
    // Declaracion de los widgets
    ImageButton buttonAniadirRecetas;
    ImageButton buttonCocina;
    ImageButton buttonForo;
    ImageButton buttonPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta);
        // Botones de la botonera inferior
        buttonAniadirRecetas = findViewById(R.id.imgBRecetas);
        buttonCocina = findViewById(R.id.imgBCocina);
        buttonForo = findViewById(R.id.imgBForo);
        buttonPerfil = findViewById(R.id.imgBPerfil);
        // Listener de los botones inferiores
        buttonAniadirRecetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarAniadirRecetas();
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
    private void iniciarAniadirRecetas(){
        Intent intent = new Intent(this, AniadirRecetasActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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