package com.proyecto.michaelmatamoros.averias.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usuario {
    @SerializedName("correo")
    @Expose
    private String correo;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("tel")
    @Expose
    private String tel;
    @SerializedName("cedula")
    @Expose
    private String cedula;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Usuario(String correo, String nombre, String tel, String cedula) {
        this.correo = correo;
        this.nombre = nombre;
        this.tel = tel;
        this.cedula = cedula;
    }
}
