package com.ccyy.resourcemanager.text;

import com.ccyy.resourcemanager.R;
import com.ccyy.resourcemanager.main.FileData;
import com.ccyy.resourcemanager.tools.FileOperation;
import com.ccyy.resourcemanager.tools.FileType;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class TextActivity extends AppCompatActivity {

    public RecyclerView text_recycler;
    public static TextView text_tips;

    private String rootPath = FileOperation.getSDPath();

    private TextAdapter adapter;

    private ArrayList<FileData> text_list_data = new ArrayList<>();

    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        initList();
    }

    @SuppressLint("HandlerLeak")
    private void initList() {
        text_recycler = findViewById(R.id.all_text_list);
        text_tips = findViewById(R.id.text_tips);

        text_tips.setVisibility(View.INVISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        text_recycler.setLayoutManager(layoutManager);

//        getText(rootPath);

        loadTextList(TextActivity.this,rootPath);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    text_list_data = (ArrayList<FileData>) msg.obj;
                    loadItem();
                }
            }
        };
    }

    public static void loadTextList(Context context,String rootPath){
        SearchAllText searchAllText = new SearchAllText(context);
        searchAllText.execute(rootPath);
    }

    private void loadItem() {
        adapter = new TextAdapter(TextActivity.this, text_list_data);
        text_recycler.setAdapter(adapter);
        text_recycler.addItemDecoration(new DividerItemDecoration(this, 1));
    }

    private void getText(String rootPath) {
        File rootFile = new File(rootPath);

        for (File file : rootFile.listFiles()) {
            if (file.isDirectory()) {
                getText(file.getPath());
            } else {
                String file_path = file.getPath();
                String file_name = file.getName();
                Bitmap file_icon = BitmapFactory.decodeResource(getResources(), R.drawable.text);
                long file_size = file.length();
                long file_last_time = file.lastModified();
                final int NO_FOLDER = 0;
                final int NO_FILE = 0;
                if (FileType.isTextFileType(file_path)) {
                    FileData data = new FileData(file_name, file_path, file_icon, file_last_time, file_size, NO_FOLDER, NO_FILE, false);
                    text_list_data.add(data);
                }
            }
        }
    }

//    public static void IterateFile(File file) {
//        if(file.listFiles()!=null)//文件目錄下掃描到的文件為空判斷
//        {
//            for( File filex : file.listFiles())
//            {
//                if(filex.isDirectory())
//                {
//                    IterateFile(filex);
//                }
//                else
//                {
//                    String filename=filex.getName();
//                    System.out.println(filename);
//                    if(filename.endsWith(".mp3"))
//                    {
//                        Song mSong=new Song();
//                        mSong.setName(filename);
//                        mSong.setPath(file.getPath());
//                        mSong.setType("mp3");
//                        songLis.add(mSong);
//                    }
//                    else if(filename.endsWith(".wmv"))
//                    {Song mSong=new Song();
//                        mSong.setName(filename);
//                        mSong.setPath(file.getPath());
//                        mSong.setType("mp3");
//                        songLis.add(mSong);
//                    }
//                }
//            };
//        }
//    }

}
