package com.example.a41638707.proyectofinal;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Modificar extends AppCompatActivity {
    EditText edtDescr;
    Spinner spnMaterias, spnTipos;
    Button btnCancelar, btnGuardar;
    Evento MiEvento;
    TipoEvento eventoTipo, tipoSeleccionado;
    MateriaEvento eventoMateria, materiaSeleccionada;
    CalendarView calendario;
    int idEvento;
    Calendar calen;
    public ProgressDialog progressDialog;
    //lo de abajo esta null
    ArrayList<MateriaEvento> materias = new ArrayList<MateriaEvento>();
    ArrayList<TipoEvento> tipos = new ArrayList<TipoEvento>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        Intent elIntent=getIntent();
        Bundle datos=elIntent.getExtras();
        //idEvento = 1;
        idEvento=datos.getInt(Listar.PARAMETRO1);
        ObtenerReferencias();
        progressDialog=new ProgressDialog(this);
        String url="http://daiuszw.hol.es/bd/traerEvento.php?Evento=";
        url=url+idEvento;
        new traerEvento().execute(url);
        //traerMaterias();
        //traerTipos();
        //url="http://daiuszw.hol.es/bd/listarMateriaEvento.php";
        //new traerMaterias().execute(url);
        //url="http://daiuszw.hol.es/bd/listarTipoEvento.php";
        //new traerTipos().execute(url);
        //mostrarEvento();
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calen = new GregorianCalendar(year, month, dayOfMonth);
            }//met
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
        spnTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoSeleccionado = tipos.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                irAtras();
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "http://daiuszw.hol.es/bd/modificarevento.php?IdUsuario=" + idEvento;
                new modificarEvento().execute(url);
                Toast.makeText(getApplicationContext(), "Se ha guardado el evento", Toast.LENGTH_SHORT).show();
                GuardarEvento();
                irAtras();
            }
        });
    }

    private void GuardarEvento() {
        Intent nuevaActivity = new Intent(Modificar.this, Listar.class);
        startActivity(nuevaActivity);
    }
    /*
    private void mostrarEvento() {
        //los spinners muestran informacion incorrecta
        //int position = 0;
        //fijarme posicion en adapter y comparar el id con el del evento
        for (int i = 0; i < materias.size(); i++) {
            //cuando ejecuto me aparece null pointer exception pero debuggeo y esta todo ok
            if (materias.get(i).getId() == MiEvento.getMateria().getId()){
                    spnMaterias.setSelection(i);
            }
        }
        //HACER ASYNTASK Y PONER ESTO EN EL ONPOST EXECUTE
        //si se llega a borrar alguna materia y hay por ej 3 y dsp 5 no va a andar
        //hay que recorrer y fijarse en que posicion del vector esta y guardarlo ahi y mostrar

        //spnMaterias.setSelection(2);
        //se muestra mal
        for (int i = 0; i < tipos.size(); i++) {
            if (tipos.get(i).getId() == MiEvento.getTipo().getId()){
                spnTipos.setSelection(i);
            }
        }
        //obtengo la fecha del evento
    }*/

    private void ObtenerReferencias() {
        calendario = (CalendarView) findViewById(R.id.calendario);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        spnMaterias = (Spinner) findViewById(R.id.spnMaterias);
        spnTipos = (Spinner) findViewById(R.id.spnTipos);
        edtDescr = (EditText) findViewById(R.id.edtDescr);
    }
