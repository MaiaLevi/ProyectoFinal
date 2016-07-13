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
    private int Año;
    private int IdMateria;
    private String Vendido;

    public Libros(int idlibro, String nombre, String descripcion, String imagen, int idUsuario, int año, int idMateria, String vendido)
    {
        IdLibro=idlibro;
        Nombre=nombre;
        Descripcion=descripcion;
        Imagen=imagen;
        IdUsuario=idUsuario;
        Año=año;
        IdMateria=idMateria;
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
    public int getIdM()
    {
        return IdMateria;
    }
    public String getVendido()
    {
        return Vendido;
    }

}
