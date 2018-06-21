package com.ccyy.resourcemanager.text;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccyy.resourcemanager.R;
import com.ccyy.resourcemanager.main.FileData;
import com.ccyy.resourcemanager.main.FileTools;

import java.util.ArrayList;

/**
 * Created by Sweven on 2018/6/20.
 * Email:sweventears@Foxmail.com
 */
public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder> {

    private LayoutInflater inflater;
    public ArrayList<FileData> textList;
    public Context context;

    public TextAdapter(Context context,ArrayList<FileData> data) {
        inflater=LayoutInflater.from(context);
        this.textList=data;
        this.context=context;
    }

    @NonNull
    @Override
    public TextAdapter.TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_single_file, parent, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextAdapter.TextViewHolder holder, int position) {
        String name = textList.get(position).getName();
        String path = textList.get(position).getPath();
        Bitmap bitmap = textList.get(position).getFileIcon();
        long size = textList.get(position).getSize();

        holder.file_name.setText(name);
        holder.file_img.setImageBitmap(bitmap);

        holder.file_sign.setText(FileTools.getFileSize(size));
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView file_img;
        private TextView file_name;
        private TextView file_sign;
        private CheckBox file_check;
        private LinearLayout file_item;

        public TextViewHolder(View itemView) {
            super(itemView);
            file_img = itemView.findViewById(R.id.file_img);
            file_name = itemView.findViewById(R.id.file_name);
            file_sign=itemView.findViewById(R.id.file_sign);
            file_check = itemView.findViewById(R.id.file_check);
            file_item = itemView.findViewById(R.id.file_item);

            file_check.setVisibility(View.INVISIBLE);
            file_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String name=textList.get(position).getName();
            String path=textList.get(position).getPath();
            switch (v.getId()){
                case R.id.file_item: {
                    Intent intent=new Intent(context,EditTextActivity.class);
                    intent.putExtra("File_Name",name);
                    intent.putExtra("File_Path",path);
                    context.startActivity(intent);
                    break;
                }
            }
        }
    }
}
