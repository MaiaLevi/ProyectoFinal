package com.example.a41638707.proyectofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class VerLibro extends AppCompatActivity {
    ImageView imgEliminar, imgModificar;
    //MAIU FIJATE QUE YA TE PUSE UNA VARIABLE PARA TU ONCLICK LISTENER
    public static final String PARAMETROLIBRO2="com.example.a41638707.proyectofinal.PARAMETROLIBRO";
    TextView edtNombre, txvDesc, txvImg, txvidmat, txvvendido;
    Libros miLibro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_libro);
        Intent intent = getIntent();
        miLibro = (Libros) intent.getSerializableExtra(ListarLibrosPropios.PARAMETROLIBRO);
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
        imgModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarModificarActividad();
                irAtras();
            }
        });
    }
    private void irAtras() {
        this.finish();
    }
    public void iniciarModificarActividad()
    {
        Intent nuevaActivity=new Intent(this,ModificarLibro.class);
        Bundle datos=new Bundle();
        datos.putInt(VerLibro.PARAMETROLIBRO2,miLibro.getId());
        nuevaActivity.putExtras(datos);
        startActivity(nuevaActivity);
    }
    public void obtenerReferencias()
    {
        edtNombre = (TextView) findViewById(R.id.edtNombre);
        txvDesc=(TextView) findViewById(R.id.txvDesc);
        txvImg=(TextView) findViewById(R.id.txvimg);
        txvidmat=(TextView) findViewById(R.id.idmateria);
        txvvendido = (TextView) findViewById(R.id.txvvendido);
        imgModificar=(ImageView) findViewById(R.id.imgModificar);
    }
}
