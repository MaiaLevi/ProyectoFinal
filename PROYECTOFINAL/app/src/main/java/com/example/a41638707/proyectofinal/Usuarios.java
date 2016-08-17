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
    public Usuarios(String nombre, String mail, String contra)
    {
        Nombre=nombre;
        Mail=mail;
        Contra=contra;
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
    public static void setId(int id) {
        Id = id;
    }
    public static void setDivision(Division division) {
        Division= division;
    }
}
