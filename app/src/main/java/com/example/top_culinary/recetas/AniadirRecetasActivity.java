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
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class AniadirRecetasActivity extends AppCompatActivity {
    // Declaración de las variables
    private DBHandler dbHandler;
    private List<Receta> recetaUsuarioList;
    private AdapterRecetaUsuario adapterRecetaUsuario;

    // Declaración de los widgets
    private Button buttonAnadirReceta;
    private RecyclerView recyclerViewRecetasAnadidas;
    private ImageButton buttonCesta;
    private ImageButton buttonCocina;
    private ImageButton buttonForo;
    private ImageButton buttonPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aniadir_recetas);

        // Inicialización de la BD Local
        dbHandler = new DBHandler(this);

        // Inicialización de los widgets
        initWidgets();

        // Configuración de RecyclerView
        setupRecyclerView();

        // Configuración de los listeners de los botones
        setupButtonListeners();

        // Obtención del nombre del usuario
        Intent intent = getIntent();
        String nombreFormateado = intent.getStringExtra("nombreFormateado");

        // Listener del botón de añadir recetas
        buttonAnadirReceta.setOnClickListener(v -> iniciarAnadirRecetas(nombreFormateado));
    }

    private void initWidgets() {
        buttonAnadirReceta = findViewById(R.id.buttonAnadirReceta);
        recyclerViewRecetasAnadidas = findViewById(R.id.recyclerViewMisRecetas);
        buttonCesta = findViewById(R.id.imgBCesta);
        buttonCocina = findViewById(R.id.imgBCocina);
        buttonForo = findViewById(R.id.imgBForo);
        buttonPerfil = findViewById(R.id.imgBPerfil);
    }

    private void setupRecyclerView() {
        recyclerViewRecetasAnadidas.setLayoutManager(new LinearLayoutManager(this));
        recetaUsuarioList = dbHandler.obtenerRecetasUsuario();
        adapterRecetaUsuario = new AdapterRecetaUsuario(recetaUsuarioList);
        recyclerViewRecetasAnadidas.setAdapter(adapterRecetaUsuario);
    }

    private void setupButtonListeners() {
        buttonCesta.setOnClickListener(v -> iniciarCesta());
        buttonCocina.setOnClickListener(v -> iniciarCocina());
        buttonForo.setOnClickListener(v -> iniciarForo());
        buttonPerfil.setOnClickListener(v -> iniciarPerfil());
    }

    private void iniciarAnadirRecetas(String nombreFormateado) {
        Intent intent = new Intent(this, NombreImagenRecetaActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        finish();
    }

    private void iniciarCesta() {
        Intent intent = new Intent(this, CestaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void iniciarCocina() {
        Intent intent = new Intent(this, CocinaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void iniciarForo() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void iniciarPerfil() {
        Intent intent = new Intent(this, PerfilActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
