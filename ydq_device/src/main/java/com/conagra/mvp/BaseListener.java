package com.conagra.mvp;

import android.content.ComponentName;
import android.os.IBinder;

/**
 * Created by yedongqin on 16/9/23.
 */
public interface BaseListener {

     interface OnItemSelectedListener<T>{
        void onItemSelectedListener(T value);
     }
    interface OnItemSelectedListener1<T>{
        void onItemSelectedListener(T value, Integer... integer);
     }

    interface OnItemDeleteListener{
        void onItemDeleteListener(int position);
    }

    interface OnServiceConnection{
        public void onServiceConnected(ComponentName name, IBinder service);
        public void onServiceDisconnected(ComponentName name);
    }

    interface Subscriber<T>{
        void onComplted();
        void onNext(T t);
        void onError(Exception e);
    }

    interface Observable<T>{
        void onAction(T t);
    }

    interface Observable1<M,T>{
        void onAction(M m);
        void onAction1(T t);
    }
    interface Observable11<T,R,M>{
        void onAction(T t);
        void onAction1(R t);
        void onAction2(M t);
    }

    interface Observable111<T,R>{
        void onAction(T t);
        void onAction1(R... t);
    }


    interface Observable2<T>{
        void onAction(T t);
        void onAction1(T t);
        void onAction2(T t);
    }


}
