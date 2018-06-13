package com.ccyy.resourcemanager.music;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ccyy.resourcemanager.R;

public class MusicActivity extends AppCompatActivity {

    private MediaPlayer myPlayer;
    private TextView musicName;
    private SeekBar myBar;
    private Button play_pauseButton;
    private Boolean isPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
