package com.zy.taopiaopiao;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.zy.base_core.BasePluginLifeCycle;

public class BaseActivity extends AppCompatActivity implements BasePluginLifeCycle {

    private AppCompatActivity mSuperActivity;

    @Override
    public void attach(Activity proxyActivity) {
        this.mSuperActivity = (AppCompatActivity) proxyActivity;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public Resources.Theme getTheme() {
        if(mSuperActivity != null){
            return mSuperActivity.getTheme();
        }
        return super.getTheme();
    }

    @Nullable
    @Override
    public ActionBar getSupportActionBar() {
        if(mSuperActivity != null){
            return mSuperActivity.getSupportActionBar();
        }
        return super.getSupportActionBar();
    }



    @Override
    public Window getWindow() {
        if(mSuperActivity != null){
            return mSuperActivity.getWindow();
        }
        return super.getWindow();
    }

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        if(mSuperActivity!= null){
            return mSuperActivity.getDelegate();
        }
        return super.getDelegate();
    }

    @Override
    public MenuInflater getMenuInflater() {
        if(mSuperActivity != null){
            return mSuperActivity.getMenuInflater();
        }
        return super.getMenuInflater();
    }

    @Override
    public void setContentView(int layoutResID) {
        if(this.mSuperActivity != null){
            mSuperActivity.setContentView(layoutResID);
        }else {
            super.setContentView(layoutResID);
        }
    }

     @Override
    public void setContentView(View view) {
         if(this.mSuperActivity != null){
             mSuperActivity.setContentView(view);
         }else {
             super.setContentView(view);
         }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
       if(mSuperActivity != null){
           return mSuperActivity.onCreateOptionsMenu(menu);
       }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mSuperActivity != null){
            return mSuperActivity.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTheme(int resid) {
        if(mSuperActivity != null){
            mSuperActivity.setTheme(resid);
        }else {
            super.setTheme(resid);
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        if(this.mSuperActivity != null){
           return this.mSuperActivity.findViewById(id);
        }
        return super.findViewById(id);
    }

    @Override
    public Intent getIntent() {
        if(this.mSuperActivity != null){
            return  mSuperActivity.getIntent();
        }
        return super.getIntent();
    }

    @Override
    public Resources getResources() {
        if(mSuperActivity != null){
            return mSuperActivity.getResources();
        }
        return super.getResources();
    }

    @Override
    public ClassLoader getClassLoader() {
        if(mSuperActivity != null){
            return mSuperActivity.getClassLoader();
        }
        return super.getClassLoader();
    }
    @Override
    public WindowManager getWindowManager() {
        if(mSuperActivity != null){
            return mSuperActivity.getWindowManager();
        }
        return super.getWindowManager();
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        if(mSuperActivity != null){
            return mSuperActivity.getLayoutInflater();
        }
        return super.getLayoutInflater();
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        if(mSuperActivity != null){
            return mSuperActivity.getApplicationInfo();
        }
        return super.getApplicationInfo();
    }

//    @Override
//    public AssetManager getAssets() {
//        if(mSuperActivity != null){
//            return mSuperActivity.getAssets();
//        }
//        return super.getAssets();
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(mSuperActivity == null){
            super.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onRestart() {
        if(mSuperActivity == null){
            super.onRestart();
        }
    }

    @Override
    public void onStart() {
        if(mSuperActivity == null){
            super.onStart();
        }
    }

    @Override
    public void onResume() {
        if(mSuperActivity == null){
            super.onResume();
        }
    }

    @Override
    public void onPause() {
        if(mSuperActivity == null){
            super.onPause();
        }
    }

    @Override
    public void onStop() {
        if(mSuperActivity == null){
            super.onStop();
        }
    }

    @Override
    public void onDestroy() {
        if(mSuperActivity == null){
            super.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(mSuperActivity == null){
            super.onSaveInstanceState(outState);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mSuperActivity != null){
            return mSuperActivity.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }
}
