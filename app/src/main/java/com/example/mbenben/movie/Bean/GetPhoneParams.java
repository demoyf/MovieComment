package com.example.mbenben.movie.Bean;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by alone on 2016/11/14.
 */
public class GetPhoneParams {
    private static DisplayMetrics metrics;
    public static int getPhoneWidth(Context context) {
        if (metrics == null) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
        return metrics.widthPixels;
    }

    public static int getPhoneHeight(Context context) {
        if (metrics == null) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
        return metrics.heightPixels;
    }
    public static int dp2px(Context context,int dp) {
        if (metrics == null) {
            metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
        }
        return (int) (dp * metrics.density);
    }

}
