package com.example.cj;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

public class Video_Dialog extends AppCompatActivity {

    VideoView video;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.video_dialog);

        video = (VideoView) findViewById(R.id.video);
        Uri uri = Uri.parse(getIntent().getStringExtra("url"));
        video.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);

        video.setMediaController(mediaController);
        mediaController.setAnchorView(video);
        video.start();

    }
}