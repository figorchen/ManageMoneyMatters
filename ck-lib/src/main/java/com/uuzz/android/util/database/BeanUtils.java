package com.uuzz.android.util.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.uuzz.android.util.database.annotation.TableProperty;
import com.uuzz.android.util.database.annotation.TablePropertyExtra;
import com.uuzz.android.util.log.UUZZLog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BeanUtils {
	private static UUZZLog logger = new UUZZLog("BeanUtils");

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
	    } catch (Exception e) {
			logger.d("Return field value failed, field name is:" + field.getName(), e);
		}
		return result;
	}
	
	/**
	 * 将一个Bean转换成ContentValues
	 * @param <T> Bean类型
	 * @param tableName
	 * @param clazz Bean的class
	 * @param obj Bean对象
	 * @return
	 */
	public static <T> ContentValues transformBeanToContentValues(String tableName, Class<T> clazz, T obj, boolean isContainKey){
		
		ContentValues maps = new ContentValues();
		Field[] fields = clazz.getDeclaredFields();
		putFieldValue(tableName, obj, isContainKey, maps, fields);
		fields = clazz.getFields();
		putFieldValue(tableName, obj, isContainKey, maps, fields);
//		for (Field field : fields) {
//			try {
//				if(!field.getName().equalsIgnoreCase("CREATOR")){
//					if(isContainKey){
//						fieldName = ParseTableXML.getTableFieldName(tableName, field.getName());
//						maps.put(fieldName, String.valueOf( BeanUtils.getValueByField(obj, field)));
//					}else{
//						if(!field.getName().equalsIgnoreCase(keyFieldName)){
//							fieldName = ParseTableXML.getTableFieldName(tableName, field.getName());
//							maps.put(fieldName, String.valueOf( BeanUtils.getValueByField(obj, field)));
//						}
//					}
//				}
//			} catch (Exception e) {
//				logger.d("Transform bean to ContentValues failed", e);
//				e.printStackTrace();
//			}
//		}
		return maps;
	}

	/**
	 * 描 述：把field对应的值填进ContentValues<br/>
	 * 作 者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/3/16 注释 <br/>
	 */
	private static void putFieldValue(String tableName, Object obj, boolean isContainKey, ContentValues maps, Field[] fields) {
		String fieldName;
		String keyFieldName = ParseTableXML.getKeyFieldName(tableName);
		for (Field field : fields) {
			try {
				TableProperty annotation = field.getAnnotation(TableProperty.class);
				if(annotation == null) {   //如果没有TableProperty注解则直接跳过
					continue;
				}
				if(isContainKey){
					fieldName = ParseTableXML.getTableFieldName(tableName, field.getName());
					maps.put(fieldName, String.valueOf( BeanUtils.getValueByField(obj, field)));
				}else{
					if(!field.getName().equalsIgnoreCase(keyFieldName)){
						fieldName = ParseTableXML.getTableFieldName(tableName, field.getName());
						maps.put(fieldName, String.valueOf( BeanUtils.getValueByField(obj, field)));
					}
				}
			} catch (Exception e) {
				logger.d("Transform bean to ContentValues failed", e);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将一个Bean转换成HashMap
	 * @param clazz table Bean class
	 * @param obj 数据源对象
	 * @return
	 */
	public static HashMap<String, Object> transformBeanToHashMap(Class clazz, Object obj){
		
		HashMap<String, Object> maps = new HashMap<String, Object>();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			maps.put(field.getName(), String.valueOf( BeanUtils.getValueByField(obj, field)));
		}
		
		return maps;
	}
	
	/**
	 * 通过游标获取Bean
	 * @param clazz Bean.class
	 * @param cursor 游标
	 * @return Bean实例
	 */
	public static <T> T getBean(Class<T> clazz, Cursor cursor, String tableName){
		T bean = null;
		try {
			bean = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			putFieldValueFromCursor(clazz, cursor, tableName, fields, bean);
			fields = clazz.getFields();
			putFieldValueFromCursor(clazz, cursor, tableName, fields, bean);
		} catch (Exception e) {
			logger.d("new instance failed. class is " + clazz.getName());
		}
		return bean;
	}

	private static <T> void putFieldValueFromCursor(Class<T> clazz, Cursor cursor, String tableName, Field[] fields, T bean) {
		try {
			String beanFieldName;
			String fieldName;
			for (Field field : fields) {
				TableProperty annotation = field.getAnnotation(TableProperty.class);
				if(annotation == null) {   //如果没有TableProperty注解则直接跳过
					continue;
				}

				field.setAccessible(true);
				//属性类型
				String typeName = field.getType().getSimpleName();
				beanFieldName = field.getName();
				try{
					TablePropertyExtra fieldAnnotation = field.getAnnotation(TablePropertyExtra.class);
					if(fieldAnnotation == null) {   //如果没有TablePropertyExtra则使用表中定义的字段名
						//根据orm得到表中属性名
						fieldName = ParseTableXML.getTableFieldName(tableName, beanFieldName);
					} else{
						fieldName = fieldAnnotation.value();
					}
					if(typeName.equals("String")){
						field.set(bean, cursor.getString(cursor.getColumnIndex(fieldName)));
					}else if(typeName.equals("int")){
						field.set(bean, cursor.getInt(cursor.getColumnIndex(fieldName)));
					}else if(typeName.equals("float")){
						field.set(bean, cursor.getFloat(cursor.getColumnIndex(fieldName)));
					}else if(typeName.equals("double")){
						field.set(bean, cursor.getDouble(cursor.getColumnIndex(fieldName)));
					}else if(typeName.equals("boolean")){
						field.set(bean, TextUtils.equals(cursor.getString(cursor.getColumnIndex(fieldName)), "1"));
					}else if(typeName.equals("long")){
						field.set(bean, cursor.getLong(cursor.getColumnIndex(fieldName)));
					}else if(typeName.equals("short")){
						field.set(bean, cursor.getShort(cursor.getColumnIndex(fieldName)));
					}
				}catch (Exception e){
					logger.d("Set value to field:"+beanFieldName+" failed", e);
				}
			}
		} catch (Exception e) {
			logger.d("Construct new Object "+clazz.getName()+" failed!", e);
		}
	}
	
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
			mNameValuePair = new BasicNameValuePair(field.getName(), String.valueOf( BeanUtils.getValueByField(obj, field)));
			parameters.add(mNameValuePair);
		}
		return parameters;
	}
}
