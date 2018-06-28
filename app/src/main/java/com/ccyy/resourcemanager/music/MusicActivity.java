package com.ccyy.resourcemanager.music;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ccyy.resourcemanager.R;

import java.util.ArrayList;
import java.util.List;

public class MusicActivity extends AppCompatActivity {

    private ArrayList<String> musicList;
    private ArrayList<String> nameList;
    private RecyclerView mRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list);

        mRecycler = findViewById(R.id.music_recycler);

        musicList = new ArrayList();
        nameList = new ArrayList();
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE
        };

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        while (cursor.moveToNext()) {
            musicList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            nameList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
        }

         mRecycler.addItemDecoration(new DividerItemDecoration(this, 1));
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
//        MusicAdapter mAdapter = new MusicAdapter(this, musicList, nameList);
//        mRecycler.setAdapter(mAdapter);

    }
}
