package com.example.wayne.tablayoutdemo;

import android.graphics.Color;
import android.graphics.ImageFormat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wayne on 2018/4/8.
 */

public class TabRecyclerAdapter extends RecyclerView.Adapter {
    public static final int VIEW_TYPE_ITEM = 1;
    public static final int VIEW_TYPE_FOOTER = 2;
    private int parentHeight;
    private int itemHeight;
    private String titles[];
    private RecyclerView recyclerView;
    public TabRecyclerAdapter(String[] titles, RecyclerView recycler) {
        this.titles = titles;
        this.recyclerView = recycler;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
           final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
            view.post(new Runnable() {
                @Override
                public void run() {
                    parentHeight = recyclerView.getHeight();
                    itemHeight = view.getHeight();
                }
            });
            return new ItemViewHolder(view);
        } else {
            View view = new View(parent.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, parentHeight - itemHeight));
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("11111", "onBindViewHolder: "+ titles.length + position);
        if (position != titles.length) {
            ((ItemViewHolder)holder).setData(position);
        }
    }

    @Override
    public int getItemCount() {
        return titles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == titles.length) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitle;
        public ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            mTitle.setTextColor(Color.BLUE);
        }

        public void setData(int position) {
            switch (position) {
                case 0:
                    mTitle.setText(titles[0] + " " + 0);
                    Log.i("title", "setData: "+ titles[0]);
                    break;
                case 1:
                    mTitle.setText(titles[1] + " " + 1);
                    break;
                case 2:
                    mTitle.setText(titles[2] + " " + 2);
                    break;
                case 3:
                    mTitle.setText(titles[3] + " " + 3);
                    break;
                case 4:
                    mTitle.setText(titles[4] + " " + 4);
                    break;
                case 5:
                    mTitle.setText(titles[5] + " " + 5);
                    break;
                case 6:
                    mTitle.setText(titles[6] + " " + 6);
                    break;
                case 7:
                    mTitle.setText(titles[7] + " " + 7);
                    break;
                case 8:
                    mTitle.setText(titles[8] + " " + 8);
                    break;
                case 9:
                    mTitle.setText(titles[9] + " " + 9);
                    break;
            }
        }
    }
}



