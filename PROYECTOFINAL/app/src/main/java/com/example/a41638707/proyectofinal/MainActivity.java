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
    Button btnListar;
    Button btnAgregar;
    Button btnListarLibros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}

