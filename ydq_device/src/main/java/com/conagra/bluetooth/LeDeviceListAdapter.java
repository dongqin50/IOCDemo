package com.conagra.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.conagra.R;

import java.util.ArrayList;
import java.util.List;

import cn.com.heaton.blelibrary.ble.BleDevice;

/**
 *
 * Created by LiuLei on 2016/11/26.
 */


public class LeDeviceListAdapter extends BaseAdapter {
    private ArrayList<BleDevice> mLeDevices;
    private LayoutInflater mInflator;

    public LeDeviceListAdapter(Activity context) {
        super();
        mLeDevices = new ArrayList<>();
        mInflator = context.getLayoutInflater();
    }

    public void addDevice(BleDevice device) {
        if (!mLeDevices.contains(device)) {
            mLeDevices.add(device);
        }
    }


    public void addDevices(List<BleDevice> devices){
        for(BleDevice device : devices){
            if(!mLeDevices.contains(device)){
                mLeDevices.add(device);
            }
        }
    }

    public BleDevice getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.item_icon11, null);
            viewHolder = new ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.icon11_tv_code);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.icon11_tv);
//            viewHolder.deviceRSSI = (TextView) view.findViewById(R.id.device_RSSI);
//            viewHolder.deviceState = (TextView) view.findViewById(R.id.icon11_status);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final BleDevice device = mLeDevices.get(i);
        final String deviceName = device.getBleName();
        final String deviceRSSI = BluetoothDevice.EXTRA_RSSI;
        if(device.isConnectting()){
            viewHolder.deviceState.setText("正在连接中...");
        }
//        else if(device.isConnected()){
//            viewHolder.deviceState.setText("已连接");
//        }else {
//            viewHolder.deviceState.setText("未连接");
//        }
        if (deviceName != null && deviceName.length() > 0)
            viewHolder.deviceName.setText(deviceName);
//        else if (deviceRSSI != null && deviceRSSI.length() > 0)
//            viewHolder.deviceName.setText(deviceRSSI);
        else
            viewHolder.deviceName.setText(R.string.unknown_device);
        viewHolder.deviceAddress.setText(device.getBleAddress());

        return view;
    }

    class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceRSSI;
        TextView deviceState;
    }

}