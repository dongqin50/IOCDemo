package com.conagra.hardware.equipment;

import android.media.MediaRecorder;
import android.util.Log;

import com.conagra.mvp.utils.StoreAddressUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by yedongqin on 16/9/19.
 */
public class MediaRecorderDemo {
    private final String TAG = "MediaRecord";
    private MediaRecorder mMediaRecorder;
    public static final int MAX_LENGTH = 1000 * 60 * 10;// 最大录音时长1000*60*10;
    private String filePath;
    private boolean isStarting=false;

    public MediaRecorderDemo(){
        this.filePath = StoreAddressUtils.FELDSHER_MUSIC_DIRECTORY  + "/xxx.amr";  //音频地址
        startRecord();
    }

    public MediaRecorderDemo(File file) {
        this.filePath = file.getAbsolutePath();
    }

    private long startTime;
    private long endTime;
    private Observable observableBmp;

    /**
     * 开始录音 使用amr格式
     *
     *            录音文件
     * @return
     */
    public void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */

        if(isStarting){
            return;
        }
        isStarting = true;

        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
            try {
                /* ②setAudioSource/setVedioSource */
                mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
                /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
                mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                            /*
                 * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
                 * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
                 */
                mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                /* ③准备 */
                mMediaRecorder.setOutputFile(filePath);
                mMediaRecorder.setMaxDuration(MAX_LENGTH);
                mMediaRecorder.prepare();
                /* ④开始 */
                mMediaRecorder.start();
                // AudioRecord audioRecord.
                /* 获取开始时间* */
                startTime = System.currentTimeMillis();
                updateMicStatus();
                Log.i("ACTION_START", "startTime" + startTime);

        } catch (IllegalStateException e) {
            Log.i(TAG,
                    "call startAmr(File mRecAudioFile) failed!"
                            + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG,
                    "call startAmr(File mRecAudioFile) failed!"
                            + e.getMessage());
        }
    }

    /**
     * 停止录音
     *
     */
    public long stopRecord() {
        if(!isStarting)
            return 0;

        isStarting = false;

        if (mMediaRecorder == null)
            return 0L;
        endTime = System.currentTimeMillis();
        Log.i("ACTION_END", "endTime" + endTime);
        mMediaRecorder.stop();
        mMediaRecorder.reset();
        mMediaRecorder.release();
        mMediaRecorder = null;
        Log.i("ACTION_LENGTH", "Time" + (endTime - startTime));
        return endTime - startTime;
    }

//    private final Handler mHandler = new Handler();
//    private Runnable mUpdateMicStatusTimer = new Runnable() {
//        public void run() {
//            updateMicStatus();
//        }
//    };

    /**
     * 更新话筒状态
     *
     */
    private int BASE = 1;
    private int SPACE = 1000;// 间隔取样时间

    private void updateMicStatus() {
        if (mMediaRecorder != null) {
            observableBmp = Observable.interval(1000, TimeUnit.MILLISECONDS)
                    .takeWhile(new Func1<Long, Boolean>() {
                        @Override
                        public Boolean call(Long aLong) {
                            return aLong < 6000 && isStarting;
                        }
                    })
                    .filter(new Func1<Long, Boolean>() {
                        @Override
                        public Boolean call(Long aLong) {
                            return isStarting;
                        }
                    }).map(new Func1<Long, Double>() {

                        @Override
                        public Double call(Long aLong) {
                            double ratio = (double)mMediaRecorder.getMaxAmplitude() /BASE;
                            double bpm = 0.0;
                            if(ratio > 1)
                                bpm = 20 * Math.log10(ratio);
                            Log.d(TAG,"分贝值："+bpm);
                            return bpm;
                        }
                    }).subscribeOn(Schedulers.io());

//                    .subscribeOn(Schedulers.io())
//                    .subscribe(new Action1<Long>() {
//                        @Override
//                        public void call(Long aLong) {
//                            if (ratio > 1)
//                                bpm = 20 * Math.log10(ratio);
//                        }
//                    });


//            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    public Observable getBpmObservable(){
        return observableBmp;
    }
}
