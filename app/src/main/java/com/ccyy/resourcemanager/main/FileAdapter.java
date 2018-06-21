package com.ccyy.resourcemanager.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccyy.resourcemanager.R;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by Sweven on 2018/6/13.
 * Email:sweventears@Foxmail.com
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private LayoutInflater inflater;
    public ArrayList<FileData> fileData;

    private onClickItem mClickItem = null;

    public interface onClickItem {
        void onClick(ArrayList<FileData> fileData, int previous_position);
    }

    public void setOnClickItem(onClickItem onClickFolder) {
        this.mClickItem = onClickFolder;
    }

    /**
     * @param context
     * @param data    file数据：name、 path
     */
    public FileAdapter(Context context, ArrayList<FileData> data) {
        inflater = LayoutInflater.from(context);
        this.fileData = data;

    }

    @NonNull
    @Override
    public FileAdapter.FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_single_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * 渲染数据
     *
     * @param holder
     * @param position item的位置信息
     */
    @Override
    public void onBindViewHolder(@NonNull final FileViewHolder holder,final int position) {
        String name = fileData.get(position).getName();
        String path = fileData.get(position).getPath();
        Bitmap bitmap = fileData.get(position).getFileIcon();
        boolean isCheck = fileData.get(position).isCheck();

        holder.file_name.setText(name);
        if (name.equals("<<previous>>"))
            holder.file_name.setText("返回上一级");
        holder.file_img.setImageBitmap(bitmap);

        Log.e("第" + position + "", isCheck + "");
        if (isCheck) {
            holder.file_check.setSelected(true);
        }

        String file_path=fileData.get(position).getPath();
        long file_size = fileData.get(position).getSize();
        File file=new File(file_path);
        if(file.isDirectory()){
            if(!holder.file_name.getText().equals("返回上一级")) {
                holder.file_sign.setTextSize(20);
                holder.file_sign.setText(">");
            }
        }
        else{
            holder.file_sign.setTextSize(12);
            holder.file_sign.setText(FileTools.getFileSize(file_size));
        }

        addItemClick(fileData, holder, position);

    }

    /**
     * @param fileData
     * @param holder
     * @param position
     */
    private void addItemClick(final ArrayList<FileData> fileData, FileViewHolder holder, final int position) {
        if (mClickItem != null) {
            holder.file_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickItem.onClick(fileData, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return fileData.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView file_img;
        private TextView file_name;
        private TextView file_sign;
        private CheckBox file_check;
        private LinearLayout file_item;

        public FileViewHolder(View itemView) {
            super(itemView);
            file_img = itemView.findViewById(R.id.file_img);
            file_name = itemView.findViewById(R.id.file_name);
            file_sign=itemView.findViewById(R.id.file_sign);
            file_check = itemView.findViewById(R.id.file_check);
            file_item = itemView.findViewById(R.id.file_item);

            file_check.setVisibility(View.INVISIBLE);
            file_check.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            boolean isCheck = fileData.get(position).isCheck();
            String name = fileData.get(position).getName();
            String path =fileData.get(position).getPath();

            switch (v.getId()){
                case R.id.file_check: {
                    if (!isCheck) {
                        fileData.get(position).setCheck(true);
                    } else {
                        fileData.get(position).setCheck(false);
                    }
                    break;
                }
            }
        }
    }
}
