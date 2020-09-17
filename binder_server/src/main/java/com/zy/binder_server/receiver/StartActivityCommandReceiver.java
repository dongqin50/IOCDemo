package com.zy.binder_server.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class StartActivityCommandReceiver extends BroadcastReceiver {

    public final static String BROADCAST_START_ACTIVITY = "broadcast_start_activity";
    public final static String BROADCAST_DATA_EXTRA = "broadcast_data_extras";


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case BROADCAST_START_ACTIVITY:
                if(intent.hasExtra(BROADCAST_DATA_EXTRA)){

                    String className = intent.getStringExtra(BROADCAST_DATA_EXTRA);
                    if(!TextUtils.isEmpty(className)){
                        Intent newIntent = new Intent();
//                        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        ComponentName componentName = new ComponentName(context.getPackageName(),className);
                        newIntent.setComponent(componentName);
                        context.startActivity(newIntent);
                    }
                }
                break;
        }
    }

//    private void s(Context context){
//
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//
//        activityManager.geta
//    }
}
