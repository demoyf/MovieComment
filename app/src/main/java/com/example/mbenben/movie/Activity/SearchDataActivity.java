package com.example.mbenben.movie.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mbenben.movie.Bean.MovieInfoBean;
import com.example.mbenben.movie.Http.LoadJSONData;
import com.example.mbenben.movie.Http.OnLoadDataResult;
import com.example.mbenben.movie.LoadDataAndVerCode.GetURL;
import com.example.mbenben.movie.Movie.PublishCritic;
import com.example.mbenben.movie.R;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.MyShowSomthingUtil;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.ShowProgressDialog;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alone on 2016/11/19.
 */
public class SearchDataActivity extends AppCompatActivity {
    private ImageView backImageView, cancelImageView;
    private EditText searchEditText;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private LoadJSONData mLoadJSONData;
    private List<String> mString = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter = null;
    private ImageView searchWeiPing,searchChangPing, searchDianYing;
    private boolean isClickItem = false;
    private Gson mGson;
    private ImageView listViewCancel;
    private boolean isBack = false;
    ShowProgressDialog mShowProgressDialog = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Slide());
        }
        setContentView(R.layout.search_movie_layout);
        mGson = new Gson();
        mShowProgressDialog = new ShowProgressDialog(this);
        backImageView = (ImageView) findViewById(R.id.search_back);
        cancelImageView = (ImageView) findViewById(R.id.search_cancel);
        searchEditText = (EditText) findViewById(R.id.search_edit);
        mListView = (ListView) findViewById(R.id.search_list);
        mProgressBar = (ProgressBar) findViewById(R.id.init_data_progress_bar);
        mLoadJSONData = new LoadJSONData(this);
        arrayAdapter = new
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mString);
        mListView.setAdapter(arrayAdapter);
        searchWeiPing = (ImageView) findViewById(R.id.search_yingping);
        searchChangPing = (ImageView) findViewById(R.id.search_changping);
        searchDianYing = (ImageView) findViewById(R.id.search_dianying);
        listViewCancel = (ImageView) findViewById(R.id.list_view_cancel);
        initEvent();
    }
    private void initEvent() {
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewCancel.setVisibility(View.GONE);
                mString.clear();
                arrayAdapter.clear();
                arrayAdapter.notifyDataSetChanged();
            }
        });
        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEditText.setText("");
                cancelImageView.setVisibility(View.GONE);
                cancelImageView.setClickable(false);
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(final Editable s) {
                if (isClickItem) {
                    isClickItem = false;
                    return;
                }
                String searchText = searchEditText.getText().toString().trim();
                if (searchText.length() == 0) {
                    mString.clear();
                    arrayAdapter.clear();
                    arrayAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.GONE);
                    listViewCancel.setVisibility(View.GONE);
                }
                if (cancelImageView.getVisibility() == View.GONE) {
                    if (searchText.length() >= 1) {
                        cancelImageView.setVisibility(View.VISIBLE);
                        cancelImageView.setClickable(true);
                        mProgressBar.setVisibility(View.GONE);
                        mString.clear();
                        arrayAdapter.clear();
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
                if (searchText.length() >= 1) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    try {
                        searchText = URLEncoder.encode(searchText, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String url = GetURL.getSearchMovieNameUrl(searchText);
                    mLoadJSONData.LoadDtaFromServer(url, new OnLoadDataResult() {
                        @Override
                        public void loadSuccess(String result) {
                            List<String> strings = mLoadJSONData.parseNoKeyJsonArray(result);
                            if (strings != null && strings.size() > 0) {
                                mString.clear();
                                mString.addAll(strings);
                                arrayAdapter.notifyDataSetChanged();
                                mProgressBar.setVisibility(View.GONE);
                                listViewCancel.setVisibility(View.VISIBLE);
                            } else {
                                mProgressBar.setVisibility(View.GONE);
                                mString.clear();
                                arrayAdapter.clear();
                                arrayAdapter.notifyDataSetChanged();
                                listViewCancel.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void loadError() {
                            mProgressBar.setVisibility(View.GONE);
                            mString.clear();
                            arrayAdapter.clear();
                            arrayAdapter.notifyDataSetChanged();
                            Toast.makeText(SearchDataActivity.this,
                                    "网络异常", Toast.LENGTH_SHORT).show();
                            listViewCancel.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isClickItem = true;
                searchEditText.setText(mString.get(position));
                mString.clear();
                arrayAdapter.clear();
                arrayAdapter.notifyDataSetChanged();
                listViewCancel.setVisibility(View.GONE);
            }
        });
        /*
        * 监听事件
        * */
        searchWeiPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = searchEditText.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    MyShowSomthingUtil.showToastShort(SearchDataActivity.this, "请输入关键词");
                    return;
                }
                if (!mShowProgressDialog.isShowing()) {
                    mShowProgressDialog.show();
                }
                String nUrl = GetURL.getSearchCriticByName(name);
                mLoadJSONData.LoadDtaFromServer(nUrl, new OnLoadDataResult() {
                    @Override
                    public void loadSuccess(String result) {
                        List<PublishCritic> publishCritics =
                                mLoadJSONData.parsePublicCirticArrayWithoutHeight(result);
                        if (!mShowProgressDialog.isShowing()) {
                            return;
                        }
                        if (publishCritics != null && publishCritics.size() != 0) {
                            Intent intent = new Intent(SearchDataActivity.this, SearchWeiPingResultActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("publish", (Serializable) publishCritics);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            mShowProgressDialog.dismiss();
                        } else {
                            MyShowSomthingUtil.showToastShort(SearchDataActivity.this, "没有找到相关数据");
                            mShowProgressDialog.dismiss();
                        }
                    }
                    @Override
                    public void loadError() {
                        MyShowSomthingUtil.showToastShort(SearchDataActivity.this, "查询失败");
                        mShowProgressDialog.dismiss();
                    }
                });
            }
        });
        searchChangPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyShowSomthingUtil.showToastShort(SearchDataActivity.this,"仅电影资讯可查询");
            }
        });

        searchDianYing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEditText.getText().toString().trim();
                if (TextUtils.isEmpty(searchText)) {
                    MyShowSomthingUtil.showToastShort(SearchDataActivity.this, "请输入关键词");
                    return;
                } else {
                    mShowProgressDialog.show();
                    String url = GetURL.getSearchMovieInfoUrl(searchText);
                    mLoadJSONData.LoadDtaFromServer(url, new OnLoadDataResult() {
                        @Override
                        public void loadSuccess(String result) {
                            String s = null;
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String error_code = jsonObject.getString("error_code");
                                if (!error_code.equals("0")) {
                                    MyShowSomthingUtil.showToastShort(SearchDataActivity.this,
                                            "未找到该影片");
                                    mShowProgressDialog.dismiss();
                                    return;
                                }
                                s = jsonObject.getString("result");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mShowProgressDialog.dismiss();
                                MyShowSomthingUtil.showToastShort(SearchDataActivity.this, "网络异常");
                            }
                            MovieInfoBean movieInfoBean = mGson.fromJson(s, MovieInfoBean.class);
                            Intent intent = new Intent(SearchDataActivity.this, ShowMovieInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("movieInfo", movieInfoBean);
                            intent.putExtras(bundle);
                            if (!mShowProgressDialog.isShowing()) {
                                return;
                            }
                            startActivity(intent);
                            mShowProgressDialog.dismiss();
                        }
                        @Override
                        public void loadError() {
                            mShowProgressDialog.dismiss();
                            MyShowSomthingUtil.showToastShort(SearchDataActivity.this, "查询失败");
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
