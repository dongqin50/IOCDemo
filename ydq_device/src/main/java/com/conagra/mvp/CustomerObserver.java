package com.conagra.mvp;

import com.conagra.mvp.bean.BaseBackBean;

import io.reactivex.Observable;

/**
 * Created by yedongqin on 2017/10/31.
 */

public abstract class CustomerObserver<T> extends Observable<BaseBackBean<T>> {


}
