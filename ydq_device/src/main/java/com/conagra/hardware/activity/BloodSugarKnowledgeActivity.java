package com.conagra.hardware.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.conagra.R;
import com.conagra.databinding.ActivityBloodsugarKnowledgeBinding;
import com.conagra.mvp.ui.activity.BaseAppCompatActivity;

/**
 * Created by yedongqin on 2016/11/28.
 */

public class BloodSugarKnowledgeActivity extends BaseAppCompatActivity<ActivityBloodsugarKnowledgeBinding> {

//    @BindView(R.id.bs_knowledge_header)
//    TempleHeaderView mHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_bloodsugar_knowledge);
    }

    public void doInit() {
//        mHeader.setTitle("血糖小知识");
//        mHeader.doBack()
//                .subscribe(new Action1() {
//                    @Override
//                    public void call(Object o) {
//                        finish();
//                    }
//                });
    }
}
