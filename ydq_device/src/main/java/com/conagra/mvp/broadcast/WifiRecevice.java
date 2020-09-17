package com.conagra.mvp.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import com.conagra.mvp.utils.ActivityManageUtils;


/**
 * Created by yedongqin on 16/10/6.
 */
public class WifiRecevice extends BroadcastReceiver {

//    private static final String  WIFI_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";


//    public interface OnWifiStateListener{
//
//        int WIFI_STATE_INIT = 1;
//        int WIFI_STATE_CONNECTED = 2;
//        int WIFI_STATE_DISCONNECTED = 3;
//
//        void onWifiState(int state);
//    }
//
//    private OnWifiStateListener wifiStateListener;
//
//    public WifiRecevice(OnWifiStateListener listener) {
//        this.wifiStateListener = listener;
//        if(wifiStateListener != null){
//            wifiStateListener.onWifiState(OnWifiStateListener.WIFI_STATE_INIT);
//        }
//    }

    public WifiRecevice() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

//        String action = intent.getAction();

        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        State stateWifi = conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        State stateMobile = conn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
//        网络已连接
        if(stateWifi == State.CONNECTED || stateMobile == State.CONNECTED){
//            if(wifiStateListener != null){
//                wifiStateListener.onWifiState(OnWifiStateListener.WIFI_STATE_CONNECTED);
//            }

//        网络断开
        }else if(stateWifi == State.DISCONNECTED || stateMobile == State.DISCONNECTED){
            if(ActivityManageUtils.getInstance().getLength() > 0){
//                ActivityUtils.showInternetError(ActivityManageUtils.getInstance().getCurrentContext());
            }
//            if(wifiStateListener != null){
//                wifiStateListener.onWifiState(OnWifiStateListener.WIFI_STATE_DISCONNECTED);
//            }
        }
    }
}
