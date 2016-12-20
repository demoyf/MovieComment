package com.example.mbenben.movie.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbenben.movie.Activity.SearchDataActivity;
import com.example.mbenben.movie.Bean.MovieNameBean;
import com.example.mbenben.movie.Interface.OnRecPositionClick;
import com.example.mbenben.movie.Activity.ShowCompleteCriticActivity;
import com.example.mbenben.movie.Activity.ShowRadomCompleteMovieActivity;
import com.example.mbenben.movie.Adapter.CircleImageAdapter;
import com.example.mbenben.movie.Adapter.MyMainRecyclerViewAdapter;
import com.example.mbenben.movie.Adapter.ShortCirtirsimItemDecoration;
import com.example.mbenben.movie.Bean.CommentCriticBean;
import com.example.mbenben.movie.Bean.MovieInfoBean;
import com.example.mbenben.movie.Http.LoadJSONData;
import com.example.mbenben.movie.Http.OnLoadDataResult;
import com.example.mbenben.movie.Interface.UpdateNewItemSuccess;
import com.example.mbenben.movie.LoadDataAndVerCode.GetURL;
import com.example.mbenben.movie.Movie.PublishCritic;
import com.example.mbenben.movie.R;
import com.example.mbenben.movie.ShowSomethingAndKeyInterface.MyShowSomthingUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Fragment1 extends Fragment {
    private EditText mEditText;
    private List<ImageView> mImageViews;
    private int[] ids = new int[]{R.drawable.p5, R.drawable.p2,
            R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p2};
    private List<PublishCritic> mPublishCritics;
    private ViewPager mViewPager;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean isPlay = true;
    private ScheduledExecutorService mExecutorService;
    private View[] dot = new View[4];
    private MyMainRecyclerViewAdapter mMyMainRecyclerViewAdapter;
    private boolean isLoadNewData = false;
    private LoadJSONData mLoadJSONData;
    private ProgressBar loadDataProgressBar;
    private TextView toTop;
    private int click = 1;
    private long end = 0;
    private ImageView toTopImage;
    private Activity mActivity;
    private SharedPreferences mSharedPreferences;
    private static int curShowId = 0;
    private ProgressBar runToCritic;
    private Button searchMovieButton;
    private static Fragment1 sFragment1;
    private Button button1,button2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mViewPager.setCurrentItem(msg.arg1, false);
            } else {
                if (isPlay) {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
            }
            int position = mViewPager.getCurrentItem();
            for (int i = 0; i < dot.length; i++) {
                if (i != position - 1) {
                    dot[i].setPressed(false);
                } else {
                    dot[i].setPressed(true);
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        curShowId = mSharedPreferences.getInt("curShowId", 30);
        sFragment1 = this;
    }
    public static Fragment1 getInstance() {
        return sFragment1;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.main_ui_layout, container, false);
        initView(layout);
        button1 = (Button) layout.findViewById(R.id.button1);
        button2 = (Button) layout.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyShowSomthingUtil.showToastShort(mActivity,"目前仅影片资讯可用");
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyShowSomthingUtil.showToastShort(mActivity,"目前仅影片资讯可用");
            }
        });
        initEvent();
        Log.d("xyf", "oncreate");
        return layout;
    }

    private void initView(View layout) {
        mEditText = (EditText) layout.findViewById(R.id.search_edit);
        mEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (runToCritic.getVisibility() == View.VISIBLE) {
                    return;
                }
                Intent intent = new Intent(mActivity, SearchDataActivity.class);
                if (Build.VERSION.SDK_INT >= 21) {
                    mActivity.startActivity(intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity).toBundle());
                } else {
                    mActivity.startActivity(intent);
                }
            }
        });
        mLoadJSONData = new LoadJSONData(mActivity);
        mPublishCritics = new ArrayList<PublishCritic>();
        mViewPager = (ViewPager) layout.findViewById(R.id.banner_pager);
        mImageViews = new ArrayList<ImageView>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(mActivity);
            mImageViews.add(imageView);
        }
        mViewPager.setAdapter(new CircleImageAdapter(mImageViews, ids,mActivity));
        mViewPager.setOnPageChangeListener(new MyPageListener());
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.main_recycler_view);
        mMyMainRecyclerViewAdapter = new
                MyMainRecyclerViewAdapter(mActivity, mPublishCritics);
        mRecyclerView.setAdapter(mMyMainRecyclerViewAdapter);
        mRecyclerView.setLayoutManager
                (new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new ShortCirtirsimItemDecoration());
        mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.BLACK, Color.GREEN);
        mExecutorService = Executors.newSingleThreadScheduledExecutor();
        mExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (isPlay) {
                    Message message = new Message();
                    message.what = 2;
                    mHandler.sendMessage(message);
                }
            }
        }, 3000, 3000, TimeUnit.MILLISECONDS);
        dot[0] = layout.findViewById(R.id.dot_1);
        dot[1] = layout.findViewById(R.id.dot_2);
        dot[2] = layout.findViewById(R.id.dot_3);
        dot[3] = layout.findViewById(R.id.dot_4);
        mViewPager.setCurrentItem(1);
        loadDataProgressBar = (ProgressBar) layout.findViewById(R.id.load_more_progress);
        toTop = (TextView) layout.findViewById(R.id.to_top);
        toTopImage = (ImageView) layout.findViewById(R.id.top_image);
        runToCritic = (ProgressBar) layout.findViewById(R.id.run_to_critic_progress);
        searchMovieButton = (Button) layout.findViewById(R.id.search_movie_button);
    }
    int size = 0;
    private void initEvent() {
        searchMovieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (runToCritic.getVisibility() == View.VISIBLE) {
                    return;
                }
                size = 0;
                isBack = false;
                runToCritic.setVisibility(View.VISIBLE);
                String url = GetURL.getGeT_RADOM_MOVIE();
                mLoadJSONData.LoadDtaFromServer(url, new OnLoadDataResult() {
                    @Override
                    public void loadSuccess(String result) {
                        final List<MovieNameBean> strings =
                                mLoadJSONData.parseRadomMovie(result);
                        final List<MovieInfoBean> movieInfoBeen = new ArrayList<MovieInfoBean>();
                        for (int i = 0;i<strings.size();i++) {
                            final MovieNameBean movieNameBean = strings.get(i);
                            String movieSearch = GetURL.getSearchMovieInfoUrl(
                                    movieNameBean.getName());
                            mLoadJSONData.LoadDtaFromServer(movieSearch, new OnLoadDataResult() {
                                @Override
                                public void loadSuccess(String result) {
                                    size++;
                                    String error_code = null;
                                    String s = null;
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        error_code = jsonObject.getString("error_code");
                                        s = jsonObject.getString("result");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    if (size!=strings.size()&&!error_code.equals("0")) {
                                        return;
                                    } else {
                                        MovieInfoBean bean =
                                                mLoadJSONData.parseMovieInfoBean(s);
                                        if (bean!=null){
                                            bean.setId(movieNameBean.getId());
                                            movieInfoBeen.add(bean);
                                        }
                                        if (size >= strings.size()) {
                                            mLoadJSONData.cancelArrayListRequest();
                                            if (movieInfoBeen.size() == 0) {
                                                MyShowSomthingUtil.showToastShort(mActivity,
                                                        "没有查找到数据");
                                                runToCritic.setVisibility(View.GONE);
                                                return;
                                            }
                                            if (isBack) {
                                                isBack = false;
                                                size = 0;
                                                return;
                                            } else {
                                                Intent intent = new Intent(mActivity,
                                                        ShowRadomCompleteMovieActivity.class);
                                                intent.putExtra("movieList", (Serializable) movieInfoBeen);
                                                mActivity.startActivity(intent);
                                                runToCritic.setVisibility(View.GONE);
                                                size = 0;
                                                isBack = false;
                                            }
                                        }
                                    }
                                }
                                @Override
                                public void loadError() {
                                    size++;
                                    if (size >= strings.size()) {
                                        mLoadJSONData.cancelArrayListRequest();
                                        if (movieInfoBeen.size() != 0) {
                                            Intent intent = new Intent(mActivity,
                                                    ShowRadomCompleteMovieActivity.class);
                                            intent.putExtra("movieList", (Serializable) movieInfoBeen);
                                            mActivity.startActivity(intent);
                                            runToCritic.setVisibility(View.GONE);
                                            size = 0;
                                            isBack = false;
                                            return;
                                        }
                                        MyShowSomthingUtil.showToastShort(mActivity,
                                                "加载失败");
                                        runToCritic.setVisibility(View.GONE);
                                        size = 0;
                                        isBack = false;
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void loadError() {
                        MyShowSomthingUtil.showToastShort(mActivity,
                                "网络异常");
                        runToCritic.setVisibility(View.GONE);
                        size = 0;
                        isBack = false;
                    }
                });
            }
        });
        mMyMainRecyclerViewAdapter.setOnRecPositionClick(new OnRecPositionClick() {
            @Override
            public void OnClickSuccsee(int position) {
                if (runToCritic.getVisibility() == View.VISIBLE) {
                    return;
                }
                final PublishCritic publishCritic = mMyMainRecyclerViewAdapter.getPositionPub(position);
                runToCritic.setVisibility(View.VISIBLE);
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
                            Intent intent = new Intent(mActivity, ShowCompleteCriticActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("publishCritic", publishCritic);
                            intent.putExtra("commentCritics", (Serializable) commentCriticBeens);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            runToCritic.setVisibility(View.GONE);
                        } else {
                            runToCritic.setVisibility(View.GONE);
                            Toast.makeText(mActivity, "网络异常", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void loadError() {
                        runToCritic.setVisibility(View.GONE);
                        Toast.makeText(mActivity, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });
                    }
        });
        toTopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
        toTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long start = System.currentTimeMillis();
                if (click % 2 == 0) {
                    if (Math.abs(start - end) <= 200)
                        mRecyclerView.smoothScrollToPosition(0);
                    else
                        click = 1;
                }
                click++;
                end = System.currentTimeMillis();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    StaggeredGridLayoutManager manager =
                            (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                    int[] postion = manager.findLastCompletelyVisibleItemPositions(null);
                    if (mMyMainRecyclerViewAdapter.getItemCount() != 0
                            && postion[1] == mMyMainRecyclerViewAdapter.getItemCount() - 1) {
                        if (isLoadNewData) {
                            if (loadDataProgressBar.getVisibility() == View.GONE) {
                                loadDataProgressBar.setVisibility(View.VISIBLE);
                            }
                            isLoadNewData = false;
                            PublishCritic critic =
                                    mMyMainRecyclerViewAdapter.getPositionPub(postion[1]);
                            String url = GetURL.getGetHotBeforeUrl(critic.getId() + "");
//                            Log.d("xyf", "cur url  " + url);
                            mLoadJSONData.LoadDtaFromServer(url, new OnLoadDataResult() {
                                @Override
                                public void loadSuccess(String result) {
                                    final List<PublishCritic> publishCritics
                                            = mLoadJSONData.parsePublicCirticArray(result);
                                    if (publishCritics != null && publishCritics.size() != 0) {
//                                        Log.d("xyf", "next " + result);
                                        mMyMainRecyclerViewAdapter.
                                                addNext(publishCritics, new UpdateNewItemSuccess() {
                                                    @Override
                                                    public void pudateSuccess(boolean isSuccess) {
//                                                        Log.d("xyf", "second get" + publishCritics.size());
                                                        isLoadNewData = isSuccess;
                                                        if (loadDataProgressBar.getVisibility() == View.VISIBLE) {
                                                            loadDataProgressBar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });
                                    } else {
                                        if (loadDataProgressBar.getVisibility() == View.VISIBLE) {
                                            loadDataProgressBar.setVisibility(View.GONE);
                                            MyShowSomthingUtil.showToastShort(mActivity,"没有更多数据");
                                        }
                                    }
                                }

                                @Override
                                public void loadError() {
                                    Toast.makeText(mActivity,
                                            "加载失败", Toast.LENGTH_SHORT).show();
                                    isLoadNewData = true;
                                    if (loadDataProgressBar.getVisibility() == View.VISIBLE) {
                                        loadDataProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    } else if (mMyMainRecyclerViewAdapter.getItemCount() != 0
                            && postion[0] == mMyMainRecyclerViewAdapter.getItemCount() - 1) {
                        if (isLoadNewData) {
                            if (loadDataProgressBar.getVisibility() == View.GONE) {
                                loadDataProgressBar.setVisibility(View.VISIBLE);
                            }
                            isLoadNewData = false;
                            PublishCritic critic =
                                    mMyMainRecyclerViewAdapter.getPositionPub(postion[0]);
                            String url = GetURL.getGetHotBeforeUrl(critic.getId() + "");
                            mLoadJSONData.LoadDtaFromServer(url, new OnLoadDataResult() {
                                @Override
                                public void loadSuccess(String result) {
                                    final List<PublishCritic> publishCritics
                                            = mLoadJSONData.parsePublicCirticArray(result);
                                    if (publishCritics != null && publishCritics.size() != 0) {
                                        mMyMainRecyclerViewAdapter.
                                                addNext(publishCritics, new UpdateNewItemSuccess() {
                                                    @Override
                                                    public void pudateSuccess(boolean isSuccess) {
                                                        isLoadNewData = isSuccess;
                                                        if (loadDataProgressBar.getVisibility() == View.VISIBLE) {
                                                            loadDataProgressBar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });
                                    } else {
                                        if (loadDataProgressBar.getVisibility() == View.VISIBLE) {
                                            loadDataProgressBar.setVisibility(View.GONE);
                                            MyShowSomthingUtil.showToastShort(mActivity,"没有更多数据");
                                        }
                                    }
                                }

                                @Override
                                public void loadError() {
                                    Toast.makeText(mActivity,
                                            "加载失败", Toast.LENGTH_SHORT).show();
                                    isLoadNewData = true;
                                    if (loadDataProgressBar.getVisibility() == View.VISIBLE) {
                                        loadDataProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                Log.d("xyf", "dy " + dy);
            }
        });

        /*
        * 第一次进入APP 加载一部分数据
        * */
        PublishCritic publishCritic = mMyMainRecyclerViewAdapter.getPositionPub(0);
        if (publishCritic == null) {
            loadFirst();
        }
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final PublishCritic publishCritic = mMyMainRecyclerViewAdapter.getPositionPub(0);
//                Log.d("xyf", "cur fisrt id " + publishCritic.getId());
                if (publishCritic != null) {
                    if (loadDataProgressBar.getVisibility() == View.VISIBLE) {
                        Toast.makeText(mActivity, "请等待其他数据加载完成",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String url = GetURL.getGetHotNewUrl(publishCritic.getId() + "");
                    mLoadJSONData.LoadDtaFromServer(url, new OnLoadDataResult() {
                        @Override
                        public void loadSuccess(String result) {
                            List<PublishCritic> publishCriticList =
                                    mLoadJSONData.parsePublicCirticArray(result);
                            if (publishCritic != null && publishCriticList.size() != 0) {
                                mMyMainRecyclerViewAdapter.addFirst(publishCriticList,
                                        new UpdateNewItemSuccess() {
                                            @Override
                                            public void pudateSuccess(boolean isSuccess) {
                                                isLoadNewData = isSuccess;
                                                mSwipeRefreshLayout.setRefreshing(false);
                                                mRecyclerView.scrollToPosition(0);
                                            }
                                        });
                            } else {
                                Toast.makeText(mActivity,
                                        "没有更多数据了", Toast.LENGTH_SHORT).show();
                                isLoadNewData = true;
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }

                        @Override
                        public void loadError() {
                            Toast.makeText(mActivity,
                                    "数据加载失败", Toast.LENGTH_SHORT).show();
                            isLoadNewData = true;
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } else {
                    if (runToCritic.getVisibility() == View.VISIBLE) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        return;
                    }
                    if (!mSwipeRefreshLayout.isRefreshing())
                    loadFirst();
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt("curShowId", curShowId);
        editor.commit();
        mMyMainRecyclerViewAdapter.clearData();
        mLoadJSONData.onCancel();
        mExecutorService.shutdown();
    }

    @Override
    public void onStop() {
        super.onStop();
        StaggeredGridLayoutManager manager =
                (StaggeredGridLayoutManager) mRecyclerView.getLayoutManager();
        int[] postion = manager.findFirstVisibleItemPositions(null);
        PublishCritic critic = mMyMainRecyclerViewAdapter.getPositionPub(postion[0]);
        PublishCritic critic2 = mMyMainRecyclerViewAdapter.getPositionPub(postion[1]);
        int re = 30;
        if (critic != null) {
            re = critic.getId()+1;
        } else if (critic2 != null) {
            re = critic2.getId()+1;
        }
        curShowId = re;
        mExecutorService.shutdown();
    }

    public void loadFirst() {
//      =====  String url = GetURL.getGetHotCriticFirstUrl();
        String url = GetURL.getGetHotBeforeUrl(curShowId + "");
        runToCritic.setVisibility(View.VISIBLE);
        isLoadNewData = false;
        mLoadJSONData.LoadDtaFromServer(url, new OnLoadDataResult() {
            @Override
            public void loadSuccess(String result) {
                List<PublishCritic> publishCritics
                        = mLoadJSONData.parsePublicCirticArray(result);
                if (publishCritics != null && publishCritics.size() != 0) {
                    mMyMainRecyclerViewAdapter.addNext(publishCritics,
                            new UpdateNewItemSuccess() {
                                @Override
                                public void pudateSuccess(boolean isSuccess) {
                                    isLoadNewData = isSuccess;
                                    if (mSwipeRefreshLayout.isRefreshing()) {
                                        mSwipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            });
                } else {
                    if (mMyMainRecyclerViewAdapter.getPositionPub(0) == null) {
                        Toast.makeText(mActivity,
                                "数据加载失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mActivity, "没有更多数据", Toast.LENGTH_SHORT).show();
                    }
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                    isLoadNewData = true;
                }
                runToCritic.setVisibility(View.GONE);

            }

            @Override
            public void loadError() {
                Toast.makeText(mActivity,
                        "数据加载失败", Toast.LENGTH_SHORT).show();
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                isLoadNewData = true;
                runToCritic.setVisibility(View.GONE);
            }
        });
    }

    private class MyPageListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == mImageViews.size() - 1) {
                Message message = new Message();
                message.what = 1;
                message.arg1 = 1;
                mHandler.sendMessageDelayed(message, 400);
            } else if (mViewPager.getCurrentItem() == 0) {
                Message message = new Message();
                message.what = 1;
                message.arg1 = 4;
                mHandler.sendMessageDelayed(message, 400);
            } else {
                for (int i = 0; i < dot.length; i++) {
                    if (i != position - 1) {
                        dot[i].setPressed(false);
                    } else {
                        dot[i].setPressed(true);
                    }
                }
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                isPlay = false;
            } else {
                isPlay = true;
            }
        }
    }

    private boolean isBack = false;
    public boolean onBack() {
        if (runToCritic.getVisibility() == View.VISIBLE) {
            isBack = true;
            runToCritic.setVisibility(View.GONE);
            return true;
        } else {
            isBack = false;
            return false;
        }
    }
}


