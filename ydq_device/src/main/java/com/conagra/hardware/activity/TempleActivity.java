package com.conagra.hardware.activity;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.conagra.R;
import com.conagra.databinding.ActivityTempleBinding;
import com.conagra.di.component.DaggerTempleComponent;
import com.conagra.di.component.TempleComponent;
import com.conagra.di.module.HardwareManagerModule;
import com.conagra.di.module.TempleModule;
import com.conagra.hardware.adapter.QuickeningAdapter;
import com.conagra.hardware.layout.TempleLayout1;
import com.conagra.hardware.layout.TempleLayout2;
import com.conagra.hardware.widget.MyDialog1;
import com.conagra.mvp.model.TempleListModel;
import com.conagra.mvp.presenter.TemplePresenter;
import com.conagra.mvp.ui.activity.DragBaseFragmentActivity;
import com.conagra.mvp.ui.view.TempleView;
import com.conagra.mvp.utils.CurrentServerManagerUtils;
import com.conagra.mvp.utils.TimeUtils;
import com.xiaoye.myutils.utils.ToastUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by yedongqin on 2016/10/27.
 */

public class TempleActivity extends DragBaseFragmentActivity<ActivityTempleBinding,TemplePresenter,TempleComponent> implements TempleView{

    private final static int MaxShowNumber = 30; 	//最大显示数据个数
    private final static int EarArrayLength = 8;	//耳温计数据个数
    private List<View> viewList;
    private TempleLayout1 view1;
    private TempleLayout2 view2;
    private readThread mreadThread;
    private QuickeningAdapter mAdapter;
    private static final String  TAG = TempleActivity.class.getSimpleName();
    private boolean mConnected = false;
    private String mDeviceAddress;
    private String currentTime;
    private BluetoothSocket socket;
    private String userId;
    private String hospitalId;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {// 覆盖handleMessage方法
            switch (msg.what) {// 根据收到的消息的what类型处理
                case 1:
                    //只保留一位小数位
                    msg.arg1 = msg.arg1 - msg.arg1 % 10;
//                    float data =  msg.arg1 - msg.arg1 % 10;
                    float data = (float)(msg.arg1 - msg.arg1 % 10) / 100;
                    ToastUtils.getInstance().makeText(TempleActivity.this,Float.toString(data));
                    if(data<= 34.6){
                        data = 34.6f;
                    }else if(data >= 50){
                        data = 50f;
                    }
                    view1.doRecordData(data + "");
                    break;
                case 2:	//EEPROM出错
                    ToastUtils.getInstance().makeText(TempleActivity.this, "EEPROM出错");
                    break;
                case 3:	//传感器出错
                    ToastUtils.getInstance().makeText(TempleActivity.this,"传感器出错");
                    break;
                //连接成功
                case 4:
                    view1.doConnected();
                    mDeviceAddress = (String) msg.obj;
                    mLocalConfig.setTempleDevices(mDeviceAddress);
                    if(!isExistDevice && CurrentServerManagerUtils.isDesignCompany){
                        mPresenter.addDevice(mDeviceAddress);
                    }
                    break;
                //断开连接
                case 5:
                    view1.doDisConnected();
                    break;
                default:
                    view1.doDisConnected();
//                    super.handleMessage(msg);// 这里最好对不需要或者不关心的消息抛给父类，避免丢失消息
                    break;
            }
        }
    };

    public boolean ismConnected() {
        return mConnected;
    }

    public String getDeviceAddress() {
        return mDeviceAddress;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_temple);
        setTitle("体温监测");
        mDeviceAddress = mLocalConfig.getTempleDevices();
        doInit();
    }

    @Override
    protected void initializeInjector() {
        mComponent = DaggerTempleComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .hardwareManagerModule(new HardwareManagerModule())
                .templeModule(new TempleModule()).build();
        mComponent.inject(this);
    }

    public void doInit() {
        viewList = new ArrayList<>();
        Intent intent = getIntent();
        if(intent != null ){
            if(intent.hasExtra("data")){
                currentTime = intent.getStringExtra("data");
            }
            if(intent.hasExtra("hospitalId")){
                hospitalId = intent.getStringExtra("hospitalId");
            }
            userId = getIntent().getStringExtra("userId");
            mPresenter.setUserId(userId);
            mPresenter.setHospitalId(hospitalId);
        }
        mPresenter.registerView(this);
        if(TextUtils.isEmpty(currentTime)){
            setRightComponent(R.drawable.time_ico, (v)->{
                Intent intent1 = new Intent(this,TempleListActivity.class);
                intent1.putExtra("userId",userId);
                startActivity(intent1);
            });
            currentTime = TimeUtils.getYMDData(0);
            view1 = new TempleLayout1(this);
            view1.setListener((s)->{
                view2.doClear();
                view2.doGetListForWeek(currentTime);
            });
            viewList.add(view1);
            view2 = new TempleLayout2(this);
        }else {
            view2 = new TempleLayout2(this);
            view2.doHideBar();
        }

        viewList.add(view2);
        view2.setCurrentTime(currentTime);
        mAdapter = new QuickeningAdapter(mBinding.bloodtempleRv, viewList);
        mBinding.bloodtempleRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.bloodtempleRv.setAdapter(mAdapter);
    }

    public void requestCreate(String tempureValue){
        mPresenter.create(tempureValue);
    }

    public void requestGetAll(String startTime,String endTime){
        mPresenter.GetALLList(startTime,endTime);
    }

   @Override
    public void isExist( boolean isEdit) {
        Toast.makeText(this,"您添加的太过频繁",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void renderAllData(List<TempleListModel> model) {
        view2.renderAllData(model);
    }

    @Override
    public void createOrUpdateSuccess() {
        Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭socket连接
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }

        //释放线程
        mreadThread = null;
        mHandler = null;
    }

    public void onConnectClick(){
        if(TextUtils.isEmpty(mDeviceAddress)){
            Intent intent = new Intent();
            intent.setClass(this,BluetoothLeAccessActivity.class);
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

                        doConnect(mDeviceAddress);
                    },(e)->{

                    });
            dialog1.show();
        }
    }

    private boolean isExistDevice;
    @Override
    public void isExistDevice(boolean isExist, String deviceAddress) {
        isExistDevice = isExist;
        if(isExist || CurrentServerManagerUtils.isDesignCompany){
            doConnect(deviceAddress);
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
        switch (resultCode) {
            // 结果返回
            case RESULT_OK:

                // 获取Bundle的数据
                Bundle bl = data.getExtras();
                String deviceaddress = bl.getString("deviceaddress");
                 if(!TextUtils.isEmpty(deviceaddress)){
                    mPresenter.isExistDevice(deviceaddress);
                }

                break;
            default:
                break;
        }
    }

    public void doConnect(String address){
        if(!TextUtils.isEmpty(address)){
            this.mDeviceAddress = address;
        }

        if(TextUtils.isEmpty(address)){
            Message message = Message.obtain();
            message.what = 5;
            mHandler.sendMessage(message);
            return;
        }
        view1.doConnecting();
        // 得到子类控件
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            // Toast.makeText(BlueToothAccess.this, "链接成功",
            // Toast.LENGTH_SHORT).show();
            socket = device.createRfcommSocketToServiceRecord(UUID
                    .fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket.connect();
            mConnected = true;

            Message msg = new Message();
            msg.obj = address;
            msg.what = 4;
            mHandler.sendMessage(msg);
//                            if(requestDialog.isShowing()){
//                                requestDialog.dismiss();
//                            }
            // 启动接受数据
            mreadThread = new readThread();
            mreadThread.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mConnected = false;
            mDeviceAddress = null;
            Toast.makeText(TempleActivity.this,"连接失败，请选择的体温计",Toast.LENGTH_SHORT).show();
            Message msg = Message.obtain();
            msg.what = 5;
            mHandler.sendMessage(msg);
        }

    }


    //读取数据
    private class readThread extends Thread {
        public void run() {

            byte[] buffer = new byte[1024];
            int bytes;
            InputStream mmInStream = null;

            try {
                mmInStream = socket.getInputStream();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            int loop = 0;
            int arg1=0,arg2=0;
            while (true) {
                try {
                    // Read from the InputStream

                    if( (bytes = mmInStream.read(buffer)) > 0 )
                    {
                        Log.i(TAG, "bytes=" + bytes);
                        if (bytes == EarArrayLength) {
                            dealWithEar(buffer);
                            continue;
                        }
                        if(loop==0){
                            if(bytes==1){
                                arg1 = buffer[0];
                            }
                            else{
                                arg1 = buffer[0];
                                arg2 = buffer[1];
                            }
                        }
                        else if(loop==1){
                            arg2 = buffer[0];
                        }
                        loop += bytes;
                        if(loop>=3){
                            Message msg = new Message();
                            msg.what = 1;
                            msg.arg1 = arg1*256+arg2;
                            mHandler.sendMessage(msg);
                            loop = 0;
                        }
                    }
                } catch (IOException e) {
                    try {
                        mmInStream.close();

                        if(mHandler != null){
                            Message msg = new Message();
                            msg.what = 5;
                            mHandler.sendMessage(msg);
                        }

                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    break;
                }
            }
        }

        private void dealWithEar(byte[] data) {
            int[] buffer = new int[EarArrayLength];

            Log.i(TAG, "dealWithEar");
            for (int i = 0; i < 8; i++) {
                buffer[i] = data[i];
                while (buffer[i] < 0) {
                    buffer[i] += 256;
                }
                Log.i(TAG, "buffer[" + i + "] = " + buffer[i]);
            }

            final int indexCN = 1;
            final int indexDI = 2;
            final int indexCMD = 3;

            if (buffer[0] == 0x8F &&
                    buffer[indexCN] == 0x6A/*蓝牙*/ &&
                    buffer[indexDI] == 0x72/*体温计*/ &&
                    buffer[indexCMD] == 0x5A/*设备上传到App*/) {

                Log.i(TAG, "计算校验和");

                //计算校验和
                int dataSum = 0;
                for (int j = 1; j < EarArrayLength - 1; j++) {
                    dataSum += buffer[j];
                }

                while (dataSum >= 256) {
                    dataSum -= 256;
                }

                Log.i(TAG, "判断校验和 " + dataSum + ", " + buffer[EarArrayLength - 1]);

                //判断校验和
                if (dataSum != buffer[EarArrayLength - 1]) {
                    return;
                }

                final int indexData1 = 6;
                byte[] arrayData1 = getBooleanArray((byte) buffer[indexData1]);
                int state = arrayData1[2] * 4 + arrayData1[3] * 2 + arrayData1[4];
                Log.i(TAG, "state=" + state);
                switch (state) {
                    case 0:	//000 - 正常
                        final int indexData2 = 5;
                        final int indexData3 = 4;
                        int temperature = buffer[indexData3] * 256 + buffer[indexData2];
                        //处理华氏度
                        if (arrayData1[7] == 1) {
                            //98.33=36.85*1.8+32.00,其中36.85是摄氏度,98.33是华氏度
                            temperature = temperature * 180 + 3200;
                        }
                        Message msg = new Message();
                        msg.what = 1;
                        msg.arg1 = temperature;
                        mHandler.sendMessage(msg);
                        break;

                    case 1:	//001 - 测量温度Lo
                    case 2:	//010 - 测量温度Hi
                    case 3:	//011 - 环境温度Lo
                    case 4:	//100 - 环境温度Hi
                        //不作处理
                        break;

                    case 5:	//101 - EEPROM出错
                        Message msg1 = new Message();
                        msg1.what = 2;
                        mHandler.sendMessage(msg1);
                        break;

                    case 6:	//110 - 传感器出错
                        Message msg2 = new Message();
                        msg2.what = 3;
                        mHandler.sendMessage(msg2);
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * 将byte数据转换为8位01数组
     * @param data byte数据
     * @return 8位01数组
     */
    public static byte[] getBooleanArray(byte data) {
        byte[] array = new byte[8];
        String tmp = "";
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (data & 1);
            data = (byte) (data >> 1);
            tmp += array[i];
        }
        Log.i(TAG, "array = " + tmp);
        return array;
    }
}
