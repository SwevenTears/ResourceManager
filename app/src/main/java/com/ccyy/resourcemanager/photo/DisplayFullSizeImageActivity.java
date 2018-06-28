package com.ccyy.resourcemanager.photo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ccyy.resourcemanager.R;

import java.io.File;
import java.util.ArrayList;

public class DisplayFullSizeImageActivity extends AppCompatActivity {

   private  RecyclerAdapter adapter;

    private ArrayList<String> imgPathList;
    private int present_position;

    private MovieAdapter movieAdapter;
    /**
     * 文件路径
     */
    public static final String PICTURE_PATH = "picture_path";
    private RecyclerView mRecyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_full_size_list_layout);
        //vpager_one = (ViewPager) findViewById(R.id.full_size_image_1);
        Intent intent = getIntent();

        ImageBean image = (ImageBean) intent.getSerializableExtra(PICTURE_PATH);
        String[] urls = intent.getStringArrayExtra("img[]");

        imgPathList = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            imgPathList.add(urls[i]);
        }


        present_position = Integer.parseInt(intent.getStringExtra("present_position"));
        Uri uri = Uri.fromFile(new File(image.getImgPath()));//得到图片的路径


        mRecyclerView = (RecyclerView) findViewById(R.id.full_size_image_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DisplayFullSizeImageActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.scrollToPositionWithOffset(present_position,0);
        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        movieAdapter=new MovieAdapter(DisplayFullSizeImageActivity.this,imgPathList);


        mRecyclerView.setAdapter(movieAdapter);

    }

    class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder>{

        private Context context;
        private LayoutInflater inflater;
        private ArrayList<String> imgPaths;

        public MovieAdapter(Context context,ArrayList<String> imgList) {
            inflater = LayoutInflater.from(context);
            this.context = context;
            this.imgPaths=imgList;
        }

        @NonNull
        @Override
        public MovieAdapter.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=inflater.inflate(R.layout.display_full,parent,false);
            return new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MovieAdapter.MovieHolder holder, int position) {

            Bitmap bitmap=BitmapFactory.decodeFile(imgPaths.get(position));
            holder.img.setImageBitmap(bitmap);
        }

        @Override
        public int getItemCount() {
            return imgPaths.size();
        }

        public class MovieHolder extends RecyclerView.ViewHolder {

            private ImageView img;

            public MovieHolder(View itemView) {
                super(itemView);
                img=itemView.findViewById(R.id.full_size_image);
            }
        }
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(DisplayFullSizeImageActivity.this).inflate(R.layout.display_full,parent,false);
            return new RecyclerListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RecyclerListViewHolder viewHolder = (RecyclerListViewHolder)holder;

            Bitmap bitmap=BitmapFactory.decodeFile(imgPathList.get(position));
            ((RecyclerListViewHolder) holder).Zoomiv.setImageBitmap(bitmap);
        }
        @Override
        public int getItemCount() {
            return imgPathList.size();
        }

        class RecyclerListViewHolder extends RecyclerView.ViewHolder{
            private ImageView Zoomiv;
            public RecyclerListViewHolder(View view){
                super(view);
                Zoomiv = (ZoomImageView)view.findViewById(R.id.full_size_image);
            }
        }
    }
}
