package com.example.top_culinary.cocina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.top_culinary.R;
import com.example.top_culinary.adapter.AdapterReceta;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Receta;

import java.util.List;

public class CocinaActivity extends AppCompatActivity {
    // Declaracion de las variables
    private DBHandler dbHandler;
    // Declaracion de los widgets
    TextView textViewHola;
    TextView textViewNSaludo;
    ImageView imageViewPerfil;
    CardView cardViewBuscador;
    Button buttonMReceta;
    Button buttonMIngredientes;
    TextView textViewMensaje;
    SearchView searchViewIngRec;
    RecyclerView recyclerViewRecetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocina);
        dbHandler = new DBHandler(this);
        textViewHola = findViewById(R.id.txvHola);
        textViewNSaludo = findViewById(R.id.txvNombreUsuario);
        imageViewPerfil = findViewById(R.id.imgPerfil);
        cardViewBuscador = findViewById(R.id.cvBuscador);
        buttonMReceta = findViewById(R.id.botonModoRecetas);
        buttonMIngredientes = findViewById(R.id.botonModoIngredientes);
        textViewMensaje = findViewById(R.id.txvDescubrir);
        searchViewIngRec = findViewById(R.id.searchViewIngRec);
        recyclerViewRecetas = findViewById(R.id.recyclerViewRecetas);
        recyclerViewRecetas.setLayoutManager(new LinearLayoutManager(this));
        // Lista para las recetas a mostrar en el Recycler View de las Recetas
        List<Receta> recetaList = dbHandler.obtenerRecetas();
        AdapterReceta adapterReceta = new AdapterReceta(recetaList);
        recyclerViewRecetas.setAdapter(adapterReceta);
    }
}