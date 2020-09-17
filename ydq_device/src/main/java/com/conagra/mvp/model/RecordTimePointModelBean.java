package com.conagra.mvp.model;

import java.util.List;

/**
 * Created by Dongqin.Ye on 2018/3/22.
 */

public class RecordTimePointModelBean {

    /**
     * StartTime : 2018-03-11
     * RecordTimePoint : ["2018-03-11 18:05:29"]
     */

    private String StartTime;
    private List<String> RecordTimePoint;

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String StartTime) {
        this.StartTime = StartTime;
    }

    public List<String> getRecordTimePoint() {
        return RecordTimePoint;
    }

    public void setRecordTimePoint(List<String> RecordTimePoint) {
        this.RecordTimePoint = RecordTimePoint;
    }
}
