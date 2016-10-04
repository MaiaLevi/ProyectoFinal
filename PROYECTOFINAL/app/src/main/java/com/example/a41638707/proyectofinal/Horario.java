package com.example.a41638707.proyectofinal;

/**
 * Created by daianaszwimer on 4/10/16.
 */
public class Horario {private int Id;
    private String dia;
    private int bloque;
    private MateriaEvento materia;
    private Division division;
    public Horario(String Dia, int Bloque, MateriaEvento materiaEvento, Division Division)
    {
        dia=Dia;
        bloque=Bloque;
        materia=materiaEvento;
        division=Division;
    }
}
