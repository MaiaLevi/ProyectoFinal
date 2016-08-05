package com.example.a41638707.proyectofinal;

import java.io.Serializable;

/**
 * Created by 42038123 on 3/8/2016.
 */
public class Usuarios implements Serializable {
    private String Nombre;
    private String Mail;
    private String Contra;
    public String toString() {
        return getNombre();
    }
    public Usuarios(String nombre, String mail, String contra)
    {
        Nombre=nombre;
        Mail=mail;
        Contra=contra;
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
}
