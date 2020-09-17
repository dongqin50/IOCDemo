package com.conagra.hardware.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.conagra.R;
import com.conagra.databinding.ActivityJkgjBinding;
import com.conagra.hardware.adapter.JkgjAdapter;
import com.conagra.hardware.model.JkgjModel;
import com.conagra.mvp.constant.TypeApi;
import com.conagra.mvp.ui.activity.BaseAppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yedongqin on 16/9/15.
 *
 * 健康工具
 */
public class JkgjActivity extends BaseAppCompatActivity<ActivityJkgjBinding> {

    private JkgjAdapter mAdapter;
    private List<JkgjModel> mList;
    private Map<String,Integer> mMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_jkgj);
        
    }

    public void doInit() {
//        mHeader.setTitle("健康工具");
//        mHeader.setOtherText("");
//        mHeader.doBack()
//                .subscribe(new Action1() {
//                    @Override
//                    public void call(Object o) {
//                        finish();
//                    }
//                });
        mList = new ArrayList<>();
        mMap = new HashMap<>();
        mList.add(new JkgjModel(R.drawable.jkgj_jkgj_tx, TypeApi.HARDWARE_STRING_FETALHEART));
        mMap.put(TypeApi.HARDWARE_FETALHEART,mList.size()-1);
        mList.add(new JkgjModel(R.drawable.jkgj_jkgj_ny,TypeApi.HARDWARE_STRING_URINE));
        mMap.put(TypeApi.HARDWARE_URINE,mList.size()-1);
        mList.add(new JkgjModel(R.drawable.jkgj_jkgj_xd,TypeApi.HARDWARE_STRING_ELECTROCARDIOGRAM));
        mMap.put(TypeApi.HARDWARE_ELECTROCARDIOGRAM,mList.size()-1);
        mList.add(new JkgjModel(R.drawable.jkgj_jkgj_xz,TypeApi.HARDWARE_STRING_BLOODFAT));
        mMap.put(TypeApi.HARDWARE_BLOODFAT,mList.size()-1);
        mList.add(new JkgjModel(R.drawable.jkgj_jkgj_td,TypeApi.HARDWARE_STRING_FETALMOVEMENT));
        mMap.put(TypeApi.HARDWARE_FETALMOVEMENT,mList.size()-1);
        mList.add(new JkgjModel(R.drawable.jkgj_jkgj_xy,TypeApi.HARDWARE_STRING_OXYGEN));
        mMap.put(TypeApi.HARDWARE_OXYGEN,mList.size()-1);
        mList.add(new JkgjModel(R.drawable.jkgj_jkgj_xl,TypeApi.HARDWARE_STRING_HEARTRATE));
        mMap.put(TypeApi.HARDWARE_HEARTRATE,mList.size()-1);
        mList.add(new JkgjModel(R.drawable.jkgj_jkgj_tw,TypeApi.HARDWARE_STRING_TEMPERATURE));
        mMap.put(TypeApi.HARDWARE_TEMPERATURE,mList.size()-1);
        mList.add(new JkgjModel(R.drawable.jkgj_jkgj_tz,TypeApi.HARDWARE_STRING_WEIGHT));
        mMap.put(TypeApi.HARDWARE_WEIGHT,mList.size()-1);
        mList.add(new JkgjModel(R.drawable.jkgj_jkgj_xy2,TypeApi.HARDWARE_STRING_BLOODPRESSURE));
        mMap.put(TypeApi.HARDWARE_BLOODPRESSURE,mList.size()-1);
        mList.add(new JkgjModel(R.drawable.jkgj_jkgj_xt,TypeApi.HARDWARE_STRING_BLOODSUGAR));
        mMap.put(TypeApi.HARDWARE_BLOODSUGAR,mList.size()-1);

//        mAdapter = new JkgjAdapter(this, new BaseRecycleViewAdapter.OnRefreshAndClickListener<JkgjModel>() {
//            @Override
//            public void onUpPullRefreshListener() {
//                mBinding.jkgjRv.doHideView();
//            }
//
//            @Override
//            public void onDownPullRefreshListener() {
//                mBinding.jkgjRv.doHideView();
//            }
//
//            @Override
//            public void onClickListener(JkgjModel value, int position) {
//                if(!value.isAdd()){
//                    doDelete(value.getCode());
//                }else {
//                    doInsert(value.getCode());
//
//                }
//            }
//
//        });
//        mBinding.jkgjRv.setAdapter(mAdapter);
//        mAdapter.addAll(mList);
//        doGetList();
    }


    private void doInsert(String code){
//        JkgjNetworkRequest.doInsert(this,code,CacheUtils.getmUserModel().getCustomerNo(),new MySubscriber<String>(String.class){
//            @Override
//            public void onBackError(String message) {
//                super.onBackError(message);
//            }
//
//            @Override
//            public void onBackNext(String obj) {
//                super.onBackNext(obj);
//            }
//        });
    }

    private void doDelete(String code){
//        JkgjNetworkRequest.doDelete(this,code, CacheUtils.getmUserModel().getCustomerNo(),new MySubscriber<String>(String.class){
//            @Override
//            public void onBackError(String message) {
//                super.onBackError(message);
//            }
//
//            @Override
//            public void onBackNext(String obj) {
//                super.onBackNext(obj);
//
//            }
//        });
    }



    private void doGetList(){
//        JkgjNetworkRequest.doGetList(this, CacheUtils.getmUserModel().getCustomerNo(),new MySubscriber<JkgjListBackModel>(JkgjListBackModel.class){
//            @Override
//            public void onBackError(String message) {
//                super.onBackError(message);
//            }
//
//            @Override
//            public void onBackNext(JkgjListBackModel jkgjListBackModel) {
//                super.onBackNext(jkgjListBackModel);
//
//                if(jkgjListBackModel != null){
//
//                    for(JkgjListBackModel.DataBean backBean : jkgjListBackModel.getData()){
//                        if(backBean != null){
//                            if(backBean != null &&  !TextUtils.isEmpty(backBean.getHardwareCode()) && mMap.containsKey(backBean.getHardwareCode().toUpperCase())) {
//                                int position = mMap.get(backBean.getHardwareCode());
//                                if (position >= 0 && position < mList.size()) {
//                                    JkgjModel bean = mList.get(position);
//                                    if (bean != null) {
//                                        bean.setAdd(true);
//                                        mAdapter.updateData(position, bean);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        });
    }
}
