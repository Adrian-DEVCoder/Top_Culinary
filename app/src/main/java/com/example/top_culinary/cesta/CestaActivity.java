package com.example.top_culinary.cesta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;

import com.example.top_culinary.R;
import com.example.top_culinary.adapter.AdapterIngrediente;
import com.example.top_culinary.chat.ChatActivity;
import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.interfaces.OnItemSelectListener;
import com.example.top_culinary.model.Ingrediente;
import com.example.top_culinary.perfil.PerfilActivity;
import com.example.top_culinary.recetas.AniadirRecetasActivity;

import java.util.ArrayList;
import java.util.List;

public class CestaActivity extends AppCompatActivity implements OnItemSelectListener {
    // Declaracion de las variables
    private List<Ingrediente> ingredienteList;
    private List<Ingrediente> ingredientesComprados;
    private AdapterIngrediente adapterIngrediente;
    // Declaracion de los widgets
    RecyclerView recyclerViewIngredientes;
    ImageButton buttonCarrito;
    ImageButton buttonBuscarIngredientes;
    ImageButton buttonAniadirRecetas;
    ImageButton buttonCocina;
    ImageButton buttonForo;
    ImageButton buttonPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cesta);

        // Inicializacion de los widgets
        initWidgets();

        // Obtenemos la lista de ingredientes
        ingredienteList = obtenerIngredientes();

        // Obtenemos el nombre del intent
        Intent intent = getIntent();
        String nombreFormateado = intent.getStringExtra("nombreFormateado");
        ingredientesComprados = intent.getParcelableArrayListExtra("ingredientesComprados");
        if (ingredientesComprados == null) {
            ingredientesComprados = new ArrayList<>();
        }

        // Configuracion del RecyclerView
        setupRecyclerView();

        // Listener de los botones de la actividad
        buttonCarrito.setOnClickListener(v -> iniciarCarrito(ingredientesComprados, nombreFormateado));
        buttonBuscarIngredientes.setOnClickListener(v -> iniciarBuscarIngredientes(nombreFormateado));
        // Listener de los botones inferiores
        buttonAniadirRecetas.setOnClickListener(v -> iniciarAniadirRecetas(nombreFormateado));
        buttonCocina.setOnClickListener(v -> iniciarCocina(nombreFormateado));
        buttonForo.setOnClickListener(v -> iniciarForo(nombreFormateado));
        buttonPerfil.setOnClickListener(v -> iniciarPerfil(nombreFormateado));
    }

    private void initWidgets() {
        recyclerViewIngredientes = findViewById(R.id.recyclerViewIngredientesComprados);
        buttonCarrito = findViewById(R.id.imageButtonCarrito);
        buttonBuscarIngredientes = findViewById(R.id.imageButtonBuscarIngredientes);
        buttonAniadirRecetas = findViewById(R.id.imgBRecetas);
        buttonCocina = findViewById(R.id.imgBCocina);
        buttonForo = findViewById(R.id.imgBForo);
        buttonPerfil = findViewById(R.id.imgBPerfil);
    }

    private void setupRecyclerView() {
        // Crea un GridLayoutManager con 2 columnas
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewIngredientes.setLayoutManager(layoutManager);
        adapterIngrediente = new AdapterIngrediente(ingredienteList, this);
        recyclerViewIngredientes.setAdapter(adapterIngrediente);
    }

    private List<Ingrediente> obtenerIngredientes() {
        // Obtener ingredientes de la base de datos o cualquier otra fuente de datos
        DBHandler dbHandler = new DBHandler(this);
        return dbHandler.obtenerIngredientes();
    }

    /**
     * Inicia la actividad de la Busqueda de Ingredientes
     */
    private void iniciarBuscarIngredientes(String nombreFormateado) {
        Intent intent = new Intent(this, BuscarIngredientesActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        intent.putParcelableArrayListExtra("ingredientesComprados", new ArrayList<>(ingredientesComprados));
        startActivity(intent);
        finish();
    }

    /**
     * Inicia la actividad del Carrito
     *
     * @param ingredientesComprados añadidos por el usuario
     */
    private void iniciarCarrito(List<Ingrediente> ingredientesComprados, String nombreFormateado) {
        Intent intent = new Intent(this, CarritoActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        intent.putParcelableArrayListExtra("ingredientesComprados", new ArrayList<>(ingredientesComprados));
        startActivity(intent);
        finish();
    }

    /**
     * Inicia la actividad de Añadir Recetas
     */
    private void iniciarAniadirRecetas(String nombreFormateado) {
        Intent intent = new Intent(this, AniadirRecetasActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    /**
     * Inicia la actividad de Cesta
     */
    private void iniciarCocina(String nombreFormateado) {
        Intent intent = new Intent(this, CocinaActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    /**
     * Inicia la actividad de Foro
     */
    private void iniciarForo(String nombreFormateado) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    /**
     * Inicia la actividad de Perfil
     */
    private void iniciarPerfil(String nombreFormateado) {
        Intent intent = new Intent(this, PerfilActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onItemSelect(Ingrediente ingredienteSeleccionado) {
        boolean ingredienteEncontrado = false;
        for (int i = 0; i < ingredientesComprados.size(); i++) {
            if (ingredientesComprados.get(i).getNombre().equals(ingredienteSeleccionado.getNombre()) &&
                    ingredientesComprados.get(i).getImagen().equals(ingredienteSeleccionado.getImagen())) {
                ingredientesComprados.get(i).setCantidad(ingredientesComprados.get(i).getCantidad() + 1);
                ingredienteEncontrado = true;
                break;
            }
        }
        if (!ingredienteEncontrado) {
            ingredienteSeleccionado.setCantidad(1); // Asegurarse de que la cantidad inicial sea 1
            ingredientesComprados.add(ingredienteSeleccionado);
        }
        adapterIngrediente.notifyDataSetChanged();
    }
}
