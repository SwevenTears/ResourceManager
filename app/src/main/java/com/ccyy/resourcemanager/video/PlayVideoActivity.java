package com.ccyy.resourcemanager.video;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ccyy.resourcemanager.R;
import com.ccyy.resourcemanager.tools.T;


public class PlayVideoActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        Intent intent=getIntent();
        String videoName=intent.getStringExtra("fileName");
        String videoPath=intent.getStringExtra("filePath");
        getSupportActionBar().setTitle(videoName);
        VideoView videoView=(VideoView) findViewById(R.id.video);
        videoView.setVideoPath(videoPath);
        videoView.setMediaController(new MediaController(this));
        videoView.start();
//        this.requestWindowFeature(Window.f);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //videoView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                finish();
            }
        });


    }

}
