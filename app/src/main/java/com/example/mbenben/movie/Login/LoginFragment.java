package com.example.mbenben.movie.Login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mbenben.movie.LoadDataAndVerCode.VolleyGetJsonString;
import com.example.mbenben.movie.LoginActivity;
import com.example.mbenben.movie.LoadDataAndVerCode.GetURL;
import com.example.mbenben.movie.R;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.DialogClick;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.MyShowSomthingUtil;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.MyStartActivityUtils;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.ShowPhoneDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alone on 2016/10/12.
 */
public class LoginFragment extends Fragment implements LoadLoginResult,DialogClick {
    private EditText phoneEdit, passwordEdit;
    private Button loginButton, regButton;
//    这个是inflate进来的整个布局
    private View view;
    private String phoneString, passwordString;
//    获取到跟碎片关联的Activity，方便一些操作
    private Activity mActivity;
//    自己封装的，通过Volley获取wsk的数据
    private VolleyGetJsonString mGetJsonString;
    private ProgressBar mProgressBar;
    private CheckBox remember;
    private TextView mForgetTextView;
    private boolean isLoginClick = false;
    private ShowPhoneDialog mShowPhoneDialog;
    //    movieImpl movie;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.login_fragment_layout, container, false);
        initView();
        initEvent();
        return view;
    }

    //  注册监听
    private void initEvent() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneString = phoneEdit.getText().toString().trim();
                isLoginClick = true;
