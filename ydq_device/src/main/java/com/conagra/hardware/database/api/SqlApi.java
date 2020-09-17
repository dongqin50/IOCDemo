package com.conagra.hardware.database.api;

import static android.provider.ContactsContract.Directory.PACKAGE_NAME;

/**
 * Created by yedongqin on 16/9/29.
 */
public interface SqlApi {

    int DATABASE_VERSION = 1;
    String DATABASE_NAME = "feldsher.db";
    String DATABASE_PATH = "/data/data/" + PACKAGE_NAME + "/heartrate.sql";
    //心率表
    String DATABASE_TABLE_HEART_RATE = "heartrate";
    String DATABASE_TABLE_HEART_RATE_TEST = "heartrate_test";
    String HEART_RATE_ID = "id";
    String HEART_RATE_TIME = "time";  //总耗时
    String HEART_RATE_PATH_MUSIC = "musicPath";//音频路径
    String HEART_RATE_TDCOUNT = "tdCount";//胎动次数
    String HEART_RATE_STARTTIME = "startTime";//开始时间
    String HEART_RATE_HEARTRATE = "heartRate"; //平均心率
    String HEART_RATE_AUDIO_FILE_SIZE= "AudiofileSize"; //音频文件大小
    String HEART_RATE_PIONTS1 = "Pionts1"; //胎心点值
    String HEART_RATE_PIONTS2 = "Pionts2"; //宫缩压点值
    String HEART_RATE_UPLOAD_STTUS = "uploadStatus"; //上传状态，0 未上传 ，1 音频已上传 ，2 上传成功
    String  HEART_RATE_OPEN_URL_MUSIC = "musicUrl"; //播放音频地址
    String HEART_RATE_DOWNLOAD_URL_MUSIC = "downloadUrl";//下载路径
    String HEART_RATE_MUSIC_NAME = "fileName";  //音频名字
    String HEART_RATE_CUSTOMERNO = "customer";  //客户编号
    String HEART_RATE_HOSPITALID = "hospitalId";  //医院


    //创建心率表
    String HEART_RATE_TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE_HEART_RATE +" (" +
            HEART_RATE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            HEART_RATE_TIME + " TEXT," +
            HEART_RATE_PATH_MUSIC + " TEXT," +
            HEART_RATE_AUDIO_FILE_SIZE + " TEXT," +
            HEART_RATE_PIONTS1 + "  TEXT," +
            HEART_RATE_PIONTS2 + " TEXT ," +
            HEART_RATE_STARTTIME + " TEXT," +
            HEART_RATE_HOSPITALID + " TEXT," +
            HEART_RATE_CUSTOMERNO + " TEXT," +
            HEART_RATE_DOWNLOAD_URL_MUSIC + " TEXT," +
            HEART_RATE_MUSIC_NAME + " TEXT," +
            HEART_RATE_OPEN_URL_MUSIC + " TEXT," +
            HEART_RATE_UPLOAD_STTUS + " TEXT," +
            HEART_RATE_HEARTRATE + " TEXT);";

    //创建心率表临时表
    String HEART_RATE_TEST_TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE_HEART_RATE_TEST +" (" +
            HEART_RATE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            HEART_RATE_TIME + " TEXT NOT NULL," +
            HEART_RATE_PATH_MUSIC + " TEXT NOT NULL," +
            HEART_RATE_AUDIO_FILE_SIZE + " TEXT NOT NULL," +
            HEART_RATE_PIONTS1 + "  TEXT NOT NULL," +
            HEART_RATE_PIONTS2 + " TEXT NOT NULL," +
            HEART_RATE_STARTTIME + " TEXT NOT NULL," +
            HEART_RATE_HEARTRATE + " TEXT NOT NULL);";

    //删除心率表
    String HEART_RATE_TABLE_DROP = "DROP TABLE if exists " + DATABASE_TABLE_HEART_RATE + ";";
    //删除心率表临时表
    String HEART_RATE_TEST_TABLE_DROP = "DROP TABLE if exists " + DATABASE_TABLE_HEART_RATE_TEST + ";";




}
