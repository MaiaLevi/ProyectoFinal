package com.example.a41638707.proyectofinal;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Registrarme extends AppCompatActivity {
    String url;
    EditText edtNombre, edtApellido, edtMail, edtContra, edtCel, edtDivi;
    CalendarView miCalen;
    Button btnListo;
    Calendar calen;
    Button btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarme);
        ObtRef();
        url="http://apicampus.azurewebsites.net/agregarUsuario.php";
        final String mail = edtMail.getText().toString();
        miCalen.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calen = new GregorianCalendar(year, month, dayOfMonth);
            }//met
        });
        btnListo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (edtNombre.getText().toString()!="")
                {
                    if (edtApellido.getText().toString()!="")
                    {
                        if (mail!="")
                        {
                            if (edtContra.getText().toString()!="")
                            {
                                if (edtCel.getText().toString().length()<10 || edtCel.getText().toString()!="")
                                {
                                    if (edtDivi.getText().toString()!="")
                                    {
                                        new agregarUsuario().execute(url);
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Completar división", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Completar celular", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Completar contraseña", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Completar email", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Completar apellido", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Completar nombre", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Atras();
            }
        });

    }
    public void ObtRef()
    {
        edtNombre= (EditText) findViewById(R.id.edtNombre);
        edtApellido= (EditText) findViewById(R.id.edtApellido);
        edtMail= (EditText) findViewById(R.id.edtMail);
        edtContra= (EditText) findViewById(R.id.edtContra);
        edtCel= (EditText) findViewById(R.id.edtNombre);
        miCalen=(CalendarView)findViewById(R.id.calendarView);
        btnListo=(Button) findViewById(R.id.btnListo);
        edtDivi=(EditText) findViewById(R.id.edtDivision);
        btnCancel=(Button) findViewById(R.id.btnCancelar);
    }
    private void Atras()
    {
        this.finish();
    }

    private void ActivityMain()
    {
        Intent nuevaActivity = new Intent(this, MainActivity.class);
        startActivity(nuevaActivity);
    }

    private class agregarUsuario extends AsyncTask<String, Void, Void> {
        public OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "YA ESTÁ REGISTRADO, INICIE SESIÓN", Toast.LENGTH_LONG).show();
            ActivityMain();
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
                json.put("nombre", edtNombre.getText());
                json.put("apellido", edtApellido.getText());
                json.put("mail", edtMail.getText());
                json.put("contrasena",contrasenaE());
                SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String reportDate = df.format(calen.getTime());
                json.put("fechanacimiento", reportDate);
                json.put("celular", edtCel.getText());
                json.put("iddivision", edtDivi.getText());
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
    public String contrasenaE()
    {
        MessageDigest crypt = null;
        try {
            crypt = java.security.MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 not supported");
        }
        byte[] digested = crypt.digest(edtContra.getText().toString().getBytes());
        String crypt_password = new String();
        // Converts bytes to string
        for (byte b : digested)
            crypt_password += Integer.toHexString(0xFF & b);
        return crypt_password;
    }
}