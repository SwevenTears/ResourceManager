package com.ccyy.resourcemanager.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ccyy.resourcemanager.R;
import com.ccyy.resourcemanager.main.DeviceShow;
import com.ccyy.resourcemanager.main.FileData;
import com.ccyy.resourcemanager.main.FileTools;
import com.ccyy.resourcemanager.tools.FileOperation;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Sweven on 2018/6/25.
 * Email:sweventears@Foxmail.com
 */
public class ChooseFolderDialog extends Dialog {

    private Activity activity;
    private View.OnClickListener mOnclick;

    private Button btn_choose;
    private Button btn_cancel;
    private LinearLayout show_folder_device;
    private RecyclerView folder_recycler;

    private String rootPath = FileOperation.getMobilePath();
    private LinearLayoutManager linearLayoutManager;
    private FolderAdapter folderAdapter;
    public String present_path;
    private String previous_path;

    public ChooseFolderDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    public ChooseFolderDialog(@NonNull Activity activity, View.OnClickListener onclick) {
        super(activity, R.style.AppTheme);
        this.activity = activity;
        this.mOnclick = onclick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choose_folder);

        btn_choose = findViewById(R.id.choose_folder);
        btn_cancel = findViewById(R.id.choose_folder_cancel);
        show_folder_device = findViewById(R.id.folder_device);


        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();//获取dialog的长宽
        params.width = 400;
        params.height = 900;

        btn_choose.setOnClickListener(mOnclick);
        btn_cancel.setOnClickListener(mOnclick);

        folder_recycler = findViewById(R.id.folder_list);
        linearLayoutManager = new LinearLayoutManager(activity);
        folder_recycler.setLayoutManager(linearLayoutManager);
        folder_recycler.addItemDecoration(new DividerItemDecoration(activity, DividerItemDecoration.VERTICAL));

        present_path = rootPath;
        getFolderDir(rootPath, false);
    }

    /**
     * @param present_path 当前点击的文件夹
     * @param isParent     是够点击的“上一层”按钮
     */
    private void getFolderDir(String present_path, boolean isParent) {
        ArrayList<FileData> folders = getFolderList(present_path);
        loadItem(folders, isParent);
    }

    /**
     * 渲染adapter
     *
     * @param folders  文件夹数据
     * @param isParent 是否点击“上一层目录”
     */
    private void loadItem(ArrayList<FileData> folders, boolean isParent) {

        show_folder_device.removeAllViews();
        new DeviceShow(activity, show_folder_device, rootPath, present_path);

        if (isParent) {
            int position = FileTools.getPositionInParentFolder(folders, new File(previous_path));
            // 通过 LayoutManager 的 srcollToPositionWithOffset 方法进行定位
            linearLayoutManager.scrollToPositionWithOffset(position, 0);
        }
        folderAdapter = new FolderAdapter(activity, folders);
        folder_recycler.setAdapter(folderAdapter);

        addListener();
    }

    /**
     * 添加文件夹点击监听事件
     */
    private void addListener() {
        folderAdapter.setOnClickItem(new FolderAdapter.onClickItem() {
            @Override
            public void onClick(ArrayList<FileData> fileData, int position) {
                String name = fileData.get(position).getName();
                String path = fileData.get(position).getPath();

                File file = new File(path);
                String parentPath = file.getParent();

                if (name.equals("<<previous>>")) {
                    present_path = parentPath;
                    previous_path = path;
                    getFolderDir(parentPath, true);
                } else {
                    present_path = path;
                    getFolderDir(path, false);
                }
            }
        });
    }

    /**
     * 取得文件架构的method
     *
     * @param filePath 文件当前目录
     * @return 当前目录的文件列表
     */
    public ArrayList<FileData> getFolderList(String filePath) {


        File f = new File(filePath);
        //找到f下的所有文件的列表
        File[] files = f.listFiles();

        ArrayList<FileData> allFolder = new ArrayList<>(), allFile = new ArrayList<>();

        boolean isRoot = false;

        if (!filePath.equals(rootPath)) {
            FileData fileData = new FileData("<<previous>>", f.getPath());
            allFolder.add(fileData);
            isRoot = true;
        }

        for (File temp : files) {
            if (!temp.isHidden()) {
                if (temp.isDirectory()) {
                    String temp_path = temp.getPath();
                    String temp_name = temp.getName();
                    FileData fileData = new FileData(temp_name, temp_path);
                    allFolder.add(fileData);
                }
            }
        }

        ArrayList<FileData> order_allFolder = FileOperation.order(allFolder, isRoot);
        ArrayList<FileData> order_allFile = FileOperation.order(allFile, false);

        ArrayList<FileData> order_file = order_allFolder;
        order_file.addAll(order_allFile);

        return order_file;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!present_path.equals(rootPath)) {
                //返回上一级，当前目录present_path将作为之前访问的目录previous_path,
                // 因此，当前目录present_path应该是当前目录上一级的目录new File(present_path).getParent()
                previous_path = present_path;
                present_path = new File(present_path).getParent();
                getFolderDir(present_path, true);
            } else {
                dismiss();
            }
        }
        return true;
    }
}
