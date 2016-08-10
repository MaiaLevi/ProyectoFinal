package com.example.a41638707.proyectofinal;

import java.io.Serializable;

/**
 * Created by 42038123 on 10/8/2016.
 */
public class Division implements Serializable{
    private int Id;
    private String Nombre;
    public Division (int id, String nombre)
    {
        Id=id;
        Nombre=nombre;
    }
    public int getId()
    {
        return Id;
    }
    public String getNombre()
    {
        return Nombre;
    }
}
