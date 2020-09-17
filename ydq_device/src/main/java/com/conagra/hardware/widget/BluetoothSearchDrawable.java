package com.conagra.hardware.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.conagra.R;
import com.conagra.databinding.ItemBluetoothsearchBinding;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;


/**
 * Created by yedongqin on 2016/10/17.
 */
public class BluetoothSearchDrawable extends RelativeLayout {

    private Observable mBtSearch;
    private boolean isClick;
    private ItemBluetoothsearchBinding mBinding;
    private Animation mAnimation;

    public BluetoothSearchDrawable(Context context) {
        super(context);
        init(context);
    }

    public BluetoothSearchDrawable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BluetoothSearchDrawable(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(final Context context){
        mAnimation = AnimationUtils.loadAnimation(context, R.anim.dialog_rotate);
        mAnimation.setDuration(1500);
        mAnimation.setInterpolator(new LinearInterpolator());
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_bluetoothsearch,this,true);

        if(mAnimation != null)
            mAnimation.start();
        mBtSearch = RxView.clicks(mBinding.bluetoothSearchFl)
                .filter((o)->!isClick)
                .map((o)->{
                    isClick = true;
                    mBinding.bluetoothSearchFl.setEnabled(false);
                    mBinding.bluetoothSearchBitmap.setImageResource(R.drawable.bluetooth_search);
                    mBinding.bluetoothSearchLine.setVisibility(VISIBLE);
//                            mLineImageView.setVisibility(VISIBLE);
//                            mLineImageView.setAnimation(mAnimation);
                    mBinding.bluetoothSearchLine.startAnimation(mAnimation);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doFinished();
                        }
                    }, 10 * 1000 * 60);
//                            Observable.interval(10 * 1000,TimeUnit.MICROSECONDS)
//                                    .subscribeOn(Schedulers.io())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribe(new Action1<Long>() {
//                                        @Override
//                                        public void call(Long aLong) {
//
//                                        }
//                                    });
//                            if(mAnimation != null)
//                                mAnimation.start();

                    return isClick;
                });
//        mBinding.bluetoothSearchFl.setOnClickListener((v)-> {
//                    mBtSearch = Flowable.just("")
//                            .filter((o) -> !isClick)
//                            .map((o) -> {
//
//                            }).toObservable().publish();
//                });
//        mBtSearch = RxView.clicks(mBinding.bluetoothSearchFl)
//                .throttleFirst(1000, TimeUnit.MICROSECONDS)
//                .filter(new Predicate<Object>() {
//
//                    @Override
//                    public boolean test(Object o) throws Exception {
//                        return !isClick;
//                    }
//                })
//                .map(new Function() {
//                    @Override
//                    public Object apply(Object o) throws Exception {
//
//                    }
//                });


    }

    public Observable doSearch(){
        return mBtSearch;
    }

    public void doFinished(){
        if(isClick){
            isClick = false;
//            if(mAnimation != null)
//                mAnimation.cancel();
            mBinding.bluetoothSearchFl.setEnabled(true);
            mBinding.bluetoothSearchLine.clearAnimation();
            mBinding.bluetoothSearchLine.setVisibility(GONE);
            mBinding.bluetoothSearchBitmap.setImageResource(R.drawable.ljsb_lylj);

        }
    }


}
