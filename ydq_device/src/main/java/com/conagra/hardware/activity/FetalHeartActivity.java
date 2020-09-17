package com.conagra.hardware.activity;


import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.conagra.LocalConfig;
import com.conagra.R;
import com.conagra.databinding.ActivityFetalheartBinding;
import com.conagra.di.component.DaggerFealHeartComponent;
import com.conagra.di.component.FealHeartComponent;
import com.conagra.di.module.FealHeartModule;
import com.conagra.hardware.database.HeartRateDao;
import com.conagra.hardware.equipment.BluetoothEquipment;
import com.conagra.hardware.model.FetalHeartRecordModel;
import com.conagra.hardware.widget.MyDialog1;
import com.conagra.mvp.constant.TypeApi;
import com.conagra.mvp.model.FetalHeartModel;
import com.conagra.mvp.presenter.FealHeartPresenter;
import com.conagra.mvp.ui.activity.DragBaseAppCompatActivity;
import com.conagra.mvp.ui.view.FealHeartView;
import com.conagra.mvp.utils.CurrentServerManagerUtils;
import com.conagra.mvp.utils.LogMessage;
import com.conagra.mvp.utils.TimeUtils;
import com.xiaoye.myutils.utils.ToastUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import rx.Subscriber;

/**
 * Created by yedongqin on 16/9/12.
 *
 * 胎心
 */
