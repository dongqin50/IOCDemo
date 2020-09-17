package com.conagra.mvp.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.conagra.R;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.constant.TypeApi;
import com.conagra.mvp.model.MessageBean;
import com.conagra.mvp.utils.LogMessage;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.model.Conversation;

/**
 * Created by yedongqin on 2018/5/5.
 */

public class ConversationListAdapterEx extends ConversationListAdapter {

    private List<MessageBean> dataList;
    private Context mContent;
    private BaseListener.Observable<MessageBean> mListener;

    public ConversationListAdapterEx(Context context,
                                     Context mContent, List<MessageBean> dataList, BaseListener.Observable<MessageBean> mListener) {
        super(context);
        this.mContent = mContent;
        this.dataList = dataList;
        this.mListener = mListener;
    }

    public ConversationListAdapterEx(Context context) {
        super(context);
    }


    @Override
    protected View newView(Context context, int position, ViewGroup group) {


        return super.newView(context, position, group);
    }

    @Override
    protected void bindView(View v, int position, UIConversation data) {
        if (data != null) {
            if (data.getConversationType().equals(Conversation.ConversationType.DISCUSSION))
                data.setUnreadType(UIConversation.UnreadRemindType.REMIND_ONLY);
        }
        super.bindView(v, position, data);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        if(type == 1){
            int length = position  - super.getCount();
            if(length >= 0 && length < dataList.size()){
                ViewHold viewHold = null;
                if(convertView == null){
                    viewHold = new ViewHold();
                    convertView = LayoutInflater.from(mContent).inflate(R.layout.item_message,null);
                    viewHold.mIv = (ImageView) convertView.findViewById(R.id.message_iv);
                    viewHold.mTvNum = (TextView) convertView.findViewById(R.id.message_tv);
                    viewHold.mTvTitle = (TextView) convertView.findViewById(R.id.message_title);
                    viewHold.mTvTime = (TextView)convertView.findViewById(R.id.message_time);
                    viewHold.mTvContent = (TextView)convertView.findViewById(R.id.message_content);
                    convertView.setTag(viewHold);
                }else {
                    viewHold = (ViewHold) convertView.getTag();
                }
                final MessageBean messageBean = dataList.get(length);
                if(messageBean != null){
                    viewHold.mIv.setBackgroundResource(R.drawable.hz_tx);
                    if(messageBean.getNum() <= 0){
                        viewHold.mTvNum.setVisibility(View.INVISIBLE);
                    }else if(messageBean.getNum() > 99){
                        viewHold.mTvNum.setVisibility(View.VISIBLE);
                        viewHold.mTvNum.setText(99 + "");
                    }else {
                        viewHold.mTvNum.setVisibility(View.VISIBLE);
                        viewHold.mTvNum.setText(messageBean.getNum() + "");
                    }

                    if(!TextUtils.isEmpty(messageBean.getTitle())){
                        viewHold.mTvTitle.setText(messageBean.getTitle());
                    }
                    if(!TextUtils.isEmpty(messageBean.getTime()))
                        viewHold.mTvTime.setText(messageBean.getTime());
                    if(messageBean.getType() == TypeApi.MESSAGE_CONTENT && messageBean.getContent() != null && messageBean.getContent() instanceof String){
                        String content = (String) messageBean.getContent();
                        if(!TextUtils.isEmpty(content)){
                            viewHold.mTvContent.setText(content);
                        }
                    }
                }

                RxView.clicks(convertView)
                        .throttleFirst(1000, TimeUnit.MICROSECONDS)
                        .subscribe((value)->{
                            if(mListener != null) {
                                mListener.onAction(messageBean);
                            }
                        });


            }
//            return convertView;
        }
        LogMessage.doLogMessage("message","message");
        return type == 1?convertView:super.getView(position, convertView, parent);
    }



    @Override
    public int getItemViewType(int position) {

        if(position >= super.getCount()){
            return 1;
        }

        return super.getItemViewType(position);
    }


    @Override
    public int getCount() {

//        return super.getCount();
        return super.getCount() + (dataList == null?0:dataList.size());
    }

    public class ViewHold{
        //        @BindView(R.id.message_iv)
        ImageView mIv;
        //        @BindView(R.id.message_tv)
        TextView mTvNum;
        //        @BindView(R.id.message_title)
        TextView mTvTitle;
        //        @BindView(R.id.message_time)
        TextView mTvTime;
        //        @BindView(R.id.message_content)
        TextView mTvContent;

        public ViewHold() {

        }
    }

//    public ConversationListAdapterEx(Context context) {
//        super(context);
//    }
//
//    @Override
//    protected View newView(Context context, int position, ViewGroup group) {
//        return super.newView(context, position, group);
//    }
//
//    @Override
//    protected void bindView(View v, int position, UIConversation data) {
//        if(data.getConversationType().equals(Conversation.ConversationType.DISCUSSION))
//            data.setUnreadType(UIConversation.UnreadRemindType.REMIND_ONLY);
//        super.bindView(v, position, data);
//    }

}
