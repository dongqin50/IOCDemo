package com.zy.binder_server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.zy.binder.MyApp;

public class MyService extends Service {

    MyApp myApp = new MyAppImpl();

    @Override
    public IBinder onBind(Intent intent) {

        try {
            myApp.setName("bbbbbbbbb");
            Log.v("binder1", " MyService setName : ");
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        try {
            String name = myApp.getName();
            Log.v("binder1", "MyService getName : " + name);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return myApp.asBinder();
    }
}
