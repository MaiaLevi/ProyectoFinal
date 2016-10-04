package com.example.a41638707.proyectofinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;

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

public class ListarHorario extends AppCompatActivity {
    String url;
    ProgressDialog progressDialog;
    ArrayList<Horario> listaHorario=new ArrayList<Horario>();
    ListView lstHorario;
    ArrayAdapter<Horario> adaptador;
    TabHost tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_horario);
        url="cambiar url";
        new listarEventos().execute(url);
        progressDialog=new ProgressDialog(this);
        obtenerReferencias();
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
    }
    private void chau()
    {
        this.finish();
    }
    private void obtenerReferencias()
    {

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
        protected void onPostExecute(ArrayList<Horario> listaMaterias) {
            super.onPostExecute(listaMaterias);
            progressDialog.dismiss();
            //adapter
            adaptador = new ArrayAdapter<Horario>(getApplicationContext(), android.R.layout.simple_list_item_1, listaHorario);
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
                int idEvento = obj.getInt("Id");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(obj.getString("Fecha"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String Dia = obj.getString("DIA");
                int Bloque = obj.getInt("BLOQUE");
                String Materia = obj.getString("Materia");
                int idMateria=obj.getInt("IdMateria");
                MateriaEvento miMateria=new MateriaEvento(idMateria,Materia);
                String Division=obj.getString("DIVISION");
                int idDivision=obj.getInt("IdDivision");
                Division miDivi=new Division(idDivision,Division);
                Horario unhorario =new Horario(Dia,Bloque,miMateria,miDivi);
                listaHorario.add(unhorario);
            }
            return listaHorario;
        }
    }
}

