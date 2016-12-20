package com.example.mbenben.movie.Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbenben.movie.Bean.MovieInfoBean;
import com.example.mbenben.movie.R;
import com.squareup.picasso.Picasso;

/**
 * Created by alone on 2016/11/22.
 */
public class ShowMovieInfoActivity extends AppCompatActivity {
    private TextView nameTextView,ratingTextView,actTextView,descTextView,areaTextView,
            dirTextView,tagTextView, yearTextView;
    private ImageView iconImageView;
    private ImageView backImageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_movie_com_layout);
        nameTextView = (TextView) findViewById(R.id.movie_name);
        ratingTextView = (TextView) findViewById(R.id.movie_rating);
        actTextView = (TextView) findViewById(R.id.movie_act);
        descTextView = (TextView) findViewById(R.id.movie_desc);
        areaTextView = (TextView) findViewById(R.id.movie_area);
        dirTextView = (TextView) findViewById(R.id.movie_dir);
        tagTextView = (TextView) findViewById(R.id.movie_tag);
        yearTextView = (TextView) findViewById(R.id.movie_year);
        iconImageView = (ImageView) findViewById(R.id.movie_cover);
        backImageView = (ImageView) findViewById(R.id.back_icon_in_tool);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final MovieInfoBean movieInfoBean = (MovieInfoBean) bundle.getSerializable("movieInfo");
            String rating = movieInfoBean.getRating();
            if (TextUtils.isEmpty(rating)) {
                rating = "暂无评分";
            }
            nameTextView.setText("电影名:"+movieInfoBean.getTitle());
            ratingTextView.setText("评分:" + rating);
            actTextView.setText("领衔主演:"+movieInfoBean.getAct());
            descTextView.setText(movieInfoBean.getDesc());
            areaTextView.setText("地区:"+movieInfoBean.getArea());
            dirTextView.setText("导演:"+movieInfoBean.getDir());
            tagTextView.setText("影片类型:"+movieInfoBean.getTag());
            yearTextView.setText("年份:"+movieInfoBean.getYear());
            Picasso.with(this).load(movieInfoBean.getCover()).
                    skipMemoryCache().fit().into(iconImageView);
            descTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = LayoutInflater.from(ShowMovieInfoActivity.this).
                            inflate(R.layout.movie_desc_layout, null);
                    TextView textView = (TextView) view.findViewById(R.id.dialog_movie_desc);
                    Dialog alertDialog = new Dialog(ShowMovieInfoActivity.this, R.style.MyDialogStyle);
                    alertDialog.setContentView(view);
                    textView.setText(movieInfoBean.getDesc());
                    Window dialogWindow = alertDialog.getWindow();
                    //设置Dialog从窗体底部弹出
                    dialogWindow.setGravity( Gravity.BOTTOM);
                    //获得窗体的属性
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.y = 300;//设置Dialog距离底部的距离
                    lp.height = 550;
//       将属性设置给窗体
                    dialogWindow.setAttributes(lp);
                    alertDialog.show();
                }
            });
        }
    }
}