public class FetalHeartActivity extends DragBaseAppCompatActivity<
        ActivityFetalheartBinding,FealHeartPresenter,FealHeartComponent> implements FealHeartView {

    private int countBpm;
    private int timeBpm;
    private String result;
    private String gysResult;
    private FetalHeartRecordModel heartBean;        //胎动记录本
    private OnServiceConnect onServiceConnect;
    private String mPiont1String="";
    private String mPiont2String="";
    private boolean isRecording;
    private boolean mConnected = false;
    private String mDeviceAddress;
    private static final int STOP_STATUS_BACK  = 0;
    private static final int STOP_STATUS_NORMAL  = 1;
    private static final int STOP_STATUS_FORCE = 2;
//    private IntentFilter intentFilter;
//    private BroadcastReceiver broadcastReceiver;
    private String userId;

    @Inject
    LocalConfig mLocalConfig;

    public class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    mConnected = true;
//                    Toast.makeText(FetalHeartActivity.this," 连接成功",0).show();
                    mBinding.fetalheartBluetooth.setVisibility(View.GONE);

                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    Log.v("dd21","ACTION_ACL_DISCONNECTED");
                    mConnected = false;
                    mBinding.fetalheartBluetooth.setVisibility(View.VISIBLE);
                    doStop(TypeApi.STOP_STATUS_FORCE);
                    break;
            }
        }
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_fetalheart);
        setTitle("胎心");
        doInit();
    }

    @Override
    protected void initializeInjector() {
        mComponent = DaggerFealHeartComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .fealHeartModule(new FealHeartModule()).build();
        mComponent.inject(this);
    }


    public void requestGetList(){

    }


    @Override
    public void createOrUpdateSuccess(FetalHeartModel model) {
        HeartRateDao dao = new HeartRateDao(this);
        heartBean.setServerpath(model.getAudioURL());
        heartBean.setAudiofsUrl(model.getAudioURL());
        heartBean.setDownloadUrl(model.getAudioURL());
        heartBean.setTimes(model.getTimes());
        heartBean.setUploadStatus(1);
        int id = dao.add(heartBean);
        if(id > 0){
            ToastUtils.getInstance().makeText(this,"上传成功");
        }
    }

    public void doInit() {
        userId = getIntent().getStringExtra("userId");
        mPresenter.setUserId(userId);
        mPresenter.registerView(this);
        mDeviceAddress = mLocalConfig.getFetalHeartDevices();
        setRightComponent(R.drawable.time_ico, (v) -> {
                Intent intent = new Intent(FetalHeartActivity.this, HeartListActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
        });
        //开始监测
        mBinding.fetalheartStartJcbt.setOnClickListener((v) -> {
                        if(mConnected){
                            doStart();
                        }else {
                            ToastUtils.getInstance().makeText(FetalHeartActivity.this,"请连接您的设备");
                        }
                });

        mBinding.feltalheartEndJcBt.setOnClickListener((v) -> {
                        doStop(STOP_STATUS_FORCE);
                });

        mBinding.feltalheartEndTd.setOnClickListener((v) -> {
                        int num = Integer.parseInt(mBinding.feltalheartEndTd.getText().toString()) + 1;
                        if(mConnected){
                            //设置胎动
                            onServiceConnect.getBluetoothEquipment().doSetFM();
                        }
                        mBinding.feltalheartEndTd.setText(String.valueOf(num));
                        mBinding.feltalheartTvTd.setText(String.valueOf(num));
                });
        mBinding.fetalheartBluetooth.setOnClickListener((v) -> {
                    onConnectClick();
                });
        onServiceConnect = new OnServiceConnect();
        Intent intent = new Intent(FetalHeartActivity.this, BluetoothEquipment.class);
        bindService(intent, onServiceConnect, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onBackPressed() {
        doStop(STOP_STATUS_BACK);
    }
    private boolean isExistDevice;
    @Override
    public void isExistDevice(boolean isExist, String deviceAddress) {
        isExistDevice = isExist;
        if(isExist || CurrentServerManagerUtils.isDesignCompany){
            mLocalConfig.setFetalHeartDevices(deviceAddress);
            onServiceConnect.doConnect(deviceAddress);
        }else if(!CurrentServerManagerUtils.isDesignCompany){
            Toast.makeText(this,"该设备未被注册，请联系您的家庭医生",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void addDeviceStatus(boolean success) {
        if(success){
            isExistDevice = true;
            Toast.makeText(this,"设备注册成功",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"设备注册失败",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                if(data != null){
//                    broadcastReceiver = new MyBroadcastReceiver();
//                    intentFilter = new IntentFilter();
//                    intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//                    intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
////                    intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//                    registerReceiver(broadcastReceiver,intentFilter);
                    Bundle bundle = data.getExtras();
                    if(bundle != null){
                        String deviceaddress = bundle.getString("deviceaddress");
                        if(!TextUtils.isEmpty(deviceaddress)){
                            mPresenter.isExistDevice(deviceaddress);
                        }

                    }
                }
                break;
        }
    }

    public void onConnectClick(){
        if(TextUtils.isEmpty(mDeviceAddress)){
            Intent intent = new Intent();
            intent.setClass(this, BluetoothLeAccessActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            startActivityForResult(intent,0);
        }else {
            MyDialog1 dialog1 = new MyDialog1(this);
            dialog1.setDialogTitle("设备");
            dialog1.doCancleEvent("更换")
                    .subscribe((v)->{
                        dialog1.dismiss();
                        mDeviceAddress = null;
                        onConnectClick();
                    },(e)->{

                    });
            dialog1.doSureEvent("连接")
                    .subscribe((v)->{
                        dialog1.dismiss();
                        onServiceConnect.doConnect(mDeviceAddress);
                    },(e)->{

                    });
            dialog1.show();
        }
    }


    /**
     * 开始监测
     */
    private void doStart() {
        heartBean = new FetalHeartRecordModel();
        isRecording = true;
        result = "0";
        gysResult = "0";

        mBinding.feltalheartStartJcrl.setVisibility(View.GONE);
        mBinding.feltalheartEndJcRl.setVisibility(View.VISIBLE);
        if (mConnected) {
            onServiceConnect.doReading();
        } else {
            ToastUtils.getInstance().makeText(FetalHeartActivity.this,"请连接您的设备");
            return;
        }
        heartBean.setStartTime(TimeUtils.getCurrentTime());
        mBinding.fetalheartFv.doStart();  //开始动画
        //获取BPM值
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .take(60*60)
                .takeWhile(new Predicate<Long>() {
                    @Override
                    public boolean test(Long aLong) throws Exception {
                        return aLong < 30 * 60 && mBinding.fetalheartFv.isStart();
                    }
                })
//                .filter(new Func1<Long, Boolean>() {
//                    @Override
//                    public Boolean call(Long aLong) {
//                        return mRecordVedioUtils.isShoting();
//                    }
//                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (aLong == 30 * 60) {
                            mBinding.fetalheartFv.doStop();
//                            mRecordVedioUtils.doStopShot();
                            onServiceConnect.getBluetoothEquipment().doStopRecord();
                            doStop(STOP_STATUS_NORMAL);
                            return;
                        }

                        onServiceConnect.doReadData();
//                                       mTvTime.setText();
                        int min = Math.round(aLong / 60);
                        int sec = Math.round(aLong % 60);
                        String time = "";
                        if (min < 10) {
                            time = "0" + min;
                        } else {
                            time = "" + min;
                        }

                        if (sec < 10) {
                            time += ":0" + sec;
                        } else {
                            time += ":" + sec;
                        }
                        mBinding.feltalheartTvTime.setText(time);
                        if (!TextUtils.isEmpty(result)) {

                            mBinding.feltalheartTvBpm.setText(result);
                        } else {
                            mBinding.feltalheartTvBpm.setText("0");
                            result = "0";
                        }

                        countBpm += Integer.valueOf(result);
                        timeBpm++;

                        if (!TextUtils.isEmpty(gysResult)) {
                            mBinding.feltalheartTvGsy.setText(gysResult);
                        } else {
                            mBinding.feltalheartTvGsy.setText("0");
                            gysResult = "0";
                        }
                        mBinding.fetalheartFv.addValue(result, gysResult);
                        mPiont1String+= (result + ",");
                        mPiont2String+= (gysResult + ",");
                        LogMessage.doLogMessage(FetalHeartActivity.class.getSimpleName(), " result : " + result);

                    }
                });
    }

    /**
     * 停止监测
     */
    private void doStop(int index){

        switch (index){
            case  STOP_STATUS_BACK:
                if(mBinding.fetalheartFv.isStart()){
                    final MyDialog1 myDialog1 = new MyDialog1(FetalHeartActivity.this);
                    myDialog1.setContent("您当前正在录制,确定要停止录制并保存吗?");
                    myDialog1.doSureEvent()
                            .subscribe(new Consumer() {
                                @Override
                                public void accept(Object o) throws Exception {

                                    myDialog1.dismiss();
                                    isRecording = false;
                                    stop();
                                    finish();
                                }
                            });
                    myDialog1.doCancleEvent()
                            .subscribe(new Consumer() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    myDialog1.dismiss();
                                }
                            });
                    myDialog1.show();
                }else {
                    finish();
                }
                break;
            case STOP_STATUS_FORCE:
                if(mBinding.fetalheartFv.isStart()){
                    final MyDialog1 myDialog1 = new MyDialog1(FetalHeartActivity.this);
                    myDialog1.setContent("您当前正在录制,确定要停止录制并保存吗?");
                    myDialog1.doSureEvent()
                            .subscribe(new Consumer() {
                                @Override
                                public void accept(Object o) throws Exception {

                                    myDialog1.dismiss();
                                    isRecording = false;
                                    stop();
                                }
                            });
                    myDialog1.doCancleEvent()
                            .subscribe(new Consumer() {
                                @Override
                                public void accept(Object o) throws Exception {
                                    myDialog1.dismiss();
                                }
                            });
                    myDialog1.show();
                }

                break;
            case STOP_STATUS_NORMAL:
                if(mBinding.fetalheartFv.isStart()){
                    isRecording = false;
                    stop();
                }
                break;
        }
    }

    private void stop(){
        if (mBinding.fetalheartFv.isStart()) {

            mBinding.fetalheartFv.doStop();
            mBinding.feltalheartTvTime.setText("00:00");
            mBinding.feltalheartTvBpm.setText("--");
            mBinding.feltalheartStartJcrl.setVisibility(View.VISIBLE);
            mBinding.feltalheartEndJcRl.setVisibility(View.GONE);
            mBinding.feltalheartEndTd.setText("0");
            mBinding.feltalheartTvTd.setText("0");
            countBpm = 0;
            if(onServiceConnect.getBluetoothEquipment() != null){
                heartBean.setAudiofs(onServiceConnect.getBluetoothEquipment().getPath());
                heartBean.setFileName(onServiceConnect.getBluetoothEquipment().getFileName());
                onServiceConnect.getBluetoothEquipment().doStopRecord();
            }
            onServiceConnect.doStopReading();
//          heartBean.setCount(mBinding.feltalheartTvTd.getText().toString());
            if (timeBpm == 0){
                heartBean.setAvgHeartRate(Integer.valueOf("0"));
            }else {
                heartBean.setAvgHeartRate(Integer.valueOf(countBpm/timeBpm));
            }
            heartBean.setTimes(TimeUtils.intervalTimeByMs(heartBean.getStartTime(), TimeUtils.getTimeByDate(System.currentTimeMillis())));
//            heartBean.setAudiofs(onServiceConnect.getPath());
//            heartBean.setAudiofs(Environment.getExternalStorageState() + "/feldsher/feltalHeart/"+System.currentTimeMillis()+".wav");
//            heartBean.setCustomerNo(CacheUtils.getmUserModel().getCustomerNo());
//            heartBean.setHospitalID(mHosptialBean.getValue1(0));
            heartBean.setPionts1(mPiont1String.substring(0,mPiont1String.length()-1));
            heartBean.setPionts2(mPiont2String.substring(0,mPiont2String.length()-1));


            if(!TextUtils.isEmpty(heartBean.getAudiofs())){
                 File file = new File(heartBean.getAudiofs());
                if(file.exists()){
                    mPresenter.create(heartBean);
                }
            }
        }
    }

    public class OnServiceConnect implements ServiceConnection {

        private BluetoothEquipment bluetoothEquipment;

        public BluetoothEquipment getBluetoothEquipment() {
            return bluetoothEquipment;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(service != null && service instanceof BluetoothEquipment.BluetoothBinder){
                bluetoothEquipment = ((BluetoothEquipment.BluetoothBinder) service).getBluetoothEquipment();
//            if(!TextUtils.isEmpty(mDeviceAddress))
//                 doConnect(mDeviceAddress);
            }

        }

        public void doConnect(String deviceAddress){

            if(bluetoothEquipment == null )
                return;
            bluetoothEquipment.doConnectEquipment(deviceAddress, new Subscriber<String>() {
                @Override
                public void onCompleted() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mConnected = true;
                            mBinding.fetalheartBluetooth.setVisibility(View.GONE);
                            mLocalConfig.setFetalHeartDevices(mDeviceAddress);
                            if(!isExistDevice&& CurrentServerManagerUtils.isDesignCompany){
                                mPresenter.addDevice(mDeviceAddress);
                            }
                        }
                    });

                }
                @Override
                public void onError(Throwable e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mConnected = false;
                            mBinding.fetalheartBluetooth.setVisibility(View.VISIBLE);
                            doStop(TypeApi.STOP_STATUS_FORCE);
                        }
                    });
                }

                @Override
                public void onNext(String  address) {
                    mDeviceAddress = address;
                }
            });
        }

        /**
         * 开始启动
         */
        public void doReading(){

            if(bluetoothEquipment != null && mConnected){
                bluetoothEquipment.doStartRecord();
            }
        }
        public void  doReadData(){
            if(bluetoothEquipment != null &&mConnected){
                result = bluetoothEquipment.doReadData();
                if(!TextUtils.isEmpty(result) && result.contains(":")){
                    String[] results = result.split(":");
                    FetalHeartActivity.this.result = results[0];
                    FetalHeartActivity.this.gysResult = results[1];
                }
            }
        }

        public String getPath(){
            return bluetoothEquipment.getPath();
        }

        public void doStopReading(){
            bluetoothEquipment.doStopReading();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            if(null != bluetoothEquipment){
                bluetoothEquipment.onDestroy();
                bluetoothEquipment = null;
            }
        }
    }

//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        if(requestCode == RecordVedioUtils.REQUEST_CODE) {
////            mRecordVedioUtils.doSaveVideo(resultCode,data);
////        }
//        if(requestCode == RecordVedioUtils.REQUEST_SETTING_CODE) {
//            if(Settings.System.canWrite(this)) {
//                //检查返回结果
//                Toast.makeText(this, "OK",
//                        Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "没有修改系统设置的权限",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(onServiceConnect != null && onServiceConnect.getBluetoothEquipment() != null){
            onServiceConnect.getBluetoothEquipment().onDestroy();
            unbindService(onServiceConnect);
        }


//        if(broadcastReceiver != null){
//            unregisterReceiver(broadcastReceiver);
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case  KeyEvent.KEYCODE_BACK:
                doStop(STOP_STATUS_BACK);
                break;
        }
        return true;
    }
}
