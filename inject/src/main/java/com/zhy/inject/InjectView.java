package com.zhy.inject;

import android.content.Context;

public class InjectView {

    public static void init(Context context){

        try {
            Class<?> clazz = Class.forName(context.getClass().getName()+"$$ViewBinder");
            ViewBinder viewBidClass = (ViewBinder) clazz.newInstance();
            viewBidClass.bind(context);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
