package com.example.a41638707.proyectofinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VerDatos extends AppCompatActivity {

    TextView TxtNombre, TxtMail, TxtCelular;
    String url="http://apicampus.azurewebsites.net/traerUsuario.php?idusuario=";
    Button btnModificar, btncancel;
    Usuarios Miusu;
    int idUsu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_datos);
        Obtnerref();
        idUsu=Usuarios.getId();
        String url="http://apicampus.azurewebsites.net/traerUsuario.php?idusuario=";
        url=url+idUsu;
        new traerUsuario().execute(url);
        //TraerUsuario();
        btnModificar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IniciarModAct();
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Atras();
            }
        });

    }
    @Override
    public void onBackPressed() {
    }
    private void Atras()
    {
        this.finish();
    }

    public void Obtnerref()
    {
        TxtNombre = (TextView) findViewById(R.id.txtNombre);
        TxtMail = (TextView) findViewById(R.id.txtMail);
        TxtCelular= (TextView) findViewById(R.id.txtCelular);
        btnModificar=(Button) findViewById(R.id.btnModificar);
        btncancel=(Button) findViewById(R.id.btnVolver);
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
                return parsearUsu(response.body().string());      // Convierto el resultado en Evento

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return null;
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("","acaa");
        }
        @Override
        protected void onPostExecute(Usuarios usuario) {
            super.onPostExecute(usuario);
            Log.d("entro","entro");
            TxtNombre.setText(Miusu.getNombre());
            TxtCelular.setText(String.valueOf(Miusu.getTelefono()));
            TxtMail.setText(Miusu.getMail());
            Log.d("entro2","entro2");

        }
        Usuarios parsearUsu(String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);                 // Convierto el String recibido a JSONObject
            int idusuario = json.getInt("idusuario");
            String nombre = json.getString("nombre");
            String apellido = json.getString("apellido");
            String mail = json.getString("mail");
            String contrasena=json.getString("contrasena");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(json.getString("fechanacimiento"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int celular = json.getInt("celular");
            int iddivision = json.getInt("iddivision");
            Miusu = new Usuarios(nombre, mail, contrasena, celular);
            Miusu.setNombre(nombre);
            Miusu.setApellido(apellido);
            Miusu.setContra(contrasena);
            Miusu.setTelefono(celular);
            Miusu.setMail(mail);
            return Miusu;
        }
    }
    //mostrar fecha nacimiento!!!
    /*  public void TraerUsuario()
      {
          int id = Usuarios.getId();
          HttpClient httpClient = new DefaultHttpClient();
          HttpGet getRequest = new HttpGet(url + id);
          getRequest.setHeader("content-type", "application/json");
          try {
              HttpResponse resp = httpClient.execute(getRequest);
              String respStr = EntityUtils.toString(resp.getEntity());
              JSONObject respJSON = new JSONObject(respStr);
              int idusuario = respJSON.getInt("idusuario");
              String nombre = respJSON.getString("nombre");
              String apellido = respJSON.getString("apellido");
              String mail = respJSON.getString("mail");
              String contrasena=respJSON.getString("contrasena");

              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
              Date convertedDate = new Date();
              try {
                  convertedDate = dateFormat.parse(respJSON.getString("fechanacimiento"));
              } catch (ParseException e) {
                  e.printStackTrace();
              }

              int celular = respJSON.getInt("celular");
              int iddivision = respJSON.getInt("iddivision");
              TxtNombre.setText(nombre);
              TxtApellido.setText(apellido);
              TxtMail.setText(mail);
              TxtFecha.setText(convertedDate.toString());
              Usuarios.setId(idusuario);
          } catch(Exception ex) {
              Log.e("ServicioRest","Error!", ex);
          }
      }*/
    public void IniciarModAct()
    {
        Intent nuevaActivity = new Intent(this, ModificarDatos.class);
        startActivity(nuevaActivity);
    }
}