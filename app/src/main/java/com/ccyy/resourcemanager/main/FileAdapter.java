package com.ccyy.resourcemanager.main;

import android.content.Context;
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

import java.util.ArrayList;

/**
 * Created by Sweven on 2018/6/13.
 * Email:sweventears@Foxmail.com
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private LayoutInflater inflater;
    public ArrayList<FileData> fileData;
    public ArrayList<FileData> fileData_More;

    private onClickItem mClickItem =null;

    public interface onClickItem {
        void onClick(String name,String path,int previous_position);
    }

    public void setOnClickItem(onClickItem onClickFolder){
        this.mClickItem =onClickFolder;
    }

    /**
     * @param context
     * @param data file数据：name、 path
     */
    public FileAdapter
    (Context context, ArrayList<FileData> data,ArrayList<FileData> extra_Information) {
        inflater = LayoutInflater.from(context);
        this.fileData_More=extra_Information;
        this.fileData =data;

    }

    @NonNull
    @Override
    public FileAdapter.FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.content_single_file,parent,false);
        return new FileViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * 渲染数据
     * @param holder
     * @param position item的位置信息
     */
    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        String name= fileData.get(position).getName();
        String path= fileData.get(position).getPath();
        Bitmap bitmap= fileData.get(position).getFileIcon();
        holder.file_name.setText(name);
        if(name.equals("previous"))
            holder.file_name.setText("返回上一级");
        holder.file_img.setImageBitmap(bitmap);

        addItemClick(path,name,holder,position);
    }

    /**
     * @method 添加文件夹的点击事件
     * @param path 文件或文件夹地址
     * @param name 文件或文件夹名称
     * @param holder
     */
    private void addItemClick(final String path, final String name, FileViewHolder holder, final int previous_position) {
        if(mClickItem !=null){
            holder.file_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickItem.onClick(name,path,previous_position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return fileData.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder{

        private ImageView file_img;
        private TextView file_name;
        private CheckBox file_check;
        private LinearLayout file_item;

        public FileViewHolder(View itemView) {
            super(itemView);
            file_img=itemView.findViewById(R.id.file_img);
            file_name=itemView.findViewById(R.id.file_name);
            file_check =itemView.findViewById(R.id.file_check);
            file_item = itemView.findViewById(R.id.file_item);
        }
    }
}
