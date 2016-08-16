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
import java.util.ArrayList;

public class ModificarLibro extends AppCompatActivity {
    public ProgressDialog progressDialog;
    ArrayList<MateriaEvento> materias = new ArrayList<MateriaEvento>();
    Spinner spnMaterias, spnAnios;
    MateriaEvento eventoMateria, materiaSeleccionada;
    Button btnGuardar, btnCancelar;
    Libros miLibro;
    EditText edtDescr, edtNombre;
    int idUsuario=1, idLibro, anioSeleccionado;
    CheckBox chkVendido;
    ArrayList<Integer> anios = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_libro);
        Intent elIntent=getIntent();
        Bundle datos=elIntent.getExtras();
        idLibro=datos.getInt(VerLibro.PARAMETROLIBRO2);
        anios.add(7);
        anios.add(1);
        anios.add(2);
        anios.add(3);
        anios.add(4);
        anios.add(5);
        anios.add(6);
        progressDialog=new ProgressDialog(this);
        obtenerReferencias();
        String url="http://daiuszw.hol.es/bd/traerLibro.php?Id=";
        url+=idLibro;
        new traerLibro().execute(url);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "http://daiuszw.hol.es/bd/modificarLibro.php" ;
                new modificarEvento().execute(url);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
        spnAnios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                anioSeleccionado = anios.get(i);
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
        btnCancelar=(Button)findViewById(R.id.btnCancelar);
        btnGuardar=(Button)findViewById(R.id.btnGuardar);
    }
    private void irAtras() {
        this.finish();
    }
    private void GuardarEvento() {
        Intent nuevaActivity = new Intent(ModificarLibro.this, ListarLibrosPropios.class);
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
            adapterMaterias.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            spnMaterias.setAdapter(adapterMaterias);
            for (int i = 0; i < materias.size(); i++) {
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
        public OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Se ha guardado el libro", Toast.LENGTH_SHORT).show();
            GuardarEvento();
            irAtras();
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
                dato.put("Anio", anioSeleccionado);
                dato.put("IdMateria", materiaSeleccionada.getId());
                dato.put("Vendido", chkVendido.isChecked());
                dato.put("IdLibro", idLibro);
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
            chkVendido.setChecked(libros.getVendido());
            //Spinner anios
            ArrayAdapter<Integer> adaptador = new ArrayAdapter<Integer>(
                    getApplicationContext(),
                    android.R.layout.simple_spinner_item,
                    anios);
            adaptador.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            spnAnios.setAdapter(adaptador);
            for (int i = 0; i < anios.size(); i++) {
                if (anios.get(i) == miLibro.getAño()){
                    spnAnios.setSelection(i);
                }
            }
        }
        Libros parsearLibro(String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);
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
