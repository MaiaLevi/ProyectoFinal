package com.example.a41638707.proyectofinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Agregar extends AppCompatActivity {
    Button btnCancelar,btnGuardar;
    EditText edtDescr;
    Spinner spnMaterias, spnTipos;
    List<TipoEvento> tipos;
    ArrayAdapter<TipoEvento> adapterTipos;
    ArrayAdapter<MateriaEvento> adapterMaterias;
    List<MateriaEvento> materias;
    TipoEvento tipoSeleccionado;
    MateriaEvento materiaSeleccionada;
    ProgressDialog  progressDialog;
    CalendarView calendar;
    Calendar calen;
    String url ="http://apicampus.azurewebsites.net/agregarevento.php";
    String url2="http://apicampus.azurewebsites.net/listarTipoEvento.php";
    String url3="http://apicampus.azurewebsites.net/listarMateriaEvento.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //api pida iddivision
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        ObtenerReferencias();
        progressDialog=new ProgressDialog(this);
        // traerTipos();
        //traerMaterias();
        new traerTipos().execute(url2);
        new traerMaterias().execute(url3);
        //CharSequence s  = DateFormat.getDateInstance().format("dd/mm/yyyy ");
        //Log.d("aca",s.toString());
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                irAtras();
            }});
        spnMaterias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                materiaSeleccionada = materias.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calen = new GregorianCalendar(year, month, dayOfMonth);
            }//met
        });
        spnTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoSeleccionado = tipos.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (spnMaterias.getSelectedItem().toString()!="") {
                    if (spnTipos.getSelectedItem().toString()!="") {
                            new agregarEvento().execute(url);
                            Toast msg = Toast.makeText(getApplicationContext(), "Evento guardado", Toast.LENGTH_LONG);
                            msg.show();
                            irAtras();
                    }
                    else {
                        Toast msg = Toast.makeText(getApplicationContext(), "Seleccione un tipo", Toast.LENGTH_LONG);
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
    private void ObtenerReferencias()
    {
        btnCancelar=(Button) findViewById(R.id.btnCancelar);
        btnGuardar=(Button)findViewById(R.id.btnGuardar);
        spnMaterias=(Spinner)findViewById(R.id.spnMaterias);
        spnTipos=(Spinner)findViewById(R.id.spnTipos);
        calendar=(CalendarView)findViewById(R.id.calendario);
        edtDescr=(EditText)findViewById(R.id.edtDescr);
    }
    private void irAtras()
    {
        this.finish();
    }
    private class agregarEvento extends AsyncTask<String, Integer, Void> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setMessage("Cargando...");
        }
        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];
            try {
                enviarJSON(url);
                int count = params.length;
                for (int i = 0; i < count; i++) {
                    publishProgress((int) ((i / (float) count) * 100));
                    // Escape early if cancel() is called
                    if (isCancelled()) break;
                }

            } catch (JSONException e) {
                Log.d("Error", e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }
        void enviarJSON(String url) throws JSONException {

            JSONObject json = new JSONObject();
            try {
                json.put("IdTipo", tipoSeleccionado.getId());
                json.put("IdMateria", materiaSeleccionada.getId());
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String reportDate = df.format(calen.getTime());
                json.put("Fecha", reportDate);
                json.put("Descripcion", edtDescr.getText().toString());
                json.put("IdUsuario", Usuarios.getId());
                //PROBAR SI LO DE ABAJO ANDA
                json.put("iddivision",Usuarios.getDivision().getId());
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
    private class traerTipos extends AsyncTask<String, Integer, List<TipoEvento>> {
        public OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(List<TipoEvento> list) {
            super.onPostExecute(list);
            adapterTipos = new ArrayAdapter<TipoEvento>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, tipos);
            adapterTipos.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            spnTipos.setAdapter(adapterTipos);
            progressDialog.dismiss();
        }
        @Override
        protected List<TipoEvento> doInBackground(String... params) {
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
        List<TipoEvento>  enviarJSON(String url) throws JSONException {
            int id=0;
            String nombre="";
            TipoEvento miTipo;
            OkHttpClient httpClient = new  OkHttpClient();
            HttpGet getRequest = new HttpGet(url);
            getRequest.setHeader("content-type", "application/json");
            tipos = new ArrayList<TipoEvento>();
            try {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response resp = client.newCall(request).execute();
                JSONObject json = new JSONObject(resp.body().string());                 // Convierto el String recibido a JSONObject
                JSONArray respJSON = json.getJSONArray("result");
                for(int i=0; i<respJSON.length(); i++) {
                    JSONObject obj = respJSON.getJSONObject(i);
                    id = obj.getInt("IdTipo");
                    nombre = obj.getString("Nombre");
                    miTipo=new TipoEvento(id,nombre);
                    tipos.add(miTipo);
                }
            }
            catch(Exception ex) {
                Log.e("ServicioRest","Error!", ex);
            }
            return tipos;
        }
    }
    private class traerMaterias extends AsyncTask<String, Integer, List<MateriaEvento>> {
        public OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(List<MateriaEvento> list) {
            super.onPostExecute(list);
            progressDialog.dismiss();
            adapterMaterias = new ArrayAdapter<MateriaEvento>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, materias);
            adapterMaterias.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            spnMaterias.setAdapter(adapterMaterias);
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
