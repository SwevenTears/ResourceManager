package com.ccyy.resourcemanager.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ccyy.resourcemanager.R;

import java.io.File;

public class FileDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_details);


        TextView detail_name=findViewById(R.id.detail_name);
        TextView detail_last_time=findViewById(R.id.detail_last_time);
        TextView detail_size=findViewById(R.id.detail_size);
        TextView detail_path=findViewById(R.id.detail_path);
        TextView detail_include_name=findViewById(R.id.detail_include_name);
        TextView detail_include=findViewById(R.id.detail_include);

        Intent details=getIntent();
        String name = details.getStringExtra("name");
        String last_time = details.getStringExtra("last_time");
        String size = details.getStringExtra("size");
        String path = details.getStringExtra("path");
        String include = details.getStringExtra("include");

        detail_name.setText(name);
        detail_last_time.setText(last_time);
        detail_size.setText(size);
        detail_path.setText(path);


        if(new File(path).isDirectory()){
            detail_include.setText(include);
        }
        else {
            detail_include_name.setText("");
        }

    }
}
