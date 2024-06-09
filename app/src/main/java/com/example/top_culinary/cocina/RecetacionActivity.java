package com.example.top_culinary.cocina;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.top_culinary.R;
import com.example.top_culinary.database.DBHandler;
import com.example.top_culinary.model.Dialogo;
import com.example.top_culinary.model.Receta;
import com.example.top_culinary.recetas.DetallesRecetaUsuarioActivity;
import java.util.ArrayList;
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
        // Inicializa la DB Local
        dbHandler = new DBHandler(this);
        // Inicializa los widgets
        initWidgets();
        // Obtiene los datos de la receta
        Receta receta = obtenerReceta();
        if (receta == null) {
            mostrarDialogo("Error","No se han podido recuperar los detalles de la receta.");
            return;
        }
        pasos = obtenerPasos(receta);
        progresoSumar = 100.0 / pasos.size();
        // Actualiza la UI con el nuevo paso a realizar
        actualizarPaso(pasoActual);
        // Configurar los diferentes listeners
        setupListeners(receta);
    }

    /**
     * Inicializa los widgets
     */
    private void initWidgets() {
        textViewPaso = findViewById(R.id.textViewPaso);
        textViewTiempoRestante = findViewById(R.id.textViewTiempoRestante);
        buttonPlayPausa = findViewById(R.id.buttonPausaComenzar);
        buttonSiguiente = findViewById(R.id.buttonSiguiente);
        progressBarPasos = findViewById(R.id.progressBarPasos);
    }

    /**
     * Obtiene la receta a comenzar
     * @return Receta
     */
    private Receta obtenerReceta() {
        Intent intent = getIntent();
        String nombreReceta = intent.getStringExtra("nombreReceta");
        String nombreRecetaUsuario = intent.getStringExtra("nombreRecetaUsuario");
        if (nombreReceta != null) {
            return dbHandler.obtenerRecetaPorNombre(nombreReceta);
        } else if (nombreRecetaUsuario != null) {
            return dbHandler.obtenerRecetaUsuarioPorNombre(nombreRecetaUsuario);
        } else {
            return null;
        }
    }

    /**
     * Obtiene los pasos de la receta
     * @param receta receta a realizar
     * @return Lista de Pasos a realizar
     */
    private List<String> obtenerPasos(Receta receta) {
        return new ArrayList<>(receta.getPasos());
    }

    /**
     * Configura los diferentes listeners
     * @param receta receta a realizar
     */
    private void setupListeners(Receta receta) {
        buttonSiguiente.setOnClickListener(v -> manejarSiguientePaso(receta));

        buttonPlayPausa.setOnClickListener(v -> {
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
        });
    }

    /**
     * Pasa a el siguiente paso de la receta
     * @param receta receta a realizar
     */
    private void manejarSiguientePaso(Receta receta) {
        if (pasoActual < pasos.size() - 1) {
            pasoActual++;
            actualizarPaso(pasoActual);
        } else {
            volverADetallesReceta(receta);
        }
    }

    /**
     * Volver a los detalles de la receta
     * @param receta receta a obtener detalles
     */
    private void volverADetallesReceta(Receta receta) {
        Intent intent;
        String nombreReceta = getIntent().getStringExtra("nombreReceta");
        String nombreRecetaUsuario = getIntent().getStringExtra("nombreRecetaUsuario");

        if (nombreReceta != null) {
            intent = new Intent(RecetacionActivity.this, DetallesRecetaActivity.class);
            intent.putExtra("nombreReceta", nombreReceta);
        } else {
            intent = new Intent(RecetacionActivity.this, DetallesRecetaUsuarioActivity.class);
            intent.putExtra("nombreReceta", nombreRecetaUsuario);
        }

        startActivity(intent);
        finish();
    }

    /**
     * Actualiza la UI con el nuevo paso a realizar
     * @param pasoActual paso a realizar
     */
    private void actualizarPaso(int pasoActual) {
        progresoActual = (int) (progresoSumar * (pasoActual + 1));
        progressBarPasos.setProgress(progresoActual);
        animacionBarraDeProgreso(progresoActual, 1000);

        String paso = pasos.get(pasoActual);
        textViewPaso.setText(paso);

        if (paso.contains("minutos") || paso.contains("hora")) {
            configurarCronometro(paso);
        } else {
            ocultarCronometro();
        }
    }

    /**
     * Configura el cronometro
     * @param paso paso a realizar
     */
    private void configurarCronometro(String paso) {
        textViewTiempoRestante.setVisibility(View.VISIBLE);
        buttonPlayPausa.setVisibility(View.VISIBLE);

        int tiempo = extraerTiempoDePaso(paso);
        if (tiempo > 0) {
            tiempoRestante = (paso.contains("hora") ? tiempo * 60 : tiempo) * 60 * 1000;
            textViewTiempoRestante.setText(formatearTiempo(tiempoRestante));
        }
    }

    /**
     * Oculta el cronometro
     */
    private void ocultarCronometro() {
        textViewTiempoRestante.setVisibility(View.GONE);
        buttonPlayPausa.setVisibility(View.GONE);
    }

    /**
     * Extrae el tiempo de un paso
     * @param paso paso a analizar
     * @return tiempo en milisegundos
     */
    private int extraerTiempoDePaso(String paso) {
        for (String parte : paso.split(" ")) {
            if (parte.matches("\\d+")) {
                return Integer.parseInt(parte);
            }
        }
        return 0;
    }

    /**
     * Inicia el cronometro
     * @param tiempoEnMilis tiempo en milisegundos
     */
    private void iniciarContador(long tiempoEnMilis) {
        countDownTimer = new CountDownTimer(tiempoEnMilis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tiempoRestante = millisUntilFinished;
                textViewTiempoRestante.setText(formatearTiempo(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                textViewTiempoRestante.setText("00:00:00");
            }
        }.start();
    }

    /**
     * Pausa el cronometro
     */
    private void pausarContador() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            enPausa = true;
        }
    }

    /**
     * Reanuda el cronometro
     */
    private void reanudarContador() {
        if (enPausa) {
            iniciarContador(tiempoRestante);
            enPausa = false;
        }
    }

    /**
     * Formatea el tiempo en un formato legible
     * @param millis tiempo en milisegundos
     * @return tiempo formateado
     */
    private String formatearTiempo(long millis) {
        long totalSegundos = millis / 1000;
        long horas = totalSegundos / 3600;
        long minutos = (totalSegundos % 3600) / 60;
        long segundos = totalSegundos % 60;
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }

    /**
     * Anima la barra de progreso
     * @param valorFinal valor final
     * @param duracion duracion en milisegundos
     */
    private void animacionBarraDeProgreso(int valorFinal, int duracion) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBarPasos, "progress", progressBarPasos.getProgress(), valorFinal);
        animation.setDuration(duracion);
        animation.start();
    }

    /**
     * Muestra un dialogo
     * @param titulo titulo
     * @param contenido contenido
     */
    private void mostrarDialogo(String titulo, String contenido) {
        Dialogo.showDialog(this,titulo,contenido);
    }
}
