package com.conagra.hardware.layout;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.conagra.R;
import com.conagra.databinding.ItemBloodpressure1Binding;
import com.conagra.hardware.activity.BloodPressureActivity;
import com.conagra.hardware.activity.BluetoothLeAccessActivity;
import com.conagra.hardware.model.BloodPressureModel;
import com.conagra.hardware.widget.MyDialog1;
import com.conagra.hardware.widget.RulerView;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.constant.TypeApi;
import com.conagra.mvp.model.ValuesBean;
import com.conagra.mvp.utils.CommonUtils;

import io.reactivex.functions.Consumer;

/**
 * Created by yedongqin on 2016/10/25.
 */

public class BloodPressureLayout1 extends FrameLayout {

    private ItemBloodpressure1Binding mBinding;
    private BloodPressureActivity mBloodPressureActivity;
    private ValuesBean<String, String> hospitalBean;
    private Context context;
    private int heartRate;
    private BaseListener.OnItemSelectedListener<BloodPressureModel> mListener;


    public BloodPressureLayout1(Context context, BaseListener.OnItemSelectedListener<BloodPressureModel> mListener) {
        this(context, null, mListener);
    }

    public BloodPressureLayout1(Context context, AttributeSet attrs, BaseListener.OnItemSelectedListener<BloodPressureModel> mListener) {
        this(context, attrs, 0, mListener);
    }

    public RulerView getRulerViewTop() {
        return mBinding.itemBp1RvTop;
    }

    public RulerView getRulerViewBottom() {
        return mBinding.itemBp1RvBottom;
    }

    public BloodPressureLayout1(final Context context, AttributeSet attrs, int defStyleAttr, BaseListener.OnItemSelectedListener<BloodPressureModel> mListener) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.mListener = mListener;
        if (context != null && context instanceof BloodPressureActivity) {
            mBloodPressureActivity = (BloodPressureActivity) context;
        } else {
            return;
        }
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_bloodpressure1, this, true);
        mBinding.itemBloodpressure1Bluetooth.setText("点击进行连接");
        mBinding.itemBloodpressure1Bluetooth.setOnClickListener((v -> {

                        if(TextUtils.isEmpty(mBloodPressureActivity.getDeviceAddress())){
                            Intent intent1 = new Intent();
                            intent1.setClass(context, BluetoothLeAccessActivity.class);
                            Bundle bl = new Bundle();
                            intent1.putExtras(bl);
                            mBloodPressureActivity.startActivityForResult(intent1, 10);
                        }else {
                            mBloodPressureActivity.startConnectDevice(true);
                        }
//


//            if(mBloodPressureActivity.ismConnected()){
//                mBloodPressureActivity.doReTest();
//            }

        }));

        mBinding.bloodpressure1BtSave.setOnClickListener((v -> {
            if (mBloodPressureActivity.ismConnected()&&(!"保存".equals(mBinding.bloodpressure1BtSave.getText().toString()))) {


//                if("重新测量".equals(mBinding.bloodpressure1BtSave.getText().toString())){
                    mBloodPressureActivity.doReTest();
//                }

                //开始测量
//                mBloodPressureActivity.sendMessage();
            }
            else {
                mBinding.bloodpressure1BtSave.setText("保存");
                doUploadData();
            }
        }));

        mBinding.itemBp1RvBottom.setShowUp(false);
        mBinding.itemBp1RvTop.setmListener(new BaseListener.Observable111<String, Integer>() {
            @Override
            public void onAction(String value) {
                mBinding.itemBloodpressure1TvSsy.setText(value);
                if (CommonUtils.isNumber(mBinding.itemBloodpressure1TvSsy.getText().toString()) && CommonUtils.isNumber(mBinding.itemBloodpressure1TvSzy.getText().toString())) {
                    ValuesBean<Integer, String> valuesBean = getState(Integer.parseInt(mBinding.itemBloodpressure1TvSsy.getText().toString()), Integer.parseInt(mBinding.itemBloodpressure1TvSzy.getText().toString()));
                    mBinding.itemBloodpressure1State.setText(valuesBean.getValue2(0));
                }
            }

            @Override
            public void onAction1(Integer... value) {
//                LogMessage.doLogMessage("Blood"," mRulerViewTop : onAction1 :  " + value);
                if (mBinding.itemBp1RvTop.getCurrentScale() < mBinding.itemBp1RvBottom.getCurrentScale()) {
                    mBinding.itemBp1RvBottom.doScroller(value[0], value[1]);
                }
            }

        });
        mBinding.itemBp1RvBottom.setmListener(new BaseListener.Observable111<String, Integer>() {
            @Override
            public void onAction(String value) {
//                LogMessage.doLogMessage("Blood"," mRulerViewBottom : onAction  : " + value);
                mBinding.itemBloodpressure1TvSzy.setText(value);
                if (CommonUtils.isNumber(mBinding.itemBloodpressure1TvSsy.getText().toString()) && CommonUtils.isNumber(mBinding.itemBloodpressure1TvSzy.getText().toString())) {
                    ValuesBean<Integer, String> valuesBean = getState(Integer.parseInt(mBinding.itemBloodpressure1TvSsy.getText().toString()), Integer.parseInt(mBinding.itemBloodpressure1TvSzy.getText().toString()));
                    mBinding.itemBloodpressure1State.setText(valuesBean.getValue2(0));
                }
            }

            @Override
            public void onAction1(Integer... value) {

//                LogMessage.doLogMessage("Blood"," mRulerViewBottom : onAction1 :  " + value);
                if (mBinding.itemBp1RvTop.getCurrentScale() < mBinding.itemBp1RvBottom.getCurrentScale()) {
                    mBinding.itemBp1RvTop.doScroller(value[0], value[1]);
                }
            }
        });

