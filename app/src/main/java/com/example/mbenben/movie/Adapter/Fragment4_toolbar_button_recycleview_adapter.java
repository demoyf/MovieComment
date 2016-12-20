package com.example.mbenben.movie.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbenben.movie.Interface.OnItemClickListener;
import com.example.mbenben.movie.R;

/**
 * Created by MBENBEN on 2016/11/4.
 */
public class Fragment4_toolbar_button_recycleview_adapter extends RecyclerView.Adapter<Fragment4_toolbar_button_recycleview_adapter.MyViewHolder>  {

    private String[] mText;
    private String[] mText2;
    private int mimages;
    private Context mContext;
    private LayoutInflater mInflater;
    OnItemClickListener mOnItemClickListener;
    public Fragment4_toolbar_button_recycleview_adapter(Context context,String[] mText,String[] mText2){
        this.mContext=context;
        this.mText=mText;
        this.mText2=mText2;
      //  this.mimages=mimages;
        mInflater=LayoutInflater.from(mContext);

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.fragment4_toolbar_button_item,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;

    }

    //填充onCreateViewHolder方法返回的holder中的控件
    public void onBindViewHolder(MyViewHolder holder, final int position) {
     holder.tv.setText(mText[position]);
       // holder.iv.setImageResource(mimages);
        holder.tv2.setText(mText2[position]);

    if(mOnItemClickListener!=null){
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onClick(position);
            }
        });
    }
    }

    @Override
    public int getItemCount() {
        return mText.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        ImageView iv;
        TextView tv2;

        public MyViewHolder(View view) {
            super(view);
            tv=(TextView) view.findViewById(R.id.fragment4_toolbar_button_item_text);
           // iv= (ImageView) view.findViewById(R.id.fragment4_toolbar_button_item_image);
         tv2= (TextView) view.findViewById(R.id.fragment4_toolbar_button_item_text2);
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }

}
