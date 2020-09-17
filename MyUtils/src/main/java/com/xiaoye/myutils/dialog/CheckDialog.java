package com.xiaoye.myutils.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaoye.myutils.R;

import io.reactivex.Flowable;

/**
 * Created by yedongqin on 16/9/17.
 */
public class CheckDialog extends BaseDialog {

    private TextView mContent;
    private RelativeLayout mCancle;
    private RelativeLayout mSure;
    private TextView mTvCancle;
    private TextView mTvSure;
    private RelativeLayout mRlChoose;
    private RelativeLayout mRlShow;
    private TextView mTvShow;
     Flowable mSureObservable;
     Flowable mCancleObservable;

    public CheckDialog(Context context) {
        this(context, R.style.Dialog);
    }

    public CheckDialog(Context context, int themeResId) {
        super(context,themeResId);
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.ul_dialog_check);
    }

    @Override
    public void initView() {
        mContent = (TextView) findViewById(R.id.dialog1_message);
        mCancle = (RelativeLayout) findViewById(R.id.dialog1_cancle);
        mSure= (RelativeLayout) findViewById(R.id.dialog1_sure);
        mTvCancle= (TextView) findViewById(R.id.negativeButton);
        mTvSure= (TextView) findViewById(R.id.positiveButton);
        mRlChoose= (RelativeLayout) findViewById(R.id.dialog1_choose);
        mRlShow= (RelativeLayout) findViewById(R.id.dialog1_show);
        mTvShow= (TextView) findViewById(R.id.dialog1_show_content);
    }

    @Override
    public void initData(Context context) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setGravity(Gravity.CENTER);

        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = d.getWidth(); //设置dialog的宽度为当前手机屏幕的宽度
        getWindow().setAttributes(p);
    }

    /**
     * 取消
     * @return
     */

    /**
     * 取消
     * @return
     */
    public Flowable doCancleEvent(String title) {
        mTvCancle.setText(title);
        return mCancleObservable;
    }

    public void setmTvCancleTitle(String title) {
        mTvCancle.setText(title);
    }

    public void setmTvSureTitle(String title) {
        mTvSure.setText(title);
    }

    /**
         * 确定
         * @return
         */
    public Flowable doSureEvent(){
        return mSureObservable;
    }
     /**
         * 确定
         * @return
         */
    public Flowable doSureEvent(String title){
        mTvSure.setText(title);
        return mSureObservable;
    }

    /**
     * 设置消息信息
     * @param content
     */
    public void setContent(String content){
        mRlChoose.setVisibility(View.VISIBLE);
        mRlShow.setVisibility(View.GONE);
        mContent.setText(content);
    }

//     /**
//     * 设置消息信息
//     * @param content
//     */
//    public Flowable doShowContent(String content){
//        mRlChoose.setVisibility(View.GONE);
//        mRlShow.setVisibility(View.VISIBLE);
//        mTvShow.setText(content);
//
//        return mShowObservable;
//    }
//


}
