package com.example.top_culinary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Ingrediente implements Parcelable {
    private String imagen;
    private String nombre;
    private String precio;
    private int cantidad; // Propiedad para almacenar la cantidad

    public Ingrediente() {
    }

    public Ingrediente(String imagen, String nombre, String precio, int cantidad) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
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

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "imagen='" + imagen + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio='" + precio + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imagen);
        dest.writeString(nombre);
        dest.writeString(precio);
        dest.writeInt(cantidad);
    }

    public static final Creator<Ingrediente> CREATOR = new Creator<Ingrediente>() {
        @Override
        public Ingrediente createFromParcel(Parcel in) {
            return new Ingrediente(in);
        }

        @Override
        public Ingrediente[] newArray(int size) {
            return new Ingrediente[size];
        }
    };

    private Ingrediente(Parcel in) {
        imagen = in.readString();
        nombre = in.readString();
        precio = in.readString();
        cantidad = in.readInt();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass()!= obj.getClass()) return false;
        Ingrediente that = (Ingrediente) obj;
        return Objects.equals(imagen, that.imagen) &&
                Objects.equals(nombre, that.nombre) &&
                Objects.equals(precio, that.precio) &&
                cantidad == that.cantidad;
    }

    @Override
    public int hashCode() {
        return Objects.hash(imagen, nombre, precio, cantidad);
    }

}
