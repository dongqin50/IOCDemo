package com.conagra.mvp.bean;

/**
 * Created by yedongqin on 2017/1/6.
 */

public class UploadBackBean{


    /**
     * fileName : 1483005202866.wav
     * files_DownloadURL : http://114.55.93.20:8099/api/Upload/DownloadFile?fileName=Audio_2017010617075075039437b1e621e4a858a05f26f1d59f1f8.wav&newName=1483005202866.wav
     * files_URL : http://114.55.93.20:8095/Uploads/0539SSL/CustomerInfo/d53e60a9-da8e-4318-a58e-66d07499119c/FetalHeartAudio/ 20170106/Audio_2017010617075075039437b1e621e4a858a05f26f1d59f1f8.wav
     */

    private String fileName;
    private String files_DownloadURL;
    private String files_URL;
    private String ServerFileRelativePath;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFiles_DownloadURL() {
        return files_DownloadURL;
    }

    public void setFiles_DownloadURL(String files_DownloadURL) {
        this.files_DownloadURL = files_DownloadURL;
    }

    public String getFiles_URL() {
        return files_URL;
    }

    public void setFiles_URL(String files_URL) {
        this.files_URL = files_URL;
    }

    public String getServerFileRelativePath() {
        return ServerFileRelativePath;
    }

    public void setServerFileRelativePath(String serverFileRelativePath) {
        ServerFileRelativePath = serverFileRelativePath;
    }
}
