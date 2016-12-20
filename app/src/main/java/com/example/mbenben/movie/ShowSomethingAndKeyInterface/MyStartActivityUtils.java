package com.example.mbenben.movie.ShowSomethingAndKeyInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

import com.example.mbenben.movie.LoginActivity;
import com.example.mbenben.movie.Movie.Activity_movie;
import com.example.mbenben.movie.RegisterAndForgetPassword.RegisterActivity;
import com.example.mbenben.movie.RegisterAndForgetPassword.ForgetPasswordActivity;

/**
 * Created by alone on 2016/10/16.
 */
public class MyStartActivityUtils implements ShareAndBundleKey {
    /*
    * 这个只是测试而已
    * */
    public static void startMainUIActivity(Activity activity, String phone) {
       Intent intent = new Intent(activity, Activity_movie.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_KEY, phone);
        intent.putExtras(bundle);
        activity.startActivity(intent);

    }

    /*
    * 启动注册的窗口
    * */
    public static void startRegisterActivity(Activity activity) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        if (Build.VERSION.SDK_INT >= 21) {
            activity.startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    /*
    * 启动忘记密码的Activity
    * */
    public static void startForgetPassword(Activity activity, String phone) {
        Intent intent = new Intent(activity, ForgetPasswordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            activity.startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity).toBundle());
        } else {
            activity.startActivity(intent);
        }
    }

    /*
    * 开始界面进入Activity，注意是不带参数的
    * */
    public static void startLoginActivityWithNoBundle(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }

    /*
    * 退出到登陆界面，带个参数，方便跳过判断是否保存了密码
    *
    * */
    public static void exitToLoginActivity(Activity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSave", false);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

}
