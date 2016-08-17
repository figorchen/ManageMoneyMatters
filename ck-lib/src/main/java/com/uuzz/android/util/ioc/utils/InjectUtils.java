package com.uuzz.android.util.ioc.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Size;
import android.util.SizeF;
import android.view.LayoutInflater;
import android.view.View;

import com.uuzz.android.ui.activity.UUZZActivity;
import com.uuzz.android.ui.fragment.UUZZFragment;
import com.uuzz.android.util.BaseArrayHelper;
import com.uuzz.android.util.Utils;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.EventBase;
import com.uuzz.android.util.ioc.annotation.SaveInstance;
import com.uuzz.android.util.ioc.annotation.ViewInject;
import com.uuzz.android.util.ioc.proxy.ListenerInvocationHandler;
import com.uuzz.android.util.log.UUZZLog;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：公用工具包<br/>
 * 类  名: InjectUtils<br/>
 * 类描述: 用于注入<br/>
 * @author 谌珂 <br/>
 * 版    本：1.0.0<br/>
 * 修改时间： 2015/12/20
 */
public class InjectUtils {

	private static UUZZLog logger = new UUZZLog("InjectUtils");

	/**
	 * 注入（布局、控件、事件）
	 * @param activity 需要注入的类
	 */
	public static void inject(UUZZActivity activity){
		injectContentView(activity);
		injectViews(activity);
		injectEvents(activity);
	}

	/**
	 * 注入（布局、控件、事件）
	 * @param fragment 需要注入的类
	 */
	public static void inject(UUZZFragment fragment){
		injectViews(fragment);
		injectEvents(fragment);
	}

	/**
	 * 描 述：获取所有父类的class<br/>
	 * 作 者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/7/5 注释 <br/>
	 */
	private static List<Class> getSuperClasses(Class cls, Class finalClass) {
		if(finalClass == null) {
			finalClass = Object.class;
		}
		String finalClassName = finalClass.getName();
		List<Class> classes = new ArrayList<>();
		classes.add(cls);
		Class superClass = cls;
		while (true) {
			superClass = superClass.getSuperclass();
			classes.add(superClass);
			if(TextUtils.equals(finalClassName, superClass.getName())) {
				break;
			}
		}
		Collections.reverse(classes);
		return classes;
	}

