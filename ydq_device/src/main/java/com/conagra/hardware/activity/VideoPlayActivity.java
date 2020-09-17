package com.conagra.hardware.activity;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.conagra.R;
import com.conagra.cache.Cache;
import com.conagra.databinding.ActivityFetalheartBinding;
import com.conagra.di.component.DaggerFealHeartComponent;
import com.conagra.di.component.FealHeartComponent;
import com.conagra.di.module.FealHeartModule;
import com.conagra.hardware.database.HeartRateDao;
import com.conagra.hardware.model.FetalHeartRecordModel;
import com.conagra.hardware.widget.MyDialog1;
import com.conagra.mvp.model.DownInfo;
import com.conagra.mvp.model.HeartListRowBean;
import com.conagra.mvp.presenter.VideoPlayPresenter;
import com.conagra.mvp.service.CountDownService;
import com.conagra.mvp.ui.activity.DragBaseAppCompatActivity;
import com.conagra.mvp.ui.view.VideoPlayView;
import com.conagra.mvp.utils.LogMessage;
import com.conagra.mvp.utils.StoreAddressUtils;
import com.conagra.mvp.utils.TimeUtils;
import com.xiaoye.myutils.utils.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yedongqin on 16/9/30.
 */
public class VideoPlayActivity extends DragBaseAppCompatActivity<ActivityFetalheartBinding,VideoPlayPresenter,
        FealHeartComponent> implements VideoPlayView {

    private HeartListRowBean heartBean;        //胎动记录本
    private String[] mPiont1List;
    private List<String> mPiont2List;
    private boolean isRecording;
    private OnServiceConnect mServiceConnect;
    private static final int STOP_STATUS_BACK = 0;
    private static final int STOP_STATUS_NORMAL = 1;
    private static final int STOP_STATUS_FORCE = 2;
    private MediaPlayer mediaPlayer;

    private final int isPreparing = 1; //正在准备中，
    private final int isPreparedSuccess = 2; //准备成功
    private final int isClickedAndIsPreparing = 3;//已经点击开始但是仍然在准备中
    private final int isPlaying = 4;//正在播放
    private final int isPlayed = 5;//正在播放
    private int playstatus = isPreparing; //播放状态
    private AudioManager am;
    int currentVolume;
    int maxVolume;
    private HeartRateDao heartRateDao;

//    @Inject
//    VideoPlayPresenter mPresenter;

    //存放音效的HashMap

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView(R.layout.activity_fetalheart);
//        initializeInjector();
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

    public void doInit() {
        mPresenter.registerView(this);
        Intent dataIntent = getIntent();
        if (dataIntent == null || dataIntent.getExtras() == null || !dataIntent.getExtras().containsKey("data")) {
            return;
        }

        heartBean = (HeartListRowBean) dataIntent.getSerializableExtra("data");
        if (heartBean == null) {
            finish();
            return;
        }
        heartRateDao = new HeartRateDao(this);
        doRequestData("");
        if (heartBean.getPiontValue() != null && heartBean.getPiontValue().contains(","))
            mPiont1List = heartBean.getPiontValue().split(",");
//        mPiont2List = heartBean.getPionts2();

        setTitle("历史记录");
        mBinding.feltalheartEndTd.setVisibility(View.GONE);
        mBinding.fetalheartStartJcbt.setText("开始播放");
        mBinding.fetalheartJc1.setText("停止");
        mBinding.fetalheartJc.setText("播放");
        mServiceConnect = new OnServiceConnect();
//        mHeader.setOtherText("");
//        mHeader.doBack()
//                .subscribe(new Action1() {
//                    @Override
//                    public void call(Object o) {
//
//                    }
//                });
        mBinding.fetalheartBluetooth.setVisibility(View.GONE);
        //开始监测
        mBinding.fetalheartStartJcbt.setOnClickListener((v) ->
                doStart()
        );
        mBinding.feltalheartEndJcBt.setOnClickListener((v) ->
                doStop(STOP_STATUS_FORCE)
        );
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        boolean flag = false;
//        heartBean.setAudioURL("http://114.55.93.20:8095/Uploads/0539SSL/CustomerInfo/d53e60a9-da8e-4318-a58e-66d07499119c/FetalHeartAudio/20161229/Audio_deke.wav");
        String path = null;
        String fileName = heartBean.getAudioFileName().substring(0,heartBean.getAudioFileName().indexOf("."));
        if (!TextUtils.isEmpty(heartBean.getAudioURL())) {

            path = heartRateDao.selectPathByFileName(fileName);

            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                if (file.exists()) {
                    flag = true;
                }
            }
        }
        final String filePath = path;
        final boolean fileFlag = flag;
        if (fileFlag || !TextUtils.isEmpty(heartBean.getAudioURL())) {
            final Uri uri = Uri.parse(Cache.getServerAddress() +heartBean.getAudioURL());
            mediaPlayer = new MediaPlayer();
            if (fileFlag) {
                play(filePath);
            } else if(!TextUtils.isEmpty(Cache.getServerAddress()  +heartBean.getAudioURL())){
                play(uri);
                DownInfo info = new DownInfo(heartBean.getAudioURL(),null);
                info.setSavePath(StoreAddressUtils.FELDSHER_MUSIC_DIRECTORY + fileName);
                mPresenter.downLoad(info);
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (playstatus == isClickedAndIsPreparing) {
                        playstatus = isPreparedSuccess;
                        doStart();
                    } else {
                        playstatus = isPreparedSuccess;
                    }
                }
            });
            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Toast.makeText(getApplicationContext(), "音频文件已损坏！", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    LogMessage.doLogMessage("cc", " onCompletion playstatus" + playstatus);
//                        if(playstatus != isPlaying) {
                    playstatus = isPlayed;
                    stop();
//                        }
                    if (fileFlag) {
                        play(filePath);
                    } else {
                        play(uri);
                    }
                }
            });
        }
