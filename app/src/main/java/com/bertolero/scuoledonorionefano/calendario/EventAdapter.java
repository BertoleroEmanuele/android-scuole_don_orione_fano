package com.bertolero.scuoledonorionefano.calendario;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bertolero.scuoledonorionefano.R;
import com.google.android.material.chip.Chip;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EventAdapter extends ArrayAdapter<Evento> {
    Context chiamante;
    public EventAdapter(Context context) {
        super(context, 0);
        this.chiamante = context;
    }

    @Override
    public void addAll(Evento... items) {
        super.addAll(items);
        Log.i("LOG", "Aggiunto array");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Evento event = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.evento_item, parent, false);
        }

        TextView tvTitolo = convertView.findViewById(R.id.nomeEvento);
        tvTitolo.setText(event.getNome());

        DateFormat giornoPerEsteso = DateFormat.getDateInstance(DateFormat.FULL, Locale.ITALY);
        SimpleDateFormat orario = (SimpleDateFormat)SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        orario.applyPattern("HH:mm");

        SimpleDateFormat giorno = (SimpleDateFormat)SimpleDateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);
        giorno.applyPattern("EEEE");

        TextView tvGiorni = convertView.findViewById(R.id.giorniInizioFine);
        TextView tvInizio = convertView.findViewById(R.id.oraInizio);
        TextView tvFine = convertView.findViewById(R.id.oraFine);
        TextView tvPosizione = convertView.findViewById(R.id.posizioneEvento);
        LinearLayout layoutInizio = convertView.findViewById(R.id.compInizio);
        LinearLayout layoutFine = convertView.findViewById(R.id.compFine);
        LinearLayout layoutPosizione = convertView.findViewById(R.id.compPosizione);
        Chip chip = convertView.findViewById(R.id.chipOggi);

        switch(event.getType()) {
            case -1:
                tvGiorni.setText("Da "+ giornoPerEsteso.format(event.getInizio()) + " \na " + giornoPerEsteso.format(event.getFine()));
                tvInizio.setText("Dalle " + orario.format(event.getInizio()) + " (di " + giorno.format(event.getInizio())+")");
                tvFine.setText("Alle " + orario.format(event.getFine()) + " (di " + giorno.format(event.getFine())+")");
                break;
            case 0:
                tvGiorni.setText(giornoPerEsteso.format(event.getInizio()));
                tvInizio.setText("Dalle "+ orario.format(event.getInizio()));
                tvFine.setText("Alle "+ orario.format(event.getFine()));
                break;
            case 1:
                layoutInizio.setVisibility(View.GONE);
                layoutFine.setVisibility(View.GONE);
                tvGiorni.setText(giornoPerEsteso.format(event.getInizio()));
                break;
            case 2:
                layoutInizio.setVisibility(View.GONE);
                layoutFine.setVisibility(View.GONE);
                tvGiorni.setText("Da "+ giornoPerEsteso.format(event.getInizio()) + " \na " + giornoPerEsteso.format(event.getFine()));
                break;
        }

        if (event.getPosizione() == null)
            layoutPosizione.setVisibility(View.GONE);
        else
            tvPosizione.setText(event.getPosizione());

        Calendar now = Calendar.getInstance();
        Date oggi = now.getTime();

        if(oggi.getDay() == event.getInizio().getDay()
                && oggi.getMonth() == event.getInizio().getMonth()
                && oggi.getYear() == event.getInizio().getYear()) {
            chip.setVisibility(View.VISIBLE);
            chip.setText("Oggi");
        }
        else
            chip.setVisibility(View.GONE);

        return convertView;
    }

}