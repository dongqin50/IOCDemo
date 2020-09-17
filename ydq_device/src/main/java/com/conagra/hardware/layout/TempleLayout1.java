package com.conagra.hardware.layout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.conagra.R;
import com.conagra.databinding.ItemTemple1Binding;
import com.conagra.hardware.activity.TempleActivity;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.utils.CommonUtils;


/**
 * Created by yedongqin on 2016/10/27.
 */

public class TempleLayout1 extends LinearLayout {

    TempleActivity mActivity;
    private BaseListener.Observable<String> mListener;
    private ItemTemple1Binding mBinding;
    public void setListener(BaseListener.Observable<String> listener) {
       this.mListener = listener;
    }
    public TempleLayout1(Context context) {
        this(context,null);
    }

    public TempleLayout1(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TempleLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_temple1,this,true);
        mActivity = (TempleActivity) context;
        doInit();
    }


    public void doConnected(){
        mBinding.itemTemple1Bt.setText("测量中...");
        mBinding.itemTemple1Bt.setEnabled(false);
        mBinding.itemTemple1Bluetooth.setVisibility(GONE);

    }

    public void doConnecting(){
        mBinding.itemTemple1Bt.setEnabled(true);
        mBinding.itemTemple1Bluetooth.setVisibility(VISIBLE);
        mBinding.itemTemple1Bt.setText("保存");
        mBinding.itemTemple1Bluetooth.setText("正在连接...");

    }
    public View getView() {
        return mBinding.getRoot();
    }

    public void doDisConnected(){
        mBinding.itemTemple1Bluetooth.setText("点击连接");
        mBinding.itemTemple1Bt.setText("保存");
        mBinding.itemTemple1Bt.setEnabled(true);
        mBinding.itemTemple1Bluetooth.setVisibility(VISIBLE);
    }

    private void doInit(){
        mBinding.itemTemple1Ruler.setmListener(new BaseListener.Observable<String>() {
            @Override
            public void onAction(String s) {
                doRecordData(s);
            }
        });

        mBinding.itemTemple1Bt.setOnClickListener((v)->{
            if(mBinding.itemTemple1Bt.getText().toString().contains("保存")){
                doHasData();
            }
        });

        mBinding.itemTemple1Bluetooth.setOnClickListener((v -> {

            mActivity.onConnectClick();
        }));

    }


    private void doHasData(){
        if(CommonUtils.isNumber( mBinding.itemTemple1Wd.getText().toString())) {
            mActivity.requestCreate(mBinding.itemTemple1Wd.getText().toString());
        }
    }

    public void doRecordData(String tempValueString){
         mBinding.itemTemple1Wd.setText(tempValueString);
        if(CommonUtils.isNumber(tempValueString)){
            Float num = Float.valueOf(tempValueString);
            if(mActivity.ismConnected()){
                mBinding.itemTemple1Ruler.setCurrentScale(num);
            }
//            item_temple1_view
            mBinding.itemTemple1View.setCurrentTemple(num);
            if(num > 37.0){
                mBinding.itemTemple1Status.setText("偏高");
                mBinding.itemTemple1Status.setTextColor(getResources().getColor(R.color.red));
                mBinding.temple1Warning.setVisibility(VISIBLE);
            }else {
                mBinding.itemTemple1Status.setText("正常");
                mBinding.itemTemple1Status.setTextColor(getResources().getColor(R.color.mine_text1));
                mBinding.temple1Warning.setVisibility(INVISIBLE);
            }
        }else {
             mBinding.itemTemple1Wd.setText("--");
        }

        if(!mBinding.itemTemple1Bt.getText().toString().contains("保存")){
            doHasData();
        }

    }


    private void doInsert(float tempValue,String lsh){
        int state = 0;
        if(tempValue > 37.0){
            state = 1;
        }
    }

}
