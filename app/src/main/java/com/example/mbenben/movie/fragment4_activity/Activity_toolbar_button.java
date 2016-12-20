package com.example.mbenben.movie.fragment4_activity;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.mbenben.movie.Adapter.Fragment4_toolbar_button_recycleview_adapter;
import com.example.mbenben.movie.Fragment.Fragment4;
import com.example.mbenben.movie.Http.LoadJSONData;
import com.example.mbenben.movie.Http.MyOkHttpUpload;
import com.example.mbenben.movie.Http.SendDataToServer;
import com.example.mbenben.movie.Http.UploadResult;
import com.example.mbenben.movie.Interface.OnItemClickListener;
import com.example.mbenben.movie.LoadDataAndVerCode.GetURL;
import com.example.mbenben.movie.Movie.Activity_movie;
import com.example.mbenben.movie.Movie.fragment4_toolbar_button_recycleview_itemdecoration;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.MyStartActivityUtils;
import com.example.mbenben.movie.Http.OnLoadDataResult;
import com.example.mbenben.movie.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.okhttp.Request;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;

/**
 * Created by MBENBEN on 2016/11/2.
 */
public class Activity_toolbar_button extends AppCompatActivity implements OnClickListener{
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView1;
    private RecyclerView mRecyclerView2;
    private String[] mText;
    private String[] mText2;
    private String[] mText3;
    private String[] mText4;
   // private int mimages;
    private Fragment4_toolbar_button_recycleview_adapter recycleAdapter;
    private Fragment4_toolbar_button_recycleview_adapter recycleAdapter2;
    //头像的布局
    private RelativeLayout mRelativeLayout;

    //对话框
    private Dialog mDialog;
    //加载布局
    View mView;
    //初始化照相,选择相册的组件
    private TextView choosePhoto;
    private TextView takePhoto;

    //拍照的变量
    public static final int TAKE_PHOTO = 1;
    public static final int CROP_PHOTO = 2;
    public static final int IMAGE_REQUEST_CODE = 3;
    private Uri imageUri;
    private RoundedImageView touxiang;
    private File outputImage;
    File tempFile;

   private Handler mHandler;

   public static Handler mHandler1;
     Bitmap bitmap=null;
    final LoadJSONData loadJSONData = new LoadJSONData(this);

    //修改信息对话框的确定和取消两个按钮
   private Button button_true;
    private Button button_false;
   //编辑框界面
    private TextInputLayout mTextInputLayout;
    //对话框标题
    private TextView mTittle;
    //编辑框
   private EditText mEditText;

