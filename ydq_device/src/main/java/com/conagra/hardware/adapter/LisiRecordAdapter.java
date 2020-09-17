package com.conagra.hardware.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.conagra.R;
import com.conagra.databinding.ItemLisiBinding;
import com.conagra.mvp.model.FetalMoveListRowBean;
import com.conagra.mvp.ui.adapter.BaseRecycleViewAdapter;

import java.util.List;


/**
 * Created by yedongqin on 2016/10/23.
 */

public class LisiRecordAdapter extends BaseRecycleViewAdapter<FetalMoveListRowBean,ItemLisiBinding> {

    public LisiRecordAdapter(RecyclerView recyclerView, List<FetalMoveListRowBean> mDataList) {
        super(recyclerView, mDataList);
    }

    @Override
    public int setLayout(int viewType) {
        return R.layout.item_lisi;
    }

    @Override
    public void onBindViewHolder(ItemLisiBinding mHold, FetalMoveListRowBean backBean, int position) {
        super.onBindViewHolder(mHold, backBean, position);


        if(backBean != null){
                mHold.lisiStatus.setText(backBean.getState());
                mHold.lisiNum.setText("胎动  " + (backBean.getRecordTimePoint() == null?"0":backBean.getRecordTimePoint().size()) + "次");
                mHold.lisiTime.setText(backBean.getCreateTime());
                mHold.lisiLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(position,backBean);
                    }

                }
            });

//                ActivityUtils.doClick(mHold.lisiLl)
//                        .subscribe(new Consumer() {
//                            @Override
//                            public void accept(Object o) throws Exception{
//                                if(mOnRefreshAndClickListener != null)
//                                    mOnRefreshAndClickListener.onClickListener(backBean,position);
//                            }
//                        });
            }
    }

}
