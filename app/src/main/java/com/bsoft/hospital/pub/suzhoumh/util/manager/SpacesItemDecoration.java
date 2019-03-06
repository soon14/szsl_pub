package com.bsoft.hospital.pub.suzhoumh.util.manager;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 设置item之间的分割空间
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private boolean flag = false;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    public SpacesItemDecoration(int space, boolean flag) {
        this.space = space;
        this.flag = flag;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (flag) {
            outRect.left = 0;
            outRect.top = space;
            outRect.right = 0;
            outRect.bottom = 0;
        } else {
            outRect.left = space;
            outRect.top = space;
            outRect.right = space;
            outRect.bottom = space;
        }

    }
}
