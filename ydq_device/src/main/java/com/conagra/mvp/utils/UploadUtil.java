package com.conagra.mvp.utils;

import android.text.TextUtils;
import android.util.Log;

import com.conagra.mvp.bean.BaseBackBean;
import com.conagra.mvp.bean.UploadBackBean;
import com.conagra.mvp.constant.MsgStatus;
import com.conagra.mvp.model.MySubscriber;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yedongqin on 2016/12/27.
 */
public class UploadUtil {
    private static UploadUtil uploadUtil;
    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private UploadUtil() {

    }

    /**
     * 单例模式获取上传工具类
     * @return
     */
    public static UploadUtil getInstance() {
        if (null == uploadUtil) {
            uploadUtil = new UploadUtil();
        }
        return uploadUtil;
    }
    private static final String TAG = "UploadUtil";
    private int readTimeOut = 10 * 1000; // 读取超时
    private int connectTimeout = 10 * 1000; // 超时时间
    /***
     * 请求使用多长时间
     */
    private static int requestTime = 0;
    private static final String CHARSET = "utf-8"; // 设置编码
    /***
     * 上传成功
     */
    public static final int UPLOAD_SUCCESS_CODE = 1;
    /**
     * 文件不存在
     */
    public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
    /**
     * 服务器出错
     */
    public static final int UPLOAD_SERVER_ERROR_CODE = 3;
    protected static final int WHAT_TO_UPLOAD = 1;
    protected static final int WHAT_UPLOAD_DONE = 2;

    /**
     * android上传文件到服务器
     *
     * @param filePaths
     * 需要上传的文件的路径
     * @param fileKey
     * 在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL
     * 请求的URL
     */
    public void uploadFileByStringList(List<String> filePaths, String  fileKey, String RequestURL,
                           Map<String, String> param, MySubscriber subscriber) {
        if (filePaths == null) {
            return;
        }
        try {
            List<File> files = new ArrayList<>();
            for(String path : filePaths){
                File f = new File(path);
                if(f.exists()){
                    files.add(f);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * android上传文件到服务器
     *
     * @param files
     * 需要上传的文件
     * @param fileKey
     * 在网页上<input type=file name=xxx/> xxx就是这里的fileKey
     * @param RequestURL
     * 请求的URL
     */
    public void uploadFileByFileList(final List<File> files, final String fileKey,
                           final String RequestURL, final Map<String, String> param,final MySubscriber subscriber) {
        if (files == null) {
            return;
        }

        new Thread(new Runnable() { //开启线程上传文件
            @Override
            public void run() {
                toUploadFile(files, fileKey, RequestURL, param,subscriber);
            }
        }).start();

    }

    private void toUploadFile(List<File> files, String fileKey, String RequestURL,
                             Map<String, String> param, MySubscriber subscriber) {
        String result = null;
        requestTime= 0;

        long requestTime = System.currentTimeMillis();
        long responseTime = 0;

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
        //            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    /**
     * 当文件不为空，把文件包装并且上传
     */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;
            String params = "";
            /***
             * 以下是用于上传参数
             */
            if (param != null && param.size() > 0) {
                Iterator<String> it = param.keySet().iterator();
                while (it.hasNext()) {
                    sb = null;
                    sb = new StringBuffer();
                    String key = it.next();
                    String value = param.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                    sb.append(value).append(LINE_END);
                    params = sb.toString();
                    Log.i(TAG, key+"="+params+"##");
                    dos.write(params.getBytes());
                }
            }

            sb = null;
            params = null;

    /**
    * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
    * filename是文件的名字，包含后缀名的 比如:abc.png
    *
    */
            int i = 0;
            for(File file :files) {

//                if(file.exists()){
//                    FileInputStream fis = new FileInputStream(file);
//                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                    int length = 0;
//                    byte[] buffer = new byte[1024];
//                    while ((length = fis.read(buffer)) != -1){
//                        bos.write(buffer,0,length);
//                    }
//                    bos.flush();
//                    bos.close();
//                    fis.close();
//                    byte[] b = bos.toByteArray(); // 将图片流以字符串形式存储下来
//                    String  tp = new String(Base64Coder.encodeLines(b));//转换后的字符串，可将该字符串上传至服务器端进行解码
//                    sb = null;
//                    sb = new StringBuffer();
//                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
//                    sb.append("Content-Disposition: form-data; name=\"").append(fileKey).append("\"").append(LINE_END).append(LINE_END);
//                    sb.append(tp).append(LINE_END);
//                    params = sb.toString();
//                    dos.write(params.getBytes());
//                }

            }
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
//            dos.write(Base64.encode(end_data,Base64.DEFAULT));
            dos.flush();
            dos.close();
//
    // dos.write(tempOutputStream.toByteArray());
    /**
     * 获取响应码 200=成功 当响应成功，获取响应的流
     */
            int res = conn.getResponseCode();
            responseTime = System.currentTimeMillis();
            this.requestTime = (int) ((responseTime-requestTime)/1000);
            Log.e(TAG, "response code:" + res);
            if (res == 200) {
                Log.e(TAG, "request success");
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int ss;
                byte[] buffer = new byte[1024];
                while ((ss = input.read(buffer)) != -1) {
                    bos.write(buffer,0,ss);
                }
                bos.flush();
                bos.close();
                input.close();
                result = new String(bos.toByteArray(),"UTF-8");
                Log.e(TAG, "result : " + result);
                Gson gson = new GsonBuilder().create();
                BaseBackBean baseBackBean = gson.fromJson(result,BaseBackBean.class);
                if(baseBackBean.getStatus() == MsgStatus.STATUS_SUCCESS){
                    Object obj = baseBackBean.getData();
                    if (obj != null){
//                        subscriber.onNext(obj);
                    }else {
                        subscriber.onNext(null);
                    }
                    subscriber.onCompleted();
                }else {
                    subscriber.onBackError(baseBackBean.getMessage());
                }
                return;
            } else {
                Log.e(TAG, "request error");
                subscriber.onError(new Exception("网络异常"));
                return;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            subscriber.onError(e);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            subscriber.onError(e);
            return;
        }
    }

    public void toUploadFile1(final List<File> files, final String fileKey, final String RequestURL,
                              final Map<String, String> param, final MySubscriber<List<UploadBackBean>> subscriber) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                toUploadFile2(files,fileKey,RequestURL,param,subscriber);
            }
        }).start();
    }

