package com.conagra.hardware.adapter;

import android.support.v7.widget.RecyclerView;

import com.conagra.R;
import com.conagra.databinding.ItemTemplelistBinding;
import com.conagra.mvp.model.TempleListModel;
import com.conagra.mvp.ui.adapter.BaseRecycleViewAdapter;

import java.util.List;

/**
 * Created by yedongqin on 2016/12/3.
 */

public class TempleListAdapter extends BaseRecycleViewAdapter<TempleListModel,ItemTemplelistBinding> {

    public TempleListAdapter(RecyclerView recyclerView, List<TempleListModel> mDataList) {
        super(recyclerView, mDataList);
    }

    @Override
    public int setLayout(int viewType) {
        return R.layout.item_templelist;
    }

    @Override
    public void onBindViewHolder(ItemTemplelistBinding holder, TempleListModel dataBean, int position) {
        super.onBindViewHolder(holder, dataBean, position);
        if(dataBean != null){
            if(dataBean.getTemperatureValue()>=36.2&&dataBean.getTemperatureValue()<37.2){
                holder.templelistStatus.setText("正常");
                holder.templelistStatus.setTextColor(mContext.getResources().getColor(R.color.ks_text1));
                holder.templelistWd.setTextColor(mContext.getResources().getColor(R.color.ks_text1));
            }else if(dataBean.getTemperatureValue()>=37.2){
                holder.templelistStatus.setText("偏高");
                holder.templelistStatus.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.templelistWd.setTextColor(mContext.getResources().getColor(R.color.red));
            }else {
                holder.templelistStatus.setText("偏低");
                holder.templelistStatus.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.templelistWd.setTextColor(mContext.getResources().getColor(R.color.red));
            }

            holder.templelistWd.setText(dataBean.getTemperatureValue() + "℃");
            holder.templelistTime.setText(dataBean.getCreateTime());
            holder.getRoot().setOnClickListener((v)->{
                if(mOnClickListener != null){
                    mOnClickListener.onClick(position,dataBean);
                }
            });
        }
    }

}
