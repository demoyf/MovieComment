package com.example.mbenben.movie.Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbenben.movie.Bean.GetPhoneParams;
import com.example.mbenben.movie.Bean.MovieInfoBean;
import com.example.mbenben.movie.Bean.MovieNameBean;
import com.example.mbenben.movie.Http.LoadJSONData;
import com.example.mbenben.movie.Http.OnLoadDataResult;
import com.example.mbenben.movie.LoadDataAndVerCode.GetURL;
import com.example.mbenben.movie.R;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.MyShowSomthingUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alone on 2016/11/23.
 */
public class ShowRadomCompleteMovieActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ImageView mImageView;
    private TextView mTextView;
    List<MovieInfoBean> mMovieInfoBeen = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LoadJSONData mLoadJSONData;
    MovieRecyclerAdapter movieRecyclerAdapter;
    private int movieId = 0;
    private ImageView toTop;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_radom_complete_movie);
        mRecyclerView = (RecyclerView) findViewById(R.id.movie_recycler);
        mTextView = (TextView) findViewById(R.id.show_fail);
        mImageView = (ImageView) findViewById(R.id.back_icon_in_tool);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mMovieInfoBeen = (List<MovieInfoBean>) getIntent().getSerializableExtra("movieList");
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.movie_info_refresh);
        mLoadJSONData = new LoadJSONData(this);
        toTop = (ImageView) findViewById(R.id.top_image);
        if (mMovieInfoBeen == null || mMovieInfoBeen.size() == 0) {
            mTextView.setVisibility(View.VISIBLE);
        } else {
            movieRecyclerAdapter= new MovieRecyclerAdapter();
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(movieRecyclerAdapter);
        }
        movieId = mMovieInfoBeen.get(mMovieInfoBeen.size() - 1).getId();
        initEvent();
    }

    private void initEvent() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                movieId++;
                getMovieInfoBeen(movieId);
            }
        });
        toTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
    }
    int size = 0;
    private void getMovieInfoBeen(final int id) {
        String getNameUrl = GetURL.getGetNewMovieName(id);
        final List<MovieInfoBean> getResultInfo = new ArrayList<>();
        size = 0;
        mLoadJSONData.LoadDtaFromServer(getNameUrl, new OnLoadDataResult() {
            @Override
            public void loadSuccess(String result) {
                final List<MovieNameBean> movieNameBeen = mLoadJSONData.parseRadomMovie(result);
                for (int i = 0;i<movieNameBeen.size();i++) {
                    MovieNameBean nameBean = movieNameBeen.get(i);
                    String getInfoUrl = GetURL.getSearchMovieInfoUrl(nameBean.getName());
                    mLoadJSONData.LoadDtaFromServer(getInfoUrl, new OnLoadDataResult() {
                        @Override
                        public void loadSuccess(String result) {
                            String errorCode = "";
                            String info = "";
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                errorCode = jsonObject.getString("error_code");
                                info = jsonObject.getString("result");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            MovieInfoBean infoBean = mLoadJSONData.parseMovieInfoBean(info);
                            size++;
                            if (!"0".equals(errorCode)) {
                                infoBean = null;
                            }
                            if (infoBean != null) {
                                getResultInfo.add(infoBean);
                            }
                            if (size >= movieNameBeen.size()) {
                                movieId = movieNameBeen.get(movieNameBeen.size() - 1).getId();
                                mMovieInfoBeen.addAll(0, getResultInfo);
                                movieRecyclerAdapter.notifyDataSetChanged();
                                mRecyclerView.smoothScrollToPosition(0);
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void loadError() {
                            size++;
                            if (size >= movieNameBeen.size()) {
                                movieId = movieNameBeen.get(movieNameBeen.size() - 1).getId();
                                mMovieInfoBeen.addAll(0, getResultInfo);
                                movieRecyclerAdapter.notifyDataSetChanged();
                                mRecyclerView.smoothScrollToPosition(0);
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }
            }

            @Override
            public void loadError() {
                MyShowSomthingUtil.showToastShort(ShowRadomCompleteMovieActivity.this, "数据加载失败");
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieHolder> {
        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.movie_item_layout_single, parent, false);
            return new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {
            final MovieInfoBean bean = mMovieInfoBeen.get(position);
            String rating = bean.getRating();
            if (TextUtils.isEmpty(rating)) {
                rating = "暂无评分";
            }
            holder.nameTextView.setText("电影名:"+bean.getTitle());
            holder.ratingTextView.setText("评分:" + rating);
            holder.actTextView.setText("领衔主演:"+bean.getAct());
            holder.descTextView.setText(bean.getDesc());
            holder.areaTextView.setText("地区:"+bean.getArea());
            holder.dirTextView.setText("导演:"+bean.getDir());
            holder.tagTextView.setText("影片类型:"+bean.getTag());
            holder.yearTextView.setText("年份:"+bean.getYear());
            Picasso.with(ShowRadomCompleteMovieActivity.this).
                    load(bean.getCover()).skipMemoryCache().fit().into(holder.iconImageView);
            holder.descTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = LayoutInflater.from(ShowRadomCompleteMovieActivity.this).
                            inflate(R.layout.movie_desc_layout, null);
                    TextView textView = (TextView) view.findViewById(R.id.dialog_movie_desc);
                    Dialog alertDialog = new Dialog(ShowRadomCompleteMovieActivity.this, R.style.MyDialogStyle);
                    alertDialog.setContentView(view);
                    textView.setText(bean.getDesc());
                    Window dialogWindow = alertDialog.getWindow();
                    //设置Dialog从窗体底部弹出
                    dialogWindow.setGravity( Gravity.BOTTOM);
                    //获得窗体的属性
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.y = GetPhoneParams.getPhoneHeight(ShowRadomCompleteMovieActivity.this) / 2 - 225;//设置Dialog距离底部的距离
                    lp.height = 450;
//       将属性设置给窗体
                    dialogWindow.setAttributes(lp);
                    alertDialog.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMovieInfoBeen.size();
        }

        class MovieHolder extends RecyclerView.ViewHolder {
           TextView nameTextView,ratingTextView,actTextView,descTextView,areaTextView,
                    dirTextView,tagTextView, yearTextView;
            ImageView iconImageView;
            public MovieHolder(View itemView) {
                super(itemView);
                nameTextView = (TextView) itemView.findViewById(R.id.movie_name);
                ratingTextView = (TextView) itemView.findViewById(R.id.movie_rating);
                actTextView = (TextView) itemView.findViewById(R.id.movie_act);
                descTextView = (TextView) itemView.findViewById(R.id.movie_desc);
                areaTextView = (TextView) itemView.findViewById(R.id.movie_area);
                dirTextView = (TextView) itemView.findViewById(R.id.movie_dir);
                tagTextView = (TextView) itemView.findViewById(R.id.movie_tag);
                yearTextView = (TextView) itemView.findViewById(R.id.movie_year);
                iconImageView = (ImageView) itemView.findViewById(R.id.movie_cover);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        size = 0;
        mLoadJSONData.onCancel();
    }
}