    /**
     *
     * @param files
     * @param fileKey
     * @param RequestURL
     * @param param
     */
    public void toUploadFile2(List<File> files, String fileKey, String RequestURL,
                              Map<String, String> param, MySubscriber<List<UploadBackBean>> subscriber) {
        String result = null;
        requestTime= 0;

        long requestTime = System.currentTimeMillis();
        long responseTime = 0;

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setConnectTimeout(60000);
//            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
//            conn.addRequestProperty("FileName",file.getName());
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            /**
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;
            String params = "";
        /***
         * 以下是用于上传参数
         */
            if (param != null && param.size() > 0) {
                Iterator<String> it = param.keySet().iterator();
                while (it.hasNext()) {
                    sb = null;
                    sb = new StringBuffer();
                    String key = it.next();
                    String value = param.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                    sb.append(value).append(LINE_END);
                    params = sb.toString();
                    Log.i(TAG, key+"="+params+"##");
                    dos.write(params.getBytes());
            //                    dos.write(Base64.encode(params.getBytes(),Base64.DEFAULT));
                // dos.flush();
                }
            }
            sb = null;
            params = null;

            if (files != null) {
                for (File file : files) {

                    if(!file.exists()){
                        continue;
                    }
                    int i = 0;

                    i++;
                    sb = new StringBuffer();
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition:form-data; name=\"" + fileKey
                            + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
                    sb.append("Content-Type:application/octet-stream" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
                    sb.append(LINE_END);
                    params = sb.toString();
                    sb = null;
                    Log.i(TAG, file.getName() + "=" + params + "##");
                    dos.write(params.getBytes());
//                dos.write(Base64.encode(params.getBytes(),Base64.DEFAULT));
/**上传文件*/
                    InputStream is = new FileInputStream(file);
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    int curLen = 0;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    while ((len = is.read(bytes)) != -1) {
                        dos.write(bytes, 0, len);
                    }
                    is.close();
                    bos.flush();
                    bos.close();
                    dos.write(LINE_END.getBytes("utf-8"));
                }
            }
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            dos.close();
            // dos.write(tempOutputStream.toByteArray());
            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            responseTime = System.currentTimeMillis();
            this.requestTime = (int) ((responseTime-requestTime)/1000);
            Log.e(TAG, "response code:" + res);
            if (res == 200) {
                Log.e(TAG, "request success");
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int ss;
                byte[] buffer = new byte[1024];
                while ((ss = input.read(buffer)) != -1) {
                    bos.write(buffer,0,ss);
                }
                bos.flush();
                bos.close();
                input.close();
                result = new String(bos.toByteArray(),"UTF-8");
                if(!TextUtils.isEmpty(result)){
                    while (result.contains("\\")) {
                        result = result.replace("\\","");
                    }
                    result = result.substring(1,result.length()-1);
                    Gson gson = new GsonBuilder().create();

                    BaseBackBean baseBackBean = gson.fromJson(result,BaseBackBean.class);
                    if(baseBackBean.getStatus() == MsgStatus.STATUS_SUCCESS){
                        Object object = baseBackBean.getData();
                        if(object != null){
                            String data = gson.toJson(baseBackBean.getData());
                            if(!TextUtils.isEmpty(data)){
                                List<UploadBackBean> uploadBackBeanList = new ArrayList<>();
                                try {
                                    JSONArray jsonArray = new JSONArray(data);
                                    for(int i = 0;i < jsonArray.length(); i++){

                                        UploadBackBean backBean = gson.fromJson(jsonArray.getString(i),UploadBackBean.class);
                                        if(backBean != null){
                                            uploadBackBeanList.add(backBean);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                                subscriber.onNext(uploadBackBeanList);
                            }
                        }
//                        subscriber.onCompleted();
                    }else {
                        subscriber.onBackError("上传失败");
                    }
                }else {
                    subscriber.onBackError("上传失败");
                }
                return;
            } else {
                Log.e(TAG, "request error");
                subscriber.onBackError("上传失败");
                return;
            }
        } catch (MalformedURLException e) {
            subscriber.onError(e);
            e.printStackTrace();
            return;
        } catch (IOException e) {
            subscriber.onError(e);
            e.printStackTrace();
            return;
        }
    }

    public void uploadFile(File file, String fileKey, String RequestURL,
                              Map<String, String> param, MySubscriber subscriber) {
        String result = null;
        requestTime= 0;

        long requestTime = System.currentTimeMillis();
        long responseTime = 0;

        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
//            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if(file != null && file.exists())
                conn.addRequestProperty("FileName",file.getName());
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            /**
             * 当文件不为空，把文件包装并且上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;
            String params = "";
/***
 * 以下是用于上传参数
 */
            if (param != null && param.size() > 0) {
                Iterator<String> it = param.keySet().iterator();
                while (it.hasNext()) {
                    sb = null;
                    sb = new StringBuffer();
                    String key = it.next();
                    String value = param.get(key);
                    sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    sb.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(LINE_END).append(LINE_END);
                    sb.append(value).append(LINE_END);
                    params = sb.toString();
                    Log.i(TAG, key+"="+params+"##");
                    dos.write(params.getBytes());
//                    dos.write(Base64.encode(params.getBytes(),Base64.DEFAULT));
// dos.flush();
                }
            }
            sb = null;
            params = null;
            if (file != null && file.exists()){
                sb = new StringBuffer();
                sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                sb.append("Content-Disposition:form-data; name=\"" + fileKey
                        + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type:application/octet-stream" + LINE_END); // 这里配置的Content-type很重要的 ，用于服务器端辨别文件的类型的
                sb.append(LINE_END);
                params = sb.toString();
                sb = null;
                Log.i(TAG, file.getName() + "=" + params + "##");
                dos.write(params.getBytes());
//                dos.write(Base64.encode(params.getBytes(),Base64.DEFAULT));
/**上传文件*/
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                int curLen = 0;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                bos.flush();
                bos.close();
                dos.write(LINE_END.getBytes("utf-8"));
            }

            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            dos.close();
            // dos.write(tempOutputStream.toByteArray());
            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            responseTime = System.currentTimeMillis();
            this.requestTime = (int) ((responseTime-requestTime)/1000);
            Log.e(TAG, "response code:" + res);
            if (res == 200) {
                Log.e(TAG, "request success");
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int ss;
                byte[] buffer = new byte[1024];
                while ((ss = input.read(buffer)) != -1) {
                    bos.write(buffer,0,ss);
                }
                bos.flush();
                bos.close();
                input.close();
                result = new String(bos.toByteArray(),"UTF-8");
                Log.e(TAG, "result : " + result);
                if(!TextUtils.isEmpty(result)){
                    BaseBackBean baseBackBean = new GsonBuilder().create().fromJson(result,BaseBackBean.class);
                    if(baseBackBean.getStatus() == MsgStatus.STATUS_SUCCESS){
//                        subscriber.onNext(baseBackBean.getData());
                        subscriber.onCompleted();
                    }else {
                        subscriber.onBackError("上传失败");
                    }
                }
                return;
            } else {
                Log.e(TAG, "request error");
                subscriber.onBackError("上传失败");
                return;
            }
        } catch (MalformedURLException e) {
            subscriber.onError(e);
            e.printStackTrace();
            return;
        } catch (IOException e) {
            subscriber.onError(e);
            e.printStackTrace();
            return;
        }
    }
}