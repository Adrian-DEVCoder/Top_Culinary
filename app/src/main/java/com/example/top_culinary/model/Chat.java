package com.example.top_culinary.model;

public class Chat {
    private String nombreUsuario;
    private String ultimoMensaje;
    private String avatarUrl;

    public Chat() {
    }

    public Chat(String nombreUsuario, String ultimoMensaje, String avatarUrl) {
        this.nombreUsuario = nombreUsuario;
        this.ultimoMensaje = ultimoMensaje;
        this.avatarUrl = avatarUrl;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getUltimoMensaje() {
        return ultimoMensaje;
    }

    public void setUltimoMensaje(String ultimoMensaje) {
        this.ultimoMensaje = ultimoMensaje;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "nombreUsuario='" + nombreUsuario + '\'' +
                ", ultimoMensaje='" + ultimoMensaje + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
