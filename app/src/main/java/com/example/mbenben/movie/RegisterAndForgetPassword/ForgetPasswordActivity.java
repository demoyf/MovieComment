package com.example.mbenben.movie.RegisterAndForgetPassword;
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

import com.example.mbenben.movie.LoadDataAndVerCode.GetURL;
import com.example.mbenben.movie.LoadDataAndVerCode.GetVerCode;
import com.example.mbenben.movie.LoadDataAndVerCode.GetVerCodeResult;
import com.example.mbenben.movie.LoadDataAndVerCode.VolleyGetJsonString;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.MyShowSomthingUtil;
import com.example.mbenben.movie.R;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by alone on 2016/10/20.
 */
public class ForgetPasswordActivity extends AppCompatActivity implements GetVerCodeResult,OnUpdatePassword{
    private EditText enterVerCodeEdit,passwordEdit, confrimEdit;
    private Button getVerCodeButton, updateButton;
    private GetVerCode mGetVerCode;
    private String phoneString, passwordString;
    private boolean isSend = false;
    private int cur = 60;
    private VolleyGetJsonString mGetJsonString;
    private ProgressBar mProgressBar;
    private ImageView mImageView;
    //  处理改变视图的Handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SEND_SUCCESS) {
                //       表明已经发送验证码
                if (cur == 60) {
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
                    MyShowSomthingUtil.showToastShort(ForgetPasswordActivity.this, error_detial);
                    String status = errorInfo.getString("status");
                    if (status.equals("603") || status.equals("457") || status.equals("602") ||
                            status.equals("604") || status.equals("601") || "462".equals(status)||
                    "463".equals(status)) {
                        cur = -1;
                        getVerCodeButton.setText("重新获取");
                        getVerCodeButton.setEnabled(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == SUBMIT_SUCCESS) {
                updatePassword();
            }
        }
    };

    /*
    * 验证成功，调用修改密码的请求
    * */
    private void updatePassword() {
        mProgressBar.setVisibility(View.VISIBLE);
        String url = GetURL.getUpdatePasswordURL(phoneString, passwordString);
        mGetJsonString.updatePassword(url, this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hide();
        setContentView(R.layout.forget_password_layout);
        Bundle bundle = getIntent().getExtras();
        initView();
        initEvent();
        if (bundle != null) {
            phoneString = bundle.getString("phone");
            if (!TextUtils.isEmpty(phoneString)) {
                mGetVerCode.sendVerCode(phoneString);
                mHandler.sendEmptyMessage(SEND_SUCCESS);
            }
        }

    }
    /*
    * 隐藏标题栏和设置进入的动画，注意是在5.0之后才有这个动画
    * */
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
        mGetVerCode = new GetVerCode(this);
        mGetVerCode.initSdk();
        mGetVerCode.registerForgetPasswordEventHandler(this);
        mGetJsonString = new VolleyGetJsonString(this);
        getVerCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 判断传入的电话号码不为空
                * */
                if (!TextUtils.isEmpty(phoneString)) {
                    mGetVerCode.sendVerCode(phoneString);
                    cur = 60;
                    mHandler.sendEmptyMessage(SEND_SUCCESS);
                    isSend = false;
                }
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * 短信已经发送了才进入下面的逻辑
                * */
                if (isSend) {
                    /*
                    * 确保验证码的正确
                    * */
                    String verCode = enterVerCodeEdit.getText().toString();
                    if (TextUtils.isEmpty(verCode)) {
                        MyShowSomthingUtil.showToastShort(ForgetPasswordActivity.this, "验证码不能为空");
                        return;
                    }
                    if (verCode.length() < 4) {
                        MyShowSomthingUtil.showToastShort(ForgetPasswordActivity.this, "验证码输入错误");
                        return;
                    }
                    /*
                    * 判断密码不为空，并且长度大于等于6，两次输入一致
                    * */
                    passwordString = passwordEdit.getText().toString();
                    String confirm = confrimEdit.getText().toString();
                    if ( TextUtils.isEmpty(passwordString) ||
                            TextUtils.isEmpty(confirm)) {
                        MyShowSomthingUtil.showToastShort(ForgetPasswordActivity.this, "密码不能为空");
                        return;
                    }
                    if (!passwordString.equals(confirm)) {
                        MyShowSomthingUtil.showToastShort(ForgetPasswordActivity.this,
                                "两次输入的密码不一致");
                        return;
                    }
                    if (passwordString.length() < 6) {
                        MyShowSomthingUtil.showToastShort(ForgetPasswordActivity.this,
                                "密码至少包含6个字符");
                        return;
                    }
                    /*
                    * 调用验证了
                    * */
                    mGetVerCode.submitVerCode(phoneString, verCode);
                } else {
                    MyShowSomthingUtil.showToastShort(ForgetPasswordActivity.this, "等待验证码发送成功");
                    return;
                }
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView() {
        enterVerCodeEdit = (EditText) findViewById(R.id.forget_ver_edit);
        passwordEdit = (EditText) findViewById(R.id.foget_password_edit);
        confrimEdit = (EditText) findViewById(R.id.forget_confirm);
        getVerCodeButton = (Button) findViewById(R.id.foget_get_ver_code_button);
        updateButton = (Button) findViewById(R.id.update_button);
        mProgressBar = (ProgressBar) findViewById(R.id.forget_progress);
        mImageView = (ImageView) findViewById(R.id.forget_back_to);
        MyShowSomthingUtil.setEditTextFilter(enterVerCodeEdit, 4);
        MyShowSomthingUtil.setEditTextFilter(passwordEdit, 16);
        MyShowSomthingUtil.setEditTextFilter(confrimEdit, 16);
    }

    /*
    * 短信发送成功
    * */
    @Override
    public void onSendVerCodeSuccess(boolean load) {
        isSend = true;
    }
    /*
    * 失败，获取详细信息，并且显示
    * */
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
    * 销毁，注销一些东西
    * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGetJsonString.StopRequestQueue();
        mGetVerCode.unForgetregister();
    }
    /*
    * 修改成功
    * */
    @Override
    public void onUpdateSuccess() {
        mProgressBar.setVisibility(View.GONE);
        MyShowSomthingUtil.showToastShort(this, "修改成功");
        onBackPressed();
    }
    /*
    * 修改失败
    * */
    @Override
    public void onUpdateError() {
        mProgressBar.setVisibility(View.GONE);
        MyShowSomthingUtil.showToastShort(this, "修改失败");
    }
}
