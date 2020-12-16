package com.bertolero.scuoledonorionefano.calendario;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bertolero.scuoledonorionefano.R;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class CalendarioActivity extends AppCompatActivity {

    private EventAdapter adapterEventi;
    private ListView listViewEventi;
    private RequestQueue requestQueue;
    private Context context;
    private ProgressDialog pd;
    private final String API_KEY = "AIzaSyClyZAOrJV2BC9IEot9PhJghugcjRMGQik";
    private final String CALENDAR_ID = "itifano.it_0mesem75cl60da492bvdatm6no@group.calendar.google.com";
    private ArrayList<Evento> listaEventiFuturi = new ArrayList<>();
    private ArrayList<Evento> listaEventiPassati = new ArrayList<Evento>();

    CalendarioActivity.LoadTask caricaCalendarioEventi = new CalendarioActivity.LoadTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario_activity);
        context = this;
        this.setTitle("Calendario Eventi");

        BottomAppBar bar = findViewById(R.id.bottomCalendario);
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarioActivity.this, ArchivioEventi.class);
                intent.putExtra("list", listaEventiPassati);
                CalendarioActivity.this.startActivity(intent);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fabAggiornaEventi);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd = new ProgressDialog(context);
                pd.setTitle("Aggiornamento");
                pd.setMessage("Aggiornamento eventi in corso...");
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                pd.show();
                caricaCalendarioEventi.doInBackground();
            }
        });

        requestQueue = newRequestQueue(this);

        listViewEventi = findViewById(R.id.list_view_eventi);
        adapterEventi = new EventAdapter(this);
        listViewEventi.setAdapter(adapterEventi);
        listViewEventi.setDivider(null);
        listViewEventi.setDividerHeight(0);
        caricaCalendarioEventi.execute();


    }

    public static Date parseDateTime(String dateString) {
        if (dateString == null) return null;
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        if (dateString.contains("T")) dateString = dateString.replace('T', ' ');
        else
            dateString = dateString.substring(0, dateString.lastIndexOf(':')) + dateString.substring(dateString.lastIndexOf(':')+1);
        try {
            return fmt.parse(dateString);
        }
        catch (ParseException e) {
            Log.e("LOG", "Could not parse datetime: " + dateString);
            return null;
        }
    }

    public static Date parseDate(String dateString) {
        if (dateString == null) return null;
        Log.i("LOG", "date: " + dateString);
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return fmt.parse(dateString);
        }
        catch (ParseException e) {
            Log.e("LOG", "Could not parse datetime: " + dateString);
            return null;
        }
    }

    class LoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(context);
            pd.setTitle("Calendario");
            pd.setMessage("Caricamento eventi in corso...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://www.googleapis.com/calendar/v3/calendars/" + CALENDAR_ID + "/events?key=" + API_KEY, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray jsonArray = response.getJSONArray("items");
                                listaEventiFuturi.clear();
                                listaEventiPassati.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    Evento temp =new Evento();
                                    JSONObject evento = jsonArray.getJSONObject(i);

                                    if (evento.getString("status").equals("confirmed")) {
                                        String nome = evento.getString("summary");
                                        temp.setNome(nome);

                                        if(!(evento.isNull("location"))) {
                                            String posizione = evento.getString("location");
                                            temp.setPosizione(posizione);
                                        }

                                        JSONObject inizio = evento.getJSONObject("start");
                                        if (!inizio.isNull("dateTime")) {
                                            temp.setInizio(parseDateTime(inizio.getString("dateTime")));
                                        } else if (!inizio.isNull("date")) {
                                            temp.setInizio(parseDate(inizio.getString("date")));
                                        }

                                        JSONObject fine = evento.getJSONObject("end");
                                        if (!fine.isNull("dateTime")) {
                                            temp.setFine(parseDateTime(fine.getString("dateTime")));
                                            if(temp.getInizio().getDay() == temp.getFine().getDay() &&
                                                    temp.getInizio().getMonth() == temp.getFine().getMonth() &&
                                                    temp.getInizio().getYear() == temp.getFine().getYear()){
                                                temp.setType(0);
                                            }
                                            else{
                                                temp.setType(-1);
                                            }
                                        } else if (!fine.isNull("date")) {
                                            Calendar c = Calendar.getInstance();
                                            c.setTime(parseDate(fine.getString("date")));
                                            c.add(Calendar.DAY_OF_MONTH, -1);
                                            temp.setFine(c.getTime());

                                            if(temp.getInizio().getDay() == temp.getFine().getDay() &&
                                                    temp.getInizio().getMonth() == temp.getFine().getMonth() &&
                                                    temp.getInizio().getYear() == temp.getFine().getYear()){
                                                temp.setType(1);
                                            }
                                            else{
                                                temp.setType(2);
                                            }
                                        }

                                        Calendar oggi = Calendar.getInstance();
                                        oggi.set(Calendar.HOUR_OF_DAY, 0);
                                        oggi.set(Calendar.MINUTE, 0);
                                        oggi.set(Calendar.SECOND, 0);
                                        oggi.set(Calendar.MILLISECOND, 0);

                                        if(oggi.getTime().before(temp.getInizio()))
                                            listaEventiFuturi.add(temp);
                                        else
                                            listaEventiPassati.add(temp);
                                    }
                                }

                                adapterEventi.clear();
                                Collections.sort(listaEventiFuturi);
                                adapterEventi.addAll(listaEventiFuturi);
                                adapterEventi.notifyDataSetChanged();
                                if (pd != null)
                                    pd.dismiss();
                            } catch (JSONException e) {
                                    e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (pd != null)
                        pd.dismiss();
                    AlertDialog alertDialog = new AlertDialog.Builder(CalendarioActivity.this).create();
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

            requestQueue.add(request);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
    }
}