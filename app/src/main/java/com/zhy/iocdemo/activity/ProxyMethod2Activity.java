package com.zhy.iocdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhy.iocdemo.R;

public class ProxyMethod2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy_method2);
    }

    @Override
    public ClassLoader getClassLoader() {
        return super.getClassLoader();
    }
}
