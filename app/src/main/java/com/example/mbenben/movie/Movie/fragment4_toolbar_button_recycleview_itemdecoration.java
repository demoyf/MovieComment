package com.example.mbenben.movie.Movie;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by MBENBEN on 2016/11/5.
 */
public class fragment4_toolbar_button_recycleview_itemdecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS=new int[]{android.R.attr.listDivider};
    private Drawable mDrawable;
   // private Paint mPaint;
    public fragment4_toolbar_button_recycleview_itemdecoration(Context context){
        final TypedArray a=context.obtainStyledAttributes(ATTRS);
        mDrawable=a.getDrawable(0);
        a.recycle();
//          mPaint=new Paint();
//        mPaint.setColor(Color.BLUE);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        drawVertical(c,parent);
    }


    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            android.support.v7.widget.RecyclerView v = new android.support.v7.widget.RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDrawable.getIntrinsicHeight();
            //c.drawRect(left, top, right, bottom,mPaint);
           mDrawable.setBounds(left, top, right, bottom);
           mDrawable.draw(c);
            //c.drawRect(left, top, right, bottom);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0,0, 0, mDrawable.getIntrinsicHeight());
    }



}
