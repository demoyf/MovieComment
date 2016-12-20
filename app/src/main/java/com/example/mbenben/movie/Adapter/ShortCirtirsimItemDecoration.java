package com.example.mbenben.movie.Adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by alone on 2016/11/14.
 */
public class ShortCirtirsimItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = 5;
        }
        outRect.right = 7;
        outRect.left = 7;
        outRect.bottom = 5;
    }
}
