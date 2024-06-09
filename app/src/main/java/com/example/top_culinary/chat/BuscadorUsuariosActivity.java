package com.example.top_culinary.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.top_culinary.R;
import com.example.top_culinary.adapter.AdapterBuscadorUsuarios;
import com.example.top_culinary.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuscadorUsuariosActivity extends AppCompatActivity {
    // Declaracion de las constantes
    private static final String TAG = "BuscadorUsuariosActivity";
    // Declaracion de las variables
    private String nombreFormateado;
    // Declaracion de los widgets
    private SearchView searchViewBuscador;
    private RecyclerView recyclerViewResultados;
    private ProgressBar progressBarCarga;
    private ImageButton buttonVolver;
    private AdapterBuscadorUsuarios adapterBuscadorUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador_usuarios);

        // Obtiene el nombre de usuario del intent
        Intent intent = getIntent();
        nombreFormateado = intent.getStringExtra("nombreFormateado");
        // Inicializa los widgets
        initWidgets();
        // Configura el SearchView/Buscador
        configurarSearchView();
        // Configura el RecyclerView
        configurarRecyclerView();
        // Configura el botón de volver
        configurarBotonVolver();
    }

    /**
     * Inicializa los widgets
     */
    private void initWidgets() {
        buttonVolver = findViewById(R.id.imageButtonVolver);
        searchViewBuscador = findViewById(R.id.searchViewBuscadorUsuarios);
        recyclerViewResultados = findViewById(R.id.recyclerViewResultados);
        progressBarCarga = findViewById(R.id.progressBarCarga);
        progressBarCarga.setVisibility(View.GONE);
    }

    /**
     * Configura el SearchView/Buscador
     */
    private void configurarSearchView() {
        // Listener de las busquedas realizadas por el usuario
        searchViewBuscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Actualiza el RecyclerView en Tiempo Real
                buscarUsuarios(newText);
                return false;
            }
        });
    }

    /**
     * Configura el Recycler View
     */
    private void configurarRecyclerView() {
        recyclerViewResultados.setLayoutManager(new LinearLayoutManager(this));
        adapterBuscadorUsuarios = new AdapterBuscadorUsuarios(new ArrayList<>(), this);
        recyclerViewResultados.setAdapter(adapterBuscadorUsuarios);
    }

    /**
     * Configura el Boton de Volver
     */
    private void configurarBotonVolver() {
        buttonVolver.setOnClickListener(v -> iniciarChat(nombreFormateado));
    }

    /**
     * Inicia la actividad del chat
     * @param nombreFormateado nombre del usuario que esta haciendo uso de la app
     */
    private void iniciarChat(String nombreFormateado) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        finish();
    }

    /**
     * Busca los usuarios en la base de datos de Firebase y los mostramos a los usuarios
     * @param query
     */
    private void buscarUsuarios(String query) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        progressBarCarga.setVisibility(View.VISIBLE);
        db.collection("usuarios")
                .whereGreaterThanOrEqualTo("display_name", query)
                .whereLessThanOrEqualTo("display_name", query + "\uf8ff")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Usuario> usuarios = queryDocumentSnapshots.toObjects(Usuario.class);
                        Log.d(TAG, "Usuarios encontrados: " + usuarios.size());
                        adapterBuscadorUsuarios.actualizarUsuarios(usuarios, nombreFormateado);
                        progressBarCarga.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error al buscar usuarios", e);
                        progressBarCarga.setVisibility(View.GONE);
                    }
                });
    }
}
