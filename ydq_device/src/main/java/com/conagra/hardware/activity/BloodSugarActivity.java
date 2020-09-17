package com.conagra.hardware.activity;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.conagra.LocalConfig;
import com.conagra.R;
import com.conagra.databinding.ActivityBloodsugarBinding;
import com.conagra.di.component.BloodSugarComponent;
import com.conagra.di.component.DaggerBloodSugarComponent;
import com.conagra.di.module.BloodSugarModule;
import com.conagra.di.module.HardwareManagerModule;
import com.conagra.hardware.adapter.QuickeningAdapter;
import com.conagra.hardware.layout.BloodSugarLayout1;
import com.conagra.hardware.layout.BloodSugarLayout2;
import com.conagra.hardware.mcSdk.BluetoothLeService;
import com.conagra.hardware.mcSdk.EhongBLEGatt;
import com.conagra.hardware.mcSdk.EhongBLEScan;
import com.conagra.hardware.widget.MyDialog1;
import com.conagra.mvp.model.BloodSugarListModel;
import com.conagra.mvp.presenter.BloodSugarPresenter;
import com.conagra.mvp.ui.activity.DragBaseFragmentActivity;
import com.conagra.mvp.ui.view.BloodSugarView;
import com.conagra.mvp.utils.CurrentServerManagerUtils;
import com.conagra.mvp.utils.LogMessage;
import com.conagra.mvp.utils.PermissionUtil;
import com.conagra.mvp.utils.TimeUtils;
import com.xiaoye.myutils.utils.StringToHex;
import com.xiaoye.myutils.utils.ToastUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.conagra.mvp.utils.PermissionUtil.REQUEST_CODE_PERMISSION_SETTING;

/**
 * Created by yedongqin on 2016/10/27.
 */

public class BloodSugarActivity extends
        DragBaseFragmentActivity<ActivityBloodsugarBinding, BloodSugarPresenter, BloodSugarComponent> implements BloodSugarView {

    private List<View> viewList;
    private BloodSugarLayout1 view1;
    private BloodSugarLayout2 view2;
    private QuickeningAdapter mAdapter;
    private String currentTime;
    private String mDeviceAddress;
    private BluetoothLeService mBluetoothLeService;
    private boolean mConnected = false;
    private boolean isClosed;
    private static final String TAG = BloodSugarActivity.class.getSimpleName();
    private boolean isExistBluetooth;
    public static EhongBLEGatt EH_MC10 = new EhongBLEGatt();        ////add by appliction
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private String userId;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 100000;
    private static final int REQUEST_COARSE_LOCATION = 0;
    @Inject
    LocalConfig mLocalConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_bloodsugar);
        userId = getIntent().getStringExtra("userId");
        mPresenter.setUserId(userId);
        mPresenter.registerView(this);
        mHandler = new Handler();
        mDeviceAddress = mLocalConfig.getBloodSugarDevices();
        setTitle("血糖监测");
//        setBluetooth();
        doInit();
    }

    public void startConnectDevice() {
        if (TextUtils.isEmpty(mDeviceAddress)) {
            scanLeDevice(true);
        } else {
            MyDialog1 dialog1 = new MyDialog1(this);
            dialog1.setDialogTitle("连接设备");
            dialog1.doCancleEvent("更换设备")
                    .subscribe((o) -> {
                        dialog1.dismiss();
                        mDeviceAddress = "";
                        scanLeDevice(true);
                    });
            dialog1.doSureEvent("直接连接")
                    .subscribe((o) -> {
                        dialog1.dismiss();
                        doConnectDevice();
                    });
            dialog1.show();
        }
    }

    @Override
    protected void initializeInjector() {
        mComponent = DaggerBloodSugarComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .hardwareManagerModule(new HardwareManagerModule())
                .bloodSugarModule(new BloodSugarModule())
                .build();
        mComponent.inject(this);
    }

    // Device scan callback. api for bluetooth api
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {

            runOnUiThread(() -> {
                String srvAdvData = EhongBLEScan.byte2HexStr(scanRecord);

                if (EhongBLEScan.EH_FilterUUID_128(srvAdvData)) {
                    String deviceAddress = device.getAddress();
                    Log.i("EH_MC10_TAG", "the module ----- deviceAddress : " + deviceAddress + " mScanning : " + mScanning);
                    if (mScanning) {
                        scanLeDevice(false);
                    }
                    if (!TextUtils.isEmpty(deviceAddress) && TextUtils.isEmpty(mDeviceAddress)) {
                        mPresenter.isExistDevice(deviceAddress);
                    }
                }
            });

        }
    };

    private boolean isExistDevice;

    private void connectDevice() {
        connectedBle();
        if (!TextUtils.isEmpty(mDeviceAddress)) {
            doConnectDevice();
        } else {
            scanLeDevice(true);
        }
    }

    private void mayRequestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要 向用户解释，为什么要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))
                    Toast.makeText(this, "动态请求权限", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);
                return;
            } else {
                connectDevice();
            }
        } else {
            connectDevice();
        }

    }

    //系统方法,从requestPermissions()方法回调结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //确保是我们的请求
        if (requestCode == REQUEST_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限被授予", Toast.LENGTH_SHORT).show();
                connectDevice();
            } else if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "权限被拒绝,无法进行血糖测量", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void isExistDevice(boolean isExist, String deviceAddress) {
        isExistDevice = isExist;
        if (isExist || CurrentServerManagerUtils.isDesignCompany) {
            mDeviceAddress = deviceAddress;
            mLocalConfig.setBloodSugarDevices(deviceAddress);
            doConnectDevice();
        } else if (!isExist) {
//            isSearchSuccess = false;
            Toast.makeText(this, "该设备未被注册，请联系您的家庭医生", Toast.LENGTH_SHORT).show();
        }
    }

