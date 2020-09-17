package com.conagra.hardware.adapter;


import android.support.v7.widget.RecyclerView;

import com.conagra.R;
import com.conagra.databinding.ItemHeartrecordBinding;
import com.conagra.mvp.model.HeartListRowBean;
import com.conagra.mvp.ui.adapter.BaseRecycleViewAdapter;
import com.conagra.mvp.utils.TimeUtils;

import java.util.List;

/**
 * Created by yedongqin on 16/9/28.
 *
 * 胎心纪录
 */
public class HeartRecordAdapter extends BaseRecycleViewAdapter<HeartListRowBean,ItemHeartrecordBinding> {

    public HeartRecordAdapter(RecyclerView recyclerView, List<HeartListRowBean> mDataList) {
        super(recyclerView, mDataList);
    }

    @Override
    public int setLayout(int viewType) {
        return R.layout.item_heartrecord;
    }

    @Override
    public void onBindViewHolder(ItemHeartrecordBinding holder, HeartListRowBean bean, int position) {
        super.onBindViewHolder(holder, bean, position);

        holder.heartrecordBpm.setText(bean.getAvgHeartRate() + "");
        int day = TimeUtils.intervalDay(TimeUtils.getYMDData(0),bean.getCreateTime());
        holder.heartrecordDay.setText("+"+ day + "天");
        holder.heartrecordWeek.setText((day/7) + "周");
        holder.heartrecordStartTime.setText(TimeUtils.update(bean.getStartTime()));
        holder.heartrecordTime.setText(bean.getStartTime());
        if(position == 0){
            holder.heartrecordItemLl.setBackgroundResource(R.drawable.rec_green_corners12);
            holder.heartrecordStartTime.setTextColor(mContext.getResources().getColor(R.color.white));
        }else{
            holder.heartrecordItemLl.setBackgroundResource(R.drawable.rec_white_corner12);
        }

        holder.getRoot().setOnClickListener((v -> {
            if(mOnClickListener != null){
                mOnClickListener.onClick(position,bean);
            }
        }));
    }
}
