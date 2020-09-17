package com.zy.skin_update;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.zy.skin_update.utils.SkinManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkinFactory implements LayoutInflater.Factory2 {

    private Map<String,Constructor<? extends View>> mConstructorMap = new HashMap<>();
    private Class[] viewConstructor = new Class[]{Context.class,AttributeSet.class};
    private String mActivityPath;

    public SkinFactory(String path) {
        this.mActivityPath = path;
    }

    private List<String> viewPackageList = Arrays.asList(
            "android.view.",
            "android.webkit.",
            "android.widget.");

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = createViewFormTag(name,context,attrs);
        if(view == null){
            view = createView(name,context,attrs);
        }
        SkinManager.getInstance().loadAttrs(mActivityPath,view,attrs);
      return view;
    }




    private View createViewFormTag(String name, Context context, AttributeSet attrs) {
        if(name.contains(".")){
            return null;
        }
        View view = null;
        for(String packageName:viewPackageList){
             view = createView(packageName + name,context,attrs);
            if(view != null){
                break;
            }
        }
        return view;
    }

    private View createView(String name, Context context, AttributeSet attrs){
        Constructor<? extends View> constructor = null;
        if(mConstructorMap.containsKey(name)){
            constructor = mConstructorMap.get(name);
        }else {
            Class<? extends View> viewClass = null;
            try {
                viewClass = context.getClassLoader().loadClass(name).asSubclass(View.class);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if(viewClass != null){
                try {
                    constructor = viewClass.getConstructor(viewConstructor);
                    mConstructorMap.put(name,constructor);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }

        if(constructor != null){
            try {
                return constructor.newInstance(context,attrs);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

}