//                获取输入的数据，如果电话号码为空或者不等于11位，提示后返回
                passwordString = passwordEdit.getText().toString().trim();
                if (TextUtils.isEmpty(phoneString) || phoneString.length() != 11) {
                    MyShowSomthingUtil.showToastShort(mActivity, "手机号输入不正确");
                    return;
                } else {
                    /*密码长度不够
                    * */
                    if (TextUtils.isEmpty(passwordString)){
                        MyShowSomthingUtil.showToastShort(mActivity,"密码不能为空");
                        return;
                    }
                    if (passwordString.length() < 6) {
                        MyShowSomthingUtil.showToastShort(mActivity, "密码长度不正确");
                        return;
                    }
                    mProgressBar.setVisibility(View.VISIBLE);
                    String url = GetURL.getRegIsNullURL(phoneString);
                    mGetJsonString.getLoginIsNull(url, LoginFragment.this);
                }
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//            准备跳转到注册界面了。
                MyStartActivityUtils.startRegisterActivity(mActivity);
                if (getActivity() instanceof LoginActivity) {
                    /*
                    * 跳转的时候要注销上一个监听器
                    * */
                    LoginActivity loginActivity = (LoginActivity) getActivity();
                    Fragment fragment = loginActivity.getArrayList().get(1);
                    if (fragment instanceof QuickLoginFragment) {
                        QuickLoginFragment quickLoginFragment = (QuickLoginFragment) fragment;
                        quickLoginFragment.unregister();
                    }
                }
            }
        });
        mForgetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoginClick = false;
                phoneString = phoneEdit.getText().toString().trim();
                String ph = phoneEdit.getText().toString().trim();
                if (TextUtils.isEmpty(ph) || ph.length() != 11) {
                    MyShowSomthingUtil.showToastShort(mActivity, "手机号码输入不正确");
                    return;
                }
                mProgressBar.setVisibility(View.VISIBLE);
                String url = GetURL.getRegIsNullURL(ph);
                mGetJsonString.getLoginIsNull(url, LoginFragment.this);
            }
        });
    }



    //    初始化控件而已
    private void initView() {
        mActivity = getActivity();
        mGetJsonString = new VolleyGetJsonString(mActivity);
        phoneEdit = (EditText) view.findViewById(R.id.phone_edit);
        MyShowSomthingUtil.setEditTextFilter(phoneEdit, 11);
        passwordEdit = (EditText) view.findViewById(R.id.password_edit);
        MyShowSomthingUtil.setEditTextFilter(passwordEdit, 16);
        loginButton = (Button) view.findViewById(R.id.login_button);
        regButton = (Button) view.findViewById(R.id.reg_button);
        mProgressBar = (ProgressBar) view.findViewById(R.id.login_progress);
        remember = (CheckBox) view.findViewById(R.id.remember_check_box);
        mForgetTextView = (TextView) view.findViewById(R.id.forget_password);
        mShowPhoneDialog = new ShowPhoneDialog(mActivity, this);
        MyShowSomthingUtil.setEditTextFilter(phoneEdit, 11);
        MyShowSomthingUtil.setEditTextFilter(passwordEdit, 16);
    }

    /*
    * 重载回调方法，此方法用于处理获取数据成功时候的返回值
    * @param s ：读取到的数据
    * */
    @Override
    public void loginLoadSuccess(String s) {
        String requestCode = null;
        mProgressBar.setVisibility(View.GONE);
//        解析JSON，获取返回的结果码
        try {
            JSONObject jsonObject = new JSONObject(s);
            requestCode = jsonObject.getString("login");
        } catch (JSONException e) {
            MyShowSomthingUtil.showToastShort(mActivity,"服务器连接异常");
            e.printStackTrace();
            return;
        }
//        要加多一个判断，防止报错后还继续运行
        if (requestCode != null) {
            if (requestCode.equals("1")) {
                MyShowSomthingUtil.showToastShort(mActivity, "登录成功");
                LoginActivity loginActivity = (LoginActivity) mActivity;
                if (remember.isChecked()) {
                    if (mActivity instanceof LoginActivity) {
                        loginActivity.saveInfo(true, phoneString);
                    }
                } else {
                    if (mActivity instanceof LoginActivity) {
                        loginActivity.saveInfo(false, "");
                    }
                }
//                goto  跳转到主界面
                if (!TextUtils.isEmpty(phoneString)) {
                    MyStartActivityUtils.startMainUIActivity(mActivity, phoneString);
                    mActivity.finish();
                }
            } else {
                MyShowSomthingUtil.showToastShort(mActivity, "用户名或密码错误");
            }
        }
    }
    @Override
    public void judgeIsNull(boolean isnull) {
        if (isnull) {
            MyShowSomthingUtil.showToastShort(mActivity, "用户不存在");
        } else {
            if (isLoginClick) {
                passwordString = passwordEdit.getText().toString().trim();
//                判断都不为空的时候
                if (!TextUtils.isEmpty(passwordString)) {
//                    先显示进度条
                    mProgressBar.setVisibility(View.VISIBLE);
//                    获取URL，自定义的静态方法。
                    String url = GetURL.getLoginURL(phoneString, passwordString);
//                    url来获取数据，等他完成之后，会回调的。登陆
                    mGetJsonString.getLoginResult(url, LoginFragment.this);
                } else {
                    MyShowSomthingUtil.showToastShort(mActivity, "密码不能为空");
                }
            } else {
                /*
                * 显示弹出框，销毁进度条
                * */
                mProgressBar.setVisibility(View.GONE);
//                Log.d("xyf", "click " + phoneString);
                mShowPhoneDialog.setPhoneString(phoneString);
                mShowPhoneDialog.showDialog();
            }
        }
    }

    /*
    * 登陆数据加载失败
    * */
    @Override
    public void loginLoadFail() {
        mProgressBar.setVisibility(View.GONE);
        MyShowSomthingUtil.showToastShort(mActivity, "网络连接异常");
    }

    /*
    * 弹出框的确定按钮被点击
    * */
    @Override
    public void yesButtonClilck() {
        /*
        * 跳转前先停掉快速登陆的监听器
        * */
        if (getActivity() instanceof LoginActivity) {
            LoginActivity loginActivity = (LoginActivity) getActivity();
            Fragment fragment = loginActivity.getArrayList().get(1);
            if (fragment instanceof QuickLoginFragment) {
                QuickLoginFragment quickLoginFragment = (QuickLoginFragment) fragment;
                quickLoginFragment.unregister();
            }
        }
        /*
        * 跳转到忘记密码，这里用的是自定义的静态方法，可以很清楚的知道要传入什么参数
        * */
        MyStartActivityUtils.startForgetPassword(mActivity, phoneString);
    }

    /*
    * 销毁的时候，把请求给停了
    * */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mGetJsonString.StopRequestQueue();
    }
    public int getProgressBarVisibility() {
        return mProgressBar.getVisibility();
    }

    public void setProgressBarGone() {
        mProgressBar.setVisibility(View.GONE);
        mGetJsonString.stopRequest();
    }
}
