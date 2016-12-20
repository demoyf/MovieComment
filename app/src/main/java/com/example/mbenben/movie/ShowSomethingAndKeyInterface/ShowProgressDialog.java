package com.example.mbenben.movie.ShowSomethingAndKeyInterface;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mbenben.movie.Bean.GetPhoneParams;
import com.example.mbenben.movie.R;

/**
 * Created by alone on 2016/12/7.
 */
public class ShowProgressDialog {
    Dialog alertDialog;
    public ShowProgressDialog(Context context) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.show_progress_dialog, null);
        alertDialog = new Dialog(context, R.style.MyDialogStyle);
        alertDialog.setContentView(view);
        Window dialogWindow = alertDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity( Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        int height = GetPhoneParams.getPhoneHeight(context) / 2 - 50;
        int width = GetPhoneParams.getPhoneWidth(context) / 2 + 50;
        lp.y = height;//设置Dialog距离底部的距离
        lp.height = 200;
        lp.width = width;
        alertDialog.setCanceledOnTouchOutside(false);
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }

    public void show() {
        alertDialog.show();
    }

    public void dismiss() {
        alertDialog.dismiss();
    }
    public boolean isShowing() {
        return alertDialog.isShowing();
    }
}
