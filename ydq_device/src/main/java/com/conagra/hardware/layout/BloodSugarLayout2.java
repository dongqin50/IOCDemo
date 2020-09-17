package com.conagra.hardware.layout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.conagra.R;
import com.conagra.databinding.ItemBloodsugar1Binding;
import com.conagra.hardware.activity.BloodSugarActivity;
import com.conagra.hardware.activity.BloodSugarDetailActivity;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.constant.TypeApi;
import com.conagra.mvp.model.BloodSugarListModel;
import com.conagra.mvp.model.BloodSugarListRowModel;
import com.conagra.mvp.utils.TimeUtils;
import com.xiaoye.myutils.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by yedongqin on 2016/10/27.
 */

public class BloodSugarLayout2 extends LinearLayout {

    private Map<Integer,String> mBloodSugarMap;
    private CustomDatePicker customDatePicker;
    private long time;
    private Context mContext;
    //向左滑动 下一个日期 DownFetalMovementDate
    private void doScrollLeft(){
        time++;
        currentTime = TimeUtils.getYMDData(time);
        mBinding.bloodsugar1CurrentTime.setText(currentTime);
        doRequestDate(currentTime);
    }

    //向右滑动 上一个日期 UpFetalMovementDate
    private void doScrollRight(){
        time--;
        currentTime = TimeUtils.getYMDData(time);
        mBinding.bloodsugar1CurrentTime.setText(currentTime);
        doRequestDate(currentTime);
    }

    private ItemBloodsugar1Binding mBinding;
    private String currentTime;
//    private BloodSugarActivity mBloodSugarActivity;


    public BloodSugarLayout2(Context context,String currentTime) {
        this(context,null,0,currentTime);
    }

    public BloodSugarLayout2(Context context, AttributeSet attrs,String currentTime) {
        this(context, attrs,0,currentTime);
    }

