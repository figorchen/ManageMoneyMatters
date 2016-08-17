package com.uuzz.android.util.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//在属性上使用
@Target(ElementType.FIELD)
//运行期，读取
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {

	int value();
	
}
