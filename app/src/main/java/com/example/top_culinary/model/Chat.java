package com.example.top_culinary.model;

import java.util.List;

public class Chat {
    private String id; // Nuevo campo ID
    private String nombreUsuario;
    private String ultimoMensaje;
    private String avatarUrl;
    private List<String> participantIds;
    private long timestamp;

    public Chat() {
    }

    public Chat(String id, String nombreUsuario, String ultimoMensaje, String avatarUrl, List<String> participantIds, long timestamp) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.ultimoMensaje = ultimoMensaje;
        this.avatarUrl = avatarUrl;
        this.participantIds = participantIds;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id='" + id + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", ultimoMensaje='" + ultimoMensaje + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", participantIds=" + participantIds +
                ", timestamp=" + timestamp +
                '}';
    }
}
