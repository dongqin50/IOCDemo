package com.conagra.mvp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yedongqin on 2018/3/11.
 */

public class HeartListModel implements Serializable{


    /**
     * rows : [{"tb_FetalHeart_ID":"4878e544-1336-474e-b5f8-f44d0ebf6606","CustomerID":"0154a9ea-06a2-4460-85fa-e394099faf62","HospitalID":null,"StartTime":"2018-03-18 21:02:11","Times":51,"AvgHeartRate":0,"PiontValue":"154,178,117,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,126,130,129,124,118,113,100,100,97,61,64,71,69,68,67,75,77","PiontUterine":"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0","AudioFileName":"1521378131890.wav","AudiofileSize":187644,"AudioURL":"upload/Audio/FetalHeart/2018/3/18/20180318090309.wav","CreateTime":"2018-03-18 21:03:09"},{"tb_FetalHeart_ID":"a4b05bae-266e-47bc-ba86-d861aeb8e7a0","CustomerID":"0154a9ea-06a2-4460-85fa-e394099faf62","HospitalID":null,"StartTime":"2018-03-15 22:53:12","Times":23,"AvgHeartRate":0,"PiontValue":"0,0,0,0,0,0,0,0,0,0,0,117,182,0,0,0,0,0,0,0,0,0","PiontUterine":"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0","AudioFileName":"1521125592245.wav","AudiofileSize":90844,"AudioURL":"upload/Audio/FetalHeart/2018/3/15/20180315105338.wav","CreateTime":"2018-03-15 22:53:38"},{"tb_FetalHeart_ID":"d1d284fb-23fe-46df-acb7-91c0142b2d62","CustomerID":"0154a9ea-06a2-4460-85fa-e394099faf62","HospitalID":null,"StartTime":"2018-03-15 00:42:01","Times":22,"AvgHeartRate":0,"PiontValue":"0,0,0,0,135,54,49,141,0,0,0,0,0,0,0,0,0,151,151,193,99,107","PiontUterine":"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0","AudioFileName":"1521045721601.wav","AudiofileSize":89244,"AudioURL":"upload/Audio/FetalHeart/2018/3/15/20180315124225.wav","CreateTime":"2018-03-15 00:42:25"}]
     * total : 3
     */

    private int total;
    private List<HeartListRowBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<HeartListRowBean> getRows() {
        return rows;
    }

    public void setRows(List<HeartListRowBean> rows) {
        this.rows = rows;
    }


}
