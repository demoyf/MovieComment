package com.example.mbenben.movie.ShowSomethingAndKeyInterface;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.mbenben.movie.R;

/**
 * Created by alone on 2016/10/20.
 */
public class ShowPhoneDialog {
    private Dialog dialog;
    private View inflate;
    private Context mContext;
    private String mPhoneString;

    public void setPhoneString(String phoneString) {
        mPhoneString = phoneString;
    }
    private Button yesButton, noButton;
    private DialogClick mDialogClick;
    private TextView mTextView;
    public ShowPhoneDialog(Context context, DialogClick dialogClick) {
        mContext = context;
        mDialogClick = dialogClick;
        initView();
        initEvent();
    }

    private void initEvent() {
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                被点击的时候，回调
                mDialogClick.yesButtonClilck();
                dialog.dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void initView() {
        dialog = new Dialog(mContext, R.style.MyDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_dialog_layout, null);
        yesButton = (Button) inflate.findViewById(R.id.pop_ver_yes_button);
        noButton = (Button) inflate.findViewById(R.id.pop_ver_no_button);
        mTextView = (TextView) inflate.findViewById(R.id.pop_show_phone_text);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        mTextView.setText(mPhoneString);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 300;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
//        点击其他地方不会被隐藏
        dialog.setCanceledOnTouchOutside(false);
    }
//    设置电话号码，并且显示弹出框
    public void showDialog() {
        if (!TextUtils.isEmpty(mPhoneString)) {
            mTextView.setText(mPhoneString);
            dialog.show();
        }
    }
}
