package com.conagra.mvp.model;

import java.util.List;

/**
 * Created by yedongqin on 2017/10/31.
 */

public class BloodSugarListModel {


    /**
     * rows : [{"tb_BloodSugar_ID":"71fd5148-4e9b-4bec-b500-d101fa5219a6","CustomerID":"0154a9ea-06a2-4460-85fa-e394099faf62","TimeSlot":0,"BloodSugarValue":1.5,"BloodSugarClass":"低","CreateTime":"2017-10-31"},{"tb_BloodSugar_ID":"a82b48ec-4789-4ad7-9a6a-9dc418b720e0","CustomerID":"0154a9ea-06a2-4460-85fa-e394099faf62","TimeSlot":0,"BloodSugarValue":1.5,"BloodSugarClass":"低","CreateTime":"2017-10-31"},{"tb_BloodSugar_ID":"17d815b8-2973-45c3-a8ae-a747e272d709","CustomerID":"0154a9ea-06a2-4460-85fa-e394099faf62","TimeSlot":0,"BloodSugarValue":4.5,"BloodSugarClass":"正常","CreateTime":"2017-10-31"},{"tb_BloodSugar_ID":"cdfa373d-0604-43c6-9b69-2e446532dd02","CustomerID":"0154a9ea-06a2-4460-85fa-e394099faf62","TimeSlot":0,"BloodSugarValue":4.5,"BloodSugarClass":"正常","CreateTime":"2017-10-31"},{"tb_BloodSugar_ID":"f02d84d4-88e7-4a01-a4f7-ff54a2039827","CustomerID":"0154a9ea-06a2-4460-85fa-e394099faf62","TimeSlot":0,"BloodSugarValue":4.1,"BloodSugarClass":"较低","CreateTime":"2017-10-29"}]
     * total : 5
     */

    private int total;
    private List<BloodSugarListRowModel> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BloodSugarListRowModel> getRows() {
        return rows;
    }

    public void setRows(List<BloodSugarListRowModel> rows) {
        this.rows = rows;
    }
}