    //性别选择
    RelativeLayout man;
    RelativeLayout woman;
    RadioButton man_radio;
    RadioButton woman_radio;
    TextView Tittle_sex;
    //手机号码
    String phone;
    //退出的按钮
    private  Button fragment4_toolbar_button_exitbutton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment4_toolbar_button);
        mHandler=new MyHandler(this);
        phone=getIntent().getStringExtra("phone");
        initData();
        initView();

     mHandler1=new Handler(){
         @Override
         public void handleMessage(Message msg) {
                 if(msg.what==8){
                     String str= (String) msg.obj;
                     mText4[0]=str;
                     recycleAdapter2.notifyDataSetChanged();
                 }
         }
     };
    }

    String str = null;
    private void initData() {
        mText = new String[]{"用户名", "性别","地址", "出生日期", "手机号码" ,"修改密码"};
        mText2 = new String[]{"喜爱电影类型", "个性签名"};
       mText3 = new String[]{"未填写", "未填写", "未填写", "未填写", "未填写","未填写"};
        mText4 = new String[]{"未填写", "未填写"};
       // mimages = R.drawable.fragment4_arrow;
    }

    private void initView() {
        fragment4_toolbar_button_exitbutton= (Button) findViewById(R.id.fragment4_exit_button);
        mToolbar = (Toolbar) findViewById(R.id.fragment4_toolbar_button_title);
        mRecyclerView1 = (RecyclerView) findViewById(R.id.fragment4_toolbar_button_recycleview);
        mRecyclerView2 = (RecyclerView) findViewById(R.id.fragment4_toolbar_button_recycleview2);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.fragment4_toolbar_button_headportrait);
        touxiang = (RoundedImageView) findViewById(R.id.touxiang2);
        mToolbar.setTitle("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary, this.getTheme()));
        } else {
            mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        setSupportActionBar(mToolbar);
        //设置toolbar左边返回键
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        inView();
        recycleAdapter = new
                Fragment4_toolbar_button_recycleview_adapter(Activity_toolbar_button.this, mText, mText3);
        recycleAdapter2 =
                new Fragment4_toolbar_button_recycleview_adapter(Activity_toolbar_button.this, mText2, mText4);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Activity_toolbar_button.this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(Activity_toolbar_button.this);
        //设置布局管理器

        mRecyclerView1.setLayoutManager(layoutManager);
        mRecyclerView2.setLayoutManager(layoutManager2);
        //设置垂直布局，默认的
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //设置Adapter
        mRecyclerView1.setAdapter(recycleAdapter);
        mRecyclerView1.addItemDecoration(new fragment4_toolbar_button_recycleview_itemdecoration(Activity_toolbar_button.this));
        mRecyclerView2.setAdapter(recycleAdapter2);
        mRecyclerView2.addItemDecoration(new fragment4_toolbar_button_recycleview_itemdecoration(Activity_toolbar_button.this));

        mRelativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new Dialog(Activity_toolbar_button.this, R.style.fragment4_toolbar_button_headportrait_dialog);
                //填充对话框的布局
                mView = LayoutInflater.from(Activity_toolbar_button.this).inflate(R.layout.fragment4_toolbar_button_headportrait_dialog, null);
                //初始化控件
                choosePhoto = (TextView) mView.findViewById(R.id.choosePhoto);
                takePhoto = (TextView) mView.findViewById(R.id.takePhoto);
                show(20);
                //照相点击事件
                takePhoto.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        takePhoto();
                    }
                });
                choosePhoto.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choosePhoto();
                    }
                });
            }
        });
    fragment4_toolbar_button_exitbutton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(Activity_toolbar_button.this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton("确定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();
        }
    });
        //第一个recycleview的点击事件
        recycleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {
                mDialog = new Dialog(Activity_toolbar_button.this, R.style.fragment4_toolbar_button_headportrait_dialog);
                if (position == 0) {
                        //填充对话框的布局
                        mView = LayoutInflater.from(Activity_toolbar_button.this).inflate(R.layout.fragment4_toolbar_button_name, null);
                        mTextInputLayout = (TextInputLayout) mView.findViewById(R.id.fragment4_toolbar_button_message_TextInputLayout);
                        mTittle = (TextView) mView.findViewById(R.id.fragment4_toolbar_button_message_Tittle);
                        mEditText = (EditText) mView.findViewById(R.id.fragment4_toolbar_button_message_EditText);
                        mTextInputLayout.setCounterMaxLength(25);
                        mEditText.setHint("输入用户名");
                        mTittle.setText("修改用户名");
                        mEditText.addTextChangedListener(new MyTextWatcher(mEditText));
                        show(400);
                        button_true= (Button) mView.findViewById(R.id.fragment4_toolbar_button_message_button_true);
                        button_false= (Button) mView.findViewById(R.id.fragment4_toolbar_button_message_button_false);
                        button_true.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!isNameValid()) {
                                    Toast.makeText(Activity_toolbar_button.this, "用户名错误或者字数超限", Toast.LENGTH_LONG).show();
                                } else {
                                   try{
                                    str = URLEncoder.encode(mEditText.getText().toString(), "utf-8");
                                    String url = GetURL.getUpdateUserNameURL(phone, str);
                                    loadJSONData.SendDataServer(url, new SendDataToServer() {
                                        @Override
                                        public void SendSuccess(String result) {
                                            Message message = new Message();
                                            message.obj = mEditText.getText().toString();
                                            message.what = 3;
                                            mHandler.sendMessage(message);

                                        }
                                        @Override
                                        public void SendloadError() {
                                            Toast.makeText(Activity_toolbar_button.this, "网络异常，无法修改信息", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                       Fragment4 fragment4 = Fragment4.getInstance();
                                       fragment4.updateImage();
                                    mDialog.dismiss();


                       }catch (UnsupportedEncodingException e){
                           e.printStackTrace();
                       }
                                }
                            }
                        });
                    button_false.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mDialog.dismiss();
                        }
                    });
                    }
                else if(position==1){
                    mView = LayoutInflater.from(Activity_toolbar_button.this).inflate(R.layout.fragment4_toolbar_button_sex, null);
                  man= (RelativeLayout) mView.findViewById(R.id.fragment4_toolbar_button_sex_select_man_layout);
                    woman= (RelativeLayout) mView.findViewById(R.id.fragment4_toolbar_button_sex_select_woman_layout);
                    man_radio= (RadioButton) mView.findViewById(R.id.fragment4_toolbar_button_sex_select_man);
                    woman_radio=(RadioButton) mView.findViewById(R.id.fragment4_toolbar_button_sex_select_woman);
                    Tittle_sex= (TextView) mView.findViewById(R.id.fragment4_toolbar_button_message_Tittle_sex);
                     Tittle_sex.setText("修改性别");
                    if(mText3[1].equals("男")){
                        man_radio.setChecked(true);
                        woman_radio.setChecked(false);
                    }
                    else {
                        man_radio.setChecked(false);
                        woman_radio.setChecked(true);
                    }
                    show(400);
                    man.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            man_radio.setChecked(true);
                            woman_radio.setChecked(false);
                            sex_dialog("男");
                        }
                    });
                    woman.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            man_radio.setChecked(false);
                            woman_radio.setChecked(true);
                            sex_dialog("女");
                        }
                    });

                    man_radio.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            man_radio.setChecked(true);
                            woman_radio.setChecked(false);
                            sex_dialog("男");
                        }
                    });
                    woman_radio.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            man_radio.setChecked(false);
                            woman_radio.setChecked(true);
                            sex_dialog("女");
                        }
                    });
                }
                else if(position==2){
                    try{
                        ArrayList<Province> data=new ArrayList<Province>();
                        String json = ConvertUtils.toString(Activity_toolbar_button.this.getAssets().open("city.json"));
                        data.addAll(JSON.parseArray(json,Province.class));
                        final AddressPicker picker=new AddressPicker(Activity_toolbar_button.this,data);
                          picker.setSelectedItem("广东","广州","白云");
                        picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                            @Override
                            public void onAddressPicked(Province province, City city, County county) {
                                try{
                                  final String  temp=province.getAreaName()+city.getAreaName()+county.getAreaName();
                                    str = URLEncoder.encode(temp, "utf-8");
                                    String url = GetURL.getUpdateUserAddressURL(phone, str);
                                    loadJSONData.SendDataServer(url, new SendDataToServer() {
                                        @Override
                                        public void SendSuccess(String result) {
                                            Message message = new Message();
                                            message.obj = temp;
                                            message.what = 5;
                                            mHandler.sendMessage(message);
                                        }
                                        @Override
                                        public void SendloadError() {
                                            Toast.makeText(Activity_toolbar_button.this, "网络异常，无法修改信息", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }catch (UnsupportedEncodingException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                        picker.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                 else if(position==3){
                    DatePicker picker=new DatePicker(Activity_toolbar_button.this,DatePicker.YEAR_MONTH_DAY);
                    picker.setRangeStart(1900,1,1);//开始范围
                    picker.setRangeEnd(2022,1,1);//结束范围
                    String s=mText3[3];
                    String date[]=s.split("-");
                    if(!s.equals("未填写")){
                        picker.setSelectedItem(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]));
                    }
                    else {
                        picker.setSelectedItem(1900,1,1);
                    }
                    picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                        public void onDatePicked(String year, String month, String day) {
                            str=year+"-"+month+"-"+day;
                            String url = GetURL.getUpdateUserBirthdayURL(phone, str);
                            loadJSONData.SendDataServer(url, new SendDataToServer() {
                                @Override
                                public void SendSuccess(String result) {
                                    Message message = new Message();
                                    message.obj = str;
                                    message.what = 6;
                                    mHandler.sendMessage(message);
                                }
                                @Override
                                public void SendloadError() {
                                    Toast.makeText(Activity_toolbar_button.this, "网络异常，无法修改信息", Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    });
                    picker.show();
                }
                else if(position==5){
                    MyStartActivityUtils.startForgetPassword(Activity_toolbar_button.this,phone);
                }

            }
        });
     recycleAdapter2.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onClick(int position) {
             mDialog = new Dialog(Activity_toolbar_button.this, R.style.fragment4_toolbar_button_headportrait_dialog);
             if(position==1){
                 //填充对话框的布局
                 mView = LayoutInflater.from(Activity_toolbar_button.this).inflate(R.layout.fragment4_toolbar_button_name, null);
                 mTextInputLayout = (TextInputLayout) mView.findViewById(R.id.fragment4_toolbar_button_message_TextInputLayout);
                 mTittle = (TextView) mView.findViewById(R.id.fragment4_toolbar_button_message_Tittle);
                 mEditText = (EditText) mView.findViewById(R.id.fragment4_toolbar_button_message_EditText);
                 mTextInputLayout.setCounterMaxLength(20);
                 mEditText.setHint("输入个性签名");
                 mTittle.setText("修改个性签名");
                 mEditText.addTextChangedListener(new MyTextWatcher(mEditText));
                 show(400);
                 button_true= (Button) mView.findViewById(R.id.fragment4_toolbar_button_message_button_true);
                 button_false= (Button) mView.findViewById(R.id.fragment4_toolbar_button_message_button_false);
                 button_true.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         if (!isPersonalizedsignature()) {
                             Toast.makeText(Activity_toolbar_button.this, "字数超限", Toast.LENGTH_LONG).show();
                         } else {
                             try{
                                 str = URLEncoder.encode(mEditText.getText().toString(), "utf-8");
                                 String url = GetURL.getUpdateUserAutographURL(phone, str);
                                 loadJSONData.SendDataServer(url, new SendDataToServer() {
                                     @Override
                                     public void SendSuccess(String result) {
                                         Message message = new Message();
                                         message.obj = mEditText.getText().toString();
                                         message.what = 7;
                                         mHandler.sendMessage(message);
                                     }

                                     @Override
                                     public void SendloadError() {
                                         Toast.makeText(Activity_toolbar_button.this, "网络异常，无法修改信息", Toast.LENGTH_LONG).show();
                                     }
                                 });
                                 mDialog.dismiss();


                             }catch (UnsupportedEncodingException e){
                                 e.printStackTrace();
                             }
                         }
                     }
                 });
                 button_false.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         mDialog.dismiss();
                     }
                 });

             }
             else {
                Intent intent=new Intent(Activity_toolbar_button.this,Favourite.class);
                 startActivity(intent);
             }
         }
     });



        String url = GetURL.getGetUserIconURL(phone);
        loadJSONData.LoadDtaFromServer(url, new OnLoadDataResult() {
            @Override
            public void loadSuccess(String result) {
                String[] imageUrl = loadJSONData.
                        parseStringJSONObject(result, new String[]{GetURL.ICON_URL_JSON_KEY});
                if (!TextUtils.isEmpty(imageUrl[0])) {
                    Log.d("xya", imageUrl[0]);
                    Picasso.with(Activity_toolbar_button.this)
                            .load(imageUrl[0]).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).
                            memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                            .placeholder(R.drawable.touxiang_load)
                            .error(R.drawable.touxiang)
                            .into(touxiang);
                } else {
                    Log.d("xya", "imageUrl " + imageUrl[0]);
                    Picasso.with(Activity_toolbar_button.this).load(R.drawable.touxiang).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE).
                            memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE).into(touxiang);
                }
            }

            public void loadError() {
                Toast.makeText(Activity_toolbar_button.this, "网络异常,获取不到头像", Toast.LENGTH_LONG).show();
            }
        });
    }
    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    MyStartActivityUtils.exitToLoginActivity(Activity_toolbar_button.this);
                    Activity_movie.getActivity().finish();
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    public void sex_dialog(final String sex){
        try{
            str = URLEncoder.encode(sex, "utf-8");
            String url = GetURL.getUpdateUserSexURL(phone, str);
            loadJSONData.SendDataServer(url, new SendDataToServer() {
                @Override
                public void SendSuccess(String result) {
                    Message message = new Message();
                    message.obj = sex;
                    message.what = 4;
                    mHandler.sendMessage(message);
                }
                public void SendloadError() {
                    Toast.makeText(Activity_toolbar_button.this, "网络异常，无法修改性别信息", Toast.LENGTH_LONG).show();
                }
            });
            mDialog.dismiss();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }


    public boolean isNameValid() {
        if(mEditText.getText().toString().trim().equals("")||
                mEditText.getText().toString().trim().isEmpty()){
            mEditText.setError("用户名不能为空");
            mEditText.requestFocus();
            return false;
        }
        if(mEditText.getText().toString().length()>25){
            mEditText.setError("字数超限");
            mEditText.requestFocus();
            return false;
        }
        mTextInputLayout.setErrorEnabled(false);
        return true;

    }
    public boolean isPersonalizedsignature() {
        if(mEditText.getText().toString().length()>25){
            mEditText.setError("字数超限");
            mEditText.requestFocus();
            return false;
        }
        mTextInputLayout.setErrorEnabled(false);
        return true;

    }

    private void inView() {
       // mText3[0]="wjg";
        String url1 = GetURL.getGetUserInfoURL(phone);

        loadJSONData.LoadDtaFromServer(url1, new OnLoadDataResult() {
            @Override
            public void loadSuccess(String result) {
                  JSONObject jsonObject=loadJSONData.JSONArraytoJSONObject(result);
                  final   String[] url=
                          loadJSONData.parseStringJSONObject(jsonObject.toString(), new String[]{"name", "sex","address","birthday","phone","label","autograph"});
                Message message=new Message();
                     message.obj=url;
                     message.what=2;
                 mHandler.sendMessage(message);

            }
            @Override
            public void loadError() {
                Toast.makeText(Activity_toolbar_button.this, "网络异常,无法获取信息", Toast.LENGTH_LONG).show();
            }
        });


    }

    //个人信息中toolbar中返回键的监听
    public boolean onOptionsItemSelected(MenuItem item) {

        //监听左上角的返回箭头
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void show(int height) {


        //将布局设置给Dialog
        mDialog.setContentView(mView);
        //获取当前Activity所在的窗体
        Window dialogWindow = mDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = height;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        mDialog.show();//显示对话框




    }

    //这是选择照片的方法
    private void choosePhoto() {
        mDialog.dismiss();
        Intent intentFromGallery = new Intent();
        intentFromGallery.setType("image/*");//设置文件类型
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);

    }

    //这是拍照的方法
    private void takePhoto() {
        mDialog.dismiss();

        imageUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "touxiang.jpg"));
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);//启动相机程序
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    tempFile = new File(
                            Environment.getExternalStorageDirectory() + "/" + "touxiang.jpg"
                    );
                    startPhotoZoom(Uri.fromFile(tempFile));
                }
                break;
            case CROP_PHOTO:
                if (data != null) {
                    try {
                        final String url = GetURL.getUploadIconImageURL(phone);
                        //  final Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //获取图片
                        Bundle extras = data.getExtras();
                        bitmap = extras.getParcelable("data");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                    if (bitmap != null) {
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                    }
                                    try {
                                        fileOutputStream.write(outputStream.toByteArray());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //发送图片去服务器
                                MyOkHttpUpload.uploadImageToServer(tempFile, url, new UploadResult() {
                                    @Override
                                    public void uploadSuccess(String response) {
                                        Log.d("xyf", response);
                                        Message message=new Message();
                                        message.what=1;
                                        mHandler.sendMessage(message);
                                    }
                                    @Override
                                    public void uploadError(Request request, Exception e) {
                                        Toast.makeText(Activity_toolbar_button.this,
                                                "发生异常,上传照片失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Log.d("xyf", "handler");
                                Fragment4 fragment4 = Fragment4.getInstance();
                                if (fragment4 != null) {
                                    Log.d("xyf", "handle update");
                                    fragment4.updateImage();
                                } else {
                                    Log.d("xyf", "null fragment");
                                }
                            }

                        }).start();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case IMAGE_REQUEST_CODE:

            try {

       Uri uri = data.getData();
      if (DocumentsContract.isDocumentUri(this, uri)) {
                   //如果是document类型的uri,则通过document Id处理
        String docId = DocumentsContract.getDocumentId(uri);
        if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
            String id = docId.split(":")[1];
            String selection = MediaStore.Images.Media._ID + "=" + id;
            String path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
           Log.d("wjjj",path);
            tempFile = new File(path);
            startPhotoZoom(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
            String path = getImagePath(contentUri, null);
            tempFile = new File(path);
            Log.d("wjjj",path);
            startPhotoZoom(contentUri);
        }
      } else if ("content".equalsIgnoreCase(uri.getScheme())) {
          String path = getImagePath(uri, null);
          tempFile = new File(path);
          Log.d("wjjj", path);
          startPhotoZoom(uri);
      } else {
          String path = data.getData().getPath();
          tempFile = new File(path);
          startPhotoZoom(uri);
          Log.d("wjj", path);
      }
}
catch (NullPointerException e){
    e.printStackTrace();//防止用户取消操作
}
                break;

            default:
                break;
        }
    }

    //裁剪图片方法实现
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //aspectX,aspectY是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selectiion来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onClick(View view) {

    }

    static class MyHandler extends Handler{
        WeakReference<Activity_toolbar_button> mActivity;
        public MyHandler(Activity_toolbar_button activity) {
            mActivity = new WeakReference<Activity_toolbar_button>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            Activity_toolbar_button mainActivity = mActivity.get();
            if(msg.what==1){
               mainActivity.touxiang.setImageBitmap(mainActivity.bitmap);
            }

            if(msg.what==2){
                String[] url= (String[]) msg.obj;
                for(int i=0;i<5;i++){
                    if(url[i].equals("")){
                        mainActivity.mText3[i]="未填写";
                    }
                    else {
                        mainActivity.mText3[i]=url[i]; ;
                    }
                }
                mainActivity.mText3[5]="";
                if(url[5].equals("")){
                    mainActivity.mText4[0]="未填写";
                }else {
                    mainActivity.mText4[0]=url[5];
                } if(url[6].equals("")){
                    mainActivity.mText4[0]="未填写";
                }else {
                    mainActivity.mText4[1]=url[6];
                }

                mainActivity.recycleAdapter.notifyDataSetChanged();
                mainActivity.recycleAdapter2.notifyDataSetChanged();
            }
            if(msg.what==3){
                String s= (String) msg.obj;
                mainActivity.mText3[0]=s;
                mainActivity.recycleAdapter.notifyDataSetChanged();
            }
            if(msg.what==4){
                mainActivity.mText3[1]= (String) msg.obj;
                mainActivity.recycleAdapter.notifyDataSetChanged();
            }
            if(msg.what==5){
                mainActivity.mText3[2]= (String) msg.obj;
                mainActivity.recycleAdapter.notifyDataSetChanged();
            }
            if(msg.what==6){
                mainActivity.mText3[3]= (String) msg.obj;
                mainActivity.recycleAdapter.notifyDataSetChanged();
            }
            if(msg.what==7){
                mainActivity.mText4[1]= (String) msg.obj;
                mainActivity.recycleAdapter2.notifyDataSetChanged();
            }
            if(msg.what==8){
                mainActivity.mText4[0]= (String) msg.obj;
                mainActivity.recycleAdapter2.notifyDataSetChanged();
            }


        }

    }
    public class MyTextWatcher implements TextWatcher {
        private View nView;
        public MyTextWatcher(View view){
            nView=view;
        }
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
          isNameValid();
        }
    }
}
