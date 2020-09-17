package com.conagra.hardware.model;

import java.io.Serializable;

/**
 * Created by yedongqin on 16/9/21.
 */
public class BluetoothModel implements Serializable {

    private String deviceName;
    private String deviceAddress;

    private boolean isConnected;

    public String getDeviceName() {
        return deviceName;
    }

    public BluetoothModel() {
    }

    public BluetoothModel(String deviceName, String deviceAddress) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
