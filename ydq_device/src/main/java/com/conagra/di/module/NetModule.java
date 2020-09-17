package com.conagra.di.module;

import com.conagra.di.Network;
import com.conagra.di.repository.BloodPressureRepository;
import com.conagra.di.repository.BloodSugarRepository;
import com.conagra.di.repository.FealHeartRepository;
import com.conagra.di.repository.FealMoveRepository;
import com.conagra.di.repository.HardwareManagerRepository;
import com.conagra.di.repository.TempleRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dongqin.Ye on 2017/10/24.
 */

@Module
public class NetModule {
    public static final String COOKIES = "APP_COOKIES";
    private Network network;

    public NetModule() {
        this.network = new Network();
    }

    public Network getNetwork() {
        return network;
    }

    @Singleton
    @Provides
    public BloodSugarRepository provideBloodSugarRepository() {
        return network.api(BloodSugarRepository.class);
    }

    @Singleton
    @Provides
    public BloodPressureRepository provideBloodPressureRepository() {
        return network.api(BloodPressureRepository.class);
    }

    @Singleton
    @Provides
    public FealHeartRepository provideFealHeartRepository() {
        return network.api(FealHeartRepository.class);
    }

    @Singleton
    @Provides
    public FealMoveRepository provideFealMoveRepository() {
        return network.api(FealMoveRepository.class);
    }

    @Singleton
    @Provides
    public TempleRepository provideTempleRepository() {
        return network.api(TempleRepository.class);
    }

    @Singleton
    @Provides
    public HardwareManagerRepository provideHardwareManagerRepository() {
        return network.api(HardwareManagerRepository.class);
    }


//    String mBaseUrl;
//
//    // Constructor needs one parameter to instantiate.
//    public NetModule(String baseUrl) {
//        this.mBaseUrl = baseUrl;
//    }
//
//    // Dagger will only look for methods annotated with @Provides
//    @Provides
//    @Singleton
//    // Application reference must come from AppModule.class
//    SharedPreferences providesSharedPreferences(MainApplication application) {
//        return PreferenceManager.getDefaultSharedPreferences(application);
//    }
//
//    @Provides
//    @Singleton
//    Cache provideOkHttpCache(MainApplication application) {
//        int cacheSize = 10 * 1024 * 1024; // 10 MiB
//        Cache cache = new Cache(application.getCacheDir(), cacheSize);
//        return cache;
//    }
//
//    @Provides
//    @Singleton
//    Gson provideGson() {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
//        return gsonBuilder.create();
//    }
//
//    @Provides
//    @Singleton
//    OkHttpClient provideOkHttpClient(Cache cache) {
//        OkHttpClient client = new OkHttpClient();
////        client.setCache(cache);
//        return client;
//    }
//
//    @Provides
//    @Singleton
//    Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .baseUrl(mBaseUrl)
//                .client(okHttpClient)
//                .build();
//        return retrofit;
//    }

}
