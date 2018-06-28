package com.ccyy.resourcemanager.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Sweven on 2018/6/11.
 * Email:sweventears@Foxmail.com
 */
public class T {

    private Context context;

    public T(Context context) {
        this.context = context;
    }

    /**
     * @param error 错误提示内容
     */
    public void error(String error) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param tips 正常提示内容
     */
    public void tips(String tips) {
        Toast.makeText(context, tips, Toast.LENGTH_SHORT).show();
    }
}
