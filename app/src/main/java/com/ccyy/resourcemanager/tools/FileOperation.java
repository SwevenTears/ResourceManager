package com.ccyy.resourcemanager.tools;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.ccyy.resourcemanager.main.FileData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
     * @param list 需要进行排序的数据
     * @param isRoot 第一个数据是否需要进行排序（针对有返回上一级的子目录）
     * @return 经过排序后的 {@link ArrayList < FileData >} 数据
     */
    public static ArrayList<FileData> order(ArrayList<FileData> list,boolean isRoot){
        ArrayList<FileData> new_list=new ArrayList<>();
        String names[]=new String[list.size()];
        String paths[]=new String[list.size()];
        int n=0;
        if(isRoot) {
            n = 1;
            new_list.add(new FileData(list.get(0).getName(),list.get(0).getPath()));
            names[0]="///";
        }
        for (int i=n;i<list.size();i++){
            names[i]=list.get(i).getName();
            paths[i]=list.get(i).getPath();
        }
        Comparator china = Collator.getInstance(Locale.CHINA);
        Arrays.sort(names,china);
        for(int i=0;i<names.length;i++){
            for(int j=0;j<paths.length;j++){
                if(names[i].equals(list.get(j).getName())){
                    new_list.add(new FileData(names[i],paths[j]));
                }
            }
        }
        return new_list;
    }

    /**
     * @param parent_folder_path 需要查询文件数量的文件夹名称
     * @return 当前文件夹的所有文件数量、文件夹数量和文件数量
     */
    public static int[] get_FolderCount_FileCount(String parent_folder_path){
        int count[]={0,0,0};
        File parent_folder=new File(parent_folder_path);
        File[] parents=parent_folder.listFiles();
        for(int i=0;i<parents.length;i++){
            File child=parents[0];
            if(child.isDirectory())
                count[1]++;
            else
                count[2]++;
            count[0]++;
        }

        return count;
    }

    /**
     * @param child_folder_name 需要查找的子目录文件夹名称
     * @param parent_folder_files 父目录文件夹的文件集合
     * @return position child_folder_name在父文件夹的位置
     */
    public static int find_folder_position(String child_folder_name,ArrayList<String> parent_folder_files){
        int position=0;

        for(int i=0;i<parent_folder_files.size();i++){
            String child=parent_folder_files.get(i);
            if(child_folder_name.equals(child)){
                position=i;
                break;
            }
        }
        return position;
    }

    /**
     * @return 获取手机内置储存卡位置
     */
    public static String getSDPath(){
        File sdDir =null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取根目录
        }
        assert sdDir != null;
        return sdDir.toString();
    }

    /**
     * @param path  图片的完整地址
     * @param bm bitmap图片
     * @param newWidth 需要设置的宽度
     * @param newHeight 需要设置的高度
     * @return 新的图片
     */
    public static Bitmap setImgSize(String path,Bitmap bm, int newWidth , int newHeight){
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        OutputStream out= null;
        try {
            out = new FileOutputStream(new File(path));
        } catch (FileNotFoundException e) {
            Log.e("压缩图片进程","失败，出错");
        }
        newbm.compress(Bitmap.CompressFormat.PNG,20,out);
        return newbm;
    }

}
