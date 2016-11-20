package com.example.a41638707.proyectofinal;

import android.app.ProgressDialog;
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

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AgregarLibro extends AppCompatActivity {
    EditText edtTitulo,edtDesc;
    Spinner spnMateria, spnAnios;
    Button btnCancelar, btnGuardar;
    List<MateriaEvento> materias;
    ArrayAdapter<MateriaEvento> adapterMaterias;
    MateriaEvento materiaSeleccionada;
    CheckBox chkVendido;
    int anioSeleccionado;
    ProgressDialog progressDialog;
    String url="http://apicampus.azurewebsites.net/AgregarLibro.php";
    String url3="http://apicampus.azurewebsites.net/listarMateriaEvento.php";
    ArrayList<Integer> anios = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_libro);
        ObtenerReferencias();
        anios.add(7);
        anios.add(1);
        anios.add(2);
        anios.add(3);
        anios.add(4);
        anios.add(5);
        anios.add(6);
        progressDialog=new ProgressDialog(this);
        ArrayAdapter<Integer> adaptador = new ArrayAdapter<Integer>(
                getApplicationContext(),
                android.R.layout.simple_spinner_item,
                anios);
        adaptador.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnAnios.setAdapter(adaptador);
        new traerMaterias().execute(url3);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                irAtras();
            }});
        spnMateria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (spnMateria.getSelectedItem().toString()!="") {
                    if (edtTitulo.getText().toString()!="") {
                        new agregarLibro().execute(url);
                        Toast msg = Toast.makeText(getApplicationContext(), "Libro guardado", Toast.LENGTH_LONG);
                        msg.show();
                        irAtras();
                    }
                    else
                    {
                        Toast msg = Toast.makeText(getApplicationContext(), "Ingrese un título al libro", Toast.LENGTH_LONG);
                        msg.show();
                    }
                }
                else
                {
                    Toast msg = Toast.makeText(getApplicationContext(), "Seleccione una materia", Toast.LENGTH_LONG);
                    msg.show();
                }
            }
        });
    }
    public void ObtenerReferencias()
    {
        spnAnios=(Spinner)findViewById(R.id.spnAnios);
        chkVendido=(CheckBox)findViewById(R.id.chkVendido);
        edtTitulo=(EditText) findViewById(R.id.edtTitulo);
        edtDesc=(EditText) findViewById(R.id.edtDesc);
        spnMateria=(Spinner) findViewById(R.id.spnMateria);
        btnCancelar=(Button) findViewById(R.id.btnCancelar);
        btnGuardar=(Button) findViewById(R.id.btnGuardar);
    }
    private void irAtras()
    {
        this.finish();
    }
    private class agregarLibro extends AsyncTask<String, Integer, Void> {
        public OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("Cargando...");
        }
        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];
            try {
                int count = params.length;
                for (int i = 0; i < count; i++) {
                    publishProgress((int) ((i / (float) count) * 100));
                    // Escape early if cancel() is called
                    if (isCancelled()) break;
                }
                enviarJSON(url);
            } catch (JSONException e) {
                Log.d("Error", e.getMessage());
            }
            return null;
        }
        // Convierte un JSON en un ArrayList de Direccion
        void enviarJSON(String url) throws JSONException {
            JSONObject json = new JSONObject();
            try {
                json.put("Nombre", edtTitulo.getText().toString());
                json.put("Descripcion", edtDesc.getText().toString());
                json.put("Anio", anioSeleccionado);
                json.put("IdMateria", materiaSeleccionada.getId());
                json.put("IdUsuario", Usuarios.getId());
                json.put("Vendido", chkVendido.isChecked());
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

    private class traerMaterias extends AsyncTask<String, Integer, List<MateriaEvento>> {
        public OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(List<MateriaEvento> list) {
            super.onPostExecute(list);
            adapterMaterias = new ArrayAdapter<MateriaEvento>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, materias);
            adapterMaterias.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            spnMateria.setAdapter(adapterMaterias);
            progressDialog.dismiss();
        }
        @Override
        protected List<MateriaEvento> doInBackground(String... params) {
            String url = params[0];
            try {
                int count = params.length;
                for (int i = 0; i < count; i++) {
                    publishProgress((int) ((i / (float) count) * 100));
                    // Escape early if cancel() is called
                    if (isCancelled()) break;
                }
                return enviarJSON(url);
            } catch (JSONException e) {
                Log.d("Error", e.getMessage());
                return null;
            }
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("Cargando...");
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
