package com.example.a41638707.proyectofinal;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
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

public class Listar extends MainActivity {
    public static final String PARAMETRO1="com.example.a41638707.proyectofinal.PARAMETRO1";
    ListView lstEventos;
    Button btnDivision;
    Evento eventoSeleccionado;
    TipoEvento tipo;
    MateriaEvento materia;
    ArrayList<Evento> listaEventos=new ArrayList<Evento>();
    ArrayAdapter<Evento> adaptador;
    NotificationManager mNotificationManager;
    ImageView imgAgregar, imgModificar, imgEliminar;
    ProgressDialog progressDialog;
    String url;
    TabHost tabs;
    boolean click=false;
    NotificationCompat.Builder mBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        ObtenerReferencias();
        //no se actualiza
        progressDialog=new ProgressDialog(this);
        traerTodo();
        //ListarJsonEventos();
        lstEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //Desea eliminar o modificar?
                //Modificar
                eventoSeleccionado= listaEventos.get(position);
                click=true;
                //param=position;
                //Dialog dialogoElegir=crearDialogoAlerta();
                //dialogoElegir.show();
                //Acciones necesarias al hacer click
            }
        });
      //ERRO ACA  mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
        btnDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IniciarDivisionActividad();
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
                    IniciarModificarActividad(eventoSeleccionado.getId());
                }
                else
                {
                    Toast toast1 = Toast.makeText(getApplicationContext(),"Por favor elija un evento",Toast.LENGTH_SHORT);
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
                    Toast toast1 = Toast.makeText(getApplicationContext(),"Por favor elija un evento",Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }
        });
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch(tabId)
                {
                    case ("usuario"):

                        Intent nuevaActivity=new Intent(Listar.this,MainActivity.class);
                        startActivity(nuevaActivity);
                        chau();
                        break;
                    case ("libros"):

                        Intent nuevaActivity2=new Intent(Listar.this,ListarLibrosPropios.class);
                        startActivity(nuevaActivity2);
                        chau();
                        break;
                    case ("horario"):
                        Intent nuevaActivity3=new Intent(getApplicationContext(),ListarHorario.class);
                        startActivity(nuevaActivity3);
                        chau();
                    break;
                }
            }
        });
    }
    @Override
    public void onRestart(){
        super.onRestart();
        click=false;
        progressDialog=new ProgressDialog(this);
        traerTodo();
    }
    private void chau()
    {
        this.finish();
    }
    private void traerTodo()
    {
        listaEventos.clear();
        url="http://apicampus.azurewebsites.net/listarEventos.php?IdUsuario=";
        //ver si lo de abajo anda
        url+= Usuarios.getId();
        new listarEventos().execute(url);
        if (adaptador!=null)
        {adaptador.notifyDataSetChanged();
        }
    }
    private void IniciarDivisionActividad()
    {
        Intent nuevaActivity=new Intent(this,ListarDivision.class);
        startActivity(nuevaActivity);
    }
    private void EliminarEvento(int param)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpClient = new DefaultHttpClient();
        HttpDelete delRequest = new HttpDelete("http://apicampus.azurewebsites.net/eliminarevento.php?Id=" + param);
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
    private void ObtenerReferencias()
    {
        lstEventos=(ListView)findViewById(R.id.lstEventos);
        imgAgregar=(ImageView)findViewById(R.id.imgAgregar);
        imgModificar=(ImageView)findViewById(R.id.imgModificar);
        imgEliminar=(ImageView)findViewById(R.id.imgEliminar);
        btnDivision=(Button)findViewById(R.id.btnDivision);
        Resources res = getResources();
        tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec=tabs.newTabSpec("usuario");
        spec.setContent(R.id.tab1);
        spec.setIndicator("",
                res.getDrawable(R.drawable.ic_person_black_24dp1));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("eventos");
        spec.setContent(R.id.tab2);
        spec.setIndicator("",
                res.getDrawable(R.drawable.ic_assignment_black_24dp));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("libros");
        spec.setContent(R.id.tab3);
        spec.setIndicator("",
                res.getDrawable(R.drawable.ic_local_library_black_24dp));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("horario");
        spec.setContent(R.id.tab4);
        spec.setIndicator("",
                res.getDrawable(R.drawable.ic_alarm_black_24dp));
        tabs.addTab(spec);
        tabs.setCurrentTab(1);
    }
    private void IniciarModificarActividad(int i)
    {
        Intent nuevaActivity=new Intent(this,Modificar.class);
        Bundle datos=new Bundle();
        datos.putInt(Listar.PARAMETRO1,i);
        nuevaActivity.putExtras(datos);
        startActivity(nuevaActivity);
    }
    private void IniciarAgregarActividad()
    {
        Intent nuevaActivity=new Intent(this,Agregar.class);
        startActivity(nuevaActivity);
    }
    private Dialog confirmarEliminar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert Dialog");
        builder.setMessage("¿Está seguro que desea eliminar el evento?");
        builder.setPositiveButton("Eliminar", new  DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.i("Diálogos", "Confirmación Aceptada.");
                    EliminarEvento(eventoSeleccionado.getId());
                    click=false;
                   //no anda adaptador.notifyDataSetChanged();
                    //probar si el notify anda, onpostexecute
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
            listaEventos.clear();}
        @Override
        protected void onPostExecute(ArrayList<Evento> listaMaterias) {
            super.onPostExecute(listaMaterias);
            progressDialog.dismiss();
            //adapter
            adaptador = new ArrayAdapter<Evento>(getApplicationContext(), android.R.layout.simple_list_item_1, listaEventos);
            lstEventos.setAdapter(adaptador);
            lstEventos.deferNotifyDataSetChanged();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        ArrayList<Evento> parsearEventos(String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);
            JSONArray respJSON = json.getJSONArray("result");
            for (int i = 0; i < respJSON.length(); i++)
            {
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
                int IdTipo=obj.getInt("IdTipo");
                String tipoEvento = obj.getString("Tipo");
                tipo=new TipoEvento(IdTipo,tipoEvento);
                materia=new MateriaEvento(IdMateriaEvento,MateriaEvento);
                Evento unEvento =new Evento(idEvento,materia,tipo,convertedDate, descEvento,Usuarios.getId(), null);
                listaEventos.add(unEvento);
            }
            return listaEventos;
        }
    }
}
