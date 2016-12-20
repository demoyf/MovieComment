package com.example.mbenben.movie.fragment4_activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mbenben.movie.Http.LoadJSONData;
import com.example.mbenben.movie.Http.OnLoadDataResult;
import com.example.mbenben.movie.LoadDataAndVerCode.GetURL;
import com.example.mbenben.movie.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by MBENBEN on 2016/11/15.
 */
public class Head_touxiang extends AppCompatActivity{
    private Toolbar mtoolbar;
    private ImageView mImageView;
    String phone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment4_touxiang);
        phone=getIntent().getStringExtra("phone");
        initView();
    }

    private void initView() {
        mtoolbar= (Toolbar) findViewById(R.id.touxiang_toolbar);
        mImageView= (ImageView) findViewById(R.id.touxiang);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mtoolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary,this.getTheme()));
        }
        else {
            mtoolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
        (this).setSupportActionBar(mtoolbar);
        (this).getSupportActionBar().setDisplayShowTitleEnabled(false);
        //是否给左上角图标的左边加上一个返回的图标
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mtoolbar.setTitle("查看头像");

        //下面是加载头像
        final LoadJSONData loadJSONData=new LoadJSONData(this);
        String url = GetURL.getGetUserIconURL(phone);
        loadJSONData.LoadDtaFromServer(url, new OnLoadDataResult() {
            @Override
            public void loadSuccess(String result) {
                String[] imageUrl = loadJSONData.
                        parseStringJSONObject(result, new String[]{GetURL.ICON_URL_JSON_KEY});
                if (!TextUtils.isEmpty(imageUrl[0])) {
                    Log.d("xya",imageUrl[0]);
                    Picasso.with(Head_touxiang.this)
                            .load(imageUrl[0]).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE).
                            memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE)
                            .placeholder(R.drawable.touxiang_load)
                            .error(R.drawable.touxiang)
                            .into(mImageView);

                } else {
                    Picasso.with(Head_touxiang.this).load(R.drawable.touxiang).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE).
                            memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE).into(mImageView);
                }
            }

            public void loadError() {
                Toast.makeText(Head_touxiang.this,"网络异常,获取不到头像",Toast.LENGTH_LONG).show();

            }
        });


    }
    //个人信息中toolbar中返回键的监听
    public boolean onOptionsItemSelected(MenuItem item) {

        //监听左上角的返回箭头
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
