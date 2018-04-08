package com.example.wayne.tablayoutdemo;

import android.graphics.Color;
import android.provider.SyncStateContract;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mManager;
    private TabRecyclerAdapter mAdapter;


    private String titles[] = new String[]{"便民生活", "财富管理", "资金往来", "购物娱乐", "教育公益", "滴滴出行",
    "飞猪旅行", "大麦盒子", "租房买房", "第三方服务"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        initTab();
        mAdapter = new TabRecyclerAdapter(titles, mRecyclerView);

        mManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setAdapter(mAdapter);
        mTabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorBlue));
        mTabLayout.setTabTextColors(Color.WHITE, ContextCompat.getColor(this, R.color.colorBlue));


        setListener();
    }

    private void setListener() {
        // 列表滑动切换tab
        mRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                // 滑动recycleview list 的时候，根据最上面一个item的position来切换tab
                mTabLayout.setScrollPosition(mManager.findFirstVisibleItemPosition(), 0, true);
            }
        });
        // tab切换 列表滑动至对应的item
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 点击tab的时候，recyclerview自动滑动到tab对应的item位置
                mManager.scrollToPositionWithOffset(tab.getPosition(), 0);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initTab() {
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[0]).setTag(0));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[1]).setTag(1));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[2]).setTag(2));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[3]).setTag(3));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[4]).setTag(4));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[5]).setTag(5));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[6]).setTag(6));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[7]).setTag(7));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[8]).setTag(8));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles[9]).setTag(9));
    }

}

