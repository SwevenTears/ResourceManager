package com.ccyy.resourcemanager.main;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ccyy.resourcemanager.MainActivity;
import com.ccyy.resourcemanager.R;
import com.ccyy.resourcemanager.tools.T;

import java.io.File;

/**
 * Created by Sweven on 2018/6/14.
 * Email:sweventears@Foxmail.com
 */
public class DeviceShow {

    private String rootPath;

    private static T t;

    /**
     * @param context     MainActivity.this
     * @param show_device  容器
     * @param rootPath     根目录
     * @param present_path 当前目录
     * @method 导航条制作
     */
    public DeviceShow
    (Context context, LinearLayout show_device, String rootPath, String present_path) {
        String parent_path_show;
        this.rootPath = rootPath;
        parent_path_show = present_path.replace(rootPath, "手机内存");

        String parent_path_shows[] = parent_path_show.split("/");
        int i = 0;
        for (final String temp : parent_path_shows) {
            TextView link_btn = new TextView(context);

            if (i < parent_path_shows.length - 1)
                link_btn.setText(temp + ">");
            else
                link_btn.setText(temp);
            link_btn.setOnClickListener(new Jump_path(context, temp, i, parent_path_shows));
            show_device.addView(link_btn);
            i++;
        }

        t = new T(context);

    }

    class Jump_path implements View.OnClickListener {

        private Context context;
        private String jump_path;

        public Jump_path(Context context, String temp, int position, String files[]) {
            this.context = context;
            for (int i = 0; i <= position; i++) {
                if (i == 0)
                    jump_path = files[i];
                else
                    jump_path = jump_path + "/" + files[i];
            }
            jump_path = jump_path.replace("手机内存", rootPath);
        }

        @Override
        public void onClick(View v) {
            t.tips(jump_path);
        }
    }
}
