package com.uuzz.android.util.database;

import android.content.Context;
import android.text.TextUtils;
import android.util.Xml;

import com.uuzz.android.util.log.UUZZLog;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseTableXML {

	private static UUZZLog loggger = new UUZZLog("ParseTableXML");
	
	public static HashMap<String, Orm> tables;
	
	public static OpResult<String> praseXML(Context context, String path){
		OpResult<String> result = new OpResult<>();
		try {
			String[] files = context.getAssets().list(path);
			for (String file : files) {
				if(file.endsWith(".orm.xml")){
					//找到以 ".orm.xml" 结尾的文件，打开并解析
					InputStream is = context.getAssets().open(path+"/"+file);
					XmlPullParser parser = Xml.newPullParser();
					parser.setInput(is, "UTF-8");
					int type = parser.getEventType();
					Orm orm = null;
					while(type != XmlPullParser.END_DOCUMENT){
						switch (type) {
						case XmlPullParser.START_DOCUMENT:
							orm = new Orm();
							break;
						case XmlPullParser.START_TAG:
							if(orm == null) {
								break;
							}
							if(parser.getName().equals("TableInfo")){
								//拿到TableInfo标签，收集表信息
								if(tables == null){
									tables = new HashMap<>();
								}
								//获取表名
								orm.setTableName(parser.getAttributeValue(null, "name"));
								//获取tpye
								try{
									if(parser.getAttributeValue(null, "type").equalsIgnoreCase("view")){
										orm.setType(Orm.TYPE_VIEW);
									}else{
										orm.setType(Orm.TYPE_TABLE);
									}
								}catch (Exception e){
									loggger.w("Can not find field named 'type' in "
									+orm.getTableName()+",set type to 'TYPE_TABLE'!", e);
								}
							}else if(parser.getName().equals("Key")){
								if(parser.getAttributeValue(null, "name") != null){
									orm.getKey().setName(parser.getAttributeValue(null, "name"));
								}
								if(parser.getAttributeValue(null, "property") != null){
									orm.getKey().setProperty(parser.getAttributeValue(null, "property"));
									orm.getPropertyTableMap().put(parser.getAttributeValue(null, "property"), parser.getAttributeValue(null, "name"));
									orm.getTablePropertyMap().put(parser.getAttributeValue(null, "name"), parser.getAttributeValue(null, "property"));
								}
								if(parser.getAttributeValue(null, "size") != null){
									orm.getKey().setSize(Integer.valueOf(parser.getAttributeValue(null, "size")));
								}
								if(parser.getAttributeValue(null, "identity") != null){
									orm.getKey().setIdentity(Boolean.valueOf(parser.getAttributeValue(null, "identity")));
								}
								if(parser.getAttributeValue(null, "default") != null){
									orm.getKey().setDefaultValue(parser.getAttributeValue(null, "default"));
								}
								if(parser.getAttributeValue(null, "type") != null){
									orm.getKey().setType(parser.getAttributeValue(null, "type"));
								}
								if(parser.getAttributeValue(null, "unique") != null){
									orm.getKey().setUnique(Boolean.valueOf(parser.getAttributeValue(null, "unique")));
								}
								if(parser.getAttributeValue(null, "notnull") != null){
									orm.getKey().setNotNull(Boolean.valueOf(parser.getAttributeValue(null, "notnull")));
								}
							}else if(parser.getName().equals("Item")){
								Item item = new Item();
								if(parser.getAttributeValue(null, "name") != null){
									item.setName(parser.getAttributeValue(null, "name"));
								}
								if(parser.getAttributeValue(null, "property") != null){
									item.setProperty(parser.getAttributeValue(null, "property"));
									orm.getPropertyTableMap().put(parser.getAttributeValue(null, "property"), parser.getAttributeValue(null, "name"));
									orm.getTablePropertyMap().put(parser.getAttributeValue(null, "name"), parser.getAttributeValue(null, "property"));
								}
								if(parser.getAttributeValue(null, "size") != null){
									item.setSize(Integer.valueOf(parser.getAttributeValue(null, "size")));
								}
								if(parser.getAttributeValue(null, "default") != null){
									item.setDefaultValue(parser.getAttributeValue(null, "default"));
								}
								if(parser.getAttributeValue(null, "type") != null){
									item.setType(parser.getAttributeValue(null, "type"));
								}
								if(parser.getAttributeValue(null, "unique") != null){
									item.setUnique(Boolean.valueOf(parser.getAttributeValue(null, "unique")));
								}
								if(parser.getAttributeValue(null, "notnull") != null){
									item.setNotNull(Boolean.valueOf(parser.getAttributeValue(null, "notnull")));
								}
								orm.getItems().add(item);
							}
							break;

						default:
							break;
						}
						type = parser.next();
					}
					if (orm != null) {
						tables.put(orm.getTableName(), orm);
					}
					result.success("");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.fail("");
		}
		return result;
	}
	
	/**
	 * 整理创建表的sql语句
	 * @return
	 */
	public static ArrayList<String> getCreateTableSql(){
		ArrayList<String> sqls = new ArrayList<>();
		
		//遍历拿出tables中的orm对象
		for (String tableName : tables.keySet()) {
			Orm orm = tables.get(tableName);
			//表属性不为table的不创建
			if(orm.getType() != Orm.TYPE_TABLE){
				continue;
			}
			Key key = orm.getKey();
			List<Item> items = orm.getItems();
			//根据orm对象解析出sql语句
			StringBuilder ormSql = new StringBuilder("CREATE TABLE ["+tableName+"] (");
			//主键名字不为null，说明主键配置存在，拼接创建主键字符串
			if(key.getName() != null){
				ormSql.append("[").append(key.getName()).append("] ");
				if(key.getType().equalsIgnoreCase("INTEGER") || key.getType().equalsIgnoreCase("SHORT") ||key.getType().equalsIgnoreCase("LONG")){
					ormSql.append(key.getType()).append(" PRIMARY KEY ");
				}else{
					ormSql.append(key.getType()).append("(").append(key.getSize()).append(") PRIMARY KEY ");
				}
				if(key.isIdentity()){
					ormSql.append("AUTOINCREMENT");
				}
				ormSql.append(", ");
			}
			//遍历每一个item，生成sql
			boolean isChar;
			for (Item item : items) {
				isChar = false;
				ormSql.append("[").append(item.getName()).append("] ");
				if(item.getType().equalsIgnoreCase("INTEGER")
						|| item.getType().equalsIgnoreCase("SHORT")
						|| item.getType().equalsIgnoreCase("LONG")
						|| item.getType().equalsIgnoreCase("DOUBLE")
						|| item.getType().equalsIgnoreCase("FLOAT")){
					ormSql.append(item.getType()).append(" ");
				}else{
					isChar = true;
					ormSql.append(item.getType()).append("(").append(item.getSize()).append(") ");
				}
				if(item.isUnique()){
					ormSql.append("UNIQUE ");
				}
				if(item.isNotNull()){
					ormSql.append("NOT NULL ");
				}
				if(item.getDefaultValue() != null){
					if(isChar){
						ormSql.append("DEFAULT ").append("\"").append( item.getDefaultValue()).append("\"");
					}else if(!item.getDefaultValue().equals("")){
						ormSql.append("DEFAULT ").append(item.getDefaultValue());
					}
				}
				ormSql.append(", ");
			}
			ormSql.delete(ormSql.length()-2, ormSql.length()-1);
			//单条创建表语句添加到整体
			sqls.add(ormSql.append(");").toString());
		}
		loggger.d(sqls.toString());
		return sqls;
	}
	
	/**
	 * 获取一张表的自增主键字段名（与类中字段相同）,如果没有主键或主键不是自增类型则返回""
	 * @param tableName 表名
	 * @return
	 */
	public static String getKeyFieldName(String tableName){
		Orm orm = tables.get(tableName);
		Key key = orm.getKey();
		if(!TextUtils.isEmpty(key.getType()) && key.getType().equals("INTEGER") && key.isIdentity()){
			return key.getProperty();
		}
		return "";
	}
	
	/**
	 * 根据Bean的属性名得到表字段名
	 * @param tableName 表名
	 * @param beanFieldName Bean属性名
	 * @return 得到的表字段名
	 * @throws Exception 如果表名称为找到则抛出异常:"no sush table named "tableName"."
	 * @throws Exception 如果Bean属性名未找到抛出异常:no sush field "beanFieldName" in table named "tableName".
	 */
	public static String getTableFieldName(String tableName, String beanFieldName) throws Exception{
		Orm orm = tables.get(tableName);
		if(orm == null){
			throw new Exception("no such table named "+tableName+".");
		}
		String tableFieldName = orm.getPropertyTableMap().get(beanFieldName);
		if(TextUtils.isEmpty(tableFieldName)) {
			//没有找到此属性名对应的字段名
			throw new Exception("no such field \""+ beanFieldName +"\" in table named "+tableName+".");
		}
		return tableFieldName;
//		try{
//			//先查找主键
//			if(orm.getKey() != null && orm.getKey().getProperty().equalsIgnoreCase(beanFieldName)){
//				return orm.getKey().getName();
//			}
//		}catch (Exception e) {
//			loggger.w("no key in table named:"+tableName);
//		}
//		//查找其他属性
//		List<Item> items = orm.getItems();
//		for (Item item : items) {
//			if(item.getProperty().equalsIgnoreCase(beanFieldName)){
//				return item.getName();
//			}
//		}
//
	}
}
