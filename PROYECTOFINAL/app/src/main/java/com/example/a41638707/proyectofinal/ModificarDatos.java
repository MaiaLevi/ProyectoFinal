package com.example.a41638707.proyectofinal;

import android.net.ParseException;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModificarDatos extends AppCompatActivity {

    EditText edtNombre, edtApellido, edtMail, edtCelular;
    Usuarios Miusu;
    Button btnGuardar, btnCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_datos);
        ObtenerRef();
        String url="http://apicampus.azurewebsites.net/traerUsuario.php?idusuario=";
        int id=Usuarios.getId();
        url=url+id;
        new traerUsuario().execute(url);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "http://apicampus.azurewebsites.net/modificarUsuario.php";
                new modificarUsuario().execute(url);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Atras();
            }
        });
    }
    public void ObtenerRef()
    {
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtApellido = (EditText) findViewById(R.id.edtApellido);
        edtMail = (EditText) findViewById(R.id.edtMail);
        edtCelular = (EditText) findViewById(R.id.edtCelular);
        btnGuardar=(Button) findViewById(R.id.btnGuardar);
        btnCancelar=(Button) findViewById(R.id.btnCancelar);
    }
    private class modificarUsuario extends AsyncTask<String, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Se han modificado los datos con éxito!", Toast.LENGTH_SHORT).show();
            Atras();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];
            try {
                enviarJSON(url);
            } catch (JSONException e) {
                Log.d("Error", e.getMessage());
            }
            return null;
        }
        void enviarJSON(String url) throws JSONException {
            try {
                JSONObject dato = new JSONObject();
                dato.put("nombre", edtNombre.getText().toString());
                dato.put("apellido", (edtApellido.getText().toString()));
                dato.put("mail", edtMail.getText().toString());
                dato.put("celular", Integer.valueOf(edtCelular.getText().toString()));
                dato.put("idUsuario", Miusu.getId());
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dato.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());
            }
        }
    }
    private void Atras()
    {
        this.finish();
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
            edtNombre.setText(Miusu.getNombre());
            edtApellido.setText(Miusu.getApellido());
            edtCelular.setText(String.valueOf(Miusu.getTelefono()));
            edtMail.setText(Miusu.getMail());
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
            } catch (java.text.ParseException e) {
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

}
