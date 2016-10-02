package com.example.a41638707.proyectofinal;

import android.app.ActionBar;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
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
    public ProgressDialog progressDialog;
    Button  btnIniciarSesion, btnLogout;
    EditText edtMail, edtContra;
    Usuarios miUsuario;
    Division miDivision;
    View layoutBotones, layoutLogin;
    TextView tvwBienvenido;
    Boolean sesion, blnMail;
    CheckBox chkMail;
    String nombre, mail, division;
    int iddivision, id;
    TabHost tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObtenerReferencias();
        progressDialog = new ProgressDialog(this);
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        sesion = prefs.getBoolean("sesion",false );
        nombre=prefs.getString("nombre","");
        mail=prefs.getString("email","");
        id=prefs.getInt("id",0);
        //PROBAR ABAJO
        iddivision=prefs.getInt("iddivision",0);
        division=prefs.getString("division", "");
        miDivision=new Division(iddivision,division);
        miUsuario= new Usuarios(nombre,mail,"",0);
        //CARGAR VALORES EN CLASE USUARIO
        if (sesion)
        {
            tvwBienvenido.setText("Bienvenido/a "+nombre);
            layoutLogin.setVisibility(View.GONE);
            layoutBotones.setVisibility(View.VISIBLE);
            Usuarios.setDivision(miDivision);
            Usuarios.setId(id);
        }
        else
        {
            if (!miUsuario.getMail().equals(""))
            {
                blnMail=prefs.getBoolean("mail", false);
                if (blnMail)
                {
                    //Mostrar miUsuario.getMail();
                }
            }
        }
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch(tabId)
                {
                    case ("eventos"):

                        Intent nuevaActivity=new Intent(getApplicationContext(),Listar.class);
                        startActivity(nuevaActivity);
                        chau();
                    break;
                    case ("libros"):

                        Intent nuevaActivity2=new Intent(getApplicationContext(),ListarLibrosPropios.class);
                        startActivity(nuevaActivity2);
                        chau();
                    break;
                    case ("horario"):
                    {
                        //todavia nada porque no esta la activity
                    }
                    break;
                }
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Sacar layout que dice bienvenido y volver a mostrar el iniciar sesion
                //En shared preferernces cambiar config
                Dialog dialog=confirmarLogout();
                dialog.show();
            }
        });
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "http://daiuszw.hol.es/bd/login.php?Mail=";
                url += edtMail.getText().toString();
                if (!edtMail.getText().toString().equals("")&&!edtMail.getText().toString().equals(" "))
                {
                    if (!edtContra.getText().toString().equals("")&&!edtContra.getText().toString().equals(" "))
                    {
                        new traerUsuario().execute(url);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "La contraseña no puede ser vacía", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "El mail no puede ser vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });
       }
        //CAMBIAR POR TAB
        /*navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        boolean blnMenu=false;
                        switch (menuItem.getItemId()) {
                            case R.id.Eventos:
                                IniciarListarActividad();
                                blnMenu=true;
                                break;
                            case R.id.Libros:
                                blnMenu=true;
                                IniciarListarLActividad();
                                break;
                            case R.id.Horario:
                                blnMenu=true;
                                break;
                        }
                        if (blnMenu){
                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }*/
    private void chau()
    {
        this.finish();
    }
    private Dialog confirmarLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert Dialog");
        builder.setMessage("¿Está seguro que desea cerrar sesión?");
        builder.setPositiveButton("Cerrar sesión", new  DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Diálogos", "Confirmación Aceptada.");
                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("sesion", false);
                editor.commit();
                layoutLogin.setVisibility(View.VISIBLE);
                layoutBotones.setVisibility(View.GONE);
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
        btnIniciarSesion=(Button)findViewById(R.id.btnIniciarSesion);
        edtMail=(EditText)findViewById(R.id.edtMail);
        edtContra=(EditText)findViewById(R.id.edtContrasena);
        tvwBienvenido=(TextView)findViewById(R.id.tvwBienvenido);
        layoutBotones=findViewById(R.id.botones);
        layoutLogin=findViewById(R.id.login);
        btnLogout=(Button)findViewById(R.id.btnLogout);
        chkMail=(CheckBox)findViewById(R.id.chkMail);
        //SETEO TABS
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
        tabs.setCurrentTab(0);
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
                    SharedPreferences prefs =
                            getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("email", miUsuario.getMail());
                    editor.putString("nombre", miUsuario.getNombre());
                    editor.putInt("iddivision", Usuarios.getDivision().getId());
                    editor.putString("division", Usuarios.getDivision().getNombre());
                    editor.putInt("id", Usuarios.getId());
                    editor.putBoolean("sesion", true);
                    if (chkMail.isChecked())
                    {
                        editor.putBoolean("mail", true);
                    }
                    editor.commit();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Usuario incorrecto", Toast.LENGTH_SHORT).show();
            }
        }
        Usuarios parsearUsuario (String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);
            int id=json.getInt("IdUsuario");
            String nombre=json.getString("Nombre");
            String contrasena = json.getString("Contrasena");
            int divi=json.getInt("IdDivision");
            String division = json.getString("Division");
            Division miDivi=new Division(divi,division);
            miUsuario=new Usuarios(nombre,edtMail.getText().toString(),contrasena,0);
            Usuarios.setId(id);
            Usuarios.setDivision(miDivi);
            return miUsuario;
        }
    }
}

