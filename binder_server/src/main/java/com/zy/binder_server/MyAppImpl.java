package com.zy.binder_server;

import android.os.RemoteException;

import com.zy.binder.MyApp;

public class MyAppImpl   extends MyApp.Stub {

    private String name;
    @Override
    public void setName(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }
}
