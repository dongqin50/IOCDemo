package com.conagra.hardware.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.conagra.R;
import com.conagra.databinding.ActivityBluetoothLeAccessBinding;
import com.conagra.hardware.adapter.BluetoothCustomAdapter;
import com.conagra.hardware.adapter.LeDeviceListAdapter;
import com.conagra.hardware.model.BluetoothModel;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.ui.activity.BaseAppCompatActivity;
import com.conagra.mvp.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.Disposable;

@SuppressLint("NewApi")
public class BluetoothLeAccessActivity extends BaseAppCompatActivity<ActivityBluetoothLeAccessBinding> {

    private final static String TAG = BluetoothLeAccessActivity.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private boolean mScanning;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 5000 * 60 * 5; // 5秒后停止查找搜索.

    private List<BluetoothModel> mYBluetoothList;
    private List<BluetoothModel> mWBluetoothList;
    private BluetoothCustomAdapter mYBluetoothAdapter;
    private BluetoothCustomAdapter mWBluetoothAdapter;
    private Disposable mSearchDisposable;
//	@BindView(R.id.access_header)
//	TempleHeaderView mHeader;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_bluetooth_le_access);
        doInit();
    }

    public void doInit() {
        mHandler = new Handler();
        mYBluetoothList = new ArrayList<>();
        mWBluetoothList = new ArrayList<>();

        setTitle("蓝牙设置");
//		mHeader.doBack().subscribe(new Action1() {
//			@Override
//			public void call(Object o) {
//				finish();
//			}
//		});
        // 检查当前手机是否支持ble 蓝牙,如果不支持退出程序
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "没有蓝牙设备，无法开启蓝牙", Toast.LENGTH_SHORT).show();
            finish();
        }

        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在android4.3或以上版本)
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // 检查设备上是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "没有蓝牙设备，无法开启蓝牙", Toast.LENGTH_SHORT).show();

            Intent intent;
            intent = this.getIntent();
            setResult(RESULT_CANCELED, intent);

            finish();
            return;
        }
        // 打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        mYBluetoothAdapter = new BluetoothCustomAdapter(mBinding.accessYppRv, mYBluetoothList, new BaseListener.OnItemSelectedListener<BluetoothModel>() {
            @Override
            public void onItemSelectedListener(BluetoothModel value) {
                doChoose(value);
            }
        });

        mWBluetoothAdapter = new BluetoothCustomAdapter(mBinding.accessWppRv, mWBluetoothList, new BaseListener.OnItemSelectedListener<BluetoothModel>() {
            @Override
            public void onItemSelectedListener(BluetoothModel value) {
                doChoose(value);
            }
        });
        mBinding.accessYppRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.accessYppRv.setAdapter(mYBluetoothAdapter);
        mBinding.accessWppRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.accessWppRv.setAdapter(mWBluetoothAdapter);
        //获取已连接设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        if (devices.size() > 0) {
            for (Iterator<BluetoothDevice> it = devices.iterator(); it.hasNext(); ) {
                BluetoothDevice deice = (BluetoothDevice) it.next();
                if (deice != null) {
                    BluetoothModel bluetoothBean = new BluetoothModel();
                    bluetoothBean.setConnected(false);
                    bluetoothBean.setDeviceAddress(deice.getAddress());
                    bluetoothBean.setDeviceName(deice.getName());
                    mYBluetoothAdapter.add(bluetoothBean);
                }
            }
        }
        mSearchDisposable = mBinding.accessBtScan.doSearch()
                .subscribe((o) -> {
                    if (mScanning) {
                        scanLeDevice(false);
                    }
                    scanLeDevice(true);
//						mBtScan.doFinished();
                });

    }

    public void doChoose(BluetoothModel device) {
        mBinding.accessBtScan.doFinished();
        Intent intent = getIntent();
        Bundle bl = intent.getExtras();
        bl.putString("deviceaddress", device.getDeviceAddress());
        // 将Bundle放入Intent传入下一个Activity
        intent.putExtras(bl);
        // 跳到下一个Activity,并且等待其返回结果
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            // 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
            if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            // Initializes list view adapter.
            mLeDeviceListAdapter = new LeDeviceListAdapter(this);
            //setListAdapter(mLeDeviceListAdapter);
            scanLeDevice(false);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
//                view1.doConnecting();
                //蓝牙打开成功，开始搜索、连接和测量
//                bluetoothManager.startBTAffair(onBTMeasureListener);
                mBluetoothAdapter.startDiscovery();
            } else {
                //蓝牙不能正常打开
                finish();
            }
        } else if (requestCode == REQUEST_CODE_PERMISSION_SETTING) {
            if (PermissionUtil.checkLocationPermission(this)) {
                mBluetoothAdapter.startDiscovery();

            } else {
                Toast.makeText(BluetoothLeAccessActivity.this, "权限获取失败",  Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }

    }

    // 浏览提供服务的外设
    @SuppressWarnings("deprecation")
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mBinding.accessBtScan.doFinished();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    //扫描回调接口
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//					Log.d(TAG, device.getName());
                    // 在此处可建立ArrayList<BluetoothDevice> mLeDevices保存外设，以备后续使用

                    if (device == null)
                        return;

                    if (!mLeDeviceListAdapter.addDevice(device)) {
                        mLeDeviceListAdapter.notifyDataSetChanged();
                        return;
                    }
                    //mLeDeviceListAdapter.addDevice(device);
                    //mLeDeviceListAdapter.notifyDataSetChanged();
//
                    int status = device.getBondState();
                    switch (status) {

                        case BluetoothDevice.BOND_BONDED:
                            boolean flag = false;
                            if (mYBluetoothList != null) {
                                for (BluetoothModel bean : mYBluetoothList) {
                                    if (!TextUtils.isEmpty(bean.getDeviceName())&&bean.getDeviceName().equals(device.getName())) {
                                        flag = true;
                                        break;
                                    }
                                }
                                if (!flag) {
                                    mYBluetoothAdapter.add(new BluetoothModel(device.getName(), device.getAddress()));
                                }
                            }
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            break;
                        case BluetoothDevice.BOND_NONE:
                            flag = false;
                            if (mWBluetoothList != null) {
//								for(BluetoothModel bean : mWBluetoothList){
//									if(!TextUtils.isEmpty(bean.getDeviceName())
//											&& !TextUtils.isEmpty(device.getName())
//											&& bean.getDeviceName().equals(device.getName())){
//
//										flag = true;
//										break;
//									}
//								}
                                if (!flag) {
                                    if (TextUtils.isEmpty(device.getName())) {
                                        mWBluetoothAdapter.add(new BluetoothModel(device.getAddress(), device.getAddress()));
                                    } else {
                                        mWBluetoothAdapter.add(new BluetoothModel(device.getName(), device.getAddress()));
                                    }

                                }
                            }

                            break;
                    }
                }
            });
        }
    };

    /**
     * sdk会自动申请权限，如果失败则手动申请
     */
    @SuppressLint("NewApi")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case com.conagra.iknetbluetoothlibrary.BluetoothManager.REQUEST_FINE_LOCATION:
                //23以上版本蓝牙扫描需要定位权限(android.permission.ACCESS_COARSE_LOCATION)，此处判断是否获取成功
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取权限成功
                    mBluetoothAdapter.startDiscovery();
                } else {
                    // 获取权限失败
                    Toast.makeText(BluetoothLeAccessActivity.this, "权限获取失败", Toast.LENGTH_SHORT).show();
                    setPermissionApplyDialog();
                }
                break;
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
                                    BluetoothLeAccessActivity.this.finish();
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearchDisposable != null && !mSearchDisposable.isDisposed()) {
            mSearchDisposable.dispose();
        }
    }
}
