package com.conagra.hardware.layout;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.conagra.R;
import com.conagra.databinding.ItemBloodsugarBinding;
import com.conagra.hardware.activity.BloodSugarActivity;
import com.conagra.hardware.activity.BloodSugarKnowledgeActivity;
import com.conagra.hardware.model.BloodSugarModel;
import com.conagra.hardware.widget.RulerView1;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.constant.TypeApi;
import com.conagra.mvp.utils.ActivityUtils;
import com.conagra.mvp.utils.CommonUtils;
import com.conagra.mvp.utils.LogMessage;
import com.conagra.mvp.utils.TimeUtils;
import com.xiaoye.myutils.utils.ToastUtils;

/**
 * Created by yedongqin on 2016/10/27.
 */

public class BloodSugarLayout1 extends LinearLayout {

    private int status;
    private ItemBloodsugarBinding mBinding;
    private BloodSugarActivity mBloodSugarActivity;
    private static final String TAG  = "BloodSugarLayout1";
    private BaseListener.OnItemSelectedListener<BloodSugarModel> mListener;


    public BloodSugarLayout1(Context context, BaseListener.OnItemSelectedListener<BloodSugarModel> mListener) {
        this(context,null,0,mListener);
    }

    public BloodSugarLayout1(Context context, AttributeSet attrs, BaseListener.OnItemSelectedListener<BloodSugarModel> mListener) {
        this(context, attrs,0,mListener);
    }

    public BloodSugarLayout1(final Context context, AttributeSet attrs, int defStyleAttr, BaseListener.OnItemSelectedListener<BloodSugarModel> mListener) {
        super(context, attrs, defStyleAttr);
        mBloodSugarActivity = (BloodSugarActivity) context;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_bloodsugar,this,true);
        this.mListener = mListener;

        mBinding.bloodsugarItemBtXzs.setOnClickListener((v) -> {
            ActivityUtils.toNextActivity(mBloodSugarActivity, BloodSugarKnowledgeActivity.class);
        });

