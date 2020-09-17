package com.xiaoye.myutils.toast;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by yedongqin on 2017/4/12.
 */

public class CustomToast extends BaseToast{

    public CustomToast(Context context, ToastEventListerner mToastEventListener) {
        super(context, mToastEventListener);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void cancel() {
        super.cancel();

    }

//    public static CustomToast makeText(Context context, CharSequence text, int duration, ToastEventListerner mToastEventListener) {
//        return CustomToast.makeText(context,text,duration);
//    }
//    public static CustomToast makeText(Context context, CharSequence text, int duration, ToastEventListerner mToastEventListener) {
//        return makeText(context,text,duration, R.layout.ul_item_props_toast,R.id.props_message, mToastEventListener);
//    }
//    public static CustomToast makeText(Context context, CharSequence text, ToastEventListerner mToastEventListener) {
//        return makeText(context,text,Toast.LENGTH_SHORT, R.layout.ul_item_props_toast,R.id.props_message, mToastEventListener);
//    }

    public static CustomToast makeText(Context context, CharSequence text, @LayoutRes int layoutRes, @IdRes int tvIdRes, ToastEventListerner mToastEventListener) {
        return makeText(context,text,Toast.LENGTH_SHORT,layoutRes,tvIdRes, mToastEventListener);
    }
    public static CustomToast makeText(Context context, CharSequence text, int duration, @LayoutRes int layoutRes, @IdRes int tvIdRes, ToastEventListerner mToastEventListener) {
        CustomToast result = new CustomToast(context, mToastEventListener);
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(layoutRes, null);
        TextView tv = (TextView)v.findViewById(tvIdRes);
        tv.setText(text);
        result.setDuration(duration);
        result.setView(v);

        return result;
    }
}
