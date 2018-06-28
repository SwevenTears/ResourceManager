package com.ccyy.resourcemanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ccyy.resourcemanager.dialog.ChooseFolderDialog;
import com.ccyy.resourcemanager.dialog.InputNameDialog;
import com.ccyy.resourcemanager.main.DeviceShow;
import com.ccyy.resourcemanager.main.FileAdapter;
import com.ccyy.resourcemanager.main.FileData;
import com.ccyy.resourcemanager.main.FileDetailsActivity;
import com.ccyy.resourcemanager.main.FileTools;
import com.ccyy.resourcemanager.music.MusicActivity;
import com.ccyy.resourcemanager.photo.PhotoActivity;
import com.ccyy.resourcemanager.tools.AffirmDialog;
import com.ccyy.resourcemanager.tools.FileOperation;
import com.ccyy.resourcemanager.tools.T;
import com.ccyy.resourcemanager.video.VideoActivity;

import java.io.File;
import java.util.ArrayList;

import static com.ccyy.resourcemanager.main.FileTools.getFileList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_photo) {
            Intent intent = new Intent(this, PhotoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_music) {
            Intent intent = new Intent(this, MusicActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_video) {
            Intent intent = new Intent(this, VideoActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    private static T t;

    private LinearLayoutManager linearLayoutManager;
    private LinearLayout show_device;
    public RecyclerView file_recycler;
    private LinearLayout content_main_layout;
    private TableLayout table_menu_layout;
    private TableLayout table_menu_layout_replace;

    private String rootPath = FileOperation.getMobilePath();
    private String present_path;

    private FileAdapter fileAdapter;
    private ChooseFolderDialog folderChooser;
    private String previous_path;

    /**
     * 当前文件管理是浏览模式，还是选择模式，true为选择模式
     */
    private boolean isCheckPattern = false;
    private int checkedItemCount = 0;
    private ArrayList<String> file_list;

    private PopupMenu popupMenu_more;
    private TableRow file_menu_table;
    private LinearLayout file_menu_more_layout;

    private TextView file_menu_send;
    private TextView file_menu_cut;
    private TextView file_menu_copy;
    private TextView file_menu_delete;

    private InputNameDialog input_FolderName_ByCreateFolder;
    private InputNameDialog input_FileName_ByReName;

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

        t = new T(getBaseContext());

        folderChooser=new ChooseFolderDialog(MainActivity.this);

        initFile();
        setMenuList();
    }

    /**
     * 设置下部菜单栏
     */
    private void setMenuList() {
        file_menu_send = findViewById(R.id.file_menu_item_send);
        file_menu_cut = findViewById(R.id.file_menu_item_cut);
        file_menu_copy = findViewById(R.id.file_menu_item_copy);
        file_menu_delete = findViewById(R.id.file_menu_item_delete);
        Button create_new_folder = findViewById(R.id.create_new_folder);
        create_new_folder.setText("新建文件夹");
        TextView file_menu_more = findViewById(R.id.file_menu_item_more);
        file_menu_table = findViewById(R.id.file_menu_table);
        file_menu_more_layout = findViewById(R.id.file_menu_more_layout);

        content_main_layout = findViewById(R.id.content_main_layout);
        table_menu_layout = findViewById(R.id.table_menu_layout);
        table_menu_layout_replace = findViewById(R.id.file_menu_table_replace);

        file_menu_table.removeView(file_menu_more_layout);
        content_main_layout.removeView(table_menu_layout);

        setFile_More_Menu(file_menu_more);

        file_menu_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareFiles();
            }
        });

        file_menu_cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!folderChooser.isShowing())
                    openFolderChooser(MainActivity.this, file_cut_listener);
            }
        });

        file_menu_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFolderChooser(MainActivity.this, file_copy_listener);
            }
        });

        file_menu_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AffirmDialog.sureDel(MainActivity.this, delFiles_sure_listener, delFiles_cancel_listener);

            }
        });

        create_new_folder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFolder();
            }
        });

    }

    /**
     * 设置“更多”菜单栏中的其他菜单
     *
     * @param file_menu_more “更多”菜单栏
     */
    private void setFile_More_Menu(TextView file_menu_more) {
        popupMenu_more = new PopupMenu(this, file_menu_more);
        Menu menu_more = popupMenu_more.getMenu();
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_file_more, menu_more);
        popupMenu_more.setOnMenuItemClickListener(this::onFileMore);
        file_menu_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu_more.show();
            }
        });
    }

    /**
     * 设置三个item的监听事件
     *
     * @param menuItem menu的item，分别是详情、重命名、取消
     * @return ，
     */
    private boolean onFileMore(MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.file_menu__item_rename) {
            rename();
        } else if (id == R.id.file_menu_item_detail) {
            loadDetails(file_list.get(0));
        } else if (id == R.id.file_menu_item_cancel) {
            popupMenu_more.dismiss();
        }
        return true;
    }

    /**
     * 创建文件夹输入文件夹名称后的监听事件，
     * 是调用 createNewFolder() 方法中的监听事件
     */
    private View.OnClickListener createFolderListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == R.id.btn_input_name_sure) {
                String name = input_FolderName_ByCreateFolder.text_name.getText().toString();
                if (FileTools.isLegal_FileName(name)) {
                    boolean state = FileTools.createNewFolder(present_path, name);
                    if (state) {
                        t.tips("创建成功");
                        String path = present_path + "/" + name;
                        File file = new File(path);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.folder);
                        long size = file.length();
                        long last_time = file.lastModified();
                        FileData new_folder = new FileData(name, path, bitmap, size, last_time, false);
                        fileAdapter.addData(new_folder);
                        input_FolderName_ByCreateFolder.dismiss();
                    } else {
                        t.error("创建失败,文件夹可能已经存在");
                    }
                } else {
                    t.error("文件名不合法：文件名中不能包括以下字符：\n\\/:*?\"<>|,");
                }
            }
            if (id == R.id.btn_input_new_name_cancel) {
                input_FolderName_ByCreateFolder.dismiss();
            }
        }
    };

    /**
     * 重命名时输入新的文件名称后的监听事件，
     * 是调用 rename() 方法中药用到的监听事件
     */
    private View.OnClickListener rename_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_input_name_sure: {
                    String new_name = input_FileName_ByReName.text_name.getText().toString();
                    if (FileTools.isLegal_FileName(new_name)) {
                        if (FileTools.isSameFile_inDir(present_path, new_name)) {
                            if (FileTools.renameFile(file_list.get(0), new_name)) {
                                t.tips("命名成功");
                                fileAdapter.amendData(file_list.get(0), new_name);
                                setShowPattern();
                                input_FileName_ByReName.dismiss();
                            } else {
                                t.error("命名失败");
                            }
                        } else {
                            t.error("该目录下已存在");
                        }
                    } else {
                        t.error("命名不合法");
                    }
                    break;
                }
                case R.id.btn_input_new_name_cancel: {
                    input_FileName_ByReName.dismiss();
                    break;
                }
            }
        }
    };

    /**
     * 删除文件时确定删除的监听事件
     */
    private DialogInterface.OnClickListener delFiles_sure_listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            for (String file : file_list) {
                if (FileTools.deleteFile(file)) {
                    fileAdapter.delData(file);
                    t.tips("删除成功");
                } else {
                    t.error("删除失败,暂不支持删除有文件的目录");
                }
            }
            setShowPattern();
        }
    };
    /**
     * 删除文件时取消删除的监听事件
     */
    private DialogInterface.OnClickListener delFiles_cancel_listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            setShowPattern();
        }
    };


    /**
     * 新建文件夹
     */
    private void createNewFolder() {
        String initialize = "新建文件夹";
        input_FolderName_ByCreateFolder =
                new InputNameDialog(MainActivity.this, createFolderListener, initialize);
        input_FolderName_ByCreateFolder.show();
    }

    /**
     * 分享文件（夹）单个或多个
     */
    private void shareFiles() {
        ArrayList<Uri> uris = new ArrayList<>();
        for (int i = 0; i < file_list.size(); i++) {
            File file = new File(file_list.get(i));
            Uri u = Uri.fromFile(file);
            uris.add(u);
        }
        boolean multiple = uris.size() > 1;
        Intent intent = new Intent(multiple ? android.content.Intent.ACTION_SEND_MULTIPLE
                : android.content.Intent.ACTION_SEND);

        if (multiple) {
            intent.setType("*/*");
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        } else {
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
        }
        startActivity(Intent.createChooser(intent, "分享到"));
        setShowPattern();
    }

    /**
     * 文件重命名时的监听事件
     */
    private void rename() {
        String path = file_list.get(0);
        String name = new File(path).getName();
        input_FileName_ByReName = new InputNameDialog(MainActivity.this, rename_listener, name);
        input_FileName_ByReName.show();
    }

    /**
     * 在选择文件夹后，对文件进行剪切时发生的监听事件
     */
    private View.OnClickListener file_cut_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String path = folderChooser.present_path;
            switch (v.getId()){
                case R.id.choose_folder:{
                    if (!path.equals(present_path)) {
                        for (String file : file_list) {
                            if (FileTools.isSameFile_inDir(path, file)) {
                                if (FileTools.cutFile(file, path)) {
                                    t.tips("剪切成功");
                                    fileAdapter.delData(file);
                                } else {
                                    t.error("剪切失败，暂不支持剪切有文件的目录");
                                }
                            } else {
                                t.tips("该目录下已有相同文件");
                            }
                        }
                        setShowPattern();
                        folderChooser.dismiss();
                    } else {
                        t.tips("目录未更改，请重新选择");
                    }
                    break;
                }
                case R.id.choose_folder_cancel:{
                    folderChooser.dismiss();
                }
            }

        }
    };

    /**
     * 在选择文件夹后，对文件进行复制时发生的监听事件
     */
    private View.OnClickListener file_copy_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.choose_folder) {
                String path = folderChooser.present_path;
                if (!path.equals(present_path)) {
                    for (String file : file_list) {
                        if (FileTools.isSameFile_inDir(path, file)) {
                            if (FileTools.copyFile(file, path)) {
                                t.tips("复制成功");
                            } else {
                                t.error("复制失败，暂不支持复制有文件的目录");
                            }
                        } else {
                            t.tips("该目录下已有相同文件");
                        }
                    }
                    setShowPattern();
                    folderChooser.dismiss();

                } else {
                    t.tips("目录未更改，请重新选择");
                }
            } else if (v.getId() == R.id.choose_folder_cancel) {
                folderChooser.dismiss();
            }
        }
    };


    /**
     * 调用文件夹选择器dialog
     *
     * @param activity        获取Activity
     * @param onClickListener 监听事件
     */
    private void openFolderChooser(Activity activity, View.OnClickListener onClickListener) {
        folderChooser = new ChooseFolderDialog(activity, onClickListener);
        folderChooser.show();
    }

    /**
     * 首次启动 Activity 时初始化并加载数据
     */
    public void initFile() {

        //初始化APP的临时文件储存位置、APP配置文件位置
        ResourceManager.createAppPath(ResourceManager.App_Path);
        ResourceManager.createAppPath(ResourceManager.App_Temp_Image_Path);
        ResourceManager.createAppPath(ResourceManager.App_Temp_Video_Image_Path);

        present_path = rootPath;

        file_recycler = findViewById(R.id.file_list);
        linearLayoutManager = new LinearLayoutManager(this);
        file_recycler.setLayoutManager(linearLayoutManager);
        file_recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        show_device = findViewById(R.id.file_device);

        getFileDir(rootPath, false);
    }

    /**
     * 取得文件架构的method
     *
     * @param parent_file_path 文件当前目录
     * @param isParent         当前操作是否为点击“返回上一级”
     */
    public void getFileDir(String parent_file_path, boolean isParent) {

        new FileTools(MainActivity.this, rootPath);

        ArrayList<FileData> allFile = getFileList(parent_file_path);

        loadData(allFile, isParent);

    }

    /**
     * @param data     {@link ArrayList<FileData>} 进行渲染item
     * @param isParent 当前操作是否为点击“返回上一级”
     */
    private void loadData(@NonNull ArrayList<FileData> data, boolean isParent) {


        show_device.removeAllViews();
        new DeviceShow(MainActivity.this, show_device, rootPath, present_path);

        if (isParent) {
            int position = FileTools.getPositionInParentFolder(data, new File(previous_path));
            // 通过 LayoutManager 的 srcollToPositionWithOffset 方法进行定位
            linearLayoutManager.scrollToPositionWithOffset(position, 0);
        }

        fileAdapter = new FileAdapter(MainActivity.this, data);
        file_recycler.setAdapter(fileAdapter);

        addListener();
    }

    /**
     * 为适配器添加监听事件
     */
    private void addListener() {
        fileAdapter.setOnClickItem(new FileAdapter.onClickItem() {
            @Override
            public void onClick(ArrayList<FileData> fileData, int position) {
                String path = fileData.get(position).getPath();
                String name = fileData.get(position).getName();

                if (!isCheckPattern) {
                    openDirectory_or_openFile(path, name);
                } else {
                    selectFiles(fileData, position, name);
                }

            }
        });
        fileAdapter.setOnLongClickItem(new FileAdapter.onLongClickItem() {
            @Override
            public void onClick(ArrayList<FileData> fileData, int position) {
                String name = fileData.get(position).getName();

                if (!name.equals("<<previous>>")) {
                    if (!isCheckPattern) {
                        setSelectPattern(fileData, position);
                    }
                }
            }
        });
    }

    /**
     * 选择（多个）文件（夹）
     *
     * @param fileData 当前目录下的文件信息
     * @param position 选择文件的当前 position
     * @param name     获取文件结构时定义的文件名
     */
    private void selectFiles(ArrayList<FileData> fileData, int position, String name) {
        boolean isChecked = fileData.get(position).isCheck();
        if (!name.equals("<<previous>>")) {
            if (isChecked) {
                fileAdapter.delCheckedItem(position);
                file_list.remove(fileData.get(position).getPath());
                checkedItemCount--;
                for (String path : file_list) {
                    Log.d("选中的文件" + checkedItemCount + "个", path);
                }
            } else {
                fileAdapter.addCheckedItem(position);
                file_list.add(fileData.get(position).getPath());
                checkedItemCount++;
                for (String path : file_list) {
                    Log.d("选中的文件" + checkedItemCount + "个", path);
                }
            }
            if (checkedItemCount == 0) {
                file_menu_send.setEnabled(false);
                file_menu_cut.setEnabled(false);
                file_menu_copy.setEnabled(false);
                file_menu_delete.setEnabled(false);
                file_menu_table.removeView(file_menu_more_layout);
            } else if (checkedItemCount == 1) {
                file_menu_send.setEnabled(true);
                file_menu_cut.setEnabled(true);
                file_menu_copy.setEnabled(true);
                file_menu_delete.setEnabled(true);
                file_menu_table.addView(file_menu_more_layout);
            } else if (checkedItemCount > 1) {
                file_menu_table.removeView(file_menu_more_layout);
            }
        }

    }

    /**
     * 打开目录或者打开文件
     *
     * @param path 文件地址
     * @param name 文件名称
     */
    private void openDirectory_or_openFile(String path, String name) {
        File file = new File(path);
        String parentPath = file.getParent();
        if (name.equals("<<previous>>")) {
            present_path = parentPath;
            previous_path = path;
            getFileDir(present_path, true);
        } else {
            if (file.isDirectory()) {
                present_path = path;
                getFileDir(path, false);
            } else {
                FileTools.openFile(file, MainActivity.this);
            }
        }
    }

    /**
     * 打开一个新的 Activity ，显示文件信息
     *
     * @param file_path 文件路径
     */
    private void loadDetails(String file_path) {
        Intent details = new Intent(MainActivity.this, FileDetailsActivity.class);
        details.putExtra("path", file_path);
        startActivity(details);
    }

    /**
     * 首次切换到文件选择状态
     *
     * @param fileData 当前文件信息集合
     * @param position 当前点击文件的 position
     */
    private void setSelectPattern(ArrayList<FileData> fileData, int position) {
        file_list = new ArrayList<>();
        fileAdapter.showCheckBox(position);
        isCheckPattern = true;
        checkedItemCount = 1;
        content_main_layout.removeView(table_menu_layout_replace);
        content_main_layout.addView(table_menu_layout);
        file_menu_table.addView(file_menu_more_layout);
        file_list.add(fileData.get(position).getPath());
    }

    /**
     * 恢复到文件展示状态，即浏览目录状态
     */
    private void setShowPattern() {
        isCheckPattern = false;
        fileAdapter.hiddenCheckBox();
        checkedItemCount = 0;
        file_list.clear();
        file_menu_table.removeView(file_menu_more_layout);
        content_main_layout.removeView(table_menu_layout);
        content_main_layout.addView(table_menu_layout_replace);
    }


    private long firstTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isCheckPattern) {
                if (!present_path.equals(rootPath)) {
                    //返回上一级，当前目录present_path将作为之前访问的目录previous_path,
                    // 因此，当前目录present_path应该是当前目录上一级的目录new File(present_path).getParent()
                    previous_path = present_path;
                    present_path = new File(present_path).getParent();
                    getFileDir(present_path, true);
                } else {
                    long secondTime=System.currentTimeMillis();
                    if(secondTime-firstTime>1000){
                        t.tips("再按一次退出");
                        firstTime=secondTime;
                    }
                    else{
                        System.exit(0);
                    }
                }
            } else {
                setShowPattern();
            }
        }
        return true;
    }
}
