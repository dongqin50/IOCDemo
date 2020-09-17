package com.zhy.iocdemo.aop.advice;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import static android.content.ContentValues.TAG;

@Aspect
public class FilterMethodAdvice {

    @Pointcut("(call(* *..aspectJTest()))&&withincode(* *..aspectJ2())")
    public void invokeAspectJTestInAspectJ2() {
    }

    @Before("invokeAspectJTestInAspectJ2()")
    public void beforeInvokeaspectJTestInAspectJ2(JoinPoint joinPoint)  {
        Log.e(TAG, "method:" + getMethodName(joinPoint).getName());
    }

    private MethodSignature getMethodName(JoinPoint joinPoint) {
        if (joinPoint == null) return null;
        return (MethodSignature) joinPoint.getSignature();
    }
}
