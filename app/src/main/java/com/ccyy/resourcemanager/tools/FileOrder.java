package com.ccyy.resourcemanager.tools;

import android.util.Log;

import com.ccyy.resourcemanager.main.FileData;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Sweven on 2018/6/14.
 * Email:sweventears@Foxmail.com
 */
public class FileOrder {


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
            names[0]="Root";
        }
        for (int i=n;i<list.size();i++){
            names[i]=list.get(i).getName();
            paths[i]=list.get(i).getPath();
        }
        Comparator china = Collator.getInstance(Locale.CHINA);
        Arrays.sort(names,china);
        for(int i=0;i<names.length;i++){
            Log.i("第一个文件：",names[0]);
            for(int j=0;j<paths.length;j++){
                if(names[i].equals(list.get(j).getName())){
                    new_list.add(new FileData(names[i],paths[j]));
                }
            }
        }
        return new_list;
    }
}
