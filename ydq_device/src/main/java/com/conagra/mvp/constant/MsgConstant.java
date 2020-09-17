package com.conagra.mvp.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yedongqin on 2017/10/31.
 */

public interface MsgConstant {

    String STATUS_FAILED_UN_LOGIN = "未登录";
    String STATUS_FAILED = "失败";
    String STATUS_SUCCESS = "成功";
    String STATUS_FAILED_PARAM_NOT_NOULL = "请求参数为空";
    String STATUS_FAILED_DATA_EXCEPTION = "数据异常";


    @Retention(RetentionPolicy.SOURCE)
    @interface BloodSugarTime{
        String BEFORE_BREAKFAST = "早餐前";
        String AFTER_BREAKFAST = "早餐后";
        String BEFORE_LUNCH = "午餐前";
        String AFTER_LUNCH = "午餐后";
        String BEFORE_DINNER = "晚餐前";
        String AFTER_DINNER = "晚餐后";
        String BEFORE_SLEEP = "睡前";
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface BloodSugarTimeStatus{
        int BEFORE_BREAKFAST = 0;
        int AFTER_BREAKFAST = 1;
        int BEFORE_LUNCH = 2;
        int AFTER_LUNCH = 3;
        int BEFORE_DINNER = 4;
        int AFTER_DINNER = 5;
        int BEFORE_SLEEP = 6;
    }

}
