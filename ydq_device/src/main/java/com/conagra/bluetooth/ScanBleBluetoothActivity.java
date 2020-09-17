package com.conagra.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.conagra.R;
import com.conagra.bluetooth.command.Command;
import com.conagra.bluetooth.utils.FileUtils;
import com.conagra.bluetooth.utils.SPUtils;
import com.conagra.bluetooth.utils.ToastUtil;
import com.conagra.databinding.ActivityScanBluetoothBinding;
import com.conagra.mvp.ui.activity.BaseFragmentActivity;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import cn.com.heaton.blelibrary.ble.Ble;
import cn.com.heaton.blelibrary.ble.BleDevice;
import cn.com.heaton.blelibrary.ble.callback.BleConnCallback;
import cn.com.heaton.blelibrary.ble.callback.BleNotiftCallback;
import cn.com.heaton.blelibrary.ble.callback.BleReadCallback;
import cn.com.heaton.blelibrary.ble.callback.BleScanCallback;
import cn.com.heaton.blelibrary.ble.callback.BleWriteCallback;

/**
 * Created by yedongqin on 2018/6/10.
 */

public class ScanBleBluetoothActivity extends BaseFragmentActivity<ActivityScanBluetoothBinding> implements View.OnClickListener, AdapterView.OnItemClickListener{

