package com.ccyy.resourcemanager;

import java.io.File;

/**
 * Created by Sweven on 2018/6/18.
 * Email:sweventears@Foxmail.com
 */
public class ResourceManager {

    public final static String App_Path = "/sdcard/TCYResourceManager";

    public final static String App_Temp_Video_Image_Path = "/sdcard/TCYResourceManager/video";

    public final static String App_Temp_Image_Path = "/sdcard/TCYResourceManager/image";

    public static void createAppPath(String path){
        File file=new File(path);
        if(!file.exists())
            file.mkdirs();
    }
}
