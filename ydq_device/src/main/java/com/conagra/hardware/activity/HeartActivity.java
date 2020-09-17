package com.conagra.hardware.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;

import com.conagra.R;
import com.conagra.databinding.ActivityHeartBinding;
import com.conagra.hardware.model.BluetoothModel;
import com.conagra.mvp.BaseListener;
import com.conagra.mvp.broadcast.HeadsetPlugReciver;
import com.conagra.mvp.ui.activity.BaseAppCompatActivity;
import com.conagra.mvp.utils.ActivityUtils;
import com.conagra.mvp.utils.BluetoothUtils;
import com.xiaoye.myutils.utils.ToastUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yedongqin on 16/9/18.
 *
 * 胎心
 */
public class HeartActivity extends BaseAppCompatActivity<ActivityHeartBinding> implements BaseListener.OnItemSelectedListener<BluetoothModel>{

    private BluetoothUtils bluetoothUtils;
    private boolean isConntected;
    private String userId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_heart);

    }

    public void doInit() {

        bluetoothUtils = BluetoothUtils.getInstance();
        bluetoothUtils.init(mBinding.heartRv,this,mBinding.heartBroadcast);

        setTitle("连接设备");
        setRightComponent(R.drawable.time_ico, (v)->
                ActivityUtils.toNextActivity(HeartActivity.this, HeartListActivity.class)
        );
        mBinding.heartBroadcast.doSearch()
                .filter((o)->
                         !bluetoothUtils.isSelected()
                ).subscribe((o)-> {
                        bluetoothUtils.setSelected(true);
                        mBinding.heartSv.setVisibility(View.VISIBLE);
                        bluetoothUtils.startSearch();
                });

       mBinding.heartYouxian.setOnClickListener((v -> {
            if(!HeadsetPlugReciver.isHeadSet){
                ToastUtils.getInstance().makeText(HeartActivity.this,"设备未连接,请连接设备!");
            }else{
                finish();
                ActivityUtils.toNextActivity(FetalHeartActivity.class);
            }
        }));

//        TestUtils.doUnUserfully(mBroadcast);
//        ActivityUtils.doClick(mBroadcast)

        mBinding.heartRv.setLayoutManager(new LinearLayoutManager(this));
        mBinding.heartRv.setAdapter(bluetoothUtils.getListAdapter());
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        bluetoothUtils.unregisterReceiver();
        mBinding.heartBroadcast.doFinished();
    }

    @Override
    public void onBackPressed() {

//        doStop(STOP_STATUS_BACK);
        super.onBackPressed();
    }

    @Override
    public void onItemSelectedListener(final BluetoothModel value) {

        Observable.just(value)
                .filter(new Predicate<BluetoothModel>() {
                    @Override
                    public boolean test(BluetoothModel bluetoothModel) throws Exception {
                        return !isConntected;
                    }
                })
                .filter(new Predicate<BluetoothModel>() {

                    @Override
                    public boolean test(BluetoothModel bluetoothModel) throws Exception {
//                        try {

//                            bluetoothUtils.connect(bluetoothDevice);
//                            if(value.getDeviceName().contains("iFM")){
                                isConntected = true;
//                            }else {
//                                isConntected = false;
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            isConntected = false;
//
//                        }
                        return isConntected;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BluetoothModel>() {
                    @Override
                    public void accept(BluetoothModel bluetoothModel) throws Exception {
                        if(bluetoothUtils.isDiscovering()){
                            bluetoothUtils.cancelDiscovery();
                            mBinding.heartBroadcast.doFinished();
                        }
                        ActivityUtils.toNextActivity(FetalHeartActivity.class);
                        isConntected = false;
                        finish();
                    }
                });



//        Intent intent = new Intent(this,BluetoothEquipment.class);
//        bluetoothServiceConnnect = new BluetoothServiceConnnect(value);
//        bindService(intent,bluetoothServiceConnnect, Context.BIND_AUTO_CREATE);
    }

//    public class BluetoothServiceConnnect implements ServiceConnection,Serializable{
//
//        private   BluetoothEquipment bluetoothEquipment;
//        private BluetoothDevice device;
//
//        public BluetoothEquipment getBluetoothEquipment() {
//            return bluetoothEquipment;
//        }
//
//        public BluetoothServiceConnnect(BluetoothDevice device) {
//            this.device = device;
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            BluetoothEquipment.BluetoothBinder bluetoothService = (BluetoothEquipment.BluetoothBinder) service;
//            bluetoothEquipment = bluetoothService.getBluetoothEquipment();
//            bluetoothEquipment.doConnectEquipment(device)
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                        if(bluetoothUtils.isDiscovering())
//                            bluetoothUtils.cancelDiscovery();
//
//                        Intent intent = new Intent(HeartActivity.this,FetalHeartActivity.class);
//                        intent.putExtra(BluetoothDevice.EXTRA_NAME,device);
//                        startActivity(intent);
//                        HeartActivity.this.finish();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(String o) {
//
//                    }
//                });
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            bluetoothEquipment.cancel();
//            bluetoothEquipment = null;
//        }
//    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {

            case KeyEvent.KEYCODE_BACK :

                // 判断是否正在搜索蓝牙，如果是，则取消搜索
                if(bluetoothUtils.isDiscovering() == true)
                    bluetoothUtils.cancelDiscovery();

                Intent result = new Intent();
//                result.putExtra("list", vd_back_ico);
                setResult(RESULT_CANCELED, result);
                finish();
                break;

            case KeyEvent.KEYCODE_HOME:

                break;

        }

        return super.onKeyDown(keyCode, event);

    }

}




