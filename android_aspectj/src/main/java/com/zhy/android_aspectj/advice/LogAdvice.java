package com.zhy.android_aspectj.advice;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LogAdvice  {

    private static final String POINTCUT_METHOD =
            "execution(@com.zhy.android_aspectj.annotation.Log  * *..*.*(..))";
//            "execution(@com.zhy.android_aspectj.annotation.Log **(..))";

    @Pointcut(POINTCUT_METHOD)
    public void methodAnnotationed(){
    }

    @After("methodAnnotationed()")
    public void doLog(ProceedingJoinPoint point) throws Throwable {

//        Signature signature = point.getSignature();
//        com.zhy.android_aspectj.annotation.Log log = signature.getClass().getAnnotation(com.zhy.android_aspectj.annotation.Log.class);
//        point.proceed();
        Log.v("AspectJ","log : "   + point.getKind());
    }

}
