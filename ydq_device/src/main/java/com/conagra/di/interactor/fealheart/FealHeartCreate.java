package com.conagra.di.interactor.fealheart;

import com.conagra.di.UseCase;
import com.conagra.di.repository.FealHeartRepository;

import java.io.File;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

//import com.facebook.react.modules.network.ProgressRequestBody;

/**
 * Created by Dongqin.Ye on 2017/10/31.
 */

public class FealHeartCreate extends UseCase {

    /**
     * 患者ID
     */
    public final static String CUSTOMER_ID = "CustomerID";
    public final static String HOSPITAL_ID = "HospitalID";
    /**
     * 开始时间
     */
    public final static String START_TIME = "StartTime";
    /**
     * 时长
     */
    public final static String TIMES = "Times";
    /**
     * 平均心率
     */
    public final static String AVG_HEART_RATE = "AvgHeartRate";
    /**
     * 胎心点的值

     */
    public final static String POINT_VALUE = "PiontValue";
    /**
     * 宫缩压点的值
     */
    public final static String PIONT_U_TERINE = "PiontUterine";
    /**
     * 音频数据流
     */
    public final static String AUDIO_URL = "AudioURL";


    private FealHeartRepository repository;

    @Inject
    public FealHeartCreate(FealHeartRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Observable buildUseCaseObservable(Object params) {
        Map map = (Map) params;
        String url = (String) map.get(AUDIO_URL);
        map.remove(AUDIO_URL);
        return repository.Create(
                getBodyAboutUpload((String) map.get(CUSTOMER_ID)),
                getBodyAboutUpload((String) map.get(START_TIME)),
                getBodyAboutUpload((String) map.get(TIMES)),
                getBodyAboutUpload((String) map.get(AVG_HEART_RATE)),
                getBodyAboutUpload((String) map.get(POINT_VALUE)),
                getBodyAboutUpload((String) map.get(PIONT_U_TERINE)),
                uploadMusic(url));
    }

    private MultipartBody.Part uploadMusic(String audioURL){
        File file=new File(audioURL);
        if(!file.exists()){
            return null;
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/otcet-stream"),file);
        MultipartBody.Part body =
// MultipartBody.Part.createFormData("file_name", file.getName(), requestBody);
                MultipartBody.Part.createFormData("AudioURL", file.getName(), requestBody);
//        MultipartBody.Part part= MultipartBody.Part.createFormData("file_name", file.getName(), new ProgressRequestBody(requestBody,new UploadProgressListener(){
//            @Override
//            public void onProgress(long currentBytesCount, long totalBytesCount) {
//
//            }
//        }));
//        UplaodApi uplaodApi = new UplaodApi(httpOnNextListener,this);
//        uplaodApi.setPart(part);
//        HttpManager manager = HttpManager.getInstance();
//        manager.doHttpDeal(uplaodApi);
        return body;
    }

    public interface UploadProgressListener {
        /**
         * 上传进度
         * @param currentBytesCount
         * @param totalBytesCount
         */
        void onProgress(long currentBytesCount, long totalBytesCount);
    }
}
