package com.ccyy.resourcemanager.photo;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView实现横向滑动，为更美观，需要将每一个item之间的间隔展示出来，
 * 而要实现这样的功能，需要手动创建一个类继承RecyclerView.ItemDecoration
 * 引用方式 mRecyclerView.addItemDecoration(new SpaceItemDecoration(15));
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration{

    int mSpace;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = mSpace;
        }

    }

    public SpaceItemDecoration(int space) {
        this.mSpace = space;
    }
}
