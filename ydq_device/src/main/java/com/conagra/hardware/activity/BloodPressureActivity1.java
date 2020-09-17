package com.conagra.hardware.activity;


public class BloodPressureActivity1{

//        extends DragBaseFragmentActivity<
//        ActivityBloodpressureBinding, BloodPressurePresenter, BloodPressureComponent> implements BloodPressureView {
//
//    private List<View> viewList;
//    private BloodPressureLayout1 view1;
//    private BloodPressureLayout2 view2;
//    private QuickeningAdapter mAdapter;
//    private String currentTime;
//    private String mDeviceAddress;
//    private boolean mConnected = false;
//    private BluetoothLeService mBluetoothLeService;
//    private GattUpdateReceiver mGattUpdateReceiver;
//    private boolean isClosed;
//    private BluetoothAdapter mBluetoothAdapter;
//    private boolean isExistBluetooth;
//    private String userId;
//    private static final String TAG = BloodPressureActivity1.class.getSimpleName();
//
//    @Inject
//    LocalConfig mLocalConfig;
//    public BluetoothLeService getmBluetoothLeService() {
//        return mBluetoothLeService;
//    }
//
//    public boolean ismConnected() {
//        return mConnected;
//    }
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setLayoutView(R.layout.activity_bloodpressure);
//        setTitle("血压监测");
//        doInit();
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public boolean isExistBluetooth() {
//        return isExistBluetooth;
//    }
//
//    public void doInit() {
//        isExistBluetooth = true;
//        userId = getIntent().getStringExtra("userId");
//        mPresenter.setUserId(userId);
//        mPresenter.registerView(this);
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        mDeviceAddress = mLocalConfig.getBloodPressureDevices();
//        if (mBluetoothAdapter == null) {
//            isExistBluetooth = false;
//        } else if (!mBluetoothAdapter.isEnabled()) {
//            mBluetoothAdapter.enable();
//        }
//        viewList = new ArrayList<>();
//
//        if (getIntent().hasExtra("data")) {
//            currentTime = getIntent().getStringExtra("data");
//        }
//        viewList = new ArrayList<>();
//        mGattUpdateReceiver = new GattUpdateReceiver(new BaseListener.Observable111<Integer, Intent>() {
//            @Override
//            public void onAction(Integer integer) {
//                switch (integer) {
//                    case 0:
//                        break;
//                    case 1:
//                        mConnected = true;
//                        Log.d(TAG, "ACTION_GATT_SERVICES_DISCOVERED : " + mBluetoothLeService.getSupportedGattServices().size());
//                        // Show all the supported services and characteristics on the user interface.
//                        displayGattServices(mBluetoothLeService.getSupportedGattServices());
//                        view1.doConnectSuccess();
////                        DialogManagerUtils.getInstance().closeDialg();
//                        break;
//                    case 2:
////                        if(dialog.isShowing()){
////                            dialog.dismiss();
////                        }
//                        isClosed = false;
//                        mConnected = false;
//                        ToastUtils.getInstance().makeText(BloodPressureActivity1.this, "与设备已断开连接");
//                        view1.doConnectFail();
//                        doStopConnection();
//                        break;
//                    case 3:
//                        isClosed = false;
//                        ToastUtils.getInstance().makeText(BloodPressureActivity1.this, "测量结束");
//                        break;
//                    case 4:
//                        view1.doUpdataView();
//                        break;
//                    case 5:
//                        view1.doUpdataView();
//                        break;
//                }
//            }
//
//            @Override
//            public void onAction1(Intent... t) {
//
//                if (t != null && t.length > 0) {
//                    Intent intent = t[0];
//                    if (intent != null) {
//                        final String systolic = intent.getStringExtra("Systolic");
//                        final String diastolic = intent.getStringExtra("Diastolic");
//                        final String heart = intent.getStringExtra("Heart");
//                        if (systolic != null && !systolic.isEmpty() &&
//                                diastolic != null && !diastolic.isEmpty() &&
//                                heart != null && heart.length() > 0) {
//
//                            view1.doSetText(systolic, diastolic, heart);
//                            //弹出血压值
////                            String data = "高压：" + systolic + "，低压：" + diastolic + "，脉搏：" + heart;
////                            Log.e(TAG, "data : " + data);
////                            Toast.makeText(BloodPressureActivity.this, data, Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                }
//
//            }
//        });
//        if (TextUtils.isEmpty(currentTime)) {
//            view1 = new BloodPressureLayout1(this, new BaseListener.OnItemSelectedListener<BloodPressureModel>() {
//                @Override
//                public void onItemSelectedListener(BloodPressureModel value) {
//                    view2.doClear();
//                    view2.doRequestDate();
////                mScroller.startScroll(mScroller.getCurrX(),mScroller.getCurrY(),mScroller.getCurrX(),getWindowManager().getDefaultDisplay().getHeight()*2);
//                }
//            });
//            viewList.add(view1);
//            currentTime = TimeUtils.getYMDData(0);
//            view2 = new BloodPressureLayout2(this, currentTime);
////            view1.doSetText("200","200","80");
////            doTest(true,44 +"",44 + "");
//            setRightComponent(R.drawable.time_ico, (o) -> {
//                Intent intent1 = new Intent(this, BloodPressureListActivity.class);
//                intent1.putExtra("userId", userId);
//                startActivity(intent1);
//            });
//        } else {
//            view2 = new BloodPressureLayout2(this, currentTime);
//            view2.doHideBar();
//            setTitle("历史详情");
//        }
//        viewList.add(view2);
//        mAdapter = new QuickeningAdapter(mBinding.bloodpressureRv, viewList);
//        mBinding.bloodpressureRv.setLayoutManager(new LinearLayoutManager(this));
//        mBinding.bloodpressureRv.setAdapter(mAdapter);
//    }
//
//    public void startConnectDevice(){
//        if(TextUtils.isEmpty(mLocalConfig.getBloodPressureDevices())){
//            Intent intent = new Intent();
//            intent.setClass(this, BluetoothLeAccessActivity.class);
//            Bundle bundle = new Bundle();
//            intent.putExtras(bundle);
//            startActivityForResult(intent, 0);
//        }else {
//            MyDialog1 dialog1 = new MyDialog1(this);
//            dialog1.setDialogTitle("连接设备");
//            dialog1.doCancleEvent("更换设备")
//                    .subscribe((o)->{
//                        dialog1.dismiss();
//                        Intent intent = new Intent();
//                        intent.setClass(this, BluetoothLeAccessActivity.class);
//                        Bundle bundle = new Bundle();
//                        intent.putExtras(bundle);
//                        startActivityForResult(intent, 0);
//                    });
//            dialog1.doSureEvent("直接连接")
//                    .subscribe((o)->{
//                        dialog1.dismiss();
//                        mDeviceAddress = mLocalConfig.getBloodPressureDevices();
//                        doConnectDevice();
//                    });
//            dialog1.show();
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        doStop();
//    }
//
//    @Override
//    public void isExist(BloodPressureModel model, boolean isEdit) {
//        doShowDialog(model, isEdit);
//    }
//
//    @Override
//    public void renderAllData(BloodPressureListModel model) {
//
//        view2.renderAllData(model);
//    }
//
//    @Override
//    public void createOrUpdateSuccess() {
//        ToastUtils.getInstance().makeText(this, "添加成功");
//    }
//
//    public static IntentFilter makeGattUpdateIntentFilter() {
//        final IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
//        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
//        return intentFilter;
//    }
//
//    public void requestHasData(BloodPressureModel model) {
//        mPresenter.isExist(model);
//    }
//
//    public void requestCreate(BloodPressureModel model) {
//        mPresenter.create(model);
//    }
//
//    public void requestGetAll(String startTime, String endTime) {
//        mPresenter.GetALLList(startTime, endTime);
//    }
//
//    public void doShowDialog(BloodPressureModel model, boolean isEdit) {
//        final MyDialog1 dialog1 = new MyDialog1(this);
//        String message;
//        if (isEdit) {
//            message = "'血压'已有监测数据，是否覆盖已有的数据";
//            dialog1.setmTvCancleTitle("放弃存储");
//            dialog1.setmTvSureTitle("覆盖");
//
//        } else {
//            message = "是否保存数据";
//            dialog1.setmTvCancleTitle("放弃存储");
//            dialog1.setmTvSureTitle("保存");
//        }
//
//        dialog1.setContent(message);
//        dialog1.doCancleEvent()
//                .subscribe((o) -> {
//                    dialog1.dismiss();
//                });
//        dialog1.doSureEvent()
//                .subscribe((o) -> {
//                    dialog1.dismiss();
//                    mPresenter.create(model);
//                });
//        dialog1.show();
//    }
//
//    @Override
//    protected void initializeInjector() {
//        mComponent = DaggerBloodPressureComponent.builder()
//                .applicationComponent(getApplicationComponent())
//                .activityModule(getActivityModule())
//                .bloodPressureModule(new BloodPressureModule()).build();
//        mComponent.inject(this);
//    }
//    /**
//     * 停止监测
//     */
//    public void doStop() {
//
//
//        if (view1 != null && (view1.getRulerViewBottom().getCurrentScale() != 0 || view1.getRulerViewTop().getCurrentScale() != 0)) {
//            final MyDialog1 dialog1 = new MyDialog1(this);
//            dialog1.setContent("您当前正在监测,确定要停止监测吗?");
//            dialog1.doCancleEvent()
//                    .subscribe((o) -> {
//                        dialog1.dismiss();
//                    });
//            dialog1.doSureEvent()
//                    .subscribe((o) -> {
//                        dialog1.dismiss();
//                        finish();
//                    });
//            dialog1.show();
//
//        } else {
//            finish();
//        }
//
//    }
//
//    // Demonstrates how to iterate through the supported GATT
//    // Services/Characteristics.
//    // In this sample, we populate the data structure that is bound to the
//    // ExpandableListView
//    // on the UI.
//    @SuppressLint("NewApi")
//    public void displayGattServices(List<BluetoothGattService> gattServices) {
//        if (gattServices == null)
//            return;
//
//        BluetoothGattService localBluetoothGattService = mBluetoothLeService.getService(BluetoothLeService.UUID_WANKANG_SEVICES);
//        mBluetoothLeService.mBluetoothService = localBluetoothGattService;
//
//        mBluetoothLeService.h2s = localBluetoothGattService.getCharacteristic(BluetoothLeService.UUID_HOST2SLAVE);
//        mBluetoothLeService.s2h = localBluetoothGattService.getCharacteristic(BluetoothLeService.UUID_SLAVE2HOST);
//
//        if (mBluetoothLeService.s2h != null) {
//            mBluetoothLeService.setCharacteristicNotification(mBluetoothLeService.s2h, true);
//        }
//
//        //和血压计蓝牙设备握手
//        mBluetoothLeService.test_send();
//    }
//
//
//    public void doReConnection(boolean flag) {
//        if (flag) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (!mConnected) {
//                        doStopConnection();
//                        doConnectDevice();
//                        doReConnection(isClosed);
//                        LogMessage.doLogMessage(TAG, "重新连接设备 : " + isClosed);
//                    }
//                }
//            }, 60 * 1000);
//        }
//    }
//
//    public void setClosed(boolean closed) {
//        isClosed = closed;
//    }
//
//    public void doStopConnection() {
//        if (mBluetoothLeService != null) {
//            unbindService(mServiceConnection);
//        }
//        mBluetoothLeService = null;
//    }
//
//    public void doConnectDevice() {
//
////        mDeviceAddress = TypeApi.BLUETOOTH_DEVICE_MAC_BLOOD_PRESSURE;
//        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
//        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
////        dialog.show();
//        //注册接收消息
//        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//        view1.doConnecting();
//        doReConnection(isClosed);
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (resultCode) {
//            // 结果返回  已连接
//            case RESULT_OK:
//                //获取Bundle的数据
//                Bundle bl = data.getExtras();
//                mDeviceAddress = bl.getString("deviceaddress");
//                mLocalConfig.setBloodPressureDevices(mDeviceAddress);
//                doConnectDevice();
//                setClosed(true);
//                break;
//            default:
//                break;
//        }
//    }
//
//    // Code to manage Service lifecycle.
//    private ServiceConnection mServiceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder service) {
//            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
//            Log.e(TAG, "create mBluetoothLeService");
//            if (!mBluetoothLeService.initialize()) {
//                Log.e(TAG, "Unable to initialize Bluetooth");
//                finish();
//            }
//            Log.e(TAG, "begin to connect..." + mDeviceAddress);
//            // Automatically connects to the device upon successful start-up initialization.
//            boolean ret = mBluetoothLeService.connect(mDeviceAddress);
//            Log.e(TAG, "mBluetoothLeService.connect: " + ret);
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            mBluetoothLeService = null;
//            Log.e(TAG, "onServiceDisconnected : mBluetoothLeService = null");
//        }
//    };
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
//        if (mBluetoothLeService != null) {
//            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
//            Log.d(TAG, "Connect request result=" + result);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(mGattUpdateReceiver);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        doStopConnection();
//        mServiceConnection = null;
//
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            doStop();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
}
