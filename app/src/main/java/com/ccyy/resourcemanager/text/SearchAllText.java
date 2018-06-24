package com.ccyy.resourcemanager.text;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ccyy.resourcemanager.R;
import com.ccyy.resourcemanager.main.FileData;
import com.ccyy.resourcemanager.tools.FileOperation;
import com.ccyy.resourcemanager.tools.FileType;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sweven on 2018/6/21.
 * Email:sweventears@Foxmail.com
 */
public class SearchAllText extends AsyncTask<String,String,Void> {

    private Context context;
    private ArrayList<FileData> text_list=new ArrayList<>();

    final int NO_FOLDER = 0;
    final int NO_FILE = 0;
    Bitmap file_icon;


    public SearchAllText(Context context) {
        this.context=context;

        file_icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.text);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(String... strings) {
        String rootPath = strings[0];
        getText(rootPath);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Message message=TextActivity.handler.obtainMessage();
        message.what=1;
        message.obj=text_list;
        TextActivity.handler.sendMessage(message);
    }

    private void getText(String rootPath) {
        File rootFile = new File(rootPath);

        for (File file : rootFile.listFiles()) {
            if (file.isDirectory()) {
                if(file!=null) {
                    getText(file.getPath());
                }
            }
            else {
                String file_path = file.getPath();
                if (FileType.isTextFileType(file_path)) {
                    String file_name = file.getName();
                    long file_size = file.length();
                    long file_last_time = file.lastModified();
                    FileData data=new FileData(file_name,file_path,file_last_time,file_size,false);
                    text_list.add(data);
                }
            }
        }
    }


}
