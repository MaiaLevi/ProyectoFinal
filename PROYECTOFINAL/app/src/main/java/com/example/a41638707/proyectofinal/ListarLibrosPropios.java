package com.example.a41638707.proyectofinal;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListarLibrosPropios extends AppCompatActivity {
    public static final String PARAMETROLIBRO="com.example.a41638707.proyectofinal.PARAMETROLIBRO";
    ListView lstviewLibros;
    ArrayList<Libros> listaLibros, lstLibros;
    ArrayAdapter<Libros> adaptador=null;
    ImageView imgAgregar;
    ProgressDialog progressDialog;
    String url;
    TabHost tabs;
    EditText edtBuscar;
    View layoutPpal;
    LinearLayout layout;
    int idUsuario, contador=0, contadorBuscar=0;
    Libros libroSeleccionado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_libros_propios);
        listaLibros=new ArrayList<Libros>();
        lstLibros=new ArrayList<Libros>();
        ObtenerReferencias();
        idUsuario=Usuarios.getId();
        progressDialog=new ProgressDialog(this);
        url="http://apicampus.azurewebsites.net/listarLibrosPropios.php?IdUsuario=";
        url+=idUsuario;
        new listarLibros().execute(url);
        if (adaptador!=null)
        {
            adaptador.notifyDataSetChanged();
        }
        lstviewLibros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                libroSeleccionado= listaLibros.get(position);
                ActivityVerL();
            }
        });

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch(tabId)
                {
                    case ("usuario"):

                        Intent nuevaActivity=new Intent(ListarLibrosPropios.this,MainActivity.class);
                        startActivity(nuevaActivity);
                        chau();
                        break;
                    case ("eventos"):

                        Intent nuevaActivity2=new Intent(ListarLibrosPropios.this,Listar.class);
                        startActivity(nuevaActivity2);
                        chau();
                        break;
                    case ("horario"):
                        //todavia nada porque no esta la activity
                    break;

                }
            }
        });
        imgAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IniciarAgregarActividad();
            }
        });
        edtBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto=edtBuscar.getText().toString();
                if (texto.equals(""))
                {
                    layoutPpal.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);
                    //sacar el otro layout
                }
                else
                {
                    //PROBAR ESTA API ACA
                    layoutPpal.setVisibility(View.GONE);
                    url="http://apicampus.azurewebsites.net/buscarLibros.php?Busqueda=";
                    url+=texto;
                    url+="&id=";
                    url+=Usuarios.getId();
                    new buscarLibros().execute(url);
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    private void IniciarAgregarActividad()
    {
        Intent nuevaActivity=new Intent(ListarLibrosPropios.this,AgregarLibro.class);
        startActivity(nuevaActivity);
    }
    private void chau()
    {
        this.finish();
    }
    @Override
    public void onRestart(){
        super.onRestart();
        url="http://apicampus.azurewebsites.net/listarLibrosPropios.php?IdUsuario=";
        url+=idUsuario;
        new listarLibros().execute(url);
        if (adaptador!=null)
        {
            adaptador.notifyDataSetChanged();
        }
    }
    public void onClickWhatsApp(View view, String s) {

        PackageManager pm=getPackageManager();
        try {
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "Hola, deseo comprar tu libro llamado "+s;
            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");
            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed el libro es "+s, Toast.LENGTH_SHORT)
                    .show();
        }
    }
    private class buscarLibros extends AsyncTask<String, Void, ArrayList<Libros>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected ArrayList<Libros> doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al API
                return parsearLibros(response.body().string());      // Convierto el resultado en Evento

            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return null;
            }
        }
        @Override
        protected void onPreExecute() {
            //limpiar layout
            layout.removeAllViews();
            lstLibros.clear();
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.show();}
        @Override
        protected void onPostExecute(final ArrayList<Libros> lstaLibros) {
            super.onPostExecute(lstaLibros);
            progressDialog.dismiss();
            //adapter
            //for y recorrer cantidad de libros
            if (lstaLibros==null||lstaLibros.isEmpty())
            {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.setOrientation(LinearLayout.VERTICAL);
                //add textView
                TextView tvwNombre = new TextView(getApplicationContext());
                tvwNombre.setText("No se han encontrado resultados para la búsqueda");
                tvwNombre.setTextColor(Color.parseColor("#ff8080"));
                tvwNombre.setId(contador);
                tvwNombre.setLayoutParams(params);
                //add the textView to LinearLayout
                layout.addView(tvwNombre);
            }
            else
            {
                for (int i=0;i<lstaLibros.size();i++)
                {
                    final int numerito=i;
                    //para que tenga id unico
                    contador++;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    //add textView
                    TextView tvwNombre = new TextView(getApplicationContext());
                    tvwNombre.setText(lstaLibros.get(i).getNombre()+"\n"+lstaLibros.get(i).getDesc()+
                            "\n Año:"+lstaLibros.get(i).getAño()+
                            "\n Materia:"+lstaLibros.get(i).getMateria().getNombre()+
                            "\n Vendedor:"+lstaLibros.get(i).getUsuario());
                    tvwNombre.setTextColor(Color.parseColor("#000000"));
                    tvwNombre.setId(contador);
                    tvwNombre.setLayoutParams(params);
                    layout.addView(tvwNombre);
                    Button btnTag = new Button(getApplicationContext());
                    // btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    btnTag.setText("Comprar");
                    btnTag.setId(contador*2);
                    btnTag.setTag(lstaLibros.get(i).getId());
                    layout.addView(btnTag);
                    btnTag.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Log.i("TAG", "The index is" + contador);
                            //preguntar si tiene al contacto
                            Dialog dialogo=agregarContacto(v, lstaLibros.get(numerito));
                            dialogo.show();
                        }
                    });
                }
            }
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        ArrayList<Libros> parsearLibros (String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);
            JSONArray respJSON = json.getJSONArray("result");
            for (int i = 0; i < respJSON.length(); i++)
            {
                JSONObject obj = respJSON.getJSONObject(i);
                int idLibro = obj.getInt("IdLibro");
                String Nombre = obj.getString("Nombre");
                String Descr = obj.getString("Descripcion");
                int Año=obj.getInt("Anio");
                String Usuario = obj.getString("Usuario");
                int IdUsuario = obj.getInt("IdUsuario");
                int IdMateria = obj.getInt("IdMateria");
                int celular=obj.getInt("Celular");
                String Materia=obj.getString("Materia");
                MateriaEvento materia=new MateriaEvento(IdMateria,Materia);
                boolean Vendido = false;
                Libros unLibro = new Libros (idLibro,Nombre,Descr,IdUsuario,Usuario,Año,materia,Vendido, celular);
                lstLibros.add(unLibro);
            }
            return lstLibros;
        }
    }
    private Dialog agregarContacto(final View v, final Libros libro){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contacto");
        builder.setMessage("¿Tiene en sus contactos al vendedor del libro ("+libro.getUsuario()+")?");
        builder.setPositiveButton("Sí", new  DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Diálogos", "Confirmación Aceptada.");
                onClickWhatsApp(v, libro.getNombre());
                dialog.cancel();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Diálogos", "Confirmación Cancelada.");
                Usuarios miUsuario=new Usuarios("","","NombrePrueba",libro.getCelular());
                contacto(v, miUsuario, libro.getNombre());
                dialog.cancel();
            }
        });
        return builder.create();
    }
    private void contacto(View v, Usuarios usuarios, String s)
    {
        ArrayList < ContentProviderOperation > ops = new ArrayList < ContentProviderOperation > ();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());
        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        usuarios.getNombre()).build());
        ops.add(ContentProviderOperation.
                newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, usuarios.getTelefono())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
            Toast.makeText(getApplicationContext(), "Contacto agregado", Toast.LENGTH_SHORT).show();
            onClickWhatsApp(v,s);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        /*
        // Creates a new Intent to insert a contact
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
// Sets the MIME type to match the Contacts Provider
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, usuarios.getTelefono());
        intent.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK);
        startActivity(intent);

        ContentValues values = new ContentValues();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, 001);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, usuarios.getTelefono());
        values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
        Uri dataUri = getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, values);*/
    }
    private void ActivityVerL()
    {
        Intent nuevaActivity = new Intent(this, VerLibro.class);
        //crear libro y meterlo en intent
        nuevaActivity.putExtra(ListarLibrosPropios.PARAMETROLIBRO,libroSeleccionado);
        startActivity(nuevaActivity);
    }
    private void ObtenerReferencias()
    {
        lstviewLibros=(ListView)findViewById(R.id.lstLibros);
        imgAgregar=(ImageView)findViewById(R.id.imgAgregar);
        edtBuscar=(EditText)findViewById(R.id.edtBusqueda);
        layoutPpal=findViewById(R.id.layoutPrincipal);
        layout=(LinearLayout)findViewById(R.id.linealLayout);//SETEO TABS
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
        tabs.setCurrentTab(2);
    }
    private class listarLibros extends AsyncTask<String, Void, ArrayList<Libros>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected ArrayList<Libros> doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    //error aca
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();  // Llamado al API
                return parsearLibros(response.body().string());      // Convierto el resultado en Evento

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
            listaLibros.clear();
        }
        @Override
        protected void onPostExecute(ArrayList<Libros> lista) {
            super.onPostExecute(lista);
            progressDialog.dismiss();
            //adapter
            ArrayAdapter<Libros> Adaptador=new ArrayAdapter<Libros>(ListarLibrosPropios.this, android.R.layout.simple_list_item_1, lista);
            lstviewLibros.setAdapter(Adaptador);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        ArrayList<Libros> parsearLibros (String JSONstring) throws JSONException {
            JSONObject json = new JSONObject(JSONstring);
            JSONArray respJSON = json.getJSONArray("result");
            for (int i = 0; i < respJSON.length(); i++)
            {
                JSONObject obj = respJSON.getJSONObject(i);
                int idLibro = obj.getInt("IdLibro");
                String Nombre = obj.getString("Nombre");
                String Descr = obj.getString("Descripcion");
                int IdMateria = obj.getInt("IdMateria");
                String materia= obj.getString("NombreMat");
                int IdUsuario = obj.getInt("IdUsuario");
                String Usuario = obj.getString("Usuario");
                int Año=obj.getInt("Anio");
                int Vendido = obj.getInt("Vendido");
                boolean blnVend;
                if (Vendido==0)
                {
                    //se deberia mostrar, de lo contrario no
                    blnVend=false;
                }
                else
                {
                    blnVend=true;
                }
                MateriaEvento materiaEv=new MateriaEvento(IdMateria,materia);
                Libros unLibro = new Libros (idLibro,Nombre,Descr,IdUsuario,Usuario,Año,materiaEv,blnVend,0);
                listaLibros.add(unLibro);
            }
            return listaLibros;
        }
    }
}
