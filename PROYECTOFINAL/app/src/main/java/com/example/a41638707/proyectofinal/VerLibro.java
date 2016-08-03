package com.example.a41638707.proyectofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class VerLibro extends AppCompatActivity {

    TextView edtNombre, txvDesc, txvImg, txvidmat, txvvendido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_libro);
        Intent intent = getIntent();
        Libros miLibro = (Libros) intent.getSerializableExtra(ListarLibrosPropios.PARAMETROLIBRO);;
        obtenerReferencias();
        edtNombre.setText(miLibro.getNombre());
        if (miLibro.getVendido()==false)
        {
            txvvendido.setText("En Venta");
        }else
        {
            txvvendido.setText("Vendido");
        }
        txvDesc.setText(miLibro.getDesc());
        txvImg.setText(miLibro.getImg());
        txvidmat.setText(miLibro.getMateria().getNombre());

    }
    public void obtenerReferencias()
    {
        edtNombre = (TextView) findViewById(R.id.edtNombre);
        txvDesc=(TextView) findViewById(R.id.txvDesc);
        txvImg=(TextView) findViewById(R.id.txvimg);
        txvidmat=(TextView) findViewById(R.id.idmateria);
        txvvendido = (TextView) findViewById(R.id.txvvendido);

    }
}