    private ActivityScanBluetoothBinding mBinding;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private Ble<BleDevice> mBle;
    private String path;
    public static final  String TAG = "ScanBleBlu";
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan_bluetooth);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_scan_bluetooth);
        setTitle("蓝牙搜索");
        onInitView();
        mBinding.accessBtScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reScan();
            }
        });

    }

    protected void onInitView() {
        initView();
        mBle = Ble.getInstance();
        //检测蓝牙是否支持BLE以及是否打开
        checkBle();
    }

    //初始化蓝牙
    private void initBle() {
        Ble.Options options = new Ble.Options();
        options.logBleExceptions = true;//设置是否输出打印蓝牙日志
        options.throwBleException = true;//设置是否抛出蓝牙异常
        options.autoConnect = false;//设置是否自动连接
        options.scanPeriod = 12 * 1000;//设置扫描时长
        options.connectTimeout = 10 * 1000;//设置连接超时时长
        options.uuid_service = UUID.fromString("0000fee9-0000-1000-8000-00805f9b34fb");//设置主服务的uuid
//        options.uuid_services_extra = new UUID[]{UUID.fromString("0000180f-0000-1000-8000-00805f9b34fb")};//添加额外的服务（如电量服务，心跳服务等）
        options.uuid_write_cha = UUID.fromString("d44bc439-abfd-45a2-b575-925416129600");//设置可写特征的uuid
//        options.uuid_read_cha = UUID.fromString("d44bc439-abfd-45a2-b575-925416129601");//设置可读特征的uuid
        //ota相关 修改为你们自己的
       /* options.uuid_ota_service = UUID.fromString("0000fee8-0000-1000-8000-00805f9b34fb");
        options.uuid_ota_notify_cha = UUID.fromString("003784cf-f7e3-55b4-6c4c-9fd140100a16");
        options.uuid_ota_write_cha = UUID.fromString("013784cf-f7e3-55b4-6c4c-9fd140100a16");*/
        mBle.init(getApplicationContext(), options);
        //开始扫描
        mBle.startScan(scanCallback);
    }

    //检查蓝牙是否支持及打开
    private void checkBle() {
        // 检查设备是否支持BLE4.0
        if (!mBle.isSupportBle(this)) {
            ToastUtil.showToast(R.string.ble_not_supported);
            finish();
            return;
        }
        if (!mBle.isBleEnable()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, Ble.REQUEST_ENABLE_BT);
        }else {
            //请求权限
            requestPermission();
        }
    }

    //请求权限
    private void requestPermission(){
        requestPermission(new String[]{Manifest.permission.BLUETOOTH_ADMIN,Manifest.permission.ACCESS_COARSE_LOCATION},
                "请求蓝牙相关权限", new GrantedResult() {
                    @Override
                    public void onResult(boolean granted) {
                        if (granted) {
                            //初始化蓝牙
                            initBle();
                        } else {
                            finish();
                        }
                    }
                });
    }

    
    private void initView() {
        mLeDeviceListAdapter = new LeDeviceListAdapter(this);
        mBinding.accessWppRv.setOnItemClickListener(this);
        mBinding.accessWppRv.setAdapter(mLeDeviceListAdapter);
    }

    @Override
    public void onClick(View v) {
        List<BleDevice> list = mBle.getConnetedDevices();
        if (list.size() == 0) {
            ToastUtil.showToast("请先连接设备");
            return;
        }
//        switch (v.getId()) {
//            case R.id.test:
//                if (mBle.isScanning()) {
//                    mBle.stopScan();
//                }
////                startActivity(new Intent(this, TestActivity.class));
//                break;
//            case R.id.readRssi:
//                mBle.readRssi(mBle.getConnetedDevices().get(0), new BleReadRssiCallback<BleDevice>() {
//                    @Override
//                    public void onReadRssiSuccess(int rssi) {
//                        super.onReadRssiSuccess(rssi);
//                        Log.e(TAG, "onReadRssiSuccess: " + rssi);
//                        ToastUtil.showToast("读取远程RSSI成功："+rssi);
//                    }
//                });
//                break;
//            case R.id.sendData:
//                synchronized (mBle.getLocker()) {
//                    for (BleDevice device : list) {
//                        sendData(device);
//                    }
//                }
//                break;
//            case R.id.updateOta:
//                //此处为了方便把OTA升级文件直接放到assets文件夹下，拷贝到/aceDownload/文件夹中  以便使用
//                requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        "读写SD卡相关权限", new GrantedResult() {
//                            @Override
//                            public void onResult(boolean granted) {
//                                if (granted) {
//                                    CopyAssetsToSD();
//                                } else {
//                                    ToastUtil.showToast("读写SD卡权限被拒绝,将会影响OTA升级功能哦!");
//                                }
//                            }
//                        });
//                File file = new File(path + StaticValue.OTA_NEW_PATH);
//                OtaManager mOtaManager = new OtaManager(this);
//                boolean result = mOtaManager.startOtaUpdate(file, (BleDevice) mBle.getConnetedDevices().get(0), mBle);
//                Log.e("OTA升级结果:", result + "");
//                break;
//            case R.id.requestMtu:
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//                    //此处第二个参数  不是特定的   比如你也可以设置500   但是如果设备不支持500个字节则会返回最大支持数
//                    mBle.setMTU(mBle.getConnetedDevices().get(0).getBleAddress(), 250, new BleMtuCallback<BleDevice>() {
//                        @Override
//                        public void onMtuChanged(BleDevice device, int mtu, int status) {
//                            super.onMtuChanged(device, mtu, status);
//                            ToastUtil.showToast("最大支持MTU："+mtu);
//                        }
//                    });
//                }else {
//                    ToastUtil.showToast("设备不支持MTU");
//                }
//                break;
//        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final BleDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;
        if (mBle.isScanning()) {
            mBle.stopScan();
        }
        if (device.isConnected()) {
            mBle.disconnect(device);
        } else if (!device.isConnectting()) {
//            mBle.connect(device, connectCallback); //也可以
            mBle.connect(device.getBleAddress(), connectCallback);//新添加通过mac地址进行连接
        }
    }

    /*发送数据*/
    public void sendData(BleDevice device) {
        boolean result = mBle.write(device, changeLevelInner(1), new BleWriteCallback<BleDevice>() {
            @Override
            public void onWriteSuccess(BluetoothGattCharacteristic characteristic) {
                ToastUtil.showToast("发送数据成功");
            }
        });
        if (!result) {
            ToastUtil.showToast("发送数据失败!");
        }
    }

    /*主动读取数据*/
    public void read(BleDevice device) {
        boolean result = mBle.read(device, new BleReadCallback<BleDevice>() {
            @Override
            public void onReadSuccess(BluetoothGattCharacteristic characteristic) {
                super.onReadSuccess(characteristic);
                byte[] data = characteristic.getValue();
                Log.w(TAG, "onReadSuccess: " + Arrays.toString(data));
            }
        });
        if (!result) {
            Log.d(TAG, "读取数据失败!");
        }
    }

    //播放音乐
    public byte[] changeLevelInner(int play) {
        byte[] data = new byte[Command.qppDataSend.length];
        System.arraycopy(Command.qppDataSend, 0, data, 0, data.length);
        data[6] = 0x03;
        data[7] = (byte) play;
//        Logger.e("data:" + Arrays.toString(data));
        return data;
    }

