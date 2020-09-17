package com.conagra.hardware.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.conagra.R;
import com.conagra.mvp.ui.activity.BaseFragmentActivity;

/**
 * Created by yedongqin on 2016/11/29.
 */

public class FetalMoveKnowledgeActivity extends BaseFragmentActivity {
//    @BindView(R.id.quicken_knowledge_header)
//    TempleHeaderView mHeader;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_felheart_knowledge);
    }



    public void doInit() {
//        mHeader.setTitle("胎动小知识");
//        mHeader.doBack()
//                .subscribe(new Action1() {
//                    @Override
//                    public void call(Object o) {
//                        finish();
//                    }
//                });
    }
}
