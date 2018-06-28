package com.ccyy.resourcemanager.music;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ccyy.resourcemanager.R;

import java.io.File;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {

    private Visualizer mVisualizer;
    private MediaPlayer myPlayer;
    private SeekBar myBar;
    private Button play_pauseButton;
    private TextView nowTime;
    private TextView endTime;
    private TimeThread timeTread;
    private Spectrogram spectrogram;
    private FrameLayout music_frameLayout;
    private ArrayList<String> musicList;
    private ArrayList<String> nameList;
    private RecyclerView myRecycler;
    private ImageView musicImg;
    private Boolean isRcycler;
    private Intent intent;
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
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                myBar.setProgress((int) myPlayer.getCurrentPosition()*100/myPlayer.getDuration());
                msg.arg1 = (int) myPlayer.getCurrentPosition() / 60000;
                msg.arg2 = (int) (myPlayer.getCurrentPosition() / 1000) % 60;
                h.sendMessage(msg);

            }
        }

    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        music_frameLayout=findViewById(R.id.myFrame);
        myRecycler = findViewById(R.id.music_recycler);
        musicName = (TextView) findViewById(R.id.musicName);
        myBar = (SeekBar) findViewById(R.id.progressBar);
        play_pauseButton = (Button) findViewById(R.id.play_paused);
        nowTime = (TextView) findViewById(R.id.nowTime);
        endTime = (TextView) findViewById(R.id.endTime);
        spectrogram = findViewById(R.id.spectrogram);
        musicImg = findViewById(R.id.musicImg);
        isRcycler = true;
        myPlayer = new MediaPlayer();
        myPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        timeTread = new TimeThread();
        Log.d("Progress为", myBar.getProgress() + "");

        music_frameLayout.removeView(spectrogram);

        intent = getIntent();

        getSupportActionBar().setTitle("音乐播放器");

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE
        };

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        musicList = new ArrayList();
        nameList = new ArrayList();
        while (cursor.moveToNext()) {
            musicList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            nameList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
        }
        myRecycler = findViewById(R.id.mmusic_recycler);
        myRecycler.addItemDecoration(new DividerItemDecoration(this, 1));
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        MusicAdapter mAdapter = new MusicAdapter(this, musicList, musicList);
        myRecycler.setAdapter(mAdapter);

//        try {
//            myPlayer.reset();
//            myPlayer.setDataSource(path);
//            myPlayer.prepare();
//            myPlayer.start();
//            timeTread.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.i("播放音乐","异常");
//        }


//        play_pauseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (myPlayer.isPlaying()) {
//                    myPlayer.pause();
//                } else {
//                    myPlayer.start();
//                    timeTread.start();
//                }
//
//            }
//        });

        nowTime.setText("00:00");
        endTime.setText("00:00");

        String path = intent.getStringExtra("path");
        if(path != null){
            play(path);
        }
//        endTime.setText("0" + (int) (myPlayer.getDuration() / 60000) + ":" + (myPlayer.getDuration() / 1000) % 60);

//        myBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//
//            int progress;
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                this.progress = i;
//                int m = (int) myPlayer.getDuration() * progress / 100;
//                String min, sec;
//                if ((int) (m / 60000) < 10)
//                    min = "0" + (int) (m / 60000);
//                else
//                    min = "" + (int) (m / 60000);
//                if ((m / 1000) % 60 < 10)
//                    sec = "0" + (m / 1000) % 60;
//                else
//                    sec = "" + (m / 1000) % 60;
//                nowTime.setText(min + ":" + sec);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                myPlayer.pause();
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                myPlayer.start();
//                timeTread.start();
//                int m = (int) myPlayer.getDuration() * progress / 100;
//                myPlayer.seekTo(m);
//            }
//        });

//        mVisualizer = new Visualizer(myPlayer.getAudioSessionId());
//        mVisualizer.setCaptureSize(128);
//        mVisualizer.setDataCaptureListener(
//                new Visualizer.OnDataCaptureListener() {
//
//                    public void onWaveFormDataCapture(Visualizer visualizer,
//                                                      byte[] bytes, int samplingRate) {
//                        spectrogram.updateVisualizer(bytes);
//                    }
//
//                    public void onFftDataCapture(Visualizer visualizer,
//                                                 byte[] fft, int samplingRate) {
//
//                        spectrogram.updateVisualizer(fft);
//                    }
//                }, Visualizer.getMaxCaptureRate() / 2, false, true);
//
//        mVisualizer.setEnabled(true);
//        new Equalizer(0, myPlayer.getAudioSessionId()).setEnabled(true);
//
//        myPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                mVisualizer.setEnabled(false);
//                finish();
//            }
//        });


    }

    public void play(String path){
        String name=new File(path).getName();
        musicName.setText(name.replace(".mp3",""));
        try {
            myPlayer.reset();
            myPlayer.setDataSource(path);
            myPlayer.prepare();
            myPlayer.start();
            endTime.setText("0" + (int) (myPlayer.getDuration() / 60000) + ":" + (myPlayer.getDuration() / 1000) % 60);
            timeTread.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("播放音乐","异常");
        }
        play_pauseButton.setText("暂停");
        if(intent.getStringExtra("path") != null){
            music_frameLayout.removeView(myRecycler);
            music_frameLayout.addView(spectrogram);
            setVisualizer();
            setListener();
            isRcycler = false;

        }else {
            setVisualizer();
            setListener();
        }
    }

    public void setVisualizer(){
        mVisualizer = new Visualizer(myPlayer.getAudioSessionId());
        mVisualizer.setEnabled(false);
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

    public void setListener(){
        play_pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myPlayer.isPlaying()) {
                    myPlayer.pause();
                    play_pauseButton.setText("播放");
                } else {
                    myPlayer.start();
                    setVisualizer();
                    play_pauseButton.setText("暂停");
                    timeTread.start();
                }

            }
        });

        myBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress;
            boolean isPlaying;

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
                if(myPlayer.isPlaying())
                    isPlaying=true;
                else
                    isPlaying=false;
                myPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myPlayer.start();
                timeTread.start();
                play_pauseButton.setText("暂停");
                int m = (int) myPlayer.getDuration() * progress / 100;
                myPlayer.seekTo(m);

            }
        });
        myPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (intent.getStringExtra("path") != null){
                    finish();
                }else {
                    mVisualizer.setEnabled(false);
                    play_pauseButton.setText("播放");
                    if(!isRcycler){
                        music_frameLayout.removeView(spectrogram);
                        music_frameLayout.addView(myRecycler);
                    }
                    isRcycler=true;
                }
            }
        });
        musicImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRcycler){
                    music_frameLayout.removeView(myRecycler);
                    music_frameLayout.addView(spectrogram);
                    isRcycler=false;
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(intent.getStringExtra("path") != null){
            super.onBackPressed();
            myPlayer.stop();
        }else if(!isRcycler){
            music_frameLayout.removeView(spectrogram);
            music_frameLayout.addView(myRecycler);
            isRcycler=true;
        }else {
            super.onBackPressed();
            myPlayer.stop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        myPlayer.pause();
        play_pauseButton.setText("播放");
    }
}
