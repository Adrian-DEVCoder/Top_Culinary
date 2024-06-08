package com.example.top_culinary.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.top_culinary.R;
import com.example.top_culinary.cocina.CocinaActivity;
import com.example.top_culinary.model.Dialogo;
import com.example.top_culinary.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class NombreUsuarioActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestoreDB;
    private ImageView imageViewPerfil;
    private ImageButton buttonSeleccionarImagen;
    private ImageButton buttonSiguiente;
    private Uri imageUri;
    private String urlImagenUsuario;
    private EditText editTextNombre;
    private String inicioDeSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre_usuario);
        inicioDeSesion = getIntent().getStringExtra("inicioDeSesion");
        firestoreDB = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("perfil_imagenes");
        firebaseAuth = FirebaseAuth.getInstance();

        imageViewPerfil = findViewById(R.id.imageViewPerfil);
        buttonSeleccionarImagen = findViewById(R.id.buttonSeleccionarImagen);
        buttonSiguiente = findViewById(R.id.imageButtonSiguiente);
        editTextNombre = findViewById(R.id.editTextNombreUsuario);

        buttonSeleccionarImagen.setOnClickListener(v -> mostrarFileChooser());

        buttonSiguiente.setOnClickListener(v -> {
            String nombreUsuario = editTextNombre.getText().toString().trim();
            if (nombreUsuario.isEmpty()) {
                Dialogo.showDialog(NombreUsuarioActivity.this, "Error", "Por favor, introduce un nombre de usuario");
                return;
            }
            if (imageUri != null) {
                subirImagenPerfil(nombreUsuario);
            } else {
                verificarNombreUsuario(nombreUsuario, null);
            }
        });
    }

    private void mostrarFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                Glide.with(this).load(bitmap).into(imageViewPerfil);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void subirImagenPerfil(String nombreUsuario) {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child(firebaseAuth.getCurrentUser().getUid() + ".jpg");
            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        urlImagenUsuario = uri.toString();
                        verificarNombreUsuario(nombreUsuario, urlImagenUsuario);
                    }).addOnFailureListener(e -> Log.e("NombreUsuarioActivity", "Error al obtener la URL de descarga", e)))
                    .addOnFailureListener(e -> {
                        Dialogo.showDialog(this, "Error", "Error al subir la imagen");
                        Log.e("NombreUsuarioActivity", "Error al subir la imagen", e);
                    });
        }
    }

    private void verificarNombreUsuario(String nombreUsuario, @Nullable String imageUrl) {
        firestoreDB.collection("usuarios")
                .whereEqualTo("display_name", nombreUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            Dialogo.showDialog(NombreUsuarioActivity.this, "Error", "El nombre de usuario ya existe, introduce otro diferente.");
                        } else {
                            agregarUsuarioFirestore(nombreUsuario, imageUrl);
                        }
                    } else {
                        Log.e("Firestore", "Error al verificar el nombre de usuario", task.getException());
                        Dialogo.showDialog(NombreUsuarioActivity.this, "Error", "Error al verificar el nombre de usuario");
                    }
                });
    }

    private void agregarUsuarioFirestore(String nombreUsuario, @Nullable String imageUrl) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String email = currentUser.getEmail();
            String tipoInicioDeSesion = (inicioDeSesion == null) ? "Google" : "Custom";
            Usuario usuario = new Usuario(uid, imageUrl != null ? imageUrl : "default_avatar_url", nombreUsuario, email, tipoInicioDeSesion, "Default", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

            firestoreDB.collection("usuarios").document(uid).set(usuario)
                    .addOnSuccessListener(aVoid -> {
                        Dialogo.showDialog(this, "Operación Satisfactoria", "Usuario registrado correctamente");
                        startActivity(new Intent(NombreUsuarioActivity.this, CocinaActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Dialogo.showDialog(this, "Error", "Error al guardar el usuario");
                        Log.e("NombreUsuarioActivity", "Error al guardar el usuario", e);
                    });
        }
    }
}
