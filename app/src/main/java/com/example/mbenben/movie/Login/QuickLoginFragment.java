package com.example.mbenben.movie.Login;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import com.example.mbenben.movie.LoadDataAndVerCode.GetVerCodeResult;
import com.example.mbenben.movie.LoadDataAndVerCode.GetVerCode;
import com.example.mbenben.movie.R;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.DialogClick;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.MyShowSomthingUtil;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.MyStartActivityUtils;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.ShowPhoneDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alone on 2016/10/14.
 */
public class QuickLoginFragment extends Fragment implements GetVerCodeResult,DialogClick {
    private boolean isInit = false;
    private View view;
    //显示的时间，60s可获取一次
    private int cur = 60;
    private EditText phoneEdit,verCodeEdit;
    private Button getVerCodeButton, loginButton;
    // 判断是否已经发送了短信验证码，没发送和已经发送的错误提示不同
    private boolean isSend = false;
    private Activity mContext;
    //  验证的实例
    private GetVerCode mGetVerCode;
    private String phone,verCode;
    private ShowPhoneDialog mShowPhoneDialog;
    //  处理改变视图的Handler
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SUBMIT_SUCCESS) {
                if (mContext!=null){
    //             goto  跳转到主界面了，注意 我还没有在这里注销eventHandler.
                MyStartActivityUtils.startMainUIActivity(mContext, phone);
                mGetVerCode.unregister();
                mContext.finish();
                }
            } else if (msg.what == SEND_SUCCESS) {
    //                表明已经发送验证码
                if (cur > 0) {
                    getVerCodeButton.setText(cur + " S");
                    cur--;
                    mHandler.sendEmptyMessageDelayed(SEND_SUCCESS, 1000);
                } else {
                    getVerCodeButton.setText("重新获取");
                    cur = 60;
                    getVerCodeButton.setEnabled(true);
                }
            } else if (msg.what == SEND_ERROR) {
    //                发送失败，因为懒得去判断是提交失败还是发送失败，所以在发送成功之后，加多个回调
                JSONObject errorInfo = (JSONObject) msg.obj;
                String error_detial = null;
                try {
                    /*
                    * 这里获取错误信息
                    * */
                    error_detial = errorInfo.getString("detail");
                    MyShowSomthingUtil.showToastShort(mContext, error_detial);
                    String status = errorInfo.getString("status");
                    if (status.equals("603") || status.equals("457")|| status.equals("602")||
                            status.equals("604")||status.equals("601")|| "462".equals(status)||
                            "463".equals(status)) {
                        cur = -1;
                        isSend = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.quick_login_layout, container, false);
        initView();
        initEvent();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    //        注销
        mGetVerCode.unregister();
    }

    private void initEvent() {
        getVerCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phoneEdit.getText().toString().trim();
                if (!TextUtils.isEmpty(phone) && phone.length() == 11) {
                    if (!isInit) {
                        /*
                        * 被点击的时候，才初始化短信SDK
                        * */
                        mGetVerCode.initSdk();
                        mGetVerCode.initEventHandler(QuickLoginFragment.this);
                        isInit = true;
                    }
                    /*
                    * 注入电话号码，显示弹出框
                    * */
                    mShowPhoneDialog.setPhoneString(phone);
                    mShowPhoneDialog.showDialog();
                } else {
                    MyShowSomthingUtil.showToastShort(mContext, "请填写正确的手机号");
                }
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verCode = verCodeEdit.getText().toString();
                /*
                * 手机号码一定不能为空
                * */
                if (TextUtils.isEmpty(phone)) {
                    MyShowSomthingUtil.showToastShort(mContext, "请输入手机号");
                    return;
                }
                /*
                * 校验验证码
                * */
                if (!TextUtils.isEmpty(verCode) && !TextUtils.isEmpty(phone)) {
                    if (verCode.length() < 4) {
                        MyShowSomthingUtil.showToastShort(mContext, "验证码输入错误");
                        return;
                    }
                    if (isSend)//6，如果发送成功的方法还没有回调，那么就不管点击的事件
                    mGetVerCode.submitVerCode(phone, verCode);
                } else {
                    MyShowSomthingUtil.showToastShort(mContext, "请输入验证码");
                    return;
                }
            }
        });
    }

    private void initView() {
        mContext = getActivity();
        mGetVerCode = new GetVerCode(mContext);
        phoneEdit = (EditText) view.findViewById(R.id.quick_phone_edit);
        //设置长度
        MyShowSomthingUtil.setEditTextFilter(phoneEdit, 11);
        verCodeEdit = (EditText) view.findViewById(R.id.enter_ver_code_edit);
        MyShowSomthingUtil.setEditTextFilter(verCodeEdit, 4);
        getVerCodeButton = (Button) view.findViewById(R.id.get_ver_code_button);
        loginButton = (Button) view.findViewById(R.id.quick_login_button);
        mShowPhoneDialog = new ShowPhoneDialog(mContext, this);
        MyShowSomthingUtil.setEditTextFilter(phoneEdit, 11);
        MyShowSomthingUtil.setEditTextFilter(verCodeEdit, 4);
    }

    /*
    * 短信发送成功的回调方法
    * */
    @Override
    public void onSendVerCodeSuccess(boolean load) {
        isSend = load;
    }

    /*
    * 短信发送失败的回调方法，这里顺带上了验证失败
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
            MyShowSomthingUtil.showToastShort(mContext, "网络连接异常");
        }
    }

    /*
    * 提交验证码成功
    *
    * */
    @Override
    public void onSubmitSuccess() {
        mHandler.sendEmptyMessage(SUBMIT_SUCCESS);
    }
    /*
    * 注销的公共方法，方便在登陆的碎片中，销毁掉监听器，防止出现点击的时候，跳转到这个界面
    * */
    public void unregister() {
        if (isInit) {
            mGetVerCode.unregister();
        }
    }
    /*
    * 弹出框确定按钮被点击
    * */
    @Override
    public void yesButtonClilck() {
        mGetVerCode.sendVerCode(phone);
        mHandler.sendEmptyMessage(SEND_SUCCESS);
        isSend = false;
        getVerCodeButton.setEnabled(false);
    }

    /*
    * 重新回来的时候，因为已经被注销了，所以将初始化的布尔值设置为false 可以重新注册监听
    * */
    @Override
    public void onResume() {
        super.onResume();
        isInit = false;
    }
}
