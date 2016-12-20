package com.example.mbenben.movie.LoadDataAndVerCode;

/**
 * Created by alone on 2016/10/15.
 * 获取验证码的回调接口
 */
public interface GetVerCodeResult {
    public final int SEND_SUCCESS = 0X11;
    public final int SEND_ERROR = 0X12;
    public final int SUBMIT_SUCCESS = 0x13;
    public void onSendVerCodeSuccess(boolean load);
    public void onSendVerCodeError(Object data);
    public void onSubmitSuccess();
}
