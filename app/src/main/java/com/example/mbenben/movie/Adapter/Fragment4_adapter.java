package com.example.mbenben.movie.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbenben.movie.R;

/**
 * Created by MBENBEN on 2016/10/31.
 */
public class Fragment4_adapter extends BaseAdapter {
    private String[] mdata;
    private LayoutInflater mInflater;

    public Fragment4_adapter(LayoutInflater inflater,String[] data){
        mInflater=inflater;
        mdata=data;
    }

    @Override
    public int getCount() {
        return mdata.length;
    }

    @Override
    public Object getItem(int i) {
        return mdata[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        //判断是否缓存
        if(convertView==null){
            holder=new ViewHolder();
            //通过LayoutInflater实例化布局
            convertView=mInflater.inflate(R.layout.fragment4_item,null);
            holder.img= (ImageView) convertView.findViewById(R.id.fragment4_item_image);
            holder.title= (TextView) convertView.findViewById(R.id.fragment4_item_text);
            convertView.setTag(holder);
        }
        else {
            //通过tag找到缓存的布局
            holder= (ViewHolder) convertView.getTag();
        }
        //设置布局中控件要显示的视图
        holder.img.setBackgroundResource(R.drawable.fragment4_arrow);
        holder.title.setText(""+mdata[i]);
        return convertView;
    }






    public final class ViewHolder {
        public ImageView img;
        public TextView title;
    }
}
