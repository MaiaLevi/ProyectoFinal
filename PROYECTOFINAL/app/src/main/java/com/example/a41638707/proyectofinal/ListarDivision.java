package com.example.a41638707.proyectofinal;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListarDivision extends AppCompatActivity {
    public static final String PARAMETRO1 = "com.example.a41638707.proyectofinal.PARAMETRO1";
    ListView lstEventos;
    Evento eventoSeleccionado;
    TipoEvento tipo;
    MateriaEvento materia;
    ArrayList<Evento> listaEventos = new ArrayList<Evento>();
    ArrayAdapter<Evento> adaptador = null;
    ImageView imgAgregar, imgModificar, imgEliminar;
    ProgressDialog progressDialog;
    String url;
    Integer idCreador;
    boolean click = false;
int param=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_division);
        ObtenerReferencias();
        progressDialog = new ProgressDialog(this);
        //LO DE ABAJO SE NECESITA PARA EL AGREGAR Y MODIFICAR
        //url="http://daiuszw.hol.es/bd/idDivision.php?nombre="+division;
        //new obtenerIdDivision().execute(url);
        url = "http://apicampus.azurewebsites.net/listarEventosDivision.php?id=";
        url += Usuarios.getDivision().getId();
        new listarEventos().execute(url);
        if (adaptador != null) {
            adaptador.notifyDataSetChanged();
        }
        lstEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //Desea eliminar o modificar?
                //Modificar
                eventoSeleccionado = listaEventos.get(position);
                click = true;
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
                if (click) {
                    if (Usuarios.getId() == eventoSeleccionado.getIdUsuario()) {
                        IniciarModificarActividad(eventoSeleccionado.getId());
                    } else {
                        Toast toast2 = Toast.makeText(getApplicationContext(), "Usted no creó este evento, por favor elija otro", Toast.LENGTH_SHORT);
                        toast2.show();
                    }
                } else {
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Por favor elija un evento", Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }
        });
        imgEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click) {
                    if (Usuarios.getId() == eventoSeleccionado.getIdUsuario()) {
                        Dialog dialogo = confirmarEliminar();
                        dialogo.show();
                    } else {
                        Toast toast2 = Toast.makeText(getApplicationContext(), "Usted no creó este evento, por favor elija otro", Toast.LENGTH_SHORT);
                        toast2.show();
                    }
                } else {
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Por favor elija un evento", Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }
        });
    }
    @Override
    public void onRestart() {
        super.onRestart();
        url = "http://apicampus.azurewebsites.net/listarEventosDivision.php?id=";
        url += Usuarios.getDivision().getId();
        new listarEventos().execute(url);
        if (adaptador != null) {
            adaptador.notifyDataSetChanged();
        }
    }
    class eliminar extends AsyncTask<String, Void, Void> {
        OkHttpClient client = new OkHttpClient();

        //esta hecho para el orto pero si anda no importa. Con amor, Daiu<3
        protected Void doInBackground(String... urls) {
            try {
                Request request = new Request.Builder()
                        .url("http://apicampus.azurewebsites.net/EliminarEvento.php?Id=" + param)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    Log.d("ANSWER", response.body().string());
                } catch (IOException e) {
                    Log.e("ERROR", e.toString());
                }
                return null;
            } catch (Exception e) {

                Log.e("ERROR", e.toString());
                return null;
            }
        }
    }
        private void ObtenerReferencias() {
            lstEventos = (ListView) findViewById(R.id.lstEventos);
            imgAgregar = (ImageView) findViewById(R.id.imgAgregar);
            imgModificar = (ImageView) findViewById(R.id.imgModificar);
            imgEliminar = (ImageView) findViewById(R.id.imgEliminar);
        }

        private void IniciarModificarActividad(int i) {
            Intent nuevaActivity = new Intent(this, Modificar.class);
            Bundle datos = new Bundle();
            datos.putInt(ListarDivision.PARAMETRO1, i);
            nuevaActivity.putExtras(datos);
            startActivity(nuevaActivity);
        }

        private void IniciarAgregarActividad() {
            Intent nuevaActivity = new Intent(this, Agregar.class);
            startActivity(nuevaActivity);
        }

        private Dialog confirmarEliminar() {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Eliminar evento");
            builder.setMessage("¿Está seguro que desea eliminar el evento?");
            builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("Diálogos", "Confirmación Aceptada.");
                    param=eventoSeleccionado.getId();
                    new eliminar().execute("anda");
                    new listarEventos().execute(url);
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

        private class listarEventos extends AsyncTask<String, Void, ArrayList<Evento>> {
            private OkHttpClient client = new OkHttpClient();

            @Override
            protected ArrayList<Evento> doInBackground(String... params) {
                String url = params[0];
                Request request = new Request.Builder()
                        //error aca
                        .url(url)
                        .build();
                try {
                    Response response = client.newCall(request).execute();  // Llamado al API
                    return parsearEventos(response.body().string());      // Convierto el resultado en Evento

                } catch (IOException | JSONException e) {
                    Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                    return null;
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMax(100);
                progressDialog.show();
                listaEventos.clear();
            }

            @Override
            protected void onPostExecute(ArrayList<Evento> listaMaterias) {
                super.onPostExecute(listaMaterias);
                progressDialog.dismiss();
                //adapter
                adaptador = new ArrayAdapter<Evento>(getApplicationContext(), android.R.layout.simple_list_item_1, listaEventos);
                lstEventos.setAdapter(adaptador);
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            ArrayList<Evento> parsearEventos(String JSONstring) throws JSONException {
                JSONObject json = new JSONObject(JSONstring);
                JSONArray respJSON = json.getJSONArray("result");
                for (int i = 0; i < respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);
                    int idEvento = obj.getInt("Id");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date convertedDate = new Date();
                    try {
                        convertedDate = dateFormat.parse(obj.getString("Fecha"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String descEvento = obj.getString("Descripcion");
                    int IdMateriaEvento = obj.getInt("IdMateria");
                    String MateriaEvento = obj.getString("Materia");
                    int IdTipo = obj.getInt("IdTipo");
                    String tipoEvento = obj.getString("Tipo");
                    idCreador = obj.getInt("IdUsuario");
                    tipo = new TipoEvento(IdTipo, tipoEvento);
                    materia = new MateriaEvento(IdMateriaEvento, MateriaEvento);
                    //no tiene sentido traer la division si son todos de la misma
                    Evento unEvento = new Evento(idEvento, materia, tipo, convertedDate, descEvento, idCreador, Usuarios.getDivision());
                    listaEventos.add(unEvento);
                }
                return listaEventos;
            }
        }
    }

