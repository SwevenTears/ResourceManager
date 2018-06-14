package com.ccyy.resourcemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.ccyy.resourcemanager.main.FileAdapter;
import com.ccyy.resourcemanager.main.FileData;
import com.ccyy.resourcemanager.music.MusicActivity;
import com.ccyy.resourcemanager.photo.PhotoActivity;
import com.ccyy.resourcemanager.text.TextActivity;
import com.ccyy.resourcemanager.tools.FileOrder;
import com.ccyy.resourcemanager.video.VideoActivity;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

    public RecyclerView file_recycler;

    private String rootPath="/storage/sdcard0";
    private String childFolder;

    public void initFile(){
        file_recycler=findViewById(R.id.file_list);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        file_recycler.setLayoutManager(linearLayoutManager);

        getFileDir(rootPath);
    }

    /**
     * 取得文件架构的method
     * @param filePath 文件当前目录
     */
    private void getFileDir(String filePath) {

        childFolder=filePath;

        File f=new File(filePath);
        //找到f下的所有文件的列表
        File[] files=f.listFiles();

        ArrayList<FileData> allFile=new ArrayList<>();
        ArrayList<FileData> file=new ArrayList<>();
        ArrayList<FileData> extra_Information=new ArrayList<>();

        Log.i("当前目录",filePath);
        Log.i("根目录：",rootPath);

        boolean isRoot=false;

        if(!filePath.equals(rootPath)) {
            /* 设定为[并到上一层] */
            allFile.add(new FileData("root",f.getPath()));
            isRoot=true;
        }
        /* 将所有文件存入ArrayList中 */
        for(int i=0;i<files.length;i++) {

            File temp=files[i];
            if(temp.exists()){
                int folder_count=0,file_count=0;
                if(temp.isDirectory()) {//是否是文件夹
                    File[] child=temp.listFiles();
                    for (int j=0;j<child.length;j++){
                        File child_item=child[j];
                        if(child_item.isDirectory())
                            folder_count++;
                        else
                            file_count++;
                    }

                    allFile.add(new FileData(temp.getName(), temp.getPath()));
                }
                else
                    file.add(new FileData(temp.getName(),temp.getPath()));


                extra_Information.add(new FileData(temp.lastModified(),temp.length(),folder_count,file_count));
            }
        }
        ArrayList<FileData> order_allFile= FileOrder.order(allFile,isRoot);
        ArrayList<FileData> order_file= FileOrder.order(file,false);
        order_allFile.addAll(order_file);
        loadData(order_allFile,extra_Information);

    }


    /**
     * @param data {@link ArrayList<FileData>} 进行渲染item
     * @param extra_Information 额外的文件信息
     */
    private void loadData(@NonNull ArrayList<FileData> data,ArrayList<FileData> extra_Information){
        FileAdapter fileAdapter=new FileAdapter(MainActivity.this,data,extra_Information);
        file_recycler.setAdapter(fileAdapter);
        file_recycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        fileAdapter.setOnClickFolder(new FileAdapter.onClickFolder() {
            @Override
            public void onClick(String name, String path) {
                if(name.equals("root")){
                    getFileDir(new File(path).getParent());
                }
                else
                    getFileDir(path);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!childFolder.equals(rootPath)){
                getFileDir(new File(childFolder).getParent());
            }
            else{
                exitApplication();
            }
        }
        return true;
    }

    /**
     * 退出确认
     */
    private void exitApplication() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setTitle("退出");
        builder.setMessage("确定退出吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        builder.show();
    }
}
