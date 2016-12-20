package com.example.mbenben.movie.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbenben.movie.Bean.CommentCriticBean;
import com.example.mbenben.movie.Bean.GetPhoneParams;
import com.example.mbenben.movie.Movie.PublishCritic;
import com.example.mbenben.movie.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by alone on 2016/11/22.
 */
public class ShowCompletedCriticAdapter extends BaseAdapter{
    private PublishCritic mPublishCritic;
    private List<CommentCriticBean> mOneUserCritics;
    private Context mContext;
    private int realWidth;
    public ShowCompletedCriticAdapter(Context context, PublishCritic publishCritic,
                                      List<CommentCriticBean> oneUserCritics) {
        mOneUserCritics = oneUserCritics;
        mPublishCritic = publishCritic;
        mContext = context.getApplicationContext();
        realWidth = GetPhoneParams.getPhoneWidth(mContext);
    }
    @Override
    public int getCount() {
        return mOneUserCritics.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == 0) {
            return mPublishCritic;
        } else {
            return mOneUserCritics.get(position - 1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TopItemHolder topItemHolder = null;
        OtherCriticHolder otherCriticHolder = null;
        if (convertView != null) {
//                convertView = LayoutInflater.from(mContext).inflate();
            if (position == 0) {
                topItemHolder = new TopItemHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.critic_top_item, parent, false);
                topItemHolder.mIcon = (ImageView) convertView.findViewById(R.id.com_critic_icon);
                topItemHolder.mShowImage = (ImageView) convertView.findViewById(R.id.critic_show_image);
                topItemHolder.nameTextView = (TextView) convertView.findViewById(R.id.send_user_name);
                topItemHolder.timeTextView = (TextView) convertView.findViewById(R.id.critic_upload_time);
                topItemHolder.contentTextView = (TextView) convertView.findViewById(R.id.critic_content);
                topItemHolder.titleTextView = (TextView) convertView.findViewById(R.id.user_critic_title);
            } else {
                if (convertView.getTag() instanceof OtherCriticHolder) {
                    otherCriticHolder = (OtherCriticHolder) convertView.getTag();
                }
                else{
                    convertView = LayoutInflater.from(mContext).
                            inflate(R.layout.user_critic_item_layout, parent, false);
                    otherCriticHolder = new OtherCriticHolder();
                    otherCriticHolder.nameTextView = (TextView) convertView.findViewById(R.id.other_user_name);
                    otherCriticHolder.timeTextView = (TextView) convertView.findViewById(R.id.other_critic_time);
                    otherCriticHolder.contentTextView = (TextView) convertView.findViewById(R.id.other_critic_content);
                    convertView.setTag(otherCriticHolder);
                }
            }
        } else {
            if (position == 0) {
                topItemHolder = new TopItemHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.critic_top_item, parent, false);
                topItemHolder.mIcon = (ImageView) convertView.findViewById(R.id.com_critic_icon);
                topItemHolder.mShowImage = (ImageView) convertView.findViewById(R.id.critic_show_image);
                topItemHolder.nameTextView = (TextView) convertView.findViewById(R.id.send_user_name);
                topItemHolder.timeTextView = (TextView) convertView.findViewById(R.id.critic_upload_time);
                topItemHolder.contentTextView = (TextView) convertView.findViewById(R.id.critic_content);
                topItemHolder.titleTextView = (TextView) convertView.findViewById(R.id.user_critic_title);
            } else {
                convertView = LayoutInflater.from(mContext).
                        inflate(R.layout.user_critic_item_layout, parent, false);
                otherCriticHolder = new OtherCriticHolder();
                otherCriticHolder.nameTextView = (TextView) convertView.findViewById(R.id.other_user_name);
                otherCriticHolder.timeTextView = (TextView) convertView.findViewById(R.id.other_critic_time);
                otherCriticHolder.contentTextView = (TextView) convertView.findViewById(R.id.other_critic_content);
                convertView.setTag(otherCriticHolder);
            }
        }
        if (position == 0) {
            Log.d("xyf", "set ");
            topItemHolder.nameTextView.setText(mPublishCritic.getName());
            topItemHolder.setShowImage(mPublishCritic.getPicture());
            if (TextUtils.isEmpty(mPublishCritic.getHeadpicture())) {
                Picasso.with(mContext).load(R.mipmap.defaulticon).resize(60,60).
                        into(topItemHolder.mIcon);
            }else {
            Picasso.with(mContext).load(mPublishCritic.getHeadpicture()).
                    error(R.mipmap.defaulticon).into(topItemHolder.mIcon);
            }
            topItemHolder.titleTextView.setText("--《"+mPublishCritic.getTitle()+"》");
            topItemHolder.contentTextView.setText(mPublishCritic.getCritic());
            topItemHolder.timeTextView.setText(mPublishCritic.getTime());
        } else {
            CommentCriticBean oneUserCritic = mOneUserCritics.get(position - 1);
            otherCriticHolder.nameTextView.setText(oneUserCritic.getName());
            otherCriticHolder.timeTextView.setText(oneUserCritic.getTime());
            otherCriticHolder.contentTextView.setText(oneUserCritic.getCritic());
        }
        return convertView;
    }
    private class TopItemHolder{
        ImageView mIcon;
        ImageView mShowImage;
        TextView nameTextView,timeTextView, contentTextView,titleTextView;
        private String mUrl = null;
        private Transformation mTransformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                int tragetWidth = realWidth - 60;
                int tragetHeight = (int) (source.getHeight() *
                        (realWidth * 1.0 / source.getWidth()));
                Log.d("xyf", "width:" + tragetWidth + "height " + tragetHeight +
                        ",source height " + source.getHeight());
                Bitmap result =
                        Bitmap.createScaledBitmap(source, tragetWidth, tragetHeight, false);
                if (result != source) {
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return mUrl;
            }
        };
        public void setShowImage(final String url) {
            mUrl = url;
            Picasso.with(mContext).load(url).config(Bitmap.Config.RGB_565)
                    .placeholder(R.mipmap.touxiang_load).error(R.drawable.p4).
                    transform(mTransformation).
                    into(mShowImage);
        }
    }

    private class OtherCriticHolder {
        TextView nameTextView,timeTextView, contentTextView;
    }

    public void clearAll() {
        mPublishCritic = null;
        mOneUserCritics.clear();
    }
}
