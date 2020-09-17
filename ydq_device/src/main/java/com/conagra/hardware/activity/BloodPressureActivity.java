package com.conagra.hardware.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.conagra.LocalConfig;
import com.conagra.R;
import com.conagra.databinding.ActivityBloodpressureBinding;
import com.conagra.di.component.BloodPressureComponent;
import com.conagra.di.component.DaggerBloodPressureComponent;
import com.conagra.di.module.BloodPressureModule;
import com.conagra.di.module.HardwareManagerModule;
import com.conagra.hardware.adapter.QuickeningAdapter;
import com.conagra.hardware.layout.BloodPressureLayout1;
import com.conagra.hardware.layout.BloodPressureLayout2;
import com.conagra.hardware.model.BloodPressureModel;
import com.conagra.hardware.widget.MyDialog1;
import com.conagra.iknetbluetoothlibrary.BluetoothManager;
import com.conagra.iknetbluetoothlibrary.MeasurementResult;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.model.BloodPressureListModel;
import com.conagra.mvp.presenter.BloodPressurePresenter;
import com.conagra.mvp.ui.activity.DragBaseFragmentActivity;
import com.conagra.mvp.ui.view.BloodPressureView;
import com.conagra.mvp.utils.CurrentServerManagerUtils;
import com.conagra.mvp.utils.PermissionUtil;
import com.conagra.mvp.utils.TimeUtils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class BloodPressureActivity extends DragBaseFragmentActivity<
        ActivityBloodpressureBinding, BloodPressurePresenter, BloodPressureComponent> implements BloodPressureView {


    private List<View> viewList;
    private BloodPressureLayout1 view1;
    private BloodPressureLayout2 view2;
    private QuickeningAdapter mAdapter;
    private BluetoothSocket socket;
    private String currentTime;
    private String mDeviceAddress;
    private boolean mConnected = false;
    private boolean isClosed;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean isExistBluetooth;
    private boolean isRecording;
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
    private String userId;
    private static final String TAG = BloodPressureActivity.class.getSimpleName();

    public boolean isRecording() {
        return isRecording;
    }

    private boolean isShowTest;
    private BluetoothManager.OnBTMeasureListener onBTMeasureListener = new BluetoothManager.OnBTMeasureListener() {

        @Override
        public void onRunning(String running) {
            //测量过程中的压力值
//            tv_heart.setText(running);
            if (!mConnected) {
                mLocalConfig.setBloodPressureDevices(mDeviceAddress);
                if (!isExistDevice) {
                    mPresenter.addDevice(mDeviceAddress);
                }
                mConnected = true;
                isRecording = true;
                view1.doConnectSuccess();
            }
        }

        @Override
        public void onPower(String power) {
            //测量前获取的电量值
//            setPower(power);
        }

        @Override
        public void onMeasureResult(MeasurementResult result) {
            isRecording = false;
            //测量结果
            view1.doSetText(result.getCheckShrink() + "", result.getCheckDiastole() + "", result.getCheckHeartRate() + "");
            view1.doReTest();
        }

        @Override
        public void onMeasureError() {
            isRecording = false;
            view1.doReTest();
//            bluetoothManager.startBTAffair(onBTMeasureListener);
            //测量错误
            Toast.makeText(BloodPressureActivity.this, "测量失败", Toast.LENGTH_SHORT).show();
//            bluetoothManager.stopMeasure();
        }

        @Override
        public void onFoundFinish(List<BluetoothDevice> deviceList) {
            //搜索结束，deviceList.size()如果为0，则没有搜索到设备
            if (deviceList.size() == 0) {
                Toast.makeText(BloodPressureActivity.this, "未搜索到设备", Toast.LENGTH_SHORT).show();
                view1.doDisConnected();
            }
        }

        @Override
        public void onDisconnected(BluetoothDevice device) {
            isRecording = false;
            mConnected = false;
            //断开连接
            view1.doDisConnected();
        }

        @Override
        public void onConnected(boolean isConnected, BluetoothDevice device) {
            doReTest();
        }
    };

    private boolean isExistDevice;

    @Override
    public void isExistDevice(boolean isExist, String deviceAddress) {
        isExistDevice = isExist;
        if (isExist || CurrentServerManagerUtils.isDesignCompany) {
            mDeviceAddress = deviceAddress;
            bluetoothManager.connectToBT(deviceAddress);
            view1.doConnecting();
        } else if (!CurrentServerManagerUtils.isDesignCompany) {
            Toast.makeText(this, "该设备未被注册，请联系您的家庭医生", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void addDeviceStatus(boolean success) {
        if (success) {
            isExistDevice = true;
            Toast.makeText(this, "设备注册成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "设备注册失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Inject
    LocalConfig mLocalConfig;
    private BluetoothManager bluetoothManager;

    public boolean ismConnected() {
        return mConnected;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_bloodpressure);
        setTitle("血压监测");
        initData();
        setBluetooth();
        doInit();
    }

    private void initData() {
        bluetoothManager = BluetoothManager.getInstance(this);
        bluetoothManager.initSDK();
    }

    /**
     * 设置蓝牙信息 ：如果蓝牙可用，则打开蓝牙； 如果蓝牙不可用，则进行提示
     */
    private void setBluetooth() {

        if (_bluetooth == null) {
            Toast.makeText(this, "本机没有找到蓝牙硬件或驱动！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!_bluetooth.isEnabled()) {
            //提醒用户打开蓝牙
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

    }

    public String getUserId() {
        return userId;
    }

    public boolean isExistBluetooth() {
        return isExistBluetooth;
    }

    public void doInit() {
        isExistBluetooth = true;
        userId = getIntent().getStringExtra("userId");
        mPresenter.setUserId(userId);
        mPresenter.registerView(this);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mDeviceAddress = mLocalConfig.getBloodPressureDevices();
        if (mBluetoothAdapter == null) {
            isExistBluetooth = false;
        } else if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
        viewList = new ArrayList<>();

        if (getIntent().hasExtra("data")) {
            currentTime = getIntent().getStringExtra("data");
        }
        viewList = new ArrayList<>();
        isShowTest = TextUtils.isEmpty(currentTime);
        view1 = new BloodPressureLayout1(this, new BaseListener.OnItemSelectedListener<BloodPressureModel>() {
            @Override
            public void onItemSelectedListener(BloodPressureModel value) {
                view2.doClear();
                view2.doRequestDate();
            }
        });
        viewList.add(view1);
        currentTime = TimeUtils.getYMDData(0);
        view2 = new BloodPressureLayout2(this, currentTime);
//            view1.doSetText("200","200","80");
//            doTest(true,44 +"",44 + "");
        setRightComponent(R.drawable.time_ico, (o) -> {
            Intent intent1 = new Intent(this, BloodPressureListActivity.class);
            intent1.putExtra("userId", userId);
            startActivity(intent1);
        });

        viewList.add(view2);
        mAdapter = new QuickeningAdapter(mBinding.bloodpressureRv, viewList);
        mBinding.bloodpressureRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.bloodpressureRv.setAdapter(mAdapter);
//        startConnectDevice(isShowTest);
        doConnectDevice();
    }

    public String getDeviceAddress() {
        return mDeviceAddress;
    }

    public void startConnectDevice(boolean isShowTest) {


        if (!TextUtils.isEmpty(mDeviceAddress) && isShowTest) {
            MyDialog1 dialog1 = new MyDialog1(this);
            dialog1.setDialogTitle("连接设备");
            dialog1.doCancleEvent("更换设备")
                    .subscribe((o) -> {
                        dialog1.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(this, BluetoothLeAccessActivity.class);
                        Bundle bundle = new Bundle();
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 10);
                    });
            dialog1.doSureEvent("直接连接")
                    .subscribe((o) -> {
                        dialog1.dismiss();
                        isExistDevice = true;
                        bluetoothManager.connectToBT(mDeviceAddress);
                        view1.doConnecting();
                    });
            dialog1.show();
        }
    }

    @Override
    public void onBackPressed() {
        doStop();
    }

    @Override
    public void isExist(BloodPressureModel model, boolean isEdit) {
        doShowDialog(model, isEdit);
    }

    @Override
    public void renderAllData(BloodPressureListModel model) {

        view2.renderAllData(model);
    }

    @Override
    public void createOrUpdateSuccess() {
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
        if (view2 != null) {
            view2.doRequestDate();
        }
    }

    public void requestHasData(BloodPressureModel model) {
        mPresenter.isExist(model);
    }

    public void requestCreate(BloodPressureModel model) {
        mPresenter.create(model);
    }

    public void requestGetAll(String startTime, String endTime) {
        mPresenter.GetALLList(startTime, endTime);
    }

    public void doShowDialog(BloodPressureModel model, boolean isEdit) {
        final MyDialog1 dialog1 = new MyDialog1(this);
        String message;
        if (isEdit) {
            message = "'血压'已有监测数据，是否覆盖已有的数据";
            dialog1.setmTvCancleTitle("放弃存储");
            dialog1.setmTvSureTitle("覆盖");

        } else {
            message = "是否保存数据";
            dialog1.setmTvCancleTitle("放弃存储");
            dialog1.setmTvSureTitle("保存");
        }

        dialog1.setContent(message);
        dialog1.doCancleEvent()
                .subscribe((o) -> {
                    dialog1.dismiss();
                });
        dialog1.doSureEvent()
                .subscribe((o) -> {
                    dialog1.dismiss();
                    mPresenter.create(model);
                });
        dialog1.show();
    }

    @Override
    protected void initializeInjector() {

        mComponent = DaggerBloodPressureComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .hardwareManagerModule(new HardwareManagerModule())
                .bloodPressureModule(new BloodPressureModule()).build();
        mComponent.inject(this);
    }

    /**
     * 停止监测
     */
    public void doStop() {


        if (view1 != null && (view1.getRulerViewBottom().getCurrentScale() != 0 || view1.getRulerViewTop().getCurrentScale() != 0)) {
            final MyDialog1 dialog1 = new MyDialog1(this);
            dialog1.setContent("您当前正在监测,确定要停止监测吗?");
            dialog1.doCancleEvent()
                    .subscribe((o) -> {
                        dialog1.dismiss();
                    });
            dialog1.doSureEvent()
                    .subscribe((o) -> {
                        dialog1.dismiss();
                        finish();
                    });
            dialog1.show();

        } else {
            finish();
        }

    }

//    public void doReConnection(boolean flag) {
//        if (flag) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (!mConnected) {
//                        doStopConnection();
//                        doConnectDevice(mDeviceAddress);
//                        doReConnection(isClosed);
//                        LogMessage.doLogMessage(TAG, "重新连接设备 : " + isClosed);
//                    }
//                }
//            }, 60 * 1000);
//        }
//    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public void doStopConnection() {
    }

    private OutputStream os;

    public void doConnectDevice() {
//        view1.doConnecting();
        doReTest();
        //蓝牙已经打开，开始搜索、连接和测量
//        bluetoothManager.startBTAffair(onBTMeasureListener);
//        if(!TextUtils.isEmpty(address)){
//            this.mDeviceAddress = address;
//        }
//        if(TextUtils.isEmpty(mDeviceAddress)){
//            Message message = Message.obtain();
//            message.what = 5;
//            mHandler.sendMessage(message);
//            return;
//        }
//
////        mDeviceAddress = TypeApi.BLUETOOTH_DEVICE_MAC_BLOOD_PRESSURE;
////        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
////        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
////        dialog.show();
////        //注册接收消息
////        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//        // 得到子类控件
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
//        try {
//            // Toast.makeText(BlueToothAccess.this, "链接成功",
//            // Toast.LENGTH_SHORT).show();
//            socket = device.createRfcommSocketToServiceRecord(UUID
//                    .fromString("00001101-0000-1000-8000-00805F9B34FB"));
//            socket.connect();
//            mConnected = true;
//            Message msg = new Message();
//            msg.what = 4;
//            mHandler.sendMessage(msg);
////                            if(requestDialog.isShowing()){
////                                requestDialog.dismiss();
////                            }
//
//            os = socket.getOutputStream();
//            // 启动接受数据
//          Thread  mreadThread = new readThread();
//            mreadThread.start();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            mConnected = false;
//            mDeviceAddress = null;
//            Toast.makeText(BloodPressureActivity.this,"连接失败，请选择的血压设备",Toast.LENGTH_SHORT).show();
//            Message msg = Message.obtain();
//            msg.what = 5;
//            mHandler.sendMessage(msg);
//        }


//        doReConnection(isClosed);
    }

    private final static int MaxShowNumber = 30;    //最大显示数据个数
    private final static int EarArrayLength = 38;    //耳温计数据个数


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        switch (resultCode) {
////            // 结果返回  已连接
////            case RESULT_OK:
////                //获取Bundle的数据
////                Bundle bl = data.getExtras();
////                mDeviceAddress = bl.getString("deviceaddress");
////                mLocalConfig.setBloodPressureDevices(mDeviceAddress);
////                doConnectDevice(mDeviceAddress);
////                setClosed(true);
////                break;
////            default:
////                break;
////        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (_bluetooth.isEnabled()) {
            _bluetooth.disable();
        }
        if (bluetoothManager != null && isShowTest) {
            bluetoothManager.stopMeasure();
            bluetoothManager.stopBTAffair();
        }

//        if(bluetoothManager.getBluetoothService() != null){
//            stopService(bluetoothManager.getBluetoothService());
//        }

        doStopConnection();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doStop();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * sdk会自动申请权限，如果失败则手动申请
     */
    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (isShowTest) {
            switch (requestCode) {
                case BluetoothManager.REQUEST_FINE_LOCATION:
                    //23以上版本蓝牙扫描需要定位权限(android.permission.ACCESS_COARSE_LOCATION)，此处判断是否获取成功
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // 获取权限成功
                        bluetoothManager.searchBluetooth();
                    } else {
                        // 获取权限失败
                        Toast.makeText(BloodPressureActivity.this, "权限获取失败", Toast.LENGTH_SHORT).show();
                        setPermissionApplyDialog();
                    }
                    break;
            }
        }

    }

    /**
     * 重新测量
     */
    public void doReTest() {
        if (bluetoothManager != null) {
            if (bluetoothManager.isConnectBT()) {
                bluetoothManager.startMeasure();
            } else {
                bluetoothManager.startBTAffair(onBTMeasureListener);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 10) {
            if (resultCode == RESULT_OK && intent != null && intent.getExtras() != null) {
                String deviceAddress = intent.getExtras().getString("deviceaddress");
                if (!TextUtils.isEmpty(deviceAddress)) {
                    mPresenter.isExistDevice(deviceAddress);
                }
            }
        } else if (requestCode == 1 && isShowTest) {
            if (resultCode == Activity.RESULT_OK) {
                view1.doConnecting();
                //蓝牙打开成功，开始搜索、连接和测量
                bluetoothManager.startBTAffair(onBTMeasureListener);
            } else {
                //蓝牙不能正常打开
                finish();
            }
        } else if (requestCode == REQUEST_CODE_PERMISSION_SETTING && isShowTest) {
            if (PermissionUtil.checkLocationPermission(this)) {
                bluetoothManager.searchBluetooth();
            } else {
                Toast.makeText(BloodPressureActivity.this, "权限获取失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * 权限申请
     */
    private void setPermissionApplyDialog() {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("蓝牙扫描需要定位权限。\n请点击“设置”-“权限管理”-“定位”打开所需权限。")
                    .setCancelable(false)
                    .setNegativeButton("拒绝",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                    .setPositiveButton("设置",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    startAppSettings();
                                }
                            }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static final int REQUEST_CODE_PERMISSION_SETTING = 102;

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE_PERMISSION_SETTING);
    }


    private void setPower(String power) {
        if (Integer.valueOf(power) > 3600) {
//            tv_turgoscope_power.setText("剩余电量：" + power);
        } else {
//            stopAnim();
//            tv_turgoscope_power.setText("血压计电量不足,请及时充电");
        }
    }

//    private void dealStopMeasureBtn() {
//        if (btn_stop_measure.getText().toString().equals(getResources().getString(R.string.stop_measurement))) {
//            tv_heart.setText("0");
//            stopAnim();
//            bluetoothManager.stopMeasure();
//            btn_stop_measure.setText(getResources().getString(R.string.re_test));
//
//        }else if(btn_stop_measure.getText().toString().equals(getResources().getString(R.string.re_test))){
//            tv_heart.setText("0");
//            startAnim();
//            btn_stop_measure.setText(getResources().getString(R.string.stop_measurement));
//            if(bluetoothManager.isConnectBT()){
//                bluetoothManager.startMeasure();
//            }else{
//                bluetoothManager.startBTAffair(onBTMeasureListener);
//            }
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//////			if (_bluetooth.isEnabled()) {
//////				_bluetooth.disable();
//////			}
//            bluetoothManager.stopMeasure();
//            finish();
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}
