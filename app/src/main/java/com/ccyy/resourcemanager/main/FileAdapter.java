package com.ccyy.resourcemanager.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.ccyy.resourcemanager.tools.FileOperation;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Sweven on 2018/6/13.
 * Email:sweventears@Foxmail.com
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private LayoutInflater inflater;
    public ArrayList<FileData> fileData;
    public ArrayList<FileData> fileData_More;


    private Bitmap folder_previous;
    private Bitmap folder_icon;
    private Bitmap mIcon4;

    private onClickFolder mClickFolder=null;

    public interface onClickFolder{
        void onClick(String name,String path,int previous_position);
    }

    public void setOnClickFolder(onClickFolder onClickFolder){
        this.mClickFolder=onClickFolder;
    }

    /**
     * @param context
     * @param data file数据：name、 path
     */
    public FileAdapter(Context context, ArrayList<FileData> data,ArrayList<FileData> extra_Information) {
        inflater = LayoutInflater.from(context);
        this.fileData_More=extra_Information;
        this.fileData =data;

        folder_previous = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.folder_previous);
        folder_icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.folder);
        mIcon4 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.file_unknown);
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
        holder.file_name.setText(name);

        judgeFileType(path,name,holder);
        addFolderClick(path,name,holder,position);
    }

    /**
     * @method 添加文件夹的点击事件
     * @param path 文件或文件夹地址
     * @param name 文件或文件夹名称
     * @param holder
     */
    private void addFolderClick(final String path, final String name, FileViewHolder holder, final int previous_position) {
        if(mClickFolder!=null){
            if(name.equals("root")){
                holder.file_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mClickFolder.onClick(name,path,previous_position);
                    }
                });
            }
            else{
                File file = new File(path);
                if (file.exists()) {
                    if(file.isDirectory())
                        holder.file_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mClickFolder.onClick(name,path,previous_position);

                            }
                        });
                }
            }
        }
    }

    /**
     * @param path 文件或文件夹的地址
     * @param name 文件或文件夹的名称
     * @param holder {@link FileViewHolder} 的参数
     * @return 渲染文件或文件夹的图标
     */
    private void judgeFileType(String path,String name,FileViewHolder holder){
        File f=new File(path);

        if(name.equals("previous")) {
            holder.file_name.setText("返回上一级");
            holder.file_img.setImageBitmap(folder_previous);
            holder.file_check.setVisibility(View.INVISIBLE);
        }
        else {
            //这时这一项是文件或者是文件夹
            holder.file_name.setText(f.getName());
            if(f.isDirectory()) {//文件夹
                holder.file_img.setImageBitmap(folder_icon);
            }
            else{//文件
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    holder.file_img.setImageBitmap
                            (FileOperation.setImgSize(name,bitmap,80,100));
                } catch (Exception e) {
                    holder.file_img.setImageBitmap(mIcon4);
                }
            }
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
