package com.conagra.hardware.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.conagra.R;
import com.conagra.databinding.ActivityBloodfatBinding;
import com.conagra.hardware.adapter.QuickeningAdapter;
import com.conagra.hardware.layout.BloodFatLayout1;
import com.conagra.hardware.layout.BloodFatLayout2;
import com.conagra.mvp.ui.activity.BaseFragmentActivity;
import com.conagra.mvp.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yedongqin on 2016/10/27.
 */

public class BloodFatActivity extends BaseFragmentActivity<ActivityBloodfatBinding> {


//    @BindView(R.id.bloodfat_header)
//    TempleHeaderView mHeader;
    private List<View> viewList;
    private BloodFatLayout1 view1;
    private BloodFatLayout2 view2;
    private QuickeningAdapter mAdapter;
    private String currentTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_bloodfat);
        setTitle("血脂监测");
        doInit();
        Log.v("dd","dddd");
    }


    public void doInit() {
        if(TextUtils.isEmpty(currentTime)){
            currentTime = TimeUtils.getYMDData(0);
        }

        setRightComponent(R.drawable.time_ico, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewList = new ArrayList<>();
////

        view1 = new BloodFatLayout1(this);
        viewList.add(view1);
        view2 = new BloodFatLayout2(this);
        viewList.add(view2);
        mAdapter = new QuickeningAdapter(mBinding.bloodfatRv, viewList);
        mBinding.bloodfatRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.bloodfatRv.setAdapter(mAdapter);
    }
}
