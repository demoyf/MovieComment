package com.example.mbenben.movie.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mbenben.movie.Bean.GetPhoneParams;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by alone on 2016/11/14.
 * 这是轮播图的Adapter
 *
 */
public class CircleImageAdapter extends PagerAdapter {
    private List<ImageView> mImageViews;
    private int[] ids;
    private Context mContext;
    private int width, height;
    public CircleImageAdapter(List<ImageView> imageViews, int[] id, Context context) {
        mImageViews = imageViews;
        ids = id;
        mContext = context;
        height = GetPhoneParams.dp2px(context, 130);
        width = GetPhoneParams.getPhoneWidth(context);
    }
    @Override
    public int getCount() {
        return mImageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//            Log.d("xyf", "destory " + position);
        position %= mImageViews.size();
        ((ViewPager)container).removeView(mImageViews.get(position));
    }
    /*
    * 加载下一张，所以position是 当前的+1
    * 当然在最开始的时候会加载第一张和第二张
    * */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        这里返回原因是，会加载的是当前图片的下一张，防止越界。不过应该会压缩在长度的范围之内。
        if (position == mImageViews.size()) {
            return null;
        }
        ImageView imageView = mImageViews.get(position);
        imageView.setScaleType(ImageView.ScaleType.FIT_START);
        Picasso.with(mContext).load(ids[position]).
                resize(width,height).centerCrop().into(imageView);
        ((ViewPager)container).addView(imageView);
        return imageView;
    }
}
