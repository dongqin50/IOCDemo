package com.conagra.hardware.database;


import android.content.Context;

import com.conagra.hardware.model.FetalMovementModel;

import java.util.List;


/**
 * Created by yedongqin on 16/10/9.
 * 胎动数据库
 */
public class FetalMoveDao extends BaseDao<FetalMovementModel> {

    public FetalMoveDao(Context context) {
        super(context);
    }

    @Override
    public int add(FetalMovementModel bean) {
        return -1;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void update(int id, FetalMovementModel bean) {

    }

    @Override
    public FetalMovementModel select(String id) {
        return null;
    }

    @Override
    public List<FetalMovementModel> selectAll() {
        return null;
    }
}
