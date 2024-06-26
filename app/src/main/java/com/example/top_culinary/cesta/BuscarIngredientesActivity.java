package com.example.top_culinary.cesta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;

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
import com.example.top_culinary.model.Dialogo;
import com.example.top_culinary.model.Ingrediente;

import java.util.ArrayList;
import java.util.List;

public class BuscarIngredientesActivity extends AppCompatActivity implements OnItemSelectListener {
    // Declaracion de las variables
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
        DBHandler dbHandler = new DBHandler(this);
        ingredienteBuscadorList = dbHandler.obtenerIngredientes();
        // Obtencion de los datos del nombre del usuario del intent
        Intent intent = getIntent();
        String nombreFormateado = intent.getStringExtra("nombreFormateado");
        ingredientesCompradosList = intent.getParcelableArrayListExtra("ingredientesComprados");
        if (ingredientesCompradosList == null) {
            ingredientesCompradosList = new ArrayList<>();
        }
        // Inicializacion de los widgets
        initWidgets();

        // Configura el RecyclerView
        setupRecyclerView();

        // Listener del boton de volver atras
        buttonAtras.setOnClickListener(v -> iniciarCesta(nombreFormateado));

        // Configura el SearchView
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
    }

    private void initWidgets() {
        buttonAtras = findViewById(R.id.imageButtonAtrasBuscar);
        cardViewBuscador = findViewById(R.id.cardViewBuscador);
        searchViewIngredientes = findViewById(R.id.searchViewIngredientes);
        recyclerViewIngredientes = findViewById(R.id.recyclerViewIngredientesComprados);
    }

    private void setupRecyclerView() {
        recyclerViewIngredientes.setLayoutManager(new LinearLayoutManager(this));
        adapterIngredienteBuscador = new AdapterIngredienteBuscador(ingredienteBuscadorList, this);
        recyclerViewIngredientes.setAdapter(adapterIngredienteBuscador);
    }

    /**
     * Filtra la busqueda del usuario en tiempo real
     *
     * @param filtro contenido del nombre del ingrediente
     */
    private void filtrarIngredientes(String filtro) {
        List<Ingrediente> ingredientesFiltrados = new ArrayList<>();
        for (Ingrediente ingrediente : ingredienteBuscadorList) {
            if (ingrediente.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
                ingredientesFiltrados.add(ingrediente);
            }
        }
        if (ingredientesFiltrados.isEmpty()) {
            mostrarDialogo("Información","No hay resultados para la busqueda");
        } else {
            adapterIngredienteBuscador.setListaIngredientesFiltrados(ingredientesFiltrados);
        }
    }

    /**
     * Muestra un dialogo al usuario
     *
     * @param titulo a incluir en el dialogo
     * @param contenido a incluir en el dialogo
     */
    private void mostrarDialogo(String titulo, String contenido) {
        Dialogo.showDialog(this,titulo,contenido);
    }

    /**
     * Inicia la actividad de la cesta
     *
     * @param nombreFormateado del usuario
     */
    private void iniciarCesta(String nombreFormateado) {
        Intent intent = new Intent(this, CestaActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        intent.putParcelableArrayListExtra("ingredientesComprados", ingredientesCompradosList);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelect(Ingrediente ingredienteSeleccionado) {
        boolean ingredienteEncontrado = false;
        for (int i = 0; i < ingredientesCompradosList.size(); i++) {
            if (ingredientesCompradosList.get(i).getNombre().equals(ingredienteSeleccionado.getNombre()) &&
                    ingredientesCompradosList.get(i).getImagen().equals(ingredienteSeleccionado.getImagen())) {
                ingredientesCompradosList.get(i).setCantidad(ingredientesCompradosList.get(i).getCantidad() + 1);
                ingredienteEncontrado = true;
                break;
            }
        }
        if (!ingredienteEncontrado) {
            // Establece que la cantidad sea 1 al añadir un ingrediente, en el caso de que lo añada por primera vez
            ingredienteSeleccionado.setCantidad(1);
            ingredientesCompradosList.add(ingredienteSeleccionado);
        }
        adapterIngredienteBuscador.notifyDataSetChanged();
    }
}
