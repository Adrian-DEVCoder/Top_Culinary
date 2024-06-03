package com.example.top_culinary.cesta;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.top_culinary.R;
import com.example.top_culinary.adapter.AdapterIngredientesComprados;
import com.example.top_culinary.model.Ingrediente;

import java.util.ArrayList;
import java.util.List;

public class CarritoActivity extends AppCompatActivity {
    // Declaracion de las variables
    private List<Ingrediente> ingredientesComprados;
    private AdapterIngredientesComprados adapterIngredientesComprados;

    // Declaracion de los widgets
    private ImageButton buttonAtras;
    private RecyclerView recyclerViewIngredientesComprados;
    private TextView textViewPrecioTotal;
    private Button buttonRealizarPago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carrito);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtencion de los datos a traves del intent
        ingredientesComprados = getIntent().getParcelableArrayListExtra("ingredientesComprados");
        if (ingredientesComprados == null) {
            ingredientesComprados = new ArrayList<>();
        }

        // Inicializacion de los widgets
        initWidgets();

        // Configuracion del RecyclerView
        setupRecyclerView();

        // Actualizacion del precio total
        actualizarPrecioTotal();

        // Listener del boton de volver atras
        buttonAtras.setOnClickListener(v -> volverACesta());
    }

    private void initWidgets() {
        buttonAtras = findViewById(R.id.imageButtonAtrasCarrito);
        recyclerViewIngredientesComprados = findViewById(R.id.recyclerViewIngredientesComprados);
        textViewPrecioTotal = findViewById(R.id.textViewPrecioTotal);
        buttonRealizarPago = findViewById(R.id.buttonRealizarPago);
    }

    private void setupRecyclerView() {
        recyclerViewIngredientesComprados.setLayoutManager(new LinearLayoutManager(this));
        adapterIngredientesComprados = new AdapterIngredientesComprados(ingredientesComprados, this);
        recyclerViewIngredientesComprados.setAdapter(adapterIngredientesComprados);
        adapterIngredientesComprados.setOnItemChangeListener(this::actualizarPrecioTotal);
    }

    private void volverACesta() {
        Intent intent = new Intent(CarritoActivity.this, CestaActivity.class);
        intent.putParcelableArrayListExtra("ingredientesComprados", new ArrayList<>(ingredientesComprados));
        startActivity(intent);
        finish();
    }

    /**
     * Actualiza el precio total del carrito
     */
    public void actualizarPrecioTotal() {
        double precioTotal = 0;
        for (Ingrediente ingrediente : ingredientesComprados) {
            double precio = 0;
            try {
                precio = Double.parseDouble(ingrediente.getPrecio().replace("€", "").trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            double totalItem = precio * ingrediente.getCantidad();
            precioTotal += totalItem;
        }
        String precioTotalFormateado = String.format("%.2f€", precioTotal);
        textViewPrecioTotal.setText("Total: " + precioTotalFormateado);
    }
}
