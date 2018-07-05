package com.ccyy.resourcemanager.tools;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import com.ccyy.resourcemanager.main.FileData;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Sweven on 2018/6/14.
 * Email:sweventears@Foxmail.com
 */
public class FileOperation {


    /**
     * @param list   需要进行排序的数据
     * @param isRoot 第一个数据是否需要进行排序（针对有返回上一级的子目录）
     * @return 经过排序后的 {@link ArrayList < FileData >} 数据
     */
    public static ArrayList<FileData> order(ArrayList<FileData> list, boolean isRoot) {
        ArrayList<FileData> new_list = new ArrayList<>();
        String names[] = new String[list.size()];
        int n = 0;
        if (isRoot) {
            n = 1;
            new_list.add(list.get(0));
            names[0] = "///";
        }
        for (int i = n; i < list.size(); i++) {
            names[i] = list.get(i).getName();
        }
        Comparator china = Collator.getInstance(Locale.CHINA);
        Arrays.sort(names, china);
        for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < names.length; j++) {
                if (names[i].equals(list.get(j).getName())) {
                    new_list.add(new FileData(names[i], list.get(j).getPath(),
                            list.get(j).getFileIcon(), list.get(j).getLast_date(),
                            list.get(j).getSize(), list.get(j).isCheck()));
                }
            }
        }
        return new_list;
    }

    /**
     * @param child_folder_name   需要查找的子目录文件夹名称
     * @param parent_folder_files 父目录文件夹的文件集合
     * @return position child_folder_name在父文件夹的位置
     */
    public static int find_folder_position(String child_folder_name, ArrayList<String> parent_folder_files) {
        int position = 0;

        for (int i = 0; i < parent_folder_files.size(); i++) {
            String child = parent_folder_files.get(i);
            if (child_folder_name.equals(child)) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * @return 获取手机内置储存卡位置
     */
    public static String getMobilePath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取手机根目录
        }
        assert sdDir != null;
        return sdDir.toString();
    }

    /**
     * @return 获取所有外置SD卡位置
     */
    public static String[] getExtraSDPath(Context context) {
        String[] str = FileOperation.getVolumePaths(context);
        ArrayList<String> dirList=new ArrayList<>();
        for (String dir : str) {
            File file = new File(dir);
            long total_size = file.getTotalSpace();
            if (total_size>0){
                dirList.add(dir);
            }
        }
        ArrayList<String> extraSDs=new ArrayList<>();
        for (String dir:dirList){
            if (!dir.equals(FileOperation.getMobilePath())){
                extraSDs.add(dir);
            }
        }
        if (!extraSDs.isEmpty()) {
            String[] dirs=new String[extraSDs.size()];
            for (int i=0;i<extraSDs.size();i++){
                dirs[i]=extraSDs.get(i);
            }
            return dirs;
        }
        return new String[]{"<<no_find>>"};
    }

    /**
     * @param context ，
     * @return 是否存在外置储存卡
     */
    public static boolean hasExtraSD(Context context){
        String[] str = FileOperation.getVolumePaths(context);
        ArrayList<String> dirList=new ArrayList<>();
        for (String dir : str) {
            File file = new File(dir);
            long total_size = file.getTotalSpace();
            if (total_size>0){
                dirList.add(dir);
            }
        }
        ArrayList<String> extraSDs=new ArrayList<>();
        for (String dir:dirList){
            if (!dir.equals(FileOperation.getMobilePath())){
                extraSDs.add(dir);
            }
        }
        return !extraSDs.isEmpty();
    }

    public static String[] getVolumePaths(Context context) {
        String[] paths = null;
        StorageManager mStorageManager;
        try {
            mStorageManager = (StorageManager) context.getSystemService(Activity.STORAGE_SERVICE);
            Method mMethodGetPaths;
            mMethodGetPaths = mStorageManager.getClass()
                    .getMethod("getVolumePaths");
            paths = (String[]) mMethodGetPaths.invoke(mStorageManager);
        } catch (IllegalArgumentException ignored) {
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return paths;
    }

}
