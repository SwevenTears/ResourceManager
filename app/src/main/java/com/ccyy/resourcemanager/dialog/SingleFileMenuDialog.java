package com.ccyy.resourcemanager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ccyy.resourcemanager.R;
import com.ccyy.resourcemanager.main.FileDetailsActivity;

import java.io.File;

/**
 * Created by Sweven on 2018/6/28.
 * Email:sweventears@Foxmail.com
 */
public class SingleFileMenuDialog extends Dialog{

    private Context context;
    private String filePath;
    private View.OnClickListener listener;
//    = v -> {
//        switch (v.getId()){
//            case R.id.single_file_menu_share: {
//                getFileOperation(0);
//                break;
//            }
//            case R.id.single_file_menu_cut: {
//                getFileOperation(1);
//                break;
//            }
//            case R.id.single_file_menu_copy: {
//                getFileOperation(2);
//                break;
//            }
//            case R.id.single_file_menu_del: {
//                getFileOperation(3);
//                break;
//            }
//            case R.id.single_file_menu_detail: {
//                getFileOperation(4);
//                break;
//            }
//            case R.id.single_file_menu_rename: {
//                getFileOperation(5);
//                break;
//            }
//        }
//    }

    private static final int File_share=0;
    private static final int File_cut=1;
    private static final int File_copy=2;
    private static final int File_del=3;
    private static final int File_detail=4;
    private static final int File_rename=5;

    public SingleFileMenuDialog(@NonNull Context context) {
        super(context);
    }

    public SingleFileMenuDialog(@NonNull Context context, String path, View.OnClickListener mListener) {
        super(context);
        this.context =context;
        this.filePath=path;
        this.listener=mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_single_file_menu);

        TextView share=findViewById(R.id.single_file_menu_share);
        TextView cut=findViewById(R.id.single_file_menu_cut);
        TextView copy=findViewById(R.id.single_file_menu_copy);
        TextView del=findViewById(R.id.single_file_menu_del);
        TextView rename=findViewById(R.id.single_file_menu_rename);
        TextView detail=findViewById(R.id.single_file_menu_detail);

        share.setOnClickListener(listener);
        cut.setOnClickListener(listener);
        copy.setOnClickListener(listener);
        del.setOnClickListener(listener);
        rename.setOnClickListener(listener);
        detail.setOnClickListener(listener);

        Window window=getWindow();
        WindowManager.LayoutParams layoutParams=window.getAttributes();
        layoutParams.width=400;
//        layoutParams.height=520;

        setTitle("文件操作");
    }

    public void getFileOperation(int file_menu_id){
        if (file_menu_id==File_share){
            shareFile();
        }
        else if(file_menu_id==File_cut){
            cutFile();
        }
        else if (file_menu_id==File_copy){
            copyFile();
        }
        else if (file_menu_id==File_del){
            delFile();
        }
        else if (file_menu_id==File_detail){
            detailFile();
        }
        else if (file_menu_id==File_rename){
            renameFile();
        }
    }

    /**
     * 为选中的文件重命名
     */
    private void renameFile() {
        File file=new File(filePath);
        String parentDir=file.getParent();
        String name=file.getName();
    }

    /**
     * 显示选中文件的详情
     */
    private void detailFile() {
        Intent showDetail = new Intent(context, FileDetailsActivity.class);
        showDetail.putExtra("path",filePath);
        context.startActivity(showDetail);
    }

    /**
     * 删除选中的文件
     */
    private void delFile() {
        File file=new File(filePath);
        file.delete();
    }

    /**
     * 复制选中的文件
     */
    private void copyFile() {
        File file=new File(filePath);
        String parentDir=file.getParent();
        String name=file.getName();
    }

    /**
     * 剪切选中的文件
     */
    private void cutFile() {
        File file=new File(filePath);
        String parentDir=file.getParent();
        String name=file.getName();
    }

    /**
     * 分享选中的文件
     */
    private void shareFile() {
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, filePath);
        intent.setType("*/*");
        context.startActivity(Intent.createChooser(intent, "分享到"));
    }
}
