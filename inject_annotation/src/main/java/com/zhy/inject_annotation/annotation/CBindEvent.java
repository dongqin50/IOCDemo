package com.zhy.inject_annotation.annotation;

import com.zhy.inject_annotation.CBindEventEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface CBindEvent {

      CBindEventEnum listenerType();

      int[] value();
}

