package com.conagra.hardware.layout;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.databinding.DataBindingUtil;
import android.os.IBinder;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.conagra.R;
import com.conagra.databinding.FragmentQuickening1Binding;
import com.conagra.hardware.activity.FetalMoveActivity;
import com.conagra.hardware.activity.FetalMoveKnowledgeActivity;
import com.conagra.hardware.model.FetalMovementModel;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.constant.TypeApi;
import com.conagra.mvp.model.ValuesListBean;
import com.conagra.mvp.service.CountDownService;
import com.conagra.mvp.utils.TimeUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by yedongqin on 16/10/8.
 */
public class Quickening1Layout extends FrameLayout {

    private FetalMoveActivity mActivity;
    private Context context;
    private CountDownService mService;
    private FetalMovementModel mFetalMovementBean;
    //    private ValuesListBean<String,Boolean> mTimeList;
    private List<String> mTimeList;
    private long currentTime;
    private String lastClilcTime;
    private BaseListener.Observable<String> observable;

    public Quickening1Layout(Context context, String backBean, BaseListener.Observable<String> observable) {
        this(context,null,backBean,observable);
    }

    public Quickening1Layout(Context context, AttributeSet attrs, String backBean, BaseListener.Observable<String> observable) {
        this(context, attrs,0,backBean,observable);
    }

