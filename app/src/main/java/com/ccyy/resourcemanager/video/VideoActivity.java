package com.ccyy.resourcemanager.video;

import com.ccyy.resourcemanager.R;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


public class VideoActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private VideoAdapter adapter;
    public static List<MyFile> myFileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        myFileList = new ArrayList<>();
        getVideo();
        adapter = new VideoAdapter(VideoActivity.this,myFileList);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview3);
        linearLayoutManager = new LinearLayoutManager(VideoActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//设置分割线
        mRecyclerView.setAdapter(adapter);
    }

    void getVideo(){
        String []projection={
                MediaStore.Video.Media._ID,MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA,MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_MODIFIED};
        String orderBy=MediaStore.Video.Media.DISPLAY_NAME;
        Uri uri=MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        getContentProvider(uri,projection,orderBy);
    }
    /**
     * ContentProvider遍历手机里面的所有视频
     */
    private void getContentProvider(Uri uri, String[] projection, String orderBy) {
        Cursor cursor=getContentResolver().query(uri,projection,null,null,orderBy);
        if (null==cursor){
            return;
        }
        while (cursor.moveToNext()){
                String path=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String name=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));//视频的名称
                String size= cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));//视频的大小
                String date=cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED));//视频的日期
                int media_id=cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                Bitmap bitmap=MediaStore.Video.Thumbnails.getThumbnail(getContentResolver(),
                        media_id, MediaStore.Video.Thumbnails.MICRO_KIND, null); //获取视频的缩略图
                myFileList.add(new MyFile(name,size,date,path,bitmap));
        }
    }

}
