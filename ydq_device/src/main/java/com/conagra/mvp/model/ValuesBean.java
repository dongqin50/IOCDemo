package com.conagra.mvp.model;

import java.io.Serializable;

/**
 * Created by yedongqin on 16/10/9.
 *
 * 可以存储两种不同的类型数组,方便使用,辅助ValuesListBean类,使其可以获取任何
 * 某一列的值
 */
public class ValuesBean<T,R> implements Serializable {

    private T[] value1;
    private R[] value2;

    public void setValue1List(T... value1) {
        this.value1 = value1;
    }

    public R getValue2(int index) {
        if (value2 == null || index >= value2.length || index < 0){
            return null;
        }
        return value2[index];
    }

    public void setValue2List(R... value2) {
        this.value2 = value2;
    }

    public void setValue1(int index, T value){
        if (value1 == null || index >= value1.length || index < 0){
            return;
        }
        value1[index] = value;
    }
    public void setValue2(int index, R value){
        if (value2 == null || index >= value2.length || index < 0){
            return;
        }
        value2[index] = value;
    }

    public T getValue1(int index) {
        if (value1 == null || index >= value1.length || index < 0){
            return null;
        }

        return value1[index];
    }
}
