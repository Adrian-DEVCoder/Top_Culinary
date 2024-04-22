package com.example.top_culinary.model;

import java.util.List;

public class Receta {
    private String imagen;
    private String titulo;
    private String descripcion;
    private String tiempoTotal;
    private String tipoPlato;
    private String kalorias;
    private List<String> ingredientes;
    private String url;
    private List<String> detallesNutricion;
    private String numPersonas;
    private String tiempoPreparacion;
    private List<String> equipamiento;
    private String tipoCocina;
    private List<String> pasos;
    private String notas;

    public Receta() {
    }

    public Receta(String imagen, String titulo, String descripcion, String tiempoTotal, String tipoPlato, String kalorias,
                  List<String> ingredientes, String url, List<String> detallesNutricion, String numPersonas, String tiempoPreparacion,
                  List<String> equipamiento, String tipoCocina, List<String> pasos, String notas) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tiempoTotal = tiempoTotal;
        this.tipoPlato = tipoPlato;
        this.kalorias = kalorias;
        this.ingredientes = ingredientes;
        this.url = url;
        this.detallesNutricion = detallesNutricion;
        this.numPersonas = numPersonas;
        this.tiempoPreparacion = tiempoPreparacion;
        this.equipamiento = equipamiento;
        this.tipoCocina = tipoCocina;
        this.pasos = pasos;
        this.notas = notas;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(String tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public String getTipoPlato() {
        return tipoPlato;
    }

    public void setTipoPlato(String tipoPlato) {
        this.tipoPlato = tipoPlato;
    }

    public String getKalorias() {
        return kalorias;
    }

    public void setKalorias(String kalorias) {
        this.kalorias = kalorias;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getDetallesNutricion() {
        return detallesNutricion;
    }

    public void setDetallesNutricion(List<String> detallesNutricion) {
        this.detallesNutricion = detallesNutricion;
    }

    public String getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(String numPersonas) {
        this.numPersonas = numPersonas;
    }

    public String getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(String tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public List<String> getEquipamiento() {
        return equipamiento;
    }

    public void setEquipamiento(List<String> equipamiento) {
        this.equipamiento = equipamiento;
    }

    public String getTipoCocina() {
        return tipoCocina;
    }

    public void setTipoCocina(String tipoCocina) {
        this.tipoCocina = tipoCocina;
    }

    public List<String> getPasos() {
        return pasos;
    }

    public void setPasos(List<String> pasos) {
        this.pasos = pasos;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    @Override
    public String toString() {
        return "Receta{" +
                "imagen='" + imagen + '\'' +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", tiempoTotal='" + tiempoTotal + '\'' +
                ", tipoPlato='" + tipoPlato + '\'' +
                ", kalorias='" + kalorias + '\'' +
                ", ingredientes=" + ingredientes +
                ", url='" + url + '\'' +
                ", detallesNutricion='" + detallesNutricion + '\'' +
                ", numPersonas='" + numPersonas + '\'' +
                ", tiempoPreparacion='" + tiempoPreparacion + '\'' +
                ", equipamiento=" + equipamiento +
                ", tipoCocina='" + tipoCocina + '\'' +
                ", pasos=" + pasos +
                ", notas='" + notas + '\'' +
                '}';
    }
}
