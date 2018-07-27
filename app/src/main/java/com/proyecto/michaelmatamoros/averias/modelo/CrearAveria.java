package com.proyecto.michaelmatamoros.averias.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CrearAveria {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("tipo")
    @Expose
    private String tipo;
    @SerializedName("usuario")
    @Expose
    private Usuario usuario;
    @SerializedName("fecha")
    @Expose
    private String fecha;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;
    @SerializedName("imagen")
    @Expose
    private String imagen;
    @SerializedName("ubicacion")
    @Expose
    private Ubicacion ubicacion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public CrearAveria( String id,String nombre, String tipo, Usuario usuario, String fecha, String descripcion, String imagen, Ubicacion ubicacion) {

        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.usuario = usuario;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.ubicacion = ubicacion;
    }
}
