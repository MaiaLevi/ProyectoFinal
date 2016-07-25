package com.example.a41638707.proyectofinal;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListarLibrosPropios extends AppCompatActivity {
    public static final String PARAMETRO1="com.example.a41638707.proyectofinal.PARAMETRO1";
    ListView lstLibros;
    ArrayList<Libros> listaLibros =new ArrayList<Libros>();
    List<Libros> listLibros = new ArrayList<Libros>();
    ArrayAdapter<Libros> adaptador=null;
    ImageView imgAgregar, imgModificar, imgEliminar;
    ProgressDialog progressDialog;
    Libros Libroselec;
    String url;
    EditText edtBuscar;
    View layoutPpal;
    LinearLayout layout;
    int idUsuario=1;
    boolean click=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_libros_propios);
        ObtenerReferencias();
        progressDialog=new ProgressDialog(this);
        url="http://daiuszw.hol.es/bd/listarLibrosPropios.php?IdUsuario=";
        url+=idUsuario;
        new listarLibros().execute(url);
        if (adaptador!=null)
        {adaptador.notifyDataSetChanged();
        }
        lstLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //Desea eliminar o modificar?
                //Modificar
                Libroselec= listaLibros.get(position);
                click=true;
                //param=position;
                //Dialog dialogoElegir=crearDialogoAlerta();
                //dialogoElegir.show();
                //Acciones necesarias al hacer click
            }
        });
        imgAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IniciarAgregarActividad();
            }
        });
        imgModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click)
                {
                    IniciarModificarActividad(Libroselec.getId());
                }
                else
                {
                    Toast toast1 = Toast.makeText(getApplicationContext(),"Por favor elija un libro",Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }
        });
        imgEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click)
                {
                    Dialog dialogo=confirmarEliminar();
                    dialogo.show();
                }
                else
                {
                    Toast toast1 = Toast.makeText(getApplicationContext(),"Por favor elija un libro",Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }
        });
        edtBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto=edtBuscar.getText().toString();
                if (texto.equals(""))
                {
                    layoutPpal.setVisibility(View.VISIBLE);
                }
                else
                {
                    layoutPpal.setVisibility(View.GONE);
                    url="http://daiuszw.hol.es/bd/buscarLibros.php?Busqueda=";
                    url+=texto;
                    new buscarLibros().execute(url);
                }
            }
        });
    }
    private class buscarLibros extends AsyncTask<String, Void, ArrayList<Libros>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected ArrayList<Libros> doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al API
                return parsearLibros(response.body().string());      // Convierto el resultado en Evento

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return null;
            }
        }
        @Override
        protected void onPreExecute() {
            //limpiar layout
            layout.removeAllViews();
            listaLibros.clear();
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.show();}
        @Override
        protected void onPostExecute(ArrayList<Libros> listaLibros) {
            super.onPostExecute(listaLibros);
            progressDialog.dismiss();
            //adapter
            //for y recorrer cantidad de libros
            for (int i=0;i<listaLibros.size();i++)
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                //add textView
                TextView tvwNombre = new TextView(getApplicationContext());
                tvwNombre.setText(listaLibros.get(i).getNombre()+"\n"+listaLibros.get(i).getDesc()+
                        "\n Año:"+listaLibros.get(i).getAño()+
                        "\n Materia:"+listaLibros.get(i).getMateria().getNombre()+
                        "\n Vendedor:"+listaLibros.get(i).getUsuario());
                tvwNombre.setTextColor(Color.parseColor("#000000"));
                tvwNombre.setId(i);
                tvwNombre.setLayoutParams(params);
                //add the textView to LinearLayout
                layout.addView(tvwNombre);
               }
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        ArrayList<Libros> parsearLibros (String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);
            JSONArray respJSON = json.getJSONArray("result");
            for (int i = 0; i < respJSON.length(); i++)
            {
                JSONObject obj = respJSON.getJSONObject(i);
                int idLibro = obj.getInt("IdLibro");
                String Nombre = obj.getString("Nombre");
                String Descr = obj.getString("Descripcion");
                int Año=obj.getInt("Anio");
                String Imagen = obj.getString("Imagen");
                String Usuario = obj.getString("Usuario");
                int IdUsuario = obj.getInt("IdUsuario");
                int IdMateria = obj.getInt("IdMateria");
                String Materia=obj.getString("Materia");
                MateriaEvento materia=new MateriaEvento(IdMateria,Materia);
                boolean Vendido = false;
                Libros unLibro = new Libros (idLibro,Nombre,Descr,Imagen,IdUsuario,Usuario,Año,materia,Vendido);
                listaLibros.add(unLibro);
            }
            return listaLibros;
        }
    }
    private void ObtenerReferencias()
    {
        lstLibros=(ListView)findViewById(R.id.lstLibros);
        imgAgregar=(ImageView)findViewById(R.id.imgAgregar);
        imgModificar=(ImageView)findViewById(R.id.imgModificar);
        imgEliminar=(ImageView)findViewById(R.id.imgEliminar);
        edtBuscar=(EditText)findViewById(R.id.edtBusqueda);
        layoutPpal=findViewById(R.id.layoutPrincipal);
        layout=(LinearLayout)findViewById(R.id.linealLayout);
    }
    private void IniciarAgregarActividad()
    {
        /*Intent nuevaActivity=new Intent(this,AgregarLibros.class);
        startActivity(nuevaActivity);*/
    }
    private void IniciarModificarActividad(int i)
    {
        /*Intent nuevaActivity=new Intent(this,ModificarLibros.class);
        Bundle datos=new Bundle();
        datos.putInt(Listar.PARAMETRO1,i);
        nuevaActivity.putExtras(datos);
        startActivity(nuevaActivity);*/
    }

    private Dialog confirmarEliminar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert Dialog");
        builder.setMessage("¿Está seguro que desea eliminar el libro?");
        builder.setPositiveButton("Eliminar", new  DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Diálogos", "Confirmación Aceptada.");
               // EliminarLibro(Libroselec.getId());
                new listarLibros().execute(url);
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
    private class listarLibros extends AsyncTask<String, Void, ArrayList<Libros>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected ArrayList<Libros> doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    //error aca
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al API
                return parsearLibros(response.body().string());      // Convierto el resultado en Evento

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            listaLibros.clear();
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.show();}
        @Override
        protected void onPostExecute(ArrayList<Libros> listaLibros) {
            super.onPostExecute(listaLibros);
            progressDialog.dismiss();
            //adapter
            adaptador = new ArrayAdapter<Libros>(ListarLibrosPropios.this, android.R.layout.simple_list_item_1, listaLibros);
            lstLibros.setAdapter(adaptador);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        ArrayList<Libros> parsearLibros (String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);
            JSONArray respJSON = json.getJSONArray("result");
            for (int i = 0; i < respJSON.length(); i++)
            {
                JSONObject obj = respJSON.getJSONObject(i);
                int idLibro = obj.getInt("IdLibro");
                String Nombre = obj.getString("Nombre");
                String Descr = obj.getString("Descripcion");
                String Imagen = obj.getString("Imagen");
                int IdMateria = obj.getInt("IdMateria");
                String materia= obj.getString("NombreMat");
                //int IdUsuario = obj.getInt("IdUsuario");
                //int Año=obj.getInt("Año");
                //Soy Daiu tu api no trae año ni idUsuario
                int Vendido = obj.getInt("Vendido");
                boolean blnVend;
                if (Vendido==0)
                {
                    //se deberia mostrar, de lo contrario no
                    blnVend=false;
                }
                else
                {
                    blnVend=true;
                }
                MateriaEvento materiaEv=new MateriaEvento(IdMateria,materia);
                Libros unLibro = new Libros (idLibro,Nombre,Descr,Imagen,idUsuario,null,0,materiaEv,blnVend);
                listaLibros.add(unLibro);
            }
            return listaLibros;
        }
    }
}
