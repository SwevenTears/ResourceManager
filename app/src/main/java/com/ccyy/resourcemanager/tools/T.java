package com.ccyy.resourcemanager.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Sweven on 2018/6/11.
 * Email:sweventears@Foxmail.com
 */
public class T {
    
    public static void error(Context context,String error){
        Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
    }

    public static void tips(Context context,String tips){
        Toast.makeText(context,tips,Toast.LENGTH_SHORT).show();
    }
}
