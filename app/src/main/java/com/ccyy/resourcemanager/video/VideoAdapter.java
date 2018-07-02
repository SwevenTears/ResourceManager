package com.ccyy.resourcemanager.video;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ccyy.resourcemanager.R;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

 class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private List<MyFile> myFileList;
    private Context mContext;
    public VideoAdapter(Context context, List<MyFile> list) {
        super();
        mContext=context;
        myFileList = list;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_video_item,
                parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

     /** 数据绑定
      * @param holder
      * @param position
      */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyFile myFile = myFileList.get(position);
        holder.myFileName.setText(myFile.getFileName());
        holder.myFileImage.setImageBitmap(myFile.getBitmap());
        holder.myFileSize.setText(getFileSizeChange(new File(myFile.getFilePath())));
        holder.myFileDate.setText(getModifiedTime(new File(myFile.getFilePath())));
    }
    @Override
    public int getItemCount() {
        return myFileList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView myFileName;
        private TextView myFileSize;
        private TextView myFileDate;
        private ImageView myFileImage;
        private LinearLayout startPlay;
        public ViewHolder(View itemView) {
            super(itemView);
            myFileName = itemView.findViewById(R.id.myFileName);
            myFileImage = itemView.findViewById(R.id.myFileImage);
            myFileSize = itemView.findViewById(R.id.fileSize);
            myFileDate = itemView.findViewById(R.id.fileDate);
            startPlay = itemView.findViewById(R.id.startPlay);
            startPlay.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            String videoName = myFileList.get(position).getFileName();
            String videoPath = myFileList.get(position).getFilePath();
            Intent intent = new Intent(mContext, PlayVideoActivity.class);
            intent.putExtra("fileName", videoName);
            intent.putExtra("filePath", videoPath);
            mContext.startActivity(intent);
        }
    }
     /**
      * @param f 获取视频文件时间
      * @return 返回修改格式后的日期
      */
     public String getModifiedTime(File f) {
         Calendar cal = Calendar.getInstance();
         long time = f.lastModified();
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式
         cal.setTimeInMillis(time);
         return formatter.format(cal.getTime());
     }
     /**
      * @param file 判断文件的大小
      * @return 返回文件的大小值：B/K/M/G
      */
     public String getFileSizeChange(File file){
         long size = file.length();
         //判断大小是用什么单位，K/M/G
         if (size / 1024 / 1024 / 1024 > 0) {
             return size / 1024 / 1024 / 1024 + "G";
         } else if (size / 1024 / 1024 > 0) {
             return size / 1024 / 1024 + "M";
         } else if (size / 1024 > 0) {
             return size / 1024 + "K";
         } else {
             return size + "B";
         }
     }
}