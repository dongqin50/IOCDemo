package com.conagra.hardware.adapter;


import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.conagra.R;
import com.conagra.hardware.model.JkgjListBackModel;
import com.conagra.mvp.constant.TypeApi;
import com.conagra.mvp.ui.adapter.BaseRecycleViewAdapter;

import java.util.List;

/**
 * Created by yedongqin on 16/9/16.
 */
public class MonitorAdapter extends BaseRecycleViewAdapter<JkgjListBackModel.DataBean,ViewDataBinding> {

    private boolean isJkgj;

    public MonitorAdapter(RecyclerView recyclerView, List<JkgjListBackModel.DataBean> mDataList) {
        super(recyclerView, mDataList);
    }

    @Override
    public int setLayout(int viewType) {
        return 0;
    }

//    @Override
//    public void onBindViewHolderCustom(BaseRecycleViewAdapter.ViewHolder mHolder, final int position) {
//
//        if(mHolder instanceof ViewHolderButton){
//            ViewHolderButton viewHolderButton = (ViewHolderButton) mHolder;
//            ActivityUtils.doClick(viewHolderButton.mBt)
//                    .subscribe(new Consumer() {
//                        @Override
//                        public void accept(Object o) throws Exception{
//                            doClick(null,position);
//                        }
//                    });
//            return;
//        }
//        if(!(mHolder instanceof ViewHolder) || position == getCount()){
//            return;
//        }
//        ViewHolder holder = (ViewHolder) mHolder;
//
//        final JkgjListBackModel.DataBean bean = dataList.get(position);
//        if(holder != null && bean != null){
//            getCodeContent(bean);
//            if(!TextUtils.isEmpty(bean.getTitle()))
//                holder.mTitle.setText(bean.getTitle());
//            if(bean.getIconResId() > 0){
////                holder.mIv.setImageDrawable(context.getResources().getDrawable(bean.getIconResId()));
//                Glide.with(context)
//                        .fromResource()
//                        .load(bean.getIconResId())
//                        .into(holder.mIv);
//            }
//
//            if(!TextUtils.isEmpty(bean.getContent()))
//                holder.mContent.setText(bean.getContent());
//            holder.mStatus.setText(bean.getLastValue());
////                holder.mTitle.setText(bean.getTitle());
//            if(!TextUtils.isEmpty(bean.getLastRecordDate())) {
//                    holder.mContent.setText("最近监测时间：" + bean.getLastRecordDate());
//            } else {
//                holder.mContent.setText("暂无数据");
//            }
//            ActivityUtils.doClick(holder.itemView)
//                    .subscribe(new Consumer() {
//                        @Override
//                        public void accept(Object o) throws Exception{
//                            if(mOnRefreshAndClickListener != null){
//                                mOnRefreshAndClickListener.onClickListener(bean,position);
//                            }
//                        }
//                    });
//
//        }
//    }
//
//    @Override
//    public int getViewType(int position) {
//
//        if(position == getCount() - 1 && isJkgj){
//            return 1;
//        }
//        return 0;
//    }
//
//    @Override
//    public int getCount() {
//        return  isJkgj?super.getCount() + 1:super.getCount();
//    }
//
//
//
//    @Override
//    public View onCreateContentView(ViewGroup parent, int viewType) {
//        if(viewType == 1){
//            return ActivityUtils.getLayoutInflater(context, R.layout.button_jkgj,parent);
//        }
//
//        return ActivityUtils.getLayoutInflater(context, R.layout.item_icon1,parent);
//
//    }
//
//
//    @Override
//    public BaseRecycleViewAdapter.ViewHolder onCreateViewHolderCustom(View realContentView, int viewType) {
//        if(viewType == 1){
//            return new ViewHolderButton(realContentView);
//        }
//        return new ViewHolder(realContentView);
//    }
//
//    public   class ViewHolder extends BaseRecycleViewAdapter.ViewHolder{
////        @BindView(R.id.icon1_title)
//        public TextView mTitle;
////        @BindView(R.id.icon1_content)
//        public TextView mContent;
////        @BindView(R.id.icon1_status)
//        public TextView mStatus;
////        @BindView(R.id.icon1_iv)
//        public ImageView mIv;
////        @BindView(R.id.icon1_selected)
//        AutoRelativeLayout mSelectedRl;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
////            ButterKnife.bind(this,itemView);
//        }
//    }
//    public   class ViewHolderButton extends BaseRecycleViewAdapter.ViewHolder{
////        @BindView(R.id.monitor_bt)
//        Button mBt;
//        public ViewHolderButton(View itemView) {
//            super(itemView);
////            mBt = itemView.findViewById(R.id.monitor_bt);
//
////            ButterKnife.bind(this,itemView);
//        }
//    }

