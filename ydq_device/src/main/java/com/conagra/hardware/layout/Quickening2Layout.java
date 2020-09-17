package com.conagra.hardware.layout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.conagra.R;
import com.conagra.databinding.FragmentQuickening2Binding;
import com.conagra.hardware.activity.FetalMoveActivity;
import com.conagra.hardware.adapter.Quickening2Adapter;
import com.conagra.hardware.model.FetalMovementIntervalBackModel;
import com.conagra.hardware.model.FetalMovementListBakeModel;
import com.conagra.hardware.widget.QuickeningView;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.model.DateModelBean;
import com.conagra.mvp.model.FetalMoveListsModel;
import com.conagra.mvp.model.FetalMoveListsRowBean;
import com.conagra.mvp.model.RecordTimePointModelBean;
import com.conagra.mvp.utils.ActivityUtils;
import com.conagra.mvp.utils.TimeUtils;
import com.xiaoye.myutils.widget.CustomDatePicker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.conagra.mvp.constant.TypeApi.TIME_IVTERVAL_AM;


/**
 * Created by yedongqin on 16/10/8.
 */
public class Quickening2Layout extends FrameLayout {


    private View view;


//    RecyclerView mRecyclerView;
    private Map<String, FetalMovementIntervalBackModel> pointMap;
    private Map<String,FetalMovementListBakeModel>  formMap;
    private String currentTime;
    private Quickening2Adapter mAdapter;
    private String customerNo;
//    private FetalMovementListBakeModel bakeBean;
    private FetalMoveActivity mActivity;
//    ViewPager viewPager;
    private long time;
    private CustomDatePicker customDatePicker;

    public QuickeningView getQuickeningView() {
        return mBinding.quickening2View;
    }

    public Quickening2Layout(Context context, String currentTime) {
        this(context,null,currentTime);
    }

    public Quickening2Layout(Context context, AttributeSet attrs,String currentTime) {
        this(context, attrs,0,currentTime);
    }

    public Quickening2Layout(Context context, AttributeSet attrs, int defStyleAttr, String currentTime) {
        super(context, attrs, defStyleAttr);
        this.currentTime = currentTime;
        time = TimeUtils.getCountData(currentTime);
        initDatePicker();
        doInit(context);
//        doWork();
//        updateQuickeningView(currentTime);
    }

//    public void setFetalMovementListBakeBean(FetalMovementListBakeModel bakeBean){
//        this.bakeBean = bakeBean;
//    }
    private FragmentQuickening2Binding mBinding;

    private void initDatePicker() {
        customDatePicker = new CustomDatePicker(getContext(), new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String t) { // 回调接口，获得选中的时间
//                currentDate.setText(time.split(" ")[0]);
                currentTime = t.split(" ")[0];
                time = TimeUtils.getCountData(currentTime);
                mBinding.quickening2CurrentTime.setText(currentTime);
                updateQuickeningView();
            }
        }); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动

    }


    public void doInit(final Context context) {
        mActivity = (FetalMoveActivity) context;
//        customerNo = mActivity.mUserBean.getCustomerNo();
        view = ActivityUtils.getLayoutInflater(context, R.layout.fragment_quickening2,null);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.fragment_quickening2,this,true);
        pointMap = new HashMap<>();
        formMap = new HashMap<>();
