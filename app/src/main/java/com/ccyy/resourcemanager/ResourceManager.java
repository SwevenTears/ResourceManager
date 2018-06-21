package com.ccyy.resourcemanager;

import java.io.File;

/**
 * Created by Sweven on 2018/6/18.
 * Email:sweventears@Foxmail.com
 */
public class ResourceManager {


    /**
     * APP根目录
     */
    public final static String App_Path = "/sdcard/TCYResourceManager";

    /**
     * APP视频缩略图保存地址
     */
    public final static String App_Temp_Video_Image_Path = "/sdcard/TCYResourceManager/video";

    /**
     * APP图像缩略图保存地址
     */
    public final static String App_Temp_Image_Path = "/sdcard/TCYResourceManager/image";

    /**
     * @param path 文件目录
     *             创建 APP的根目录
     */
    public static void createAppPath(String path) {
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
    }
}
