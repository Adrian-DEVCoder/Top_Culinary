package com.example.top_culinary.cocina;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.top_culinary.R;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Receta;
import com.example.top_culinary.recetas.DetallesRecetaUsuarioActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecetacionActivity extends AppCompatActivity {
    // Declaracion de los widgets
    private TextView textViewPaso;
    private TextView textViewTiempoRestante;
    private CountDownTimer countDownTimer;
    private ProgressBar progressBarPasos;
    private ImageButton buttonSiguiente;
    private ImageButton buttonPlayPausa;

    // Declaracion de las variables
    private List<String> pasos;
    private DBHandler dbHandler;
    private long tiempoRestante = 0;
    private boolean enPausa = false;
    private boolean comienzo = false;
    private int progresoActual = 0;
    private double progresoSumar = 0;
    private int pasoActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetacion);
        // Inicializacion de la DB Local
        dbHandler = new DBHandler(this);
        // Obtencion del nombre de la receta y la receta dependiendo de si accede por una variable o por otra llama a metodos diferentes
        Receta receta = null;
        Intent intent = getIntent();
        String nombreReceta = intent.getStringExtra("nombreReceta");
        String nombreRecetaUsuario = intent.getStringExtra("nombreRecetaUsuario");
        if(nombreReceta != null) {
            receta = dbHandler.obtenerRecetaPorNombre(nombreReceta);
        } else if (nombreRecetaUsuario != null) {
            receta = dbHandler.obtenerRecetaUsuarioPorNombre(nombreRecetaUsuario);
        } else {
            mostrarToast("No se ha podido recuperar los detalles de la receta.");
        }
        // Inicializacion de los componentes
        textViewPaso = findViewById(R.id.textViewPaso);
        textViewTiempoRestante = findViewById(R.id.textViewTiempoRestante);
        buttonPlayPausa = findViewById(R.id.buttonPausaComenzar);
        buttonSiguiente = findViewById(R.id.buttonSiguiente);
        progressBarPasos = findViewById(R.id.progressBarPasos);
        pasos = obtenerPasos(receta);
        progresoSumar = 100 / pasos.size();

        actualizarPaso(pasoActual);

        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pasoActual < pasos.size() - 1) {
                    pasoActual++;
                    actualizarPaso(pasoActual);
                } else {
                    if(nombreReceta != null) {
                        Intent intent = new Intent(RecetacionActivity.this, DetallesRecetaActivity.class);
                        intent.putExtra("nombreReceta", nombreReceta);
                        startActivity(intent);
                        finish();
                    } else if (nombreRecetaUsuario != null) {
                        Intent intent = new Intent(RecetacionActivity.this, DetallesRecetaUsuarioActivity.class);
                        intent.putExtra("nombreReceta", nombreRecetaUsuario);
                        startActivity(intent);
                        finish();
                    } else {
                        mostrarToast("No se ha podido iniciar correctamente la actividad.");
                    }
                }
            }
        });
    }

    /**
     * Metodo para actualizar pasos de las recetas
     */
    private void actualizarPaso (int pasoActual) {
        // Restablecer progresoActual al inicio de cada paso
        progresoActual = 0;

        String paso = pasos.get(pasoActual);
        textViewPaso.setText(paso);
        // Verificamos si el paso contiene minutos u horas
        if(paso.contains("minutos") || paso.contains("hora")){
            textViewTiempoRestante.setVisibility(View.VISIBLE);
            buttonPlayPausa.setVisibility(View.VISIBLE);
            actualizarBarraDeProgreso();
        } else {
            // Si no contiene, inflamos el layout para pasos sin cocción
            textViewTiempoRestante.setVisibility(View.GONE);
            buttonPlayPausa.setVisibility(View.GONE);
            actualizarBarraDeProgreso();
        }
        // Ajustamos el cronometro si el paso contiene minutos o horas
        if(paso.contains("minutos") || paso.contains("hora")){
            String[] partesPaso = paso.split(" ");
            String retorno = "";
            for (String p : partesPaso){
                if(p.matches("\\d+")){
                    retorno = p;
                }
            }

            int tiempo = Integer.parseInt(retorno);
            if (tiempo > 0) {
                // Convertimos los minutos en milisegundos
                // Ajuste aquí: si el paso contiene "hora", multiplicamos por 60 para convertir horas en minutos
                tiempoRestante = (paso.contains("hora") ? tiempo * 60 : tiempo) * 60 * 1000;
                textViewTiempoRestante.setText(formatearTiempo(tiempoRestante));
                buttonPlayPausa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!comienzo) {
                            iniciarContador(tiempoRestante);
                            comienzo = true;
                            buttonPlayPausa.setImageResource(R.drawable.pausa);
                        } else if (enPausa) {
                            reanudarContador();
                            buttonPlayPausa.setImageResource(R.drawable.pausa);
                        } else {
                            pausarContador();
                            buttonPlayPausa.setImageResource(R.drawable.siguiente);
                        }
                    }
                });
            }
        }
        // Actualizamos el progreso basado en el paso actual
        progresoActual = (int) (progresoSumar * (pasoActual + 1));
        progressBarPasos.setProgress(progresoActual);
        animacionBarraDeProgreso(progresoActual, 1000);
    }

    /**
     * Metodo para iniciar el contador
     */
    private void iniciarContador(long tiempoEnMilis){
        countDownTimer = new CountDownTimer(tiempoEnMilis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante = millisUntilFinished;
                TextView textViewTiempoRestante = findViewById(R.id.textViewTiempoRestante);
                textViewTiempoRestante.setText(formatearTiempo(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                TextView textViewTiempoRestante = findViewById(R.id.textViewTiempoRestante);
                textViewTiempoRestante.setText("00:00");
            }
        }.start();
    }

    /**
     * Metodo para pausar el contador
     */
    private void pausarContador(){
        if(countDownTimer != null){
            countDownTimer.cancel();
            enPausa = true;
        }
    }

    /**
     * Metodo para reanudar el contador
     */
    private void reanudarContador(){
        if(enPausa) {
            iniciarContador(tiempoRestante);
            enPausa = false;
        }
    }

    /**
     * Metodo para formatear el tiempo del contador
     * @param millis a formatear
     * @return tiempo formateado
     */
    private String formatearTiempo(long millis) {
        String output = "00:00:00";
        long totalSegundos = millis / 1000;
        long horas = totalSegundos / 3600;
        long minutos = (totalSegundos%3600) / 60;
        long segundos = totalSegundos % 60;
        output = String.format("%02d:%02d:%02d",horas,minutos,segundos);
        return output;
    }

    /**
     * Metodo para obtener los pasos de la receta
     * @param receta de la cual obtenemos los pasos
     * @return pasos de la receta a realizar
     */
    private List<String> obtenerPasos(Receta receta) {
        List<String> pasosReceta = receta.getPasos();
        List<String> pasosFormateados = new ArrayList<>();
        for(String pasoReceta : pasosReceta){
            pasosFormateados.add(pasoReceta);
        }
        return pasosFormateados;
    }

    // Metodo para actualizar la barra de progreso
    private void actualizarBarraDeProgreso() {
        // Actualizamos el progreso actual y la barra de progreso
        progresoActual += progresoSumar;
        int valorInicial = progresoActual;
        progressBarPasos.setProgress(progresoActual);
        int valorFinal = progresoActual;
        int duracion = 1000;
        animacionBarraDeProgreso(valorFinal,duracion);
    }

    private void animacionBarraDeProgreso(int valorFinal, int duracion) {
        // Obtiene el valor actual de la barra de progreso
        int valorInicial = progressBarPasos.getProgress();
        // Crea la animación
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBarPasos, "progress", valorInicial, valorFinal);
        animation.setDuration(duracion);
        // Inicia la animación
        animation.start();
    }

    // Muestra un toast al usuario
    private void mostrarToast(String mensaje) {
        Toast.makeText(RecetacionActivity.this,mensaje,Toast.LENGTH_SHORT).show();
    }

}