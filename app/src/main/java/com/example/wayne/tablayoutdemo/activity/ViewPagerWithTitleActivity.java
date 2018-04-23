package com.example.wayne.tablayoutdemo.activity;

import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.wayne.tablayoutdemo.R;
import com.example.wayne.tablayoutdemo.src.adapter.MyPagerWithTitlesAdapter;

import java.util.ArrayList;

public class ViewPagerWithTitleActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private PagerTitleStrip pagerTitleStrip;
    private ArrayList<View>  aList;
    private ArrayList<String>  titleList;
    private MyPagerWithTitlesAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_with_title);
        viewPager = (ViewPager) findViewById(R.id.vpager_title);
        pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pager_titles);

        aList = new ArrayList<View>();
        titleList = new ArrayList<String>();
        LayoutInflater li = getLayoutInflater();
        aList.add(li.inflate(R.layout.view_one,null, false));
        aList.add(li.inflate(R.layout.view_two,null, false));
        aList.add(li.inflate(R.layout.view_three,null, false));

        titleList.add("关注");
        titleList.add("推荐");
        titleList.add("头条");

        mAdapter = new MyPagerWithTitlesAdapter(aList, titleList);

        viewPager.setAdapter(mAdapter);
    }
}
