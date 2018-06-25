package com.ccyy.resourcemanager.music;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ccyy.resourcemanager.R;

import java.io.File;

public class PlayActivity extends AppCompatActivity {

    private Visualizer mVisualizer;
    private MediaPlayer myPlayer;
    private SeekBar myBar;
    private Button play_pauseButton;
    private TextView nowTime;
    private TextView endTime;
    private TimeThread timeTread;
    private Spectrogram spectrogram;
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String min, sec;
            if (msg.arg1 < 10)
                min = "0" + msg.arg1;
            else
                min = "" + msg.arg1;
            if (msg.arg2 < 10)
                sec = "0" + msg.arg2;
            else
                sec = "" + msg.arg2;
            nowTime.setText(min + ":" + sec);
        }
    };
    private TextView musicName;

    class TimeThread extends Thread {

        public void run() {
            while (myPlayer.isPlaying()) {
                Message msg = new Message();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                msg.arg1 = (int) myPlayer.getCurrentPosition() / 60000;
                msg.arg2 = (int) (myPlayer.getCurrentPosition() / 1000) % 60;
                h.sendMessage(msg);

            }
        }

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        musicName = (TextView) findViewById(R.id.musicName);
        myBar = (SeekBar) findViewById(R.id.progressBar);
        play_pauseButton = (Button) findViewById(R.id.play_paused);
        nowTime = (TextView) findViewById(R.id.nowTime);
        endTime = (TextView) findViewById(R.id.endTime);
        spectrogram = findViewById(R.id.spectrogram);
        myPlayer = new MediaPlayer();
        myPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        timeTread = new TimeThread();
        Log.d("Progress为", myBar.getProgress() + "");

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        String name=new File(path).getName();
        musicName.setText(name);

        getSupportActionBar().setTitle("音乐播放器");

        try {
            myPlayer.reset();
            myPlayer.setDataSource(path);
            myPlayer.prepare();
            myPlayer.start();
            timeTread.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("播放音乐","异常");
        }


        play_pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myPlayer.isPlaying()) {
                    myPlayer.pause();
                } else {
                    myPlayer.start();
                }

            }
        });

        nowTime.setText("00:00");
        endTime.setText("0" + (int) (myPlayer.getDuration() / 60000) + ":" + (myPlayer.getDuration() / 1000) % 60);

        myBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                this.progress = i;
                int m = (int) myPlayer.getDuration() * progress / 100;
                String min, sec;
                if ((int) (m / 60000) < 10)
                    min = "0" + (int) (m / 60000);
                else
                    min = "" + (int) (m / 60000);
                if ((m / 1000) % 60 < 10)
                    sec = "0" + (m / 1000) % 60;
                else
                    sec = "" + (m / 1000) % 60;
                nowTime.setText(min + ":" + sec);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                myPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myPlayer.start();
                int m = (int) myPlayer.getDuration() * progress / 100;
                myPlayer.seekTo(m);
            }
        });

        mVisualizer = new Visualizer(myPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(128);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {

                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        spectrogram.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] fft, int samplingRate) {

                        spectrogram.updateVisualizer(fft);
                    }
                }, Visualizer.getMaxCaptureRate() / 2, false, true);

        mVisualizer.setEnabled(true);
        new Equalizer(0, myPlayer.getAudioSessionId()).setEnabled(true);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            myPlayer.stop();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
