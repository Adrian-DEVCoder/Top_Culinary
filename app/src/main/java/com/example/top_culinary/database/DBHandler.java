package com.example.top_culinary.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.top_culinary.model.Ingrediente;
import com.example.top_culinary.model.Receta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    // Constantes DB
    private static final String NOMBRE_DB = "Recetas.sqlite";
    private static final int DB_VERSION = 1;

    // Constantes Tabla Recetas Aplicacion
    private static final String NOMBRE_TABLA = "Recetas";
    private static final String ID_COL = "id";
    private static final String IMAGEN_COL = "imagen";
    private static final String TITULO_COL = "titulo";
    private static final String DESCRIPCION_COL = "descripcion";
    private static final String TIEMPO_TOTAL_COL = "tiempototal";
    private static final String TIPO_PLATO_COL = "tipoplato";
    private static final String INGREDIENTES_COL = "ingredientes";
    private static final String URL_COL = "url";
    private static final String DETALLES_NUTRICION_COL = "detallesnutricion";
    private static final String NUM_PERSONAS_COL = "numpersonas";
    private static final String TIEMPO_PREPARACION_COL = "tiempopreparacion";
    private static final String EQUIPAMIENTO_COL = "equipamiento";
    private static final String TIPO_COCINA_COL = "tipococina";
    private static final String PASOS_COL = "pasos";
    private static final String KALORIAS_COL = "kalorias";
    private static final String NOTAS_COL = "notas";

    // Constantes Tabla Recetas Usuario
    private static final String NOMBRE_TABLA_USUARIO = "RecetasUsuario";
    private static final String ID_USUARIO_COL = "id";
    private static final String IMAGEN_USUARIO_COL = "imagen";
    private static final String TITULO_USUARIO_COL = "titulo";
    private static final String INGREDIENTES_USUARIO_COL = "ingredientes";
    private static final String PASOS_USUARIO_COL = "pasos";

    // Constantes Tabla Ingredientes
    private static final String NOMBRE_TABLA_INGREDIENTES = "Ingredientes";
    private static final String ID_INGREDIENTES_COL = "id";
    private static final String NOMBRE_INGREDIENTE_COL = "nombre";
    private static final String IMAGEN_INGREDIENTE_COL = "imagen";
    private static final String PRECIO_INGREDIENTE_COL = "precio";

    private Context context;

    /**
     * Constructor
     *
     * @param context Contexto de la actividad
     */
    public DBHandler(Context context) {
        super(context, NOMBRE_DB, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        creacionTablaRecetas(db);
        creacionTablaRecetasUsuario(db);
        creacionTablaIngredientes(db);
    }

    // Crea la tabla para las recetas generadas por la app
    private void creacionTablaRecetas(SQLiteDatabase db) {
        // Query para la creacion de la tabla de las Recetas de la app
        String queryRecetas = "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + "("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IMAGEN_COL + " TEXT, "
                + TITULO_COL + " TEXT, "
                + DESCRIPCION_COL + " TEXT, "
                + TIEMPO_TOTAL_COL + " TEXT, "
                + TIPO_PLATO_COL + " TEXT, "
                + KALORIAS_COL + " TEXT, "
                + INGREDIENTES_COL + " TEXT, "
                + URL_COL + " TEXT, "
                + DETALLES_NUTRICION_COL + " TEXT, "
                + NUM_PERSONAS_COL + " TEXT, "
                + TIEMPO_PREPARACION_COL + " TEXT, "
                + EQUIPAMIENTO_COL + " TEXT, "
                + TIPO_COCINA_COL + " TEXT, "
                + PASOS_COL + " TEXT, "
                + NOTAS_COL + " TEXT)";
        // Ejecutamos la query de la creacion de la tabla
        db.execSQL(queryRecetas);
        // Insertamos los datos obtenidos del JSON de las recetas insertadas en la APP
        insertarRecetasGeneral(db);
    }

    // Inserta las recetas mediante los datos obtenidos a traves del archivo JSON
    private void insertarRecetasGeneral(SQLiteDatabase db) {
        try {
            String jsonString = leerJSONDesdeAsset("recetas.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(IMAGEN_COL, jsonObject.getString("imagen"));
                contentValues.put(TITULO_COL, jsonObject.getString("titulo"));
                contentValues.put(DESCRIPCION_COL, jsonObject.getString("descripcion"));
                contentValues.put(TIEMPO_TOTAL_COL, jsonObject.getString("tiempoTotal"));
                contentValues.put(TIPO_PLATO_COL, jsonObject.getString("tipoPlato"));
                contentValues.put(KALORIAS_COL, jsonObject.getString("numCalorias"));
                contentValues.put(INGREDIENTES_COL, convertirJSONArrayAString(jsonObject.getJSONArray("ingredientes")));
                contentValues.put(URL_COL, jsonObject.getString("url"));
                contentValues.put(DETALLES_NUTRICION_COL, convertirJSONArrayAString(jsonObject.getJSONArray("detallesNutricion")));
                contentValues.put(NUM_PERSONAS_COL, jsonObject.getString("numPersonas"));
                contentValues.put(TIEMPO_PREPARACION_COL, jsonObject.getString("tiempoPreparacion"));
                contentValues.put(EQUIPAMIENTO_COL, convertirJSONArrayAString(jsonObject.getJSONArray("equipamiento")));
                contentValues.put(TIPO_COCINA_COL, jsonObject.getString("tipoCocina"));
                contentValues.put(PASOS_COL, convertirJSONArrayAString(jsonObject.getJSONArray("pasos")));
                contentValues.put(NOTAS_COL, jsonObject.getString("notas"));

                db.insert(NOMBRE_TABLA, null, contentValues);
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void creacionTablaRecetasUsuario(SQLiteDatabase db) {
        String queryRecetasUsuario = "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA_USUARIO + "("
                + ID_USUARIO_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IMAGEN_USUARIO_COL + " TEXT, "
                + TITULO_USUARIO_COL + " TEXT, "
                + INGREDIENTES_USUARIO_COL + " TEXT, "
                + PASOS_USUARIO_COL + " TEXT)";
        db.execSQL(queryRecetasUsuario);
    }

    public void eliminarRecetaDeUsuario(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOMBRE_TABLA_USUARIO,TITULO_USUARIO_COL + "= ?", new String[]{nombre});
        db.close();
    }

    private void creacionTablaIngredientes(SQLiteDatabase db) {
        String queryIngredientes = "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA_INGREDIENTES + "("
                + ID_INGREDIENTES_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOMBRE_INGREDIENTE_COL + " TEXT, "
                + IMAGEN_INGREDIENTE_COL + " TEXT, "
                + PRECIO_INGREDIENTE_COL + " TEXT)";
        db.execSQL(queryIngredientes);
        insertarIngredientesGeneral(db);
    }

    private void insertarIngredientesGeneral(SQLiteDatabase db) {
        try {
            String jsonString = leerJSONDesdeAsset("ingredientes.json");
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(NOMBRE_INGREDIENTE_COL, jsonObject.getString("nombre"));
                contentValues.put(IMAGEN_INGREDIENTE_COL, jsonObject.getString("imagen"));
                contentValues.put(PRECIO_INGREDIENTE_COL, jsonObject.getString("precio"));
                db.insert(NOMBRE_TABLA_INGREDIENTES, null, contentValues);
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String leerJSONDesdeAsset(String nombreArchivo) throws IOException {
        InputStream inputStream = context.getAssets().open(nombreArchivo);
        int tamanio = inputStream.available();
        byte[] buffer = new byte[tamanio];
        inputStream.read(buffer);
        inputStream.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    private String convertirJSONArrayAString(JSONArray jsonArray) throws JSONException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            stringBuilder.append(jsonArray.getString(i)).append("\n");
        }
        return stringBuilder.toString();
    }

    @SuppressLint("Range")
    public List<Ingrediente> obtenerIngredientes() {
        List<Ingrediente> ingredientes = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + NOMBRE_TABLA_INGREDIENTES, null)) {

            if (cursor.moveToFirst()) {
                do {
                    Ingrediente ingrediente = new Ingrediente();
                    ingrediente.setImagen(cursor.getString(cursor.getColumnIndex(IMAGEN_INGREDIENTE_COL)));
                    ingrediente.setNombre(cursor.getString(cursor.getColumnIndex(NOMBRE_INGREDIENTE_COL)));
                    ingrediente.setPrecio(cursor.getString(cursor.getColumnIndex(PRECIO_INGREDIENTE_COL)));
                    ingredientes.add(ingrediente);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DBHandler", "Error al obtener los ingredientes", e);
        }
        return ingredientes;
    }

    @SuppressLint("Range")
    public Ingrediente obtenerIngredientePorNombre(String nombreIngrediente) {
        Ingrediente ingrediente = new Ingrediente();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + NOMBRE_TABLA_INGREDIENTES + " WHERE " + NOMBRE_INGREDIENTE_COL + " LIKE ?", new String[]{"%" + nombreIngrediente + "%"})) {

            if (cursor.moveToFirst()) {
                ingrediente.setImagen(cursor.getString(cursor.getColumnIndex(IMAGEN_INGREDIENTE_COL)));
                ingrediente.setNombre(cursor.getString(cursor.getColumnIndex(NOMBRE_INGREDIENTE_COL)));
                ingrediente.setPrecio(cursor.getString(cursor.getColumnIndex(PRECIO_INGREDIENTE_COL)));
            }
        } catch (Exception e) {
            Log.d("DBHandler", "Error al obtener el ingrediente", e);
        }
        return ingrediente;
    }

    @SuppressLint("Range")
    public List<Receta> obtenerRecetas() {
        List<Receta> recetas = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + NOMBRE_TABLA, null)) {

            if (cursor.moveToFirst()) {
                do {
                    Receta receta = new Receta();
                    receta.setImagen(cursor.getString(cursor.getColumnIndex(IMAGEN_COL)));
                    receta.setTitulo(cursor.getString(cursor.getColumnIndex(TITULO_COL)));
                    receta.setTiempoTotal(cursor.getString(cursor.getColumnIndex(TIEMPO_TOTAL_COL)));
                    receta.setTipoPlato(cursor.getString(cursor.getColumnIndex(TIPO_PLATO_COL)));
                    recetas.add(receta);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DBHandler", "Error al obtener las recetas", e);
        }
        return recetas;
    }

    @SuppressLint("Range")
    public List<Receta> obtenerRecetasUsuario() {
        List<Receta> recetasUsuario = new ArrayList<>();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + NOMBRE_TABLA_USUARIO, null)) {

            if (cursor.moveToFirst()) {
                do {
                    Receta receta = new Receta();
                    receta.setImagen(cursor.getString(cursor.getColumnIndex(IMAGEN_USUARIO_COL)));
                    receta.setTitulo(cursor.getString(cursor.getColumnIndex(TITULO_USUARIO_COL)));
                    recetasUsuario.add(receta);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DBHandler", "Error al obtener las recetas del usuario", e);
        }
        return recetasUsuario;
    }

    @SuppressLint("Range")
    public Receta obtenerRecetaPorNombre(String nomReceta) {
        Receta receta = new Receta();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + TITULO_COL + " LIKE ?", new String[]{"%" + nomReceta + "%"})) {

            if (cursor.moveToFirst()) {
                receta.setImagen(cursor.getString(cursor.getColumnIndex(IMAGEN_COL)));
                receta.setTitulo(cursor.getString(cursor.getColumnIndex(TITULO_COL)));
                receta.setDescripcion(cursor.getString(cursor.getColumnIndex(DESCRIPCION_COL)));
                receta.setTiempoTotal(cursor.getString(cursor.getColumnIndex(TIEMPO_TOTAL_COL)));
                receta.setTipoPlato(cursor.getString(cursor.getColumnIndex(TIPO_PLATO_COL)));
                receta.setKalorias(cursor.getString(cursor.getColumnIndex(KALORIAS_COL)));
                receta.setIngredientes(Arrays.asList(cursor.getString(cursor.getColumnIndex(INGREDIENTES_COL)).split("\n")));
                receta.setUrl(cursor.getString(cursor.getColumnIndex(URL_COL)));
                receta.setDetallesNutricion(Arrays.asList(cursor.getString(cursor.getColumnIndex(DETALLES_NUTRICION_COL)).split("\n")));
                receta.setNumPersonas(cursor.getString(cursor.getColumnIndex(NUM_PERSONAS_COL)));
                receta.setTiempoPreparacion(cursor.getString(cursor.getColumnIndex(TIEMPO_PREPARACION_COL)));
                receta.setEquipamiento(Arrays.asList(cursor.getString(cursor.getColumnIndex(EQUIPAMIENTO_COL)).split("\n")));
                receta.setTipoCocina(cursor.getString(cursor.getColumnIndex(TIPO_COCINA_COL)));
                receta.setPasos(Arrays.asList(cursor.getString(cursor.getColumnIndex(PASOS_COL)).split("\n")));
                receta.setNotas(cursor.getString(cursor.getColumnIndex(NOTAS_COL)));
            }
        } catch (SQLiteException e) {
            Log.d("DBHandler", "Error al obtener los detalles de la receta", e);
        }
        return receta;
    }

    @SuppressLint("Range")
    public Receta obtenerRecetaUsuarioPorNombre(String nombreReceta) {
        Receta receta = new Receta();
        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM " + NOMBRE_TABLA_USUARIO + " WHERE " + TITULO_USUARIO_COL + " LIKE ?", new String[]{"%" + nombreReceta + "%"})) {

            if (cursor.moveToFirst()) {
                receta.setImagen(cursor.getString(cursor.getColumnIndex(IMAGEN_USUARIO_COL)));
                receta.setTitulo(cursor.getString(cursor.getColumnIndex(TITULO_USUARIO_COL)));
                receta.setIngredientes(Arrays.asList(cursor.getString(cursor.getColumnIndex(INGREDIENTES_USUARIO_COL)).split("\n")));
                receta.setPasos(Arrays.asList(cursor.getString(cursor.getColumnIndex(PASOS_USUARIO_COL)).split("\n")));
            }
        } catch (SQLiteException e) {
            Log.d("DBHandler", "Error al obtener los detalles de la receta", e);
        }
        return receta;
    }

    public void insertarRecetaUsuario(String imagen, String titulo, String ingredientes, String pasos) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(IMAGEN_USUARIO_COL, imagen);
            contentValues.put(TITULO_USUARIO_COL, titulo);
            contentValues.put(INGREDIENTES_USUARIO_COL, ingredientes);
            contentValues.put(PASOS_USUARIO_COL, pasos);
            long resultado = db.insert(NOMBRE_TABLA_USUARIO, null, contentValues);
            if (resultado == -1) {
                throw new SQLiteException("Error al insertar la receta en la tabla RecetasUsuario");
            }
        }
    }

    private void eliminarRecetaUsuario(String titulo) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            String queryEliminacion = "DELETE FROM " + NOMBRE_TABLA_USUARIO + " WHERE " + TITULO_USUARIO_COL + " =?";
            db.execSQL(queryEliminacion, new String[]{titulo});
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String queryRecetas = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
        db.execSQL(queryRecetas);
        String queryRecetasUsuario = "DROP TABLE IF EXISTS " + NOMBRE_TABLA_USUARIO;
        db.execSQL(queryRecetasUsuario);
        String queryIngredientes = "DROP TABLE IF EXISTS " + NOMBRE_TABLA_INGREDIENTES;
        db.execSQL(queryIngredientes);
        onCreate(db);
    }
}
