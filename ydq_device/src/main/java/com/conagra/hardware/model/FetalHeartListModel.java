package com.conagra.hardware.model;

import java.io.Serializable;

/**
 * Created by yedongqin on 16/10/8.
 */
public class FetalHeartListModel implements Serializable {

    private String customerNo;  //客户编号
    private String beginDate;  //起始日期
    private String endDate; //结束日期
    private String pageSize;        //       页行总数
    private String pageIndex; //页码

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }
}
