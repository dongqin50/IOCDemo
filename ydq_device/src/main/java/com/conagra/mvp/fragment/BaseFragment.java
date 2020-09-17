package com.conagra.mvp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by yedongqin on 16/9/15.
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
//    protected MainActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
//        mActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = getCurrentView(inflater,container,savedInstanceState);
//        ButterKnife.bind(this,view);
        doWork();
        return view;
    }

    /**
     * 添加视图
     * @return
     */
    public abstract View getCurrentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState );

    /**
     * 做一些其他业务处理
     */
    public abstract void doWork();

    @Override
    public void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = BaseApplication.getRefWatcher(getActivity());
//        refWatcher.watch(this);
    }
}
