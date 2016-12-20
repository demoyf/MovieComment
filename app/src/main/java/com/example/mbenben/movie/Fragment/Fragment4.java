package com.example.mbenben.movie.Fragment;

/**
 * Created by MBENBEN on 2016/10/11.
 * 该类是登录后的第四个碎片
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbenben.movie.fragment4_activity.Head_touxiang;
import com.example.mbenben.movie.Adapter.Fragment4_adapter;

import com.example.mbenben.movie.Http.LoadJSONData;
import com.example.mbenben.movie.Http.OnLoadDataResult;
import com.example.mbenben.movie.LoadDataAndVerCode.GetURL;
import com.example.mbenben.movie.R;
import com.example.mbenben.movie.fragment4_activity.Activity_toolbar_button;
import com.example.mbenben.movie.fragment4_activity.Code;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;


public class Fragment4 extends Fragment  {
    private Toolbar mToolbar;
    private ListView mlistView;
    private View  rootView;//缓存Fragment view
    private Fragment4_adapter mAdapter;
    private String[] mdata={"个人设置","我的二维码","我的影评","我的论坛","我的影评","我的收藏"};
    public static Fragment4 sFragment4;
    private Activity_toolbar_button head_touxiang;
    //定义toolbarbutton
    private RoundedImageView toolbar_button;
    //Fragment4中的图像
     private RoundedImageView touxiang1;

     Handler mHandler;

    TextView name;
    LoadJSONData loadJSONData =null;
    public static String[] mText3;
    public static String[] mText4;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sFragment4 = this;
    }

    String phone;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //检查是否第一次加载
        if(rootView==null){
            rootView=inflater.inflate(R.layout.tab_fragment4, null);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        //inView();
        //toolbar初始化
        phone=getActivity().getIntent().getStringExtra("phoneNum");
        Log.d("phone",phone);
          name= (TextView) rootView.findViewById(R.id.name);
        mToolbar= (Toolbar) rootView.findViewById(R.id.toolbar);
        mlistView= (ListView) rootView.findViewById(R.id.fragment4_item_listview);
        toolbar_button= (RoundedImageView) rootView.findViewById(R.id.fragment4_toolbar_button);
        touxiang1= (RoundedImageView) rootView.findViewById(R.id.touxiang1);
        //实例化Adapter
        mAdapter=new Fragment4_adapter(inflater,mdata);
        initView();
        toolbar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Activity_toolbar_button.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", phone);
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.actionsheet_dialog_in,R.anim.actionsheet_dialog_out);
            }
        });
        touxiang1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Head_touxiang.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", phone);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                loadtouxiang();
                loadtext();
//                if(msg.what==2){
//                    String s= (String) msg.obj;
//                    name.setText(s);
//                }
            }
        };
        return rootView;
    }
    public void updateImage() {
        mHandler.sendEmptyMessage(0);
    }
    public static Fragment4 getInstance() {
        return sFragment4;
    }
    private void initView() {

        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.touxiang_bg);
       //提取toolbar下面控件的颜色来填充toolbar
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch swatch=palette.getVibrantSwatch();
                ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
                mToolbar.setBackground(new ColorDrawable(swatch.getRgb()));
                mToolbar.setTitle("");
            }
        });
         //提供适配器给ListView
        mlistView.setAdapter(mAdapter);
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Intent intent=new Intent(getActivity(),Activity_toolbar_button.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", phone);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else if (i==1){
                     Intent intent=new Intent(getActivity(), Code.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", phone);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getActivity(),"此功能暂未开发,敬请期待",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //加载名字
         loadtext();
        //下面是加载头像
        loadtouxiang();



    }

    public void loadtext(){
        loadJSONData = new LoadJSONData(getActivity());
        String url1 = GetURL.getGetUserInfoURL(phone);
        loadJSONData.LoadDtaFromServer(url1, new OnLoadDataResult() {
            @Override
            public void loadSuccess(String result) {
                JSONObject jsonObject=loadJSONData.JSONArraytoJSONObject(result);
                final   String[] url=
                        loadJSONData.parseStringJSONObject(jsonObject.toString(), new String[]{"name"});
                      name.setText(url[0]);

            }
            @Override
            public void loadError() {
                Toast.makeText(getActivity(), "网络异常,无法获取信息", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadtouxiang() {
        final LoadJSONData loadJSONData=new LoadJSONData(getActivity());
        String url = GetURL.getGetUserIconURL(phone);
        loadJSONData.LoadDtaFromServer(url, new OnLoadDataResult() {
            @Override
            public void loadSuccess(String result) {
                String[] imageUrl = loadJSONData.
                        parseStringJSONObject(result, new String[]{GetURL.ICON_URL_JSON_KEY});
                if (!TextUtils.isEmpty(imageUrl[0])) {
                    Log.d("xya",imageUrl[0]);
                    Picasso.with(getActivity())
                            .load(imageUrl[0]).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE).
                            memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE)
                            .placeholder(R.drawable.touxiang_load)
                            .error(R.drawable.touxiang)
                            .into(touxiang1);
                } else {
                    Log.d("xya", "imageUrl " + imageUrl[0]);
                    Picasso.with(getActivity()).load(R.drawable.touxiang).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE).
                            memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE).into(touxiang1);

                }
            }

            public void loadError() {
                Toast.makeText(getActivity(),"网络异常,获取不到头像",Toast.LENGTH_LONG).show();

            }
        });
    }



}