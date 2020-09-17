package com.conagra.mvp.utils;

import android.util.Log;

/**
 * Created by yedongqin on 16/7/14.
 *
 * 打印日志
 */
public class LogMessage {

    /**
     * 打印普通日志
     * @param tag
     * @param msg
     */
    public static void doLogMessage(String tag,String msg){

        Log.i(tag, tag + " : " + msg);
    }

    /**
     * 打印错误日志
     * @param tag
     * @param msg
     */
    public static void doLogErrorMessage(String tag,String msg){
        Log.e(tag,tag + " : " + msg);
    }


}
