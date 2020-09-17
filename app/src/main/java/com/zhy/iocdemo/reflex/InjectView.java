package com.zhy.iocdemo.reflex;

import android.content.Context;
import android.view.View;

import com.zhy.iocdemo.reflex.annotation.BindEvent;
import com.zhy.iocdemo.reflex.annotation.BindView;
import com.zhy.iocdemo.reflex.annotation.SetContentView;
import com.zhy.iocdemo.reflex.proxy.ViewInvocationHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectView {

    public static void inject(Context activity) {

        injectLayoutView(activity);
        injectField(activity);
        injectEventListener(activity);
    }

    private static void injectLayoutView(Context activity) {
        SetContentView setContentView = activity.getClass().getAnnotation(SetContentView.class);
        int layoutId = setContentView.value();
        try {
            Method method = activity.getClass().getMethod("setContentView", int.class);
            method.invoke(activity, layoutId);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void injectField(Context activity) {

        Field[] fields = activity.getClass().getDeclaredFields();

        for (Field field : fields) {

            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView == null) {
                continue;
            }

            int resId = bindView.value();

            try {
                Method method = activity.getClass().getMethod("findViewById", int.class);

                Object result = method.invoke(activity, resId);

                field.set(activity, result);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }


        }

    }

    private static void injectEventListener(Context activity) {

        Method[] methods = activity.getClass().getMethods();
        for (Method method : methods) {

            String name = method.getName();
            Annotation[] annotations = method.getAnnotations();
            try {
                for (Annotation annotation : annotations) {
                    BindEvent bindEvent = annotation.annotationType().getAnnotation(BindEvent.class);
                    if (bindEvent == null) {
                        continue;
                    }

                    Method resIdMethod = annotation.annotationType().getDeclaredMethod("value");
                    int[] resIds = (int[]) resIdMethod.invoke(annotation);
                    for (int resId : resIds) {

                        Method findViewMethod = activity.getClass().getMethod("findViewById", int.class);
                        View view = (View) findViewMethod.invoke(activity, resId);
                        ViewInvocationHandler invocationHandler = new ViewInvocationHandler(activity, method);
                        Method listener = view.getClass().getMethod(bindEvent.setMethod(), bindEvent.interfaceName());
                        Object activityProxy = Proxy.newProxyInstance(
                                bindEvent.interfaceName().getClassLoader(), new Class[]{bindEvent.interfaceName()}, invocationHandler);

                        listener.invoke(view, activityProxy);
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }


    }


}
