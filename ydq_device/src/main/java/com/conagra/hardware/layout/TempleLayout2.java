package com.conagra.hardware.layout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.conagra.R;
import com.conagra.databinding.ItemTemple2Binding;
import com.conagra.hardware.activity.TempleActivity;
import com.conagra.hardware.model.TempleListForWeekBackModel;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.model.TempleListModel;
import com.conagra.mvp.utils.TimeUtils;
import com.xiaoye.myutils.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by yedongqin on 2016/10/27.
 */

public class TempleLayout2 extends LinearLayout {
    
    TempleActivity mActivity;
    private List<TempleListForWeekBackModel> temple2ViewList;
    private Map<String,Integer> temple2ViewMap;
    private String currentTime;
    private ItemTemple2Binding mBinding;
    private CustomDatePicker customDatePicker;
    private long time;

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
        time = TimeUtils.getCountData(currentTime);
        doWork();
        doGetListForWeek(currentTime);
    }

    public void doHideBar(){
        mBinding.temple2BtDown.setVisibility(GONE);
    }

    public TempleLayout2(Context context) {
        this(context,null);
    }

    public TempleLayout2(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TempleLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBinding =  DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_temple2,this,true);
        mActivity = (TempleActivity) context;
        initDatePicker();
//        ButterKnife.bind(this);

    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        customDatePicker = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String t) { // 回调接口，获得选中的时间
//                currentDate.setText(time.split(" ")[0]);
                currentTime = t.split(" ")[0];
                time = TimeUtils.getCountData(currentTime);
                mBinding.temple2CurrentTime.setText(currentTime);
                doGetListForWeek(currentTime);
            }
        }); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动

    }


    private void doWork(){
        mBinding.temple2CurrentTime.setText(currentTime);
        temple2ViewList = new ArrayList<>();
        temple2ViewMap = new HashMap<>();
        mBinding.temple2Left.setOnClickListener((v -> {
            doScrollRight(currentTime);
        }));
         mBinding.temple2Right.setOnClickListener((v -> {
             doScrollLeft(currentTime);
        }));
         mBinding.temple2TimeRl.setOnClickListener((v -> {

             customDatePicker.show(currentTime);
//             CustomDatePicker  customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
//                 @Override
//                 public void handle(String t) { // 回调接口，获得选中的时间
////                     currentTime = TimeUtils.doChangeFormat(t.split(" ")[0],"yyyy-MM-dd");
//                     currentTime = t.split(" ")[0];
//                     time = TimeUtils.getCountData(currentTime);
//                     mBinding.temple2CurrentTime.setText(currentTime);
//                     doGetListForWeek(currentTime);
////                     currentDate.setText(time.split(" ")[0]);
//                 }
//             }, TimeUtils.getYMDData(currentTime,"yyyy-MM-dd"), now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
//             customDatePicker.showSpecificTime(false); // 不显示时和分
//             customDatePicker.setIsLoop(false); // 不允许循环滚动

//             //时间选择器
//             TimePickerView pvTime = new TimePickerBuilder(getContext(), new TimePickerView.OnTimeSelectListener() {
//                 @Override
//                 public void onTimeSelect(Date date, View v) {
////                     Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
//                 }
//             }).build();

//             new SlideDateTimePicker.Builder(mActivity.getSupportFragmentManager())
//                     .setListener(new SlideDateTimeListener() {
//                         @Override
//                         public void onDateTimeSet(Date date) {
//                             currentTime = TimeUtils.doChangeFormat(date,"yyyy-MM-dd");
//                             time = TimeUtils.getCountData(currentTime);
//                             mBinding.temple2CurrentTime.setText(currentTime);
//                             doGetListForWeek(currentTime);
//
//                         }
//                     })
//                     .setInitialDate(TimeUtils.getYMDData(currentTime,"yyyy-MM-dd"))
//                     //.setMinDate(minDate)
////                                .setMaxDate(maxDate)
//                     .setmYMDTime(false)
//                     .setIs24HourTime(false)
//                     .setTheme(SlideDateTimePicker.HOLO_LIGHT)
//                     //.setIndicatorColor(Color.parseColor("#990000"))
//                     .build()
//                     .show();
        }));


        mBinding.temple2View.setObservable1(new BaseListener.Observable1<String,String>() {
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
    }

    //向左滑动 下一个日期 DownFetalMovementDate
    private void doScrollLeft(String time1){

        time++;
        currentTime = TimeUtils.getYMDData(time);
        mBinding.temple2CurrentTime.setText(currentTime);
        doGetListForWeek(currentTime);

//        if(!TextUtils.isEmpty(time) && temple2ViewMap.containsKey(time)){
//            ToastUtils.getInstance().makeText(mActivity,"向左滑动 : " + time);
//
////                        doGetListForWeek(currentTime,true);
////                        int position = temple2ViewMap.get(time);
////                        BloodPressureListWeekBackModel fetalMovementListBakeBean = temple2ViewList.get(position).getValue1(0);
////                        updateDate(fetalMovementListBakeBean,true);
////                        quickeningView.updataData(fetalMovementListBakeBean,time);
////                        currentTime = time;
//            mBinding.temple2CurrentTime.setText(currentTime);
////                        updateLinePoint(TypeApi.TIME_IVTERVAL_AM);
//        } else {
//            ToastUtils.getInstance().makeText(mActivity,"向左滑动到底了 ");
//        }

//        if(temple2ViewMap.containsKey(time1)){
//            TempleListForWeekBackModel bakeBean =  temple2ViewList.get(temple2ViewMap.get(time1));
//            if(bakeBean != null){
//                TempleListForWeekBackModel.DataDateBean dataDateBean = bakeBean.getData_Date();
//                if(dataDateBean != null){
//                    String time = (String) dataDateBean.getDownRecordDate();
//
//                    if(!TextUtils.isEmpty(time) && temple2ViewMap.containsKey(time)){
//                        ToastUtils.getInstance().makeText(mActivity,"向左滑动 : " + time);
//                        currentTime = time;
//                        doGetListForWeek(true,currentTime);
////                        doGetListForWeek(currentTime,true);
////                        int position = temple2ViewMap.get(time);
////                        BloodPressureListWeekBackModel fetalMovementListBakeBean = temple2ViewList.get(position).getValue1(0);
////                        updateDate(fetalMovementListBakeBean,true);
////                        quickeningView.updataData(fetalMovementListBakeBean,time);
////                        currentTime = time;
//                        mBinding.temple2CurrentTime.setText(currentTime);
////                        updateLinePoint(TypeApi.TIME_IVTERVAL_AM);
//                    } else {
//                        ToastUtils.getInstance().makeText(mActivity,"向左滑动到底了 ");
//                    }
//                }
//            }
//        }
    }

    //向右滑动 上一个日期 UpFetalMovementDate
    private void doScrollRight(String time1){

        time--;
        currentTime = TimeUtils.getYMDData(time);
        mBinding.temple2CurrentTime.setText(currentTime);
        doGetListForWeek(currentTime);
//        if(temple2ViewMap.containsKey(time1)) {
//            TempleListForWeekBackModel bakeBean = temple2ViewList.get(temple2ViewMap.get(time1));
//            if (bakeBean != null) {
//                TempleListForWeekBackModel.DataDateBean dataDateBean = bakeBean.getData_Date();
//                if (dataDateBean != null) {
//                    String time = (String) dataDateBean.getUpRecordDate();
//                    if(temple2ViewMap.containsKey(time)){
//                        if(TimeUtils.getCountData(currentTime) != 0){
//                            time = TimeUtils.getYMDData(0);
//                        }
//                        if(!TextUtils.isEmpty(time)){
//                            ToastUtils.getInstance().makeText(mActivity,"向右滑动 : " + time);
//                            currentTime = time;
//                            doGetListForWeek(true,currentTime);
////                            int position = temple2ViewMap.get(time);
////                            BloodPressureListWeekBackModel fetalMovementListBakeBean = temple2ViewList.get(position).getValue1(0);
////                            updateDate(fetalMovementListBakeBean, true);
////                            quickeningView.updataData(fetalMovementListBakeBean, time);
////                            currentTime = time;
//                            mBinding.temple2CurrentTime.setText(currentTime);
////                            updateLinePoint(TypeApi.TIME_IVTERVAL_AM);
//
//                        }else {
//                            ToastUtils.getInstance().makeText(mActivity,"向右滑动到底了 ");
//                        }
//
//                    } else {
//                        ToastUtils.getInstance().makeText(mActivity,"向右滑动到底了 ");
//                    }
//                }
//            }
//        }
    }

    public void renderAllData(List<TempleListModel> model) {
        mBinding.temple2View.updataData(model,currentTime);
        double value = 0.0;
        if(model != null){
            for(TempleListModel m : model){
                if(currentTime.equals(m.getCreateTime())){
                    value = m.getTemperatureValue();
                }
            }
            mBinding.temple2Wd.setText(value + "");
            mBinding.temple2Pg.setVisibility(value > 37.0?VISIBLE:GONE);
        }
    }
    public void doGetListForWeek(String time){
        mActivity.requestGetAll(time, TimeUtils.getYMDData(TimeUtils.getCountData(time)+7));
//
//        if(times != null && times.length > 0){
//            final List<Observable<BaseBackBean>> list = new ArrayList<>();
//            for(String time : times){
//                if(!TextUtils.isEmpty(time) && time.contains("-")){
//                    if(!temple2ViewMap.containsKey(time) || isRequest) {
//                        if(isRequest)
//                            currentTime = time;
//                        list.add(TempleNetworkRequest.doGetListForWeek(
//                                mActivity,
//                                mActivity.mHosptialBean.getValue1(0),
//                                mActivity.mUserBean.getCustomerNo(), time));
//                    }else {
//                        TempleListForWeekBackModel listWeekBackBean = temple2ViewList.get(temple2ViewMap.get(time));
//                        doUpdateDate(listWeekBackBean,isRequest);
//                    }
//                }
//            }
//
//            Observable.merge(list)
//                    .subscribe(new MySubscriber<TempleListForWeekBackModel>(TempleListForWeekBackModel.class){
//                        @Override
//                        public void onBackNext(TempleListForWeekBackModel listWeekBackBean) {
//                            super.onBackNext(listWeekBackBean);
//                            if(listWeekBackBean != null && listWeekBackBean.getData_Info() != null){
//                                String tm = TimeUtils.getTime(listWeekBackBean.getData_Info().getRecordDate(),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd");
//                                temple2ViewList.add(listWeekBackBean);
//                                if(isRequest)
//                                    temple2ViewMap.put(currentTime,temple2ViewList.size()-1);
//                                else
//                                    temple2ViewMap.put(tm,temple2ViewList.size()-1);
//                            }
//                            doUpdateDate(listWeekBackBean,isRequest);
//                        }
//
//                        @Override
//                        public void onBackError(String message) {
//                            super.onBackError(message);
//                        }
//                    });
//        }
    }

    public void doClearData(){
//        mTvSsy.setText("--");
//        mTvSzy.setText("--");
        mBinding.temple2Wd.setText("--");
        mBinding.temple2View.updataData(null,null);
    }

    /**
     * 清空数据
     */
    public void doClear(){
        temple2ViewList.clear();
        temple2ViewMap.clear();
        doClearData();
    }

    private void doUpdateDate(TempleListForWeekBackModel templeListWeekBackBean, final boolean isRequest){

        if(templeListWeekBackBean == null){
            doClearData();

            return;
        }

        TempleListForWeekBackModel.DataDateBean dataDateBean = templeListWeekBackBean.getData_Date();
        if(dataDateBean != null && isRequest){
//            nextTime =  dataDateBean.getDownRecordDate();
//            lastTime = dataDateBean.getUpRecordDate();

//            doGetListForWeek(lastTime,nextTime);
        }
        if(isRequest){
            TempleListForWeekBackModel.DataInfoBean dataInfoBean = templeListWeekBackBean.getData_Info();
            if(dataInfoBean != null){
                mBinding.temple2Wd.setText(dataInfoBean.getTemperatureValue() + "");
                if(dataInfoBean.getTemperatureValue() > 37.0){
                    mBinding.temple2Pg.setVisibility(VISIBLE);
                }else {
                    mBinding.temple2Pg.setVisibility(GONE);
                }
            }else{
                doClearData();
            }
//            mBinding.temple2View.updataData(templeListWeekBackBean,currentTime);
        }
    }
}
