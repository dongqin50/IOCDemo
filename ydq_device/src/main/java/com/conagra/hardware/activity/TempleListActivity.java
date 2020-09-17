package com.conagra.hardware.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.conagra.R;
import com.conagra.databinding.ActivityTemplelistBinding;
import com.conagra.di.component.DaggerTempleComponent;
import com.conagra.di.component.TempleComponent;
import com.conagra.di.module.TempleModule;
import com.conagra.hardware.adapter.TempleListAdapter;
import com.conagra.mvp.model.TempleListModel;
import com.conagra.mvp.presenter.TempleListPresenter;
import com.conagra.mvp.ui.activity.DragBaseAppCompatActivity;
import com.conagra.mvp.ui.view.TempleListView;

import java.util.List;

/**
 * Created by yedongqin on 2016/12/2.
 */

public class TempleListActivity extends DragBaseAppCompatActivity<
        ActivityTemplelistBinding,TempleListPresenter,TempleComponent> implements TempleListView {

    private TempleListAdapter mAdapter;
    private int page;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_templelist);
        setTitle("历史记录");
        userId = getIntent().getStringExtra("userId");
        mPresenter.setUserId(userId);
        mPresenter.registerView(this);
        doInit();
    }

    @Override
    public void renderAllData(List<TempleListModel> model, boolean update) {

       if(mAdapter.getCount() <= 0){
           if(model == null){
               showEmpty();
           }else {
               mAdapter.setDataList(model);
           }
       }else {
           mAdapter.addAll(model);
       }
        if(mAdapter.getCount() <= 0){
            showEmpty();
        }
    }

    @Override
    protected void initializeInjector() {
        mComponent = DaggerTempleComponent.builder()
                .activityModule(getActivityModule())
                .applicationComponent(getApplicationComponent())
                .templeModule(new TempleModule()).build();
        mComponent.inject(this);
    }

    public void doInit() {
        page = 1;
        mAdapter = new TempleListAdapter(mBinding.templelistRv,null);
        mAdapter.setOnClickListener((index,model)->{
            Intent intent = new Intent(TempleListActivity.this, TempleActivity.class);
                intent.putExtra("data", model.getCreateTime());
                startActivity(intent);
        });
        mBinding.templelistRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.templelistRv.setAdapter(mAdapter);
        doGetList();
    }


    private void doGetList(){
        mPresenter.GetALLList();
    }
}
