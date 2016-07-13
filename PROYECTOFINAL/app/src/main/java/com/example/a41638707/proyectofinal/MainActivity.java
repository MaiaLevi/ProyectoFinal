package com.example.a41638707.proyectofinal;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
   // public static final ArrayList<Evento> PARAMETRO1=new ArrayList<Evento>();
    String url="192.168.56.1";
    Button btnListar;
    Button btnAgregar;
    Button btnListarLibros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
       // navigationView.setNavigationItemSelectedListener(this);
*/
        ObtenerReferencias();
        btnListar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //new ListarEventos().execute(url);
             //NO ANDA   List<Evento> lisLin= (List<Evento>) new ProgressTask(MainActivity.this).execute("192.168.56.1/listareventos.php");
                IniciarListarActividad();
            }
        });
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IniciarAgregarActividad();;
            }
        });
        btnListarLibros.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               IniciarListarLActividad();
            }
        });
    }

    ArrayList<Evento> eventos = new ArrayList<>();
    private void ObtenerReferencias()
    {
        btnListarLibros=(Button)findViewById(R.id.btnListarLibros);
        btnAgregar=(Button) findViewById(R.id.btnAgregar);
        btnListar=(Button)findViewById(R.id.btnListar);
    }
    private void IniciarAgregarActividad()
    {
        Intent nuevaActivity=new Intent(MainActivity.this,Agregar.class);
        startActivity(nuevaActivity);
    }

    private void IniciarListarActividad()
    {
        Intent nuevaActivity=new Intent(MainActivity.this,Listar.class);
        startActivity(nuevaActivity);
    }
    private void IniciarListarLActividad()
    {
        Intent nuevaActivity=new Intent(MainActivity.this,ListarLibrosPropios.class);
        startActivity(nuevaActivity);
    }

    /*
private class ListarEventos extends AsyncTask<String, Void, ArrayList<Evento>> {
    private OkHttpClient client = new OkHttpClient();
    @Override
    protected void onPostExecute(ArrayList<Evento> direcciones) {
        super.onPostExecute(direcciones);
    }
    @Override
    protected ArrayList<Evento> doInBackground(String... params) {
        String url=params[0];
        Request request = new Request.Builder()
                .url(url+"/listareventos.php")
                .build();
        try {

            Response response = client.newCall(request).execute();  // Llamado al API
            return parsearResultado(response.body().string());      // Convierto el resultado en ArrayList<Direccion>
        } catch (IOException | JSONException e) {
            Log.d("Error",e.getMessage());                          // Error de Network o al parsear JSON
            return new ArrayList<Evento>();
        }
    }

    // Convierte un JSON en un ArrayList de Eventos
    //LISTAR EVENTOS
    ArrayList<Evento> parsearResultado(String JSONstr) throws JSONException {
        JSONObject json = new JSONObject(JSONstr);                 // Convierto el String recibido a JSONObject
        JSONArray jsonEventos = json.getJSONArray("eventos");
        Date result=new Date ();
        // Array - una busqueda puede retornar varios resultados
        for (int i=0; i<jsonEventos.length(); i++) {
            // Recorro los resultados recibidos
            JSONObject jsonResultado = jsonEventos.getJSONObject(i);
            int jsonId = jsonResultado.getInt("Id");
            String jsonMat = jsonResultado.getString("Materia");
            String jsonTipo = jsonResultado.getString("Tipo");
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH");
            String Fecha=jsonResultado.getString("Fecha");
            try {
                result = df.parse(Fecha);
            }catch (Exception e) {

            }
            String jsonDesc = jsonResultado.getString("Descripcion");
            Evento d = new Evento();                    // Creo nueva instancia de direccion
            d.Evento(jsonId,jsonMat,jsonTipo,result,jsonDesc);
            eventos.add(d);                                                 // Agrego objeto d al array list
        }
        return eventos;
    }

}*/
}

