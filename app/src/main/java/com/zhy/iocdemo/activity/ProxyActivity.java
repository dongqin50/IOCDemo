package com.zhy.iocdemo.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.zhy.iocdemo.PluginManager;
import com.zy.base_core.BasePluginLifeCycle;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ProxyActivity extends AppCompatActivity {

    private String className;
    private BasePluginLifeCycle mLoadActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fullscreen);
        className = getIntent().getStringExtra("className");
        try {
            Class<?> clazz = getClassLoader().loadClass(className);
            Constructor<?> constructor = clazz.getConstructor(new Class[]{});
            mLoadActivity = (BasePluginLifeCycle) constructor.newInstance(new Object[]{});
            mLoadActivity.attach(this);
            Bundle bundle = new Bundle();
            mLoadActivity.onCreate(bundle);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mLoadActivity != null)
            mLoadActivity.onResume();
    }

    @Override
    public ClassLoader getClassLoader() {
        return PluginManager.getInstance().getDexClassLoader();
    }

    @Override
    public Resources getResources() {
        return PluginManager.getInstance().getExtraResource();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mLoadActivity != null)
            mLoadActivity.onStart();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(mLoadActivity != null){
            mLoadActivity.onPostCreate(savedInstanceState);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLoadActivity != null)
            mLoadActivity.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mLoadActivity != null)
            mLoadActivity.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLoadActivity != null)
            mLoadActivity.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadActivity != null)
            mLoadActivity.onDestroy();
    }

    @Override
    public Window getWindow() {
        if(mLoadActivity != null){
        }
        return super.getWindow();
    }
}
