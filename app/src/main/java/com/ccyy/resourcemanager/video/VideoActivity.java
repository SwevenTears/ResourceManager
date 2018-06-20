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
    private int imagedId;
    private Iterable<? extends Future<SubDirectoriesAndSize>> partialResults;
    private long total;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_first);
        adapter=new MyFileAdapter(myFileList);
        mRecyclerView=(RecyclerView) findViewById(R.id.id_recyclerview3);
        linearLayoutManager=new LinearLayoutManager(VideoActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(adapter);
        initMyFile(Environment.getExternalStorageDirectory());
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
                myFileName=itemView.findViewById(R.id.myFileImage);
                myFileImage=itemView.findViewById(R.id.myFileImage);
                myFileSize=itemView.findViewById(R.id.fileSize);
                myFilePower=itemView.findViewById(R.id.filePower);
                myFileDate=itemView.findViewById(R.id.fileDate);
            }
        }
    }


    private void initMyFile(File sourceFile) {
        myFileList.clear();
        List<File> files=new ArrayList<>();
        Collections.addAll(files,sourceFile.listFiles());
        for (File file:files){
            String fileName=file.getName();
            //默认是未知文件
//            int imageId=R.drawable.file_unknown;
            int imageId=R.drawable.ic_launcher_foreground;

            if (file.isDirectory()){
//                imageId=R.drawable.folder;
                imageId=R.drawable.ic_launcher_foreground;
            }else {
                int dotIndex=fileName.lastIndexOf(".");
                if (dotIndex>=0){
                    String end=fileName.substring(dotIndex,fileName.length()).toLowerCase();
                    if(!Objects.equals(end,"")){
                        if (Objects.equals(end,".mp4")||Objects.equals(end,".3gp")||Objects.equals(end,".wmv")||Objects.equals(end,".ts")||Objects.equals(end,".rmvb")||Objects.equals(end,".mov")||Objects.equals(end,".m4v")||Objects.equals(end,".avi")||Objects.equals(end,".m3u8")||Objects.equals(end,".3ggp")||Objects.equals(end,".3ggp2")||Objects.equals(end,".mkv")||Objects.equals(end,".flv")||Objects.equals(end,".divx")||Objects.equals(end,".f4v")||Objects.equals(end,".rm")||Objects.equals(end,".asf")||Objects.equals(end,".ram")||Objects.equals(end,".mpg")||Objects.equals(end,".v8")||Objects.equals(end,".swf")||Objects.equals(end,".m2v")||Objects.equals(end,".asx")||Objects.equals(end,".ra")||Objects.equals(end,".ndivx")||Objects.equals(end,".xvid")){
//                            Bitmap bitmap;
//                            bitmap = ThumbnailUtils.createVideoThumbnail(file.getPath(), MediaStore.Images.Thumbnails.MICRO_KIND);
//                            bitmap = ThumbnailUtils.extractThumbnail(bitmap,80,100,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

//                            imageId=R.drawable.video;
                            imageId=R.drawable.ic_launcher_foreground;

                        }
                    }
                }
            }
            String fileSize="";
            long size=0;
            //开始判断文件大小
            if (file.isDirectory()){
                try{
                    size=getTotalSizeofFilesInDir(file);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                size=file.length();
            }
            //判断大小是用什么单位，K/M/G
            if (size/1024/1024/1024>0){
                fileSize=size/1024/1024/1024+"G";
            }else if (size/1024/1024>0){
                fileSize=size/1024/1024+"M";
            }else {
                fileSize=size/1024+"K";
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
            MyFile myFile=new MyFile(fileName,imagedId,fileSize,filePower,fileDate);
            VideoActivity.myFileList.add(myFile);
        }
        adapter.notifyDataSetChanged();
    }
    //获取文件大小
    private class SubDirectoriesAndSize{
        final public long size;
        final public List<File> subDirectories;


        public SubDirectoriesAndSize(final long totalSize,final List<File> theSubDirs) {
            size = totalSize;
            subDirectories =Collections.unmodifiableList(theSubDirs);
        }
    }

    private SubDirectoriesAndSize getTotalAndSubDirs(final File file){
        long total=0;
        final List<File> subDirectories=new ArrayList<File>();
        if (file.isDirectory()){
            final File[] children=file.listFiles();
            if (children!=null){
                for (final File child:children){
                    if (child.isFile()){
                        total+=child.length();
                    }else{
                        subDirectories.add(child);
                    }
                }
            }
        }
        return new SubDirectoriesAndSize(total,subDirectories);
    }


    private long getTotalSizeofFilesInDir(final File file) throws InterruptedException,ExecutionException,TimeoutException {
        final ExecutorService service= Executors.newFixedThreadPool(100);
        try{
            long total=0;
            final List<File> directories=new ArrayList<File>();
            directories.add(file);
            while (!directories.isEmpty()){
                final List<Future<SubDirectoriesAndSize>> partialResults=new ArrayList<Future<SubDirectoriesAndSize>>();
                for (final File directory:directories){
                    partialResults.add(service.submit(new Callable<SubDirectoriesAndSize>() {
                        @Override
                        public SubDirectoriesAndSize call() {
                            return getTotalAndSubDirs(directory);
                        }
                    }));
                }
                directories.clear();
                for (final Future<SubDirectoriesAndSize> partialResultFuture:partialResults){
                    final SubDirectoriesAndSize subDirectoriesAndSize=partialResultFuture.get(100, TimeUnit.SECONDS);
                    directories.addAll(subDirectoriesAndSize.subDirectories);
                    total+=subDirectoriesAndSize.size;
                }
            }
            return total;
        }finally {
            service.shutdown();
        }
    }

    public static String getModifiedTime_2(File f) {
        Calendar cal=Calendar.getInstance();
        long time=f.lastModified();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());

    }




}
