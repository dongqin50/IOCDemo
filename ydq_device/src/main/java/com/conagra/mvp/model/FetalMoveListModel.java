package com.conagra.mvp.model;

import java.util.List;

/**
 * Created by yedongqin on 2018/3/10.
 */

public class FetalMoveListModel {

    private int total;
    private List<FetalMoveListRowBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<FetalMoveListRowBean> getRows() {
        return rows;
    }

    public void setRows(List<FetalMoveListRowBean> rows) {
        this.rows = rows;
    }

}
