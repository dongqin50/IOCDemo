package com.conagra.hardware.adapter;

import android.support.v7.widget.RecyclerView;

import com.conagra.R;
import com.conagra.databinding.ItemBloodsugarListBinding;
import com.conagra.mvp.model.BloodSugarListRowModel;
import com.conagra.mvp.ui.adapter.BaseRecycleViewAdapter;
import com.conagra.mvp.utils.TimeUtils;

import java.util.List;


/**
 * Created by yedongqin on 2016/11/3.
 */

public class BloodSugarListAdapter extends BaseRecycleViewAdapter<BloodSugarListRowModel,ItemBloodsugarListBinding> {


    public BloodSugarListAdapter(RecyclerView recyclerView, List<BloodSugarListRowModel> mDataList) {
        super(recyclerView, mDataList);
    }

    @Override
    public int setLayout(int viewType) {
        return R.layout.item_bloodsugar_list;
    }

    @Override
    public void onBindViewHolder(ItemBloodsugarListBinding holder, final BloodSugarListRowModel pageDataBean,final int position) {
        super.onBindViewHolder(holder, pageDataBean, position);
        if(pageDataBean != null && holder != null){

            holder.bloodsugarListTime.setText(TimeUtils.getTime(pageDataBean.getCreateTime(),"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd"));
//            holder.bloodsugarListTime.setText(pageDataBean.getCreateTime());
            holder.bloodsugarListState.setText(getStatus(pageDataBean.getTimeSlot()));
            holder.bloodsugarListStatus.setText(pageDataBean.getBloodSugarClass());
            holder.bloodsugarListXyNum.setText(pageDataBean.getBloodSugarValue() + "");
            holder.getRoot().setOnClickListener((v)->{
                if(mOnClickListener != null){
                    mOnClickListener.onClick(position,pageDataBean);
                }
            });
        }
    }

    private String getStatus(int state){
        switch (state){
            case 0:
                return "早餐前";
            case 1:
                return "早餐后";
            case 2:
                return "午餐前";
            case 3:
                return "午餐后";
            case 4:
                return "晚餐前";
            case 5:
                return "晚餐后";
            case 6:
                return "睡前";
        }
        return "";
    }

}
