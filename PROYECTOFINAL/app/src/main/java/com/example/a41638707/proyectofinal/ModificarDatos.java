package com.example.a41638707.proyectofinal;

import android.content.Intent;
import android.net.ParseException;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ModificarDatos extends AppCompatActivity {

    EditText edtNombre, edtApellido, edtMail, edtCelular;
    CalendarView miCalen;
    Calendar calen;
    Boolean calendarioToco=false;
    Usuarios Miusu;
    Button btnGuardar, btnCancelar;
    Date convertedDate;
    ArrayList<Integer> divi = new ArrayList<Integer>();
    int diviselec=1, iddivision=1;
    Spinner spnDivi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_datos);
        ObtenerRef();
        divi.add(1);
        divi.add(2);
        divi.add(3);
        divi.add(4);
        ArrayAdapter<Integer> adaptador = new ArrayAdapter<Integer>(
                getApplicationContext(),
                android.R.layout.simple_spinner_item,
                divi);
        adaptador.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spnDivi.setAdapter(adaptador);
        String url="http://apicampus.azurewebsites.net/traerUsuario.php?idusuario=";
        int id=Usuarios.getId();
        url=url+id;
        new traerUsuario().execute(url);
        miCalen.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calen = new GregorianCalendar(year, month, dayOfMonth);
                calendarioToco=true;
            }//met
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(calendarioToco) {
                    String url = "http://apicampus.azurewebsites.net/modificarUsuario.php";
                    new modificarUsuario().execute(url);
                    ActivityPrincipal();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Seleccione una fecha", Toast.LENGTH_SHORT).show();
                }
            }
        });
        spnDivi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                diviselec = divi.get(i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Atras();
            }
        });
    }
    private void ActivityPrincipal(){
        Intent nuevaActivity = new Intent(this, MainActivity.class);
        startActivity(nuevaActivity);
    }
    @Override
    public void onBackPressed() {
    }
    public void ObtenerRef()
    {
        miCalen=(CalendarView)findViewById(R.id.nacimiento);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtApellido = (EditText) findViewById(R.id.edtApellido);
        edtMail = (EditText) findViewById(R.id.edtMail);
        edtCelular = (EditText) findViewById(R.id.edtCelular);
        btnGuardar=(Button) findViewById(R.id.btnGuardar);
        spnDivi=(Spinner) findViewById(R.id.spnDivi);
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
                dato.put("celular", edtCelular.getText().toString());
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String reportDate = df.format(calen.getTime());
                dato.put("fechanacimiento", reportDate);
                dato.put("iddivision", diviselec);
                dato.put("idusuario", Miusu.getId());
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
            for (int i = 0; i < divi.size(); i++) {
                if (divi.get(i) == iddivision){
                    spnDivi.setSelection(i);
                }
            }
            edtNombre.setText(Miusu.getNombre());
            edtApellido.setText(Miusu.getApellido());
            edtCelular.setText(String.valueOf(Miusu.getTelefono()));
            edtMail.setText(Miusu.getMail());
            Calendar cal = Calendar.getInstance();
            cal.setTime(convertedDate);
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
            miCalen.setDate(milliTime, false, false);
        }
        Usuarios parsearUsu(String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);                 // Convierto el String recibido a JSONObject
            int idusuario = json.getInt("idusuario");
            String nombre = json.getString("nombre");
            String apellido = json.getString("apellido");
            String mail = json.getString("mail");
            String contrasena=json.getString("contrasena");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(json.getString("fechanacimiento"));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            int celular = json.getInt("celular");
            iddivision = json.getInt("iddivision");
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
