package com.example.top_culinary.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.top_culinary.R;
import com.example.top_culinary.adapter.AdapterBuscadorUsuarios;
import com.example.top_culinary.model.Usuario;
import com.example.top_culinary.perfil.PerfilActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BuscadorUsuariosActivity extends AppCompatActivity {
    // Declaracion de las variables
    private String nombreFormateado;
    // Declaracion de los widgets
    SearchView searchViewBuscador;
    RecyclerView recyclerViewResultados;
    ProgressBar progressBarCarga;
    ImageButton buttonVolver;
    AdapterBuscadorUsuarios adapterBuscadorUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscador_usuarios);
        // Obtencion del nombre del usuario a traves del intent
        Intent intent = getIntent();
        nombreFormateado = intent.getStringExtra("nombreFormateado");
        buttonVolver = findViewById(R.id.imageButtonVolver);
        searchViewBuscador = findViewById(R.id.searchViewBuscadorUsuarios);
        searchViewBuscador.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                buscarUsuarios(newText);
                return false;
            }
        });
        recyclerViewResultados = findViewById(R.id.recyclerViewResultados);
        recyclerViewResultados.setLayoutManager(new LinearLayoutManager(this));
        progressBarCarga = findViewById(R.id.progressBarCarga);
        progressBarCarga.setVisibility(View.GONE);
        adapterBuscadorUsuarios = new AdapterBuscadorUsuarios(new ArrayList<>(),this);
        recyclerViewResultados.setAdapter(adapterBuscadorUsuarios);
        buttonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarChat(nombreFormateado);
            }
        });
    }
    // Inicia la actividad del chat
    private void iniciarChat(String nombreFormateado) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        finish();
    }
    // Busca usuarios en Firebase y muestra las coincidencias en un recycler
    private void buscarUsuarios(String query) {
        progressBarCarga.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereGreaterThanOrEqualTo("display_name", query)
                .whereLessThanOrEqualTo("display_name", query + "\uf8ff")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Usuario> usuarios = queryDocumentSnapshots.toObjects(Usuario.class);
                        adapterBuscadorUsuarios.actualizarUsuarios(usuarios, nombreFormateado);
                        progressBarCarga.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase Error", "Error al buscar usuarios", e);
                        progressBarCarga.setVisibility(View.GONE);
                    }
                });
    }

}