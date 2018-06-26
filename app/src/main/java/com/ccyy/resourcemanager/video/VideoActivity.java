package com.ccyy.resourcemanager.video;

import com.ccyy.resourcemanager.R;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 *
 */
public class VideoActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MyFileAdapter adapter;
    public static List<MyFile> myFileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        myFileList = new ArrayList<>();
        initMyFile(Environment.getExternalStorageDirectory());//获取手机储存根目录
        adapter = new MyFileAdapter(myFileList);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview3);
        linearLayoutManager = new LinearLayoutManager(VideoActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//设置分割线
        mRecyclerView.setAdapter(adapter);


    }

    /**
     *
     */
    private class MyFileAdapter extends RecyclerView.Adapter<MyFileAdapter.ViewHolder> {
        private List<MyFile> myFileList;
        private Context mContext;

        public MyFileAdapter(List<MyFile> list) {
            super();
            myFileList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (mContext == null) {
                mContext = parent.getContext();
            }
            View view = LayoutInflater.from(mContext).inflate(R.layout.activity_video_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            MyFile myFile = myFileList.get(position);
            holder.myFileName.setText(myFile.getFileName());
            Bitmap bitmap;
            bitmap = ThumbnailUtils.createVideoThumbnail(myFile.getFilePath(), MediaStore.Images.Thumbnails.MICRO_KIND);
            bitmap = ThumbnailUtils.extractThumbnail(bitmap,80,100,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            holder.myFileImage.setImageBitmap(bitmap);
            holder.myFileSize.setText(myFile.getFileSize());
            holder.myFileDate.setText(myFile.getFileDate());

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
                Intent intent = new Intent(VideoActivity.this, PlayActivity.class);
                intent.putExtra("fileName", videoName);
                intent.putExtra("filePath", videoPath);
                startActivity(intent);
            }
        }
    }


    private void initMyFile(File sourceFile) {
        List<File> files = new ArrayList<>();
        files.addAll(Arrays.asList(sourceFile.listFiles()));
        String videoPath = "";
        for (File file : files) {

            String videoName = "<<>>";

            if (file.isDirectory()) {
                initMyFile(file);
            } else {
                String fileName = file.getName();
                int dotIndex = fileName.lastIndexOf(".");
                if (dotIndex >= 0) {
                    String end = fileName.substring(dotIndex, fileName.length()).toLowerCase();
                    if (!Objects.equals(end, "")) {
                        if (Objects.equals(end, ".mp4") || Objects.equals(end, ".3gp") ||
                                Objects.equals(end, ".wmv") || Objects.equals(end, ".ts") ||
                                Objects.equals(end, ".rmvb") || Objects.equals(end, ".mov") ||
                                Objects.equals(end, ".m4v") || Objects.equals(end, ".avi") ||
                                Objects.equals(end, ".m3u8") || Objects.equals(end, ".3ggp") ||
                                Objects.equals(end, ".3ggp2") || Objects.equals(end, ".mkv") ||
                                Objects.equals(end, ".flv") || Objects.equals(end, ".divx") ||
                                Objects.equals(end, ".f4v") || Objects.equals(end, ".rm") ||
                                Objects.equals(end, ".asf") || Objects.equals(end, ".ram") ||
                                Objects.equals(end, ".mpg") || Objects.equals(end, ".v8") ||
                                Objects.equals(end, ".swf") || Objects.equals(end, ".m2v") ||
                                Objects.equals(end, ".asx") || Objects.equals(end, ".ra") ||
                                Objects.equals(end, ".ndivx") || Objects.equals(end, ".xvid")) {
                            videoName = file.getName();
                            videoPath = file.getPath();
                        }
                    }
                }
            }
            String fileSize = "";
            long size = 0;
            //开始判断文件大小
            if (!file.isDirectory()) {
                size = file.length();
            }
            //判断大小是用什么单位，K/M/G
            if (size / 1024 / 1024 / 1024 > 0) {
                fileSize = size / 1024 / 1024 / 1024 + "G";
            } else if (size / 1024 / 1024 > 0) {
                fileSize = size / 1024 / 1024 + "M";
            } else if (size / 1024 > 0) {
                fileSize = size / 1024 + "K";
            } else {
                fileSize = size + "B";
            }

            String fileDate = getModifiedTime_2(file);
            if (!videoName.equals("<<>>")) {
                MyFile myFile = new MyFile(videoName, fileSize, fileDate,videoPath);
                myFileList.add(myFile);
            }

        }
    }

    public static String getModifiedTime_2(File f) {
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());

    }


}