        mBinding.bloodsugarItemBtRuler.setmListener(new BaseListener.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelectedListener(String value) {
                if(!CommonUtils.isNumber(value)){
                    return;
                }
                mBinding.bloodsugarItemTvValue.setText(value);
                mBinding.bloodsugarItemBsv.setmCurrentValue(Double.parseDouble(value));
            }
        });

        doShowState(TypeApi.CATEGORY_AM_BEFORE);
        int time =  TimeUtils.intervalTimePN(TimeUtils.getCurrentTime(), TimeUtils.getYMDData(0) + " 06:00:00");
        if(time > 5*3600){
            time =  TimeUtils.intervalTimePN(TimeUtils.getCurrentTime(), TimeUtils.getYMDData(0) + " 11:00:00");
            if(time > 6*3600){
                time =  TimeUtils.intervalTimePN(TimeUtils.getCurrentTime(), TimeUtils.getYMDData(0) + " 17:00:00");
                if(time > 4 *3600 ){
                    time =  TimeUtils.intervalTimePN(TimeUtils.getCurrentTime(), TimeUtils.getYMDData(0) + " 19:00:00");
                    if(time > 5 * 3600){
                        doShowState(TypeApi.CATEGORY_AM_BEFORE);
                    }else if(time > 0){
                        doShowState(TypeApi.CATEGORY_SLEEP_BEFORE);
                    }

                }else if(time > 0){
                    doShowState(TypeApi.CATEGORY_NIGHT_BEFORE);
                }

            }else if(time > 0){
                doShowState(TypeApi.CATEGORY_PM_BEFORE);
            }
        }else if(time > 0){
            doShowState(TypeApi.CATEGORY_AM_BEFORE);
        }

        mBinding.bloodsugarItemLlZc.setOnClickListener((v) -> {
            int time1 =  TimeUtils.intervalTimePN(TimeUtils.getCurrentTime(), TimeUtils.getYMDData(0) + " 06:00:00");
            if(time1 > 5*3600){
                ToastUtils.getInstance().makeText(context,"已过早餐时间");
            }else if(time1 < 0){
                ToastUtils.getInstance().makeText(context,"未到早餐时间");
            }else{
                doShowState(TypeApi.CATEGORY_AM_BEFORE);
            }
        });

        mBinding.bloodsugarItemLlWc.setOnClickListener((v) -> {
            int time1 =  TimeUtils.intervalTimePN(TimeUtils.getCurrentTime(), TimeUtils.getYMDData(0) + " 11:00:00");
            if(time1 > 6*3600){
                ToastUtils.getInstance().makeText(context,"已过午餐时间");
            }else if(time1 < 0){
                ToastUtils.getInstance().makeText(context,"未到午餐时间");
            }else{
                doShowState(TypeApi.CATEGORY_PM_BEFORE);
            }
        });
        mBinding.bloodsugarItemLlWsc.setOnClickListener((v)->{
            int time1 =  TimeUtils.intervalTimePN(TimeUtils.getCurrentTime(), TimeUtils.getYMDData(0) + " 17:00:00");
            if(time1 > 4 * 3600){
                ToastUtils.getInstance().makeText(context,"已过晚餐时间");
            }else if(time1 < 0){
                ToastUtils.getInstance().makeText(context,"未到晚餐时间");
            }else{
                doShowState(TypeApi.CATEGORY_NIGHT_BEFORE);
            }
        });
        mBinding.bloodsugarItemLlSq.setOnClickListener((v) -> {
            int time1 =  TimeUtils.intervalTimePN(TimeUtils.getCurrentTime(), TimeUtils.getYMDData(0) + " 19:00:00");
            if(time1 > 5 * 3600){
                ToastUtils.getInstance().makeText(context,"已过睡前时间");
            }else if(time1 < 0){
                ToastUtils.getInstance().makeText(context,"未到睡前时间");
            }else{
                doShowState(TypeApi.CATEGORY_SLEEP_BEFORE);
            }
        });

        mBinding.bloodsugarItemBtCq.setOnClickListener((v) -> {
            if(status %2 == 1){
                doShowState(status-1);
            }
        });

        mBinding.bloodsugarItemBtCh.setOnClickListener((v) -> {
            if(status %2 == 0){
                doShowState(status+1);
            }
        });

        mBinding.bloodsugarItemBluetooth.setText("点击进行连接");

        mBinding.bloodsugarItemBluetooth.setOnClickListener((v) -> mBloodSugarActivity.startConnectDevice());
        mBinding.bloodsugarItemBtSave.setOnClickListener((v) -> {
            if (mBloodSugarActivity.getmBluetoothLeService() != null && mBloodSugarActivity.ismConnected()) {
                mBinding.bloodsugarItemBtSave.setText("测量中...");
                mBinding.bloodsugarItemBtSave.setEnabled(false);
                //开始测量
//                mBloodSugarActivity.getmBluetoothLeService().send_start();
            } else {
                mBinding.bloodsugarItemBtSave.setText("保存");
                doUploadData();
            }
        });

    }

    public void doUpdataView(){
        if(mBinding.bloodsugarItemBtSave.getText().toString().contains("测量")){
            mBinding.bloodsugarItemBtSave.setText("测量");
            mBinding.bloodsugarItemBtSave.setEnabled(true);
        }
    }

    public void doShowState(int state){

        mBinding.bloodsugarItemBtStateLl.setVisibility(VISIBLE);
        mBinding.bloodsugarItemVSq.setVisibility(INVISIBLE);
        mBinding.bloodsugarItemVWc.setVisibility(INVISIBLE);
        mBinding.bloodsugarItemVWsc.setVisibility(INVISIBLE);
        mBinding.bloodsugarItemVZc.setVisibility(INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBinding.bloodsugarItemBtCh.setBackground(null);
        }
        mBinding.bloodsugarItemTvSq.setTextColor(getResources().getColor(R.color.color_text1));
        mBinding.bloodsugarItemTvWc.setTextColor(getResources().getColor(R.color.color_text1));
        mBinding.bloodsugarItemTvWsc.setTextColor(getResources().getColor(R.color.color_text1));
        mBinding.bloodsugarItemTvZc.setTextColor(getResources().getColor(R.color.color_text1));

        switch (state){
            case TypeApi.CATEGORY_AM_BEFORE:        //早餐前
                mBinding.bloodsugarItemVZc.setVisibility(VISIBLE);
                mBinding.bloodsugarItemBtCq.setBackgroundResource(R.drawable.rec_green_arc1);
                mBinding.bloodsugarItemBtCq.setTextColor(getResources().getColor(R.color.white));
                mBinding.bloodsugarItemBtCh.setBackgroundResource(android.R.color.transparent);
                mBinding.bloodsugarItemBtCh.setTextColor(getResources().getColor(R.color.color_text1));
                mBinding.bloodsugarItemTvZc.setTextColor(getResources().getColor(R.color.colorMain));
                break;
            case TypeApi.CATEGORY_AM_AFTER:         //早餐后
                mBinding.bloodsugarItemVZc.setVisibility(VISIBLE);
                mBinding.bloodsugarItemBtCh.setBackgroundResource(R.drawable.rec_green_arc1);
                mBinding.bloodsugarItemBtCq.setBackgroundResource(android.R.color.transparent);
                mBinding.bloodsugarItemBtCh.setTextColor(getResources().getColor(R.color.white));
                mBinding.bloodsugarItemBtCq.setTextColor(getResources().getColor(R.color.color_text1));
                mBinding.bloodsugarItemTvZc.setTextColor(getResources().getColor(R.color.colorMain));
                break;
            case TypeApi.CATEGORY_PM_BEFORE:        //午餐前
                mBinding.bloodsugarItemVWc.setVisibility(VISIBLE);
                mBinding.bloodsugarItemBtCq.setBackgroundResource(R.drawable.rec_green_arc1);
                mBinding.bloodsugarItemBtCq.setTextColor(getResources().getColor(R.color.white));
                mBinding.bloodsugarItemBtCh.setTextColor(getResources().getColor(R.color.color_text1));
                mBinding.bloodsugarItemBtCh.setBackgroundResource(android.R.color.transparent);
                mBinding.bloodsugarItemTvWc.setTextColor(getResources().getColor(R.color.colorMain));
                break;
            case TypeApi.CATEGORY_PM_AFTER:         //午餐后
                mBinding.bloodsugarItemVWc.setVisibility(VISIBLE);
                mBinding.bloodsugarItemBtCh.setBackgroundResource(R.drawable.rec_green_arc1);
                mBinding.bloodsugarItemBtCh.setTextColor(getResources().getColor(R.color.white));
                mBinding.bloodsugarItemBtCq.setBackgroundResource(android.R.color.transparent);
                mBinding.bloodsugarItemBtCq.setTextColor(getResources().getColor(R.color.color_text1));
                mBinding.bloodsugarItemTvWc.setTextColor(getResources().getColor(R.color.colorMain));
                break;
            case TypeApi.CATEGORY_NIGHT_BEFORE:         //晚餐前
                mBinding.bloodsugarItemVWsc.setVisibility(VISIBLE);
                mBinding.bloodsugarItemTvWsc.setTextColor(getResources().getColor(R.color.colorMain));
                mBinding.bloodsugarItemBtCq.setTextColor(getResources().getColor(R.color.white));
                mBinding.bloodsugarItemBtCh.setTextColor(getResources().getColor(R.color.color_text1));
                mBinding.bloodsugarItemBtCh.setBackgroundResource(android.R.color.transparent);
                mBinding.bloodsugarItemBtCq.setBackgroundResource(R.drawable.rec_green_arc1);
                break;
            case TypeApi.CATEGORY_NIGHT_AFTER:          //晚餐后
                mBinding.bloodsugarItemVWsc.setVisibility(VISIBLE);
                mBinding.bloodsugarItemTvWsc.setTextColor(getResources().getColor(R.color.colorMain));
                mBinding.bloodsugarItemBtCh.setTextColor(getResources().getColor(R.color.white));
                mBinding.bloodsugarItemBtCq.setBackgroundResource(android.R.color.transparent);
                mBinding.bloodsugarItemBtCq.setTextColor(getResources().getColor(R.color.color_text1));
                mBinding.bloodsugarItemBtCh.setBackgroundResource(R.drawable.rec_green_arc1);
                break;
            case TypeApi.CATEGORY_SLEEP_BEFORE:         //睡觉
                mBinding.bloodsugarItemBtStateLl.setVisibility(INVISIBLE);
                mBinding.bloodsugarItemTvSq.setTextColor(getResources().getColor(R.color.colorMain));
                mBinding.bloodsugarItemBtCq.setTextColor(getResources().getColor(R.color.white));
                mBinding.bloodsugarItemBtCh.setTextColor(getResources().getColor(R.color.color_text1));
                mBinding.bloodsugarItemBtCh.setBackgroundResource(android.R.color.transparent);
                mBinding.bloodsugarItemVSq.setVisibility(VISIBLE);
                break;
        }
        status = state;
        LogMessage.doLogMessage(TAG,"  state : " + state);

    }

    public void doConnectSuccess(){
        mBinding.bloodsugarItemBluetooth.setText("蓝牙成功");
        mBinding.bloodsugarItemBluetooth.setVisibility(GONE);
        mBinding.bloodsugarItemBtSave.setText("测量");
    }

    public void doScaning(){
        mBinding.bloodsugarItemBluetooth.setText("正在扫描设备...");
    }

    public void doConnecting(){
        mBinding.bloodsugarItemBluetooth.setText("正在准备连接...");
    }

    public void doConnectFail(){
        mBinding.bloodsugarItemBluetooth.setVisibility(VISIBLE);
        mBinding.bloodsugarItemBluetooth.setText("点击进行连接");
        mBinding.bloodsugarItemBtSave.setText("保存");
    }

    /**
     * 蓝牙连接后，显示设备传送过来的数据，并上传
     * @param value
     */
    public void doSetText(String  value){
            doUpdataView();
//        if(!TextUtils.isEmpty(value) && CommonUtils.isNumber(value)){
            mBinding.bloodsugarItemTvValue.setText(value);
        mBinding.bloodsugarItemBtRuler.setCurrentScale(Double.parseDouble(value));
            mBinding.bloodsugarItemBsv.setmCurrentValue(Double.parseDouble(value));
            doUploadData();
//        }
    }

    public RulerView1 getmRulerView() {
        return mBinding.bloodsugarItemBtRuler;
    }

    /**
     * 上传数据
     */
    public void doUploadData(){
//         final BloodSugarModel mBloodSugarBean = new BloodSugarModel();
//        mBloodSugarBean.setCategory(status + "");
//        mBloodSugarBean.setBloodSugarValue(mTvValue.getText().toString());
//        mBloodSugarBean.setCustomerNo(mBloodSugarActivity.mUserBean.getCustomerNo());
//        mBloodSugarBean.setHospitalID(mBloodSugarActivity.mHosptialBean.getValue1(0));
//        mBloodSugarBean.setState("0");
//        mBloodSugarBean.setRecorderID(mBloodSugarActivity.mUserBean.getCustomerNo());

        mBloodSugarActivity.requestHasData(status,mBinding.bloodsugarItemTvValue.getText().toString());
//        BloodSugarNetWorkRequest.doHasData(
//                mBloodSugarActivity,
//                mBloodSugarBean.getHospitalID(),
//                mBloodSugarBean.getCustomerNo(),mBloodSugarBean.getCategory(),new MySubscriber<String>(String.class){
//            @Override
//            public void onBackError(String message) {
//                super.onBackError(message);
//
//            }
//
//            @Override
//            public void onBackNext(String obj) {
//                super.onBackNext(obj);
//                if(!TextUtils.isEmpty(obj)){
//
//                    if(!obj.contains("不存在")){
//                        mBloodSugarBean.setIsDelete("1" + "");
//                    }else {
//                        mBloodSugarBean.setIsDelete("0" + "");
//                    }
//                    doShowDialog(mBloodSugarBean);
//                }
//            }
//        });
    }


}