//        mBinding.feltalheartEndTd.setText(heartBean.getc);
//        RxView.clicks(mBinding.feltalheartEndTd)
//                .throttleFirst(3000,TimeUnit.MILLISECONDS)
//                .subscribe(new Action1<Void>() {
//                    @Override
//                    public void call(Void aVoid) {
//                        int num = Integer.parseInt(mBinding.feltalheartEndTd.getText().toString()) + 1;
//                        mBinding.feltalheartEndTd.setText(String.valueOf(num));
//                        mBinding.feltalheartTvTd.setText(String.valueOf(num));
//
//                    }
//                });

    }

    @Override
    public void downloadSuccess(String path) {
        FetalHeartRecordModel fetalMoveListsModel = new FetalHeartRecordModel();
        fetalMoveListsModel.setFileName(heartBean.getAudioFileName().substring(0,heartBean.getAudioFileName().indexOf(".")));
        fetalMoveListsModel.setDownloadUrl(heartBean.getAudioURL());
        fetalMoveListsModel.setAudiofsUrl(heartBean.getAudioURL());
        fetalMoveListsModel.setAvgHeartRate(heartBean.getAvgHeartRate());
        fetalMoveListsModel.setCustomerNo(heartBean.getCustomerID());
        fetalMoveListsModel.setPionts1(heartBean.getPiontValue());
        fetalMoveListsModel.setPionts2(heartBean.getPiontUterine());
        fetalMoveListsModel.setAudiofs(path + ".mp3");

        heartRateDao.add(fetalMoveListsModel);
    }

    @Override
    public void downloadError() {

    }

    private void doRequestData(String FetalHeartCode) {

//        HardwareNetWorkRequest.doGetFetalheartPointListByRnage(this,FetalHeartCode,new MySubscriber1<BaseBackBean>(){
//            @Override
//            public void onBackError(String message) {
//                super.onBackError(message);
//            }
//
//            @Override
//            public void onBackNext(String obj) {
//                super.onBackNext(obj);
//            }
//        });

    }

    private void play(Uri uri) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(this, uri);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void play(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始监测
     */
    private void doStart() {
        LogMessage.doLogMessage("cc", " doStart playstatus" + playstatus);
        if (playstatus == isPreparedSuccess) {
            playstatus = isPlaying;
            isRecording = true;
            mediaPlayer.start();
            heartBean.setStartTime(TimeUtils.getCurrentTime());
            mBinding.feltalheartStartJcrl.setVisibility(View.GONE);
            mBinding.feltalheartEndJcRl.setVisibility(View.VISIBLE);
            mBinding.fetalheartFv.doStart();  //开始动画
            if (mServiceConnect.getCountDownService() == null) {
                Intent intent = new Intent(this, CountDownService.class);
                bindService(intent, mServiceConnect, Context.BIND_AUTO_CREATE);
            } else {
                mServiceConnect.doStart();
            }
//        //获取BPM值
            Observable.interval(1000, TimeUnit.MILLISECONDS)
                    .takeWhile(new Predicate<Long>() {
                        @Override
                        public boolean test(Long aLong) throws Exception {
                            return aLong < heartBean.getTimes() && mBinding.fetalheartFv.isStart();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {

                        }
                    });
        } else {
            ToastUtils.getInstance().makeText(VideoPlayActivity.this, "正在准备中");
            playstatus = isClickedAndIsPreparing;
        }
    }

    /**
     * 停止监测
     */
    private void doStop(final int index) {
//        playstatus = isPreparing;
        switch (index) {
            case STOP_STATUS_BACK:
                if (isRecording) {
                    final MyDialog1 myDialog1 = new MyDialog1(VideoPlayActivity.this);
                    myDialog1.setContent("您当前正在播放历史记录,确定要停止吗?");
                    myDialog1.doSureEvent()
                            .subscribe(new Consumer() {

                                @Override
                                public void accept(Object o) throws Exception {
                                    myDialog1.dismiss();
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
                } else {
                    finish();
                }
                break;
            case STOP_STATUS_FORCE:
                if (isRecording) {
                    final MyDialog1 myDialog1 = new MyDialog1(VideoPlayActivity.this);
                    myDialog1.setContent("您当前正在播放历史记录,确定要停止吗?");
                    myDialog1.doSureEvent()
                            .subscribe(new Consumer() {

                                @Override
                                public void accept(Object o) throws Exception {
                                    myDialog1.dismiss();
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
                if (isRecording) {
                    stop();
                }
                break;
        }
    }

    private void stop() {
        LogMessage.doLogMessage("cc", " stop playstatus" + playstatus);
//        soundPool.stop(soundId);
        if (playstatus == isPlaying || playstatus == isPlayed) {
            if (mServiceConnect != null && mServiceConnect.device != null)
                mServiceConnect.device.setStarting(false);
            isRecording = false;
            if (mBinding.fetalheartFv != null)
                mBinding.fetalheartFv.doStop();
            if (playstatus == isPlaying) {
                if (mediaPlayer != null && mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                try {
                    if (mediaPlayer != null)
                        mediaPlayer.prepareAsync();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            mBinding.feltalheartTvTime.setText("00:00");
            mBinding.feltalheartTvBpm.setText("--");
            mBinding.feltalheartStartJcrl.setVisibility(View.VISIBLE);
            mBinding.feltalheartEndJcRl.setVisibility(View.GONE);
            mBinding.feltalheartEndTd.setText("0");
            mBinding.feltalheartTvTd.setText("0");
//            mediaPlayer.reset();
//            mediaPlayer.prepareAsync();

            playstatus = isPreparing;
        }
    }

    public class OnServiceConnect implements ServiceConnection {
        private CountDownService device;

        public CountDownService getCountDownService() {
            return device;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            device = ((CountDownService.CountDownBinder) service).getCountDownService();
            doStart();
        }

        public void doStart() {
            if (device == null) {
                return;
            }
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
//            soundPool.play(soundId,//声音资源
//                    volumnRatio,//左声道
//                    volumnRatio,//右声道
//                    1,//优先级
//                    0,//循环次数，0是不循环，-1是一直循环
//                    1);
            device.getCode(VideoPlayActivity.this, Math.round(Float.valueOf(Double.valueOf(heartBean.getTimes()).toString())))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .filter(new Predicate() {
                        @Override
                        public boolean test(Object o) throws Exception {
                            if (mPiont1List == null) {
                                return false;
                            }
                            return true;
                        }
                    }).subscribe(new Consumer<Long>() {

                @Override
                public void accept(Long value) throws Exception {
                    if (value == heartBean.getTimes() - 1) {
//                                mBinding.fetalheartFv.doStop();
//                                Log.v("cc"," getCode playstatus" + playstatus);
//                                doStop(STOP_STATUS_NORMAL);
                        return;
                    }
                    value = value + 1;
//                           mBinding.feltalheartTvTime.setText();
                    int min = Math.round(value / 60);
                    int sec = Math.round(value % 60);
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

                    int position = Integer.parseInt(value.toString());
                    String result = "0";
                    if (position < mPiont1List.length)
                        result = mPiont1List[position];
                    String gysResult = "0";
                    mBinding.fetalheartFv.addValue(result, gysResult);
                    if (!TextUtils.isEmpty(gysResult)) {
                        mBinding.feltalheartTvGsy.setText(gysResult);
                    } else {
                        mBinding.feltalheartTvGsy.setText("0");
                    }
                    if (!TextUtils.isEmpty(result)) {
                        mBinding.feltalheartTvBpm.setText(result);
                    } else {
                        mBinding.feltalheartTvBpm.setText("0");
                    }
                }
            });
        }

        //
        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceConnect.getCountDownService() != null) {
            mServiceConnect.getCountDownService().setStarting(false);
            unbindService(mServiceConnect);
        }
//      soundPool.stop(soundId);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                doStop(STOP_STATUS_BACK);
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_UP && keyCode != KeyEvent.KEYCODE_BACK) {
                    if (currentVolume < maxVolume) {
                        currentVolume = currentVolume + 1;
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
                    } else {
                        am.setStreamVolume(AudioManager.STREAM_MUSIC,
                                currentVolume, 0);
                    }
                }
                return false;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_UP) {

                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        doStop(STOP_STATUS_BACK);
                        return true;
                    }

                    if (currentVolume > 0) {
                        currentVolume = currentVolume - 1;
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
                    } else {
                        am.setStreamVolume(AudioManager.STREAM_MUSIC,
                                currentVolume, 0);
                    }
                }
                return false;
            default:
                return super.dispatchKeyEvent(event);
        }
    }


}