package com.conagra.hardware.equipment;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.conagra.mvp.utils.LogMessage;
import com.conagra.mvp.utils.StoreAddressUtils;
import com.luckcome.lmtpdecorder.LMTPDecoder;
import com.luckcome.lmtpdecorder.LMTPDecoderListener;
import com.luckcome.lmtpdecorder.data.FhrData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.UUID;

import rx.Subscriber;

/**
 *
 * 蓝牙连接设备
 */
public class BluetoothEquipment extends Service implements Serializable{

    // 蓝牙适配器
    BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    //    private static  BluetoothEquipment bluetoothEquipment = new BluetoothEquipment();
    public static final UUID MY_UUID = 	UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // 服务当前连接的蓝牙设备
//    private BluetoothDevice mBtDevice;
    // 蓝牙 socket
    private BluetoothSocket mSocket = null;

    // 蓝牙输出流
    private OutputStream mOutputStream;
    /**
     * 返回的数据
     */
    private String backData;
    private String backDataGsy;
    // 是否保存标志
    private boolean isRecord = false;
    private boolean isStop = true;
    /*
    * 读数据线程
    */
    private boolean isReading = false;


    /** 终端协议解析器 */
    private LMTPDecoder mLMTPDecoder = null;
    /** 解析器回调接口 */
    private LMTPDListener mLMTPDListener = null;
    private String path;
    private String fname;
    private InputStream mInputStream;

//    private Observable mObserva

//    public static BluetoothEquipment getInstance(){
//        return bluetoothEquipment;
//    }

//    public void doInitData(){
//        mLMTPDecoder = new LMTPDecoder();
//        mLMTPDListener = new LMTPDListener();
//        mLMTPDecoder.setLMTPDecoderListener(mLMTPDListener);
//        mLMTPDecoder.prepare();
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLMTPDecoder = new LMTPDecoder();
        mLMTPDListener = new LMTPDListener();
        mLMTPDecoder.setLMTPDecoderListener(mLMTPDListener);
        mLMTPDecoder.prepare();
        LogMessage.doLogMessage("BaseActivity",mLMTPDecoder.toString());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new BluetoothBinder();
    }

    public class BluetoothBinder extends Binder implements Serializable{
        public BluetoothEquipment getBluetoothEquipment(){
            return BluetoothEquipment.this;
        }
    }

    /*
         * 连接蓝牙,接设备
         */
    public void doConnectEquipment(String deviceAddress,final Subscriber<String> subscriber){
        final BluetoothDevice device = mAdapter.getRemoteDevice(deviceAddress);
        if(mLMTPDecoder != null) {
            mLMTPDecoder.startWork();
        }
        try {
            int sdk = Build.VERSION.SDK_INT;

            if (sdk >= 10) {
                mSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } else {
                mSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mAdapter.cancelDiscovery();
                    LogMessage.doLogMessage("BaseActivity","蓝牙搜索完毕");
                    mSocket.connect();
                    LogMessage.doLogMessage("BaseActivity","胎心仪连接成功");
                    mOutputStream = mSocket.getOutputStream();
                    mInputStream = mSocket.getInputStream();
                    isReading = true;
                    isStop = false;
                    subscriber.onNext(deviceAddress);
                    subscriber.onCompleted();
                    LogMessage.doLogMessage("BaseActivity","获取数据流");
                    while (!isStop) {
                        //正在读取
                        if (isReading) {
                            byte[] buffer = new byte[2048];
                            LogMessage.doLogMessage("BaseActivity","isReading:"+ isReading);
                            int len = mInputStream.read(buffer);
                            if (mLMTPDecoder != null&&len>0) {
                                LogMessage.doLogMessage("BaseActivity","mLMTPDecoder:"+ mLMTPDecoder);
                                mLMTPDecoder.putData(buffer, 0, len);
                            }
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
//                       LogMessage.doLogMessage("BaseActivity","aLong : " + time);
                    }
//00001101-0000-1000-8000-00805F9B34FB
//
                } catch (IOException e) {
                    e.printStackTrace();
                     isReading = false;
                    isStop = true;
                    mOutputStream = null;
                    mInputStream = null;
                    try {
                        if(mSocket != null){
                            mSocket.close();
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
//                    onDestroy();
                    subscriber.onError(new Exception("连接断开"));
                }
            }
        }).start();
    }


    public void onDestroy() {
        isReading = false;
        isStop = true;
        if(mSocket != null) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSocket = null;
        }
        if(mLMTPDecoder != null && mLMTPDecoder.isWorking())
            mLMTPDecoder.stopWork();
        if(mLMTPDecoder != null){
            mLMTPDecoder.release();
            mLMTPDecoder = null;
            mLMTPDListener = null;
        }
    }

    public String  doReadData() {
        String data = (TextUtils.isEmpty(backData) ? "0" : backData) + ":" + (TextUtils.isEmpty(backDataGsy) ? "0" : backDataGsy);
        backData = "0";
        backDataGsy = "0";
        return data;
//        return  Observable.create(new Observable.OnSubscribe<InputStream>() {
//
//
//            @Override
//            public void call( Subscriber<? super InputStream> subscriber) {
//                InputStream mIs = null;
//                try {
//                    mIs = mSocket.getInputStream();
//                } catch (IOException e) {
//                    subscriber.onError(new Exception("网络断开"));
//                    isReading = false;
//                }
//                isReading = true;
//                subscriber.onNext(mIs);
//
//            }
//        }).flatMap(new Func1<InputStream, Observable<String>>() {
//            @Override
//            public Observable<String> call(final InputStream inputStream) {
//                final byte[] buffer = new byte[2048];
//
//                return Observable.interval(1000, TimeUnit.MILLISECONDS)
//                        .takeWhile(new Func1<Long, Boolean>() {
//                            @Override
//                            public Boolean call(Long aLong) {
//                                LogMessage.doLogMessage("BaseActivity","isReading : " + isReading);
//                                return aLong < 60 * 30 && isReading;
//                            }
//                        })
//                        .map(new Func1<Long, String>() {
//                            @Override
//                            public String call(Long aLong) {
//                                try {
//                                    int len = 0;
//                                    len = inputStream.read(buffer);
//
//                                    if(mLMTPDecoder != null){
//                                        mLMTPDecoder.putData(buffer, 0, len);
//                                    }
//                                    LogMessage.doLogMessage("BaseActivity","aLong : " + aLong);
//                                } catch (IOException e) {
//                                    isReading = false;
//                                }
//
//                                String data = (TextUtils.isEmpty(backData)?"0":backData) +":" + (TextUtils.isEmpty(backDataGsy)?"0":backDataGsy);
//                                backData = "0";
//                                backDataGsy = "0";
////                                LogMessage.doLogMessage(BluetoothEquipment.class.getSimpleName(),backData);
//                                return data;
//                            }
//                        }).subscribeOn(Schedulers.io());
//            }
//        });
    }
