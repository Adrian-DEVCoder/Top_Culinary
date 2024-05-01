package com.example.top_culinary.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.top_culinary.R;
import com.example.top_culinary.cesta.CestaActivity;
import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.foro.ForoActivity;
import com.example.top_culinary.recetas.AniadirRecetasActivity;

public class PerfilActivity extends AppCompatActivity {
    // Declaracion de los widgets
    TextView textViewNomUsuario;
    ImageView imageViewAvatar;
    TextView textViewSeguidores;
    TextView textViewNumSeguidores;
    View viewLineaDivisora;
    TextView textViewSeguidos;
    TextView textViewNumSeguidos;
    TextView textViewNumRecetas;
    TextView textViewRecetasPublicadas;
    Button buttonSeguir;
    ImageButton buttonAniadirRecetas;
    ImageButton buttonCesta;
    ImageButton buttonCocina;
    ImageButton buttonForo;
    LinearLayout linearLayoutBotoneraInferior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        // Obtenemos el nombre del usuario del intent
        Intent intent = getIntent();
        String nombreFormateado = intent.getStringExtra("nombreFormateado");
        // Botones de la botonera inferior
        textViewNomUsuario = findViewById(R.id.textViewNomUsuario);
        textViewNomUsuario.setText(nombreFormateado);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        imageViewAvatar.setImageResource(R.drawable.avatar);
        textViewSeguidores = findViewById(R.id.textViewSeguidores);
        textViewNumSeguidores = findViewById(R.id.textViewNumSeguidores);
        viewLineaDivisora = findViewById(R.id.viewLineaDivisora);
        textViewSeguidos = findViewById(R.id.textViewSeguidos);
        textViewNumSeguidos = findViewById(R.id.textViewNumSeguidos);
        textViewNumRecetas = findViewById(R.id.textViewNumRecetas);
        textViewRecetasPublicadas = findViewById(R.id.textViewRecetas);
        buttonSeguir = findViewById(R.id.buttonSeguir);
        buttonAniadirRecetas = findViewById(R.id.imgBRecetas);
        buttonCesta = findViewById(R.id.imgBCesta);
        buttonCocina = findViewById(R.id.imgBCocina);
        buttonForo = findViewById(R.id.imgBForo);
        linearLayoutBotoneraInferior = findViewById(R.id.linearLayoutBotoneraInferior);
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
        buttonForo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarForo(nombreFormateado);
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
    private void iniciarForo(String nombreFormateado){
        Intent intent = new Intent(this, ForoActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}