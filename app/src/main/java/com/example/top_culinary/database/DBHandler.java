package com.example.top_culinary.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.top_culinary.model.Receta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    // Constantes
    private static final String NOMBRE_DB = "Recetas.sqlite";
    private static final int DB_VERSION = 1;
    private static final String NOMBRE_TABLA = "Recetas";
    private static final String ID_COL = "id";
    private static final String IMAGEN_COL = "imagen";
    private static final String NOMBRE_COL = "nombre";
    private static final String TIEMPO_COL = "tiempo";
    private static final String TIPOPLATO_COL = "tipoplato";
    private static final String KALORIAS_COL = "kalorias";

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
                + NOMBRE_COL + " TEXT, "
                + TIEMPO_COL + " TEXT, "
                + TIPOPLATO_COL + " TEXT, "
                + KALORIAS_COL + " TEXT)";
        // Ejecutamos la query de la creacion de la tabla
        db.execSQL(query);
        try{
            InputStream inputStream = context.getAssets().open("recetas.json");
            int tamanio = inputStream.available();
            byte[] buffer = new byte[tamanio];
            inputStream.read(buffer);
            inputStream.close();

            JSONArray jsonArray = new JSONArray(new String(buffer,"UTF-8"));

            // Bucle para insertar cada receta contenida dentro del JSON
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String imagen = jsonObject.getString("imagen");
                String nombre = jsonObject.getString("titulo");
                String tiempo = jsonObject.getString("tiempoTotal");
                String tipoPlato = jsonObject.getString("tipoPlato");
                String kalorias = jsonObject.getString("numCalorias");
                ContentValues contentValues = new ContentValues();
                contentValues.put(IMAGEN_COL,imagen);
                contentValues.put(NOMBRE_COL,nombre);
                contentValues.put(TIEMPO_COL,tiempo);
                contentValues.put(TIPOPLATO_COL,tipoPlato);
                contentValues.put(KALORIAS_COL,kalorias);
                db.insert(NOMBRE_TABLA,null,contentValues);
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Obtenemos las recetas de la base de datos
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
                    receta.setNombre(cursor.getString(cursor.getColumnIndex(NOMBRE_COL)));
                    receta.setTiempo(cursor.getString(cursor.getColumnIndex(TIEMPO_COL)));
                    receta.setTipoPlato(cursor.getString(cursor.getColumnIndex(TIPOPLATO_COL)));
                    receta.setKalorias(cursor.getString(cursor.getColumnIndex(KALORIAS_COL)));
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + NOMBRE_TABLA;
        db.execSQL(query);
        onCreate(db);
    }
}
