package com.example.top_culinary.model;

public class Receta {
    private String imagen;
    private String nombre;
    private String tipoPlato;
    private String tiempo;
    private String kalorias;

    public Receta() {
    }

    public Receta(String imagen, String nombre, String tipoPlato, String tiempo, String kalorias) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.tipoPlato = tipoPlato;
        this.tiempo = tiempo;
        this.kalorias = kalorias;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoPlato() {
        return tipoPlato;
    }

    public void setTipoPlato(String tipoPlato) {
        this.tipoPlato = tipoPlato;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getKalorias() {
        return kalorias;
    }

    public void setKalorias(String kalorias) {
        this.kalorias = kalorias;
    }

    @Override
    public String toString() {
        return "Receta{" +
                "imagen='" + imagen + '\'' +
                ", nombre='" + nombre + '\'' +
                ", dificultad='" + tipoPlato + '\'' +
                ", tiempo='" + tiempo + '\'' +
                ", kalorias='" + kalorias + '\'' +
                '}';
    }
}
