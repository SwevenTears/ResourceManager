package com.ccyy.resourcemanager.main;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by Sweven on 2018/6/18.
 * Email:sweventears@Foxmail.com
 */
public class FileTools {

    /**
     * @param path 文件地址
     * @return intent 将文件分享到其他应用
     */
    public static Intent shareFile(String path){
        Intent share=new Intent(Intent.ACTION_SEND);
        share.setType("*/*");
        share.putExtra(Intent.EXTRA_SUBJECT, "分享");
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        return share;
    }
}
