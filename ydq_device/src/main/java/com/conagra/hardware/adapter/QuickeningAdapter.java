package com.conagra.hardware.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.conagra.R;
import com.conagra.databinding.ItemQuickeningBinding;
import com.conagra.mvp.ui.adapter.BaseRecycleViewAdapter;

import java.util.List;


public class QuickeningAdapter extends BaseRecycleViewAdapter<View,ItemQuickeningBinding> {


    public QuickeningAdapter(RecyclerView recyclerView, List<View> mDataList) {
        super(recyclerView, mDataList);
    }

    @Override
    public void onBindViewHolder(ItemQuickeningBinding holder, View data, int position) {
        super.onBindViewHolder(holder, data, position);
        holder.itemQuickeningLl.addView(data);
    }

    @Override
    public int setLayout(int viewType) {
        return R.layout.item_quickening;
    }

}
