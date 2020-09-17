package com.conagra.hardware.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.conagra.R;
import com.conagra.databinding.ActivityQuickeningBinding;
import com.conagra.di.component.DaggerFealMoveComponent;
import com.conagra.di.component.FealMoveComponent;
import com.conagra.di.module.FealMoveModule;
import com.conagra.hardware.adapter.QuickeningAdapter;
import com.conagra.hardware.layout.Quickening1Layout;
import com.conagra.hardware.layout.Quickening2Layout;
import com.conagra.hardware.model.FetalMovementModel;
import com.conagra.hardware.widget.MyDialog1;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.constant.TypeApi;
import com.conagra.mvp.model.FetalMoveListsModel;
import com.conagra.mvp.presenter.FealMovePresenter;
import com.conagra.mvp.ui.activity.DragBaseFragmentActivity;
import com.conagra.mvp.ui.view.FealMoveView;
import com.conagra.mvp.utils.TimeUtils;
import com.xiaoye.myutils.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by yedongqin on 16/10/7.
 * 胎动
 */
public class FetalMoveActivity extends DragBaseFragmentActivity<ActivityQuickeningBinding,FealMovePresenter,FealMoveComponent> implements BaseListener.Observable<String>,FealMoveView{

    private List<View> viewList;
    private Quickening1Layout view1;
    private Quickening2Layout view2 ;
    private QuickeningAdapter mAdapter;
    private String currentTime;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_quickening);
        setTitle("胎动监测");
        doInit();

    }

    @Override
    protected void initializeInjector() {
        mComponent = DaggerFealMoveComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .fealMoveModule(new FealMoveModule()).build();
        mComponent.inject(this);
    }

    //    @Override
//    public void onCreate() {
//        setContentView(R.layout.activity_quickening);
//    }

    /**
     * 渲染某个的数据
     */
    @Override
    public void renderOtherData() {

    }

    public String getUserId() {
        return userId;
    }

    public void doInit() {
        mPresenter.registerView(this);
        Intent intent = getIntent();
        if(intent != null ){
            Bundle bundle = intent.getExtras();
            if(bundle != null && bundle.containsKey("data")){
                currentTime = intent.getStringExtra("data");
            }
            userId = getIntent().getStringExtra("userId");
            mPresenter.setUserId(userId);
        }

        viewList = new ArrayList<>();

        if(TextUtils.isEmpty(currentTime)){
            view1 = new Quickening1Layout(this,"",this);
            viewList.add(view1);
            currentTime = TimeUtils.getYMDData(0);
            view2 = new Quickening2Layout(this,currentTime);
//            mHeader.setTitle("胎动监测");
//            mHeader.setOtherText("");
//            mHeader.setmRightIv(R.drawable.time_ico);
//            mHeader.doBtRightClick()
//                    .subscribe(new Action1() {
//                        @Override
//                        public void call(Object o) {
//                            ActivityUtils.toNextActivity(FetalMoveListActivity.class);
//                        }
//                    });
            setRightComponent(R.drawable.time_ico, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(FetalMoveActivity.this,FetalMoveListActivity.class);
                    i.putExtra("userId",userId);
                    startActivity(i);
                }
            });
        }else {
//            mHeader.setTitle("历史详情");
            view2 = new Quickening2Layout(this,currentTime);
            view2.doHideBar();
        }

//        mHeader.doBack()
//                .subscribe(new Action1() {
//                    @Override
//                    public void call(Object o) {
//                        doStop(TypeApi.STOP_STATUS_BACK);
//                    }
//                });
//        view2.setCurrentTime(currentTime);
        viewList.add(view2);
        mAdapter = new QuickeningAdapter(mBinding.quickeningRv, viewList);
        mBinding.quickeningRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.quickeningRv.setAdapter(mAdapter);
    }



   public void requestCreate(FetalMovementModel model){
       mPresenter.create(model);
   }
    public void requestGetALLList(String startTime,String endTime){
       mPresenter.GetALLList(startTime,endTime);
   }


    @Override
    public void renderAllData(FetalMoveListsModel model) {
        view2.renderAllData(model);
    }

    @Override
    public void createOrUpdateSuccess() {
        ToastUtils.getInstance().makeText(this,"添加成功");
    }

    /**
     * 停止监测
     */
    public void doStop(int index){

        switch (index){
            case  TypeApi.STOP_STATUS_BACK:
                if(view1 != null && view1.getRlStopVisibility() == View.VISIBLE){
                    final MyDialog1 myDialog1 = new MyDialog1(this);
                    myDialog1.setContent("您当前正在监测,确定要停止监测吗?");
                    myDialog1.doSureEvent()
                            .subscribe(new Consumer() {

                                @Override
                                public void accept(Object o) throws Exception {
                                    myDialog1.dismiss();
                                    view1.doStopJc(false);
                                    finish();
                                }
                            });
                    myDialog1.doCancleEvent()
                            .subscribe(new Consumer() {

                                @Override
                                public void accept(Object o) throws Exception {
                                    myDialog1.dismiss();
                                }
                            });
                    myDialog1.show();
                }else {
                    finish();
                }
                break;
            case TypeApi.STOP_STATUS_FORCE:
                if(view1 != null && view1.getRlStopVisibility() == View.VISIBLE){
                    final MyDialog1 myDialog1 = new MyDialog1(this);
                    myDialog1.setContent("您当前正在监测,确定要停止监测吗?");
                    myDialog1.doSureEvent()
                            .subscribe(new Consumer() {

                                @Override
                                public void accept(Object o) throws Exception {
                                    myDialog1.dismiss();
                                    view1.doStopJc(false);
                                }
                            });
                    myDialog1.doCancleEvent()
                            .subscribe(new Consumer() {

                                @Override
                                public void accept(Object o) throws Exception {
                                    myDialog1.dismiss();
                                }
                            });
                    myDialog1.show();
                }

                break;
            case TypeApi.STOP_STATUS_NORMAL:
                if(view1 != null && view1.getRlStopVisibility() == View.VISIBLE){
                    view1.doStopJc(true);
                }
                break;
        }
    }


    @Override
    public void finish() {
        if(view1 != null && view1.getRlStopVisibility() == View.VISIBLE){
            final MyDialog1 myDialog1 = new MyDialog1(this);
            myDialog1.setContent("您当前正在录制,确定要停止录制并保存吗?");
            myDialog1.doSureEvent()
                    .subscribe(new Consumer() {
                        @Override
                        public void accept(Object o) throws Exception {
                            myDialog1.dismiss();
                            FetalMoveActivity.super.finish();
                        }
                    });
            myDialog1.doCancleEvent()
                    .subscribe(new Consumer() {
                        @Override
                        public void accept(Object o) throws Exception {
                            myDialog1.dismiss();
                        }
                    });
            myDialog1.show();
        }else {
            super.finish();
        }

    }



    @Override
    public void onAction(String time) {
        if(view2 != null){
            view2.doClear();
            view2.setCurrentTime(time);
            view2.updateQuickeningView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(view1 != null)
            view1.stopService();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK){
            doStop(TypeApi.STOP_STATUS_BACK);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
