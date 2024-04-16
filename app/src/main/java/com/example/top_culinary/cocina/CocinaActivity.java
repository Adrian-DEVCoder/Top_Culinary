package com.example.top_culinary.cocina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.top_culinary.R;
import com.example.top_culinary.adapter.AdapterReceta;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Receta;

import java.util.ArrayList;
import java.util.List;

public class CocinaActivity extends AppCompatActivity {
    // Declaracion de las variables
    private DBHandler dbHandler;
    // Declaracion de los widgets
    TextView textViewHola;
    TextView textViewNSaludo;
    ImageView imageViewPerfil;
    CardView cardViewBuscador;
    TextView textViewMensaje;
    SearchView searchViewIngRec;
    RecyclerView recyclerViewRecetas;
    // Declaracion de variables
    List<Receta> recetaList;
    AdapterReceta adapterReceta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocina);
        dbHandler = new DBHandler(this);
        textViewHola = findViewById(R.id.txvHola);
        textViewNSaludo = findViewById(R.id.txvNombreUsuario);
        imageViewPerfil = findViewById(R.id.imgPerfil);
        cardViewBuscador = findViewById(R.id.cvBuscador);
        textViewMensaje = findViewById(R.id.txvDescubrir);
        // Buscador de ingredientes y recetas
        searchViewIngRec = findViewById(R.id.searchViewIngRec);
        searchViewIngRec.clearFocus();
        searchViewIngRec.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarRecetasIngredientes(newText);
                return true;
            }
        });
        recyclerViewRecetas = findViewById(R.id.recyclerViewRecetas);
        recyclerViewRecetas.setLayoutManager(new LinearLayoutManager(this));
        // Lista para las recetas a mostrar en el Recycler View de las Recetas
        recetaList = dbHandler.obtenerRecetas();
        adapterReceta = new AdapterReceta(recetaList);
        recyclerViewRecetas.setAdapter(adapterReceta);
    }

    // Filtramos la lista de las recetas o ingredientes dependiendo de lo que busque el usuario en tiempo real
    private void filtrarRecetasIngredientes(String filtro){
        List<Receta> recetasFiltradas = new ArrayList<>();
        for(Receta receta : recetaList){
            if(receta.getNombre().toLowerCase().contains(filtro.toLowerCase())){
                recetasFiltradas.add(receta);
            }
        }
        // Comprobamos si la lista de las recetas filtradas esta vacia
        if(recetasFiltradas.isEmpty()){
            String mensaje = "No hemos encontrado datos";
            mostrarToast(mensaje);
        } else {
            adapterReceta.setListaRecetasFiltradas(recetasFiltradas);
        }
    }

    // Metodo para mostrar toast a los usuarios dependiendo del mensaje indicado por parametro
    private void mostrarToast(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}