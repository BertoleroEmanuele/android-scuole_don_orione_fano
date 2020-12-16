package com.bertolero.scuoledonorionefano.notizie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ListView;

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
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class NotizieActivity extends AppCompatActivity {

    private ListView listView;
    private RequestQueue requestQueue;
    private AdapterNotizia adapter;
    private Context context;
    private ProgressDialog pd;

    final private String url = "https://www.iti.donorionefano.edu.it/feed/";

    LoadTask caricaNotizie = new LoadTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notizie_activity);
        context = this;
        this.setTitle("Notizie");

        FloatingActionButton fab = findViewById(R.id.fabAggiornaNotizie);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caricaNotizie.doInBackground();
                Snackbar.make(view, "Aggiornamento dati...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        requestQueue = newRequestQueue(this);

        listView = findViewById(R.id.list_view_notizie);

        adapter = new AdapterNotizia(this);
        listView.setAdapter(adapter);
        listView.setDivider(null);
        listView.setDividerHeight(0);

        caricaNotizie.execute();
    }

    class LoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle("Notizie");
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
                                parser.setInput(new ByteArrayInputStream(response.getBytes()), "UTF-8");
                                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

                                ArrayList<Notizia> newsArrayList = new ArrayList<>();
                                int eventType= parser.getEventType();

                                Notizia notizia = null;
                                boolean letturaNotizia = false;

                                while (eventType!= XmlPullParser.END_DOCUMENT)
                                {
                                    String valName = null;
                                    switch (eventType)
                                    {
                                        case XmlPullParser.START_DOCUMENT:
                                            break;
                                        case XmlPullParser.START_TAG:
                                            valName=parser.getName();

                                            if(valName.equals("item")) {
                                                notizia = new Notizia();
                                                letturaNotizia = true;
                                            }
                                            else if (valName.equals("title") && letturaNotizia==true) {
                                                notizia.setTitolo(parser.nextText());
                                            }
                                            else if (valName.equals("enclosure") && letturaNotizia==true) {
                                                notizia.setImage(parser.getAttributeValue(0));
                                            }
                                            else if (valName.equals("link") && letturaNotizia==true) {
                                                notizia.setLink(parser.nextText());
                                            }
                                            else if (valName.equals("description") && letturaNotizia==true) {
                                                notizia.setDescrizione((parser.nextText()));
                                                newsArrayList.add(notizia);
                                                letturaNotizia = false;
                                            }
                                            break;
                                    }
                                    eventType= parser.next();
                                }
                                adapter.clear();
                                adapter.addAll(newsArrayList);
                                if (pd != null)
                                    pd.dismiss();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pd != null)
                        pd.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(NotizieActivity.this).create();
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
