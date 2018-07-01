package com.ccyy.resourcemanager.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Sweven on 2018/6/14.
 * Email:sweventears@Foxmail.com
 */
public class DeviceShow {

    private String rootPath;

    private onClickItem mListener;

    /**
     * @param context      MainActivity.this
     * @param show_device  容器
     * @param rootPath     根目录
     * @param present_path 当前目录
     * @method 导航条制作
     */
    @SuppressLint("SetTextI18n")
    public DeviceShow
    (Context context, LinearLayout show_device, String rootPath, String present_path) {
        String parent_path_show;
        this.rootPath = rootPath;
        // 将手机内存目录替换成可读文字
        parent_path_show = present_path.replace(rootPath, "手机内存");

        // 将目录分隔开进行渲染
        String parent_path_shows[] = parent_path_show.split("/");
        int i = 0;
        // 代码添加 TextView 作导航用
        for (final String temp : parent_path_shows) {
            TextView link_btn = new TextView(context);

            if (i < parent_path_shows.length - 1)
                link_btn.setText(temp + ">");
            else
                link_btn.setText(temp);
            // 为每一个目录做监听事件
            link_btn.setOnClickListener(new Jump_path(i, parent_path_shows));
            show_device.addView(link_btn);
            i++;
        }
    }

    /**
     * 导航条每个TextView的监听事件
     */
    class Jump_path implements View.OnClickListener {

        private String jump_path;

        /**
         * 给每一个 TextView 设置全路径，以便于直接跳转
         * @param position 点击第 position 个 TextView
         * @param files 最深目录分离集合->
         *             如：123//12//1 -> {"123", "12", "1"}
         */
        Jump_path(int position, String files[]) {
            for (int i = 0; i <= position; i++) {
                if (i == 0) {
                    jump_path = files[i];
                } else {
                    jump_path = String.format("%s%s", jump_path, "/" + files[i]);
                }
            }
            jump_path = jump_path.replace("手机内存", rootPath);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onClick(jump_path);
            }
        }
    }

    public interface onClickItem {
        void onClick(String jump_path);
    }

    public void setOnClickItem(onClickItem onClickItem) {
        this.mListener = onClickItem;
    }
}
