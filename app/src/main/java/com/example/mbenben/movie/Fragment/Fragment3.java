package com.example.mbenben.movie.Fragment;

/**
 * Created by MBENBEN on 2016/10/11.
 * 该类是引导页的第三个碎片
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mbenben.movie.R;

public class Fragment3 extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.tab_fragment3, container, false);

        return view;
    }

}
