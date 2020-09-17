package com.conagra.hardware.adapter;


import android.support.v7.widget.RecyclerView;

import com.conagra.R;
import com.conagra.databinding.ItemBloodpressureListBinding;
import com.conagra.mvp.model.BloodPressureRowBean;
import com.conagra.mvp.ui.adapter.BaseRecycleViewAdapter;

import java.util.List;

import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_EIGHT;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_ELEVEN;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_FIVE;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_FOUR;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_NINE;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_ONE;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_SERVEN;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_SIX;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_EIGHT;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_ELEVEN;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_FIVE;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_FOUR;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_NINE;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_ONE;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_SERVEN;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_SIX;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_TEN;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_THIRTEEN;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_THREE;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_TWELVE;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_TWO;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_STRING_ZERO;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_TEN;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_THIRTEEN;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_THREE;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_TWELVE;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_TWO;
import static com.conagra.mvp.constant.TypeApi.BLOODPRESSURE_STATE_ZERO;


/**
 * Created by yedongqin on 2016/11/1.
 */

public class BloodPressureListAdapter extends BaseRecycleViewAdapter<BloodPressureRowBean,ItemBloodpressureListBinding> {

    public BloodPressureListAdapter(RecyclerView recyclerView, List<BloodPressureRowBean> mDataList) {
        super(recyclerView, mDataList);
    }

    @Override
    public int setLayout(int viewType) {
        return R.layout.item_bloodpressure_list;
    }

    @Override
    public void onBindViewHolder(ItemBloodpressureListBinding holder, BloodPressureRowBean pageDataBean, int position) {
        super.onBindViewHolder(holder, pageDataBean, position);
        if(holder != null){
            holder.bloodpressureListStatus.setText(getYyStatus(pageDataBean.getState()));
            holder.bloodpressureListTime.setText(pageDataBean.getCreateTime());
            holder.bloodpressureListXlNum.setText(pageDataBean.getHeartRate()+"");
            holder.bloodpressureListXyNum.setText(pageDataBean.getDiastolicPressure() + "/" + pageDataBean.getSystolicPressure());
            holder.getRoot().setOnClickListener((v)->{
                if(mOnClickListener != null){
                    mOnClickListener.onClick(position,pageDataBean);
                }
            });
        }
    }



    public String getYyStatus(int state){
        String text = "";
        switch (state){
            case  BLOODPRESSURE_STATE_ZERO:
                text = BLOODPRESSURE_STATE_STRING_ZERO;
                break;
            case BLOODPRESSURE_STATE_ONE:
                text = BLOODPRESSURE_STATE_STRING_ONE;
                break;
            case BLOODPRESSURE_STATE_TWO:
                text = BLOODPRESSURE_STATE_STRING_TWO;
                break;
            case BLOODPRESSURE_STATE_THREE:
                text = BLOODPRESSURE_STATE_STRING_THREE;
                break;
            case BLOODPRESSURE_STATE_FOUR:
                text = BLOODPRESSURE_STATE_STRING_FOUR;
                break;
            case BLOODPRESSURE_STATE_FIVE:
                text = BLOODPRESSURE_STATE_STRING_FIVE;
                break;
            case BLOODPRESSURE_STATE_SIX:
                text = BLOODPRESSURE_STATE_STRING_SIX;
                break;
            case BLOODPRESSURE_STATE_SERVEN:
                text = BLOODPRESSURE_STATE_STRING_SERVEN;
                break;
            case BLOODPRESSURE_STATE_EIGHT:
                text = BLOODPRESSURE_STATE_STRING_EIGHT;
                break;
            case BLOODPRESSURE_STATE_NINE:
                text = BLOODPRESSURE_STATE_STRING_NINE;
                break;
            case BLOODPRESSURE_STATE_TEN:
                text = BLOODPRESSURE_STATE_STRING_TEN;
                break;
            case BLOODPRESSURE_STATE_ELEVEN:
                text = BLOODPRESSURE_STATE_STRING_ELEVEN;
                break;
            case BLOODPRESSURE_STATE_TWELVE:
                text = BLOODPRESSURE_STATE_STRING_TWELVE;
                break;
            case BLOODPRESSURE_STATE_THIRTEEN:
                text = BLOODPRESSURE_STATE_STRING_THIRTEEN;
                break;

        }
        return text;
    }
}