//
//        quickeningViewMap.put(currentTime,quickeningViewList.size()- 1);
        mBinding.quickening2CurrentTime.setText(currentTime);
        mBinding.quickening2View.setObservable1(new BaseListener.Observable1<String,String>() {
            //左滑动
            @Override
            public void onAction(String time1) {
                doScrollLeft(time1);

            }
            //右滑动
            @Override
            public void onAction1(String time1) {
                doScrollRight(currentTime);
            }
        });
        mBinding.quickening2Left.setOnClickListener((v -> {
            doScrollRight(currentTime);
        }));
        mBinding.quickening2Right.setOnClickListener((v -> {
            doScrollLeft(currentTime);
        }));
        mBinding.quickening2TimeRl.setOnClickListener((v -> {
            customDatePicker.show(currentTime);
            if(context instanceof FragmentActivity){
//                FragmentActivity fragmentActivity = (FragmentActivity) context;
//                new SlideDateTimePicker.Builder(fragmentActivity.getSupportFragmentManager())
//                        .setListener(new SlideDateTimeListener() {
//                            @Override
//                            public void onDateTimeSet(Date date) {
//                                currentTime = TimeUtils.doChangeFormat(date,"yyyy-MM-dd");
//                                time = TimeUtils.getCountData(currentTime);
//                                mBinding.quickening2CurrentTime.setText(currentTime);
//                                updateQuickeningView();
////                                mActivity.requestGetALLList(currentTime,currentTime);
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
        mBinding.quickening2Rl1.setOnClickListener((v -> {
//            if(pointMap.containsKey(currentTime)){
//                FetalMovementIntervalBackModel backBean = pointMap.get(currentTime);
//                updateLinePoint(backBean, TIME_IVTERVAL_AM);
//            }
        }));
        mBinding.quickening2Rl2.setOnClickListener((v -> {
//            if(pointMap.containsKey(currentTime)){
//                FetalMovementIntervalBackModel backBean = pointMap.get(currentTime);
//                updateLinePoint(backBean, TypeApi.TIME_IVTERVAL_PM);
//            }

        }));
        mBinding.quickening2Rl3.setOnClickListener((v -> {
//            if(pointMap.containsKey(currentTime)){
//                FetalMovementIntervalBackModel backBean = pointMap.get(currentTime);
//                updateLinePoint(backBean, TypeApi.TIME_IVTERVAL_NIGHT);
//            }
        }));
        updateQuickeningView();
    }

    public void renderAllData(FetalMoveListsModel model) {

        if(model != null&&model.getRows().size() > 0){
            FetalMoveListsRowBean bean = model.getRows().get(0);
            if(bean == null)
                return;
            List<DateModelBean> dateModelBean = bean.getDateModel();
            List<RecordTimePointModelBean> recordTimePointModelBeans = bean.getRecordTimePointModel();

            mBinding.quickening2Time.setText(bean.getSUMClickNumber() + "");
            mBinding.quickening2View.updataData(model,currentTime);

            if(recordTimePointModelBeans != null&&recordTimePointModelBeans.size()>0){
                int t = 0;
                mBinding.quickening2LinePoint.updateValue(recordTimePointModelBeans.get(t).getStartTime(),recordTimePointModelBeans.get(t).getRecordTimePoint());
            }
        }
    }

    public void doHideBar(){
        mBinding.quickening2BtDown.setVisibility(GONE);
    }

    //向左滑动 下一个日期 DownFetalMovementDate
    private void doScrollLeft(String time1){
        time++;
        currentTime = TimeUtils.getYMDData(time);
        mBinding.quickening2CurrentTime.setText(currentTime);
        updateQuickeningView();
//        mActivity.requestGetALLList(currentTime,currentTime);
//        if(formMap.containsKey(time1)){
//            FetalMovementListBakeModel bakeBean =  formMap.get(time1);
//            if(bakeBean != null){
//                FetalMovementListBakeModel.DataDateBean dataDateBean = bakeBean.getData_Date();
//                if(dataDateBean != null){
//                    String time = dataDateBean.getDownFetalMovementDate();
//
//                    if(!TextUtils.isEmpty(time) && formMap.containsKey(time)){
////                        currentTime = time;
//                        currentTime = TimeUtils.getDate(TimeUtils.getCountData(currentTime) - 7 );
//                        mBinding.quickening2CurrentTime.setText(currentTime);
////                        updateQuickeningView(true,currentTime);
//
//                        mActivity.requestGetALLList(currentTime,currentTime);
////                        currentTime = time;
//
//                    } else {
////                        ToastUtils.getInstance().makeText(mActivity,"向左滑动到底了 ");
//                    }
//                }
//            }
//        }
    }

    //向右滑动 上一个日期 UpFetalMovementDate
    private void doScrollRight(String time1){
        time--;
        currentTime = TimeUtils.getYMDData(time);
        mBinding.quickening2CurrentTime.setText(currentTime);
        updateQuickeningView();
//        if(formMap.containsKey(time1)) {
//            time1 = TimeUtils.getDate(TimeUtils.getCountData(currentTime) - 7 );
//            FetalMovementListBakeModel bakeBean = formMap.get(time1);
//            if (bakeBean != null) {
//                FetalMovementListBakeModel.DataDateBean dataDateBean = bakeBean.getData_Date();
//                if (dataDateBean != null) {
//                    String time = dataDateBean.getUpFetalMovementDate();
//                    if(!TextUtils.isEmpty(time) && formMap.containsKey(time)){
//                        currentTime = time;
//                        updateQuickeningView(true,currentTime);
//                        mBinding.quickening2CurrentTime.setText(currentTime);
//
//                    } else {
////                        ToastUtils.getInstance().makeText(mActivity,"向右滑动到底了 ");
//                    }
//                }
//            }
//        }
    }

//    public void doWork(final Context context) {
//        updateQuickeningView(currentTime);
////        updateLinePoint(TypeApi.TIME_IVTERVAL_AM);
//    }

    public  void doClear(){
        formMap.clear();
        pointMap.clear();
        updateLinePoint(null,TIME_IVTERVAL_AM);
        mBinding.quickening2SwTv2.setText("--");
        mBinding.quickening2XwTv2.setText("--");
        mBinding.quickening2WsTv2.setText("--");
    }

    public void setCurrentTime(String times) {
        currentTime = times;
        time = TimeUtils.getCountData(currentTime);
    }

    public void updateQuickeningView(){
        mActivity.requestGetALLList(currentTime, TimeUtils.getYMDData(time+7));
//        if(times != null && times.length > 0){
//            final List<Observable<BaseBackBean>> list = new ArrayList<>();
//            for(String time : times){
//                if(!TextUtils.isEmpty(time) && time.contains("-")){
//                    if(!formMap.containsKey(time) || isRequest) {
//                        if(isRequest)
//                            currentTime = time;
//                        list.add(HardwareNetWorkRequest.getFetalMoveMentListForWeek(mActivity,
//                                mActivity.mHosptialBean.getValue1(0),
//                                mActivity.mUserBean.getCustomerNo(), time));
//                    }else {
//                        if(formMap.containsKey(time)){
//                            FetalMovementListBakeModel fetalMovementListBakeModel = formMap.get(time);
//                            if(fetalMovementListBakeModel != null){
//                                updateDate(fetalMovementListBakeModel,isRequest);
//                                quickeningView.updataData(fetalMovementListBakeModel,time);
//                            }
//                        }
//                    }
//
//                    if(!pointMap.containsKey(time) || isRequest) {
//                        if(isRequest)
//                            currentTime = time;
//                        list.add(HardwareNetWorkRequest.getFetalMoveMentIntervalByRang(mActivity,
//                                mActivity.mHosptialBean.getValue1(0),
//                                mActivity.mUserBean.getCustomerNo(),time));
//                    }else {
//                        if(pointMap.containsKey(time)){
//                            FetalMovementIntervalBackModel movementIntervalBackModel = pointMap.get(time);
//                            if(movementIntervalBackModel != null){
//                                updateLinePoint(movementIntervalBackModel, TIME_IVTERVAL_AM);
//                            }
//                        }
//                    }
//                }
//            }
//
//            Observable.merge(list)
//                    .subscribe(new MySubscriber<String>(String.class){
//                        @Override
//                        public void onBackNext(String obj) {
//                            super.onBackNext(obj);
////
//                            if(!TextUtils.isEmpty(obj)){
//                                if(!obj.contains("data_Night")){
//                                    final FetalMovementListBakeModel fetalMovementListBakeBean = new Gson().fromJson(obj,FetalMovementListBakeModel.class);
//                                    if(fetalMovementListBakeBean != null && fetalMovementListBakeBean.getData_Info() != null){
//                                        String tm = TimeUtils.getTime(fetalMovementListBakeBean.getData_Info().getFetalMovementDate(),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd");
//                                        if(isRequest){
//
//                                            formMap.put(currentTime,fetalMovementListBakeBean);
//                                            updateDate(fetalMovementListBakeBean,isRequest);
//                                            quickeningView.updataData(fetalMovementListBakeBean,currentTime);
//                                        }else {
//                                            formMap.put(tm,fetalMovementListBakeBean);
//                                        }
//                                    }
//                                }else {
//                                    FetalMovementIntervalBackModel fetalMovementIntervalBackBean = new GsonBuilder().create().fromJson(obj, FetalMovementIntervalBackModel.class);
//                                    if(fetalMovementIntervalBackBean != null && fetalMovementIntervalBackBean.getData_CurrentDate() != null){
//                                        String tm = fetalMovementIntervalBackBean.getData_CurrentDate().getData_CurrentDate();
//                                        if(isRequest){
//                                            pointMap.put(currentTime,fetalMovementIntervalBackBean);
//                                            updateLinePoint(fetalMovementIntervalBackBean, TIME_IVTERVAL_AM);
//                                        }else {
//                                            pointMap.put(tm,fetalMovementIntervalBackBean);
//                                        }
//                                    }
//
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onBackError(String message) {
//                            super.onBackError(message);
//                        }
//                    });
//        }
    }

    /**
     * 更新界面上的数据
     * @param fetalMovementListBakeBean
     * @param isRequest
     */
    public void updateDate(FetalMovementListBakeModel fetalMovementListBakeBean, final Boolean isRequest) {

        if (fetalMovementListBakeBean != null) {

//            List<FetalMovementListBakeModel.DataWeekBean> list = fetalMovementListBakeBean.getData_Week();
//            if (list != null) {

//                FetalMovementListBakeModel.DataWeekBean recentlyFeltal = list.get(0);
//                for(FetalMovementListBakeModel.DataWeekBean fm : list){
//                    int result =  TimeUtils.compartorTime(fm.getFetalMovementDateTime(), recentlyFeltal.getFetalMovementDateTime(), "yyyy-MM-dd");
//                    if(result > 1){
//                        recentlyFeltal = fm;
//                    }
//                }
            FetalMovementListBakeModel.DataInfoBean dataInfoBean = fetalMovementListBakeBean.getData_Info();
            if (dataInfoBean != null && isRequest) {
                mBinding.quickening2LinePoint.start(dataInfoBean.getStartTime());

//                    final String currentDate = dataInfoBean.getFetalMovementDateTime().split("T")[0];
                //设置状态
                mBinding.quickening2TvStatus.setText(dataInfoBean.getFetalMovementState());
                mBinding.quickening2CurrentTime.setText(currentTime);

                if (dataInfoBean.getMorning() == 0) {
                    mBinding.quickening2SwTv2.setText("--");
                } else {
                    mBinding.quickening2SwTv2.setText(dataInfoBean.getMorning() + "");
                }
                if (dataInfoBean.getAfternoon() == 0) {
                    mBinding.quickening2XwTv2.setText("--");
                } else {
                    mBinding.quickening2XwTv2.setText(dataInfoBean.getAfternoon() + "");
                }
                if (dataInfoBean.getMorning() == 0) {
                    mBinding.quickening2WsTv2.setText("--");
                } else {
                    mBinding.quickening2WsTv2.setText(dataInfoBean.getNight() + "");
                }
                mBinding.quickening2Time.setText(dataInfoBean.getTwelveHour() + "");
//                    updateLinePoint(TypeApi.TIME_IVTERVAL_AM);
            }

            FetalMovementListBakeModel.DataDateBean dataDateBean = fetalMovementListBakeBean.getData_Date();
            if (dataDateBean != null && isRequest) {
//                lastTime = dataDateBean.getUpFetalMovementDate();
//                nextTime = dataDateBean.getDownFetalMovementDate();
//                updateQuickeningView(false,lastTime,nextTime);
            }

        }
    }


    /**
     * 更新界面上点的显示
     * @param backBean
     * @param time
     */
    public void updateLinePoint(FetalMovementIntervalBackModel backBean, int time){

//        String startTime = null;
//        List<String> list = new ArrayList<>();
//        switch (time){
//            case TIME_IVTERVAL_AM:
//                mBinding.quickening2Line1.setVisibility(VISIBLE);
//                mBinding.quickening2Line2.setVisibility(INVISIBLE);
//                mBinding.quickening2Line3.setVisibility(INVISIBLE);
//
//                if(backBean == null){
//                    mBinding.quickening2LinePoint.updateValue(null,null);
//                    return;
//                }
//
//                List<FetalMovementIntervalBackModel.DataMorningBean> dataMorningBeanList = backBean.getData_Morning();
//                if(dataMorningBeanList != null){
//                    for(FetalMovementIntervalBackModel.DataMorningBean db : dataMorningBeanList){
//                        list.add(db.getFetalMovementTime());
//                    }
//                    if(dataMorningBeanList.size() > 0)
//                        startTime = dataMorningBeanList.get(0).getStartTime();
//                }
//                break;
//            case TypeApi.TIME_IVTERVAL_PM:
//                mBinding.quickening2Line2.setVisibility(VISIBLE);
//                mBinding.quickening2Line1.setVisibility(INVISIBLE);
//                mBinding.quickening2Line3.setVisibility(INVISIBLE);
//                if(backBean == null){
//                    mBinding.quickening2LinePoint.updateValue(null,null);
//                    return;
//                }
//
//                List<FetalMovementIntervalBackModel.DataAfternoonBean> dataAfternoonBeanList= backBean.getData_Afternoon();
//                if(dataAfternoonBeanList != null){
//                    for(FetalMovementIntervalBackModel.DataAfternoonBean db : dataAfternoonBeanList){
//                        list.add(db.getFetalMovementTime());
//                    }
//                    if(dataAfternoonBeanList.size() > 0)
//                        startTime = dataAfternoonBeanList.get(0).getStartTime();
//                }
//                break;
//            case TypeApi.TIME_IVTERVAL_NIGHT:
//                mBinding.quickening2Line3.setVisibility(VISIBLE);
//                mBinding.quickening2Line2.setVisibility(INVISIBLE);
//                mBinding.quickening2Line1.setVisibility(INVISIBLE);
//                if(backBean == null){
//                    mBinding.quickening2LinePoint.updateValue(null,null);
//                    return;
//                }
//
//                List<FetalMovementIntervalBackModel.DataNightBean> dataNightBeanList= backBean.getData_Night();
//                if(dataNightBeanList != null){
//                    for(FetalMovementIntervalBackModel.DataNightBean db : dataNightBeanList){
//                        list.add(db.getFetalMovementTime());
//                    }
//                    if(dataNightBeanList.size() > 0)
//                        startTime = dataNightBeanList.get(0).getStartTime();
//                }
//                break;
//        }
//        mBinding.quickening2LinePoint.updateValue(startTime,list);
    }


}
