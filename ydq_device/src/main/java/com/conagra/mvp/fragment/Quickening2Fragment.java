package com.conagra.mvp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.conagra.R;
import com.conagra.hardware.widget.QuickeningView;
import com.conagra.mvp.utils.ActivityUtils;


/**
 * Created by yedongqin on 2016/10/21.
 */

public class Quickening2Fragment extends BaseFragment {
   
//    @BindView(R.id.item_quickening2_qv)
    QuickeningView quickeningView;
    @Override
    public View getCurrentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ActivityUtils.getLayoutInflater(mContext, R.layout.item_quickening2,container);
    }

    @Override
    public void doWork() {

//        Bundle bundle = getArguments();
//        if(bundle != null && bundle.containsKey("data")){
//
//            FetalMovementListBakeModel dataWeekBeanList = (FetalMovementListBakeModel) bundle.getSerializable("data");
//            if(dataWeekBeanList != null && null != dataWeekBeanList.getData_Week()) {
////
//                for (FetalMovementListBakeModel.DataWeekBean dataWeekBean : dataWeekBeanList.getData_Week()) {
//                    if (dataWeekBean != null) {
//                        LogMessage.doLogMessage("BaseActivity"," fragment : " + dataWeekBean.getFetalMovementDate());
//                        quickeningView.add12Value(dataWeekBean.getFetalMovementDate().split("T")[0], ((int)dataWeekBean.getTwelveHour()) + "");
//                        quickeningView.addSingleValue(dataWeekBean.getFetalMovementDate().split("T")[0], TypeApi.TIME_IVTERVAL_AM, ((int)dataWeekBean.getMorning()) + "");
//                        quickeningView.addSingleValue(dataWeekBean.getFetalMovementDate().split("T")[0], TypeApi.TIME_IVTERVAL_PM, ((int)dataWeekBean.getAfternoon()) + "");
//                        quickeningView.addSingleValue(dataWeekBean.getFetalMovementDate().split("T")[0], TypeApi.TIME_IVTERVAL_NIGHT, ((int)dataWeekBean.getNight()) + "");
//                    }
//                }
//            }
//        }
    }
}
