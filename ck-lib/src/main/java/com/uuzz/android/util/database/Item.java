package com.uuzz.android.util.database;

public class Item {
	//表字段名
	private String name;
	//Bean属性名
	private String property;
	//唯一标示
	private boolean unique;
	//非空标示
	private boolean notNull;
	//默认值
	private String defaultValue;
	//表字段长度
	private int size;
	//表字段数据类型
	private String type;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}
	/**
	 * @return the unique
	 */
	public boolean isUnique() {
		return unique;
	}
	/**
	 * @return the notNull
	 */
	public boolean isNotNull() {
		return notNull;
	}
	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}
	/**
	 * @param unique the unique to set
	 */
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	/**
	 * @param notNull the notNull to set
	 */
	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}
	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	public Item() {
		super();
	}
	public Item(String name, String property, boolean unique, boolean notNull,
			String defaultValue, int size, String type) {
		super();
		this.name = name;
		this.property = property;
		this.unique = unique;
		this.notNull = notNull;
		this.defaultValue = defaultValue;
		this.size = size;
		this.type = type;
	}
}
