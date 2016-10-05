package com.example.a41638707.proyectofinal;

/**
 * Created by daianaszwimer on 4/10/16.
 */
public class Horario {
    //private int Id;
    //private String dia;
    private int bloque;
    private MateriaEvento materia;
    @Override
    public String toString()
    {
        return "Bloque: "+getBloque()+" materia: "+getMateria();
    }
    public Horario(int Bloque, MateriaEvento materiaEvento)
    {
       // dia=Dia;
        bloque=Bloque;
        materia=materiaEvento;
    }
    /*public int getId()
    {
        return Id;
    }*/
    public int getBloque()
    {
        return bloque;
    }
    public MateriaEvento getMateria()
    {
        return materia;
    }

}