/*
    private void traerTipos() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int id = 0;
        String nombre = "";
        TipoEvento miTipo;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet("http://daiuszw.hol.es/bd/listarTipoEvento.php");
        getRequest.setHeader("content-type", "application/json");
        try {
            HttpResponse resp = httpClient.execute(getRequest);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONArray respJSON = new JSONArray(respStr);
            for (int i = 0; i < respJSON.length(); i++) {
                JSONObject obj = respJSON.getJSONObject(i);
                id = obj.getInt("IdTipo");
                nombre = obj.getString("Nombre");
                miTipo = new TipoEvento(id, nombre);
                tipos.add(miTipo);
            }
            try {
                ArrayAdapter<TipoEvento> adapterTipos = new ArrayAdapter<TipoEvento>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, tipos);
                adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnTipos.setAdapter(adapterTipos);
            } catch (Exception ex) {
                Log.e("ErrorAdapter", "Error!", ex);
            }
            //spnTipos.setAdapter(adapterTipos);
        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
    }

    private void traerMaterias() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        int id = 0;
        String nombre = "";
        MateriaEvento miMateria;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet("http://daiuszw.hol.es/bd/listarMateriaEvento.php");
        getRequest.setHeader("content-type", "application/json");
        try {
            HttpResponse resp = httpClient.execute(getRequest);
            String respStr = EntityUtils.toString(resp.getEntity());
            JSONArray respJSON = new JSONArray(respStr);
            for (int i = 0; i < respJSON.length(); i++) {
                JSONObject obj = respJSON.getJSONObject(i);
                id = obj.getInt("IdMateria");
                nombre = obj.getString("Nombre");
                miMateria = new MateriaEvento(id, nombre);
                materias.add(miMateria);
            }
            ArrayAdapter<MateriaEvento> adapterMaterias = new ArrayAdapter<MateriaEvento>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, materias);
            adapterMaterias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnMaterias.setAdapter(adapterMaterias);
        } catch (Exception ex) {
            Log.e("ServicioRest", "Error!", ex);
        }
    }
*/
    private void irAtras() {
        this.finish();
    }

    private class modificarEvento extends AsyncTask<String, Void, Void> {
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

        // Convierte un JSON en un ArrayList de Direccion
        void enviarJSON(String url) throws JSONException {
            try {
                JSONObject dato = new JSONObject();
                dato.put("IdTipo", tipoSeleccionado.getId());
                dato.put("IdMateria", materiaSeleccionada.getId());
                // Create an instance of SimpleDateFormat used for formatting
                // the string representation of date (month/day/year)
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                // Get the date today using Calendar object.
                //Calendar cal = new GregorianCalendar();
                    /*Calendar cal = Calendar.getInstance();
                    int month=cal.get(Calendar.MONTH);
                    int year =cal.get(Calendar.YEAR);
                    int day=cal.get(Calendar.DAY_OF_MONTH);*/
                //si no anda .getDate
                // Using DateFormat format method we can create a string
                // representation of a date with the defined format.
                String reportDate = df.format(calen.getTime());
                //String reportDate = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
                dato.put("Fecha", reportDate);
                dato.put("Descripcion", (edtDescr.getText().toString()));
                dato.put("IdUsuario", 1);
                dato.put("Id", idEvento);
                //falta iddivision
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
    private class traerEvento extends AsyncTask<String, Void, Evento> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected Evento doInBackground(String... params) {
            String url = params[0];

            Request request = new Request.Builder()
                    //error aca
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al API
                return parsearEvento(response.body().string());      // Convierto el resultado en Evento

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
        protected void onPostExecute(Evento evento) {
            super.onPostExecute(evento);
            progressDialog.dismiss();
            String url="http://daiuszw.hol.es/bd/listarMateriaEvento.php";
            new traerMaterias().execute(url);
            url="http://daiuszw.hol.es/bd/listarTipoEvento.php";
            new traerTipos().execute(url);
            Calendar cal = Calendar.getInstance();
            cal.setTime(MiEvento.getFecha());
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int year = cal.get(Calendar.YEAR);
            //seteo la fecha
            Calendar calendar = new GregorianCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);
            long milliTime = calendar.getTimeInMillis();
            //hacer try catch porque si hay una fecha rara se rompe
            calendario.setDate(milliTime, true, true);
            edtDescr.setText(MiEvento.getDescripcion());
        }
        Evento parsearEvento(String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);                 // Convierto el String recibido a JSONObject
            int id = json.getInt("Id");
            //obtener fecha como string y convertirla
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            //probar sin null tambien
            Date convertedDate = null;
            String strFecha = json.getString("Fecha");
            //llega mal la fecha
            try {
                convertedDate = dateFormat.parse(strFecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String descripcion = json.getString("Descripcion");
            int idMateria = json.getInt("IdMateria");
            String materia = json.getString("Materia");
            int idTipo = json.getInt("IdTipo");
            String tipo = json.getString("Tipo");
            int idUsuario = json.getInt("IdUsuario");
            eventoTipo = new TipoEvento(idTipo, tipo);
            eventoMateria = new MateriaEvento(idMateria, materia);
            MiEvento = new Evento(id, eventoMateria, eventoTipo, convertedDate, descripcion, idUsuario);
            return MiEvento;
        }
    }
    private class traerMaterias extends AsyncTask<String, Void, ArrayList<MateriaEvento>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected ArrayList<MateriaEvento> doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    //error aca
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
                if (materias.get(i).getId() == MiEvento.getMateria().getId()){
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
    private class traerTipos extends AsyncTask<String, Void, ArrayList<TipoEvento>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected ArrayList<TipoEvento> doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    //error aca
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al API
                return parsearTipos(response.body().string());      // Convierto el resultado en Evento

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
        protected void onPostExecute(ArrayList<TipoEvento> listaTipos) {
            super.onPostExecute(listaTipos);
            ArrayAdapter<TipoEvento> adapterTipos = new ArrayAdapter<TipoEvento>(getApplicationContext(),
                    android.R.layout.simple_spinner_item, tipos);
            adapterTipos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnTipos.setAdapter(adapterTipos);
            progressDialog.dismiss();
            for (int i = 0; i < tipos.size(); i++) {
                if (tipos.get(i).getId() == MiEvento.getTipo().getId()){
                    spnTipos.setSelection(i);
                }
            }
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        ArrayList<TipoEvento> parsearTipos(String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);
            JSONArray respJSON = json.getJSONArray("result");
            for (int i=0;i<respJSON.length();i++)
            {
                JSONObject jsonResult=respJSON.getJSONObject(i);
                int id = jsonResult.getInt("IdTipo");
                String nombre = jsonResult.getString("Nombre");
                eventoTipo = new TipoEvento(id, nombre);
                tipos.add(eventoTipo);
            }
            return tipos;
        }
    }
}