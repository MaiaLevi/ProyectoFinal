package com.example.a41638707.proyectofinal;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

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

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    public boolean refresh = false;
    String url="";
    ArrayList<Evento> lista = new ArrayList<Evento>();
    public MyIntentService() {
        super("MyIntentService");
    }
    @Override
    protected void onHandleIntent(final Intent workIntent) {
//devolver lista de eventos
        SinEstoNoFunca();
        OkHttpClient client = new OkHttpClient();
        url ="http://apicampus.azurewebsites.net/notificaciones.php?IdDivision=";
        url+=Usuarios.getDivision().getId();
        JSONObject json = new JSONObject();
        try {
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            String JSONstr = response.body().string();
            ParsearResultado(JSONstr);
        } catch (IOException e) {
            //e.printStackTrace();
            Log.e("IOIOIOIO", "ERROR", e);
        }
        if(lista.size() > 0)
        {
            Notification n  = null;
            Intent intent = new Intent(getApplicationContext(), Listar.class);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.ic_assignment_black_24dp);
                //hacer if con tamao lista, si hay mas de uno hacer for
                PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), intent, 0);
                if (lista.size()==1)
                {
                    Evento miEvento=new Evento(lista.get(0).getId(),lista.get(0).getMateria(),lista.get(0).getTipo(), null, "",0,null);
                    n = new Notification.Builder(getApplicationContext())
                            .setContentTitle(miEvento.getTipo().getNombre()+" de "+miEvento.getMateria().getNombre()+" en una semana")
                            //si hay mas de una cosa que no especifique
                            .setContentText("Ver eventos")
                            .setSmallIcon(R.drawable.ic_assignment_black_24dp)
                            .setContentIntent(pIntent)
                            .setAutoCancel(true)
                            .setLargeIcon(icon)
                            .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                            //.addAction(R.drawable.logoproyecto, "Call", pIntent)
                            .build();
                }
                else
                {
                    String principio="";
                    for (int i=0;i<lista.size();i++)
                    {
                        if (i==0)
                        {
                            principio="En una semana tenés: ";
                        }
                        else
                        {
                            principio=" ,";
                        }
                        Evento miEvento=new Evento(lista.get(i).getId(),lista.get(i).getMateria(),lista.get(i).getTipo(), null, "",0,null);
                        n = new Notification.Builder(getApplicationContext())
                                .setContentTitle(principio+ miEvento.getTipo().getNombre()+" de "+miEvento.getMateria().getNombre())
                                //si hay mas de una cosa que no especifique
                                .setContentText("Ver eventos")
                                .setSmallIcon(R.drawable.ic_assignment_black_24dp)
                                .setContentIntent(pIntent)
                                .setAutoCancel(true)
                                .setLargeIcon(icon)
                                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                                //.addAction(R.drawable.logoproyecto, "Call", pIntent)
                                .build();
                    }
                }

            }
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, n);
        }
    }
    private void SinEstoNoFunca()
    {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    private void ParsearResultado(String JSONstr)
    {
        Evento devolver;
        try {
            JSONObject json = new JSONObject(JSONstr);
            JSONArray respJSON = json.getJSONArray("result");
            for (int i = 0; i < respJSON.length(); i++)
            {
                JSONObject obj = respJSON.getJSONObject(i);
                int idEvento= obj.getInt("Id");
                int idMateria = obj.getInt("IdMateria");
                //fecha al fadi y descripcion tambien
                String materia=obj.getString("Materia");
                int idTipo=obj.getInt("IdTipo");
                String tipo=obj.getString("Tipo");
                MateriaEvento miMat=new MateriaEvento(idMateria,materia);
                TipoEvento miTipo=new TipoEvento(idTipo,tipo);
                devolver = new Evento(idEvento, miMat,miTipo,null,"",0,null);
                lista.add(devolver);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
