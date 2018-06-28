package com.ccyy.resourcemanager.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.ccyy.resourcemanager.R;

import java.io.File;

/**
 * Created by Sweven on 2018/6/28.
 * Email:sweventears@Foxmail.com
 */
public class SingleFileMenuDialog extends Dialog{

    private Activity activity;
    private String filePath;
    private View.OnClickListener listener= v -> {
        switch (v.getId()){
            case R.id.single_file_menu_share: {
                getFileOperation(0);
                break;
            }
            case R.id.single_file_menu_cut: {
                getFileOperation(1);
                break;
            }
            case R.id.single_file_menu_copy: {
                getFileOperation(2);
                break;
            }
            case R.id.single_file_menu_del: {
                getFileOperation(3);
                break;
            }
            case R.id.single_file_menu_detail: {
                getFileOperation(4);
                break;
            }
            case R.id.single_file_menu_rename: {
                getFileOperation(5);
                break;
            }
        }
    };

    private static final int File_share=0;
    private static final int File_cut=1;
    private static final int File_copy=2;
    private static final int File_del=3;
    private static final int File_detail=4;
    private static final int File_rename=5;

    public SingleFileMenuDialog(@NonNull Activity activity, String path, View.OnClickListener mListener) {
        super(activity);
        this.activity=activity;
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

    private boolean renameFile() {
        File file=new File(filePath);
        String parentDir=file.getParent();
        String name=file.getName();
        return false;
    }

    private boolean detailFile() {
        File file=new File(filePath);
        String parentDir=file.getParent();
        String name=file.getName();
        return false;
    }

    private boolean delFile() {
        File file=new File(filePath);
        String parentDir=file.getParent();
        String name=file.getName();
        return false;
    }

    private boolean copyFile() {
        File file=new File(filePath);
        String parentDir=file.getParent();
        String name=file.getName();
        return false;
    }

    private boolean cutFile() {
        File file=new File(filePath);
        String parentDir=file.getParent();
        String name=file.getName();
        return false;
    }

    private boolean shareFile() {
        File file=new File(filePath);
        String parentDir=file.getParent();
        String name=file.getName();
        return false;
    }
}
