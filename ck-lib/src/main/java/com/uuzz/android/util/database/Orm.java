package com.uuzz.android.util.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Orm {
	public static final int TYPE_TABLE = 100;
	public static final int TYPE_VIEW = 200;
	
	private Key key = new Key();
	private List<Item> items = new ArrayList<>();
	/** 存放（表字段，Bean字段）的map */
	private HashMap<String, String> tablePropertyMap = new HashMap<>();
	/** 存放（Bean字段，表字段）的map */
	private HashMap<String, String> propertyTableMap = new HashMap<>();
	private String tableName;
	private int type;
	
	public Orm() {
		super();
	}

	public HashMap<String, String> getTablePropertyMap() {
		return tablePropertyMap;
	}

	public void setTablePropertyMap(HashMap<String, String> tablePropertyMap) {
		this.tablePropertyMap = tablePropertyMap;
	}

	public HashMap<String, String> getPropertyTableMap() {
		return propertyTableMap;
	}

	public void setPropertyTableMap(HashMap<String, String> propertyTableMap) {
		this.propertyTableMap = propertyTableMap;
	}

	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}
	/**
	 * @return the items
	 */
	public List<Item> getItems() {
		return items;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}
	/**
	 * @param items the items to set
	 */
	public void setItems(List<Item> items) {
		this.items = items;
	}
	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
}
