package com.example.mbenben.movie.animation;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by MBENBEN on 2016/10/11.
 * 此类为滑动引导页面缩小移动的动画
 */
public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE=0.85f;
    private static final float MIN_ALPHA=0.5f;

    @Override
    public void transformPage(View page, float position) {
        int pageWidth=page.getWidth();
        int pageHeight=page.getHeight();
//当前页面划出了屏幕左边缘之外不可见的情况，设置透明
        if(position<-1){
            page.setAlpha(0);
        }
     //当前页面差不多要划出了左边，或者是这个页面刚刚从右边准备滑进来
        else if(position<=1){
            //缩放系数，将系数控制在(MIN_SCALE,1)之间
            float scaleFactor=Math.max(MIN_SCALE,1-Math.abs(position));
            float vertMargin=pageHeight*(1-scaleFactor)/2;
            float horzMargin=pageWidth*(1-scaleFactor)/2;
            if(position<0){
                page.setTranslationX(horzMargin-vertMargin/2);
            }
            else {
                page.setTranslationX(-horzMargin+vertMargin/2);
            }
            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

            page.setAlpha(MIN_ALPHA+(scaleFactor-MIN_SCALE)/(1-MIN_SCALE)*(1-MIN_ALPHA));

        }
        else {
            page.setAlpha(0);
        }
    }
}
