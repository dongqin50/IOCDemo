package com.conagra.hardware.database;

import android.content.Context;

import java.util.List;

/**
 * Created by yedongqin on 16/9/29.
 */
public abstract class BaseDao<T> {

    protected FeldsherSqlHelp sqlHelp;

    public BaseDao(Context context) {
        this.sqlHelp = new FeldsherSqlHelp(context);
    }

    /**
     * 添加数据
     * @param t
     */
    public abstract int add(T t);

    /**
     * 删除纪录
     * @param id
     */
    public abstract void delete(String id);

    /**
     * 更新数据
     * @param id
     * @param t
     */
    public abstract void update(int id,T t);

    /**
     * 查找
     * @param id
     * @return
     */
    public abstract T select(String  id);

 /**
     * 查找
     *
     * @return
     */
    public abstract List<T> selectAll();


}
