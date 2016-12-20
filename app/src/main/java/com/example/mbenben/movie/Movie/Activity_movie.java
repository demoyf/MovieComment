package com.example.mbenben.movie.Movie;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbenben.movie.Fragment.Fragment1;
import com.example.mbenben.movie.Fragment.Fragment4;
import com.example.mbenben.movie.Fragment.Fragment2;
import com.example.mbenben.movie.Fragment.Fragment3;
import com.example.mbenben.movie.R;

public class Activity_movie extends AppCompatActivity {
private FragmentTabHost mFragmentTabHost;
public static Activity_movie mActivity_movie;
    public static String phone;

    private String[] tab_text={"首页","消息","影","发现","我"};
    private int tab_button[]={R.drawable.tab_home_selector,R.drawable.tab_message_selector,
            R.drawable.tab_me,
            R.drawable.tab_discover_selector, R.drawable.tab_me_selector
    };
    private Class fragmentArray[]={Fragment1.class, Fragment2.class,
            Fragment1.class, Fragment3.class, Fragment4.class};

//    private ImageView tab_centerbutton1;
//    private  ImageView tab_centerbutton2;
//    private boolean mFlag=true;

    private Dialog dialog;
    private ImageView tab_centter_select1;
    private ImageView tab_centter_select2;
    private ImageView close;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        phone=getIntent().getStringExtra("phoneNum");
         initView();
        mActivity_movie=this;
    }

    private void initView() {


        mFragmentTabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        mFragmentTabHost.setup(this,getSupportFragmentManager(),R.id.maincontent);
       for(int i=0;i<tab_text.length;i++){
           TabHost.TabSpec spec=mFragmentTabHost.newTabSpec(tab_text[i]).setIndicator(getView(i));
               mFragmentTabHost.addTab(spec,fragmentArray[i],null);

       }
        mFragmentTabHost.getTabWidget().getChildTabViewAt(2).setOnClickListener(new View.OnClickListener() {
            //第3个按钮，点击事件我们这边处理
            @Override
            public void onClick(View v) {

                        show();

            }
        });

    }

   public void show(){
       dialog=new Dialog(this,R.style.ActionSheetDialogStyle);
       //填充对话框的布局
       View inflate= LayoutInflater.from(this).inflate(R.layout.tab_center,null);
       //初始化控件
       tab_centter_select1= (ImageView) inflate.findViewById(R.id.tab_centter_select1);
       tab_centter_select2= (ImageView) inflate.findViewById(R.id.tab_centter_select2);
        close= (ImageView) inflate.findViewById(R.id.close);




       //将布局设置给Dialog
       dialog.setContentView(inflate);
       //获取当前Activity所在的窗体
       Window dialogWindow=dialog.getWindow();
       //设置Dialog从窗体底部弹出
       dialogWindow.setGravity(Gravity.BOTTOM);
       //获得窗体的属性
       WindowManager.LayoutParams lp=dialogWindow.getAttributes();



       lp.y=400;//设置Dialog距离底部的距离
       //将属性设置给窗体
       dialogWindow.setAttributes(lp);
       dialog.setCanceledOnTouchOutside(false);


       dialog.show();//显示对话框

       close.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dialog.dismiss();
           }
       });

       tab_centter_select1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               dialog.dismiss();
               Intent intent=new Intent(Activity_movie.this,SendCriticActivity.class);
               startActivity(intent);
           }
       });
       tab_centter_select2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(Activity_movie.this,"论坛功能暂未开发",Toast.LENGTH_LONG).show();
               dialog.dismiss();
           }
       });
   }

    private View getView(int i) {
                 //取得布局实例
                 View view=View.inflate(this, R.layout.tabcontent, null);

                 //取得布局对象
                 ImageView imageView=(ImageView) view.findViewById(R.id.image);
                TextView textView=(TextView) view.findViewById(R.id.text);

             if(i==2){
                 view=View.inflate(this, R.layout.tab_button, null);

             }
           else {
                 //设置图标
                 imageView.setImageResource(tab_button[i]);
                 //设置标题
                 textView.setText(tab_text[i]);
             }

                 return view;
             }
    @Override
    public void onBackPressed() {
        Fragment1 mFragment = Fragment1.getInstance();
        if (mFragment.onBack()) {
            return;
        }
        super.onBackPressed();
    }

   public static Activity_movie getActivity(){
       return mActivity_movie;
   }
}
