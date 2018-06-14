package com.ccyy.resourcemanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccyy.resourcemanager.main.DeviceShow;
import com.ccyy.resourcemanager.main.FileAdapter;
import com.ccyy.resourcemanager.main.FileData;
import com.ccyy.resourcemanager.music.MusicActivity;
import com.ccyy.resourcemanager.photo.PhotoActivity;
import com.ccyy.resourcemanager.text.TextActivity;
import com.ccyy.resourcemanager.tools.ExitSure;
import com.ccyy.resourcemanager.tools.FileOperation;
import com.ccyy.resourcemanager.tools.T;
import com.ccyy.resourcemanager.video.VideoActivity;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayoutManager linearLayoutManager;
    private LinearLayout show_device;
    public RecyclerView file_recycler;

    private String rootPath=FileOperation.getSDPath();
    private String childFolder_path;
    private String childFolder_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initFile();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();

        if (id == R.id.action_add_folder) {
            return true;
        }
        else if(id == R.id.action_add_file) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        //todo 事件触发
        if (id == R.id.nav_photo) {
            Intent intent=new Intent(this, PhotoActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_music) {
            Intent intent=new Intent(this, MusicActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_video) {
            Intent intent=new Intent(this, VideoActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_text) {
            Intent intent=new Intent(this, TextActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_share) {

        }
        else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initFile(){
        childFolder_path=rootPath;

        file_recycler=findViewById(R.id.file_list);
        linearLayoutManager=new LinearLayoutManager(this);

        getFileDir(rootPath,false);
    }

    /**
     * 取得文件架构的method
     * @param filePath 文件当前目录
     * @param isParent 当前操作是否为点击“返回上一级”
     */
    public void getFileDir(String filePath,boolean isParent) {

        int previous_position=0;

        File f=new File(filePath);
        //找到f下的所有文件的列表
        File[] files=f.listFiles();

        ArrayList<FileData>
                allFile=new ArrayList<>()
                ,file=new ArrayList<>()
                ,extra_Information=new ArrayList<>();

        Log.i("当前目录",filePath);
        Log.i("根目录：",rootPath);

        boolean isRoot=false;

        if(!filePath.equals(rootPath)) {
            /* 设定为[并到上一层] */
            allFile.add(new FileData("previous",f.getPath()));
            isRoot=true;
        }
        /* 将所有文件存入ArrayList中 */
        for (File temp : files) {

            if (temp.exists()) {
                if (temp.isDirectory()) {//是否是文件夹
                    int folder_count = FileOperation.get_FolderCount_FileCount(temp.getPath())[1];
                    int file_count = FileOperation.get_FolderCount_FileCount(temp.getPath())[2];
                    extra_Information.add(new FileData(temp.lastModified(), temp.length(), folder_count, file_count));
                    allFile.add(new FileData(temp.getName(), temp.getPath()));
                }

                else{
                    extra_Information.add(new FileData(temp.lastModified(), temp.length(), 0, 0));
                    file.add(new FileData(temp.getName(), temp.getPath()));
                }

            }
        }
        ArrayList<FileData> order_allFile= FileOperation.order(allFile,isRoot);
        ArrayList<FileData> order_file= FileOperation.order(file,false);
        if(isParent){
            ArrayList<String> folder_names = new ArrayList<>();
            for(int i=0;i<order_allFile.size();i++)
                folder_names.add(order_allFile.get(i).getName());
            childFolder_name = new File(childFolder_path).getName();
            previous_position=FileOperation.find_folder_position(childFolder_name,folder_names);
        }

        order_allFile.addAll(order_file);
        loadData(order_allFile,extra_Information,previous_position,isParent);

    }


    /**
     * @param data {@link ArrayList<FileData>} 进行渲染item
     * @param extra_Information 额外的文件信息
     * @param previous_position 当前文件目录在上一级文件夹目录的位置
     * @param isParent 当前操作是否为点击“返回上一级”
     */
    private void loadData
    (@NonNull ArrayList<FileData> data,ArrayList<FileData> extra_Information,
     int previous_position,boolean isParent){

        show_device=findViewById(R.id.file_device);
        show_device.removeAllViews();
        new DeviceShow(MainActivity.this,show_device,rootPath,childFolder_path,isParent);

        if(isParent){
            // 通过LayoutManager的srcollToPositionWithOffset方法进行定位
            linearLayoutManager.scrollToPositionWithOffset(previous_position, 0);
        }

        file_recycler.setLayoutManager(linearLayoutManager);

        FileAdapter fileAdapter=new FileAdapter(MainActivity.this,data,extra_Information);
        file_recycler.setAdapter(fileAdapter);
        file_recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        fileAdapter.setOnClickFolder(new FileAdapter.onClickFolder() {
            @Override
            public void onClick(String name, String path,int previous_position) {
                File temp=new File(path);
                String parentPath=temp.getParent();
                childFolder_path=path;

                if(name.equals("previous")){
                    Log.i("当前点击的文件或文件夹名称",temp.getName());
                    getFileDir(parentPath,true);
                }
                else{
                    Log.i("当前点击的文件或文件夹名称",name);
                    if(temp.isDirectory())
                        getFileDir(path,false);
                }

            }
        });
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!childFolder_path.equals(rootPath)){
                getFileDir(new File(childFolder_path).getParent(),true);
                childFolder_path=new File(childFolder_path).getParentFile().getPath();
            }
            else{
                ExitSure.exitApp(this);
            }
        }
        return true;
    }

}
