package com.example.mbenben.movie.LoadDataAndVerCode;

import android.content.Context;
import android.util.Log;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
/**
 * Created by alone on 2016/10/15.
 */
public class GetVerCode {
    private final String appKey = "181bcd3d83836";
    private final String appSecret = "cf95afe5926d93a8646a0b15d35f413e";
    private EventHandler eventHandler;
    private GetVerCodeResult mGetVerCodeResult;
    private Context mContext;
    private EventHandler forgetEventHandler;
    public GetVerCode(Context context) {
        mContext = context;
    }
    /*
    * 必要步骤，先初始化
    * */
    public void initSdk() {
        SMSSDK.initSDK(mContext, appKey, appSecret);
    }
    /*
    *
    * 初始化负责快捷登陆和注册的eventHandler
    * */
    public void initEventHandler(GetVerCodeResult code) {
        mGetVerCodeResult = code;
        if (eventHandler != null) {
            SMSSDK.unregisterEventHandler(eventHandler);
        }
        //        注册一个eventHandler
        eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
//                            咱还是短信验证，不智能好
                          if (mGetVerCodeResult != null) {
//                            发送成功，回调
                              mGetVerCodeResult.onSendVerCodeSuccess(true);
                          }
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        if (mGetVerCodeResult != null) {
//                            提交成功，回调
                            mGetVerCodeResult.onSubmitSuccess();
                        }
                    }
                } else {
//                    失败，回调
                    if (mGetVerCodeResult!=null){
                        if (data instanceof String) {
                            Log.d("xyf", data + "");
                        } else {
                            mGetVerCodeResult.onSendVerCodeError(data);
                        }
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
    }

    /*
    * 初始化忘记密码的eventHandler
    * */
    public void registerForgetPasswordEventHandler(final GetVerCodeResult codeResult) {
        forgetEventHandler= new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                super.afterEvent(event, result, data);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
//                            咱还是短信验证，不智能好
                        if (codeResult != null) {
//                            发送成功，回调
                            codeResult.onSendVerCodeSuccess(true);
                        }
                    } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        if (codeResult != null) {
//                            提交成功，回调
                            codeResult.onSubmitSuccess();
                        }
                    }
                } else {
//                    失败，回调
                    if (codeResult!=null)
                        codeResult.onSendVerCodeError(data);
                }
            }
        };
        SMSSDK.registerEventHandler(forgetEventHandler);
    }
    /*
    * @param phone，要发送的电话号码
    *
    * */
    public void sendVerCode(String phone) {
        SMSSDK.getVerificationCode("86", phone);
    }

    /*
    *@param phone code
    * 电话和验证码
    * 验证输入的是否正确
    * */
    public void submitVerCode(String phone, String code) {
        SMSSDK.submitVerificationCode("86", phone, code);
    }
    /*
    * 注销的方法
    * */
    public void unregister(){
        if (eventHandler!=null)
        SMSSDK.unregisterEventHandler(eventHandler);
    }
    /*
    * 注销忘记密码
    * */
    public void unForgetregister() {
        if (forgetEventHandler != null) {
            SMSSDK.unregisterEventHandler(forgetEventHandler);
        }
    }
}
