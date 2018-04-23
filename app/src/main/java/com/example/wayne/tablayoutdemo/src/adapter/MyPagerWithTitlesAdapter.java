package com.example.wayne.tablayoutdemo.src.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by wayne on 2018/4/20.
 */

public class MyPagerWithTitlesAdapter extends PagerAdapter {
    private ArrayList<View> viewArrayList;
    private ArrayList<String> titleList;
    public MyPagerWithTitlesAdapter() {
    }
    public MyPagerWithTitlesAdapter(ArrayList<View> viewLists, ArrayList<String> titleList) {
        this.viewArrayList = viewLists;
        this.titleList = titleList;

    }
    @Override
    public int getCount() {
        return viewArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewArrayList.get(position));
        return viewArrayList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(viewArrayList.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
