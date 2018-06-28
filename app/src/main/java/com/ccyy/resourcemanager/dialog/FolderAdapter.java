package com.ccyy.resourcemanager.dialog;

import android.annotation.SuppressLint;
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
import com.ccyy.resourcemanager.main.FileData;

import java.util.ArrayList;

/**
 * Created by Sweven on 2018/6/25.
 * Email:sweventears@Foxmail.com
 */
public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<FileData> folders;
    private Bitmap bitmap;

    private onClickItem mClickItem;

    /**
     * @param context
     * @param data    file数据：name、 path
     */
    public FolderAdapter(Context context, ArrayList<FileData> data) {
        inflater = LayoutInflater.from(context);
        this.folders = data;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.folder);
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dialog_choose_folder_item, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FileData folder = folders.get(position);
        holder.folder_name.setText(folder.getName());
        holder.folder_img.setImageBitmap(bitmap);

        if (folder.getName().equals("<<previous>>"))
            holder.folder_name.setText("返回上一级");

        if (!folder.getName().equals("<<previous>>")) {
            holder.folder_sign.setTextSize(10);
            holder.folder_sign.setText(">");
        } else {
            holder.folder_sign.setText("");
        }

        if (mClickItem != null) {
            holder.folder_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickItem.onClick(folders, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    class FolderViewHolder extends RecyclerView.ViewHolder {

        private ImageView folder_img;
        private TextView folder_name;
        private TextView folder_sign;
        private LinearLayout folder_item;

        FolderViewHolder(View itemView) {
            super(itemView);
            folder_img = itemView.findViewById(R.id.folder_img);
            folder_name = itemView.findViewById(R.id.folder_name);
            folder_sign = itemView.findViewById(R.id.folder_sign);
            folder_item = itemView.findViewById(R.id.folder_item);

        }
    }

    public interface onClickItem {
        void onClick(ArrayList<FileData> fileData, int previous_position);
    }

    public void setOnClickItem(onClickItem onClickFolder) {
        this.mClickItem = onClickFolder;
    }
}
