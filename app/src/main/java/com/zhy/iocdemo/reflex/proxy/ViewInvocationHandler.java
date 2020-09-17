package com.zhy.iocdemo.reflex.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ViewInvocationHandler implements InvocationHandler {

    private Object target;
//    private Map<String,Method> methodMap;
    private Method targetMethod;

    public ViewInvocationHandler(Object target, Method targetMethod) {
        this.target = target;
        this.targetMethod = targetMethod;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if(targetMethod != null){
            return targetMethod.invoke(target,args);
        }

        return method.invoke(target,args);
    }

    private void invokeType(Object proxy, Method method, Object[] args){


    }

    private void invokeField(Object proxy, Method method, Object[] args){

    }

    private void invokeMethod(Object proxy, Method method, Object[] args){

    }



}