//    /**
//     * 停止数据读取和解析
//     */
//    public void cancel() {
//
//        isReading = false;
//        if(mLMTPDecoder != null && mLMTPDecoder.isWorking())
//            mLMTPDecoder.stopWork();
//    }

    /**
     * 获取工作状态
     * @return
     */
    public boolean getReadingStatus() {
        return isReading;
    }

    /**
     * 启动记录功能
     */
    public void doStartRecord() {
        File file = new File(StoreAddressUtils.FELDSHER_MUSIC_DIRECTORY);
        fname = "" + System.currentTimeMillis();
        if(mLMTPDecoder != null) {
            mLMTPDecoder.beginRecordWave(file, fname);
            LogMessage.doLogMessage(BluetoothEquipment.class.getSimpleName(), " file " + file.getPath() + " name" + fname);
            isRecord = true;
            isReading = true;
            path = StoreAddressUtils.FELDSHER_MUSIC_DIRECTORY + fname + ".mp3";
        }
    }



    public void doStopReading(){
        path = "";
        isReading = false;
        isRecord = false;
    }

    /**
     * 获取录音的存储地址
     * @return
     */
    public String getFileName() {
        return fname;
    }
    public String getPath() {
        return path;
    }

    /**
     * 结束记录
     */
    public void doStopRecord() {
        isRecord = false;
        if(mLMTPDecoder != null && mLMTPDecoder.isRecording())
            mLMTPDecoder.finishRecordWave();
    }

    /**
     * 获取记录状态
     * @return
     */
    public boolean getRecordStatus() {
        return isRecord;
    }


    /**
     * 设置宫缩复位
     * @param value		宫缩复位的值
     */
    public void setTocoReset(int value) {
        if(mLMTPDecoder != null)
            mLMTPDecoder.sendTocoReset(value);
    }


    /**
     * 设置一次手动胎动
     */
    public void doSetFM() {
        if(mLMTPDecoder != null)
            mLMTPDecoder.setFM();
    }


    /**
     * 设置胎心音量
     * @param value		胎心音量大小
     */
    public void setFhrVolume(int value) {
        if(mLMTPDecoder != null)
            mLMTPDecoder.sendFhrVolue(value);
    }

    /**
     * 协议解析器回调接口
     * @author Administrator
     *
     */
    class LMTPDListener implements LMTPDecoderListener {

        /**
         * 有新数据产生的时候回调
         */
        @Override
        public void fhrDataChanged(FhrData fhrData) {

            if(fhrData == null){
                return;
            }

            LogMessage.doLogMessage("BaseActivity","  LMTPDListener  ");
            String infor = String.format(
                    "{\"FHR1\":\"%-3d\",\"TOCO\":\"%-3d\",\"AFM\":\"%-3d\",\"SIGN\":\"%-3d\",\"BATT\":\"%-3d\""
                            + "\"isFHR1\":\"%-3d\",\"isTOCO\":\"%-3d\",\"isAFM\":\"%-3d\"}",
                    fhrData.fhr1, fhrData.toco, fhrData.afm, fhrData.fhrSignal, fhrData.devicePower,
                    fhrData.isHaveFhr1, fhrData.isHaveToco, fhrData.isHaveAfm
            );

            LogMessage.doLogMessage("BaseActivity",infor);

            if(fhrData.fmFlag != 0)
                Log.v("LMTPD", "LMTPD...1...fm");

            if(fhrData.tocoFlag != 0)
                Log.v("LMTPD", "LMTPD...2...toco");
            int devicePower = Integer.valueOf(fhrData.devicePower);

            backData = Integer.valueOf(fhrData.fhr1).toString();
            backDataGsy = Integer.valueOf(fhrData.toco).toString();
        }

        /**
         * 有命令产生的时候回调
         */
        @Override
        public void sendCommand(byte[] cmd) {
            // 这里添加从蓝牙发送数据的代码
            if(mOutputStream != null) {
                try {
                    mOutputStream.write(cmd);
                    mOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
