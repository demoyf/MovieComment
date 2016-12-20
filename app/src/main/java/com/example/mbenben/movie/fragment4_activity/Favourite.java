package com.example.mbenben.movie.fragment4_activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mbenben.movie.Http.LoadJSONData;
import com.example.mbenben.movie.Http.SendDataToServer;
import com.example.mbenben.movie.Movie.Activity_movie;
import com.example.mbenben.movie.LoadDataAndVerCode.GetURL;
import com.example.mbenben.movie.R;
import com.fyales.tagcloud.library.TagBaseAdapter;
import com.fyales.tagcloud.library.TagCloudLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by MBENBEN on 2016/11/23.
 */
public class Favourite extends AppCompatActivity {
    private TagCloudLayout mTagCloudLayout;
   private ArrayList<String>  mList;
    private TagBaseAdapter mTagBaseAdapter;
    private TextView mTextView;
    Handler mTimeHandler;
    String phone;
    final LoadJSONData loadJSONData = new LoadJSONData(this);
    private Button mButton1;
    private Button mButton2;
    private ImageView backImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_favourite);
        phone= Activity_movie.phone;
        mTextView= (TextView) findViewById(R.id.fragment4_toolbar_button_favourite_textview_bg);
        mTagCloudLayout= (TagCloudLayout) findViewById(R.id.container);
        mButton1= (Button) findViewById(R.id.fragment4_toolbar_button_favourite_button_true);
        mButton2= (Button) findViewById(R.id.fragment4_toolbar_button_favourite_button_false);
        mList=new ArrayList<>();
        mList.add("爱情");mList.add("文艺");mList.add("恐怖");mList.add("伦理");
        mList.add("热血");mList.add("悬疑");mList.add("动作");mList.add("惊悚");
        mList.add("动漫");mList.add("古装");mList.add("喜剧");mList.add("冒险");
        mList.add("记录");mList.add("战争");mList.add("励志");mList.add("科幻");
        mList.add("家庭");mList.add("传记");mList.add("幻想");mList.add("青春");
        mList.add("欧美"); mList.add("犯罪"); mList.add("音乐"); mList.add("武侠");
        mTagBaseAdapter=new TagBaseAdapter(this,mList);
     mTagCloudLayout.setAdapter(mTagBaseAdapter);
        backImage = (ImageView) findViewById(R.id.back_icon_in_tool);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mTagCloudLayout.setItemClickListener(new TagCloudLayout.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                    Message message = new Message();
                    message.what = 0;
                    message.arg1 = position;
                    mTimeHandler.sendMessage(message);
            }
        });

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final String temp = mTextView.getText().toString();
                    String str = URLEncoder.encode(temp, "utf-8");
                    String url = GetURL.getUpdateUserHobbyLabelURL(phone, str);
                    loadJSONData.SendDataServer(url, new SendDataToServer() {
                        @Override
                        public void SendSuccess(String result) {
                           finish();
                        }
                        @Override
                        public void SendloadError() {
                            Toast.makeText(Favourite.this, "网络异常，无法修改信息", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Message message=new Message();
                message.what=8;
                message.obj=mTextView.getText().toString();
               Activity_toolbar_button.mHandler1.sendMessage(message);
            }
        });


          mButton2.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  mTextView.setText("");
              }
          });



        //在类里声明一个Handler
        mTimeHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    int temp=msg.arg1;
                    mTextView.append(mList.get(temp)+",");
                }
            }
        };

//在你的onCreate的类似的方法里面启动这个Handler就可以了：

    }
    }

