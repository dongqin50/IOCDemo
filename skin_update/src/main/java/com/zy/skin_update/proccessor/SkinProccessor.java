package com.zy.skin_update.proccessor;


import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

//@Aspect
public class SkinProccessor {

    /**
     * 查找所有在onCreate方法中call请求setContentView的地方
     */
    @Pointcut("(call(* *..setContentView(..)))&&withincode(* *..onCreate(..))")
    public void injectSkinAttrs(){

    }

    @Around("injectSkinAttrs()")
    public void aroundInjectAttrs(ProceedingJoinPoint proceedingJoinPoint){

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        signature.getName();
//        signature.

        try {
            proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        Log.v("aaaadddd"," -------------- aroundInjectAttrs  ---------------------");


    }

}
