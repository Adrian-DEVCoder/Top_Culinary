package com.example.top_culinary.cocina;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.top_culinary.chat.ChatActivity;
import com.example.top_culinary.model.Receta;
import com.example.top_culinary.perfil.PerfilActivity;
import com.example.top_culinary.recetas.AniadirRecetasActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CocinaActivity extends AppCompatActivity {
    // Declaracion de las variables
    private DBHandler dbHandler;
    private FirebaseFirestore firestoreDB;
    private List<Receta> recetaList;
    private AdapterReceta adapterReceta;
    // Declaracion de los widgets
    private TextView textViewHola;
    private TextView textViewNSaludo;
    private ImageView imageViewPerfil;
    private SearchView searchViewIngRec;
    private RecyclerView recyclerViewRecetas;
    private ImageButton buttonAniadirRecetas;
    private ImageButton buttonCesta;
    private ImageButton buttonForo;
    private ImageButton buttonPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocina);

        // Inicializacion de la BD Local y Online
        dbHandler = new DBHandler(this);
        firestoreDB = FirebaseFirestore.getInstance();

        // Inicializacion de los widgets
        initWidgets();

        // Configurar RecyclerView
        setupRecyclerView();

        // Configurar Firestore y obtener datos de usuario
        setupFirestore();

        // Configurar listeners
        setupListeners();
    }

    private void initWidgets() {
        textViewHola = findViewById(R.id.txvHola);
        textViewNSaludo = findViewById(R.id.txvNombreUsuario);
        imageViewPerfil = findViewById(R.id.imgPerfil);
        searchViewIngRec = findViewById(R.id.searchViewIngRec);
        recyclerViewRecetas = findViewById(R.id.recyclerViewRecetas);
        buttonAniadirRecetas = findViewById(R.id.imgBRecetas);
        buttonCesta = findViewById(R.id.imgBCesta);
        buttonForo = findViewById(R.id.imgBForo);
        buttonPerfil = findViewById(R.id.imgBPerfil);
    }

    private void setupRecyclerView() {
        recyclerViewRecetas.setLayoutManager(new LinearLayoutManager(this));
        recetaList = dbHandler.obtenerRecetas();
        adapterReceta = new AdapterReceta(recetaList);
        recyclerViewRecetas.setAdapter(adapterReceta);
    }

    private void setupFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid(); // Obtenemos el uid del usuario actualmente autenticado
        // Consulta para obtener el documento del usuario
        firestoreDB.collection("usuarios").document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Si el documento existe, extraemos el nombre de usuario
                            String nombreUsuario = document.getString("display_name");
                            textViewNSaludo.setText(nombreUsuario); // Establecemos el nombre de usuario
                        } else {
                            Log.d("Firestore", "No se encontró el documento del usuario");
                        }
                    } else {
                        Log.d("Firestore", "Error al obtener el documento del usuario", task.getException());
                    }
                });
    }

    private void setupListeners() {
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

        buttonAniadirRecetas.setOnClickListener(v -> iniciarAniadirRecetas(textViewNSaludo.getText().toString()));
        buttonCesta.setOnClickListener(v -> iniciarCesta(textViewNSaludo.getText().toString()));
        buttonForo.setOnClickListener(v -> iniciarForo(textViewNSaludo.getText().toString()));
        buttonPerfil.setOnClickListener(v -> iniciarPerfil(textViewNSaludo.getText().toString()));
        imageViewPerfil.setOnClickListener(v -> iniciarPerfil(textViewNSaludo.getText().toString()));
    }

    /**
     * Inicia la actividad de Añadir Recetas
     */
    private void iniciarAniadirRecetas(String nombreFormateado){
        Intent intent = new Intent(CocinaActivity.this, AniadirRecetasActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    /**
     * Inicia la actividad de Cesta
     */
    private void iniciarCesta(String nombreFormateado){
        Intent intent = new Intent(CocinaActivity.this, CestaActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    /**
     * Inicia la actividad de Foro
     */
    private void iniciarForo(String nombreFormateado){
        Intent intent = new Intent(CocinaActivity.this, ChatActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    /**
     * Inicia la actividad de Perfil
     */
    private void iniciarPerfil(String nombreFormateado){
        Intent intent = new Intent(CocinaActivity.this, PerfilActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    /**
     * Filtramos la busqueda del usuario en tiempo real
     * @param filtro introducido por el usuario
     */
    private void filtrarRecetasIngredientes(String filtro){
        List<Receta> recetasFiltradas = new ArrayList<>();
        for (Receta receta : recetaList) {
            if (receta.getTitulo().toLowerCase().contains(filtro.toLowerCase())) {
                recetasFiltradas.add(receta);
            }
        }
        // Comprobamos si la lista de las recetas filtradas está vacía
        if (recetasFiltradas.isEmpty()) {
            mostrarToast("No hay datos");
        } else {
            adapterReceta.setListaRecetasFiltradas(recetasFiltradas);
        }
    }

    /**
     * Mostramos Toast al usuario dependiendo del mensaje introducido
     * @param mensaje introducido por el programador
     */
    private void mostrarToast(String mensaje){
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
