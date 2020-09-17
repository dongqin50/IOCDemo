package com.conagra.mvp.broadcast;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.conagra.mvp.BaseListener;
import com.conagra.mvp.service.BluetoothLeService;


/**
 * Created by yedongqin on 2016/11/3.
 */

public class GattUpdateReceiver extends BroadcastReceiver {


    private static final String TAG = "GattUpdateReceiver";
    private BaseListener.Observable111<Integer,Intent> observable;

    public GattUpdateReceiver(BaseListener.Observable111<Integer, Intent> observable) {
        this.observable = observable;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
            observable.onAction(0);
            Log.d(TAG, "ACTION_GATT_CONNECTED");
        } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
            observable.onAction(2);
            Log.d(TAG, "ACTION_GATT_DISCONNECTED");
        } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            observable.onAction(1);
        } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
            String state = intent.getStringExtra("State");
            if (state != null) {
                if (state.equals("OK") && observable != null) {
                    observable.onAction1(intent);

                    //测量
                } else if (state.equals("Measuring")) {

//                        StartGif();
                }  else if (state.equals("Stop")) {
                    observable.onAction(3);
//                        StopGif();
                } else if (state.equals("Error")) {
                    observable.onAction(4);
                    Toast.makeText(context, "测量错误",	Toast.LENGTH_SHORT).show();
                } else if (state.equals("BatteryLow")) {
                    observable.onAction(5);
                    Toast.makeText(context, "低电量,请及时充电", Toast.LENGTH_SHORT).show();
                }
            }

            if (intent.getStringExtra(BluetoothLeService.EXTRA_DATA) != null) {
                Log.i(TAG, "EXTRA_DATA : " + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
            if (intent.getStringExtra("State") != null) {
                Log.i(TAG, "State : " + intent.getStringExtra("State"));
            }
        }else if(BluetoothLeService.ACTION_DATA_NOTIFY.equals(action)){
            observable.onAction1(intent);
        }else if(BluetoothLeService.ACTION_DATA_READBACK.equals(action)){
            observable.onAction1(intent);
        }
    }
}