	/**
	 * 给控件注入事件
	 * @param activity
	 */
	public static void injectEvents(Activity activity) {
		try {
			List<Class> classes = getSuperClasses(activity.getClass(), UUZZActivity.class);
			for (Class clazz : classes ) {
				//获取带有OnClick注解的方法
				Method[] methods = clazz.getDeclaredMethods();
				for (Method method : methods) {
					//带有EventBase注解的注解
					Annotation[] annotations = method.getAnnotations();
					for (Annotation annotation : annotations) {
						//获取注解上的注解（有可能@Target、@Retention、@EventBase）
						Class<?> annotationType = annotation.annotationType();
						//指定注解上的注解类型EventBase
						EventBase eventBase = annotationType.getAnnotation(EventBase.class);
						//有可能没有EventBase注解
						if(eventBase == null){
							continue;
						}

						//有，获取事件3要素
						//设置监听的方法名称
						String listenerSetter = eventBase.listenerSetter();
						//监听接口
						Class<?> listenerType = eventBase.listenerType();
						//回调方法的名称
						String callBackMethod = eventBase.callBackMethod();
						//当callBackMethod方法执行时，实际要执行method
						Map<String, Method> methodMap = new HashMap<String, Method>();
						methodMap.put(callBackMethod, method);

						/**
						 * view.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
						}
						}
						 */
						//要给视图注册事件监听，首先要有视图，获取到view id再通过findViewById就可以拿到视图对象
						//@OnClick({R.id.btn_forward,R.id.btn_back})
						//注解的value方法
						Method valueMethod = annotationType.getDeclaredMethod("value");
						int[] viewIds = (int[])valueMethod.invoke(annotation);
						//获取viewIds所有的视图
						for (int viewId : viewIds) {
							View view = activity.findViewById(viewId);
							//给每一个view注册事件监听
							if(view == null){
								continue;
							}

							//执行
							//代理模式：控制访问
							//目标对象：事件监听对象
							//动态生成一个listenerType的代理对象
							ListenerInvocationHandler handler = new ListenerInvocationHandler(activity,methodMap);
							//new Class<?>[]{listenerType} 有什么接口，动态代理对象就会实现什么接口
							//也就是说，传什么接口，就可以成为什么类型的代理对象
							//经典总结：Java动态代理，只能通过实现接口的方式
							Object proxy = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class<?>[]{listenerType}, handler);

							//client客户端
							//获取设置事件监听方法
							Method setEventListenerMtd = view.getClass().getMethod(listenerSetter, listenerType);
							if(setEventListenerMtd != null) {
								setEventListenerMtd.invoke(view, proxy);
								return;
							}
//							setEventListenerMtd = view.getClass().getMethod(listenerSetter, null);
//							setEventListenerMtd.invoke(null, proxy);

						}

					}
				}
			}
		} catch (Exception e) {
			logger.i("inject event failed!", e);
		}
	}

	/**
	 * 给控件注入事件
	 * @param fragment
	 */
	private static void injectEvents(UUZZFragment fragment) {
		try {
			List<Class> classes = getSuperClasses(fragment.getClass(), UUZZFragment.class);
			for (Class clazz : classes ) {
				//获取带有OnClick注解的方法
				Method[] methods = clazz.getDeclaredMethods();
				for (Method method : methods) {
					//我们要的是带有EventBase注解的注解
					Annotation[] annotations = method.getAnnotations();
					for (Annotation annotation : annotations) {
						//获取注解上的注解（有可能@Target、@Retention、@EventBase）
						Class<?> annotationType = annotation.annotationType();
						//指定注解上的注解类型EventBase
						EventBase eventBase = annotationType.getAnnotation(EventBase.class);
						//有可能没有EventBase注解
						if (eventBase == null) {
							continue;
						}

						//有，获取事件3要素
						//设置监听的方法名称
						String listenerSetter = eventBase.listenerSetter();
						//监听接口
						Class<?> listenerType = eventBase.listenerType();
						//回调方法的名称
						String callBackMethod = eventBase.callBackMethod();
						//当callBackMethod方法执行时，实际要执行method
						Map<String, Method> methodMap = new HashMap<String, Method>();
						methodMap.put(callBackMethod, method);

						/**
						 * view.setOnClickListener(new View.OnClickListener() {

						@Override public void onClick(View v) {
						}
						}
						 */
						//要给视图注册事件监听，首先要有视图，获取到view id再通过findViewById就可以拿到视图对象
						//@OnClick({R.id.btn_forward,R.id.btn_back})
						//注解的value方法
						Method valueMethod = annotationType.getDeclaredMethod("value");
						int[] viewIds = (int[]) valueMethod.invoke(annotation);
						//获取viewIds所有的视图
						for (int viewId : viewIds) {
							View view = fragment.getmRootView().findViewById(viewId);
							//给每一个view注册事件监听
							if (view == null) {
								continue;
							}
							//获取设置事件监听方法
							Method setEventListenerMtd = view.getClass().getMethod(listenerSetter, listenerType);
							//执行
							//代理模式：控制访问
							//目标对象：事件监听对象
							//动态生成一个listenerType的代理对象
							ListenerInvocationHandler handler = new ListenerInvocationHandler(fragment, methodMap);
							//new Class<?>[]{listenerType} 有什么接口，动态代理对象就会实现什么接口
							//也就是说，传什么接口，就可以成为什么类型的代理对象
							//经典总结：Java动态代理，只能通过实现接口的方式
							Object proxy = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class<?>[]{listenerType}, handler);

							//client客户端
							setEventListenerMtd.invoke(view, proxy);
						}

					}
				}
			}
		} catch (Exception e) {
			logger.i("inject event failed!", e);
		}
	}


	/**
	 * 注入视图，视图注入到属性上
	 * @param activity
	 */
	private static void injectViews(UUZZActivity activity) {
		try {
			List<Class> classes = getSuperClasses(activity.getClass(), UUZZActivity.class);
			for (Class clazz : classes ) {
				//通过Class获取类的属性列表
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					//只获取带有ViewInject注解的属性
					ViewInject viewInject = field.getAnnotation(ViewInject.class);
					if (viewInject == null) {
						continue;
					}
					//控件的id，也就是需要给属性注入的控件
					int viewId = viewInject.value();

						//通过findViewById获取到控件的实例
						//Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
						//Object view = method.invoke(activity, viewId);
						View view = activity.findViewById(viewId);
						//允许访问私有的属性
						field.setAccessible(true);
						//把控件赋值给属性
						field.set(activity, view);
				}
			}
		} catch (Exception e) {
			logger.i("inject view failed!", e);
		}
	}

	/**
	 * 注入视图，视图注入到属性上
	 * @param view 需要注入的目标View集合
	 * @param o 需要被注入的对象
	 */
	public static void injectViews(View view, Object o) {
		try {
			List<Class> classes = getSuperClasses(o.getClass(), null);
			for (Class clazz : classes ) {
				//通过Class获取类的属性列表
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					//只获取带有ViewInject注解的属性
					ViewInject viewInject = field.getAnnotation(ViewInject.class);
					if (viewInject == null) {
						continue;
					}
					//控件的id，也就是需要给属性注入的控件
					int viewId = viewInject.value();
					View v = view.findViewById(viewId);

					//通过findViewById获取到控件的实例
					//Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
					//Object view = method.invoke(activity, viewId);
					//允许访问私有的属性
					field.setAccessible(true);
					//把控件赋值给属性
					field.set(o, v);
				}
			}
		} catch (Exception e) {
			logger.i("inject view failed!", e);
		}
	}

	/**
	 * 注入视图，视图注入到属性上
	 * @param fragment
	 */
	private static void injectViews(UUZZFragment fragment) {
		try {
			List<Class> classes = getSuperClasses(fragment.getClass(), UUZZFragment.class);
			for (Class clazz : classes ) {
				//通过Class获取类的属性列表
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					//只获取带有ViewInject注解的属性
					ViewInject viewInject = field.getAnnotation(ViewInject.class);
					if (viewInject == null) {
						continue;
					}
					//控件的id，也就是需要给属性注入的控件
					int viewId = viewInject.value();

					//通过findViewById获取到控件的实例
					//Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID, int.class);
					//Object view = method.invoke(activity, viewId);
					View view = fragment.getmRootView().findViewById(viewId);
					//允许访问私有的属性
					field.setAccessible(true);
					//把控件赋值给属性
					field.set(fragment, view);
				}
			}
		} catch (Exception e) {
			logger.i("inject view failed!", e);
		}
	}



	/**
	 * 注入布局
	 * @param activity
	 */
	private static void injectContentView(UUZZActivity activity) {
		//通过对象获取到Mainctivity Class
		Class<?> clazz = activity.getClass();
		try {
			//获取Mainctivity Class上的ContentView注解
			ContentView contentView = clazz.getAnnotation(ContentView.class);
			if(contentView == null) {
				logger.i("Annotation ContentView is null!");
				return;
			}
			//获取注解的值（布局id）
			int layoutId = contentView.value();
			//设置布局
			LayoutInflater inflater = LayoutInflater.from(activity);
			View v = inflater.inflate(layoutId, null);
			activity.getmContentView().addView(v, 0);
		} catch (Exception e) {
			logger.i("inject content view failed!", e);
		}
	}

	/**
	 * 注入布局
	 * @param activity
	 */
	public static void injectContentView(Activity activity) {
		//通过对象获取到Mainctivity Class
		Class<?> clazz = activity.getClass();
		try {
			//获取Mainctivity Class上的ContentView注解
			ContentView contentView = clazz.getAnnotation(ContentView.class);
			if(contentView == null) {
				logger.i("Annotation ContentView is null!");
				return;
			}
			//获取注解的值（布局id）
			int layoutId = contentView.value();
			activity.setContentView(layoutId);
		} catch (Exception e) {
			logger.i("inject content view failed!", e);
		}
	}

	/**
	 * 注入布局
	 * @param fragment
	 */
	public static View injectContentView(Fragment fragment) {
		//通过对象获取到Mainctivity Class
		Class<?> clazz = fragment.getClass();
		try {
			//获取Mainctivity Class上的ContentView注解
			ContentView contentView = clazz.getAnnotation(ContentView.class);
			if(contentView == null) {
				logger.i("Annotation ContentView is null!");
				return new View(fragment.getActivity());
			}
			//获取注解的值（布局id）
			int layoutId = contentView.value();
			//设置布局
			LayoutInflater inflater = LayoutInflater.from(fragment.getActivity());
			return inflater.inflate(layoutId, null);
		} catch (Exception e) {
			logger.i("inject content view failed!", e);
			return new View(fragment.getActivity());
		}
	}

	/**
	 * 描 述：当activity被kill之前执行的保存数据的方法<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
	 * @param obj 需要保存数据的实例
	 * @param bundle Bundle对象
	 */
	public static void saveInstances(Object obj, Bundle bundle){
		if(bundle == null){
			return;
		}
		//通过对象获取到Mainctivity Class
		Class<?> clazz = obj.getClass();

		Class<? extends Bundle> cls = bundle.getClass();   //Bundle的class，用于获取具体方法
		Field[] fields;
		//保存父类的数据
		fields = clazz.getFields();
		saveFields(obj, bundle, cls, fields);
		//保存本类的数据
		fields = clazz.getDeclaredFields();
		saveFields(obj, bundle, cls, fields);
	}

	private static void saveFields(Object obj, Bundle bundle, Class<? extends Bundle> cls,  Field[] fields) {
		SaveInstance lSaveInstance;
		for (Field field : fields) {
			//只获取带有NeedSave注解的属性
			lSaveInstance = field.getAnnotation(SaveInstance.class);
			if(lSaveInstance == null){
				continue;
			}
			String fieldName = field.getName();             //属性名
			try {
				field.setAccessible(true);
				Object value = field.get(obj);             //获取属性值
				if(value == null){
					continue;
				}
				String fieldTypeName = field.getType().getSimpleName();
				switch (fieldTypeName){       //根据属性的类型名称put进bundle，如果匹配到了则进入下一轮循环
					case "int" :
					case "Integer" :
						bundle.putInt(fieldName, (Integer) value);
						continue;
					case "int[]" :
						bundle.putIntArray(fieldName, (int[]) value);
						continue;
					case "Integer[]" :
						bundle.putIntArray(fieldName, BaseArrayHelper.getIntArray((Integer[]) value));
						continue;
					case "boolean" :
					case "Boolean" :
						bundle.putBoolean(fieldName, (Boolean) value);
						continue;
					case "boolean[]" :
						bundle.putBooleanArray(fieldName, (boolean[]) value);
						continue;
					case "Boolean[]" :
						bundle.putBooleanArray(fieldName, BaseArrayHelper.getBooleanArray((Boolean[]) value));
						continue;
					case "byte" :
					case "Byte" :
						bundle.putByte(fieldName, (Byte) value);
					case "byte[]" :
						bundle.putByteArray(fieldName, (byte[]) value);
						continue;
					case "Byte[]" :
						bundle.putByteArray(fieldName, BaseArrayHelper.getByteArray((Byte[]) value));
						continue;
					case "float" :
					case "Float" :
						bundle.putFloat(fieldName, (Float) value);
						continue;
					case "float[]" :
						bundle.putFloatArray(fieldName, (float[]) value);
						continue;
					case "Float[]" :
						bundle.putFloatArray(fieldName, BaseArrayHelper.getFloatArray((Float[]) value));
						continue;
					case "double" :
					case "Double" :
						bundle.putDouble(fieldName, (Double) value);
						continue;
					case "double[]" :
						bundle.putDoubleArray(fieldName, (double[]) value);
						continue;
					case "Double[]" :
						bundle.putDoubleArray(fieldName, BaseArrayHelper.getDoubleArray((Double[]) value));
						continue;
					case "char" :
					case "Character" :
						bundle.putChar(fieldName, (Character) value);
						continue;
					case "char[]" :
						bundle.putCharArray(fieldName, (char[]) value);
						continue;
					case "Character[]" :
						bundle.putCharArray(fieldName, BaseArrayHelper.getCharacterArray((Character[]) value));
						continue;
					case "short" :
					case "Short" :
						bundle.putShort(fieldName, (Short) value);
					case "short[]" :
						bundle.putShortArray(fieldName, (short[]) value);
						continue;
					case "Short[]" :
						bundle.putShortArray(fieldName, BaseArrayHelper.getShortArray((Short[]) value));
						continue;
					case "long" :
					case "Long" :
						bundle.putLong(fieldName, (Long) value);
						continue;
					case "long[]" :
						bundle.putLongArray(fieldName, (long[]) value);
						continue;
					case "Long[]" :
						bundle.putLongArray(fieldName, BaseArrayHelper.getLongArray((Long[]) value));
						continue;
					case "CharSequence" :
						bundle.putCharSequence(fieldName, (CharSequence) value);
						continue;
					case "CharSequence[]" :
						bundle.putCharSequenceArray(fieldName, (CharSequence[]) value);
						continue;
					case "String" :
						bundle.putString(fieldName, (String) value);
						continue;
					case "String[]" :
						bundle.putStringArray(fieldName, (String[]) value);
						continue;
					case "Size" :
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
							bundle.putSize(fieldName, (Size) value);
						}else {
							logger.i("can not put Size object into Bundle with low android version:" + Build.VERSION.SDK_INT);
						}
						continue;
					case "SizeF" :
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
							bundle.putSizeF(fieldName, (SizeF) value);
						} else {
							logger.i("can not put SizeF object into Bundle with low android version:" + Build.VERSION.SDK_INT);
						}
						continue;
					default:
						logger.i("can not find the type with field, it will find by interface type.");
						break;
				}

				Method lMethod = null;     //Bundle最终要调用的方法
				//判断是不是List类型的属性，如果是则传入Integer,String,CharSequence,Parcelable四种类型
				if(field.getType().isAssignableFrom(List.class)) {
					//获取泛型的类型，其中Parcelable类型的为接口，其他是基本类型
					Class<?> lTClass = Utils.getFieldTClass(field);    //泛型类型的class
					String lTName = lTClass.getSimpleName();           //泛型的名称
					if (TextUtils.equals(lTName, "Integer")) {
						lMethod = cls.getDeclaredMethod("putIntegerArrayList", String.class, ArrayList.class);
					} else if (TextUtils.equals(lTName, "String")) {
						lMethod = cls.getDeclaredMethod("putStringArrayList", String.class, ArrayList.class);
						lMethod.invoke(bundle, fieldName, value);
					} else if (TextUtils.equals(fieldName, "CharSequence")) {
						lMethod = cls.getDeclaredMethod("putCharSequenceArrayList", String.class, ArrayList.class);
					} else {
						Class<?>[] interfaces = lTClass.getInterfaces();    //泛型类型实现的所有接口
						for (Class<?> lInterface : interfaces) {
							if(!TextUtils.equals(lInterface.getSimpleName(), "Parcelable")){
								continue;
							}
							lMethod = cls.getDeclaredMethod("putParcelableArrayList", String.class, ArrayList.class);
							break;
						}
					}
					if(lMethod != null){     //把值放进Bundle中
						lMethod.invoke(bundle, fieldName, value);
						continue;
					}
				}

				//判断是不是实现了Parcelable或Serializable接口的类型
				if(fieldTypeName.endsWith("[]")){                          //如果是数组下标结尾则一定是Parcelable[]
					Class<?> lFieldTypeComponentType = field.getType().getComponentType();      //数组元素的类型class
					Class<?>[] interfaces = lFieldTypeComponentType.getInterfaces();    //获取数组元素属性类型实现的所有接口
					for (Class<?> lInterface : interfaces) {
						if(!TextUtils.equals(lInterface.getSimpleName(), "Parcelable")){
							continue;
						}
						lMethod = cls.getDeclaredMethod("putParcelableArray", String.class, Parcelable[].class);
						break;
					}
				}else{                                                     //否则是Parcelable或Serializable类型
					Class<?>[] interfaces = field.getType().getInterfaces();     //获取属性类型实现的所有接口
					for (Class<?> lInterface : interfaces) {
						if(TextUtils.equals(lInterface.getSimpleName(), "Parcelable")){
							lMethod = cls.getDeclaredMethod("putParcelable", String.class, Parcelable.class);
							break;
						} else if(TextUtils.equals(lInterface.getSimpleName(), "Serializable")) {
							lMethod = cls.getDeclaredMethod("putSerializable", String.class, Serializable.class);
							break;
						}
					}
				}
				if(lMethod != null){    //把值放进Bundle中
					lMethod.invoke(bundle, fieldName, value);
				}else {
					logger.w("Can not save this instance in Bundle instance:"+field.toString());
				}
			} catch (Exception e) {
				logger.i("save instance failed!" + "field name is:"+fieldName, e);
			}
		}
	}

	/**
	 * 描 述：<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
	 * @param obj 需要恢复数据的实例
	 * @param bundle Bundle对象
	 */
	public static void restoreInstance(Object obj, Bundle bundle){
		if(bundle == null){
			return;
		}
		//通过对象获取到Mainctivity Class
		Class<?> clazz = obj.getClass();
		Field[] fields = clazz.getDeclaredFields();
		SaveInstance lSaveInstance;
		for (Field field : fields) {
			//只获取带有NeedSave注解的属性
			lSaveInstance = field.getAnnotation(SaveInstance.class);
			if(lSaveInstance == null){
				continue;
			}
			String fieldName = field.getName();
			try {
				field.setAccessible(true);
				field.set(obj, bundle.get(fieldName));
			} catch (Exception e) {
				logger.i("restore instance failed!" + "field name is:"+fieldName, e);
			}
		}
	}
}
