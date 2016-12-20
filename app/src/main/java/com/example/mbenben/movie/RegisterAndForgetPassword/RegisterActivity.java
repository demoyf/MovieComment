package com.example.mbenben.movie.RegisterAndForgetPassword;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.mbenben.movie.LoadDataAndVerCode.GetVerCodeResult;
import com.example.mbenben.movie.LoadDataAndVerCode.VolleyGetJsonString;
import com.example.mbenben.movie.LoadDataAndVerCode.GetURL;
import com.example.mbenben.movie.LoadDataAndVerCode.GetVerCode;
import com.example.mbenben.movie.R;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.DialogClick;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.MyShowSomthingUtil;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.ShowPhoneDialog;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by alone on 2016/10/16.
 * 注册的Activity
 */
public class RegisterActivity extends AppCompatActivity
        implements LoadRegisterResult,GetVerCodeResult,DialogClick {
//    校验成功
    private boolean isSubmitSuccess = false;
    private int cur = 60;
//    一些控件
    private EditText phoneEdit,passwordEdit, confirmEdit,verCodeEdit;
    private Button registerButton,getVerCodeButton;
    private ImageView backImage;
//    Volley的封装类
    private VolleyGetJsonString mGetJsonString;
    private String phoneString,passwordSring, confirmString;
    private ProgressBar mProgressBar;
//    获取验证码的封装对象
    private GetVerCode mGetVerCode;
    private String myVerCode;
//    短信已经发送
    private boolean isSend = false;
//    第一次进入的时候，要初始化
    boolean isFirst = false;
    private ShowPhoneDialog mShowPhoneDialog;
    //  处理改变视图的Handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SEND_SUCCESS) {
                //       表明已经发送验证码
                if (cur == 60) {
//                    Log.d("xyf", "send success");
                    getVerCodeButton.setEnabled(false);
                }
                cur--;
                if (cur > 0) {
                    getVerCodeButton.setText(cur + " S");
                    mHandler.sendEmptyMessageDelayed(SEND_SUCCESS, 1000);
                } else {
                    getVerCodeButton.setText("重新获取");
                    cur = 60;
                    getVerCodeButton.setEnabled(true);
                }
            } else if (msg.what == SEND_ERROR) {
                //  发送失败，因为懒得去判断是提交失败还是发送失败，所以在发送成功之后，加多个回调
                JSONObject errorInfo = (JSONObject) msg.obj;
                String error_detial = null;
                try {
                    error_detial = errorInfo.getString("detail");
                    MyShowSomthingUtil.showToastShort(RegisterActivity.this, error_detial);
                    String status = errorInfo.getString("status");
                    if (status.equals("603") || status.equals("457")|| status.equals("602")||
                            status.equals("604")||status.equals("601")|| "462".equals(status)||
                            "463".equals(status)) {
                        cur = -1;
                        getVerCodeButton.setText("重新获取");
                        getVerCodeButton.setEnabled(true);
                        isSubmitSuccess = false;
                    }
                    mProgressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == SUBMIT_SUCCESS) {
                isSubmitSuccess = true;
                registerMy();
            }
        }
    };
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hide();
        setContentView(R.layout.register_activity_layout);
        initView();
        initEvent();
    }

    private void hide() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Slide());
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void initEvent() {
        /*
        退出图标的点击事件
        * */
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                if (isSubmitSuccess) {
//                    已经验证成功了
                    registerMy();
                    return;
                }
//                短信已经发送
                if (isSend) {
                        myVerCode = verCodeEdit.getText().toString().trim();
                        if (TextUtils.isEmpty(myVerCode)) {
                            MyShowSomthingUtil.showToastShort(RegisterActivity.this,
                                    "请先输入验证码");
                            mProgressBar.setVisibility(View.GONE);
                        } else {
                            if (myVerCode.length() < 4) {
                                MyShowSomthingUtil.showToastShort(RegisterActivity.this,
                                        "验证码输入错误");
                                mProgressBar.setVisibility(View.GONE);
                                return;
                            }
                            passwordSring = passwordEdit.getText().toString().trim();
                            confirmString = confirmEdit.getText().toString().trim();
                            if (TextUtils.isEmpty(passwordSring)||TextUtils.isEmpty(confirmString)) {
                                MyShowSomthingUtil.showToastShort(RegisterActivity.this, "密码不能为空");
                                mProgressBar.setVisibility(View.GONE);
                                return;
                            }
                            if (passwordSring.length() < 6) {
                                mProgressBar.setVisibility(View.GONE);
                                MyShowSomthingUtil.showToastShort(RegisterActivity.this, "密码至少包含6个字符");
                                return;
                            }
                            if (!confirmString.equals(passwordSring)) {
                                mProgressBar.setVisibility(View.GONE);
                                MyShowSomthingUtil.showToastShort(RegisterActivity.this,
                                        "两次输入的密码不一致");
                                return;
                            }
                            mGetVerCode.submitVerCode(phoneString, myVerCode);
                        }
                    }
                    else {
                    mProgressBar.setVisibility(View.GONE);
                        MyShowSomthingUtil.showToastShort(RegisterActivity.this,
                                "请先获取验证码");
                }
            }
        });
        getVerCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFirst) {
                    mGetVerCode.initSdk();
                    mGetVerCode.initEventHandler(RegisterActivity.this);
                    isFirst = true;
                }
                if (!isSubmitSuccess) {
                    judgeIsNull();
                }
            }
        });
    }
    /*
    * 调用注册的请求
    * */
    private void registerMy() {
        String url = GetURL.getRegUrl(phoneString, passwordSring);
        mGetJsonString.getRegister(url);
    }
    /*
    * 判断是否为空
    * */
    private void judgeIsNull() {
        phoneString = phoneEdit.getText().toString().trim();
        if (TextUtils.isEmpty(phoneString)) {
            MyShowSomthingUtil.showToastShort(this, "手机号不能为空");
            return;
        }
        if (phoneString.length() != 11) {
            MyShowSomthingUtil.showToastShort(this, "手机号输入错误");
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        String url = GetURL.getRegIsNullURL(phoneString);
        mGetJsonString.getIsNull(url);
    }
    /*
    * 初始化控件
    * */
    private void initView() {
        mGetJsonString = new VolleyGetJsonString(this);
        mGetJsonString.setLoadRegisterResult(this);
        mGetVerCode = new GetVerCode(this);
        phoneEdit = (EditText) findViewById(R.id.reg_phone_edit);
        passwordEdit = (EditText) findViewById(R.id.reg_password_edit);
        confirmEdit = (EditText) findViewById(R.id.reg_confirm_edit);
        verCodeEdit = (EditText) findViewById(R.id.reg_enter_ver_edit);
        getVerCodeButton = (Button) findViewById(R.id.reg_ver_button);
        registerButton = (Button) findViewById(R.id.real_register_button);
        backImage = (ImageView) findViewById(R.id.back_to);
        mProgressBar = (ProgressBar) findViewById(R.id.register_progress);
        mShowPhoneDialog = new ShowPhoneDialog(this, this);
        MyShowSomthingUtil.setEditTextFilter(verCodeEdit, 4);
        MyShowSomthingUtil.setEditTextFilter(phoneEdit, 11);
        MyShowSomthingUtil.setEditTextFilter(passwordEdit, 16);
        MyShowSomthingUtil.setEditTextFilter(confirmEdit, 16);
    }
    /*
    * 注册的数据返回了
    * */
    @Override
    public void registerSuccess(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String code = jsonObject.getString("register");
            if (code.equals("1")) {
                MyShowSomthingUtil.showToastShort(this, "注册成功");
                finish();
            } else {
                MyShowSomthingUtil.showToastShort(this, "注册失败");
                mProgressBar.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            MyShowSomthingUtil.showToastShort(this,"服务器连接异常");
            e.printStackTrace();
        }
        mProgressBar.setVisibility(View.GONE);
    }
    /*
    * 注册的请求异常
    * */
    @Override
    public void registerError() {
        mProgressBar.setVisibility(View.GONE);
        MyShowSomthingUtil.showToastShort(this,"服务器连接异常");
    }

    @Override
    public void onBackPressed() {
        if (mProgressBar.getVisibility() == View.GONE) {
            finish();
        } else {
            mGetJsonString.stopRequest();
            mProgressBar.setProgress(View.GONE);
        }
    }

    @Override
    public void phoneIsNull(boolean isnull) {
        mProgressBar.setVisibility(View.GONE);
//        Log.d("xyf", "is null");
        if (isnull) {
            mShowPhoneDialog.setPhoneString(phoneString);
            mShowPhoneDialog.showDialog();
        } else {
            MyShowSomthingUtil.showToastShort(this, "该号码已经被注册过");
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*
        *注销，防止内存泄漏
        * */
        mGetJsonString.StopRequestQueue();
        mGetVerCode.unregister();
    }

    @Override
    public void onSendVerCodeSuccess(boolean load) {
        isSend = true;
    }

    @Override
    public void onSendVerCodeError(Object data) {
        try {
            Throwable throwable = (Throwable) data;
            String message = throwable.getMessage();
            JSONObject object = new JSONObject(message);
            Message result = new Message();
            result.what = SEND_ERROR;
            result.obj = object;
            mHandler.sendMessage(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
    * 校验成功
    * */
    @Override
    public void onSubmitSuccess() {
        mHandler.sendEmptyMessage(SUBMIT_SUCCESS);
    }
    /*
    * 弹出框确定按钮被点击
    * */
    @Override
    public void yesButtonClilck() {
        mGetVerCode.sendVerCode(phoneString);
        cur = 60;
        mHandler.sendEmptyMessage(SEND_SUCCESS);
    }
}
