package com.conagra.hardware.layout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.conagra.R;
import com.conagra.databinding.ItemBloodpressure2Binding;
import com.conagra.hardware.activity.BloodPressureActivity;
import com.conagra.hardware.activity.BloodPressureDetailActivity;
import com.conagra.hardware.model.BloodPressureListWeekBackModel;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.model.BloodPressureListModel;
import com.conagra.mvp.model.BloodPressureRowBean;
import com.conagra.mvp.utils.TimeUtils;
import com.xiaoye.myutils.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BloodPressureLayout2 extends FrameLayout {

    private Context context;
    private ItemBloodpressure2Binding mBinding;
    private BloodPressureActivity activity;
    private BloodPressureDetailActivity mBloodPressureDetailActivity;
    private List<BloodPressureListWeekBackModel> mBloodPressureViewList;
    private Map<String,Integer> mBloodPressureViewMap;
    private String currentTime;
    private String lastTime;
    private String nextTime;
    private CustomDatePicker customDatePicker;
    private long time;

    //向左滑动 下一个日期 DownFetalMovementDate
    private void doScrollLeft(String time1){

        time++;
        currentTime = TimeUtils.getYMDData(time);
        mBinding.bloodpressure2CurrentTime.setText(currentTime);
        doRequestDate();
    }

    //向右滑动 上一个日期 UpFetalMovementDate
    private void doScrollRight(String time1){
        time--;
        currentTime = TimeUtils.getYMDData(time);
        mBinding.bloodpressure2CurrentTime.setText(currentTime);
        doRequestDate();
    }

    public BloodPressureLayout2(Context context,String timeData) {
        this(context,null,0,timeData);

    }

    public BloodPressureLayout2(Context context, AttributeSet attrs,String timeData) {
        this(context, attrs,0,timeData);
    }

    public BloodPressureLayout2(final Context context, AttributeSet attrs, int defStyleAttr,String timeData) {
        super(context, attrs, defStyleAttr);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_bloodpressure2,this,true);
        this.currentTime = timeData;
        time = TimeUtils.getCountData(currentTime);

        mBinding.bloodpressure2CurrentTime.setText(currentTime);
        if(context instanceof BloodPressureDetailActivity){
            mBloodPressureDetailActivity = (BloodPressureDetailActivity) context;
        }else if(context instanceof BloodPressureActivity){
            activity = (BloodPressureActivity)context;
        }

        mBloodPressureViewList = new ArrayList<>();
        mBloodPressureViewMap = new HashMap<>();
        this.context = context;

        mBinding.bloodpressure2View.setObservable1(new BaseListener.Observable1<String,String>() {
            //zuo
            @Override
            public void onAction(String s) {
                doScrollLeft(s);
            }
            //you
            @Override
            public void onAction1(String s) {
                doScrollRight(s);
            }
        });
        mBinding.bloodpressure2Left.setOnClickListener((v -> {
            doScrollRight(currentTime);
        }));
        mBinding.bloodpressure2Right.setOnClickListener((v -> {
            doScrollLeft(currentTime);
        }));
        mBinding.bloodpressure2TimeRl.setOnClickListener((v -> {
            if(context instanceof FragmentActivity){
                customDatePicker.show(currentTime);
//                BloodPressureActivity fragmentActivity = (BloodPressureActivity) context;
//                new SlideDateTimePicker.Builder(fragmentActivity.getSupportFragmentManager())
//                        .setListener(new SlideDateTimeListener() {
//                            @Override
//                            public void onDateTimeSet(Date date) {
//                                currentTime = TimeUtils.doChangeFormat(date,"yyyy-MM-dd");
//                                mBinding.bloodpressure2CurrentTime.setText(currentTime);
//                                doRequestDate();
//
//                            }
//                        })
//                        .setInitialDate(TimeUtils.getYMDData(currentTime,"yyyy-MM-dd"))
//                        //.setMinDate(minDate)
////                                .setMaxDate(maxDate)
//                        .setIs24HourTime(false)
//                        .setTheme(SlideDateTimePicker.HOLO_LIGHT)
//                        //.setIndicatorColor(Color.parseColor("#990000"))
//                        .build()
//                        .show();
            }

        }));
        initDatePicker();
        doRequestDate();
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        customDatePicker = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String t) { // 回调接口，获得选中的时间
//                currentDate.setText(time.split(" ")[0]);
                currentTime = t.split(" ")[0];
                mBinding.bloodpressure2CurrentTime.setText(currentTime);
                doRequestDate();
            }
        }); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动

    }


    public void doHideBar(){
        mBinding.bloodpressure2BtDown.setVisibility(GONE);
    }

    public void doClearData(){
        mBinding.bloodpressure2SsyTv.setText("--");
        mBinding.bloodpressure2SzyTv.setText("--");
        mBinding.bloodpressure2XlTv.setText("--");
        mBinding.bloodpressure2View.updataData(null,null);
    }

    public void doRequestDate(){
        if(activity != null){
            activity.requestGetAll(currentTime, TimeUtils.getYMDData(time + 7));
        }else if(mBloodPressureDetailActivity != null){
            mBloodPressureDetailActivity.requestGetAll(currentTime, TimeUtils.getYMDData(time + 7));
        }

    }

    public void renderAllData(BloodPressureListModel model) {
        doClearData();
        mBinding.bloodpressure2View.updataData(model,currentTime);
        if(model != null){
            for(BloodPressureRowBean rowsBean : model.getRows()){
                if(currentTime.equals(TimeUtils.getTime(rowsBean.getCreateTime(),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"))){
                    mBinding.bloodpressure2SsyTv.setText(rowsBean.getSystolicPressure() + "");
                    mBinding.bloodpressure2SzyTv.setText(rowsBean.getDiastolicPressure()  + "");
                    mBinding.bloodpressure2XlTv.setText(rowsBean.getHeartRate() + "");
                }
            }
        }
    }

    /**
     * 清空数据
     */
    public void doClear(){
        mBloodPressureViewList.clear();
        mBloodPressureViewMap.clear();
        doClearData();
    }

    public void doUpdateDate(BloodPressureListWeekBackModel bloodPressureListWeekBackBean, boolean isRequest){

//        if(bloodPressureListWeekBackBean == null){
//            doClearData();
//
//            return;
//        }
//        if(isRequest){
//            BloodPressureListWeekBackModel.DataInfoBean dataInfoBean = bloodPressureListWeekBackBean.getData_Info();
//            if(dataInfoBean != null){
//                mBinding.bloodpressure2SsyTv.setText((dataInfoBean.getSystolicPressure()) + "");
//                mBinding.bloodpressure2SzyTv.setText((dataInfoBean.getDiastolicPressure()) + "");
//                mBinding.bloodpressure2XlTv.setText((dataInfoBean.getHeartRate()) + "");
//            }else{
//                doClearData();
//            }
//            mBinding.bloodpressure2View.updataData(bloodPressureListWeekBackBean,currentTime);
//        }
//        BloodPressureListWeekBackModel.DataDateBean dataDateBean = bloodPressureListWeekBackBean.getData_Date();
//        if(dataDateBean != null && isRequest){
//            lastTime = (String) dataDateBean.getUpRecordDate();
//            nextTime = (String) dataDateBean.getDownRecordDate();
//            doRequestDate(false,lastTime,nextTime);
//        }


    }
}
