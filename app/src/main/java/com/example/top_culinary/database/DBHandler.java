package com.example.top_culinary.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.top_culinary.model.Receta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
    // Inicializacion de las variables
    private Context context;
    /**
     * Constructor
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
        try{
            InputStream inputStream = context.getAssets().open("recetas.json");
            int tamanio = inputStream.available();
            byte[] buffer = new byte[tamanio];
            inputStream.read(buffer);
            inputStream.close();
            JSONArray jsonArray = new JSONArray(new String(buffer,"UTF-8"));
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String imagen = jsonObject.getString("imagen");
                String titulo = jsonObject.getString("titulo");
                String descripcion = jsonObject.getString("descripcion");
                String tiempoTotal = jsonObject.getString("tiempoTotal");
                String tipoPlato = jsonObject.getString("tipoPlato");
                String kalorias = jsonObject.getString("numCalorias");
                JSONArray jsonIngredientes = jsonObject.getJSONArray("ingredientes");
                List<String> ingredientes = new ArrayList<>();
                for (int j = 0; j < jsonIngredientes.length(); j++) {
                    ingredientes.add(jsonIngredientes.getString(j));
                }
                String url = jsonObject.getString("url");
                JSONArray jsonDetallesNutricion = jsonObject.getJSONArray("detallesNutricion");
                List<String> detallesNutricion = new ArrayList<>();
                for (int j = 0; j < jsonDetallesNutricion.length(); j++) {
                    detallesNutricion.add(jsonDetallesNutricion.getString(j));
                }
                String numPersonas = jsonObject.getString("numPersonas");
                String tiempoPreparacion = jsonObject.getString("tiempoPreparacion");
                JSONArray jsonEquipamiento = jsonObject.getJSONArray("equipamiento");
                List<String> equipamientos = new ArrayList<>();
                for (int j = 0; j < jsonEquipamiento.length(); j++) {
                    equipamientos.add(jsonEquipamiento.getString(j));
                }
                String tipoCocina = jsonObject.getString("tipoCocina");
                JSONArray jsonPasos = jsonObject.getJSONArray("pasos");
                List<String> pasos = new ArrayList<>();
                for (int j = 0; j < jsonPasos.length(); j++) {
                    pasos.add(jsonPasos.getString(j));
                }
                String notas = jsonObject.getString("notas");
                ContentValues contentValues = new ContentValues();
                contentValues.put(IMAGEN_COL,imagen);
                contentValues.put(TITULO_COL,titulo);
                contentValues.put(DESCRIPCION_COL,descripcion);
                contentValues.put(TIEMPO_TOTAL_COL,tiempoTotal);
                contentValues.put(TIPO_PLATO_COL,tipoPlato);
                contentValues.put(KALORIAS_COL,kalorias);
                StringBuilder stbIngredientes = new StringBuilder();
                for(String ingrediente : ingredientes) {
                    stbIngredientes.append(ingrediente + "\n");
                }
                contentValues.put(INGREDIENTES_COL,stbIngredientes.toString());
                contentValues.put(URL_COL,url);
                StringBuilder stbDetallesNutricion = new StringBuilder();
                for(String detalleNutricion : detallesNutricion) {
                    stbDetallesNutricion.append(detalleNutricion + "\n");
                }
                contentValues.put(DETALLES_NUTRICION_COL, stbDetallesNutricion.toString());
                contentValues.put(NUM_PERSONAS_COL,numPersonas);
                contentValues.put(TIEMPO_PREPARACION_COL,tiempoPreparacion);
                StringBuilder stbEquipamiento = new StringBuilder();
                for(String equipamiento : equipamientos) {
                    stbEquipamiento.append(equipamiento + "\n");
                }
                contentValues.put(EQUIPAMIENTO_COL,stbEquipamiento.toString());
                contentValues.put(TIPO_COCINA_COL,tipoCocina);
                StringBuilder stbPasos = new StringBuilder();
                for(String paso : pasos) {
                    stbPasos.append(paso + "\n");
                }
                contentValues.put(PASOS_COL, stbPasos.toString());
                contentValues.put(NOTAS_COL, notas);
                // Implementando la nueva base de datos
                db.insert(NOMBRE_TABLA,null,contentValues);
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Crea la tabla para las recetas creadas por el usuario
    private void creacionTablaRecetasUsuario(SQLiteDatabase db) {
        // Query para la creacion de la tabla de las recetas creadas por el usuario
        String queryRecetasUsuario = "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA_USUARIO + "("
                + ID_USUARIO_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + IMAGEN_USUARIO_COL + " TEXT, "
                + TITULO_USUARIO_COL + " TEXT, "
                + INGREDIENTES_USUARIO_COL + " TEXT, "
                + PASOS_USUARIO_COL + " TEXT)";
        // Ejecutamos la query para la creacion de la tabla
        db.execSQL(queryRecetasUsuario);
    }

    // Crea la tabla de los ingredientes disponibles en la app
    private void creacionTablaIngredientes(SQLiteDatabase db) {
        // Query para la creacion de la tabla de los ingredientes
        String queryIngredientes = "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA_INGREDIENTES + "("
                + ID_INGREDIENTES_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOMBRE_INGREDIENTE_COL + " TEXT, "
                + IMAGEN_INGREDIENTE_COL + " TEXT, "
                + PRECIO_INGREDIENTE_COL + " TEXT)";
        // Ejecutamos la query para la creacion de la tabla
        db.execSQL(queryIngredientes);
        // Insertamos los datos de los ingredientes obtenidos del archivo JSON
        insertarIngredientesGeneral(db);
    }

    // Inserta los diferentes ingredientes dentro de la tabla especificada
    private void insertarIngredientesGeneral(SQLiteDatabase db) {
        try {
            InputStream inputStream = context.getAssets().open("ingredientes.json");
            int tamanio = inputStream.available();
            byte[] buffer = new byte[tamanio];
            inputStream.read(buffer);
            inputStream.close();
            JSONArray jsonArray = new JSONArray(new String(buffer,"UTF-8"));
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                String imagen = jsonObject.getString("imagen");
                String precio = jsonObject.getString("precio");
                ContentValues contentValues = new ContentValues();
                contentValues.put(NOMBRE_INGREDIENTE_COL,nombre);
                contentValues.put(IMAGEN_INGREDIENTE_COL,imagen);
                contentValues.put(PRECIO_INGREDIENTE_COL,precio);
                db.insert(NOMBRE_TABLA_INGREDIENTES,null,contentValues);
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtenemos todas las recetas de la base de datos
     * @return Lista de recetas
     */
    @SuppressLint("Range")
    public List<Receta> obtenerRecetas(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<Receta> recetas = new ArrayList<Receta>();
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + NOMBRE_TABLA, null);
            if(cursor.moveToFirst()){
                do {
                    Receta receta = new Receta();
                    receta.setImagen(cursor.getString(cursor.getColumnIndex(IMAGEN_COL)));
                    receta.setTitulo(cursor.getString(cursor.getColumnIndex(TITULO_COL)));
                    receta.setTiempoTotal(cursor.getString(cursor.getColumnIndex(TIEMPO_TOTAL_COL)));
                    receta.setTipoPlato(cursor.getString(cursor.getColumnIndex(TIPO_PLATO_COL)));
                    recetas.add(receta);
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.d("Insercion BD","Error al obtener las recetas.");
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return recetas;
    }

    /**
     * Obtenemos todas las recetas de los usuarios de la base de datos
     * @return Lista de Recetas del Usuario
     */
    @SuppressLint("Range")
    public List<Receta> obtenerRecetasUsuario() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<Receta> recetasUsuario = new ArrayList<Receta>();
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + NOMBRE_TABLA_USUARIO, null);
            if(cursor.moveToFirst()) {
                do {
                    Receta receta = new Receta();
                    receta.setImagen(cursor.getString(cursor.getColumnIndex(IMAGEN_USUARIO_COL)));
                    receta.setTitulo(cursor.getString(cursor.getColumnIndex(TITULO_USUARIO_COL)));
                    recetasUsuario.add(receta);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("Insercion BD", "Error al obtener las recetas del usuario");
        } finally {
            if(cursor != null) {
                cursor.close();
            }
         }
        return recetasUsuario;
    }

    /**
     * Obtenemos los detalles de la receta pulsada
     * @param nomReceta Nombre de la Receta seleccionada
     * @return Detalles de la receta seleccionada
     */
    @SuppressLint("Range")
    public Receta obtenerRecetaPorNombre(String nomReceta) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Receta receta = new Receta();
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + NOMBRE_TABLA + " WHERE " + TITULO_COL + " LIKE ?", new String[]{"%" + nomReceta + "%"});
            if(cursor.moveToFirst()){
                receta.setImagen(cursor.getString(cursor.getColumnIndex(IMAGEN_COL)));
                receta.setTitulo(cursor.getString(cursor.getColumnIndex(TITULO_COL)));
                receta.setDescripcion(cursor.getString(cursor.getColumnIndex(DESCRIPCION_COL)));
                receta.setTiempoTotal(cursor.getString(cursor.getColumnIndex(TIEMPO_TOTAL_COL)));
                receta.setTipoPlato(cursor.getString(cursor.getColumnIndex(TIPO_PLATO_COL)));
                receta.setKalorias(cursor.getString(cursor.getColumnIndex(KALORIAS_COL)));
                String ingredientes = cursor.getString(cursor.getColumnIndex(INGREDIENTES_COL));
                List<String> listaIngredientes = new ArrayList<>();
                listaIngredientes.addAll(Arrays.asList(ingredientes.split("\n")));
                receta.setIngredientes(listaIngredientes);
                receta.setUrl(cursor.getString(cursor.getColumnIndex(URL_COL)));
                String detallesNutricion = cursor.getString(cursor.getColumnIndex(DETALLES_NUTRICION_COL));
                List<String> listaDetallesNutricion = new ArrayList<>();
                listaDetallesNutricion.addAll(Arrays.asList(detallesNutricion.split("\n")));
                receta.setDetallesNutricion(listaDetallesNutricion);
                receta.setNumPersonas(cursor.getString(cursor.getColumnIndex(NUM_PERSONAS_COL)));
                receta.setTiempoPreparacion(cursor.getString(cursor.getColumnIndex(TIEMPO_PREPARACION_COL)));
                String equipamiento = cursor.getString(cursor.getColumnIndex(EQUIPAMIENTO_COL));
                List<String> listaEquipamiento = new ArrayList<>();
                listaEquipamiento.addAll(Arrays.asList(equipamiento.split("\n")));
                receta.setEquipamiento(listaEquipamiento);
                receta.setTipoCocina(cursor.getString(cursor.getColumnIndex(TIPO_COCINA_COL)));
                String pasos = cursor.getString(cursor.getColumnIndex(PASOS_COL));
                List<String> listaPasos = new ArrayList<>();
                listaPasos.addAll(Arrays.asList(pasos.split("\n")));
                receta.setPasos(listaPasos);
                receta.setNotas(cursor.getString(cursor.getColumnIndex(NOTAS_COL)));
            }
        } catch (SQLiteException e){
            Log.d("DB","Error al obtener los detalles de la receta");
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return receta;
    }

    /**
     * Obtenemos los detalles de la receta del usuario pulsada
     * @param nombreReceta de la receta
     * @return receta seleccionada
     */
    @SuppressLint("Range")
    public Receta obtenerRecetaUsuarioPorNombre(String nombreReceta) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Receta receta = new Receta();
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + NOMBRE_TABLA_USUARIO + " WHERE " + TITULO_USUARIO_COL + " LIKE ?", new String[]{"%" + nombreReceta + "%"});
            if(cursor.moveToFirst()) {
                receta.setImagen(cursor.getString(cursor.getColumnIndex(IMAGEN_USUARIO_COL)));
                receta.setTitulo(cursor.getString(cursor.getColumnIndex(TITULO_USUARIO_COL)));
                String ingredientes = cursor.getString(cursor.getColumnIndex(INGREDIENTES_USUARIO_COL));
                List<String> listaIngredientes = new ArrayList<>();
                listaIngredientes.addAll(Arrays.asList(ingredientes.split("\n")));
                receta.setIngredientes(listaIngredientes);
                String pasos = cursor.getString(cursor.getColumnIndex(PASOS_USUARIO_COL));
                List<String> listaPasos = new ArrayList<>();
                listaPasos.addAll(Arrays.asList(pasos.split("\n")));
                receta.setPasos(listaPasos);
            }
        } catch (SQLiteException e) {
            Log.d("DB","Error al obtener los detalles de la receta.");
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
        return receta;
    }

    /**
     * Inserta la receta creada por el usuario
     * @param imagen de la receta
     * @param titulo de la receta
     * @param ingredientes de la receta
     * @param pasos de la receta
     */
    public void insertarRecetaUsuario(String imagen, String titulo, String ingredientes, String pasos) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IMAGEN_USUARIO_COL, imagen);
        contentValues.put(TITULO_USUARIO_COL, titulo);
        contentValues.put(INGREDIENTES_USUARIO_COL, ingredientes);
        contentValues.put(PASOS_USUARIO_COL, pasos);
        long resultado = sqLiteDatabase.insert(NOMBRE_TABLA_USUARIO, null, contentValues);
        if(resultado == -1) {
            throw new SQLiteException("Error al insertar la receta en la tabla RecetasUsuario");
        }
        sqLiteDatabase.close();
    }

    /**
     * Elimina una receta insertada por el usuario
     * @param titulo de la receta
     */
    private void eliminarRecetaUsuario(String titulo) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String queryEliminacion = "DELETE FROM " + NOMBRE_TABLA_USUARIO + " WHERE " + TITULO_USUARIO_COL + " =?";
        sqLiteDatabase.execSQL(queryEliminacion, new String[]{titulo});
        sqLiteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String queryRecetas = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
        db.execSQL(queryRecetas);
        String queryRecetasUsuario = "DROP TABLE IF EXISTS " + NOMBRE_TABLA_USUARIO;
        db.execSQL(queryRecetasUsuario);
        onCreate(db);
    }
}
