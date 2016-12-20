package com.example.mbenben.movie.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbenben.movie.Bean.GetPhoneParams;
import com.example.mbenben.movie.Interface.OnRecPositionClick;
import com.example.mbenben.movie.Interface.UpdateNewItemSuccess;
import com.example.mbenben.movie.Movie.PublishCritic;
import com.example.mbenben.movie.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by alone on 2016/11/9.
 */
public class MyMainRecyclerViewAdapter extends RecyclerView.Adapter<MyMainRecyclerViewAdapter.MyViewHolder> {
    private Context mContext;
    List<PublishCritic> mPublishCritics;
    int width = 0;
    private OnRecPositionClick mOnRecPositionClick;
    public MyMainRecyclerViewAdapter(Context context, List<PublishCritic> publishCritics) {
        mContext = context;
        mPublishCritics = publishCritics;
        width = GetPhoneParams.getPhoneWidth(context);
        width = width / 2 - 5;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.main_rec_item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (mPublishCritics.size() != 0) {
            PublishCritic publishCritic = mPublishCritics.get(position);
            holder.setTitle(publishCritic.getTitle() + "");
            holder.setContentText(publishCritic.getCritic());
            holder.setIconImageView(publishCritic.getPicture(),position);
            holder.setOnclick(position);
        }
    }

    public void addNext(List<PublishCritic> publishCritics, UpdateNewItemSuccess updateNewItemSuccess) {
        if (publishCritics.size() != 0) {
            mPublishCritics.addAll(publishCritics);
            notifyDataSetChanged();
        }
        updateNewItemSuccess.pudateSuccess(true);
    }
    @Override
    public int getItemCount() {
        return mPublishCritics.size();
    }
    public PublishCritic getPositionPub(int position) {
        if (position < 0) {
            return null;
        }
        if (mPublishCritics.size()==0) {
            return null;
        }
        return mPublishCritics.get(position);
    }
    public void addFirst(List<PublishCritic> publishCritics, UpdateNewItemSuccess updateNewItemSuccess) {
        if (publishCritics.size() != 0) {
            for (int i = 0; i < publishCritics.size(); i++) {
                mPublishCritics.add(0, publishCritics.get(i));
            }
            notifyDataSetChanged();
        }
        updateNewItemSuccess.pudateSuccess(true);
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView;
        ImageView iconImageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.item_title);
            contentTextView = (TextView) itemView.findViewById(R.id.item_content);
            iconImageView = (ImageView) itemView.findViewById(R.id.item_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("xyf", "this turn");
                }
            });
        }

        public void setOnclick(final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnRecPositionClick.OnClickSuccsee(position);
                }
            });
        }
        public void setTitle(String title) {
            if (!TextUtils.isEmpty(title))
            titleTextView.setText("——《" + title+"》");
        }
        public void setContentText(String content) {
            if (!TextUtils.isEmpty(content))
            contentTextView.setText(content);
        }
        public void setIconImageView(String url,int position) {
            ViewGroup.LayoutParams params = iconImageView.getLayoutParams();
            int height = mPublishCritics.get(position).getHeight();
            if (params.height == height) {
                return;
            } else {
                params.height = height;
                iconImageView.setLayoutParams(params);
            }
            if (!TextUtils.isEmpty(url))
                Picasso.with(mContext).
                        load(url).resize(width, height).config(Bitmap.Config.RGB_565)
                        .placeholder(R.mipmap.touxiang_load).
                        centerCrop().noFade().into(iconImageView);
        }
    }
    public void clearData() {
        mPublishCritics.clear();
    }
    public void setOnRecPositionClick(OnRecPositionClick onRecPositionClick) {
        mOnRecPositionClick = onRecPositionClick;
    }
}
