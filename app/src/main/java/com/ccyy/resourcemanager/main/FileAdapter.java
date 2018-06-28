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

    private boolean isShowCheck = false;

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
        Bitmap bitmap = fileDatas.get(position).getFileIcon();


        holder.file_name.setText(name);
        if (name.equals("<<previous>>"))
            holder.file_name.setText("返回上一级");
        holder.file_img.setImageBitmap(bitmap);

        cutShow_SelectState(holder, name, position);

        addItemClick(holder, position);

    }

    /**
     * 切换文件浏览和文件选择状态后的不同渲染方式
     *
     * @param holder   ，
     * @param name     创建文件目录时赋予的文件名
     * @param position 渲染数据时当前位置
     */
    private void cutShow_SelectState(FileViewHolder holder, String name, int position) {
        boolean isCheck = fileDatas.get(position).isCheck();
        if (isShowCheck) {
            holder.file_sign.setVisibility(View.INVISIBLE);
            if (name.equals("<<previous>>")) {
                holder.file_check.setVisibility(View.INVISIBLE);
            } else {
                holder.file_check.setVisibility(View.VISIBLE);
            }

            if (isCheck) {
                holder.file_check.setChecked(true);
            } else {
                holder.file_check.setChecked(false);
            }
        } else {
            holder.file_sign.setVisibility(View.VISIBLE);
            holder.file_check.setVisibility(View.INVISIBLE);
            setFileSign(name, holder, position);
        }
    }

    /**
     * 显示文件的大小和添加目录的引导
     *
     * @param name     文件名称
     * @param holder   。
     * @param position
     */
    private void setFileSign(String name, FileViewHolder holder, int position) {

        String path = fileDatas.get(position).getPath();
        long file_size = fileDatas.get(position).getSize();
        File file = new File(path);
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
     * @param holder
     * @param position
     */
    private void addItemClick(FileViewHolder holder, final int position) {
        if (mClickItem != null) {
            holder.file_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickItem.onClick(fileDatas, position);
                }
            });
            holder.file_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickItem.onClick(fileDatas, position);
                }
            });
        }
        if (mLongClickItem != null) {
            holder.file_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongClickItem.onClick(fileDatas, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return fileDatas.size();
    }

    /**
     * 新增数据后并刷新adapter
     *
     * @param singleFile 单个文件信息
     */
    public void addData(FileData singleFile) {
        fileDatas.add(singleFile);
        notifyItemInserted(getOrderPosition(fileDatas, singleFile.getPath()));
        notifyDataSetChanged();
    }

    /**
     * 删除数据并刷新adapter
     *
     * @param singleFile 单个文件的路径
     */
    public void delData(String singleFile) {
        int position = getOrderPosition(fileDatas, singleFile);
        notifyItemRemoved(position);
        fileDatas.remove(position);
        notifyDataSetChanged();
        hiddenCheckBox();
    }

    /**
     * 修改数据后并刷新adapter
     *
     * @param file_path 要修改的文件的地址
     * @param new_name  修改后要显示的文件名称
     */
    public void amendData(String file_path, String new_name) {
        int position = getOrderPosition(fileDatas, file_path);
        fileDatas.get(position).setName(new_name);
        notifyItemChanged(position);
        notifyDataSetChanged();
    }

    /**
     * 首次进入选择文件时的改变
     *
     * @param position 长按文件的position
     */
    public void showCheckBox(int position) {
        isShowCheck = true;
        fileDatas.get(position).setCheck(true);
        notifyDataSetChanged();
    }

    /**
     * 切换成浏览模式时隐藏CheckBox
     */
    public void hiddenCheckBox() {
        isShowCheck = false;
        for (int i = 0; i < getItemCount(); i++) {
            fileDatas.get(i).setCheck(false);
        }
        notifyDataSetChanged();
    }

    /**
     * 选中一个数据后刷新数据
     *
     * @param position 选中的文件位置
     */
    public void addCheckedItem(int position) {
        fileDatas.get(position).setCheck(true);
        notifyDataSetChanged();
    }

    /**
     * 取消选择文件后刷新数据
     *
     * @param position 选择文件时的位置
     */
    public void delCheckedItem(int position) {
        fileDatas.get(position).setCheck(false);
        notifyDataSetChanged();
    }

    /**
     * @param fileDatas  当前所有文件信息
     * @param searchFile 要查询位置的文件路径
     * @return 文件的位置
     */
    private int getOrderPosition(ArrayList<FileData> fileDatas, String searchFile) {
        int position = 0;
        for (int i = 0; i < fileDatas.size(); i++) {
            if (fileDatas.get(i).getPath().equals(searchFile)) {
                position = i;
            }
        }
        return position;
    }

    class FileViewHolder extends RecyclerView.ViewHolder {

        private ImageView file_img;
        private TextView file_name;
        private TextView file_sign;
        private CheckBox file_check;
        private LinearLayout file_item;

        FileViewHolder(View itemView) {
            super(itemView);
            file_img = itemView.findViewById(R.id.file_img);
            file_name = itemView.findViewById(R.id.file_name);
            file_sign = itemView.findViewById(R.id.file_sign);
            file_check = itemView.findViewById(R.id.file_check);
            file_item = itemView.findViewById(R.id.file_item);

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