    public void doHideBar(){
        mBinding.bloodsugar1BtDown.setVisibility(GONE);
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        customDatePicker = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String t) { // 回调接口，获得选中的时间
//                currentDate.setText(time.split(" ")[0]);
                currentTime = t.split(" ")[0];
                time = TimeUtils.getCountData(currentTime);
                mBinding.bloodsugar1CurrentTime.setText(currentTime);
                doRequestDate(currentTime);
            }
        }); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动

    }

    public BloodSugarLayout2(Context context, AttributeSet attrs, int defStyleAttr,String dataTime) {
        super(context, attrs, defStyleAttr);
//        mBloodSugarActivity = (BloodSugarActivity) context;
        mContext = context;
        this.currentTime = dataTime;
        time = TimeUtils.getCountData(currentTime);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_bloodsugar1,this,true);

        mBloodSugarMap = new HashMap<>();
        mBinding.bloodsugar1CurrentTime.setText(currentTime);
        mBinding.bloodsugar1View.setObservable1(new BaseListener.Observable1<String,String>() {
            //zuo
            @Override
            public void onAction(String s) {
                doScrollLeft();
            }
            //you
            @Override
            public void onAction1(String s) {
                doScrollRight();
            }
        });

        mBinding.bloodsugar1Left.setOnClickListener((v)->{
            doScrollRight();
        });
        mBinding.bloodsugar1Right.setOnClickListener((v)->{
            doScrollLeft();
        });
        mBinding.bloodsugar1TimeRl.setOnClickListener((v)->{
            customDatePicker.show(currentTime);
//            new SlideDateTimePicker.Builder(mBloodSugarActivity.getSupportFragmentManager())
//                    .setListener(new SlideDateTimeListener() {
//                        @Override
//                        public void onDateTimeSet(Date date) {
//                            currentTime = TimeUtils.doChangeFormat(date,"yyyy-MM-dd");
//                            time = TimeUtils.getCountData(currentTime);
//                            mBinding.bloodsugar1CurrentTime.setText(currentTime);
//                            doRequestDate(currentTime);
//                        }
//                    })
//                    .setInitialDate(TimeUtils.getYMDData(currentTime,"yyyy-MM-dd"))
//                    .setIs24HourTime(false)
//                    .setTheme(SlideDateTimePicker.HOLO_LIGHT)
//                    .build()
//                    .show();
        });
        initDatePicker();
        doRequestDate(currentTime);
    }

    public void renderAllData(BloodSugarListModel model){
        doClearData();
        mBinding.bloodsugar1View.updataData(model.getRows(),currentTime);
        if(model.getRows() != null){
            for(BloodSugarListRowModel row:model.getRows()){
                showData(row.getTimeSlot(),row.getBloodSugarValue() + "",row.getBloodSugarClass());
            }
        }
    }

    public void setCurrentTime(String ct){
        currentTime = ct;
        time = TimeUtils.getCountData(currentTime);
        mBinding.bloodsugar1CurrentTime.setText(ct);
    }


    public void doRequestDate(String time){
        if(mContext instanceof BloodSugarActivity){
            ((BloodSugarActivity)mContext).requestAllData(time);
        }else if(mContext instanceof BloodSugarDetailActivity){
            ((BloodSugarDetailActivity)mContext).requestAllData(time);
        }
    }

    public void doClearData(){
        mBinding.bloodsugar1TvZcq.setText("--");
        mBinding.bloodsugar1TvZch.setText("--");
        mBinding.bloodsugar1TvWcq.setText("--");
        mBinding.bloodsugar1TvWch.setText("--");
        mBinding.bloodsugar1TvWscq.setText("--");
        mBinding.bloodsugar1TvWsch.setText("--");
        mBinding.bloodsugar1TvSq.setText("--");

        mBinding.bloodsugar1Pg1.setVisibility(INVISIBLE);
        mBinding.bloodsugar1Pg2.setVisibility(INVISIBLE);
        mBinding.bloodsugar1Pg3.setVisibility(INVISIBLE);
        mBinding.bloodsugar1Pg4.setVisibility(INVISIBLE);
        mBinding.bloodsugar1Pg5.setVisibility(INVISIBLE);
        mBinding.bloodsugar1Pg6.setVisibility(INVISIBLE);
    }

    public void doClear(){
        mBloodSugarMap.clear();
        doClearData();
        mBinding.bloodsugar1View.updataData(null,null);
    }

    public void showData(int state,String value,String pg){

        pg="较高血糖";
        switch (state){
            case TypeApi.CATEGORY_AM_BEFORE:        //早餐前
                mBinding.bloodsugar1TvZcq.setText(value);
                break;
            case TypeApi.CATEGORY_AM_AFTER:         //早餐后
                mBinding.bloodsugar1TvZch.setText(value);
                if(!TextUtils.isEmpty(pg) && !pg.contains("正常")){
                    mBinding.bloodsugar1Pg1.setVisibility(VISIBLE);
                    mBinding.bloodsugar1Pg1.setText(pg);
                }
                break;
            case TypeApi.CATEGORY_PM_BEFORE:        //午餐前
                if(!TextUtils.isEmpty(pg) && !pg.contains("正常")) {
                    mBinding.bloodsugar1Pg2.setVisibility(VISIBLE);
                    mBinding.bloodsugar1Pg2.setText(pg);
                }
                mBinding.bloodsugar1TvWcq.setText(value);
                break;

            case TypeApi.CATEGORY_PM_AFTER:         //午餐后
                if(!TextUtils.isEmpty(pg) && !pg.contains("正常")){
                    mBinding.bloodsugar1Pg3.setVisibility(VISIBLE);
                    mBinding.bloodsugar1Pg3.setText(pg);
                }
                mBinding.bloodsugar1TvWch.setText(value);
                break;
            case TypeApi.CATEGORY_NIGHT_BEFORE:         //晚餐前
                if(!TextUtils.isEmpty(pg) && !pg.contains("正常")) {

                    mBinding.bloodsugar1Pg4.setVisibility(VISIBLE);
                    mBinding.bloodsugar1Pg4.setText(pg);
                }
                mBinding.bloodsugar1TvWscq.setText(value);
                break;
            case TypeApi.CATEGORY_NIGHT_AFTER:          //晚餐后
                if(!TextUtils.isEmpty(pg) && !pg.contains("正常")) {

                    mBinding.bloodsugar1Pg5.setVisibility(VISIBLE);
                    mBinding.bloodsugar1Pg5.setText(pg);
                }
                mBinding.bloodsugar1TvWsch.setText(value);
                break;
            case TypeApi.CATEGORY_SLEEP_BEFORE:         //睡觉
                if(!TextUtils.isEmpty(pg) && !pg.contains("正常")) {
                    mBinding.bloodsugar1Pg6.setVisibility(VISIBLE);
                    mBinding.bloodsugar1Pg6.setText(pg);
                }
                mBinding.bloodsugar1TvSq.setText(value);
                break;
        }
    }

}
