package com.uuzz.android.util;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.uuzz.android.util.log.UUZZLog;

/**
 * 根据bean自动解析xml的工具类。
 * 每个xml的开始标签就代表一个bean，其中的属性必须是基本类型属性，子标签代表一个子bean或集合子bean
 * @author 谌珂
 */
public class XmlParser {

	private static UUZZLog logger = new UUZZLog("XmlParse");

	/**
	 * 描 述：<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
	 * @param is xml文件的输入流
	 * @param inputEncoding 文件编码格式
	 * @param result 解析xml文件后返回的对象
	 * @param clazz 返回对象的class
     * @param <T> 返回对象的类型
     */
	public static <T> void parse(InputStream is, String inputEncoding, T result, Class<?> clazz){
		TagBundle bundle;
		TagBundle finalBundle;
		TagBundle faterBundle;
		// 创建一个堆栈，用来保存解析xml的进度
		Stack<TagBundle> stack = new Stack<TagBundle>();
		XmlPullParser parser = Xml.newPullParser();
		try {
			parser.setInput(is, inputEncoding);
			int type = parser.getEventType();
			while(type != XmlPullParser.END_DOCUMENT){
				switch (type) {
				case XmlPullParser.START_DOCUMENT:
					//整个文档开始,清除一下堆栈，并把栈顶元素定为result的TagBundle
					stack.clear();
					if(result.getClass().isAssignableFrom(ArrayList.class)){
						stack.push(new TagBundle(clazz, result, true));
					}else{
						stack.push(new TagBundle(clazz, result, false));
					}
					break;
				case XmlPullParser.START_TAG:
					bundle = stack.peek();
					Class<?> topClazz = bundle.getClazz();
					Field[] fields = topClazz.getDeclaredFields();
					
					// 为标签属性赋值
					if(bundle.getInstance().getClass().isAssignableFrom(ArrayList.class)){
						Object listInstance = topClazz.newInstance();
						setTagFieldsValue(topClazz, listInstance, parser);
						stack.push(new TagBundle(topClazz, listInstance, false));
					}else{
						setTagFieldsValue(topClazz, bundle.getInstance(), parser);
					}
					
					
					// 判断标签是否是栈顶元素的属性,如果标签名称与栈顶元素的某个属性一致,创建此属性对象并入栈
					for (Field field : fields) {
						field.setAccessible(true);
						Class<?> fieldType = field.getType();
						//如果是基本类型或String类型,直接continue
						if(fieldType.getSimpleName().equals("String") || fieldType.isPrimitive()){
							continue;
						}
						// 如果是List类型，获取到其泛型类型Class，赋值给fieldType，并new一个ArrayList或者从原对象中获取
						if(fieldType.isAssignableFrom(List.class)){
							fieldType = Utils.getFieldTClass(field);
							// 标签名与属性类型名不相同，直接continue
							if(!parser.getName().equals(fieldType.getSimpleName())){
								continue;
							}
							List<Object> arrayList = (ArrayList<Object>) field.get(bundle.getInstance());
							if(arrayList == null){
								arrayList = new ArrayList<Object>();
								field.set(bundle.getInstance(), arrayList);
							}
							// 把当前的操作对象入栈
							stack.push(new TagBundle(fieldType, arrayList, true));
							Object newInstance = fieldType.newInstance();
							setTagFieldsValue(fieldType, newInstance, parser);
							arrayList.add(newInstance);
						}else if(parser.getName().equals(fieldType.getSimpleName())){
							Object newInstance = fieldType.newInstance();
							stack.push(new TagBundle(fieldType, newInstance, false));
							setTagFieldsValue(fieldType, newInstance, parser);
						}
					}
					
//					//类名与标签名、属性名大小写需保持一致
//					//判断标签名与主Bean名是否一致，相同则进入给基本属性赋值
//					if(parser.getName().equals(clazz.getSimpleName())){ 
//						// 拿到开头标签后首先给基本类型赋值
//						for (Field field : fields) {
//							setValue(field, result, parser);
//						}
//						break;
//					}
//					
//					// 标签名与主Bean名不一致，递归遍历主Bean中的自定义属性
//					findField(parser, result);
					break;
				case XmlPullParser.END_TAG:
					// 弹出栈顶元素
					finalBundle = stack.peek();
					if(stack.size() > 1 && parser.getName().equals(finalBundle.getClazz().getSimpleName())){
						stack.pop();
						faterBundle = stack.peek();
						Object fatherInstance = faterBundle.getInstance();
						if(faterBundle.isList()){
							Method method = faterBundle.getInstance().getClass().getDeclaredMethod("add", new Class[]{Object.class});
							method.invoke(fatherInstance, finalBundle.getInstance());
						} else {
							Field[] fatherFields = faterBundle.getClazz().getDeclaredFields();
							for (Field fatherField : fatherFields) {
								fatherField.setAccessible(true);
								if(fatherField.getType().getSimpleName().equals(finalBundle.getClazz().getSimpleName())){
									fatherField.set(fatherInstance, finalBundle.getInstance());
									break;
								}
							}
						}
					}
					break;
				default:
					break;
				}
				type = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.w(parser.getName()+" parser failed!", e);
		}
	}
	
	/**
	 * 给属性赋值（只支持基本类型和String）
	 * @param field 属性
	 * @param instance 赋值对象
	 * @param parser xml解析对象
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static void setValue(Field field, Object instance, XmlPullParser parser) throws IllegalArgumentException, IllegalAccessException{
		Class<?> newInstanceFieldType = field.getType();
		field.setAccessible(true);
		String value = parser.getAttributeValue(null, field.getName());
		if(value != null){
			if(newInstanceFieldType.getSimpleName().equals("String") || newInstanceFieldType.getSimpleName().equals("char")){ 
				//给String类型赋值
				field.set(instance, parser.getAttributeValue(null, field.getName()));
			}else if(newInstanceFieldType.getSimpleName().equals("int")){
				field.set(instance, Integer.valueOf(parser.getAttributeValue(null, field.getName())));
			}else if(newInstanceFieldType.getSimpleName().equals("boolean")){
				field.set(instance, Boolean.valueOf(parser.getAttributeValue(null, field.getName())));
			}else if(newInstanceFieldType.getSimpleName().equals("long")){
				field.set(instance, Long.valueOf(parser.getAttributeValue(null, field.getName())));
			}else if(newInstanceFieldType.getSimpleName().equals("short")){
				field.set(instance, Short.valueOf(parser.getAttributeValue(null, field.getName())));
			}else if(newInstanceFieldType.getSimpleName().equals("byte")){
				field.set(instance, Byte.valueOf(parser.getAttributeValue(null, field.getName())));
			}else if(newInstanceFieldType.getSimpleName().equals("float")){
				field.set(instance, Float.valueOf(parser.getAttributeValue(null, field.getName())));
			}else if(newInstanceFieldType.getSimpleName().equals("double")){
				field.set(instance, Double.valueOf(parser.getAttributeValue(null, field.getName())));
			}
		}
	}
	
	/**
	 * 给标签中属性对应的基本类型赋值
	 * @param clazz
	 * @param obj
	 * @param parser
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static void setTagFieldsValue(Class<?> clazz, Object obj, XmlPullParser parser) throws IllegalArgumentException, IllegalAccessException{
		Field[] fields = clazz.getDeclaredFields();
		// 如果标签名与栈顶元素的类名相同则为其属性赋值
		if(parser.getName().equals(clazz.getSimpleName())){ 
			for (Field field : fields) {
				setValue(field, obj, parser);
			}
		}
	}
}

class TagBundle {
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TagBundle [clazz=" + clazz + ", instance=" + instance
				+ ", isList=" + isList + "]";
	}
	/**
	 * @return the className
	 */
	public Class<?> getClazz() {
		return clazz;
	}
	/**
	 * @return the instance
	 */
	public Object getInstance() {
		return instance;
	}
	/**
	 * @return the isList
	 */
	public boolean isList() {
		return isList;
	}
	/**
	 * @param clazz the clazz to set
	 */
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	/**
	 * @param instance the instance to set
	 */
	public void setInstance(Object instance) {
		this.instance = instance;
	}
	/**
	 * @param isList the isList to set
	 */
	public void setList(boolean isList) {
		this.isList = isList;
	}
	private Class<?> clazz;
	private Object instance;
	private boolean isList;
	public TagBundle(Class<?> clazz, Object instance, boolean isList) {
		super();
		this.clazz = clazz;
		this.instance = instance;
		this.isList = isList;
	}
	public TagBundle() {
		super();
	}
}
