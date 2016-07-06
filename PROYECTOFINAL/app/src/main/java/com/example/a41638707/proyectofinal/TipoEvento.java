package com.example.a41638707.proyectofinal;

/**
 * Created by 41638707 on 22/6/2016.
 */
public class TipoEvento {
    private int IdTipo;
    private String Nombre;
    @Override
    public String toString() {
        return Nombre;
    }

    public TipoEvento(int id, String nombre)
    {
        IdTipo=id;
        Nombre=nombre;
    }
    public int getId()
    {
        return IdTipo;
    }
    public String getNombre()
    {
        return Nombre;
    }
}
