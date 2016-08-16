package com.example.a41638707.proyectofinal;

import android.app.Application;

import java.io.Serializable;

/**
 * Created by 42038123 on 3/8/2016.
 */
public class Usuarios implements Serializable  {
    private static int Id;
    private String Nombre;
    private String Mail;
    private String Contra;
    private static Division Division;
    public String toString() {
        return getNombre();
    }
    public Usuarios(int id, String nombre, String mail, String contra, Division div)
    {
        Id=id;
        Nombre=nombre;
        Mail=mail;
        Contra=contra;
        Division=div;
    }
    public static int getId()
    {
        return Id;
    }
    public String getNombre()
    {
        return  Nombre;
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
    public void setIds(int id, Division division) {
        this.Id = id;
        this.Division=division;
    }
}
