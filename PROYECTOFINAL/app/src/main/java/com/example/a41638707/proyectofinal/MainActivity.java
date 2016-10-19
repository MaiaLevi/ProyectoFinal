package com.example.a41638707.proyectofinal;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
/*
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;*/

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    // public static final ArrayList<Evento> PARAMETRO1=new ArrayList<Evento>();
    public ProgressDialog progressDialog;
    Button btnIniciarSesion, btnLogout;
    EditText edtMail, edtContra;
    ArrayList<Evento> listaEventos;
    Usuarios miUsuario;
    Division miDivision;
    View layoutBotones, layoutLogin;
    TextView tvwBienvenido;
    Boolean sesion, blnMail;
    CheckBox chkMail;
    Animation in,out;
    String nombre, mail, division;
    TextSwitcher mSwitcher, mSwitcher2,mSwitcher3, mSwitcher4;
    String texto3[]={"No hay eventos","No hay eventos"};
    String texto2[]={"No hay eventos","No hay eventos"};
    String texto4[]={"No hay eventos","No hay eventos"};
    String texto1[]={"No hay eventos","No hay eventos"};
    int iddivision, id, messageCount=0, currentIndex, messageCount4=0, currentIndex4, messageCount3=0, currentIndex3, messageCount2=0, currentIndex2;
    TabHost tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ObtenerReferencias();
        progressDialog = new ProgressDialog(this);
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        sesion = prefs.getBoolean("sesion", false);
        nombre = prefs.getString("nombre", "");
        mail = prefs.getString("email", "");
        id = prefs.getInt("id", 0);
        Log.i("sesion", sesion.toString());
        //PROBAR ABAJO
        iddivision = prefs.getInt("iddivision", 0);
        division = prefs.getString("division", "");
        miDivision = new Division(iddivision, division);
        miUsuario = new Usuarios(nombre, mail, "", 0);
        String urlEvento="http://apicampus.azurewebsites.net/listarEventosHome.php?IdUsuario="+id;
        Log.i("sesion", String.valueOf(id));
        new traerEventos().execute(urlEvento);
        //CARGAR VALORES EN CLASE USUARIO
        if (sesion) {
            tvwBienvenido.setText("Bienvenido/a " + nombre);
            layoutLogin.setVisibility(View.GONE);
            layoutBotones.setVisibility(View.VISIBLE);
            Usuarios.setDivision(miDivision);
            Usuarios.setId(id);
        } else {
            if (!miUsuario.getMail().equals("")) {
                blnMail = prefs.getBoolean("mail", false);
                if (blnMail) {
                    //Mostrar miUsuario.getMail();
                }
            }
        }
        // Declare the in and out animations and initialize them
        in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        out = AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (sesion) {
                    switch (tabId) {
                        case ("eventos"):
                            Intent nuevaActivity = new Intent(getApplicationContext(), Listar.class);
                            startActivity(nuevaActivity);
                            chau();
                            break;
                        case ("libros"):
                            Intent nuevaActivity2 = new Intent(getApplicationContext(), ListarLibrosPropios.class);
                            startActivity(nuevaActivity2);
                            chau();
                            break;
                        case ("horario"): {
                            Intent nuevaActivity3 = new Intent(getApplicationContext(), ListarHorario.class);
                            startActivity(nuevaActivity3);
                            chau();
                        }
                        break;
                    }
                } else {
                    if (!tabId.equals("usuario")) {
                        Toast.makeText(getApplicationContext(), "Para acceder debe iniciar sesión", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        /*Timer timer = new Timer("desired_name");
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    public void run() {
                        //switch your text using either runOnUiThread() or sending alarm and receiving it in your gui thread
                    }
                }, 0, 2000);*/
        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Sacar layout que dice bienvenido y volver a mostrar el iniciar sesion
                //En shared preferernces cambiar config
                Dialog dialog = confirmarLogout();
                dialog.show();
            }
        });
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String url = "http://apicampus.azurewebsites.net/login.php?Mail=";
                url += edtMail.getText().toString();
                if (!edtMail.getText().toString().equals("") && !edtMail.getText().toString().equals(" ")) {
                    if (!edtContra.getText().toString().equals("") && !edtContra.getText().toString().equals(" ")) {
                        new traerUsuario().execute(url);
                    } else {
                        Toast.makeText(getApplicationContext(), "La contraseña no puede ser vacía", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "El mail no puede ser vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSwitcher.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stubcurrentIndex++;
                // If index reaches maximum reset it
                currentIndex++;
                if(currentIndex==messageCount)
                    currentIndex=0;
                mSwitcher.setText(texto1[currentIndex]);
            }
        });
        // Set the ViewFactory of the TextSwitcher that will create TextView object when asked

        // set the animation type of textSwitcher
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);
        mSwitcher2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stubcurrentIndex++;
                // If index reaches maximum reset it
                currentIndex2++;
                if(currentIndex2==messageCount2)
                    currentIndex2=0;
                mSwitcher2.setText(texto2[currentIndex2]);
            }
        });
        // set the animation type of textSwitcher
        mSwitcher2.setInAnimation(in);
        mSwitcher2.setOutAnimation(out);
        mSwitcher3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stubcurrentIndex++;
                // If index reaches maximum reset it
                currentIndex3++;
                if(currentIndex3==messageCount3)
                    currentIndex3=0;
                mSwitcher3.setText(texto3[currentIndex3]);
            }
        });
        // set the animation type of textSwitcher
        mSwitcher3.setInAnimation(in);
        mSwitcher3.setOutAnimation(out);
        mSwitcher4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stubcurrentIndex++;
                // If index reaches maximum reset it
                currentIndex4++;
                if(currentIndex4==messageCount4)
                    currentIndex4=0;
                mSwitcher4.setText(texto4[currentIndex4]);
            }
        });
        // Set the ViewFactory of the TextSwitcher that will create TextView object when asked

        mSwitcher4.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
                TextView myText = new TextView(MainActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(25);
                //myText.setTextColor(Color.BLUE);
                return myText;
            }
        });
        mSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
                TextView myText = new TextView(MainActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(25);
                //myText.setTextColor(Color.BLUE);
                return myText;
            }
        });

        // Set the ViewFactory of the TextSwitcher that will create TextView object when asked
        mSwitcher2.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
                TextView myText = new TextView(MainActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(25);
                //myText.setTextColor(Color.BLUE);
                return myText;
            }
        });
        // Set the ViewFactory of the TextSwitcher that will create TextView object when asked
        mSwitcher3.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
                TextView myText = new TextView(MainActivity.this);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(25);
                //myText.setTextColor(Color.BLUE);
                return myText;
            }
        });
        // set the animation type of textSwitcher
        mSwitcher4.setInAnimation(in);
        mSwitcher4.setOutAnimation(out);
    }
    //CAMBIAR POR TAB
        /*navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        boolean blnMenu=false;
                        switch (menuItem.getItemId()) {
                            case R.id.Eventos:
                                IniciarListarActividad();
                                blnMenu=true;
                                break;
                            case R.id.Libros:
                                blnMenu=true;
                                IniciarListarLActividad();
                                break;
                            case R.id.Horario:
                                blnMenu=true;
                                break;
                        }
                        if (blnMenu){
                            menuItem.setChecked(true);
                            getSupportActionBar().setTitle(menuItem.getTitle());
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }*/
    private void chau()
    {
        this.finish();
    }
    private Dialog confirmarLogout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert Dialog");
        builder.setMessage("¿Está seguro que desea cerrar sesión?");
        builder.setPositiveButton("Cerrar sesión", new  DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Diálogos", "Confirmación Aceptada.");
                sesion=false;
                SharedPreferences prefs =
                        getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("sesion", sesion);
                editor.commit();
                layoutLogin.setVisibility(View.VISIBLE);
                layoutBotones.setVisibility(View.GONE);
                dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Diálogos", "Confirmación Cancelada.");
                dialog.cancel();
            }
        });
        return builder.create();
    }
    public String iniciarSesion()
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
    private void ObtenerReferencias()
    {
        btnIniciarSesion=(Button)findViewById(R.id.btnIniciarSesion);
        edtMail=(EditText)findViewById(R.id.edtMail);
        edtContra=(EditText)findViewById(R.id.edtContrasena);
        tvwBienvenido=(TextView)findViewById(R.id.tvwBienvenido);
        layoutBotones=findViewById(R.id.botones);
        layoutLogin=findViewById(R.id.login);
        btnLogout=(Button)findViewById(R.id.btnLogout);
        chkMail=(CheckBox)findViewById(R.id.chkMail);
        mSwitcher=(TextSwitcher)findViewById(R.id.textSwitcher2);
        mSwitcher2=(TextSwitcher)findViewById(R.id.textSwitcher3);
        mSwitcher3=(TextSwitcher)findViewById(R.id.textSwitcher4);
        mSwitcher4=(TextSwitcher)findViewById(R.id.textSwitcher5);
        //SETEO TABS
        Resources res = getResources();
        tabs=(TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec=tabs.newTabSpec("usuario");
        spec.setContent(R.id.tab1);
        spec.setIndicator("",
                res.getDrawable(R.drawable.ic_person_black_24dp1));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("eventos");
        spec.setContent(R.id.tab2);
        spec.setIndicator("",
                res.getDrawable(R.drawable.ic_assignment_black_24dp));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("libros");
        spec.setContent(R.id.tab3);
        spec.setIndicator("",
                res.getDrawable(R.drawable.ic_local_library_black_24dp));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("horario");
        spec.setContent(R.id.tab4);
        spec.setIndicator("",
                res.getDrawable(R.drawable.ic_alarm_black_24dp));
        tabs.addTab(spec);
        tabs.setCurrentTab(0);
    }
    private class traerUsuario extends AsyncTask<String, Void, Usuarios> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected Usuarios doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al API
                return parsearUsuario(response.body().string());      // Convierto el resultado en Evento

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
        protected void onPostExecute(Usuarios usu) {
            super.onPostExecute(usu);
            progressDialog.dismiss();
            if (miUsuario!=null)
            {
                if (miUsuario.getContra().equals(iniciarSesion()))
                {
                    tvwBienvenido.setText("Bienvenido/a "+miUsuario.getNombre());
                    layoutLogin.setVisibility(View.GONE);
                    layoutBotones.setVisibility(View.VISIBLE);
                    sesion=true;
                    SharedPreferences prefs =
                            getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("email", miUsuario.getMail());
                    editor.putString("nombre", miUsuario.getNombre());
                    editor.putInt("iddivision", Usuarios.getDivision().getId());
                    editor.putString("division", Usuarios.getDivision().getNombre());
                    editor.putInt("id", Usuarios.getId());
                    editor.putBoolean("sesion", sesion);
                    if (chkMail.isChecked())
                    {
                        editor.putBoolean("mail", true);
                    }
                    editor.commit();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Usuario incorrecto", Toast.LENGTH_SHORT).show();
            }
        }
        Usuarios parsearUsuario (String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);
            int id=json.getInt("IdUsuario");
            String nombre=json.getString("Nombre");
            String contrasena = json.getString("Contrasena");
            int divi=json.getInt("IdDivision");
            String division = json.getString("Division");
            Division miDivi=new Division(divi,division);
            miUsuario=new Usuarios(nombre,edtMail.getText().toString(),contrasena,0);
            Usuarios.setId(id);
            Usuarios.setDivision(miDivi);
            return miUsuario;
        }
    }
    private class traerEventos extends AsyncTask<String, Void, ArrayList<Evento>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected ArrayList<Evento> doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al API
                return parsearEventos(response.body().string());      // Convierto el resultado en Evento

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
        protected void onPostExecute(ArrayList<Evento> evento) {
            super.onPostExecute(evento);
            progressDialog.dismiss();
            if (listaEventos.size()>0)
            {
                //recorrer lista obtener fehca materia y tipo
                for (int i=0; i<listaEventos.size(); i++)
                {
                    DateFormat df = new SimpleDateFormat("dd/MM");
                    String reportDate="";
                    switch (i)
                    {
                        case 0:
                            texto1[0]=listaEventos.get(i).getTipo().getNombre()+" de "+listaEventos.get(i).getMateria().getNombre();
                            reportDate = df.format(listaEventos.get(i).getFecha());
                            texto1[1]=reportDate;
                            currentIndex=-1;
                            break;
                        case 1:
                            //i=1
                            texto2[0]=listaEventos.get(i).getTipo().getNombre()+" de "+listaEventos.get(i).getMateria().getNombre();
                            reportDate = df.format(listaEventos.get(i).getFecha());
                            texto2[1]=reportDate;
                            currentIndex2=-1;
                            break;
                        case 2:
                            //i=2
                            texto3[0]=listaEventos.get(i).getTipo().getNombre()+" de "+listaEventos.get(i).getMateria().getNombre();
                            reportDate = df.format(listaEventos.get(i).getFecha());
                            texto3[1]=reportDate;
                            currentIndex3=-1;
                            break;
                        case 3:
                            //i=3
                            texto4[0]=listaEventos.get(i).getTipo().getNombre()+" de "+listaEventos.get(i).getMateria().getNombre();
                            reportDate = df.format(listaEventos.get(i).getFecha());
                            texto4[1]=reportDate;
                            currentIndex4=-1;
                    }
                }
            }
            messageCount3=texto3.length;
            messageCount2=texto2.length;
            messageCount4=texto4.length;
            messageCount=texto1.length;
        }
        ArrayList<Evento> parsearEventos(String JSONstring) throws JSONException {
             listaEventos=new ArrayList<>();
            JSONObject json = new JSONObject(JSONstring);
            JSONArray respJSON = json.getJSONArray("result");
            for (int i = 0; i < respJSON.length(); i++)
            {
                JSONObject obj = respJSON.getJSONObject(i);
                int idEvento = obj.getInt("Id");SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date convertedDate = new Date();
                try {
                    convertedDate = dateFormat.parse(obj.getString("Fecha"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String descEvento = obj.getString("Descripcion");
                int IdMateriaEvento = obj.getInt("IdMateria");
                String MateriaEvento = obj.getString("Materia");
                int IdTipo=obj.getInt("IdTipo");
                String tipoEvento = obj.getString("Tipo");
                TipoEvento tipo=new TipoEvento(IdTipo,tipoEvento);
                MateriaEvento materia=new MateriaEvento(IdMateriaEvento,MateriaEvento);
                Evento unEvento =new Evento(idEvento,materia,tipo,convertedDate, descEvento,Usuarios.getId(), null);
                listaEventos.add(unEvento);
            }
            return listaEventos;
        }
    }
}

