package com.conagra.hardware.model;

import java.io.Serializable;

/**
 * Created by yedongqin on 16/9/28.
 */
public class HeartModel implements Serializable {

    private String id;
    private String time;    //总耗时
    private String videoPath;   //视频路径
    private String musicPath;   //音频路径
    private String count;   //胎动次数
    private String startTime;   //开始时间
    private String heartRate;   //平均心率

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
