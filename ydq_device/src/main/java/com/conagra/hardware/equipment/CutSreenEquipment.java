package com.conagra.hardware.equipment;

import android.annotation.TargetApi;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yedongqin on 16/9/14.
 *
 * 屏幕录制设备
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CutSreenEquipment implements Runnable{

    private static final String TAG = CutSreenEquipment.class.getSimpleName();

    private static final String VIDEO_MIME_TYPE = "video/avc";
    private static final int IFRAME_INTERVAL = 10;
    private static final int TIMEOUT_US = 10000;


    /**
     * 录制的分辨率
     */
    private int mWidth;
    private int mHeight;

    /**
     * 录制的比特率
     */
    private int mBitRate;

    /**
     * 录制的帧数
     */
    private int mFrameRate;     // 30 FPS

    /**
     * 这个数传 1 就可以
     */
    private int mDpi;

    /**
     * 文件保存路径
     */
    private String mSaveFilePath;

    private MediaProjection mMediaProjection;
    private MediaCodec mVideoEncoder;
    private Surface mSurface;
    private MediaMuxer mMuxer;

    private boolean mMuxerStarted = false;
    private int mVideoTrackIndex = -1;

    private AtomicBoolean mAtomicQuit = new AtomicBoolean(false);

    private MediaCodec.BufferInfo mBufferInfo = new MediaCodec.BufferInfo();

    private VirtualDisplay mVirtualDisplay;


    public CutSreenEquipment(MediaProjection mMediaProjection, String filePath) {
        this(360, 640, 1000000, 1, 5, mMediaProjection, filePath);
    }

    public CutSreenEquipment(int width, int height, int bitRate, int dpi, int fps,
                    MediaProjection mediaProjection, String filePath) {
        this.mWidth = width;
        this.mHeight = height;
        this.mBitRate = bitRate;
        this.mDpi = dpi;
        this.mFrameRate = fps;
        this.mMediaProjection = mediaProjection;
        if(!filePath.endsWith(".mp4"))
            filePath += ".mp4";
        this.mSaveFilePath = filePath;
    }


    public void stopShot() {
        mAtomicQuit.set(true);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void run() {

        Log.e(TAG, "run: " + mWidth + "   " + mHeight + "  " + mBitRate + "  " + mDpi + "  " +
                mFrameRate);

        try {

            prepareEncoder();
            mMuxer = new MediaMuxer(mSaveFilePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            Log.e(TAG, "run: mMuxer init");

            mVirtualDisplay =
                    mMediaProjection.createVirtualDisplay(TAG + "_Video", mWidth, mHeight, mDpi,
                            DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC, mSurface, null, null);
            shotDisplay();
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Muxur:::", e);
        } finally {
            releaseEncoder();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void prepareEncoder() throws IOException {

        MediaFormat format =
                MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, mWidth, mHeight);

        format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
                MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, mBitRate);
        format.setInteger(MediaFormat.KEY_FRAME_RATE, mFrameRate);
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);

        mVideoEncoder = MediaCodec.createEncoderByType(VIDEO_MIME_TYPE);
        mVideoEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);

        mSurface = mVideoEncoder.createInputSurface();

        mVideoEncoder.start();
    }


    private void shotDisplay() {
        while(!mAtomicQuit.get()) {
            int bufferIndex = mVideoEncoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_US);

            if(bufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                resetOutputFormat();
            } else if(bufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
                SystemClock.sleep(10);
            } else if(bufferIndex >= 0) {
                if(!mMuxerStarted) {
                    throw new IllegalStateException("MediaMuxer is  not add the  addTrack format ");
                }

                encodeToVideoTrack(bufferIndex);

                mVideoEncoder.releaseOutputBuffer(bufferIndex, false);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void resetOutputFormat() {

        if(mMuxerStarted) {
            throw new IllegalStateException("format changed");
        }
        MediaFormat newFormat = mVideoEncoder.getOutputFormat();

        mVideoTrackIndex = mMuxer.addTrack(newFormat);
        mMuxer.start();
        mMuxerStarted = true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void encodeToVideoTrack(int bufferIndex) {
        ByteBuffer encodeData = mVideoEncoder.getOutputBuffer(bufferIndex);

        if((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
            mBufferInfo.size = 0;
        }

        if(mBufferInfo.size == 0) {
            encodeData = null;
        } else {
            //还不清楚到哪里去的.
        }

        if(encodeData != null) {
            encodeData.position(mBufferInfo.offset);
            encodeData.limit(mBufferInfo.offset + mBufferInfo.size);

            mMuxer.writeSampleData(mVideoTrackIndex, encodeData, mBufferInfo);
        }


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void releaseEncoder() {
        if(mVideoEncoder != null) {
            mVideoEncoder.stop();
            mVideoEncoder.release();
            mVideoEncoder = null;
        }

        if(mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }

        if(mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }

        if(mMuxer != null) {
            mMuxer.stop();
            mMuxer.release();
            mMuxer = null;
        }

        mBufferInfo = null;
        mVideoTrackIndex = -1;

    }



}
