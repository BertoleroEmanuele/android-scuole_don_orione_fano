package com.bertolero.scuoledonorionefano.circolari;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bertolero.scuoledonorionefano.R;
import com.bertolero.scuoledonorionefano.notizie.WebViewNotizia;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class AdapterCircolare extends ArrayAdapter<Circolare> {

    Context chiamante;

    public AdapterCircolare(Context context) {
        super(context, 0);
        this.chiamante = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Circolare circ = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.circolare_item, parent, false);
        }

        TextView textViewTitolo = convertView.findViewById(R.id.titoloCircolare);
        textViewTitolo.setText(Html.fromHtml(circ.getTitolo()));
        Button apriSuNuvola = convertView.findViewById(R.id.apriPaginaCircolare);
        apriSuNuvola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(chiamante, WebViewNotizia.class);
                intent.putExtra("link", circ.getLinkNuvola());
                intent.putExtra("tipologia", "circolare");
                chiamante.startActivity(intent);

            }
        });

        Button visualizzaPDF = convertView.findViewById(R.id.visualizzaCircolare);
        visualizzaPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    Document doc = null;
                    doc = Jsoup.connect(circ.getLinkNuvola()).get();
                    String linkDoc = "https://nuvola.madisoft.it/" + doc.select("a").get(3).attr("href");

                    Intent intent = new Intent(chiamante, CircolareReader.class);
                    intent.putExtra("link", linkDoc);
                    chiamante.startActivity(intent);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        Button scaricaPDF = convertView.findViewById(R.id.scaricaCircolare);
        scaricaPDF.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view)
            {
                try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                Document doc = null;
                doc = Jsoup.connect(circ.getLinkNuvola()).get();
                String linkDoc = "https://nuvola.madisoft.it/" + doc.select("a").get(3).attr("href");
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(linkDoc));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                request.setTitle(circ.getTitolo());

                request.setDescription("Downloading PDF...");

                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, circ.getTitolo() + ".pdf");
                request.setMimeType("application/pdf");

                DownloadManager manager = (DownloadManager)chiamante.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return convertView;
    }

}