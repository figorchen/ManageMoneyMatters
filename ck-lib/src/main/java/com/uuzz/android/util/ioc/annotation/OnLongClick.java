package com.uuzz.android.util.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import android.view.View;

//在方法上使用
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//完整注册点击事件监听需要三个要素
//1.设置监听的方法名称setOnClickListener
//2.监听接口
//3.事件触发之后的回调方法
//通过注解的方式给OnClick注解指定以上三要素
@EventBase(listenerSetter = "setOnLongClickListener", listenerType = View.OnLongClickListener.class, callBackMethod = "onLongClick")
public @interface OnLongClick {

	int[] value();
}
