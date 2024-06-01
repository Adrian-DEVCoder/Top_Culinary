package com.example.top_culinary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Usuario implements Parcelable{
    private String uid;
    private String urlImagenUsuario;
    private String display_name;
    private String email;
    private String tipo_inicio_de_sesion;
    private String tema_favorito;
    private List<String> seguidores;
    private List<String> seguidos;
    private List<String> recetas;
    private List<String> recetasPublicadas;
    private List<String> chats;

    public Usuario() {
    }

    public Usuario(String uid, String urlImagenUsuario, String display_name, String email, String tipo_inicio_de_sesion, String tema_favorito, List<String> seguidores, List<String> seguidos, List<String> recetas, List<String> recetasPublicadas, List<String> chats) {
        this.uid = uid;
        this.urlImagenUsuario = urlImagenUsuario;
        this.display_name = display_name;
        this.email = email;
        this.tipo_inicio_de_sesion = tipo_inicio_de_sesion;
        this.tema_favorito = tema_favorito;
        this.seguidores = seguidores;
        this.seguidos = seguidos;
        this.recetas = recetas;
        this.recetasPublicadas = recetasPublicadas;
        this.chats = chats;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrlImagenUsuario() {
        return urlImagenUsuario;
    }

    public void setUrlImagenUsuario(String urlImagenUsuario) {
        this.urlImagenUsuario = urlImagenUsuario;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo_inicio_de_sesion() {
        return tipo_inicio_de_sesion;
    }

    public void setTipo_inicio_de_sesion(String tipo_inicio_de_sesion) {
        this.tipo_inicio_de_sesion = tipo_inicio_de_sesion;
    }

    public String getTema_favorito() {
        return tema_favorito;
    }

    public void setTema_favorito(String tema_favorito) {
        this.tema_favorito = tema_favorito;
    }

    public List<String> getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(List<String> seguidores) {
        this.seguidores = seguidores;
    }

    public List<String> getSeguidos() {
        return seguidos;
    }

    public void setSeguidos(List<String> seguidos) {
        this.seguidos = seguidos;
    }

    public List<String> getRecetas() {
        return recetas;
    }

    public void setRecetas(List<String> recetas) {
        this.recetas = recetas;
    }

    public List<String> getRecetasPublicadas() {
        return recetasPublicadas;
    }

    public void setRecetasPublicadas(List<String> recetasPublicadas) {
        this.recetasPublicadas = recetasPublicadas;
    }

    public List<String> getChats() {
        return chats;
    }

    public void setChats(List<String> chats) {
        this.chats = chats;
    }

    // Implementación de Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(urlImagenUsuario);
        dest.writeString(display_name);
        dest.writeString(email);
        dest.writeString(tipo_inicio_de_sesion);
        dest.writeString(tema_favorito);
        dest.writeList(seguidores);
        dest.writeList(seguidos);
        dest.writeList(recetas);
        dest.writeList(recetasPublicadas);
        dest.writeList(chats);
    }

    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    // Constructor privado para crear instancias a partir de un Parcel
    private Usuario(Parcel parcel) {
        uid = parcel.readString();
        urlImagenUsuario = parcel.readString();
        display_name = parcel.readString();
        email = parcel.readString();
        tipo_inicio_de_sesion = parcel.readString();
        tema_favorito = parcel.readString();
        seguidores = parcel.readArrayList(String.class.getClassLoader());
        seguidos = parcel.readArrayList(String.class.getClassLoader());
        recetas = parcel.readArrayList(String.class.getClassLoader());
        recetasPublicadas = parcel.readArrayList(String.class.getClassLoader());
        chats = parcel.readArrayList(String.class.getClassLoader());
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "uid='" + uid + '\'' +
                ", urlImagenUsuario='" + urlImagenUsuario + '\'' +
                ", display_name='" + display_name + '\'' +
                ", email='" + email + '\'' +
                ", tipo_inicio_de_sesion='" + tipo_inicio_de_sesion + '\'' +
                ", tema_favorito='" + tema_favorito + '\'' +
                ", seguidores=" + seguidores +
                ", seguidos=" + seguidos +
                ", recetas=" + recetas +
                ", recetasPublicadas=" + recetasPublicadas +
                ", chats=" + chats +
                '}';
    }
}
