package com.conagra.hardware.adapter;


import android.support.v7.widget.RecyclerView;

import com.conagra.R;
import com.conagra.databinding.ItemIcon11Binding;
import com.conagra.hardware.model.BluetoothModel;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.ui.adapter.BaseRecycleViewAdapter;

import java.util.List;

//import butterknife.ButterKnife;

/**
 * Created by yedongqin on 16/9/21.
 */
public class BluetoothCustomAdapter extends BaseRecycleViewAdapter<BluetoothModel,ItemIcon11Binding> {

    private static final String TAG = "BluetoothCustomAdapter";

//    private List<BluetoothModel> mList;
    private BaseListener.OnItemSelectedListener<BluetoothModel> mListener;

    public BluetoothCustomAdapter(RecyclerView recyclerView, List<BluetoothModel> mDataList, BaseListener.OnItemSelectedListener<BluetoothModel> listener) {
        super(recyclerView, mDataList);
        this.mListener = listener;
    }

    @Override
    public int setLayout(int viewType) {
        return R.layout.item_icon11;
    }


    @Override
    public void onBindViewHolder(ItemIcon11Binding holder, final BluetoothModel bean, final int position) {
        super.onBindViewHolder(holder, bean, position);
        if(holder != null && bean != null) {
            holder.icon11Tv.setText(bean.getDeviceName());
            holder.icon11TvCode.setText(bean.getDeviceAddress());
            if (bean.isConnected())
                holder.icon11Iv.setImageResource(R.drawable.blutooth_search);
            else
                holder.icon11Iv.setImageResource(R.drawable.bluetooth_icon);
            holder.getRoot().setOnClickListener((v -> {
                updateSelectedStatus(position);
                if (null != mListener) {
                    mListener.onItemSelectedListener(bean);
                }
            }));
        }

    }
//
//
//
//    public void addItem(BluetoothModel bean){
//        if(bean == null || mList == null)
//            return;
//        synchronized (BluetoothCustomAdapter.this){
//            if(bean != null && mList != null){
//                mList.add(bean);
//            }
//        }
//
//        notifyDataSetChanged();
//    }

    /**
     * 更新选择数据的状态
     */
    public void updateSelectedStatus(int  position){
        if(mDataList.size() > 0 && position < mDataList.size()){
                if(mDataList.size() > 0 && position < mDataList.size()){
                    for(int i = 0; i < mDataList.size(); i++){
                        BluetoothModel key = mDataList.get(i);
                        if(i == position){
                            if(!key.isConnected()){
                                key.setConnected(true);
                            }
                        }else {
                            key.setConnected(false);
                        }

                    }
            }
        }
        notifyDataSetChanged();
    }
}
