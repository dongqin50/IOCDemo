package com.zhy.iocdemo.reflex.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@BindEvent(
        setMethod="setOnClickListener",
        methodName = "onClick",
        interfaceName = View.OnClickListener.class)
public @interface OnClick {

    int[] value() default -1;

}
