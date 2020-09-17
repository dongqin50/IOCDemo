package com.zhy.iocdemo.aop.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class PointCut {

    @Pointcut("execution(@com.zhy.iocdemo.aop.annotation.Log **.*(..))}")
    public static void logMethodAnnotationed(){
    }
}
