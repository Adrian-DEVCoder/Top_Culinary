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
import com.example.top_culinary.foro.ForoActivity;
import com.example.top_culinary.model.Receta;
import com.example.top_culinary.perfil.PerfilActivity;
import com.example.top_culinary.recetas.AniadirRecetasActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CocinaActivity extends AppCompatActivity {
    // Declaracion de las variables
    private DBHandler dbHandler;
    private FirebaseFirestore firestoreDB;
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
        // Inicializacion de la BD Local
        dbHandler = new DBHandler(this);
        // Inicializacion de la BD Online
        firestoreDB = FirebaseFirestore.getInstance();
        // Obtenemos los datos del usuario
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid(); // Obtenemos el uid del usuario actualmente autenticado
        // Consulta para obtener el documento del usuario
        firestoreDB.collection("usuarios").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // El documento existe, extraemos el nombre de usuario
                                String nombreUsuario = document.getString("display_name");
                                // Inicializacion de los widgets
                                textViewHola = findViewById(R.id.txvHola);
                                textViewNSaludo = findViewById(R.id.txvNombreUsuario);
                                textViewNSaludo.setText(nombreUsuario); // Establecemos el nombre de usuario
                                // El resto de tu código de inicialización de widgets...
                            } else {
                                Log.d("Firestore", "No se encontró el documento del usuario");
                                // Manejar el caso en que el documento no se encuentra
                            }
                        } else {
                            Log.d("Firestore", "Error al obtener el documento del usuario", task.getException());
                            // Manejar el error
                        }
                    }
                });
        // Inicializacion de los widgets
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
                iniciarAniadirRecetas(textViewNSaludo.getText().toString());
            }
        });
        buttonCesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCesta(textViewNSaludo.getText().toString());
            }
        });
        buttonForo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarForo(textViewNSaludo.getText().toString());
            }
        });
        buttonPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarPerfil(textViewNSaludo.getText().toString());
            }
        });
        imageViewPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarPerfil(textViewNSaludo.getText().toString());
            }
        });
    }

    /**
     * Inicia la actividad de Añadir Recetas
     */
    private void iniciarAniadirRecetas(String nombreFormateado){
        Intent intent = new Intent(CocinaActivity.this, AniadirRecetasActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    /**
     * Inicia la actividad de Cesta
     */
    private void iniciarCesta(String nombreFormateado){
        Intent intent = new Intent(CocinaActivity.this, CestaActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    /**
     * Inicia la actividad de Foro
     */
    private void iniciarForo(String nombreFormateado){
        Intent intent = new Intent(CocinaActivity.this, ForoActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }

    /**
     * Inicia la actividad de Perfil
     */
    private void iniciarPerfil(String nombreFormateado){
        Intent intent = new Intent(CocinaActivity.this, PerfilActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
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