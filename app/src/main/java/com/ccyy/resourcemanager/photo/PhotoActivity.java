package com.ccyy.resourcemanager.photo;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;

import com.ccyy.resourcemanager.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


@SuppressWarnings("ALL")
public class PhotoActivity extends AppCompatActivity {

    private HashMap<String, List<ImageBean>> mGruopMap = new HashMap<String, List<ImageBean>>();
    private List<ImageBean> imgs = new ArrayList<>();
    private final static int SCAN_OK = 1;
    private ProgressDialog mProgressDialog;
    private RecyclerView rePhotoWall;
    private PhotoWallAdapter adapter;
    private ImageView imageView;
    private ImageView mImageView;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_item);

        initView();
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    99);
            return;
        }
        //作用：设置界面布局
    }

    private void initView(){
        rePhotoWall=findViewById(R.id.recycler_view); //资源ID
        //设置RecyclerView为网格布局 3列
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rePhotoWall.setLayoutManager(layoutManager);
        adapter=new PhotoWallAdapter(PhotoActivity.this,imgs);
        rePhotoWall.setAdapter(adapter);
        if (imgs.size()==0)
            getImages();
    }
    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     * 防止线程阻塞
     */
    private void getImages() {
        //显示进度dialog
        mProgressDialog = ProgressDialog.show(this, null, "正在加载。。。。。");
        //开启线程扫描
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = PhotoActivity.this.getContentResolver();
                //只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_TAKEN);
                if (mCursor == null) {
                    return;
                }
                while (mCursor.moveToNext()) {
                    //获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                    ImageBean bean = new ImageBean(path, false);
                    imgs.add(bean);
                    //获取该图片的父路径名
                    String parentName = new File(path).getParentFile().getName();
                    //根据父路径名将图片放入到mGruopMap中
                    if (!mGruopMap.containsKey(parentName)) {
                        List<ImageBean> childList = new ArrayList<ImageBean>();
                        ImageBean imageBean = new ImageBean(path, false);
                        childList.add(imageBean);
                        mGruopMap.put(parentName, childList);
                    } else {
                        mGruopMap.get(parentName).add(new ImageBean(path, false));
                    }
                }
                //添加全部图片的集合
                mGruopMap.put("全部图片", imgs);
                //通知Handler扫描图片完成
                mhandler.sendEmptyMessage(SCAN_OK);
                mCursor.close();
            }
        }).start();
    }

    Handler mhandler=new Handler(){
        /**
         * Subclasses must implement this to receive messages.
         *
         * @param msg
         *Handler 可以根据 Message 中的 what 值的不同来分发处理
         */
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SCAN_OK:

                    //扫描完毕,关闭进度dialog
                    mProgressDialog.dismiss();
                    adapter.notifyDataSetChanged();
                    break;

            }
        }
    };
    /**
     * 组装分组界面的数据源，因为我们扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     * 用于展示图库分组列表
     * @param mGruopMap
     * @return
     */
    private  List<ImageGroupBean> subGroupOfImage(HashMap<String, List<ImageBean>> mGruopMap) {
        if (mGruopMap.size() == 0) {
            return null;
        }
        //遍历
        List<ImageGroupBean> list = new ArrayList<ImageGroupBean>();
        Iterator<Map.Entry<String, List<ImageBean>>> it = mGruopMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<ImageBean>> entry = it.next();
            ImageGroupBean mImageBean = new ImageGroupBean();

            //根据key获取其中图片list
            String key = entry.getKey();
            List<ImageBean> value = entry.getValue();

            mImageBean.setFolderName(key);//获取该组文件夹名称
            mImageBean.setImageCounts(value.size());//获取该组图片数量
            mImageBean.setTopImagePath(value.get(0).getImgPath());//获取该组的第一张图片
            //将全部图片放在第一位置
            if (mImageBean.getFolderName().equals("全部图片")){
                list.add(0,mImageBean);
            }else {
                list.add(mImageBean);
            }
        }
        return list;
    }
}

