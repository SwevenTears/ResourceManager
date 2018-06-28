package com.ccyy.resourcemanager.photo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ccyy.resourcemanager.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoWallAdapter extends RecyclerView.Adapter<PhotoWallAdapter.PhotoWallViewHolder> {
    private Context context;
    private List<ImageBean> imgs;
    private ImageView imageView;
    private ImageView mImageView;
    private Dialog dialog;


    public PhotoWallAdapter(Context context, List<ImageBean> imgs) {
        this.context = context;
        this.imgs = new ArrayList<>();
        this.imgs = imgs;
        this.dialog = dialog;
    }

    @Override
    public PhotoWallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_photo, parent, false);
        return new PhotoWallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoWallViewHolder holder, int position) {
        //图片每行3个，则有4个间隔，每个6dp，就为24dp（前面的paddingLeft 6dp为第一个左边的间距）,这样得出每个图片的宽度
        holder.ivPhoto.setLayoutParams(new FrameLayout.LayoutParams((ScreenUtil.getScreenWidth(context) - SizeUtils.dp2px(context, 24)) / 3,
                (ScreenUtil.getScreenWidth(context) - SizeUtils.dp2px(context, 24)) / 3));
        //glide加载
        Glide.with(context)
                .load(imgs.get(position).getImgPath())
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                // .placeholder(R.drawable.default_img)
                .into(holder.ivPhoto);
    }


    @Override
    public int getItemCount() {
        return imgs.size();
    }

    class PhotoWallViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        ImageView ivChoose;


        public PhotoWallViewHolder(View itemView) {
            super(itemView);
            ivChoose = itemView.findViewById(R.id.iv_choose);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            //小图的点击事件（弹出大图）

            ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,DisplayFullSizeImageActivity.class);
                    //putExtra只能用序列化对象
                    intent.putExtra(DisplayFullSizeImageActivity.PICTURE_PATH, imgs.get(getAdapterPosition()));
                    String[] str_imgs=new String[imgs.size()];
                    for(int i=0;i<imgs.size();i++){
                        str_imgs[i]=imgs.get(i).getImgPath();
                    }
                    intent.putExtra("img[]",str_imgs);
                    intent.putExtra("present_position",""+getAdapterPosition());
                    context.startActivity(intent);

                }
            });
        }
    }
}

