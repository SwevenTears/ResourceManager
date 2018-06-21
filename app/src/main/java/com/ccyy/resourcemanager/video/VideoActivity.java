package com.ccyy.resourcemanager.video;

import com.ccyy.resourcemanager.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class VideoActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MyFileAdapter adapter;
    public static List<MyFile> myFileList;
    private Context mContext;
    private File file;
    private String fileName;
    private long total;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_first);

        myFileList=new ArrayList<>();
        initMyFile(Environment.getExternalStorageDirectory());
        adapter=new MyFileAdapter(myFileList);
        mRecyclerView=(RecyclerView) findViewById(R.id.id_recyclerview3);
        linearLayoutManager=new LinearLayoutManager(VideoActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);

    }



    private class MyFileAdapter extends RecyclerView.Adapter<MyFileAdapter.ViewHolder>{
        private List<MyFile> myFileList;
        private Context mContext;
        public MyFileAdapter(List<MyFile> list) {
            super();
            myFileList=list;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mContext==null){
                mContext=parent.getContext();
            }
            View view= LayoutInflater.from(mContext).inflate(R.layout.activity_video_list2,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
//            LayoutInflater layoutInflater=LayoutInflater.from(VideoActivity.this);
//            View view=layoutInflater.inflate(R.layout.activity_video_list,parent,false);
//            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MyFile myFile=myFileList.get(position);
            holder.myFileName.setText(myFile.getFileName());
            holder.myFileImage.setImageResource(myFile.getImageId());
            holder.myFileSize.setText(myFile.getFileSize());
            holder.myFilePower.setText(myFile.getFilePower());
            holder.myFileDate.setText(myFile.getFileDate());

        }

        @Override
        public int getItemCount() {
            return myFileList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView myFileName;
            private ImageView myFileImage;
            private TextView myFileSize;
            private TextView myFilePower;
            private TextView myFileDate;
            public ViewHolder(View itemView) {
                super(itemView);
                myFileName=itemView.findViewById(R.id.myFileName);
                myFileImage=itemView.findViewById(R.id.myFileImage);
                myFileSize=itemView.findViewById(R.id.fileSize);
                myFilePower=itemView.findViewById(R.id.filePower);
                myFileDate=itemView.findViewById(R.id.fileDate);
            }
        }
    }


    public void initMyFile(File sourceFile) {
//        myFileList.clear();
        List<File> files=new ArrayList<>();
//        Collections.addAll(files,sourceFile.listFiles());
        files.addAll(Arrays.asList(sourceFile.listFiles()));
        for (File file:files){
            String vedioName="<<<空>>>";
            //默认是未知文件
//            int imageId=R.drawable.file_unknown;
//            int imageId=R.drawable.ic_launcher_foreground;
            int imageId=0;
            if (file.isDirectory()){
                initMyFile(file);
            }else {
                String fileName=file.getName();
                int dotIndex=fileName.lastIndexOf(".");
                if (dotIndex>=0){
                    String end=fileName.substring(dotIndex,fileName.length()).toLowerCase();
                    if(!Objects.equals(end,"")){
                        if (Objects.equals(end,".mp4")||Objects.equals(end,".3gp")||Objects.equals(end,".wmv")||
                                Objects.equals(end,".ts")||Objects.equals(end,".rmvb")||
                                Objects.equals(end,".mov")||Objects.equals(end,".m4v")||
                                Objects.equals(end,".avi")||Objects.equals(end,".m3u8")||
                                Objects.equals(end,".3ggp")||Objects.equals(end,".3ggp2")||
                                Objects.equals(end,".mkv")||Objects.equals(end,".flv")||
                                Objects.equals(end,".divx")||Objects.equals(end,".f4v")||
                                Objects.equals(end,".rm")||Objects.equals(end,".asf")||
                                Objects.equals(end,".ram")||Objects.equals(end,".mpg")||
                                Objects.equals(end,".v8")||Objects.equals(end,".swf")||
                                Objects.equals(end,".m2v")||Objects.equals(end,".asx")||
                                Objects.equals(end,".ra")||Objects.equals(end,".ndivx")||
                                Objects.equals(end,".xvid")){
//                            Bitmap bitmap;
//                            bitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Images.Thumbnails.MICRO_KIND);
//                            bitmap = ThumbnailUtils.extractThumbnail(bitmap,80,100,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

                            imageId=R.drawable.video;
//                            imageId=R.drawable.ic_launcher_foreground;
                            vedioName=file.getName();

                        }
                    }
                }
            }
            String fileSize="";
            long size=0;
            //开始判断文件大小
            if (!file.isDirectory()){
                size=file.length();
            }
            //判断大小是用什么单位，K/M/G
            if (size/1024/1024/1024>0){
                fileSize=size/1024/1024/1024+"G";
            }else if (size/1024/1024>0){
                fileSize=size/1024/1024+"M";
            }else if(size/1024>0){
                fileSize=size/1024+"K";
            }else{
                fileSize=size+"B";
            }

            String filePower="";
            StringBuilder builder=new StringBuilder();
            builder.append("-");
            if (file.canRead()) builder.append("r");
            if (file.canWrite()) builder.append("w");
            if (file.canExecute()) builder.append("x");
            filePower=builder.toString();
            String fileDate="";
            fileDate=getModifiedTime_2(file);
            if(!vedioName.equals("<<<空>>>")) {
                MyFile myFile=new MyFile(vedioName,imageId,fileSize,filePower,fileDate);
                myFileList.add(myFile);
            }

        }
//        adapter.notifyDataSetChanged();
    }

    public static String getModifiedTime_2(File f) {
        Calendar cal=Calendar.getInstance();
        long time=f.lastModified();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());

    }



}
