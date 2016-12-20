package com.example.mbenben.movie.guide;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mbenben.movie.Adapter.GuideViewPagerAdapter;
import com.example.mbenben.movie.Fragment.Fragment3_guide;
import com.example.mbenben.movie.Fragment.Fragment4_guide;
import com.example.mbenben.movie.Fragment.Fragment1_guide;
import com.example.mbenben.movie.Fragment.Fragment2_guide;
import com.example.mbenben.movie.R;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.MyStartActivityUtils;
import com.example.mbenben.movie.animation.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//该类是引导页的Activity
public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    private ViewPager mVPActivity;
    private Fragment mFragment1;
    private Fragment mFragment2;
    private Fragment mFragment3;
    private Fragment mFragment4;
    private TextView mTextView1,mTextView2;
    //标识页数
    public int current=0;
    //初始倒计时的时间
    int mtimer=4;
    //储存Fragment的List
    private List<Fragment> mListFragment = new ArrayList<Fragment>();
    private PagerAdapter mPgAdapter;

     // 装图形下面小点点的ImageView数组
    private ImageView[] tips;
    //Timer倒计时
   public static Timer timer;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
        //小点点的切换和加入
          tips();
      timer = new Timer();
      //将任务设定每一秒循环一次
      timer.schedule(mTimerTask2,0,1000);
//设置跳过的文本框的点击事件
        mTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               intent();
            }
        });
    }
    //利用Handler和TimerTask搭配来倒计时和跳转页面
    final Handler handler = new Handler(){
            public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 2:
                            //只显示到倒计时大于0
                            if(mtimer>0)
                            mTextView1.setText("" + mtimer);
                            //若倒计时的数刚好到0，就是要跳转页面了
                            if (mtimer==0) {
                                //页面加一，即跳转页面
                                current++;
                                mVPActivity.setCurrentItem(current);
                                //这里设置文本框为空是为了美观
                                mTextView1.setText("");
                                //这里重置倒计时的数
                                mtimer=4;
                                //如果页面在最后一页就要跳转
                                if (current == 4) {
                                    //退出计时器
                                    timer.cancel();
                                    MyStartActivityUtils.
                                            startLoginActivityWithNoBundle(MainActivity.this);
                                    finish();
                                }
                            }
                            break;
                    }
                super.handleMessage(msg);
                     }
             };
//一秒任务发送一次信息
    TimerTask mTimerTask2=new TimerTask() {
        @Override
        public void run() {
            Message message=new Message();
            message.what=2;
            mtimer--;
            handler.sendMessage(message);
        }
    };


    //初始化
    private void initView() {
        mTextView1= (TextView) findViewById(R.id.timer);
        mTextView2= (TextView) findViewById(R.id.textView);
        mVPActivity = (ViewPager) findViewById(R.id.vp_activity);
        mFragment1 = new Fragment1_guide();
        mFragment2 = new Fragment2_guide();
        mFragment3 = new Fragment3_guide();
        mFragment4 = new Fragment4_guide();
        mListFragment.add(mFragment1);
        mListFragment.add(mFragment2);
        mListFragment.add(mFragment3);
        mListFragment.add(mFragment4);
        mPgAdapter = new GuideViewPagerAdapter(getSupportFragmentManager(), mListFragment);
        mVPActivity.setAdapter(mPgAdapter);
        mVPActivity.addOnPageChangeListener(this);
        mVPActivity.setPageTransformer(true, new ZoomOutPageTransformer());
    }
//这里负责小点点的切换，设置，添加
    private void tips() {
        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
        tips = new ImageView[4];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.mipmap.page_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
            }
            //这里为设置小点点的大小
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10,10);
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            group.addView(imageView, layoutParams);
        }
    }
//点击跳转文本框的跳转方法
    public void intent(){
        MainActivity.timer.cancel();
        MyStartActivityUtils.startLoginActivityWithNoBundle(this);
         finish();
    }


    //这里是实现ViewPager.OnPageChangeListener必须实现的三个方法之一，下面三个方法都是
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //这是页面滚动不断调用的方法
    }


    //这是页面切换到另一个页面后调用的方法
    public void onPageSelected(int position) {
        setImageBackground(position);
        current=position;
        mtimer=4;

    }

    //这是手指在页面上滑动手势的监听，例如滑动，按下，离开
    public void onPageScrollStateChanged(int state) {

    }
    /**
     * 设置选中的tip的背景
     * @param selectItems
     */
    private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.mipmap.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.mipmap.page_indicator_unfocused);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimerTask2.cancel();
    }
}
