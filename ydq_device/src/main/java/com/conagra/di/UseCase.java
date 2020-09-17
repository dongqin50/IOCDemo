package com.conagra.di;

import com.conagra.mvp.utils.LogMessage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by yedongqin on 2017/10/29.
 */

public abstract class UseCase<T> {

    private Subscription subscription = Subscriptions.empty();
    private Scheduler executor;
    private Scheduler observe;

    public UseCase() {
        executor = Schedulers.io();
        observe = AndroidSchedulers.mainThread();
    }

    public UseCase(Scheduler executor, Scheduler observe) {
        this.executor = executor;
        this.observe = observe;
    }

    protected RequestBody getBody(String params){
        return RequestBody.create(MediaType.parse("application/json"),params);
    }

    protected RequestBody getBodyAboutUpload(String params){
        return RequestBody.create(MediaType.parse("multipart/form-data"),params);
    }




    /**
     * Builds an {@link rx.Observable} which will be used when executing the current {@link UseCase}.
     */
    protected abstract Observable<T> buildUseCaseObservable(Object params);

    /**
     * Executes the current use case.
     *
     * @param useCaseSubscriber The guy who will be listen to the observable build
     * with {@link #buildUseCaseObservable(Object params)}.
     */
    @SuppressWarnings("unchecked")
    public Observable execute(Subscriber useCaseSubscriber, Object params) {
        Observable observable = buildUseCaseObservable(params);
        this.subscription = observable
                .subscribeOn(executor)
                .observeOn(observe)
                .subscribe(useCaseSubscriber);
        return observable;
    }

    @SuppressWarnings("unchecked")
    public Observable execute(Subscriber useCaseSubscriber) {
        Observable observable = buildUseCaseObservable(null);
        this.subscription = observable
                .subscribeOn(executor)
                .observeOn(observe)
                .subscribe(useCaseSubscriber);
        return observable;
    }



    @SuppressWarnings("unchecked")
    public Observable execute(Object params) {
        Observable observable = buildUseCaseObservable(params);
        observable.subscribeOn(executor)
                .observeOn(observe);
        return observable;
    }



    public Observable getObservable(Object params){
        return this.buildUseCaseObservable(params);
    }

    public Observable getObservable(){
        return this.buildUseCaseObservable(null);
    }

    public void execute(Observable observable,Subscriber useCaseSubscriber) {
        subscription = observable.subscribeOn(executor)
                .observeOn(observe)
                .subscribe(useCaseSubscriber);
    }




    /**
     * Unsubscribes from current {@link rx.Subscription}.
     */
    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public boolean isUnsubscribe() {
        return subscription.isUnsubscribed();
    }

    public String buildJsonListBody(List<Map> params){
        JsonArray array = new JsonArray();
        for (Map p:params){
            array.add(makeParamBody(p));
        }
        JsonObject body = new JsonObject();
        body.add("queryBody",array);
        return body.toString();
    }

    public JsonArray buildJsonList(List<Map> params){
        JsonArray array = new JsonArray();
        for (Map p:params){
            array.add(makeParamBody(p));
        }
        return array;
    }

    public String buildJsonBody(Map params){
        JsonObject query = makeParamBody(params);
        LogMessage.doLogMessage("okhttp1", " body:" + query.toString());
        return query.toString();
    }


    private JsonObject makeParamBody(Map params){
        Iterator<Map.Entry> iterator = params.entrySet().iterator();
        JsonObject query = new JsonObject();
        Gson gson = new Gson();
        while (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            if(entry.getValue() instanceof String){
                query.addProperty(entry.getKey().toString(),entry.getValue().toString());
            }else if(entry.getValue() instanceof Integer){
                query.addProperty(entry.getKey().toString(),(Integer)entry.getValue());
            }else if(entry.getValue() instanceof JsonArray){
                query.add(entry.getKey().toString(),((JsonArray)entry.getValue()));
            }else {
                query.add(entry.getKey().toString(),gson.toJsonTree(entry.getValue()));
            }
        }
        return query;
    }
}
