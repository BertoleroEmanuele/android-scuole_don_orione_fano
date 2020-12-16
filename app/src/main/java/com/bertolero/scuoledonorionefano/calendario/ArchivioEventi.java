package com.bertolero.scuoledonorionefano.calendario;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.bertolero.scuoledonorionefano.R;

import java.util.ArrayList;
import java.util.Collections;


public class ArchivioEventi extends AppCompatActivity {

    private EventAdapter adapterEventi;
    private ListView listViewEventiPassati;
    private ArrayList<Evento> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.archivio_eventi);
        this.setTitle("Eventi Passati");

        list = getIntent().getParcelableArrayListExtra("list");
        Collections.reverse(list);

        listViewEventiPassati = findViewById(R.id.list_view_eventi_passati);
        adapterEventi = new EventAdapter(this);
        listViewEventiPassati.setAdapter(adapterEventi);
        listViewEventiPassati.setDivider(null);
        listViewEventiPassati.setDividerHeight(0);
        adapterEventi.clear();
        adapterEventi.addAll(list);
        adapterEventi.notifyDataSetChanged();

    }
}
