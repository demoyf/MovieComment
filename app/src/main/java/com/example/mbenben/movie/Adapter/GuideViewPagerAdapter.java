package com.example.mbenben.movie.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MBENBEN on 2016/10/11.
 * 该类是引导页的适配器
 */

public class GuideViewPagerAdapter extends FragmentPagerAdapter
{
   private List<Fragment> fragmentList=new ArrayList<Fragment>();

    public  GuideViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);

    }
    public  GuideViewPagerAdapter(FragmentManager fragmentManager,List<Fragment> arrayList){
        super(fragmentManager);
        this.fragmentList=arrayList;
    }

//返回要显示的fragment的对象
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    //返回的是ViewPager的页面的数量
    public int getCount() {
        return fragmentList.size();
    }
}
