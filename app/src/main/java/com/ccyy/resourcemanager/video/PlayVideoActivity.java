package com.ccyy.resourcemanager.video;


import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ccyy.resourcemanager.R;


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
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

}
