package com.conagra.mvp.utils;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.conagra.hardware.adapter.BluetoothCustomAdapter;
import com.conagra.hardware.model.BluetoothModel;
import com.conagra.hardware.widget.BluetoothSearchDrawable;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.broadcast.BluetoothReciver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by yedongqin on 16/9/21.
 * 蓝牙工具
 */
public class BluetoothUtils {

    /** 用来存储搜索到的蓝牙设备 */
    private List<BluetoothModel> deviceList;
    private BluetoothCustomAdapter listAdapter;
    /** 获取蓝牙适配器 */
    private BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    private BluetoothReciver receiver;
    private boolean isSelected = false; //连接蓝牙
    private boolean isSupport;      //是否支持蓝牙
    private boolean isRegister;
    private boolean isExist;
    private Set<BluetoothModel> bluetoothDeviceSet;

//    private boolean isRegister;
    /**
     * 过滤器
     */
    private IntentFilter filter;
    public static BluetoothUtils instance = new BluetoothUtils();

    /**
     * 获取单例对象
     * @return
     */
    public static BluetoothUtils getInstance(){
        return instance;
    }

    public void setExist(boolean exist) {
        isExist = exist;
    }

    public void init(RecyclerView view, BaseListener.OnItemSelectedListener<BluetoothModel> bluetoothDevice, BluetoothSearchDrawable bluetoothSearchDrawable){
        mContext = view.getContext();
        deviceList = new ArrayList<>();
        bluetoothDeviceSet = new HashSet<>();
        listAdapter = new BluetoothCustomAdapter(view,deviceList,bluetoothDevice);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            isSupport = false;
            return;
        }

        isSupport = true;
        if(isSupport){
            if(!mBluetoothAdapter.isEnabled()){
                mBluetoothAdapter.enable();
            }
            regesiterBroadcastReceiver(bluetoothSearchDrawable);
        }
//        if(isSupport && !mBluetoothAdapter.isEnabled()){
//
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            // 设置蓝牙可见性，最多300秒
//            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//            context.startActivity(intent);
//
//        }
    }

    public boolean isDiscovering(){
        if(!isSupport){
            return false;
        }

        return mBluetoothAdapter.isDiscovering();
    }

    /**
     * 取消
     */
    public void cancelDiscovery(){
        if(!isSupport){
            return;
        }
        mBluetoothAdapter.cancelDiscovery();
    }

    public void connect(BluetoothDevice device) throws IOException {
        // 固定的UUID00001101-0000-1000-8000-00805F9B34FB
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";

        UUID uuid = UUID.fromString(SPP_UUID);
        BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
        socket.connect();
        socket.close();
    }

    /**
     * 注册广播接收器
     */
    public void regesiterBroadcastReceiver(final BluetoothSearchDrawable bluetoothSearchDrawable) {
        if(!isSupport){
            return;
        }
        isRegister = true;
        filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        if(isExist)
//            filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        receiver = new BluetoothReciver(mContext,new BluetoothReciver.OnBluetoothStatus() {
            @Override
            public void onFoundDevice(BluetoothModel bean) {

//                if(!TextUtils.isEmpty(bean.getDeviceAddress()) )
//                    synchronized (BluetoothUtils.this){
//                        if(flag && !TextUtils.isEmpty(bean.getDevName())){
//
//                            listAdapter.addItem(bean);
//                        }
//                    }


                if(bean != null && !TextUtils.isEmpty(bean.getDeviceAddress()) && !deviceList.contains(bean.getDeviceAddress().trim())) {

                    synchronized (deviceList) {
                        if (bean != null && !TextUtils.isEmpty(bean.getDeviceAddress())&& !deviceList.contains(bean.getDeviceAddress())) {
                            LogMessage.doLogMessage(BluetoothUtils.class.getSimpleName(), "deviceName : " + bean.getDeviceAddress());
                            listAdapter.add(bean);
                        }
                    }
                }
            }

            @Override
            public void onSelectedFinished() {
                isSelected = false;
                if(bluetoothSearchDrawable != null){
                    bluetoothSearchDrawable.doFinished();
                }

            }
        });
//        if(!isRegister){
            mContext.registerReceiver(receiver,filter);
//            isRegister = true;
//        }

    }

    /**
     * 启动搜索
     */
    public void startSearch(){
        if(!isSupport){
            return;
        }
        new Thread(){
            @Override
            public void run() {
                super.run();
                mBluetoothAdapter.startDiscovery();
            }
        }.start();
    }

    public boolean isSelected() {
        if(!isSupport){
            return false;
        }
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isRegister() {
        return isRegister;
    }

    public void unregisterReceiver(){
        if(!isSupport){
            return;
        }
        isRegister = false;
        bluetoothDeviceSet.clear();
        bluetoothDeviceSet = null;
        if(mBluetoothAdapter.isDiscovering())
             mBluetoothAdapter.cancelDiscovery();
        mContext.unregisterReceiver(receiver);
    }

    public BluetoothCustomAdapter getListAdapter(){
        if(!isSupport){
            return null;
        }
        return listAdapter;
    }
}
