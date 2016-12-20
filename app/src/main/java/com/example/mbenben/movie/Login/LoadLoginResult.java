package com.example.mbenben.movie.Login;

/**
 * Created by alone on 2016/10/12.
 * 回调接口，登陆成功或者是登陆失败等
 */
public interface LoadLoginResult {
    //    成功获取到JSON数据，返回结果，再后续判断
    public void loginLoadSuccess(String s);

    //    获取登陆的JSON失败
    public void loginLoadFail();

    //   判断是否为空
    public void judgeIsNull(boolean isnull);
    
}
