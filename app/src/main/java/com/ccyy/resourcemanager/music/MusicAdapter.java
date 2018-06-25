package com.ccyy.resourcemanager.music;

import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccyy.resourcemanager.R;

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
    private Context context;

    public MusicAdapter(Context context, ArrayList<String> music_list, ArrayList<String> name_list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
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
        holder.music_name.setText(name_list.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, PlayActivity.class);
                i.putExtra("path", music_list.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return music_list.size();
    }

    class MusicViewHolder extends RecyclerView.ViewHolder {
        private ImageView music_img;
        private TextView music_name;

        public MusicViewHolder(View itemView) {
            super(itemView);
            music_img = (ImageView) itemView.findViewById(R.id.music_img);
            music_name = (TextView) itemView.findViewById(R.id.music_name);
        }
    }
}
