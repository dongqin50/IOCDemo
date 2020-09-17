package com.conagra.di.interceptor;

import com.conagra.di.requestBody.DownloadResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by yedongqin on 2018/3/15.
 */

public class DownloadInterceptor implements Interceptor {

//    private DownloadProgressListener listener;

    public DownloadInterceptor() {
//        this.listener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        return originalResponse.newBuilder()
                .body(new DownloadResponseBody(originalResponse.body()))
                .build();
    }
}

