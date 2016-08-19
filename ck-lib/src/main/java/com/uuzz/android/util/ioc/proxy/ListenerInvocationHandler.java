package com.uuzz.android.util.ioc.proxy;

import android.app.Activity;
import android.app.Fragment;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 项目名称：公用工具包<br/>
 * 类  名: ListenerInvocationHandler<br/>
 * 类描述: 监听代理的句柄<br/>
 * @author 谌珂 <br/>
 * 版    本：1.0.0<br/>
 * 修改时间： 2015/12/20
 */
public class ListenerInvocationHandler implements InvocationHandler {

	private Map<String, Method> methodMap;
	private Activity activity;
	private Fragment fragment;

	public ListenerInvocationHandler(Activity activity, Map<String, Method> methodMap) {
		this.activity = activity;
		this.methodMap = methodMap;
	}

	public ListenerInvocationHandler(Fragment fragment, Map<String, Method> methodMap) {
		this.fragment = fragment;
		this.methodMap = methodMap;
	}

	//listenerType 中声明的所有方法都是在invoke方法中执行
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		String name = method.getName();
		Method mtd = methodMap.get(name);
		if(mtd != null){
			mtd.setAccessible(true);
			//当代理对象中OnClick方法传入时，我们执行注入类中mClick方法
			if(activity != null) {
				if(mtd.getParameterTypes().length == 0) {
					return mtd.invoke(activity);
				}
				return mtd.invoke(activity, args);
			} else {
				if(mtd.getParameterTypes().length == 0) {
					return mtd.invoke(fragment);
				}
				return mtd.invoke(fragment, args);
			}
		}
		//其他我们不需要拦截的方法，放行
		//OnClickListener有且只有一个方法，但是OnPageChangeListener有多个方法，我们有时只处理一个
		return method.invoke(proxy, args);
	}

}
