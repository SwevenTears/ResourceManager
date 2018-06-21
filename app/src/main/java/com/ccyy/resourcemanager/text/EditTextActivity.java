package com.ccyy.resourcemanager.text;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.ccyy.resourcemanager.R;
import com.ccyy.resourcemanager.tools.FileOperation;
import com.ccyy.resourcemanager.tools.T;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;

public class EditTextActivity extends AppCompatActivity {

    private EditText editText;
    private String textNameAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get(new File(FileOperation.getSDPath()));
                T.tips(EditTextActivity.this, textNameAll);
                textNameAll="";
            }
        });

        editText = findViewById(R.id.edit_text_scroll);

        Intent intent = getIntent();
        String name = intent.getStringExtra("File_Name");
        String path = intent.getStringExtra("File_Path");
        getSupportActionBar().setTitle(name);
        showTextData(path);
    }

    private void get(File f) {
        String[] paths;

        try {

            // create new filename filter
            FilenameFilter fileNameFilter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.lastIndexOf('.') > 0) {
                        // get last index for '.' char
                        int lastIndex = name.lastIndexOf('.');

                        // get extension
                        String str = name.substring(lastIndex);

                        // match path name extension
                        if (str.equals(".txt")) {
                            return true;
                        }
                    }
                    return false;
                }
            };
            // returns pathnames for files and directory
            paths = f.list(fileNameFilter);
            File[] files = f.listFiles();
            for (File path : files) {
                if(path.isDirectory()){
                    if(path!=null){
                        get(path);
                    }

                }
            }

            // for each pathname in pathname array
            for (String path : paths) {
                // prints file and directory paths
                textNameAll = textNameAll + path + "\n";
            }
        } catch (Exception e) {
            // if any error occurs
            e.printStackTrace();
        }
    }

    private void showTextData(String path){
        File file = new File(path);
        try {
            InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            line = br.readLine();
            while (line != null) {
                editText.setText(editText.getText() + line + "\r\n");
                line = br.readLine(); // 一次读入一行数据
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
