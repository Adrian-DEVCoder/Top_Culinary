package com.example.top_culinary.recetas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.top_culinary.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Continuation;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class NombreImagenRecetaActivity extends AppCompatActivity {
    // Declaracion de constantes
    private static final int REQUEST_CODE_SELECCIONAR_FOTO = 1001;
    // Declaracion de las variables
    private String nombreFormateado;
    private String nombreReceta;
    private String imagenRecetaUrl;
    private StorageReference storageRef;
    // Declaracion de los widgets
    ImageView imageViewReceta;
    ImageButton buttonFoto;
    EditText editTextNombreReceta;
    ImageButton buttonSiguiente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nombre_receta);
        // Inicializacion del Storage de Firebase
        storageRef = FirebaseStorage.getInstance().getReference("imagenes/recetas/");
        // Obtencion del nombre de usuario a traves del intent;
        Intent intent = getIntent();
        nombreFormateado = intent.getStringExtra("nombreFormateado");
        // Inicializacion de los widgets
        imageViewReceta = findViewById(R.id.imageViewReceta);
        buttonFoto = findViewById(R.id.imageButtonFoto);
        editTextNombreReceta = findViewById(R.id.editTextNombreReceta);
        buttonSiguiente = findViewById(R.id.imageButtonSiguiente);
        // Solicitamos permisos al usuario
        solicitarPermiso();
        // Listener de los diferentes botones
        buttonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccionarFoto();
            }
        });
        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validacion de si el usuario a indicado un valor en el campo del nombre de la receta
                if(editTextNombreReceta.getText().toString().trim().isEmpty()) {
                    mostrarToast("Por favor, introduce el nombre de la receta.");
                    editTextNombreReceta.requestFocus();
                } else {
                    nombreReceta = editTextNombreReceta.getText().toString().trim();
                    Intent intent = new Intent(NombreImagenRecetaActivity.this,IngredientesActivity.class);
                    intent.putExtra("nombreReceta",nombreReceta);
                    intent.putExtra("nombreFormateado", nombreFormateado);
                    intent.putExtra("imagenReceta",imagenRecetaUrl);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void solicitarPermiso() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_SELECCIONAR_FOTO);
        } else {
            seleccionarFoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_SELECCIONAR_FOTO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                seleccionarFoto();
            } else {
                mostrarToast("No se ha podido acceder a la galería.");
            }
        }
    }

    private void seleccionarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_SELECCIONAR_FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SELECCIONAR_FOTO && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imageViewReceta.setImageURI(uri);
            uploadImageToFirebaseStorage(uri);
        }
    }

    // Subira la imagen seleccionada por el usuario al storage de firebase
    private void uploadImageToFirebaseStorage(Uri imageUri) {
        // Crea una referencia de almacenamiento para la imagen
        StorageReference imageRef = storageRef.child(System.currentTimeMillis() + ".jpg");

        // Inicia la subida de la imagen
        UploadTask uploadTask = imageRef.putFile(imageUri);

        // Encadena la operación de subida con la obtención de la URL de descarga
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imagenRecetaUrl = uri.toString();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}