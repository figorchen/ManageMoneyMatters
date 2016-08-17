package com.uuzz.android.util;

import android.annotation.SuppressLint;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("DefaultLocale") public class ReflectUtils {
	/**
	 * return field value
	 * @param o the object that include field
	 * @param field
	 * @return
	 */
	public static Object getValueByField(Object o, Field field){
		Object result = null;
		try {  
			field.setAccessible(true);
			result =  field.get(o);
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IllegalArgumentException e) {  
	        e.printStackTrace();  
	    } catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return result;
	}
	
//	/**
//	 * 将一个Bean转换成ContentValues
//	 * @param <T> Bean类型
//	 * @param tableName
//	 * @param clazz Bean的class
//	 * @param obj Bean对象
//	 * @return
//	 */
//	public static <T> ContentValues transformBeanToContentValues(String tableName, Class<T> clazz, T obj, boolean isContainKey){
//
//		ContentValues maps = new ContentValues();
//		Field[] fields = clazz.getDeclaredFields();
//		String fieldName;
//		String keyFieldName = ParseTableXML.getKeyFieldName(tableName);
//		for (Field field : fields) {
//			try {
//				if(!field.getName().equalsIgnoreCase("CREATOR")){
//					if(isContainKey){
//						fieldName = ParseTableXML.getTableFieldName(tableName, field.getName());
//						maps.put(fieldName, String.valueOf( ReflectUtils.getValueByField(obj, field)));
//					}else{
//						if(!field.getName().equalsIgnoreCase(keyFieldName)){
//							fieldName = ParseTableXML.getTableFieldName(tableName, field.getName());
//							maps.put(fieldName, String.valueOf( ReflectUtils.getValueByField(obj, field)));
//						}
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		fieldName = null;
//		return maps;
//	}
	
	/**
	 * 将一个Bean转换成HashMap
	 * @param <T>
	 * @param clazz table Bean class
	 * @param obj
	 * @return
	 */
	public static <T> HashMap<String, Object> transformBeanToHashMap(Class<T> clazz, Object obj){
		
		HashMap<String, Object> maps = new HashMap<String, Object>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			maps.put(field.getName(), String.valueOf( ReflectUtils.getValueByField(obj, field)));
		}
		
		return maps;
	}
	
//	/**
//	 * 通过游标获取Bean
//	 * @param clazz Bean.class
//	 * @param cursor 游标
//	 * @return Bean实例
//	 */
//	public static <T> T getBean(Class<T> clazz, Cursor cursor, String tableName){
//		Field[] fields = clazz.getDeclaredFields();
//		T bean = null;
//		try {
//			bean = (T) clazz.newInstance();
//			String beanFieldName;
//			String fieldName;
//			for (Field field : fields) {
//				field.setAccessible(true);
//				//属性类型
//				String typeName = field.getType().getSimpleName();
//				beanFieldName = field.getName().toLowerCase();
//				try{
//					//根据orm得到表中属性名
//					fieldName = ParseTableXML.getTableFieldName(tableName, beanFieldName);
//					if(typeName.equals("String")){
//						field.set(bean, cursor.getString(cursor.getColumnIndex(fieldName)));
//					}else if(typeName.equals("int")){
//						field.set(bean, cursor.getInt(cursor.getColumnIndex(fieldName)));
//					}else if(typeName.equals("float")){
//						field.set(bean, cursor.getFloat(cursor.getColumnIndex(fieldName)));
//					}else if(typeName.equals("double")){
//						field.set(bean, cursor.getDouble(cursor.getColumnIndex(fieldName)));
//					}else if(typeName.equals("boolean")){
//						field.set(bean, Boolean.valueOf(cursor.getString(cursor.getColumnIndex(fieldName))));
//					}else if(typeName.equals("long")){
//						field.set(bean, cursor.getLong(cursor.getColumnIndex(fieldName)));
//					}else if(typeName.equals("short")){
//						field.set(bean, cursor.getShort(cursor.getColumnIndex(fieldName)));
//					}
//				}catch (Exception e){
//					e.printStackTrace();
//				}
//
//			}
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//		}
//		return bean;
//	}
	
	/**
	 * 把Bean转换成List<NameValuePair>
	 * @param clazz Bean对应的Class
	 * @param obj Bean实例
	 * @return
	 */
	public static <T> List<NameValuePair> transformBeanToNameValuePairs(Class<T> clazz, Object obj){
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		Field[] fields = clazz.getDeclaredFields();
		NameValuePair mNameValuePair;
		for (Field field : fields) {
			mNameValuePair = new BasicNameValuePair(field.getName(), String.valueOf( ReflectUtils.getValueByField(obj, field)));
			parameters.add(mNameValuePair);
		}
		return parameters;
	}
}
