package com.bertolero.scuoledonorionefano.contatti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.bertolero.scuoledonorionefano.R;
import com.google.android.material.snackbar.Snackbar;

public class ActivityContatti extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contatti_activity);
        this.setTitle("Contatti");

        ImageButton btnTelefono = (ImageButton) findViewById(R.id.contatti_telefono);
        btnTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:0721801462"));
                startActivity(callIntent);
            }
        });

        ImageButton btnMail = (ImageButton) findViewById(R.id.contatti_mail);
        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","info@donorionefano.it", null));
                startActivity(emailIntent);
            }
        });
        ImageButton btnSito = (ImageButton) findViewById(R.id.contatti_sito);
        btnSito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse("https://donorionefano.edu.it"));
                startActivity(webIntent);
            }
        });

        ImageButton btnWa = (ImageButton) findViewById(R.id.contatti_whatsapp);
        btnWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Uri uri = Uri.parse("smsto:" + "3510397113");
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(i, ""));
            }
        });

        ImageButton btnFb = (ImageButton) findViewById(R.id.contatti_facebook);
        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                try {
                    getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/484712705044492")));
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/itifano")));
                }
            }
        });

        ImageButton btnIg = (ImageButton) findViewById(R.id.contatti_instagram);
        btnIg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                try {
                    if (getPackageManager().getPackageInfo("com.instagram.android", 0) != null) {
                        intent.setData(Uri.parse("http://instagram.com/_u/" + "scuole_don_orione_fano"));
                        intent.setPackage("com.instagram.android");
                    }
                    else{
                        intent.setData(Uri.parse("https://www.instagram.com/scuole_don_orione_fano/"));
                    }

                    startActivity(intent);
                } catch (PackageManager.NameNotFoundException ignored) {
                }


            }
        });

        ImageButton btnYt = (ImageButton) findViewById(R.id.contatti_youtube);
        btnYt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCC7x1e8b55r_Vaejc_1nNzw")));

            }
        });

        ImageButton btnMeccanograficoITI = (ImageButton) findViewById(R.id.contatti_mecc_iti);
        btnMeccanograficoITI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Codice meccanografico", "PSTF005006");
                clipboard.setPrimaryClip(clip);
                Snackbar.make(view, "Codice meccanografico copiato", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

        ImageButton btnMeccanograficoCFP = (ImageButton) findViewById(R.id.contatti_mecc_cfp);
        btnMeccanograficoCFP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Codice meccanografico", "PSRI005009");
                clipboard.setPrimaryClip(clip);
                Snackbar.make(view, "Codice meccanografico copiato", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

        ImageButton btnCodFiscale = (ImageButton) findViewById(R.id.contatti_codice_fiscale);
        btnCodFiscale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Codice fiscale", "01277870414");
                clipboard.setPrimaryClip(clip);
                Snackbar.make(view, "Codice fiscale copiato", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

        ImageButton btnCodUfficio = (ImageButton) findViewById(R.id.contatti_codice_ufficio);
        btnCodUfficio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Codice ufficio", "J6URRTW");
                clipboard.setPrimaryClip(clip);
                Snackbar.make(view, "Codice ufficio copiato", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });





    }
}
