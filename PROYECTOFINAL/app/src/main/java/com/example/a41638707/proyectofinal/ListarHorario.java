package com.example.a41638707.proyectofinal;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class ListarHorario extends AppCompatActivity {
    String url;
    ProgressDialog progressDialog;
    ArrayList<Horario> listaHorario=new ArrayList<Horario>();
    ListView lstHorario;
    ArrayAdapter<Horario> adaptador;
    TabHost tabs;
    boolean boton1=false,boton2=false,boton3=false,boton4=false,boton5=false;
    Button btnLunes, btnMartes, btnMierc, btnJueves, btnViernes, btnAgregar;
    Horario horarioSeleccionado;
    //meter en variable num de dia seleccionado y cuando es on restart cambiar parametro
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_horario);
        //por default, lunes
        obtenerReferencias();
        progressDialog=new ProgressDialog(this);
        //en cada on click listener se limpia la lista
        url="http://apicampus.azurewebsites.net/traerDia.php?IdDivision="+Usuarios.getDivision().getId()+"&Dia=1";
        new listarEventos().execute(url);
        /*lstHorario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                horarioSeleccionado= listaHorario.get(position);
                confirmarEliminar();
            }
        });*/
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch(tabId)
                {
                    case ("usuario"):
                        Intent nuevaActivity=new Intent(ListarHorario.this,MainActivity.class);
                        startActivity(nuevaActivity);
                        chau();
                        break;
                    case ("libros"):
                        Intent nuevaActivity2=new Intent(ListarHorario.this,ListarLibrosPropios.class);
                        startActivity(nuevaActivity2);
                        chau();
                        break;
                    case ("eventos"):
                        Intent nuevaActivity3=new Intent(getApplicationContext(),Listar.class);
                        startActivity(nuevaActivity3);
                        chau();
                        break;
                }
            }
        });
        btnLunes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url="http://apicampus.azurewebsites.net/traerDia.php?IdDivision="+Usuarios.getDivision().getId()+"&Dia=1";
                new listarEventos().execute(url);
                boton1=true;
                boton2=false;
                boton5=false;
                boton4=false;
                boton3=false;
                if (boton1)
                {
                    // If you're in an activity:
                    btnLunes.setBackgroundColor(0xff7b7b);
                }
            }
        });
        btnMartes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url="http://apicampus.azurewebsites.net/traerDia.php?IdDivision="+Usuarios.getDivision().getId()+"&Dia=2";
                new listarEventos().execute(url);
                boton2=true;
                boton1=false;
                boton5=false;
                boton4=false;
                boton3=false;
                if (boton2)
                {
                    // If you're in an activity:
                    btnLunes.setBackgroundColor(0xff7b7b);
                }
            }
        });
        btnMierc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url="http://apicampus.azurewebsites.net/traerDia.php?IdDivision="+Usuarios.getDivision().getId()+"&Dia=3";
                new listarEventos().execute(url);
                boton3=true;
                boton2=false;
                boton5=false;
                boton4=false;
                boton1=false;
                if (boton3)
                {
                    // If you're in an activity:
                    btnLunes.setBackgroundColor(0xff7b7b);
                }
            }
        });
        btnJueves.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url="http://apicampus.azurewebsites.net/traerDia.php?IdDivision="+Usuarios.getDivision().getId()+"&Dia=4";
                new listarEventos().execute(url);
                boton4=true;
                boton2=false;
                boton5=false;
                boton1=false;
                boton3=false;
                if (boton4)
                {
                    // If you're in an activity:
                    btnLunes.setBackgroundColor(0xff7b7b);
                }
                //volver a stear con color orignal
            }
        });
        btnViernes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                url="http://apicampus.azurewebsites.net/traerDia.php?IdDivision="+Usuarios.getDivision().getId()+"&Dia=5";
                new listarEventos().execute(url);
                boton5=true;
                boton2=false;
                boton1=false;
                boton4=false;
                boton3=false;
                if (boton5)
                {
                    // If you're in an activity:
                    btnLunes.setBackgroundColor(0xff7b7b);
                }
            }
        });
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                agregarHorario();
            }
        });
    }@Override
    public void onRestart(){
        super.onRestart();
        url="http://apicampus.azurewebsites.net/traerDia.php?IdDivision="+Usuarios.getDivision().getId()+"&Dia=1";
        new listarEventos().execute(url);}
    private void agregarHorario()
    {
        Intent nuevaActivity3=new Intent(getApplicationContext(),AgregarHorario.class);
        startActivity(nuevaActivity3);
    }
    private void chau()
    {
        this.finish();
    }
    private void obtenerReferencias()
    {
        btnAgregar=(Button)findViewById(R.id.btnagregarHorario);
        btnLunes=(Button)findViewById(R.id.btnLunes);
        btnMartes=(Button)findViewById(R.id.btnMartes);
        btnMierc=(Button)findViewById(R.id.btnMierc);
        btnJueves=(Button)findViewById(R.id.btnJueves);
        btnViernes=(Button)findViewById(R.id.btnViernes);
        lstHorario=(ListView)findViewById(R.id.listaHorario);
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
        tabs.setCurrentTab(3);
    }
    private class listarEventos extends AsyncTask<String, Void, ArrayList<Horario>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected ArrayList<Horario> doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return parsearEventos(response.body().string());

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());
                return null;
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.show();
            listaHorario.clear();}
        @Override
        protected void onPostExecute(ArrayList<Horario> lsHorario) {
            super.onPostExecute(lsHorario);
            progressDialog.dismiss();
            //adapter
            if (lsHorario.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "No hay datos", Toast.LENGTH_SHORT).show();
            }
            adaptador = new ArrayAdapter<Horario>(getApplicationContext(), android.R.layout.simple_list_item_1, lsHorario);
            lstHorario.setAdapter(adaptador);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        ArrayList<Horario> parsearEventos(String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);
            JSONArray respJSON = json.getJSONArray("result");
            for (int i = 0; i < respJSON.length(); i++)
            {
                JSONObject obj = respJSON.getJSONObject(i);
                int Bloque = obj.getInt("Bloque");
                String Materia = obj.getString("Materia");
                int idMateria=obj.getInt("IdMateria");
                MateriaEvento miMateria=new MateriaEvento(idMateria,Materia);
                Horario unhorario =new Horario(Bloque,miMateria);
                listaHorario.add(unhorario);
            }
            return listaHorario;
        }
    }
    private Dialog confirmarEliminar(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert Dialog");
        builder.setMessage("¿Desea agregar un evento?");
        builder.setPositiveButton("Eliminar", new  DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Diálogos", "Confirmación Aceptada.");
                //EliminarHorario(horarioSeleccionado.getId());
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
}