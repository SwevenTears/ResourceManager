package com.ccyy.resourcemanager.photo;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.ccyy.resourcemanager.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoActivity extends AppCompatActivity {
    //查看图片按钮
    private Button look;
    private  Button add;
    //显示图片名称的List
    ListView show_list;
    ArrayList names=null;
    ArrayList descs=null;
    ArrayList fileNames=null;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        look=findViewById(R.id.look);
        add=findViewById(R.id.add);
        show_list= findViewById(R.id.show_List);

        names=new ArrayList();//创建对象
        descs=new ArrayList();
        fileNames=new ArrayList();

        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor=getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,null,null,null);
                while (cursor.moveToNext()){
                    //获取图片名称
                    String name=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                    //获取图片生成日期
                    String desc=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));
                    //获取图片详细信息
                    byte[] data=cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    names.add(name);
                    descs.add(desc);
                    fileNames.add(new String(data,0,data.length-1));

                }
                List<Map<String,Object >>listItems=new ArrayList<>();
                for (int  i=0;i<names.size();i++){
                    Map<String,Object>map=new HashMap<>();
                    map.put("name",names.get(i));
                    map.put("desc",descs.get(i));
                    listItems.add(map);
                }
                //设置Adapter
                //SimpleAdapter adapter=new SimpleAdapter(GetAllImg.this,listItems,R.layout.activity_photo,new String[]{"name","desc"},new int[]{R.id.name,R.id.desc});
                  //  show_list.setAdapter(adapter);
            }
        });
        //List点击事件
        show_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

    }
}
