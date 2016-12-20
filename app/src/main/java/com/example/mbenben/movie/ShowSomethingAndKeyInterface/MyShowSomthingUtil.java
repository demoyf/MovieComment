package com.example.mbenben.movie.ShowSomethingAndKeyInterface;

import android.content.Context;
import android.text.InputFilter;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by alone on 2016/10/13.
 * 自定义封装的LogUtil，方便快速的去除所有的Log
 */
public class MyShowSomthingUtil {
    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int CUR = 0;
    /*
    * 方法都太简单，不过多注释
    * */
    public static void V(String tag, String infomation) {
        if (VERBOSE > CUR) {
            Log.v(tag, infomation);
        }
    }
    public static void D(String tag, String infomation) {
        if (DEBUG > CUR) {
            Log.d(tag, infomation);
        }
    }
    public static void I(String tag, String infomation) {
        if (INFO > CUR) {
            Log.i(tag, infomation);
        }
    }
    /*
    * 简单的封装，让Toast显示简单一点
    * */
    public static void showToastShort(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
    public static void showToastLong(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }
    /*
    * 设置输入框的长度
    * */
    public static void setEditTextFilter(EditText editText, int length) {
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }
}
