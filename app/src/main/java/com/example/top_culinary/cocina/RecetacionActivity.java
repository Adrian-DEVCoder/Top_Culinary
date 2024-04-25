package com.example.top_culinary.cocina;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.top_culinary.R;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Receta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecetacionActivity extends AppCompatActivity {
    // Declaracion de los widgets
    private ViewFlipper viewFlipperPasos;
    private List<String> pasos;
    private CountDownTimer countDownTimer;
    private DBHandler dbHandler;
    private long tiempoRestante = 0;
    private boolean enPausa = false;
    private boolean comienzo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetacion);
        dbHandler = new DBHandler(this);
        Intent intent = getIntent();
        String nombreReceta = intent.getStringExtra("nombreReceta");
        Receta receta = null;
        receta = dbHandler.obtenerRecetaPorNombre(nombreReceta);
        viewFlipperPasos = findViewById(R.id.viewFlipperPasos);
        pasos = obtenerPasos(receta);

        // Recorremos la lista de pasos a realizar
        // Dentro del bucle for que recorre los pasos
        for(String paso : pasos){
            LayoutInflater inflater = getLayoutInflater();
            View pasoView;
            // Verificamos si el paso contiene minutos u horas
            if(paso.contains("minutos") || paso.contains("hora")){
                // Si contiene, inflamos el layout para pasos con cocción
                pasoView = inflater.inflate(R.layout.paso_con_coccion_layout, null);
            } else {
                // Si no contiene, inflamos el layout para pasos sin cocción
                pasoView = inflater.inflate(R.layout.paso_sin_coccion_layout, null);
            }
            viewFlipperPasos.addView(pasoView);

            // Configuramos el textview del paso a realizar
            TextView textViewPaso = pasoView.findViewById(R.id.textViewPaso);
            textViewPaso.setText(paso);

            // Configuramos el boton de siguiente
            ImageButton buttonSiguiente = pasoView.findViewById(R.id.buttonSiguiente);
            buttonSiguiente.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewFlipperPasos.getDisplayedChild() == pasos.size() - 1) {
                        Intent intent = new Intent(RecetacionActivity.this, DetallesRecetaActivity.class);
                        intent.putExtra("nombreReceta",nombreReceta);
                        startActivity(intent);
                    } else {
                        viewFlipperPasos.showNext();
                        if(!enPausa){
                            pausarContador();
                        }
                    }
                }
            });

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
                if (tiempo > 0){
                    // Convertimos los minutos en milisegundos
                    tiempoRestante = tiempo * 60 * 1000;
                    // Buscamos el TextView dentro del View inflado
                    TextView textViewTiempoRestante = pasoView.findViewById(R.id.textViewTiempoRestante);
                    textViewTiempoRestante.setText(formatearTiempo(tiempoRestante));
                    ImageButton buttonPausaPlay = pasoView.findViewById(R.id.buttonPausaComenzar);
                    buttonPausaPlay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!comienzo){
                                iniciarContador(tiempoRestante);
                                comienzo = true;
                                buttonPausaPlay.setImageResource(R.drawable.pausa);
                            } else if (enPausa){
                                reanudarContador();
                                buttonPausaPlay.setImageResource(R.drawable.pausa);
                            } else {
                                pausarContador();
                                buttonPausaPlay.setImageResource(R.drawable.siguiente);
                            }
                        }
                    });
                }
            }
        }
    }

    /**
     * Metodo para iniciar el contador
     */
    private void iniciarContador(long tiempoEnMilis){
        countDownTimer = new CountDownTimer(tiempoEnMilis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante = millisUntilFinished;
                // Actualizamos la interfaz
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
     * Metodo para pausar el contador
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
        String output = "00:00";
        long totalSegundos = millis / 1000;
        long minutos = totalSegundos / 60;
        long segundos = totalSegundos % 60;
        output = String.format("%02d:%02d",minutos,segundos);
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
            String pasoFormateado = pasoReceta.substring(2);
            pasosFormateados.add(pasoFormateado);
        }
        return pasosFormateados;
    }

}