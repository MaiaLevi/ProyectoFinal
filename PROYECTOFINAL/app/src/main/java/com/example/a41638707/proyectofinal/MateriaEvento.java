package com.example.a41638707.proyectofinal;

import java.io.Serializable;

/**
 * Created by 42038123 on 30/6/2016.
 */
public class MateriaEvento implements Serializable{
    private int IdMateria;
    private String Nombre;
    @Override
    public String toString() {
        return Nombre;
    }

    public MateriaEvento(int id, String nombre)
    {
        IdMateria=id;
        Nombre=nombre;
    }
    public int getId()
    {
        return IdMateria;
    }
    public String getNombre()
    {
        return Nombre;
    }
}