//        mRulerViewBottom.setCurrentScale(90);
//        mRulerVip.setCurrentScale(130);

    }

    public void doConnectSuccess() {
        mBinding.itemBloodpressure1Bluetooth.setText("蓝牙成功");
        mBinding.itemBloodpressure1Bluetooth.setVisibility(GONE);
//        mBinding.bloodpressure1BtSave.setEnabled(false);
        mBinding.bloodpressure1BtSave.setText("停止测量");

    }

    public void doReTest() {
        mBinding.bloodpressure1BtSave.setEnabled(true);
        mBinding.bloodpressure1BtSave.setText("重新测量");
    }

    public void doConnecting() {
        mBinding.itemBloodpressure1Bluetooth.setEnabled(true);
        mBinding.itemBloodpressure1Bluetooth.setText("正在准备连接...");
    }
    public void doDisConnected() {
        mBinding.itemBloodpressure1Bluetooth.setVisibility(VISIBLE);
        mBinding.itemBloodpressure1Bluetooth.setText("点击进行连接");
        mBinding.bloodpressure1BtSave.setText("保存");
    }

    public void doConnectFail() {
        mBinding.itemBloodpressure1Bluetooth.setVisibility(VISIBLE);
        mBinding.itemBloodpressure1Bluetooth.setText("点击进行连接");
        mBinding.bloodpressure1BtSave.setText("保存");
    }

    public void doUpdataView() {
        if (mBinding.bloodpressure1BtSave.getText().toString().contains("测量")) {
            mBinding.bloodpressure1BtSave.setText("测量");
        }
    }

    /**
     * 蓝牙连接后，显示设备传送过来的数据，并上传
     *
     * @param systolicPressure
     * @param diastolicPressure
     * @param heartRate
     */
    public void doSetText(String systolicPressure, String diastolicPressure, String heartRate) {
        if (mBinding.bloodpressure1BtSave.getText().toString().contains("测量")) {
            mBinding.bloodpressure1BtSave.setText("测量");
        }
        mBinding.itemBloodpressure1TvSsy.setText(systolicPressure);
        mBinding.itemBloodpressure1TvSzy.setText(diastolicPressure);
        this.heartRate = Integer.parseInt(heartRate);
        mBinding.itemBp1RvTop.setCurrentScale(Integer.parseInt(systolicPressure));
        mBinding.itemBp1RvBottom.setCurrentScale(Integer.parseInt(diastolicPressure));
        if (CommonUtils.isNumber(systolicPressure) && CommonUtils.isNumber(diastolicPressure)) {
            ValuesBean<Integer, String> valuesBean = getState(Integer.parseInt(mBinding.itemBloodpressure1TvSsy.getText().toString()), Integer.parseInt(mBinding.itemBloodpressure1TvSzy.getText().toString()));
            mBinding.itemBloodpressure1State.setText(valuesBean.getValue2(0));
        }

        doUploadData();

    }

    /**
     * 上传数据
     */
    public void doUploadData() {

        if (!CommonUtils.isNumber(mBinding.itemBloodpressure1TvSsy.getText().toString()) || !CommonUtils.isNumber(mBinding.itemBloodpressure1TvSzy.getText().toString())) {
            return;
        }

        if (Integer.parseInt(mBinding.itemBloodpressure1TvSsy.getText().toString()) < Integer.parseInt(mBinding.itemBloodpressure1TvSzy.getText().toString())) {
            return;
        }


        final BloodPressureModel bloodPressureBean = new BloodPressureModel();
        bloodPressureBean.setSystolicPressure(Integer.parseInt(mBinding.itemBloodpressure1TvSsy.getText().toString()));
        bloodPressureBean.setDiastolicPressure(Integer.parseInt(mBinding.itemBloodpressure1TvSzy.getText().toString()));
        bloodPressureBean.setHeartRate(heartRate);
        bloodPressureBean.setCustomerNo(mBloodPressureActivity.getUserId());
        if (CommonUtils.isNumber(mBinding.itemBloodpressure1TvSsy.getText().toString()) && CommonUtils.isNumber(mBinding.itemBloodpressure1TvSzy.getText().toString())) {
            ValuesBean<Integer, String> valuesBean = getState(Integer.parseInt(mBinding.itemBloodpressure1TvSsy.getText().toString()),
                    Integer.parseInt(mBinding.itemBloodpressure1TvSzy.getText().toString()));
            bloodPressureBean.setState(valuesBean.getValue1(0) + "");
        }
        heartRate = 0;
        mBloodPressureActivity.requestHasData(bloodPressureBean);
//        mBloodPressureActivity.requestCreate(bloodPressureBean);
//        BloodPressureNetWorkRequest.doHasData(context,hospitalBean.getValue1(0),dataBean.getCustomerNo(),new MySubscriber<String>(String.class){
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
//                    if(!obj.contains("不存在")){
//                        bloodPressureBean.setIsDelete("1" + "");
//                    }else {
//                        bloodPressureBean.setIsDelete("0" + "");
//                    }
//                    doShowDialog(bloodPressureBean);
//                }
//           }
//        });
    }

    public void doShowDialog(final BloodPressureModel bloodPressureBean) {
        final MyDialog1 dialog1 = new MyDialog1(context);
        String message = "";
        if ("1".equals(bloodPressureBean.getIsDelete())) {
            message = "'血压'已有监测数据，是否覆盖已有的数据";
            dialog1.setmTvCancleTitle("放弃存储");
            dialog1.setmTvSureTitle("覆盖");

        } else {
            message = "是否保存数据";
            dialog1.setmTvCancleTitle("放弃存储");
            dialog1.setmTvSureTitle("保存");
        }

        dialog1.setContent(message);
        dialog1.doCancleEvent()
                .subscribe((v)-> {
                    dialog1.dismiss();
                },(e)->{});
        dialog1.doSureEvent()
                .subscribe(new Consumer() {

                    @Override
                    public void accept(Object o) throws Exception {
                        dialog1.dismiss();
//                        BloodPressureNetWorkRequest.doInsert(context,bloodPressureBean,new MySubscriber<String>(String.class){
//                            @Override
//                            public void onBackError(String message) {
//                                super.onBackError(message);
//                            }
//
//                            @Override
//                            public void onBackNext(String obj) {
//                                super.onBackNext(obj);
//                                if(null != mListener){
//                                    mListener.onItemSelectedListener(bloodPressureBean);
//                                }
//                            }
//                        });
                    }
                });
        dialog1.show();
    }

    /**
     * @param SystolicPressure  收缩压
     * @param DiastolicPressure 舒张压
     * @return
     */
    public ValuesBean<Integer, String> getState(int SystolicPressure, int DiastolicPressure) {

        ValuesBean<Integer, String> valuesBean = new ValuesBean<>();

        int pressure = SystolicPressure - DiastolicPressure;

        if (SystolicPressure >= 90 && SystolicPressure < 120 && DiastolicPressure < 80) {
            if (pressure < 20) {
                valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_EIGHT);
            } else if (pressure > 60) {
                valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_NINE);
            } else {
                valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_ZERO);
            }
            valuesBean.setValue2List(TypeApi.BLOODPRESSURE_TYPE_NORMAL);
        } else if ((SystolicPressure >= 120 && SystolicPressure <= 139) || (DiastolicPressure <= 89 && DiastolicPressure >= 80)) {
            valuesBean.setValue2List(TypeApi.BLOODPRESSURE_TYPE_GXY_NORMAL);

            if (pressure < 20) {
                valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_TEN);
            } else if (pressure > 60) {
                valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_ELEVEN);
            } else {
                valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_ONE);
            }

        } else if (SystolicPressure >= 140 || DiastolicPressure >= 90) {
            valuesBean.setValue2List(TypeApi.BLOODPRESSURE_TYPE_GXY);
            valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_TWO);
        } else if ((SystolicPressure >= 140 && SystolicPressure <= 159) || (DiastolicPressure <= 99 && DiastolicPressure >= 90)) {
            valuesBean.setValue2List(TypeApi.BLOODPRESSURE_TYPE_GXY_ONE);
            valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_THREE);
        } else if ((SystolicPressure >= 160 && SystolicPressure <= 179) || (DiastolicPressure <= 109 && DiastolicPressure >= 99)) {
            valuesBean.setValue2List(TypeApi.BLOODPRESSURE_TYPE_GXY_TWO);
            valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_FOUR);
        } else if (SystolicPressure >= 180 || DiastolicPressure >= 110) {
            valuesBean.setValue2List(TypeApi.BLOODPRESSURE_TYPE_GXY_THREE);
            valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_FIVE);
        } else if (SystolicPressure >= 140 && DiastolicPressure < 90) {
            valuesBean.setValue2List(TypeApi.BLOODPRESSURE_TYPE_GXY_DCSSQ);
            valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_SIX);
        } else if (SystolicPressure < 90 || DiastolicPressure < 60) {
            valuesBean.setValue2List(TypeApi.BLOODPRESSURE_TYPE_DXY);
            if (pressure < 20) {
                valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_TWELVE);
            } else if (pressure > 60) {
                valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_THIRTEEN);
            } else {
                valuesBean.setValue1List(TypeApi.BLOODPRESSURE_STATE_SERVEN);
            }
        }

        return valuesBean;
    }
}
