package com.conagra.hardware.model;

import java.io.Serializable;

/**
 * Created by yedongqin on 16/10/8.
 */
public class FetalHeartRecordModel implements Serializable{

    private int id;
    private String  audiofs; //音频数据流地址
    private int audioFileSize;  //音频文件大小
    private String pionts1;  //胎心点值
//    private List<Integer> pionts3;  //胎心点值
    private String pionts2;  //宫缩压点值
//    private List<Integer> pionts4;  //宫缩压点值
    private int times;  //时长
    private String hospitalID;  //医院编码
    private String customerNo;  //客户编号
    private String startTime;   //开始时间
    private int avgHeartRate;    //平均心率
    private String serverpath;      //服务器地址
    private String audiofsUrl;  //网页地址
    private String downloadUrl; //下载地址
    private int uploadStatus;    //上传状态   0 未上传 ，1 音频已上传 ，2 上传成功
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAudiofs() {
        return audiofs;
    }

    public String getPionts1() {
        return pionts1;
    }

    public void setPionts1(String pionts1) {
        this.pionts1 = pionts1;
    }

    public String getPionts2() {
        return pionts2;
    }

    public void setPionts2(String pionts2) {
        this.pionts2 = pionts2;
    }

    public void setAudiofs(String audiofs) {
        this.audiofs = audiofs;
    }

    public String getServerpath() {
        return serverpath;
    }

    public void setServerpath(String serverpath) {
        this.serverpath = serverpath;
    }
    //    public void setPionts3(List<Integer> pionts3) {
//        this.pionts3 = pionts3;
//    }
//
//    public void setPionts4(List<Integer> pionts4) {
//        this.pionts4 = pionts4;
//    }

    public int getAudioFileSize() {
        return audioFileSize;
    }

    public void setAudioFileSize(int audioFileSize) {
        this.audioFileSize = audioFileSize;
    }



//    public List<Integer> getPionts5() {
//
//
//        return pionts3;
//
//    }
//    public List<Integer> getPionts6() {
//
//        return pionts4;
//    }


    public String getAudiofsUrl() {
        return audiofsUrl;
    }

    public void setAudiofsUrl(String audiofsUrl) {
        this.audiofsUrl = audiofsUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(int uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getHospitalID() {
        return hospitalID;
    }

    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getAvgHeartRate() {
        return avgHeartRate;
    }

    public void setAvgHeartRate(int avgHeartRate) {
        this.avgHeartRate = avgHeartRate;
    }
}
