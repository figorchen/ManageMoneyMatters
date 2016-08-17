package com.uuzz.android.util.ioc.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描 述：注册点击回调事件<br/>
 * 作 者：谌珂<br/>
 * 历 史: (版本) 谌珂 2016/2/17 注释 <br/>
 * value:需要注册事件的控件id
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//完整注册点击事件监听需要三个要素
//1.设置监听的方法名称setOnClickListener
//2.监听接口
//3.事件触发之后的回调方法
//通过注解的方式给OnClick注解指定以上三要素
@EventBase(listenerSetter = "setOnClickListener", listenerType = View.OnClickListener.class, callBackMethod = "onClick")
public @interface OnClick {
	int[] value();
}
