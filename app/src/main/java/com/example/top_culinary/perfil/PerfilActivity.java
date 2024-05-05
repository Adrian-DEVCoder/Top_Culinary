package com.example.top_culinary.perfil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.top_culinary.R;
import com.example.top_culinary.cesta.CestaActivity;
import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.foro.ForoActivity;
import com.example.top_culinary.recetas.AniadirRecetasActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

public class PerfilActivity extends AppCompatActivity {
    // Declaracion de las variables
    private FirebaseFirestore firestoreDB = FirebaseFirestore.getInstance();
    private boolean esUsuarioActual = true;
    private String uidUsuarioActividad;
    private String uidUsuarioSesion;
    // Declaracion de los widgets
    TextView textViewNomUsuario;
    ImageButton buttonAjustes;
    ImageView imageViewAvatar;
    TextView textViewSeguidores;
    TextView textViewNumSeguidores;
    View viewLineaDivisora;
    TextView textViewSeguidos;
    TextView textViewNumSeguidos;
    TextView textViewNumRecetas;
    TextView textViewRecetasPublicadas;
    Button buttonSeguir;
    ImageButton buttonAniadirRecetas;
    ImageButton buttonCesta;
    ImageButton buttonCocina;
    ImageButton buttonForo;
    LinearLayout linearLayoutBotoneraInferior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        // Obtencion del usuario actualmente en la sesion
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        uidUsuarioSesion = currentUser.getUid();
        // Obtenemos el nombre del usuario del intent
        Intent intent = getIntent();
        String nombreFormateado = intent.getStringExtra("nombreFormateado");
        // Obtencion del uid del usuario actual
        obtenerUidActividad(nombreFormateado)
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            uidUsuarioActividad = task.getResult();
                            if(uidUsuarioSesion.equals(uidUsuarioActividad)) {
                                obtenerYMostrarSeguidores(uidUsuarioSesion);
                                obtenerYMostrarSeguidos(uidUsuarioSesion);
                                obtenerYMostrarNumRecetas(uidUsuarioSesion);
                                buttonSeguir.setVisibility(View.GONE);
                                buttonAjustes.setVisibility(View.VISIBLE);
                            } else {
                                obtenerYMostrarSeguidores(uidUsuarioActividad);
                                obtenerYMostrarSeguidos(uidUsuarioActividad);
                                obtenerYMostrarNumRecetas(uidUsuarioActividad);
                                buttonAjustes.setVisibility(View.GONE);
                                buttonSeguir.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
        // Botones de la botonera inferior
        textViewNomUsuario = findViewById(R.id.textViewNomUsuario);
        buttonAjustes = findViewById(R.id.imageButtonAjustes);
        textViewNomUsuario.setText(nombreFormateado);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        imageViewAvatar.setImageResource(R.drawable.avatar);
        textViewSeguidores = findViewById(R.id.textViewSeguidores);
        textViewNumSeguidores = findViewById(R.id.textViewNumSeguidores);
        viewLineaDivisora = findViewById(R.id.viewLineaDivisora);
        textViewSeguidos = findViewById(R.id.textViewSeguidos);
        textViewNumSeguidos = findViewById(R.id.textViewNumSeguidos);
        textViewNumRecetas = findViewById(R.id.textViewNumRecetas);
        textViewRecetasPublicadas = findViewById(R.id.textViewRecetas);
        buttonSeguir = findViewById(R.id.buttonSeguir);
        buttonAniadirRecetas = findViewById(R.id.imgBRecetas);
        buttonCesta = findViewById(R.id.imgBCesta);
        buttonCocina = findViewById(R.id.imgBCocina);
        buttonForo = findViewById(R.id.imgBForo);
        linearLayoutBotoneraInferior = findViewById(R.id.linearLayoutBotoneraInferior);
        // Listener de los botones inferiores
        buttonAjustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarAjustes(nombreFormateado);
            }
        });
        buttonAniadirRecetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarAniadirRecetas(nombreFormateado);
            }
        });
        buttonCesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCesta(nombreFormateado);
            }
        });
        buttonCocina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarCocina(nombreFormateado);
            }
        });
        buttonForo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarForo(nombreFormateado);
            }
        });
        buttonSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uidUsuarioSesionActual = uidUsuarioSesion;
                String uidUsuarioSeguido = uidUsuarioActividad;
                firestoreDB.collection("usuarios")
                        .document(uidUsuarioSesionActual)
                        .update("seguidos",FieldValue.arrayUnion(new HashMap<String, Object>() {{
                            put("uid",uidUsuarioSeguido);
                            put("display_name",nombreFormateado);
                        }}))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                firestoreDB.collection("usuarios")
                                        .document(uidUsuarioSeguido)
                                        .update("seguidores", FieldValue.arrayUnion(uidUsuarioSesion))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                buttonSeguir.setText("Siguiendo");
                                                buttonSeguir.setEnabled(false);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("PerfilActivity","Error al seguir al usuario",e);
                                            }
                                        });
                            }
                        });
            }
        });
    }

    /**
     * Obtiene el uid del usuario a traves de su nombre de usuario
     */
    private Task<String> obtenerUidActividad(String nombreUsuario) {
        return firestoreDB.collection("usuarios")
                .whereEqualTo("display_name", nombreUsuario)
                .get()
                .continueWith(new Continuation<QuerySnapshot, String>() {
                    @Override
                    public String then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                return document.getId();
                            }
                        }
                        return null;
                    }
                });
    }

    /**
     * Obtiene y muestra el numero de seguidores
     */
    private void obtenerYMostrarSeguidores(String uidUsuario) {
        firestoreDB.collection("usuarios")
                .document(uidUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                List<String> seguidores = (List<String>) document.get("seguidores");
                                textViewNumSeguidores.setText(String.valueOf(seguidores.size()));
                            } else {
                                textViewNumSeguidores.setText(String.valueOf(000));
                            }
                        }
                    }
                });
    }

    /**
     * Obtiene y muestra el numero de seguidos
     */
    private void obtenerYMostrarSeguidos(String uidUsuario) {
        firestoreDB.collection("usuarios")
                .document(uidUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                List<String> seguidos = (List<String>) document.get("seguidos");
                                textViewNumSeguidos.setText(String.valueOf(seguidos.size()));
                            } else {
                                textViewNumSeguidos.setText(String.valueOf(000));
                            }
                        }
                    }
                });
    }

    /**
     * Obtiene y muestra el numero de recetas publicadas
     */
    private void obtenerYMostrarNumRecetas(String uidUsuario) {
        firestoreDB.collection("usuarios")
                .document(uidUsuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            List<String> recetasPublicadas = (List<String>) document.get("recetasPublicadas");
                            textViewNumRecetas.setText(String.valueOf(recetasPublicadas.size()));
                        } else {
                            textViewNumRecetas.setText(String.valueOf(000));
                        }
                    }
                });
    }

    /**
     * Inicia la actividad de Añadir Recetas
     */
    private void iniciarAjustes(String nombreFormateado) {
        Intent intent = new Intent(this, AjustesActivity.class);
        intent.putExtra("nombreFormateado", nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
    private void iniciarAniadirRecetas(String nombreFormateado){
        Intent intent = new Intent(this, AniadirRecetasActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    /**
     * Inicia la actividad de Cesta
     */
    private void iniciarCesta(String nombreFormateado){
        Intent intent = new Intent(this, CestaActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    /**
     * Inicia la actividad de Foro
     */
    private void iniciarCocina(String nombreFormateado){
        Intent intent = new Intent(this, CocinaActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    /**
     * Inicia la actividad de Perfil
     */
    private void iniciarForo(String nombreFormateado){
        Intent intent = new Intent(this, ForoActivity.class);
        intent.putExtra("nombreFormateado",nombreFormateado);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}
