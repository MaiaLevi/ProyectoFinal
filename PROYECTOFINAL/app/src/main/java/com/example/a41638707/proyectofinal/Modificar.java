package com.example.a41638707.proyectofinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
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

public class Modificar extends AppCompatActivity {
    EditText edtDescr;
    Spinner spnMaterias, spnTipos;
    Button btnCancelar, btnGuardar;
    Evento MiEvento;
    TipoEvento eventoTipo, tipoSeleccionado;
    MateriaEvento eventoMateria, materiaSeleccionada;
    CalendarView calendario;
    int idEvento;
    Boolean calenToco=false;
    Calendar calen;
    public ProgressDialog progressDialog;
    ArrayList<MateriaEvento> materias = new ArrayList<MateriaEvento>();
    ArrayList<TipoEvento> tipos = new ArrayList<TipoEvento>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        Intent elIntent=getIntent();
        Bundle datos=elIntent.getExtras();
        idEvento=datos.getInt(Listar.PARAMETRO1);
        ObtenerReferencias();
        progressDialog=new ProgressDialog(this);
        String url="http://apicampus.azurewebsites.net/traerEvento.php?Evento=";
        url=url+idEvento;
        new traerEvento().execute(url);
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calen = new GregorianCalendar(year, month, dayOfMonth);
                calenToco=true;
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
                if (calenToco)
                {
                    String url = "http://apicampus.azurewebsites.net/modificarevento.php";
                    //se manda parametro? hace falta?
                    new modificarEvento().execute(url);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Seleccione una fecha", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void GuardarEvento() {
        Intent nuevaActivity = new Intent(Modificar.this, Listar.class);
        startActivity(nuevaActivity);
    }
    private void ObtenerReferencias() {
        calendario = (CalendarView) findViewById(R.id.calendario1);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        spnMaterias = (Spinner) findViewById(R.id.spnMaterias);
        spnTipos = (Spinner) findViewById(R.id.spnTipos);
        edtDescr = (EditText) findViewById(R.id.edtDescr);
    }
    @Override
    public void onBackPressed() {
    }
    private void irAtras() {
        this.finish();
    }
    private class modificarEvento extends AsyncTask<String, Void, Void> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Se ha guardado el evento", Toast.LENGTH_SHORT).show();
            GuardarEvento();
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
                dato.put("IdUsuario", Usuarios.getId());
                //NO SE GUARDA EL IDDIVISION
                //dato.put("iddivision",MainActivity.idDivision);
                dato.put("Id", idEvento);
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
            String url="http://apicampus.azurewebsites.net/listarMateriaEvento.php";
            new traerMaterias().execute(url);
            url="http://apicampus.azurewebsites.net/listarTipoEvento.php";
            new traerTipos().execute(url);
            Calendar cal = Calendar.getInstance();
            cal.setTime(MiEvento.getFecha());
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int year = cal.get(Calendar.YEAR);
            //seteo la fecha
            Calendar calendar = new GregorianCalendar();
            //Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, day);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.YEAR, year);
            long milliTime = calendar.getTimeInMillis();
            //hacer try catch porque si hay una fecha rara se rompe
            calendario.setDate(milliTime, false, false);
           // calendario.setMinDate(milliTime);
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
            int idDivi = json.getInt("IdDivision");
            String nombreDivi = json.getString("Division");
            eventoTipo = new TipoEvento(idTipo, tipo);
            eventoMateria = new MateriaEvento(idMateria, materia);
            Division miDivision=new Division(idDivi, nombreDivi);
            MiEvento = new Evento(id, eventoMateria, eventoTipo, convertedDate, descripcion, idUsuario, miDivision);
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
            adapterMaterias.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            spnMaterias.setAdapter(adapterMaterias);
            for (int i = 0; i < materias.size(); i++) {
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
            adapterTipos.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
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