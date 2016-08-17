package com.example.a41638707.proyectofinal;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class VerLibro extends AppCompatActivity {
    ImageView imgEliminar, imgModificar;
    //MAIU FIJATE QUE YA TE PUSE UNA VARIABLE PARA TU ONCLICK LISTENER
    public static final String PARAMETROLIBRO2="com.example.a41638707.proyectofinal.PARAMETROLIBRO";
    TextView edtNombre, txvDesc, txvImg, txvidmat, txvvendido;
    Button btnAtras;
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
        //No tiene imagen txvImg.setText(miLibro.getImg());
        txvidmat.setText(miLibro.getMateria().getNombre());
        imgModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarModificarActividad();
                irAtras();
            }
        });
        imgEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialogo=confirmarEliminar();
                dialogo.show();
            }
        });
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irAtras();
            }
        });
    }
    private Dialog confirmarEliminar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert Dialog");
        builder.setMessage("¿Está seguro que desea eliminar el libro?");
        builder.setPositiveButton("Eliminar", new  DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Diálogos", "Confirmación Aceptada.");
                iniciarEliminar(miLibro.getId());
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Diálogos", "Confirmación Cancelada.");
                dialog.cancel();
            }
        });
        return builder.create();
    }
    public void iniciarEliminar(int param)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpDelete delRequest = new HttpDelete("http://daiuszw.hol.es/bd/EliminarLibro.php?Id=" + param);
        delRequest.setHeader("content-type", "application/json");
        try {
            HttpResponse resp = httpClient.execute(delRequest);
            String respStr = EntityUtils.toString(resp.getEntity());
            if(respStr.equals("true")) {
                Toast toast1 = Toast.makeText(getApplicationContext(),"Eliminado OK.",Toast.LENGTH_SHORT);
                toast1.show();
            }
        } catch(Exception ex) {
            Log.e("ServicioRest","Error!", ex);
            Toast toast2 = Toast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_SHORT);
            toast2.show();
        }
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
        imgEliminar=(ImageView)findViewById(R.id.imgEliminar);
        edtNombre = (TextView) findViewById(R.id.edtNombre);
        txvDesc=(TextView) findViewById(R.id.txvDesc);
        txvImg=(TextView) findViewById(R.id.txvimg);
        txvidmat=(TextView) findViewById(R.id.idmateria);
        txvvendido = (TextView) findViewById(R.id.txvvendido);
        imgModificar=(ImageView) findViewById(R.id.imgModificar);
        btnAtras=(Button)findViewById(R.id.btnAtras);
    }
}
