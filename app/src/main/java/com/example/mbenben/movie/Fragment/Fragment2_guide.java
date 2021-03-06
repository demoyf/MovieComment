package com.example.mbenben.movie.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mbenben.movie.R;
import com.squareup.picasso.Picasso;

/**
 * Created by MBENBEN on 2016/10/11.
 * 该类是引导页的第二个碎片
 */
public class Fragment2_guide extends Fragment  {
    private ImageView mImageView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        mImageView= (ImageView) view.findViewById(R.id.gudie_fragment2);
        Picasso.with(getActivity()).load(R.mipmap.guide_2).fit().into(mImageView);
        return view;
    }

}