    public Quickening1Layout(Context context, AttributeSet attrs, int defStyleAttr, String backBean, BaseListener.Observable<String> observable) {
        super(context, attrs, defStyleAttr);
        this.observable = observable;
        this.context = context;
        doInit(context);
        doWork(context);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CountDownService.CountDownBinder binder = (CountDownService.CountDownBinder) service;
            mService = binder.getCountDownService();
            updateTime();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    private void updateTime(){
//        final int num = 60 * 60;
            final int num = 5;
        mService.getCode(context,num)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long value) throws Exception {
                        Long time = num - value - 1;

                        if(value % 100 == 0){
                            mBinding.quickening1Qv.setCurrentIndex(Math.round(value/100));
                        }

                        if (time <= 0) {
                            mBinding.quickening1Time.setText("60:00");
                            //正常停止,并保存数据
                            ((FetalMoveActivity)(Quickening1Layout.this.context)).doStop(TypeApi.STOP_STATUS_NORMAL);
                        } else {
                            mBinding.quickening1Time.setText(TimeUtils.getCurrentTimeByMS(time * 1000));
                        }
                    }

                });
    }
    private FragmentQuickening1Binding mBinding;
    public void doInit(Context context) {
        mActivity = (FetalMoveActivity)context;
        mTimeList = new ValuesListBean();
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_quickening1,this,true);
    }

    public int getRlStopVisibility(){
        return mBinding.quickening1RlStop.getVisibility();
    }

    public void doWork(Context context) {
//        ButterKnife.bind(this);
//        ButterKnife.bin
        mBinding.quickening1TvTime.setText(TimeUtils.getYMDData(0));
        //开始监测
        mBinding.quickening1Start.setOnClickListener((v -> {
            doStartJc();
        }));
        //停止监测
         mBinding.quickening1BtStop.setOnClickListener((v -> {
             mActivity.doStop(TypeApi.STOP_STATUS_FORCE);
        }));
         mBinding.quickening1Xiaozhishi.setOnClickListener((v -> {
            Intent intent = new Intent(mActivity, FetalMoveKnowledgeActivity.class);
        }));
         mBinding.quickening1BtTd.setOnClickListener((v -> {
             int  numTd = Integer.parseInt(mBinding.quickening1TvTdNum.getText().toString());
             int  numClick = Integer.parseInt(mBinding.quickening1TvClickNum.getText().toString());
//                        ValuesBean<String,Boolean> valuesBean = new ValuesBean<>();
             long currentTime = System.currentTimeMillis();
             String currentTimeString = TimeUtils.getTimeByDate(currentTime);

             if(mTimeList.size() == 0){
//                            valuesBean.setValue2(true);
                 numTd++;
                 numClick++;
                 mBinding.quickening1TvTdNum.setText(numTd+"");
                 mBinding.quickening1TvClickNum.setText(numClick + "");
                 mBinding.quickening1Qv.doClick(currentTime);
//                            lastClilcTime = TimeUtils.getTimeByDate(currentTime);
                 mTimeList.add(currentTimeString);
             }else if(mTimeList.size() > 0){
                 int time = TimeUtils.intervalTime(lastClilcTime,currentTimeString);
//                            int time = TimeUtils.intervalTime((String)mTimeList.get(mTimeList.size()-1).getValue1(0),valuesBean.getValue1(0));
//                            int time = 0;
                 if(time >= 3 * 60){
                     numTd++;
                     mBinding.quickening1TvTdNum.setText(numTd+"");
//                                valuesBean.setValue2(true);
                     mBinding.quickening1Qv.doClick(currentTime);
                     mTimeList.add(currentTimeString);
                 }else{
//                                ToastUtils.getInstance().makeText(mActivity,"3分钟内连续点击算一次胎动");
                 }

                 numClick++;
                 mBinding.quickening1TvClickNum.setText(numClick + "");
             }
             lastClilcTime = currentTimeString;
        }));

    }

    public void stopService(){
        try {
            if(context != null && connection != null)
                context.unbindService(connection);
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * 开始监测
     */
    private void doStartJc() {

        mBinding.quickening1Start.setVisibility(GONE);
        mBinding.quickening1RlStop.setVisibility(VISIBLE);
        if(mService == null){
            mFetalMovementBean = new FetalMovementModel();
            mFetalMovementBean.setStartTime(TimeUtils.getCurrentTime());
            Intent intent = new Intent(context, CountDownService.class);
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }else {
            mFetalMovementBean = new FetalMovementModel();
            mFetalMovementBean.setStartTime(TimeUtils.getCurrentTime());
            updateTime();
        }

    }

    /**
     * 停止监测
     */
    public void doStopJc(boolean status) {
        mBinding.quickening1Start.setVisibility(VISIBLE);
        mBinding.quickening1RlStop.setVisibility(GONE);
        mBinding.quickening1TvTdNum.setText("0");
        mBinding.quickening1Time.setText("60:00");
        mBinding.quickening1TvClickNum.setText("0");
        mBinding.quickening1Qv.doClear();

        //正常情况下停止,保存数据
        if (status) {

            mFetalMovementBean.setCustomerNo(mActivity.getUserId());
            mFetalMovementBean.setClickNumber(Integer.parseInt(mBinding.quickening1TvTdNum.getText().toString()));
            mFetalMovementBean.setFetalMovementTimes(mTimeList);
            mActivity.requestCreate(mFetalMovementBean);
//            String s = mFetalMovementBean.getFetalMovementTimes().getValues1ToString(0);
//            if(s == null)
//                s = new String("[2015-10-12 12:10:00]");
//            s = s.replace("[","{");
//            s = s.replace("]","}");



//            FetalHeartNetWorkRequest.doInsertFetalMovement(context,mFetalMovementBean)
//                    .subscribe(new Subscriber() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onNext(Object o) {
//
//                        }
//                    });
//            HardwareNetWorkRequest.doInsertFetalMovement(
//                    context,
//                    mFetalMovementBean.getCustomerNo(),
//                    mFetalMovementBean.getHospitalID(),
//                    mFetalMovementBean.getStartTime(),
//                    mFetalMovementBean.getClickNumber(),
//                    mFetalMovementBean.getFetalMovementTimes(),
//                    new MySubscriber<String>(String.class){
//                        @Override
//                        public void onBackError(String message) {
//                            super.onBackError(message);
//                        }
//
//                        @Override
//                        public void onBackNext(String obj) {
//                            super.onBackNext(obj);
//                            if(observable != null){
//                                observable.onAction(mFetalMovementBean.getStartTime().split("\\ +")[0]);
//                            }
//                        }
//
//                        @Override
//                        public void onCompleted() {
//                            super.onCompleted();
//                            mTimeList.clear();
//                        }
//                    }
//            );


            //强制退出,不保存数据
        } else {
            mService.setStarting(false);
            mTimeList.clear();

        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(mBinding.quickening1RlStop.getVisibility() == View.VISIBLE && KeyEvent.KEYCODE_HOME == event.getKeyCode()){

        }
        return super.dispatchKeyEvent(event);
    }
}