package com.ccyy.resourcemanager.tools;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.ccyy.resourcemanager.R;

/**
 * Created by Sweven on 2018/6/14.
 * Email:sweventears@Foxmail.com
 */
public class AffirmDialog {

    /**
     * 删除提示
     *
     * @param context
     */
    public static void sureDel(Context context, DialogInterface.OnClickListener btn_sure, DialogInterface.OnClickListener btn_cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_launcher_background);
        builder.setTitle("删除提示");
        builder.setMessage("删除后无法撤销，是否继续？");
        builder.setPositiveButton("继续", btn_sure);
        builder.setNegativeButton("取消", btn_cancel);
        builder.show();
    }

}
