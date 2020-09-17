package com.conagra.listener;

/**
 * Created by yedongqin on 2018/3/15.
 */

public interface DownloadProgressListener {

    /**
     * 下载进度
     * @param read
     * @param count
     * @param done
     */
    void update(long read, long count, boolean done);
}
