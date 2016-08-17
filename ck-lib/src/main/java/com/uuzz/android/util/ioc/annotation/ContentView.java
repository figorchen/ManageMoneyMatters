package com.uuzz.android.util.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//在类上使用
@Target(ElementType.TYPE)
//在代码运行期，需要读取这个注解的内容
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentView {
	
	int value();
	
}
