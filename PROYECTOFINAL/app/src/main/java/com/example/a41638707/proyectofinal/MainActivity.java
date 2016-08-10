package com.example.a41638707.proyectofinal;

import android.app.ActionBar;
import android.app.DownloadManager;
import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
/*
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;*/

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // public static final ArrayList<Evento> PARAMETRO1=new ArrayList<Evento>();
    public static int idUsuario, idDivision;
    public ProgressDialog progressDialog;
    Button btnListar, btnAgregar, btnListarLibros, btnIniciarSesion;
    EditText edtMail, edtContra;
    Usuarios miUsuario;
    View layoutBotones, layoutLogin;
    TextView tvwBienvenido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObtenerReferencias();
        progressDialog = new ProgressDialog(this);
        btnListar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //new ListarEventos().execute(url);
                //NO ANDA   List<Evento> lisLin= (List<Evento>) new ProgressTask(MainActivity.this).execute("192.168.56.1/listareventos.php");
                IniciarListarActividad();
            }
        });
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IniciarAgregarActividad();
            }
        });
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "http://daiuszw.hol.es/bd/login.php?Mail=";
                url += edtMail.getText().toString();
                new traerUsuario().execute(url);
            }
        });

        btnListarLibros.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IniciarListarLActividad();
            }
        });
    }
    public String iniciarSesion()
    {
        MessageDigest crypt = null;
        try {
            crypt = java.security.MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 not supported");
        }
        byte[] digested = crypt.digest(edtContra.getText().toString().getBytes());
        String crypt_password = new String();
        // Converts bytes to string
        for (byte b : digested)
            crypt_password += Integer.toHexString(0xFF & b);
        return crypt_password;
    }
    private void ObtenerReferencias()
    {
        btnListarLibros=(Button)findViewById(R.id.btnListarLibros);
        btnAgregar=(Button) findViewById(R.id.btnAgregar);
        btnListar=(Button)findViewById(R.id.btnListar);
        btnIniciarSesion=(Button)findViewById(R.id.btnIniciarSesion);
        edtMail=(EditText)findViewById(R.id.edtMail);
        edtContra=(EditText)findViewById(R.id.edtContrasena);
        tvwBienvenido=(TextView)findViewById(R.id.tvwBienvenido);
        layoutBotones=findViewById(R.id.botones);
        layoutLogin=findViewById(R.id.login);
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
    private class traerUsuario extends AsyncTask<String, Void, Usuarios> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected Usuarios doInBackground(String... params) {
            String url = params[0];

            Request request = new Request.Builder()
                    //error aca
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al API
                return parsearUsuario(response.body().string());      // Convierto el resultado en Evento

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
            progressDialog.show();}
        @Override
        protected void onPostExecute(Usuarios usu) {
            super.onPostExecute(usu);
            progressDialog.dismiss();
            if (miUsuario!=null)
            {
                if (miUsuario.getContra().equals(iniciarSesion()))
                {
                    tvwBienvenido.setText("Bienvenido/a "+miUsuario.getNombre());
                    layoutLogin.setVisibility(View.GONE);
                    layoutBotones.setVisibility(View.VISIBLE);
                    MainActivity.idUsuario=miUsuario.getId();
                    MainActivity.idDivision=miUsuario.getDivision().getId();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Usuario incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        }
        Usuarios parsearUsuario (String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);
            int id=json.getInt("IdUsuario");
            String nombre=json.getString("Nombre");
            String contrasena = json.getString("Contrasena");
            int divi=json.getInt("IdDivision");
            Division miDivi=new Division(divi,"");
            miUsuario=new Usuarios(id, nombre,null,contrasena,miDivi);
            return miUsuario;
        }
    }
}

