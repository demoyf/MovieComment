package com.example.mbenben.movie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import java.util.ArrayList;

import com.example.mbenben.movie.ShowSomethingAndKeyInterface.ShareAndBundleKey;
import com.example.mbenben.movie.LoadDataAndVerCode.VolleyGetJsonString;
import com.example.mbenben.movie.Login.LoginFragment;
import com.example.mbenben.movie.Login.LoginTextAttrbute;
import com.example.mbenben.movie.Login.LoginTitleTextView;
import com.example.mbenben.movie.Login.QuickLoginFragment;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.MyStartActivityUtils;

public class LoginActivity extends AppCompatActivity implements
        LoginTextAttrbute,ShareAndBundleKey {
//    注意这里在退出登陆的时候，要修改，否则就GG了
//    ViewPager
    private ViewPager mViewPager;
//    Fragment集合
    private ArrayList<Fragment> arrayList = new ArrayList<>();
//     ViewPager的适配器
    private FragmentPagerAdapter viewPagerAdapter;
    //    两个标题的View
    private ArrayList<LoginTitleTextView> mTextViews = new ArrayList<>();
    private LoginTitleTextView normalLogin, quickLogin;
    private SharedPreferences mSharedPreferences;
    private LoginFragment loginFragment;
    private QuickLoginFragment quickLoginFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_main);
        hide();
        wantTo();
    }

    /*
    * 判断是第一次进入，还是已经保存密码要跳过，或者是退出后回到这个界面
    * */
    private void wantTo(){
        //   加多这个东西，后面会用到的。退出的时候，重新启动，我们就要先传输数据给这个Ac。
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mSharedPreferences = getSharedPreferences(SHARE_FILE_NAME, MODE_PRIVATE);
        if (bundle != null) {
            // bundle 不为空，表明是退出登录来的。
            initView();
            initEvent();
            saveInfo(false, "");
        }else {
            boolean isSave = mSharedPreferences.getBoolean(SHARE_IS_SAVE_KEY, false);
            //        这主要是为了测试的时候用的。防止跳转。
//            有保存
            if (isSave) {
                //  直接跳过这个Activity，进入主界面
                String phone = mSharedPreferences.getString(SHARE_PHONE_KEY, "");
                MyStartActivityUtils.startMainUIActivity(this,phone);
                finish();
            }else {
                initView();
                initEvent();
            }
        }

    }
    /*
    * 初始化事件监听
    * 登陆、注册、忘记密码
    * */
    private void initEvent() {
//        注册监听器，实现左右滑动的时候，产生渐变效果
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                判断是否有发生偏移
                if (positionOffset > 0 && positionOffset < 1) {
                    LoginTitleTextView curLeft = null;
                    LoginTitleTextView curRight = null;
//                    获取第一个和第二个标题文本
                    curLeft = mTextViews.get(position);
                    curRight = mTextViews.get(position + 1);
                    if (curLeft != null) {
//                      分别设置它们的方向，凭此来判断如何绘制
                        curLeft.setDirection(DIR_RIGHT);
                        curRight.setDirection(DIR_LEFT);
//                        这个偏移量实际上就是要变换颜色的比重
                        curLeft.setProcess(positionOffset);
                        curRight.setProcess(positionOffset);
                    }
                }
            }
            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        这个设定有点6，还在研究中，主要是在切换的过程中，让页面缩放或者是透明度改变
        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            final float MIN_SCALE = 0.85f;
            final float MIN_ALPHA = 0.5f;
//            position 实际就是当前可见的第一个页的下标，哪怕只是偏移了一点点
            @Override
            public void transformPage(View page, float position) {
                int viewWidth = page.getWidth();
                int viewHeight = page.getHeight();
//                这个是用于偏移的，就是滑动的时候，page的移动
                if(position<-1){
                    page.setAlpha(0);
                } else if (position <= 1) {
                    //总感觉是0-1，然后一直是这样
                    float scaleFactor = Math.max(MIN_SCALE, 1-Math.abs(position));
                    float horMargin = viewWidth * (1 - scaleFactor) / 2;
                    float verMargin = viewHeight * (1 - scaleFactor) / 2;
                    if (position < 0) {
//                        wocao  这是保持间隔的效果。。。。
                        page.setTranslationX(horMargin - verMargin / 2);
                    }else{
                        page.setTranslationX(-horMargin + verMargin / 2);
                    }
//                    缩放
                    page.setScaleX(scaleFactor);
                    page.setScaleY(scaleFactor);
//                     透明度变化
                    page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_ALPHA)/
                            (1-MIN_ALPHA)*(1-MIN_ALPHA));
                }else{
                    page.setAlpha(0);
                }
            }
        });
        /*这里出现了一点小bug，主要是在布局中使用了相对布局，但是没有指定好ViewPager的位置，导致覆盖到
       标题，点击无效
        两个方法用于注册标题栏的监听，进行切换
       */
        normalLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        quickLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });
    }
    /*
    * 初始化视图控件，并且设置第一个显示的是正常登陆Fragment
    * */

    @Override
    public void onBackPressed() {
        if (loginFragment.getProgressBarVisibility() == View.GONE) {
            finish();
        } else {
            loginFragment.setProgressBarGone();
        }
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        loginFragment = new LoginFragment();
        quickLoginFragment = new QuickLoginFragment();
        arrayList.add(loginFragment);
        arrayList.add(quickLoginFragment);
        viewPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return arrayList.get(position);
            }
            @Override
            public int getCount() {
                return arrayList.size();
            }
        };
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(0);
        normalLogin = (LoginTitleTextView) findViewById(R.id.normal_login);
        quickLogin = (LoginTitleTextView) findViewById(R.id.quick_login);
        mTextViews.add(normalLogin);
        mTextViews.add(quickLogin);
        mTextViews.get(0).setProcess(1);
    }
    /*
    * 该方法用户隐藏标题栏
    * */
    private void hide() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    //    提供保存用户信息的方法
    public void saveInfo(boolean isSave, String phone) {
        if (mSharedPreferences != null) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(SHARE_IS_SAVE_KEY, isSave);
            editor.putString(SHARE_PHONE_KEY, phone);
            editor.commit();
        }
    }
//    返回Fragment，方便各个碎片之间的通信
    public ArrayList<Fragment> getArrayList() {
        return arrayList;
    }
//    最大的都被销毁，那么就可以停掉Volley的请求队列了
    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyGetJsonString.StopRequestQueue();
    }
}
