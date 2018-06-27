package com.ccyy.resourcemanager.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ccyy.resourcemanager.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FileDetailsActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_title_center);

        TextView title=findViewById(R.id.actionbar_title_name);
        title.setText("详  情");
        title.setTextColor(Color.WHITE);


        TextView detail_name = findViewById(R.id.detail_name);
        TextView detail_last_time = findViewById(R.id.detail_last_time);
        TextView detail_size = findViewById(R.id.detail_size);
        TextView detail_path = findViewById(R.id.detail_path);
        TextView detail_include_name = findViewById(R.id.detail_include_name);
        TextView detail_include = findViewById(R.id.detail_include);

        Intent details = getIntent();
        String path = details.getStringExtra("path");
        File file=new File(path);
        String name=file.getName();
        String size=FileTools.getFileSize(file.length());

        long millionSeconds = file.lastModified();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millionSeconds);
        Date date = c.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        String last_time = sdf.format(date);

        detail_name.setText(name);
        detail_last_time.setText(last_time);
        detail_path.setText(path);

        if (new File(path).isDirectory()) {
            int[] count = get_FolderCount_FileCount(path);
            detail_include.setText(count[1] + "个文件夹、" + count[2] + "个文件");
            String folder_size = FileTools.getFileSize(getDirectorySize(new File(path)));
            detail_size.setText(folder_size);
        } else {
            detail_include_name.setText("");
            detail_size.setText(size);
        }

    }

    /**
     * @param folder_path 需要查询文件数量的文件夹名称
     * @return 当前文件夹的所有文件数量、文件夹数量和文件数量
     */
    public int[] get_FolderCount_FileCount(String folder_path) {
        int count[] = {0, 0, 0};
        File parent_folder = new File(folder_path);
        File[] parents = parent_folder.listFiles();
        for (File child:parents) {
            if(!child.isHidden()) {
                if (child.isDirectory()) {
                    count[1]++;
                    int[] child_count = get_FolderCount_FileCount(child.getPath());
                    count[0] += child_count[0];
                    count[1] += child_count[1];
                    count[2] += child_count[2];
                } else {
                    count[2]++;
                }
                count[0]++;
            }

        }

        return count;
    }

    /**
     * @param file 需要获取大小的目录
     * @return 目录的大小
     */
    private static long getDirectorySize(File file) {
        long size = 0;
        File files[] = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                size = size + getDirectorySize(files[i]);
            } else {
                size = size + files[i].length();
            }
        }
        return size;
    }
}
