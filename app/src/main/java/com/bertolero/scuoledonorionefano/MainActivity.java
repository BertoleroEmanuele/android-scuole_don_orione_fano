package com.bertolero.scuoledonorionefano;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bertolero.scuoledonorionefano.calendario.CalendarioActivity;
import com.bertolero.scuoledonorionefano.circolari.CircolariActivity;
import com.bertolero.scuoledonorionefano.contatti.ActivityContatti;
import com.bertolero.scuoledonorionefano.notizie.NotizieActivity;
import com.bertolero.scuoledonorionefano.orientamento.OrientamentoActivity;
import com.bertolero.scuoledonorionefano.video.VideoActivity;

public class MainActivity extends AppCompatActivity {

    GridLayout mainGrid;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        scrollView = findViewById(R.id.scrollView);

        //Set Event
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);
    }

    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                        Toast.makeText(MainActivity.this, "State : True", Toast.LENGTH_SHORT).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        Toast.makeText(MainActivity.this, "State : False", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Class selezionata = null;
                    switch(finalI) {
                        case 0:
                            selezionata = NotizieActivity.class;
                            break;
                        case 1:
                            selezionata = CalendarioActivity.class;
                            break;
                        case 2:
                            selezionata = CircolariActivity.class;
                            break;
                        case 3:
                            selezionata = OrientamentoActivity.class;
                            break;
                        case 4:
                            selezionata = VideoActivity.class;
                            break;
                        case 5:
                            selezionata = ActivityContatti.class;
                            break;
                    }
                    Intent intent = new Intent(MainActivity.this, selezionata);
                    startActivity(intent);


                }
            });
        }
    }
}
