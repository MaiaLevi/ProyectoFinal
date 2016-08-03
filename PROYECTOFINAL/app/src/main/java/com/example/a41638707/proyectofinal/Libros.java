package com.example.a41638707.proyectofinal;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Date;

public class Libros implements Serializable{

    private int IdLibro;
    private String Nombre;
    private String Descripcion;
    private String Imagen;
    private int IdUsuario;
    private String Usuario;
    private int Año;
    private MateriaEvento Materia;
    private boolean Vendido;
    @Override
    public String toString() {
        return getNombre() + " "+getDesc();
    }
    public Libros(int idlibro, String nombre, String descripcion, String imagen, int idUsuario,String usuario, int año, MateriaEvento materia, boolean vendido)
    {
        IdLibro=idlibro;
        Nombre=nombre;
        Descripcion=descripcion;
        Imagen=imagen;
        IdUsuario=idUsuario;
        Usuario=usuario;
        Año=año;
        Materia=materia;
        Vendido=vendido;
    }
    public int getId()
    {
        return IdLibro;
    }
    public String getNombre()
    {
        return Nombre;
    }
    public String getDesc()
    {
        return Descripcion;
    }
    public String getImg()
    {
        return Imagen;
    }
    public int getIdU()
    {
        return IdUsuario;
    }
    public Integer getAño()
    {
        return Año;
    }
    public MateriaEvento getMateria()
    {
        return Materia;
    }
    public boolean getVendido()
    {
        return Vendido;
    }
    public String getUsuario(){
        return Usuario;
    }

}
