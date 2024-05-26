package com.example.top_culinary.cesta;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Ingrediente;

import java.util.ArrayList;
import java.util.List;

public class CarritoActivity extends AppCompatActivity {
    // Declaracion de las variables
    private DBHandler dbHandler;
    private List<Ingrediente> ingredientesComprados;
    // Declaracion de los widgets
    ImageButton buttonAtras;
    RecyclerView recyclerViewIngredientesComprados;
    TextView textViewPrecioTotal;
    Button buttonRealizarPago;
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
        // Inicializacion de la DB Local
        dbHandler = new DBHandler(this);
        // Obtencion de los datos a traves del intent
        Intent intent = getIntent();
        if(intent.getParcelableArrayListExtra("ingredientesComprados") != null) {
            ingredientesComprados = intent.getParcelableArrayListExtra("ingredientesComprados");
        } else {
            ingredientesComprados = new ArrayList<>();
        }
        // Inicializacion de los widgets
        buttonAtras = findViewById(R.id.imageButtonAtrasCarrito);
        recyclerViewIngredientesComprados = findViewById(R.id.recyclerViewIngredientesComprados);
        textViewPrecioTotal = findViewById(R.id.textViewPrecioTotal);
        buttonRealizarPago = findViewById(R.id.buttonRealizarPago);
        // Configuracion del RecyclerView
        recyclerViewIngredientesComprados.setLayoutManager(new LinearLayoutManager(this));
        AdapterIngredientesComprados adapterIngredientesComprados = new AdapterIngredientesComprados(ingredientesComprados, this);
        recyclerViewIngredientesComprados.setAdapter(adapterIngredientesComprados);
        // Actualizacion del precio total
        actualizarPrecioTotal(ingredientesComprados);
        // Listener del boton de volver atras
        buttonAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarritoActivity.this, CestaActivity.class);
                intent.putParcelableArrayListExtra("ingredientesComprados", new ArrayList<>(ingredientesComprados));
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Actualiza el precio total del carrito
     * @param ingredientesCompradosList lista de ingredientes seleccionados por el usuario
     */
    public void actualizarPrecioTotal(List<Ingrediente> ingredientesCompradosList) {
        double precioTotal = 0;
        for (Ingrediente ingrediente : ingredientesCompradosList) {
            double precio = Double.parseDouble(ingrediente.getPrecio().replace("€", "").trim());
            double totalItem = precio * ingrediente.getCantidad();
            precioTotal += totalItem;
        }
        String precioTotalFormateado = String.format("%.2f€", precioTotal);
        String sPrecioTotal = "Total: " + precioTotalFormateado;
        textViewPrecioTotal.setText(sPrecioTotal);
    }
}