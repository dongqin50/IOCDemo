package com.conagra.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.conagra.R;


/**
 * Created by yedongqin on 2017/1/16.
 */

public abstract class BaseActivity extends FragmentActivity{

    protected LinearLayout mView;
    protected RelativeLayout mHeader;
    protected FrameLayout mContainer;
    protected TextView mTitle;
    protected LinearLayout mBack;
    protected ViewGroup itemView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_header1);
        mView = (LinearLayout) findViewById(R.id.layout_ll);
        mTitle = (TextView) findViewById(R.id.header_title);
        mBack = (LinearLayout) findViewById(R.id.header_back);
        mHeader = (RelativeLayout) findViewById(R.id.layout_header);
        mContainer = (FrameLayout) findViewById(R.id.layout_container);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.setContentView(getLayoutId());
    }

    public abstract int getLayoutId();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        View view = LayoutInflater.from(this).inflate(layoutResID,null);
        if(view instanceof ViewGroup){
            itemView = (ViewGroup) view;
        }
        this.setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        if(view instanceof ViewGroup){
            itemView = (ViewGroup) view;
        }
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContainer.addView(view,layoutParams);
    }

    public void setHeader(String title){
        mTitle.setText(title);
    }

}
