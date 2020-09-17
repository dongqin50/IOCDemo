package com.conagra.di;

import com.conagra.BuildConfig;
import com.conagra.cache.Cache;
import com.conagra.di.requestBody.DownloadResponseBody;
import com.conagra.mvp.constant.NetApi;
import com.conagra.mvp.utils.LogMessage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yedongqin on 2018/3/14.
 */

public class Network {

    private static final int TIMEOUT_CONNECTION = 60;
    private static final int TIMEOUT_READING = 60;
    private Retrofit mRetrofit;
    private OkHttpClient mClient;


    public Network() {
        mClient = getClient();
        mRetrofit = getRetrofit(mClient);
    }

    public <T> T api(Class<T> clazz) {
        if (mRetrofit == null)
            return null;

        return mRetrofit.create(clazz);
    }


    private Retrofit getRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(NetApi.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }


    private OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_READING, TimeUnit.SECONDS)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
        /*添加回调处理类*/
//        ProgressDownSubscriber subscriber=new ProgressDownSubscriber(info);
//        DownloadInterceptor interceptor = new DownloadInterceptor();
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //手动创建一个OkHttpClient并设置超时时间
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(loggingInterceptor);
        builder.addInterceptor(new LogInterceptor());
//        builder.addInterceptor(interceptor);
        return builder.build();
    }

    private class LogInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            HttpUrl url;
            if(chain.request().url().encodedPath().contains("DrSick/Device/")){

                 url = chain.request()
                        .url()
                        .newBuilder()
                        .host(NetApi.SERVER_KC_IP)
                        .port(NetApi.SERVER_KC_PORT)
                        .scheme("http").build();
            }else {
                 url = chain.request()
                        .url()
                        .newBuilder()
                        .host(Cache.getServerIp())
                        .port(Integer.parseInt(Cache.getServerPort()))
                        .scheme("http").build();
            }

            Request.Builder requestBuilder =
                    chain.request().
                            newBuilder().
                            url(url.url());

            if (url.encodedPath().contains("upload/Audio/FetalHeart")) {
                Request request = requestBuilder.build();
                LogMessage.doLogMessage("okhttp1", "request:" + request.url().toString());
                Response response = chain.proceed(request);
                return response.newBuilder()
                        .body(new DownloadResponseBody(
                                response.body())).build();
            } else {
                requestBuilder
                        .header("Content-type", "application/json")
                        .header("Accept", "application/json");
                Request request = requestBuilder.build();
                LogMessage.doLogMessage("okhttp1", "request:" + request.url().toString());

                okhttp3.Response response = chain.proceed(request);
                okhttp3.MediaType mediaType = response.body().contentType();
                String content = response.body().string();
                LogMessage.doLogMessage("okhttp1", "response body:" + content);
                if (response.body() != null) {
                    ResponseBody body = ResponseBody.create(mediaType, content);
                    return response.newBuilder().body(body).build();
                } else {
                    return response;
                }
            }
        }
    }
}
