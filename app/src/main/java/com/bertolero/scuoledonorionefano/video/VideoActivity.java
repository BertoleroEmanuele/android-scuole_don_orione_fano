package com.bertolero.scuoledonorionefano.video;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bertolero.scuoledonorionefano.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        this.setTitle("Video");

        YouTubePlayerView youTubePlayerView = findViewById(R.id.videoInformatica);
        getLifecycle().addObserver(youTubePlayerView);
    }
}
