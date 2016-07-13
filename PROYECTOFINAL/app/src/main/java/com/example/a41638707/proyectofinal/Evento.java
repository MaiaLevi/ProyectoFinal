package com.example.a41638707.proyectofinal;

import android.content.ClipData;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by 41638707 on 24/5/2016.
 */
public class Evento implements Serializable{
    private int Id;
    private MateriaEvento Materia;
    private TipoEvento Tipo;
    private Date Fecha;
    private String Descripcion;
    private int IdUsuario;
    @Override
    public String toString() {
        return getMateria() + " "+getTipo()+" "+getFecha()+" "+getDescripcion();
    }

    public Evento(int id, MateriaEvento materia, TipoEvento tipo, Date fec, String descr, Integer IdU)
    {
        Id=id;
        Materia=materia;
        Tipo=tipo;
        Fecha=fec;
        Descripcion=descr;
        IdUsuario=IdU;
    }
    public int getId()
    {
        return Id;
    }
    public MateriaEvento getMateria()
    {
        return Materia;
    }
    public TipoEvento getTipo()
    {
        return Tipo;
    }
    public Date getFecha()
    {
        return Fecha;
    }
    public String getDescripcion()
    {
        return Descripcion;
    }
    public Integer getIdUsuario()
    {
        return IdUsuario;
    }
}
