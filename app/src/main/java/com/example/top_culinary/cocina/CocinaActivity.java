package com.example.top_culinary.cocina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.top_culinary.R;
import com.example.top_culinary.adapter.AdapterReceta;
import com.example.top_culinary.cesta.CestaActivity;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.foro.ForoActivity;
import com.example.top_culinary.model.Receta;
import com.example.top_culinary.perfil.PerfilActivity;
import com.example.top_culinary.recetas.AniadirRecetasActivity;

import java.util.ArrayList;
import java.util.List;

public class CocinaActivity extends AppCompatActivity {
    // Declaracion de las variables
    private DBHandler dbHandler;
    private List<Receta> recetaList;
    private AdapterReceta adapterReceta;
    // Declaracion de los widgets
    TextView textViewHola;
    TextView textViewNSaludo;
    ImageView imageViewPerfil;
    CardView cardViewBuscador;
    TextView textViewMensaje;
    SearchView searchViewIngRec;
    RecyclerView recyclerViewRecetas;
    ImageButton buttonAniadirRecetas;
    ImageButton buttonCesta;
    ImageButton buttonCocina;
    ImageButton buttonForo;
    ImageButton buttonPerfil;

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
        // Botones de la botonera inferior
        buttonAniadirRecetas = findViewById(R.id.imgBRecetas);
        buttonCesta = findViewById(R.id.imgBCesta);
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
        buttonCesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCesta();
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
        Intent intent = new Intent(CocinaActivity.this, AniadirRecetasActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    /**
     * Inicia la actividad de Cesta
     */
    private void iniciarCesta(){
        Intent intent = new Intent(CocinaActivity.this, CestaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    /**
     * Inicia la actividad de Foro
     */
    private void iniciarForo(){
        Intent intent = new Intent(CocinaActivity.this, ForoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

    /**
     * Inicia la actividad de Perfil
     */
    private void iniciarPerfil(){
        Intent intent = new Intent(CocinaActivity.this, PerfilActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

    /**
     * Filtramos la busqueda del usuario en tiempo real
     * @param filtro introducido por el usuario
     */
    private void filtrarRecetasIngredientes(String filtro){
        List<Receta> recetasFiltradas = new ArrayList<>();
        for(Receta receta : recetaList){
            if(receta.getTitulo().toLowerCase().contains(filtro.toLowerCase())){
                recetasFiltradas.add(receta);
            }
        }
        // Comprobamos si la lista de las recetas filtradas esta vacia
        if(recetasFiltradas.isEmpty()){
            String mensaje = "No hay datos";
            mostrarToast(mensaje);
        } else {
            adapterReceta.setListaRecetasFiltradas(recetasFiltradas);
        }
    }

    /**
     * Mostramos Toast al usuario dependiendo del mensaje introducido
     * @param mensaje introducido por el programac
     */
    private void mostrarToast(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}