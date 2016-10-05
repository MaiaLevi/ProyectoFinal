package com.example.a41638707.proyectofinal;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AgregarHorario extends AppCompatActivity {
    Spinner spndia,spnbloque,spnMateria;
    List<MateriaEvento> materias;
    Button btnAtras, btnGuardar;
    MateriaEvento materiaSeleccionada;
    String[] arrayDias = {"Lunes", "Martes", "Miercoles", "Jueves", "Viernes"};
    int[] arrayBloques = {1,2,3, 4, 5, 6};
    ArrayAdapter<MateriaEvento> adapterMaterias;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_horario);
        ObtenerRef();
        String urlMateria="http://apicampus.azurewebsites.net/listarMateriaEvento.php";
        new traerMaterias().execute(urlMateria);
        //adapter de spinners de dias y bloques
        spnMateria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                materiaSeleccionada = materias.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnAtras.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                chau();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url="http://apicampus.azurewebsites.net/agregarHorario.php";
                new agregarhorario().execute(url);
            }
        });
    }
    private void chau()
    {
        this.finish();
    }
    private void ObtenerRef()
    {
        btnAtras=(Button)findViewById(R.id.btnAtras);
        btnGuardar=(Button)findViewById(R.id.btnGuardar);
        spndia=(Spinner) findViewById(R.id.spndia);
        spnbloque=(Spinner) findViewById(R.id.spnBloque);
        spnMateria=(Spinner) findViewById(R.id.spnMateria);
    }
    private class agregarhorario extends AsyncTask<String, Void, Void> {
        public OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
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
            JSONObject json = new JSONObject();
            try {
                json.put("iddivision", Usuarios.getDivision().getId());
                json.put("bloque", spnbloque.getSelectedItem().toString());
                json.put("idsemana", spndia.getSelectedItem());
                json.put("idmateria", materiaSeleccionada.getId());
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class traerMaterias extends AsyncTask<String, Void, List<MateriaEvento>> {
        public OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(List<MateriaEvento> list) {
            super.onPostExecute(list);
            adapterMaterias = new ArrayAdapter<MateriaEvento>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, materias);
            adapterMaterias.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            spnMateria.setAdapter(adapterMaterias);
        }
        @Override
        protected List<MateriaEvento> doInBackground(String... params) {
            String url = params[0];
            try {
                return enviarJSON(url);
            } catch (JSONException e) {
                Log.d("Error", e.getMessage());
                return null;
            }
        }
        // Convierte un JSON en un ArrayList de Direccion
        List<MateriaEvento>  enviarJSON(String url) throws JSONException {
            int id=0;
            String nombre="";
            MateriaEvento mimat;
            OkHttpClient httpClient = new  OkHttpClient();
            HttpGet getRequest = new HttpGet(url);
            getRequest.setHeader("content-type", "application/json");
            materias = new ArrayList<MateriaEvento>();
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response resp = client.newCall(request).execute();
                JSONObject json = new JSONObject(resp.body().string());                 // Convierto el String recibido a JSONObject
                JSONArray respJSON = json.getJSONArray("result");
                for(int i=0; i<respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);
                    id = obj.getInt("IdMateria");
                    nombre = obj.getString("Nombre");
                    mimat=new MateriaEvento(id,nombre);
                    materias.add(mimat);
                }
            }
            catch(Exception ex) {
                Log.e("ServicioRest","Error!", ex);
            }
            return materias;
        }
    }
}