    public void getCodeContent(JkgjListBackModel.DataBean monitorBean){
        if(monitorBean == null || TextUtils.isEmpty(monitorBean.getHardwareCode()))
            return;
        switch (monitorBean.getHardwareCode().toUpperCase()){
            case  TypeApi.HARDWARE_FETALHEART:// "胎心";// FetalHeart（胎心仪）
                monitorBean.setIconResId(R.drawable.jkgj_jkgj_tx);
                monitorBean.setTitle(TypeApi.HARDWARE_STRING_FETALHEART);
                break;
            case  TypeApi.HARDWARE_FETALMOVEMENT: // "胎动";// FetalMovement（胎动）

                monitorBean.setIconResId(R.drawable.jkgj_jkgj_td);

                monitorBean.setTitle(TypeApi.HARDWARE_STRING_FETALMOVEMENT); // "胎动";// FetalMovement（胎动）
                break;
            case  TypeApi.HARDWARE_BLOODPRESSURE:// "血压";//BloodPressure（血压仪）
                monitorBean.setIconResId(R.drawable.jkgj_jkgj_xy2);
                monitorBean.setTitle( TypeApi.HARDWARE_STRING_BLOODPRESSURE);// "血压";//BloodPressure（血压仪）
                break;
            case  TypeApi.HARDWARE_BLOODSUGAR:   // "血糖";//BloodSugar（血糖仪器）
                monitorBean.setIconResId(R.drawable.jkgj_jkgj_xt);
                monitorBean.setTitle( TypeApi.HARDWARE_STRING_BLOODSUGAR);   // "血糖";//BloodSugar（血糖仪器）
                break;
            case  TypeApi.HARDWARE_BLOODFAT: // "血脂";//BloodFat（血脂）
                monitorBean.setIconResId(R.drawable.jkgj_jkgj_xz);
                monitorBean.setTitle( TypeApi.HARDWARE_STRING_BLOODFAT); // "血脂";//BloodFat（血脂）
                break;
            case  TypeApi.HARDWARE_ELECTROCARDIOGRAM:   // "心电";//Electrocardiogram（心电图）
                monitorBean.setIconResId(R.drawable.jkgj_jkgj_xd);
                monitorBean.setTitle( TypeApi.HARDWARE_STRING_ELECTROCARDIOGRAM);   // "心电";//Electrocardiogram（心电图）
                break;
            case  TypeApi.HARDWARE_TEMPERATURE:         // "体温";//Temperature（体温）
                monitorBean.setIconResId(R.drawable.jkgj_jkgj_tw);
                monitorBean.setTitle(TypeApi.HARDWARE_STRING_TEMPERATURE);         // "体温";//Temperature（体温）
                break;
            case  TypeApi.HARDWARE_URINE:       // "尿液";//Urine（尿液）
                monitorBean.setIconResId(R.drawable.jkgj_jkgj_ny);
                monitorBean.setTitle(TypeApi.HARDWARE_STRING_URINE);       // "尿液";//Urine（尿液）
                break;
            case  TypeApi.HARDWARE_OXYGEN:      // "血氧";//Oxygen（血氧）
                monitorBean.setIconResId(R.drawable.jkgj_jkgj_xy);
                monitorBean.setTitle( TypeApi.HARDWARE_STRING_OXYGEN);      // "血氧";//Oxygen（血氧）
                break;
            case  TypeApi.HARDWARE_HEARTRATE:       // "心率";//HeartRate（心率)
                monitorBean.setIconResId(R.drawable.jkgj_jkgj_xl);
                monitorBean.setTitle( TypeApi.HARDWARE_STRING_HEARTRATE);       // "心率";//HeartRate（心率)
                break;
            case  TypeApi.HARDWARE_WEIGHT:      // "体重";//Weight（体重）
                monitorBean.setIconResId(R.drawable.jkgj_jkgj_tw);
                monitorBean.setTitle(TypeApi.HARDWARE_STRING_WEIGHT);      // "体重";//Weight（体重）
                break;

        }
    }
}
