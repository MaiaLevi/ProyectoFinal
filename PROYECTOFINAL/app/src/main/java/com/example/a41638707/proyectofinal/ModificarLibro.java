package com.example.a41638707.proyectofinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ModificarLibro extends AppCompatActivity {
    public ProgressDialog progressDialog;
    ArrayList<MateriaEvento> materias = new ArrayList<MateriaEvento>();
    Spinner spnMaterias, spnAnios;
    MateriaEvento eventoMateria, materiaSeleccionada;
    Button btnGuardar;
    Libros miLibro;
    EditText edtDescr, edtNombre;
    int idUsuario=1;
    CheckBox chkVendido;
    int[] anios = new int[]{7,1,2,3,4,5,6};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_libro);
        obtenerReferencias();
        String url="http://daiuszw.hol.es/bd/traerLibro.php?Id=";
        new traerLibro().execute(url);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "http://daiuszw.hol.es/bd/modificarLibro.php" ;
                new modificarEvento().execute(url);
                Toast.makeText(getApplicationContext(), "Se ha guardado el libro", Toast.LENGTH_SHORT).show();
                GuardarEvento();
                irAtras();
            }
        });
        spnMaterias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                materiaSeleccionada = materias.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
}
    private void obtenerReferencias()
    {
        edtDescr=(EditText)findViewById(R.id.edtDescr);
        edtNombre=(EditText)findViewById(R.id.edtNombre);
        spnMaterias=(Spinner)findViewById(R.id.spnMaterias);
        spnAnios=(Spinner)findViewById(R.id.spnAnios);
        chkVendido=(CheckBox)findViewById(R.id.chkVendido);
    }
    private void irAtras() {
        this.finish();
    }
    private void GuardarEvento() {
        Intent nuevaActivity = new Intent(ModificarLibro.this, VerLibro.class);
        startActivity(nuevaActivity);
    }
    private class traerMaterias extends AsyncTask<String, Void, ArrayList<MateriaEvento>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected ArrayList<MateriaEvento> doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al API
                return parsearMaterias(response.body().string());      // Convierto el resultado en Evento

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
        }
        @Override
        protected void onPostExecute(ArrayList<MateriaEvento> listaMaterias) {
            super.onPostExecute(listaMaterias);
            progressDialog.dismiss();
            ArrayAdapter<MateriaEvento> adapterMaterias = new ArrayAdapter<MateriaEvento>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, materias);
            adapterMaterias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnMaterias.setAdapter(adapterMaterias);
            for (int i = 0; i < materias.size(); i++) {
                //cuando ejecuto me aparece null pointer exception pero debuggeo y esta todo ok
                if (materias.get(i).getId() == miLibro.getMateria().getId()){
                    spnMaterias.setSelection(i);
                }
            }
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        ArrayList<MateriaEvento> parsearMaterias(String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);
            JSONArray respJSON = json.getJSONArray("result");
            for (int i=0;i<respJSON.length();i++)
            {
                JSONObject jsonResult=respJSON.getJSONObject(i);
                int id = jsonResult.getInt("IdMateria");
                String nombre = jsonResult.getString("Nombre");
                eventoMateria = new MateriaEvento(id, nombre);
                materias.add(eventoMateria);
            }
            return materias;
        }
    }
    private class modificarEvento extends AsyncTask<String, Void, Void> {
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
            try {
                JSONObject dato = new JSONObject();
                dato.put("Nombre", (edtNombre.getText().toString()));
                dato.put("Descripcion", (edtDescr.getText().toString()));
                dato.put("IdMateria", materiaSeleccionada.getId());
                //dato.put("IdUsuario", 1);
                //dato.put("Id", idEvento);
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), dato.toString());
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
            } catch (JSONException e) {
                Log.d("Error", e.getMessage());
            }
        }
    }
    private class traerLibro extends AsyncTask<String, Void, Libros> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected Libros doInBackground(String... params) {
            String url = params[0];

            Request request = new Request.Builder()
                    //error aca
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al API
                return parsearLibro(response.body().string());      // Convierto el resultado en Evento

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
        protected void onPostExecute(Libros libros) {
            super.onPostExecute(libros);
            progressDialog.dismiss();
            String url="http://daiuszw.hol.es/bd/listarMateriaEvento.php";
            new traerMaterias().execute(url);
            edtDescr.setText(libros.getDesc());
            edtNombre.setText(libros.getNombre());
        }
        Libros parsearLibro(String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);                 // Convierto el String recibido a JSONObject
            Libros miLibro;
            int id = json.getInt("Id");
            String nombre=json.getString("Nombre");
            String descripcion = json.getString("Descripcion");
            //año
            int año = json.getInt("Anio");
            String materia =json.getString("Materia");
            int idMateria = json.getInt("IdMateria");
            int vendido = json.getInt("Vendido");
            boolean blnVendido=false;
            if (vendido==1)
            {
                blnVendido=true;
            }
            eventoMateria = new MateriaEvento(idMateria, materia);
            miLibro=new Libros(id,nombre,descripcion, null, idUsuario, "", año, eventoMateria, blnVendido );
            return miLibro;
        }
    }
}
