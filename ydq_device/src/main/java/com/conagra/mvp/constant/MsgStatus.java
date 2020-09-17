package com.conagra.mvp.constant;

/**
 * Created by yedongqin on 2017/10/31.
 */

public interface MsgStatus {

    //未登录
    int STATUS_FAILED_UN_LOGIN = -1;

    int STATUS_FAILED = 0;
    //    失败
    int STATUS_SUCCESS = 1;
    //    成功
    int STATUS_FAILED_PARAM_NOT_NOULL = 2;
    //    请求参数为空
    int STATUS_FAILED_DATA_EXCEPTION = 3;//数据异常
}
