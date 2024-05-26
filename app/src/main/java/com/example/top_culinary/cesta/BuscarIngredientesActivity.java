package com.example.top_culinary.cesta;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.top_culinary.R;
import com.example.top_culinary.adapter.AdapterIngredienteBuscador;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.interfaces.OnItemSelectListener;
import com.example.top_culinary.model.Ingrediente;

import java.util.ArrayList;
import java.util.List;

public class BuscarIngredientesActivity extends AppCompatActivity implements OnItemSelectListener {
    // Declaracion de las variables
    private DBHandler dbHandler;
    private List<Ingrediente> ingredienteBuscadorList;
    private ArrayList<Ingrediente> ingredientesCompradosList;
    private AdapterIngredienteBuscador adapterIngredienteBuscador;
    // Declaracion de los widgets
    ImageButton buttonAtras;
    CardView cardViewBuscador;
    SearchView searchViewIngredientes;
    RecyclerView recyclerViewIngredientes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar_ingredientes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Inicializacion de la BD Local
        dbHandler = new DBHandler(this);
        ingredienteBuscadorList = dbHandler.obtenerIngredientes();
        // Obtencion de los datos del nombre del usuario del intent
        Intent intent = getIntent();
        String nombreFormateado = intent.getStringExtra("nombreFormateado");
        if(intent.getParcelableArrayListExtra("ingredientesComprados") != null) {
            ingredientesCompradosList = intent.getParcelableArrayListExtra("ingredientesComprados");
        } else {
            ingredientesCompradosList = new ArrayList<>();
        }
        // Inicializacion de los widgets
        buttonAtras = findViewById(R.id.imageButtonAtrasBuscar);
        cardViewBuscador = findViewById(R.id.cardViewBuscador);
        searchViewIngredientes = findViewById(R.id.searchViewIngredientes);
        searchViewIngredientes.clearFocus();
        searchViewIngredientes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarIngredientes(newText);
                return true;
            }
        });
        recyclerViewIngredientes = findViewById(R.id.recyclerViewIngredientesComprados);
        recyclerViewIngredientes.setLayoutManager(new LinearLayoutManager(this));
        adapterIngredienteBuscador = new AdapterIngredienteBuscador(ingredienteBuscadorList, this);
        recyclerViewIngredientes.setAdapter(adapterIngredienteBuscador);
        // Listener del boton de volver atras
        buttonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCesta(nombreFormateado);
            }
        });
    }

    /**
     * Filtra la busqueda del usuario en tiempo real
     * @param filtro contenido del nombre del ingrediente
     */
    private void filtrarIngredientes(String filtro) {
        List<Ingrediente> ingredientesFiltrados = new ArrayList<>();
        for(Ingrediente ingrediente : ingredienteBuscadorList) {
            if(ingrediente.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
                ingredientesFiltrados.add(ingrediente);
            }
        }
        if(ingredientesFiltrados.isEmpty()) {
            mostrarToast("No hay datos");
        } else {
            adapterIngredienteBuscador.setListaIngredientesFiltrados(ingredientesFiltrados);
        }
    }

    /**
     * Muestra un toast al usuario
     * @param mensaje a incluir en el toast
     */
    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    /**
     * Inicia la actividad de la cesta
     * @param nombreFormateado del usuario
     */
    private void iniciarCesta (String nombreFormateado) {
        Intent intent = new Intent(this, CestaActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        intent.putParcelableArrayListExtra("ingredientesComprados", ingredientesCompradosList);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelect(Ingrediente ingredienteSeleccionado) {
        boolean ingredienteEncontrado = false;
        for(int i = 0; i < ingredientesCompradosList.size(); i++) {
            if(ingredientesCompradosList.get(i).getNombre().equals(ingredienteSeleccionado.getNombre()) &&
            ingredientesCompradosList.get(i).getImagen().equals(ingredienteSeleccionado.getImagen())) {
                ingredientesCompradosList.get(i).setCantidad(ingredientesCompradosList.get(i).getCantidad() + 1);
                ingredienteEncontrado = true;
                break;
            }
            if(!ingredienteEncontrado) {
                ingredientesCompradosList.add(ingredienteSeleccionado);
            }
            adapterIngredienteBuscador.notifyDataSetChanged();
        }
    }
}