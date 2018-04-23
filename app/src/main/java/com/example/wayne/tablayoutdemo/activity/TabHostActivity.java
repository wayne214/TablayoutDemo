package com.example.wayne.tablayoutdemo.activity;

import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wayne.tablayoutdemo.R;
import com.example.wayne.tablayoutdemo.src.adapter.MyPagerAdapter;

import java.util.ArrayList;

public class TabHostActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private ImageView view;
    private TextView t1;
    private TextView t2;
    private TextView t3;

    private ArrayList<View> arrayList;
    private int offset = 0; // 偏移量
    private int currIndex = 0; // 当前页面编号
    private int bmpWidth; // 移动条图片的长度
    private int one; // 移动条滑动一页的距离
    private int two; // 移动条滑动两页的距离
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_host);

        initViews();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.vpager_four);
        view = (ImageView) findViewById(R.id.underLine);
        t1 = (TextView) findViewById(R.id.tv_one);
        t2 = (TextView) findViewById(R.id.tv_two);
        t3 = (TextView) findViewById(R.id.tv_three);

        // 下划线动画的相关设置
        bmpWidth = BitmapFactory.decodeResource(getResources(), R.mipmap.line).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels; // 获取分辨率宽度
        offset = (screenW / 3 - bmpWidth) / 2; //计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        view.setImageMatrix(matrix); // 设置动画初始位置
        // 滑动距离
        one = offset * 2 + bmpWidth;
        two = one * 2;


        arrayList = new ArrayList<>();
        LayoutInflater li = getLayoutInflater();
        arrayList.add(li.inflate(R.layout.view_one,null, false));
        arrayList.add(li.inflate(R.layout.view_two,null, false));
        arrayList.add(li.inflate(R.layout.view_three,null, false));
        viewPager.setAdapter(new MyPagerAdapter(arrayList));
        viewPager.setCurrentItem(0);

        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);

        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_one:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_two:
                viewPager.setCurrentItem(1);
                break;
            case R.id.tv_three:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.i("TabHost", "onPageSelected: " + position);
        Animation animation = null;
        switch (position) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, 0, 0, 0);
                }
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, one, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, one, 0, 0);
                }
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, two, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(one, two, 0, 0);
                }
                break;
        }
        currIndex = position;
        animation.setFillAfter(true); // true表示图片停在动画结束的位置
        animation.setDuration(300); // 设置动画时间为300毫秒
        view.startAnimation(animation); // 开始动画
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
