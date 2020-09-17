package com.conagra.mvp.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.LayoutRes;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.ref.WeakReference;


/**
 * Created by yedongqin on 16/8/9.
 */
public class ActivityUtils {


//    /**
//     * y
//     * @param context
//     */
//    public static void hideRequestDialog(Context context){
//
//        DialogManagerUtils.getInstance().closeDialg();
//    }
//    public static void hideWifiDialog(Context context){
//
//        DialogManagerUtils.getInstance().closeDialg();
//    }

    /**
     * 显示状态栏
     */
    public static  void showStateBar(){

        WindowManager.LayoutParams layoutParams = ActivityManageUtils.getInstance().getCurrentActivity().getWindow().getAttributes();
        layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        ActivityManageUtils.getInstance().getCurrentActivity().getWindow().setAttributes(layoutParams);
    }

    /**
     * 隐藏状态栏
     */
    public static void hideStateBar(){
        WindowManager.LayoutParams layoutParams = ActivityManageUtils.getInstance().getCurrentActivity().getWindow().getAttributes();
        layoutParams.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        ActivityManageUtils.getInstance().getCurrentActivity().getWindow().setAttributes(layoutParams);
    }

    /**
     * 隐藏键盘
     */
    public static  void  hideKeyboard(){
        View view = ActivityManageUtils.getInstance().getCurrentActivity().getCurrentFocus();
        if(view != null){
            ((InputMethodManager) ActivityManageUtils.getInstance().getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        activity = (Activity) getWeakActivity(activity);
        if(activity != null){
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = activity.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(activity.getResources().getColor(colorResId));

                    //底部导航栏
                    //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static Object getWeakActivity(Object activity){
        WeakReference<Object> weakReference = new WeakReference<>(activity);
        return  weakReference.get();
    }


    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        dialog = (Dialog) getWeakActivity(dialog);
        if(dialog == null){
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void popSoftInput(EditText editText,Boolean isSoft){
        editText = (EditText) getWeakActivity(editText);
        if(editText == null){
            return;
        }
        InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(isSoft){
            editText.requestFocus();
            imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
        }else {
            imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
        }



    }
//
////    //弹出网络连接异常
//    public static boolean showInternetError(Context context){
//        context = (Context) getWeakActivity(context);
//        if(context == null){
//            return false;
//        }
//       DialogManagerUtils.getInstance().showNetworkErrorDialog(context);
//        return CacheUtils.isConnected();
//    }
//// //弹出网络请求
//    public static boolean showQuestionDialog(Context context){
//        context = (Context) getWeakActivity(context);
//        if(context == null){
//            return false;
//        }
//        DialogManagerUtils.getInstance().showRequestDialog(context);
//        return CacheUtils.isConnected();
//    }

//    public static View getLayoutInflater(Context context, @LayoutRes int resId, ViewGroup view, boolean attatchToRoot){
//        return ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resId,view,attatchToRoot);
//    }attatchToRoot

    public static View getLayoutInflater(Context context, @LayoutRes int resId, ViewGroup view, boolean attatchToRoot){
        return LayoutInflater.from(context).inflate(resId,view,attatchToRoot);
    }


    public static View getLayoutInflater(Context context, @LayoutRes int  resId, ViewGroup parent){
       return LayoutInflater.from(context).inflate(resId,parent,false);
    }

    /**
     * 跳转到下一Activity
     * @param clazz
     */
    public static void toNextActivity(Class clazz){
        Intent intent = new Intent(ActivityManageUtils.getInstance().getCurrentActivity(),clazz);
        ActivityManageUtils.getInstance().getCurrentActivity().startActivity(intent);
    }
    /**
     * 跳转到下一Activity
     * @param clazz
     */
    public static void toNextActivity(Activity activity,Class clazz){
        Intent intent = new Intent(activity,clazz);
        activity.startActivity(intent);
    }



    /**
     * 跳转到下一Activity
     * @param clazz
     * @param actions
     */
    public static void toNextActivity(Class clazz,String... actions){
        Intent intent = new Intent();
        for(String a : actions){
            intent.setAction(a);
        }
        intent.setClass(ActivityManageUtils.getInstance().getCurrentActivity(),clazz);
        ActivityManageUtils.getInstance().getCurrentActivity().startActivity(intent);
    }
    /**
     * 获取当前窗体的大小
     *
     * @return
     */
    public static Display getWindowDisplay(){
        Display display = ActivityManageUtils.getInstance().getCurrentActivity().getWindowManager().getDefaultDisplay();
        return  display;
    }

    /**
     * sd卡是否存在
     */
    public static boolean isSDCardAvailable(){

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }

        LogMessage.doLogMessage(ActivityUtils.class.getSimpleName(),"sd卡不可用");
//        ToastUtils.getInstance().makeText("sd卡不可用").show();
        return false;
    }

    /**
     * 点击事件
     * @param
     * @return
     */
//    public static Observable doClick(View view){
//        view = (View) getWeakActivity(view);
//        if(view == null){
//            return Observable.just(view);
//        }
//        Observable observable = null;
//
//        if(view != null){
//            observable = RxView.clicks(view)
//                    .throttleFirst(1000, TimeUnit.MILLISECONDS);
//        }
//        return  observable;
//    };

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int totalWidth = 0;
        int count = listAdapter.getCount();
        if (count > 6) {
            View listItem = listAdapter.getView(0, null, listView);
            listItem.measure(0, 0);
            totalHeight = listItem.getMeasuredHeight() * 6;
            totalWidth = listItem.getMeasuredWidth();
        }else{
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
                totalWidth = listItem.getMeasuredWidth();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        params.width =totalWidth;
        listView.setLayoutParams(params);
    }


}

