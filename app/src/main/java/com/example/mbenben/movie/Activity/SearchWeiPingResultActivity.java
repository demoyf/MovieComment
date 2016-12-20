package com.example.mbenben.movie.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbenben.movie.Bean.CommentCriticBean;
import com.example.mbenben.movie.Http.LoadJSONData;
import com.example.mbenben.movie.Http.OnLoadDataResult;
import com.example.mbenben.movie.LoadDataAndVerCode.GetURL;
import com.example.mbenben.movie.Movie.PublishCritic;
import com.example.mbenben.movie.R;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by alone on 2016/12/7.
 */
public class SearchWeiPingResultActivity extends AppCompatActivity {
    private ImageView backImage;
    private RecyclerView mRecyclerView;
    List<PublishCritic> publishCritics;
    private MyAdapter mMyAdapter;
    private LoadJSONData mLoadJSONData;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_search_weiping_result_layout);
        Bundle bundle = getIntent().getExtras();
        publishCritics = (List<PublishCritic>) bundle.getSerializable("publish");
        backImage = (ImageView) findViewById(R.id.back_icon_in_tool);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_weiping_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);
        mLoadJSONData = new LoadJSONData(this);
        mProgressBar = (ProgressBar) findViewById(R.id.search_weiping_progress);
        initEvent();
    }

    private void initEvent() {
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private boolean isBack = false;
    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.search_weiping_item_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.bindText(position);
        }
        @Override
        public int getItemCount() {
            return publishCritics.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView desc, title;
            public MyViewHolder(View itemView) {
                super(itemView);
                desc = (TextView) itemView.findViewById(R.id.weiping_desc);
                title = (TextView) itemView.findViewById(R.id.weiping_title);
            }
            public void bindText(final int position) {
                PublishCritic publishCritic = publishCritics.get(position);
                desc.setText(publishCritic.getCritic());
                title.setText("--《"+publishCritic.getTitle()+"》");
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mProgressBar.getVisibility() == View.VISIBLE) {
                            return;
                        }
                        mProgressBar.setVisibility(View.VISIBLE);
                        final PublishCritic publishCritic = publishCritics.get(position);
                        String url = GetURL.getGetWhoCriticItUrl(publishCritic.getId());
                        mLoadJSONData.LoadDtaFromServer(url, new OnLoadDataResult() {
                            @Override
                            public void loadSuccess(String result) {
                                List<CommentCriticBean> commentCriticBeens =
                                        mLoadJSONData.parseCommentCriticArray(result);
                                if (commentCriticBeens == null || commentCriticBeens.size() == 0) {
                                    for (int i = 0; i < 10; i++) {
                                        CommentCriticBean commentCriticBean = new CommentCriticBean();
                                        commentCriticBean.setTime(new Date().toString());
                                        commentCriticBean.setCritic("critic in " + (i + 1));
                                        commentCriticBean.setName("userName" + (i + 1));
                                        commentCriticBeens.add(commentCriticBean);
                                    }
                                }
                                if (publishCritic != null) {
                                    Intent intent = new Intent(SearchWeiPingResultActivity.this,
                                            ShowCompleteCriticActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("publishCritic", publishCritic);
                                    intent.putExtra("commentCritics", (Serializable) commentCriticBeens);
                                    intent.putExtras(bundle);
                                    if (isBack) {
                                        isBack = false;
                                        return;
                                    }
                                    startActivity(intent);
                                    mProgressBar.setVisibility(View.GONE);
                                } else {
                                    mProgressBar.setVisibility(View.GONE);
                                    return;
                                }
                            }
                            @Override
                            public void loadError() {
                                Toast.makeText(SearchWeiPingResultActivity.this,
                                        "网络异常", Toast.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mProgressBar.getVisibility() == View.VISIBLE) {
            isBack = false;
            mProgressBar.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
}
