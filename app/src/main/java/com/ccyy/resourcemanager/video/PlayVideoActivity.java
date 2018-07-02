package com.ccyy.resourcemanager.video;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;
import com.ccyy.resourcemanager.R;

/**
 * 视频播放界面
 */
public class PlayVideoActivity extends AppCompatActivity{
    private ActionBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        Intent intent=getIntent();
        String videoName=intent.getStringExtra("fileName"); //获取视频名称
        String videoPath=intent.getStringExtra("filePath");  //获取视频地址
        getSupportActionBar().setTitle(videoName); //获取视频的标题
        VideoView videoView=(VideoView) findViewById(R.id.video);
        videoView.setVideoPath(videoPath);
        videoView.setMediaController(new MediaController(this));
        videoView.start();

        bar=getSupportActionBar();
        bar.hide(); //去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //去掉信息栏
        /**
         * @param mediaPlayer
         * 对视频播放结束的监听
         * 播放结束退出自动退出
         */
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                finish(); //播放完成，自动关闭退出
            }
        });
        /**对标题栏显隐的监听
         * @param mediaPlayer
         */
        findViewById(R.id.hidden).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bar.isShowing()){
                    bar.hide(); //隐藏标题栏
                }
                else{
                    bar.show(); //显示标题栏
                }
            }
        });
    }
}
