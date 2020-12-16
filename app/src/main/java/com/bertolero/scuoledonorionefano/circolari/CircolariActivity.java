package com.bertolero.scuoledonorionefano.circolari;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bertolero.scuoledonorionefano.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class CircolariActivity extends AppCompatActivity {

    private ListView listView;
    private RequestQueue requestQueue;
    private AdapterCircolare adapter;
    private Context context;
    private ProgressDialog pd;

    final private String url = "https://nuvola.madisoft.it/feed/PSTF005006/rss/7";

    LoadTask caricaCircolari = new LoadTask();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circolari_activity);
        context = this;
        this.setTitle("Circolari");
        FloatingActionButton fab = findViewById(R.id.fabAggiornaCircolari);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caricaCircolari.doInBackground();
                Snackbar.make(view, "Aggiornamento dati...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        requestQueue = newRequestQueue(this);

        listView = findViewById(R.id.list_view_circolari);

        adapter = new AdapterCircolare(this);
        listView.setAdapter(adapter);
        listView.setDivider(null);
        listView.setDividerHeight(0);

        askPermission();

        caricaCircolari.execute();


    }

    private void askPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1000);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                else{
                    DialogInterface.OnClickListener dialogPermessi = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    askPermission();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Non consentendo il permesso a salvare file nel tuo telefono sarà impossibile scaricare le circolari, ma solo visualizzarle all'interno dell'app.").setPositiveButton("Darò il permesso", dialogPermessi)
                            .setNegativeButton("Ok, non mi importa", dialogPermessi).show();
                }
        }
    }

    class LoadTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setTitle("Circolari");
        pd.setMessage("Caricamento in corso...");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {


        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            XmlPullParserFactory parserFactory = XmlPullParserFactory.newInstance();
                            XmlPullParser parser = parserFactory.newPullParser();
                            parser.setInput(new ByteArrayInputStream(response.getBytes()), null);
                            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                            ArrayList<Circolare> circolareArrayList = new ArrayList<>();
                            int eventType = parser.getEventType();

                            Circolare circolare = null;
                            boolean letturaItem = false;

                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                String valName = null;
                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;
                                    case XmlPullParser.START_TAG:
                                        valName = parser.getName();

                                        if (valName.equals("item")) {
                                            circolare = new Circolare();
                                            letturaItem = true;
                                        } else if (valName.equals("title") && letturaItem == true) {
                                            circolare.setTitolo((parser.nextText()));
                                        } else if (valName.equals("link") && letturaItem == true) {
                                            circolare.setLinkNuvola(parser.nextText());
                                            circolareArrayList.add(circolare);
                                            letturaItem = false;
                                        }
                                        break;
                                }
                                eventType = parser.next();
                            }
                            adapter.clear();
                            adapter.addAll(circolareArrayList);
                            if (pd != null)
                                pd.dismiss();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (pd != null)
                    pd.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(CircolariActivity.this).create();
                alertDialog.setTitle("Errore nello scaricamento dati");

                alertDialog.setMessage(error.getMessage());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

            }
        }
        );
        requestQueue.add(stringRequest);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        adapter.notifyDataSetChanged();
        super.onPostExecute(result);
    }
}

}