//    public void isExistDevice(boolean isExist, String deviceAddress) {
//        isExistDevice = isExist;
//        if(isExist || CurrentServerManagerUtils.isDesignCompany){
//            doConnect(deviceAddress);
//        }else if(!CurrentServerManagerUtils.isDesignCompany){
//            Toast.makeText(this,"该设备未被注册，请联系您的家庭医生",Toast.LENGTH_SHORT).show();
//        }
//    }

    private void connect(String deviceAddress) {
        mLocalConfig.setBloodSugarDevices(deviceAddress);
        doConnectDevice();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                view1.doConnecting();
                //蓝牙打开成功，开始搜索、连接和测量
//                bluetoothManager.startBTAffair(onBTMeasureListener);
            } else {
                //蓝牙不能正常打开
                finish();
            }
        } else if (requestCode == REQUEST_CODE_PERMISSION_SETTING) {
            if (PermissionUtil.checkLocationPermission(this)) {
//                bluetoothManager.searchBluetooth();
            } else {
                Toast.makeText(BloodSugarActivity.this, "权限获取失败", Toast.LENGTH_SHORT).show();
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

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE_PERMISSION_SETTING);
    }

//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // User chose not to enable Bluetooth.
//        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
//            finish();
//            return;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

//    static class DeviceScan {
//
//        /**
//         * check if BLE Supported device
//         */
//        public static boolean isBLESupported(Context context) {
//            return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
//        }
//
//        /**
//         * get BluetoothManager
//         */
//        public static BluetoothManager getManager(Context context) {
//            return (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
//        }
//    }

    public void connectedBle() {

        mBluetoothAdapter = EhongBLEScan.EH_GetBLE_Manager(this);

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            //提醒用户打开蓝牙
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

    }

    /**
     * 扫描设备
     */
    public void scanLeDevice(final boolean enable) {
        if (enable && !mScanning) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    view1.doConnectFail();
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
//            isSearchSuccess = false;
            view1.doScaning();
            boolean flag = mBluetoothAdapter.startLeScan(mLeScanCallback);
            Log.v("dfdfd", " flag : " + flag);
        } else {
            mScanning = false;
//            isSearchSuccess = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
//            mBluetoothAdapter.cancelDiscovery();
//            mBluetoothAdapter.disable();
//            mBluetoothAdapter = null;
        }
        invalidateOptionsMenu();
    }

    ///my data
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            if (!EH_MC10.EH_GATT_Init(BloodSugarActivity.this, service, mDeviceAddress)) {
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            EH_MC10.EH_GATT_Reinit();
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.i("EH_MC10_TAG", "the module ----- !" + action);
            if (EH_MC10.EH_GATT_IsConnectAction(action)) {
                mConnected = true;
                Log.i("EH_MC10_TAG", "the module ----- connected!");
                updateConnectionState();
                playVoice();        ///for test ppay voice
                invalidateOptionsMenu();

            } else if (EH_MC10.EH_GATT_IsDisconnectAction(action)) {
                mConnected = false;
                updateConnectionState();
                invalidateOptionsMenu();
                Log.v("bloodsugar1111result", " EH_GATT_IsDisconnectAction : " + action);
                clearUI();
            } else if (EH_MC10.EH_GATT_IsDiscoverService(action)) {
                if (!EH_MC10.EH_GATT_GetServiceAndCharacteristics()) {
                    //Toast.makeText(this, "Can't get service / characteristics", Toast.LENGTH_SHORT).show();
                }

            } else if (EH_MC10.EH_GATT_IsDataComeIn(action)) {
                ///display the data from ble device
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));

            } else if (EH_MC10.EH_GATT_IsDataReadBack(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };


    private void clearUI() {
        //mGattServicesList.setAdapter((SimpleExpandableListAdapter) null);
        //mDataField.setText(R.string.no_data);
    }

    private void playVoice() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }


    public void setClosed(boolean closed) {
        isClosed = closed;
    }


    public void doInit() {

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null && bundle.containsKey("data")) {
                currentTime = intent.getStringExtra("data");
            }
        }
        viewList = new ArrayList<>();
        view1 = new BloodSugarLayout1(this, (value) -> {
            view2.doClear();
            view2.doRequestDate(TimeUtils.getYMDData(0));
        });
        viewList.add(view1);
        currentTime = TimeUtils.getYMDData(0);
        view2 = new BloodSugarLayout2(this, currentTime);
        setRightComponent(R.drawable.time_ico, (v) -> {
                    Intent intent1 = new Intent(BloodSugarActivity.this, BloodSugarListActivity.class);
                    intent1.putExtra("userId", userId);
                    startActivity(intent1);
                }
        );
        mayRequestLocation();

        viewList.add(view2);
        mAdapter = new QuickeningAdapter(mBinding.bloodsugarRv, viewList);
        mBinding.bloodsugarRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.bloodsugarRv.setAdapter(mAdapter);
    }

    public boolean ismConnected() {
        return mConnected;
    }

    public BluetoothLeService getmBluetoothLeService() {
        return mBluetoothLeService;
    }

    private void updateConnectionState() {
        runOnUiThread(() -> {
            if (mConnected) {
                view1.doConnectSuccess();
                if (!isExistDevice && CurrentServerManagerUtils.isDesignCompany) {
                    getPresenter().addDevice(mDeviceAddress);
                }

            } else {
                view1.doConnectFail();
            }
        });
    }

    private boolean flag = false;
    private StringBuilder builder = new StringBuilder();
    private StringBuilder dataBuilder = new StringBuilder();
    private String mCRC = "";
    private String currentValue = "";

    private void displayData(String data) {

        if (data != null) {
            Log.v("bloodsugar1111result", "result : " + data);
            if (TextUtils.isEmpty(currentValue) || TextUtils.isEmpty(currentValue)) {
                currentValue = data;
            }

            String s = StringToHex.convertStringToHex(data);
            builder.append(s);
            dataBuilder.append(data);
            String crc = "";
            flag = s.endsWith("d") || s.endsWith("D");
            if (flag) {
                String hexSS = builder.toString();
                int start = hexSS.lastIndexOf("1e6") + 3;
                int end = hexSS.lastIndexOf("d");
                crc = hexSS.substring(start, end);
            }

            if (!TextUtils.isEmpty(crc) && !mCRC.equals(crc)) {
                String[] vs = dataBuilder.toString().split(" +");
                int i = 0;
                for (; i < vs.length; i++) {
                    if (vs[i].length() > 8) {
                        break;
                    }
                    i++;
                }

                float result = Float.parseFloat(vs[i + 1]);
                result = result / 18 + 0.05f;
                if (result < 0) {
                    result = 0.0f;
                } else if (result > 20.0f) {
                    result = 20.0f;
                }
                Log.v("bloodsugar1111result", "result : " + result);
                BigDecimal b = new BigDecimal(result);
                double f1 = b.setScale(1, BigDecimal.ROUND_FLOOR).doubleValue();
                view1.doSetText(String.valueOf(f1));
                mCRC = crc;

                String ss = "26445A20312006" + crc + "0D";
                byte[] send = ss.getBytes();
                EH_MC10.EH_GATT_SendData(send);

            }

            if (flag) {
                flag = false;
                currentValue = "";
                dataBuilder = new StringBuilder();
                builder = new StringBuilder();
            }
        }
    }

    private void displayData1(String data) {

        if (data != null) {
            Log.v("bloodsugar1111data", " data  : " + data);
            Log.v("bloodsugar1111data11", " data  : " + StringToHex.convertStringToHex(data));
//            stringBuilder.append(data);
//            for (String s : stringBuilder.toString().split(" +")) {
//                String ss = StringToHex.convertStringToHex(s);
//                if (ss.startsWith("6")) {
//                    Log.v("bloodsugar1111dataasc12", " asc  : " + ss);
//                }
//                Log.v("bloodsugar1111dataasc11", " asc  : " + StringToHex.convertStringToHex(s));
//            }
//            stringBuilder = new StringBuilder();
            if (!flag || builder.toString().length() < 30) {
                builder.append(data);
                String[] vs = builder.toString().split(" +");
                int index = 0;
                if (builder.toString().length() >= 24) {
                    for (String v : vs) {
                        if (v.length() > 8) {
                            flag = true;
                            break;
                        }
                        index++;
                    }
                }
                Log.v("bloodsugar1111result", "result111 : " + vs[index + 1]);
                if (flag) {
                    String v = vs[index + 1];
                    float result = Float.parseFloat(v);
                    result = result / 18 + 0.05f;
                    if (result < 0) {
                        result = 0.0f;
                    } else if (result > 15.0f) {
                        result = 15.0f;
                    }
                    Log.v("bloodsugar1111result", "result : " + result);
                    BigDecimal b = new BigDecimal(result);
                    double f1 = b.setScale(1, BigDecimal.ROUND_FLOOR).doubleValue();
                    view1.doSetText(String.valueOf(f1));
//                    String s = "0x260x440x310x200x310x200x060x320x370x380x380x340x0D";
                    String s = "0x26445A|203120|06【CRC 校验】|0D";

//                    String s = "0x26445A|203120|06【CRC 校验】|0D";
                    byte[] send = s.getBytes();
                    EH_MC10.EH_GATT_SendData(send);
                }
            }
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_READBACK);
        return intentFilter;
    }

    public void doReConnection(boolean flag) {
        if (flag) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!mConnected) {
//                        doStopConnection();
                        destory();
                        doConnectDevice();
                        doReConnection(isClosed);
                        LogMessage.doLogMessage(TAG, "重新连接设备 : " + isClosed);
                    }
                }
            }, 60 * 1000);
        }
    }

    public boolean isExistBluetooth() {
        return isExistBluetooth;
    }

    public void doStopConnection() {
        if (mBluetoothLeService != null) {
            unbindService(mServiceConnection);
        }
        mBluetoothLeService = null;
    }

    private boolean isStart;

    public void doConnectDevice() {

        destory();

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        view1.doConnecting();
        doReConnection(isClosed);
        isStart = true;
    }

    public void requestHasData(int timeSlot, String value) {
        mPresenter.isExist(timeSlot, value);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Initializes list view adapter.
//        scanLeDevice(true);
        if (!TextUtils.isEmpty(mDeviceAddress)) {
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
            if (mBluetoothLeService != null) {
                final boolean result = mBluetoothLeService.connect(mDeviceAddress);
                Log.d(TAG, "Connect request result=" + result);
            }

            if (EH_MC10.EH_GATT_ReturnLeService() != null) {
                final boolean result = EH_MC10.EH_GATT_Connect(mDeviceAddress);
            }
        }
//        else {
//            if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
//                if (!mBluetoothAdapter.isEnabled()) {
//                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//                }
//            }
//        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mScanning) {
            scanLeDevice(false);
        }
        if (!TextUtils.isEmpty(mDeviceAddress)) {
            unregisterReceiver(mGattUpdateReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destory();
    }

    private void destory() {
        if (!TextUtils.isEmpty(mDeviceAddress) && isStart) {
            isStart = false;
//            doStopConnection();
            if (mServiceConnection != null) {
                unbindService(mServiceConnection);
                EH_MC10.EH_GATT_Reinit();
            }
        }
    }

    /**
     * 停止监测
     */
    public void doStop() {

        if (view1 != null && view1.getmRulerView().getCurrentScale() != 0) {
            final MyDialog1 myDialog1 = new MyDialog1(this);
            myDialog1.setContent("您当前正在监测,确定要停止监测吗?");
            myDialog1.doSureEvent()
                    .subscribe((a) -> {
                        myDialog1.dismiss();
                        finish();
                    });
            myDialog1.doCancleEvent()
                    .subscribe((o) -> {
                        myDialog1.dismiss();
                    });
            myDialog1.show();
        } else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doStop();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void requestAllData(String time) {
        mPresenter.setTime(time);
        mPresenter.GetALLList();
    }

    @Override
    public void isExist(String bloodSugarId, String value, int timeSlot, boolean isEdit) {
        doShowDialog(bloodSugarId, value, timeSlot, isEdit);
    }

    @Override
    public void renderAllData(BloodSugarListModel model) {
        view2.renderAllData(model);
    }

    @Override
    public void createOrUpdateSuccess() {
        ToastUtils.getInstance().makeText(this, "添加成功");
        view2.setCurrentTime(TimeUtils.getYMDData(0));
        requestAllData(TimeUtils.getYMDData(0));
    }

    public void doShowDialog(String bloodSugarId, String value, int timeSlot, boolean isEdit) {
        final MyDialog1 dialog1 = new MyDialog1(this);
        String message = "";
        if (isEdit) {
            message = "'血糖'已有监测数据，是否覆盖已有的数据";
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
                    if (isEdit) {
                        mPresenter.edit(bloodSugarId, value);
                    } else {
                        mPresenter.create(value, timeSlot);
                    }
                });
        dialog1.show();
    }
}

