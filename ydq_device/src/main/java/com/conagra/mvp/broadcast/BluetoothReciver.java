package com.conagra.mvp.broadcast;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.conagra.hardware.activity.FetalHeartActivity;
import com.conagra.hardware.model.BluetoothModel;
import com.conagra.mvp.utils.ActivityUtils;

import java.io.IOException;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yedongqin on 16/9/21.
 *
 * 蓝牙接收器
 */
public class BluetoothReciver extends BroadcastReceiver {

    public  OnBluetoothStatus onBluetoothStatus;


    public BluetoothReciver(Context context, OnBluetoothStatus onBluetoothStatus){
       this.onBluetoothStatus = onBluetoothStatus;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {

        String  action = intent.getAction();
        switch (action){
            case BluetoothDevice.ACTION_FOUND:
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                onBluetoothStatus.onFoundDevice(new BluetoothModel(device.getName(),device.getAddress()));
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                onBluetoothStatus.onSelectedFinished();
                break;
//            case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
//
//                BluetoothDevice device1 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                BluetoothModel bluetoothBean = CacheUtils.getBluetoothBean();
//
//                if(device1 != null && bluetoothBean != null && device1.getName() != null && device1.getName().equals(bluetoothBean.getDevName())){
//                    int status = device1.getBondState();
//                    switch (status){
//
//                        case BluetoothDevice.BOND_BONDED:
//                            connect(context,device1);
//
//                            break;
//                        case BluetoothDevice.BOND_BONDING:
//                        break;
//                        case BluetoothDevice.BOND_NONE:
//                        break;
//                    }
//
//                }
//
//                break;
        }
    }


    public interface OnBluetoothStatus{
        /**
         * 发现设备
         * @param device
         */
         void onFoundDevice(BluetoothModel device);

        /**
         * 查找完成
         */
         void onSelectedFinished();

//         void onConnected();
    }

    public void connect(final Context context, final BluetoothDevice device) {
        if(device == null)
            return;

        Observable.just(device)
                .filter(new Predicate<BluetoothDevice>() {
                    @Override
                    public boolean test(BluetoothDevice bluetoothDevice) throws Exception {
                        BluetoothSocket socket = null;
                        try {
                            // 固定的UUID00001101-0000-1000-8000-00805F9B34FB
                            final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
                            UUID uuid = UUID.fromString(SPP_UUID);
                            socket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                            socket.connect();
                            socket.close();
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            if(socket != null){
                                if(socket.isConnected()){
                                    try {
                                        socket.close();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                                socket = null;
                            }
                            return false;
                        }
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object view) throws Exception {
                        ActivityUtils.toNextActivity(FetalHeartActivity.class);
//                        if(status){
//                            MyDialog1 dialog = new MyDialog1(context);
//                            dialog.setContent("yan");
//
//
//                        }else {
//
//                        }
                    }
                });

    }

}
