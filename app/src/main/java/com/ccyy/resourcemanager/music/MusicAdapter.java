package com.ccyy.resourcemanager.music;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccyy.resourcemanager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sweven on 2018/6/21.
 * Email:sweventears@Foxmail.com
 */
public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private LayoutInflater inflater;
    public ArrayList<String> music_list;
    private ArrayList<String> name_list;
    private PlayActivity activity;

    public MusicAdapter(PlayActivity activity, ArrayList<String> music_list, ArrayList<String> name_list) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.music_list = music_list;
        this.name_list = name_list;
    }

    @NonNull
    @Override
    public MusicAdapter.MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.music_item, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MusicViewHolder holder, final int position) {
        holder.music_name.setText(new File(name_list.get(position)).getName().replace(".mp3",""));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.play(music_list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return music_list.size();
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {
        private TextView music_name;

        public MusicViewHolder(View itemView) {
            super(itemView);
            music_name = (TextView) itemView.findViewById(R.id.music_name);
        }
    }
}
