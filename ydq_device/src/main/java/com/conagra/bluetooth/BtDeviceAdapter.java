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

import cn.com.heaton.blelibrary.spp.BtDevice;

/**
 *
 * Created by LiuLei on 2017/9/25.
 */

public class BtDeviceAdapter extends BaseAdapter{
    private ArrayList<BtDevice> mBtDevices;
    private LayoutInflater mInflator;

    public BtDeviceAdapter(Activity context) {
        super();
        mBtDevices = new ArrayList<BtDevice>();
        mInflator = context.getLayoutInflater();
    }

    public void addDevice(BtDevice device) {
        if (!mBtDevices.contains(device)) {
            mBtDevices.add(device);
        }
    }

    public BtDevice getDevice(int position) {
        if(position>=0&&position<mBtDevices.size()){
            return mBtDevices.get(position);
        }
       return null;
    }

    public void clear() {
        mBtDevices.clear();
    }

    @Override
    public int getCount() {
        return mBtDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mBtDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        BtDeviceAdapter.ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.item_icon11, null);
            viewHolder = new BtDeviceAdapter.ViewHolder();
            viewHolder.deviceAddress = (TextView) view.findViewById(R.id.icon11_tv_code);
            viewHolder.deviceName = (TextView) view.findViewById(R.id.icon11_tv);
//            viewHolder.deviceRSSI = (TextView) view.findViewById(R.id.device_RSSI);
//            viewHolder.deviceState = (TextView) view.findViewById(R.id.icon11_status);
            view.setTag(viewHolder);
        } else {
            viewHolder = (BtDeviceAdapter.ViewHolder) view.getTag();
        }

        final BtDevice device = mBtDevices.get(i);
        final String deviceName = device.getName();
        final String deviceRSSI = BluetoothDevice.EXTRA_RSSI;
        if(device.isConnecting()){
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
        viewHolder.deviceAddress.setText(device.getAddress());

        return view;
    }

    class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceRSSI;
        TextView deviceState;
    }
}
