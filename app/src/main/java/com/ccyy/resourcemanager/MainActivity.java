package com.ccyy.resourcemanager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccyy.resourcemanager.main.DeviceShow;
import com.ccyy.resourcemanager.main.FileAdapter;
import com.ccyy.resourcemanager.main.FileData;
import com.ccyy.resourcemanager.main.FileTools;
import com.ccyy.resourcemanager.music.MusicActivity;
import com.ccyy.resourcemanager.photo.PhotoActivity;
import com.ccyy.resourcemanager.text.TextActivity;
import com.ccyy.resourcemanager.tools.ExitSure;
import com.ccyy.resourcemanager.tools.FileOperation;
import com.ccyy.resourcemanager.tools.FileType;
import com.ccyy.resourcemanager.tools.T;
import com.ccyy.resourcemanager.video.VideoActivity;

import java.io.File;
import java.util.ArrayList;

import static com.ccyy.resourcemanager.main.FileTools.getFileList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayoutManager linearLayoutManager;
    private LinearLayout show_device;
    public RecyclerView file_recycler;

    private String rootPath = FileOperation.getSDPath();
    private String childFolder_path;
    private String childFolder_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initFile();

        setButton();

        TextActivity.loadTextList(MainActivity.this,rootPath);

    }

    private void setButton() {
        TextView file_send = findViewById(R.id.file_send);


        file_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.tips(MainActivity.this, "分享");
                //TODO 一个测试例子
                Intent intent = FileTools.shareFile("/sdcard/cb72f994370f9e817eaa495aaf428644.png");
                startActivity(intent);
            }
        });
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
        } else if (id == R.id.action_add_file) {
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
            Intent intent = new Intent(this, PhotoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_music) {
            Intent intent = new Intent(this, MusicActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_video) {
            Intent intent = new Intent(this, VideoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_text) {
            Intent intent = new Intent(this, TextActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initFile() {

        //初始化APP的临时文件储存位置、APP配置文件位置
        ResourceManager.createAppPath(ResourceManager.App_Path);
        ResourceManager.createAppPath(ResourceManager.App_Temp_Image_Path);
        ResourceManager.createAppPath(ResourceManager.App_Temp_Video_Image_Path);

        childFolder_path = rootPath;

        file_recycler = findViewById(R.id.file_list);
        linearLayoutManager = new LinearLayoutManager(this);

        getFileDir(rootPath, false);
    }

    /**
     * 取得文件架构的method
     *
     * @param parent_file_path 文件当前目录
     * @param isParent         当前操作是否为点击“返回上一级”
     */
    public void getFileDir(String parent_file_path, boolean isParent) {

        File present_file = new File(childFolder_path);

        new FileTools(MainActivity.this, rootPath);

        ArrayList<FileData> allFile = getFileList(parent_file_path);

        loadData(allFile, present_file, isParent);

    }


    /**
     * @param data              {@link ArrayList<FileData>} 进行渲染item
     * @param present_file      当前文件，即需要查询的文件在父目录中位置的文件
     * @param isParent          当前操作是否为点击“返回上一级”
     */
    private void loadData(@NonNull ArrayList<FileData> data, File present_file, boolean isParent) {

        show_device = findViewById(R.id.file_device);
        show_device.removeAllViews();
        new DeviceShow(MainActivity.this, show_device, rootPath, childFolder_path, isParent);

        if (isParent) {
            int position = FileTools.getPositionInParentFolder(data, present_file);
            // 通过 LayoutManager 的 srcollToPositionWithOffset 方法进行定位
            linearLayoutManager.scrollToPositionWithOffset(position, 0);
        }

        file_recycler.setLayoutManager(linearLayoutManager);

        FileAdapter fileAdapter = new FileAdapter(MainActivity.this, data);
        file_recycler.setAdapter(fileAdapter);
        file_recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        fileAdapter.setOnClickItem(new FileAdapter.onClickItem() {
            @Override
            public void onClick(ArrayList<FileData> fileData, int position) {
                String name=fileData.get(position).getName();
                String path=fileData.get(position).getPath();

                File file = new File(path);
                String parentPath = file.getParent();
                childFolder_path = path;

                if (name.equals("<<previous>>")) {
                    getFileDir(parentPath, true);
                } else {
                    if (file.isDirectory()) {
                        getFileDir(path, false);
                    }
                    else {
                        FileTools.openFile(file,MainActivity.this);
                    }
                }

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!childFolder_path.equals(rootPath)) {
                getFileDir(new File(childFolder_path).getParent(), true);
                childFolder_path = new File(childFolder_path).getParentFile().getPath();
            } else {
                ExitSure.exitApp(this);
            }
        }
        return true;
    }

}
