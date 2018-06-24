package com.ccyy.resourcemanager.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccyy.resourcemanager.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Sweven on 2018/6/13.
 * Email:sweventears@Foxmail.com
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private LayoutInflater inflater;
    public ArrayList<FileData> fileDatas;

    private onClickItem mClickItem = null;
    private onLongClickItem mLongClickItem = null;

    /**
     * @param context
     * @param data    file数据：name、 path
     */
    public FileAdapter(Context context, ArrayList<FileData> data) {
        inflater = LayoutInflater.from(context);
        this.fileDatas = data;

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
    public void onBindViewHolder(@NonNull final FileViewHolder holder, final int position) {
        String name = fileDatas.get(position).getName();
        String path = fileDatas.get(position).getPath();
        Bitmap bitmap = fileDatas.get(position).getFileIcon();
        boolean isCheck = fileDatas.get(position).isCheck();
        long file_size = fileDatas.get(position).getSize();

        holder.file_name.setText(name);
        if (name.equals("<<previous>>"))
            holder.file_name.setText("返回上一级");
        holder.file_img.setImageBitmap(bitmap);

        Log.e("第" + position + "", isCheck + "");
        if (isCheck) {
            holder.file_check.setSelected(true);
        }

        setFileSign(path, name, holder, file_size);

        addItemClick(fileDatas, holder, position);

    }

    private void setFileSign(String file_path, String name, FileViewHolder holder, long file_size) {
        File file = new File(file_path);
        if (file.isDirectory()) {
            if (!name.equals("<<previous>>")) {
                holder.file_sign.setTextSize(14);
                holder.file_sign.setText(">");
            } else {
                holder.file_sign.setText("");
            }
        } else {
            holder.file_sign.setTextSize(12);
            holder.file_sign.setText(FileTools.getFileSize(file_size));
        }
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
        if(mLongClickItem != null){
            holder.file_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickItem.onClick(fileData, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return fileDatas.size();
    }

    public void addData(FileData singleFile) {
        fileDatas.add(singleFile);
        notifyItemInserted(getOrderPosition(fileDatas, singleFile));
    }

    public void delData(FileData singleFile) {
        notifyItemRemoved(getOrderPosition(fileDatas, singleFile));
        fileDatas.remove(singleFile);
    }

    private int getOrderPosition(ArrayList<FileData> fileDatas, FileData searchFile) {
        int position = 0;
        for (int i = 0; i < fileDatas.size(); i++) {
            if (fileDatas.get(i).equals(searchFile)) {
                position = i;
            }
        }
        return position;
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
            file_sign = itemView.findViewById(R.id.file_sign);
            file_check = itemView.findViewById(R.id.file_check);
            file_item = itemView.findViewById(R.id.file_item);

            file_check.setVisibility(View.INVISIBLE);
            file_check.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            boolean isCheck = fileDatas.get(position).isCheck();
            String name = fileDatas.get(position).getName();
            String path = fileDatas.get(position).getPath();

            switch (v.getId()) {
                case R.id.file_check: {
                    if (!isCheck) {
                        fileDatas.get(position).setCheck(true);
                    } else {
                        fileDatas.get(position).setCheck(false);
                    }
                    break;
                }
            }
        }
    }

    public interface onClickItem {
        void onClick(ArrayList<FileData> fileData, int previous_position);
    }

    public void setOnClickItem(onClickItem onClickFolder) {
        this.mClickItem = onClickFolder;
    }

    public interface onLongClickItem {
        void onClick(ArrayList<FileData> fileData, int position);
    }

    public void setOnLongClickItem(onLongClickItem LongClickFolder) {
        this.mLongClickItem = LongClickFolder;
    }
}
