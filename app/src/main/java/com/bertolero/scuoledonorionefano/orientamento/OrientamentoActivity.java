package com.bertolero.scuoledonorionefano.orientamento;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bertolero.scuoledonorionefano.MainActivity;
import com.bertolero.scuoledonorionefano.R;

public class OrientamentoActivity extends AppCompatActivity {

    private WebView webView;
    private final String URL_FORM = "https://orientamento.donorionefano.edu.it";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientamento_activity);
        this.setTitle("Spazio Orientamento");
        dialog = new ProgressDialog(this);

        webView = findViewById(R.id.webviewOrientamento);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        dialog.setMessage("Caricamento dello spazio orientamento...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        webView.loadUrl(URL_FORM);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }
}
