package com.conagra.hardware.adapter;


import android.support.v7.widget.RecyclerView;

import com.conagra.R;
import com.conagra.databinding.ItemIcon9Binding;
import com.conagra.hardware.model.JkgjModel;
import com.conagra.mvp.ui.adapter.BaseRecycleViewAdapter;

import java.util.List;

/**
 * Created by yedongqin on 16/9/15.
 */
public class JkgjAdapter extends BaseRecycleViewAdapter<JkgjModel,ItemIcon9Binding> {

    public JkgjAdapter(RecyclerView recyclerView, List<JkgjModel> mDataList) {
        super(recyclerView, mDataList);
    }

    @Override
    public int setLayout(int viewType) {
        return R.layout.item_icon9;
    }

    @Override
    public void onBindViewHolder(ItemIcon9Binding holder,final JkgjModel bean, int position) {
        super.onBindViewHolder(holder, bean, position);

        /**
         * //        @BindView(R.id.icon9_iv)
         ImageView mIv;
         //        @BindView(R.id.icon9_title)
         TextView mTitle;
         //        @BindView(R.id.icon9_add)
         Button mAdd;
         */

        if(holder != null && bean != null) {
            holder.icon9Iv.setImageDrawable(mContext.getResources().getDrawable(bean.getResId()));
            holder.icon9Title.setText(bean.getContent());
            if (bean.isAdd()) {
                holder.icon9Add.setBackgroundResource(R.drawable.black_corners12);
                if (bean.getType() == 0) {
                    holder.icon9Add.setText("已添加");
                } else {
                    holder.icon9Add.setText("已订阅");
                }
            } else {
                holder.icon9Add.setBackgroundResource(R.drawable.rec_green_corners12);
                if (bean.getType() == 0) {
                    holder.icon9Add.setText("添加");
                } else {
                    holder.icon9Add.setText("+订阅");
                }
            }

//            RxView.clicks(holder.icon9Add)
//                    .subscribe(new Consumer<Object>() {
//                        @Override
//                        public void accept(Object view) throws Exception {
//
//                            if (!bean.isAdd()) {
//                                bean.setAdd(true);
//                                holder.mAdd.setBackgroundResource(R.drawable.black_corners12);
//                                if (bean.getType() == 0) {
//                                    holder.mAdd.setText("已添加");
//                                } else {
//                                    holder.mAdd.setText("已订阅");
//                                }
//                            } else {
//                                bean.setAdd(false);
//                                holder.mAdd.setBackgroundResource(R.drawable.rec_green_corners12);
//                                if (bean.getType() == 0) {
//                                    holder.mAdd.setText("添加");
//                                } else {
//                                    holder.mAdd.setText("+订阅");
//                                }
//                            }
//                            if (mOnRefreshAndClickListener != null) {
//                                mOnRefreshAndClickListener.onClickListener(bean, position);
//                            }
//                        }
//                    });
        }
    }
}
