package com.bertolero.scuoledonorionefano.notizie;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bertolero.scuoledonorionefano.R;
import com.squareup.picasso.Picasso;

public class AdapterNotizia extends ArrayAdapter<Notizia> {
    Context chiamante;
    public AdapterNotizia(Context context) {
        super(context, 0);
        this.chiamante = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Notizia news = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notizia_item, parent, false);
        }

        TextView textViewTitolo = convertView.findViewById(R.id.titoloNotizia);
        textViewTitolo.setText(news.getTitolo());
        TextView textViewDescrizione = convertView.findViewById(R.id.descrizioneNotizia);
        textViewDescrizione.setText(Html.fromHtml(news.getDescrizione()));
        Button apriNews = convertView.findViewById(R.id.visualizzaNotizia);
        apriNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent apri = new Intent(chiamante, WebViewNotizia.class);
                apri.putExtra("link", news.getLink());
                apri.putExtra("tipologia","notizia");
                chiamante.startActivity(apri);

            }
        });

        Button shareNews = convertView.findViewById(R.id.condividiNotizia);
        shareNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, news.getLink());
                share.setType("text/plain");

                Intent shareIntent = Intent.createChooser(share, null);
                chiamante.startActivity(shareIntent);

            }
        });

        ImageView imageViewNotizia = convertView.findViewById(R.id.immagineNotizia);

        if(!news.getImage().isEmpty())
            Picasso.get().load(news.getImage()).into(imageViewNotizia);


        return convertView;
    }

}