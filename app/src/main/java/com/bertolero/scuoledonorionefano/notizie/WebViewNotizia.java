package com.bertolero.scuoledonorionefano.notizie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bertolero.scuoledonorionefano.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WebViewNotizia extends AppCompatActivity {

    private WebView webView;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notizia_web_view);

        final String indirizzo = getIntent().getStringExtra("link");
        final String tipologia = getIntent().getStringExtra("tipologia");

        this.setTitle("Leggi " + tipologia);
        dialog = new ProgressDialog(this);

        webView = findViewById(R.id.webview);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setMessage("Caricamento " + tipologia + "...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        webView.loadUrl(indirizzo);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, indirizzo);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        });
    }

    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }

}

