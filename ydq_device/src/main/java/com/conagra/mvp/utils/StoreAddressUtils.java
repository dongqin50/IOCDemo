package com.conagra.mvp.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by yedongqin on 16/9/14.
 *
 * 存储地址
 *
 */
public class StoreAddressUtils {

    /**
     * 本地存储数据地址
     */
    public final static String FELDSHER_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/conagra/";
    /**
     * 存储视频地址
     */
    public final static  String FELDSHER_VEDIO_DIRECTORY = FELDSHER_DIRECTORY + "video/";
    /**
     * 存储图片地址
     */
    public final static  String FELDSHER_IMAGE_DIRECTORY = FELDSHER_DIRECTORY + "image/";
    /**
     * 存储音频地址
     */
    public final static  String FELDSHER_MUSIC_DIRECTORY = FELDSHER_DIRECTORY + "music/";


    static {
        File file = new File(FELDSHER_VEDIO_DIRECTORY);
        if(!file.exists()){
            file.mkdirs();
        }

        File imgFile = new File(FELDSHER_IMAGE_DIRECTORY);
        if(!imgFile.exists()){
            imgFile.mkdirs();
        }

        File musicFile = new File(FELDSHER_MUSIC_DIRECTORY);
        if(!musicFile.exists()){
            musicFile.mkdirs();
        }
    }

    public static void doClear(){
        File file = new File(FELDSHER_VEDIO_DIRECTORY);
        if(file.exists()){
            File[] files = file.listFiles();
            for(File f : files){
                f.delete();
            }
        }

        File imgFile = new File(FELDSHER_IMAGE_DIRECTORY);
        if(imgFile.exists()){
            File[] files = imgFile.listFiles();
            for(File f : files){
                f.delete();
            }
        }

        File musicFile = new File(FELDSHER_MUSIC_DIRECTORY);
        if(musicFile.exists()){
            File[] files = musicFile.listFiles();
            for(File f : files){
                f.delete();
            }
        }
    }




}
