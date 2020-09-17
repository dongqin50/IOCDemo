package com.conagra.bluetooth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.conagra.R;
import com.conagra.databinding.ActivityScanBluetoothBinding;
import com.conagra.mvp.ui.activity.BaseAppCompatActivity;

import cn.com.heaton.blelibrary.spp.BtDevice;
import cn.com.heaton.blelibrary.spp.BtManager;
import io.reactivex.functions.Consumer;

/**
 * Created by yedongqin on 2018/6/10.
 */

public class ScanSppBluetoothActivity extends BaseAppCompatActivity<ActivityScanBluetoothBinding> implements AdapterView.OnItemClickListener {

    private String TAG = ScanSppBluetoothActivity.class.getSimpleName();
    public final static int OPEN_BLUETH = 0x89;//请求打开蓝牙

    private BtDeviceAdapter mBtAdapter;
    private BtDeviceAdapter mBondedBtAdapter;
    private BtManager mBtManager;
    private boolean isScanning = false;//是否正在扫描
    //播放音乐
    boolean lock = false;//默认关


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_scan_bluetooth);
        initView();
        initBle();
        mBinding.accessBtScan.doSearch()
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (mBtManager != null && !isScanning) {
                            mBtAdapter.clear();
//                    mBtManager.release();
                            mBtManager.startDiscovery();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final BtDevice device = mBtAdapter.getDevice(position);
        if (device == null) return;
        if (isScanning) {
            mBinding.accessBtScan.doFinished();
            mBtManager.cancelDiscovery();
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("deviceaddress",device.getAddress());
        intent.putExtras(bundle);
//        intent.putExtra("deviceAddress",device.getAddress());
        setResult(RESULT_OK,intent);

        finish();
//        if (device.isConnected()) {
//            device.close();
//            sendData();
//        } else {
//            mBtManager.addDevice(device);
////                    BtUtils.pair(device);
//            mBtManager.connect(device);
//        }
    }


    private void initView() {
        // Initializes list view adapter.
        if (mBtAdapter == null) {
            mBtAdapter = new BtDeviceAdapter(this);
        }
        if (mBondedBtAdapter == null) {
            mBondedBtAdapter = new BtDeviceAdapter(this);
        }


        mBinding.accessWppRv.setOnItemClickListener(this);
        mBinding.accessWppRv.setAdapter(mBtAdapter);

        mBinding.accessYppRv.setOnItemClickListener(this);
        mBinding.accessYppRv.setAdapter(mBondedBtAdapter);


    }

    public void sendData() {
        if (mBtManager.getConnectedDevices().size() == 0) {//若当前没有连接设备则直接返回
            Toast.makeText(this,"请连接设备后重试",Toast.LENGTH_SHORT).show();
            return;
        }
        lock = !lock;
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] oc = new byte[6];
                oc[0] = 0;    //包的索引
                oc[1] = 4;    //包的长度
                oc[2] = 'A';
                oc[3] = 'T';
                oc[4] = 'E';
                oc[5] = (byte) (lock ? '1': '0');
                boolean result = mBtManager.getConnectedDevices().get(0).sendOnePacket(oc, 10, true);
                Log.e(TAG, "sendData: "+result);
            }
        }).start();
    }

    private BtManager.BtDeviceListener mBtListener = new BtManager.BtDeviceListener() {
        @Override
        public void onStateChanged(int state, BtDevice btDevice) {
            if (mBtAdapter != null) {
                mBtAdapter.notifyDataSetChanged();
            }
            if (mBondedBtAdapter != null) {
                mBondedBtAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onDevicesChanged() {
            Log.e(TAG, "onDevicesChanged: " + "设备连接状态改变");
            if (mBtAdapter != null) {
                mBtAdapter.notifyDataSetChanged();
            }
            if (mBondedBtAdapter != null) {
                mBondedBtAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onError(int errorCode, BtDevice btDevice) {

        }

        @Override
        public void onRead(byte[] buffer, BtDevice btDevice) {

        }

        @Override
        public void onWrite(byte[] buffer, BtDevice btDevice) {

        }
        @Override
        public void onFound(BtDevice btDevice) {
//            synchronized (mBtManager.getLocker()) {
                if(btDevice.getBluetoothDevice().getBondState() == BluetoothDevice.BOND_BONDED){
                    mBondedBtAdapter.addDevice(btDevice);
                    mBondedBtAdapter.notifyDataSetChanged();
                }else {
                    mBtAdapter.addDevice(btDevice);
                    mBtAdapter.notifyDataSetChanged();
                }

//            }
        }

        @Override
        public void onStartScan() {
            isScanning = true;
        }

        @Override
        public void onStopScan() {
            isScanning = false;
        }
    };

    private void initBle() {
        try {
            mBtManager = new BtManager(this, mBtListener);
            mBtManager.setSecure(true);
            //如果没有打开蓝牙，则弹出请打开蓝牙
            if (mBtManager.getAdapter() != null && !mBtManager.getAdapter().isEnabled()) {
                Intent enableBlueIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBlueIntent, OPEN_BLUETH);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBtManager != null && !isScanning) {
            mBtAdapter.clear();
//            mBtManager.release();
            mBondedBtAdapter.clear();
            mBtManager.startDiscovery();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBtManager != null) {
            mBtManager.cancelDiscovery();
        }
        mBondedBtAdapter.clear();
        mBtAdapter.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private int    mPermissionIdx = 0x10;//请求权限索引
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
