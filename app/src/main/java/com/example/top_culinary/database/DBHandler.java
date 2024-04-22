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
    // Constantes
    private static final String NOMBRE_DB = "Recetas.sqlite";
    private static final int DB_VERSION = 1;
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
        String query = "CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + "("
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
        db.execSQL(query);
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
            Log.d("Insercion BD","Error al insertar la receta.");
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return recetas;
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
        db.execSQL(query);
        onCreate(db);
    }
}
