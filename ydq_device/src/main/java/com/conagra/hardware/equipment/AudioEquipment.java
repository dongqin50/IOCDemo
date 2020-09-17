package com.conagra.hardware.equipment;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * Created by yedongqin on 16/9/18.
 */
public class AudioEquipment {
    private Context context;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;


    public AudioEquipment(Context context) {
        this.context = context;

//        //获取对应音频采样率下的最小缓冲区的大小
//          int pwMinBufferSize = AudioTrack.getMinBufferSize(EncoderCore.getPowerSupplySamplerate(),
//                AudioFormat.CHANNEL_OUT_STEREO,
//                AudioFormat.ENCODING_PCM_8BIT);
//        int pwMinBufferSize = AudioTrack.getMinBufferSize(22050,
//                AudioFormat.CHANNEL_OUT_STEREO,
//                AudioFormat.ENCODING_PCM_8BIT);
////        //新建AudioTrack
//        AudioTrack pwAT = new AudioTrack(AudioManager.STREAM_MUSIC,
//                        22050,
//                        AudioFormat.CHANNEL_OUT_MONO,
//                        AudioFormat.ENCODING_PCM_8BIT,
//                        pwMinBufferSize*2,
//                        AudioTrack.MODE_STATIC);
////
////        ③将缓冲区中的音频数据写入音频播放线程并设置左右声道音量等
//        byte[] carrierSignal = new byte[22050];
//        pwAT.write(carrierSignal, 0,22050);//写入音频数据
//        pwAT.flush();//刷新
//        pwAT.setStereoVolume(1, 0);//设置左右声道播放音量
//        pwAT.setLoopPoints(0, 22050, -1);//设置音频播放循环点
//        pwAT.play();//开始播放
////        this.context = context;
//        doListening();
    }


    private void  doListening(){
//        audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
//        audioManager.setMode(AudioManager.ROUTE_EARPIECE);
//        if(audioManager.isWiredHeadsetOn()){
//            LogMessage.doLogMessage("","");
//            audioManager.getRouting(AudioManager.ROUTE_EARPIECE);
//        }
////        android.hardware.C
//        audioManager.setR
//        AudioManager.ROUTE_EARPIECE




    }


}
