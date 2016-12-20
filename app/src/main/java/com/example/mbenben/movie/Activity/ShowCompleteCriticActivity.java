package com.example.mbenben.movie.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.mbenben.movie.Adapter.ShowCompletedCriticAdapter;
import com.example.mbenben.movie.Bean.CommentCriticBean;
import com.example.mbenben.movie.Movie.PublishCritic;
import com.example.mbenben.movie.R;

import java.util.List;

/**
 * Created by alone on 2016/11/23.
 */
public class ShowCompleteCriticActivity extends AppCompatActivity {
    private ListView mListView;
    private ImageView backImageView;
    ShowCompletedCriticAdapter completedCriticAdapter = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_critic_complete_activity_layout);
        mListView = (ListView) findViewById(R.id.show_critic_list);
        List<CommentCriticBean> commentCriticBeanList =
                (List<CommentCriticBean>) getIntent().getSerializableExtra("commentCritics");
        Bundle bundle = getIntent().getExtras();
        PublishCritic critic = (PublishCritic) bundle.getSerializable("publishCritic");
        if (commentCriticBeanList != null && commentCriticBeanList.size() != 0) {
//            Log.d("xyf","other " +commentCriticBeanList.get(0).getCritic());
        }
        if (critic != null) {
//            Log.d("xyf", "user " + critic.getCritic());
        }
        completedCriticAdapter = new ShowCompletedCriticAdapter(this, critic, commentCriticBeanList);
        mListView.setAdapter(completedCriticAdapter);
        backImageView = (ImageView) findViewById(R.id.back_icon_in_tool);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        completedCriticAdapter.clearAll();
    }
}
