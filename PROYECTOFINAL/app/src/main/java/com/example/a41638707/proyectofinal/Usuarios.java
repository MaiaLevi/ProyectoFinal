package com.example.a41638707.proyectofinal;

import android.app.Application;

import java.io.Serializable;

/**
 * Created by 42038123 on 3/8/2016.
 */
public class Usuarios implements Serializable  {
    private static int Id;
    private String Nombre;
    private String Apellido;
    private String Mail;
    private String Contra;
    private static Division Division;
    private int Telefono;
    public String toString() {
        return getNombre();
    }
    public Usuarios(String nombre, String mail, String contra, int telefono)
    {
        Nombre=nombre;
        Mail=mail;
        Contra=contra;
        Telefono=telefono;
    }
    public void setNombre(String nombre)
    {
        Nombre=nombre;
    }
    public void setContra(String contra)
    {
        Contra=contra;
    }
    public void setApellido(String apellido)
    {
        Apellido=apellido;
    }
    public void setMail(String mail){Mail=mail;}
    public static int getId()
    {
        return Id;
    }
    public String getNombre()
    {
        return  Nombre;
    }
    public String getApellido()
    {
        return  Apellido;
    }
    public String getMail()
    {
        return Mail;
    }
    public String getContra()
    {
        return Contra;
    }
    public static Division getDivision()
    {
        return Division;
    }
    public static void setId(int id) {
        Id = id;
    }
    public static void setDivision(Division division) {
        Division= division;
    }
    public void setTelefono(int telefono)
    {
        Telefono=telefono;
    }
    public int getTelefono ()
    {
        return Telefono;
    }
}