//    播放音乐
//    public byte[] changeLevelInner() {
//        int var = 0xAA51;//左邊是高位  右邊是低位
//        byte[] data = new byte[2];
//        data[0] =  (byte)((var>>8) & 0xff);
//        data[1] =  (byte)(var & 0xff);
//        Logger.e("data:" + Arrays.toString(data));
//        Logger.e("data11:" + Integer.toHexString(var));
//        return data;
//    }

    //扫描的回调
    BleScanCallback<BleDevice> scanCallback = new BleScanCallback<BleDevice>() {
        @Override
        public void onLeScan(final BleDevice device, int rssi, byte[] scanRecord) {
            synchronized (mBle.getLocker()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLeDeviceListAdapter.addDevice(device);
                        mLeDeviceListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            Log.e(TAG, "onStop: ");
        }
    };

    /*连接的回调*/
    private BleConnCallback<BleDevice> connectCallback = new BleConnCallback<BleDevice>() {
        @Override
        public void onConnectionChanged(final BleDevice device) {
            if (device.isConnected()) {
                 /*连接成功后，设置通知*/
                mBle.startNotify(device, bleNotiftCallback);
            }
            Log.e(TAG, "onConnectionChanged: " + device.isConnected());
            mLeDeviceListAdapter.notifyDataSetChanged();
            setConnectedNum();
        }

        @Override
        public void onConnectException(BleDevice device, int errorCode) {
            super.onConnectException(device, errorCode);
            ToastUtil.showToast("连接异常，异常状态码:" + errorCode);
        }
    };

    /*设置通知的回调*/
    private BleNotiftCallback<BleDevice> bleNotiftCallback =  new BleNotiftCallback<BleDevice>() {
        @Override
        public void onChanged(BleDevice device, BluetoothGattCharacteristic characteristic) {
            UUID uuid = characteristic.getUuid();
            Log.e(TAG, "onChanged==uuid:" + uuid.toString());
            Log.e(TAG, "onChanged==address:"+ device.getBleAddress());
            Log.e(TAG, "onChanged==data:" + Arrays.toString(characteristic.getValue()));
        }
    };

    /*更新当前连接设备的数量*/
    private void setConnectedNum() {
        if (mBle != null) {
            Log.e("mConnectedNum", "已连接的数量：" + mBle.getConnetedDevices().size() + "");
            for (BleDevice device : mBle.getConnetedDevices()) {
                Log.e("device", "设备地址：" + device.getBleAddress());
            }
        }
    }

    /*拷贝OTA升级文件到SD卡*/
    private void CopyAssetsToSD() {
        //判断是否是第一次进入   默认第一次进入
        if (!SPUtils.get(this, StaticValue.IS_FIRST_RUN, true)) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取SD卡路径
                path = Environment.getExternalStorageDirectory()
                        + "/aceDownload/";
                File file = new File(path);
                // 如果SD卡目录不存在创建
                if (!file.exists()) {
                    file.mkdir();
                }

                final File newFile = new File(path + StaticValue.OTA_NEW_PATH);
                final File oldFile = new File(path + StaticValue.OTA_OLD_PATH);
                try {
                    FileUtils.copyBigDataToSD(ScanBleBluetoothActivity.this, StaticValue.OTA_NEW_PATH, newFile.getAbsolutePath());
                    FileUtils.copyBigDataToSD(ScanBleBluetoothActivity.this, StaticValue.OTA_OLD_PATH, oldFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //设置程序非第一次进入
                SPUtils.put(ScanBleBluetoothActivity.this, StaticValue.IS_FIRST_RUN, false);
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_scan:
//                Logger.e("点击了扫描按钮");
//                reScan();
//                break;
//            case R.id.menu_stop:
//                Logger.e("点击了停止扫描按钮");
//                if (mBle != null) {
//                    mBle.stopScan();
//                }
//                break;
//            case R.id.menu_connect_all:
//                Logger.e("点击了连接全部设备按钮");
//                if (mBle != null) {
//                    for (int i = 0; i < mLeDeviceListAdapter.getCount(); i++) {
//                        BleDevice device = mLeDeviceListAdapter.getDevice(i);
//                        mBle.connect(device, connectCallback);
//                    }
//                }
//                break;
//            case R.id.menu_disconnect_all:
//                Logger.e("点击了断开全部设备按钮");
//                if (mBle != null) {
//                    ArrayList<BleDevice> list = mBle.getConnetedDevices();
//                    for (BleDevice device : list) {
//                        mBle.disconnect(device);
//                    }
//                }
//                break;
////            case R.id.menu_introduced:
////                startActivity(new Intent(this, IntroducedActivity.class));
////                break;
//        }
        return super.onOptionsItemSelected(item);
    }

    //重新扫描
    private void reScan() {
        if (mBle != null && !mBle.isScanning()) {
            mLeDeviceListAdapter.clear();
            mLeDeviceListAdapter.addDevices(mBle.getConnetedDevices());
            mBle.startScan(scanCallback);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == Ble.REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        } else {
            //请求权限
            requestPermission();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBle != null) {
            mBle.destory(getApplicationContext());
        }
    }

    private int  mPermissionIdx = 0x10;//请求权限索引
    private SparseArray<GrantedResult> mPermissions   = new SparseArray<>();//请求权限运行列表

    @SuppressLint("Override")
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        GrantedResult runnable = mPermissions.get(requestCode);
        if (runnable == null) {
            return;
        }
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            runnable.mGranted = true;
        }
        runOnUiThread(runnable);
    }

    public void requestPermission(String[] permissions, String reason, GrantedResult runnable) {
        if(runnable == null){
            return;
        }
        runnable.mGranted = false;
        if (Build.VERSION.SDK_INT < 23 || permissions == null || permissions.length == 0) {
            runnable.mGranted = true;//新添加
            runOnUiThread(runnable);
            return;
        }
        final int requestCode = mPermissionIdx++;
        mPermissions.put(requestCode, runnable);

		/*
			是否需要请求权限
		 */
        boolean granted = true;
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                granted = granted && checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            }
        }

        if (granted) {
            runnable.mGranted = true;
            runOnUiThread(runnable);
            return;
        }

		/*
			是否需要请求弹出窗
		 */
        boolean request = true;
        for (String permission : permissions) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                request = request && !shouldShowRequestPermissionRationale(permission);
            }
        }

        if (!request) {
            final String[] permissionTemp = permissions;
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage(reason)
                    .setPositiveButton(R.string.btn_sure, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(permissionTemp, requestCode);
                            }
                        }
                    })
                    .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            GrantedResult runnable = mPermissions.get(requestCode);
                            if (runnable == null) {
                                return;
                            }
                            runnable.mGranted = false;
                            runOnUiThread(runnable);
                        }
                    }).create();
            dialog.show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions, requestCode);
            }
        }
    }

    public static abstract class GrantedResult implements Runnable{
        private boolean mGranted;
        public abstract void onResult(boolean granted);
        @Override
        public void run(){
            onResult(mGranted);
        }
    }
}
