package com.uuzz.android.util.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//用于指定事件的要素
//在注解上使用
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBase {

	/**
	 * btn_forward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		}
	 */
	
	/**
	 * 设置监听的方法名称（setOnClickListener）
	 * @return
	 */
	String listenerSetter();
	
	/**
	 * 监听接口（OnClickListener）
	 * @return
	 */
	Class<?> listenerType();
	
	/**
	 * 回调方法的名称（onClick）
	 * @return
	 */
	String callBackMethod();
}
