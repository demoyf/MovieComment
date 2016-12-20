package com.example.mbenben.movie.RegisterAndForgetPassword;

/**
 * Created by alone on 2016/10/17.
 * 注册的几个回调接口
 */
public interface LoadRegisterResult {
    /*
    *注册成功
    * */
    public void registerSuccess(String result);
    /*
    * 注册失败
    * */
    public void registerError();
    /*
    *账户存在与否
    * */
    public void phoneIsNull(boolean isnull);

}
