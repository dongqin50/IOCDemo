package com.conagra.mvp.utils;


import android.app.Activity;
import android.content.Context;

import com.conagra.MainApplication;

import java.util.Stack;

/**
 * Created by yedongqin on 16/8/1.
 * Activity管理工具
 */
public class ActivityManageUtils {

    private Context mContext;
    private Activity mActivity;
    private MainApplication mApplication;
    private Stack<Context> mActivityStack;
    private  static ActivityManageUtils mManageUtils;

    public void init(MainApplication application){
        mApplication = application;
        mActivityStack = new Stack<>();
    }


    public static ActivityManageUtils getInstance(){

        if(mManageUtils == null){
            synchronized (ActivityManageUtils.class){
                if(mManageUtils == null){
                    mManageUtils = new ActivityManageUtils();
                }
            }
        }
        return mManageUtils;
    }


    /**
     * 添加一个Activity
     * @param context
     */
    public  void doPush(Context context)  {
        mActivity = (Activity) context;
        mContext = context;
        if(mActivity != null){
            mActivityStack.push(context);
        }
    }



    public  int getLength(){
        return mActivityStack.size();
    }


    /**
     * 弹出一个Activity
     * @return
     */
    public Context doPop() {
        if(mActivityStack != null && mActivityStack.size() > 0){
            return mActivityStack.pop();
        }
        return null;
    }

    /**
     * 清空所有的Activity
     */
    public  void doClear(){
        if(mActivityStack == null){
            return;
        }
        while (mActivityStack.size() > 0){
            Context bean = mActivityStack.pop();
            if(bean != null){
                ((Activity)bean).finish();
            }
        }
    }


    /**
     * 获取当前Activiy
     * @return
     * @throws  
     */
    public  Activity getCurrentActivity(){

        return mActivity;
    }

    /**
     * 获得上下文
     * @return
     * @throws  
     */
    public  Context getCurrentContext()  {
        return mContext;
    }

    /**
     * 获取Application
     * @return
     * @throws  
     */
    public  MainApplication getMyApplication()  {
        return mApplication;
    }
}
