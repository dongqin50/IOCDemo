package com.zhy.iocdemo.aop.advice;

import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class LogAdvice  {
//
//    private static final String POINTCUT_METHOD =
//            "execution(@com.zhy.iocdemo.aop.annotation.Log  * *..*.*(..))";
////            "execution(@com.zhy.android_aspectj.annotation.Log **(..))";
//
//    @Pointcut(POINTCUT_METHOD)
//    public void methodAnnotationed(){
//    }

    @Around("com.zhy.iocdemo.aop.pointcut.PointCut.logMethodAnnotationed()")
    public void doLog(ProceedingJoinPoint point) throws Throwable {

        Signature signature = point.getSignature();

        if(signature instanceof MethodSignature){

            MethodSignature methodSignature = (MethodSignature) signature;
            com.zhy.iocdemo.aop.annotation.Log log = methodSignature.getMethod().getAnnotation(com.zhy.iocdemo.aop.annotation.Log.class);
            long startTime = System.currentTimeMillis();
            Log.v("AspectJ"," ------------ " + log.value() +"  "  + signature.getName() + " : log before : "   + " ------------");
            point.proceed();
            long endTime = (System.currentTimeMillis()-startTime)/1000;
            Log.v("AspectJ"," ------------ " + log.value() + " " + signature.getName() + "  : spend time on something : " + endTime + " s "   );

        }
    }

}
