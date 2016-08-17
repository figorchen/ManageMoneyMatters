package com.uuzz.android.util.database.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.uuzz.android.util.database.BeanUtils;
import com.uuzz.android.util.database.OpResult;
import com.uuzz.android.util.database.ParseTableXML;
import com.uuzz.android.util.log.UUZZLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * It is a abstract class, user must write getBeanClass and getTableName when extend it.
 * @author chenke
 * @param <T> 返回数据的类型
 */
public abstract class AbstractDAO<T> extends Observable {
	/** 数据库改变的标记，用于传递给监听者 */
	public static final int DB_CHANGED = 200;


	/** 线程池 */
	protected ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	/** 日志 */
	protected UUZZLog logger = new UUZZLog(getTag());
	/** 数据库 */
	protected SQLiteDatabase db;

	/**
	 * 描 述：获取Tag，用于日志tag<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/1/25 注释 <br/>
	 */
	protected abstract String getTag();

	/**
	 * @return the db
	 */
	public SQLiteDatabase getDb() {
		return db;
	}

	/**
	 * @param db the db to set
	 */
	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

	public AbstractDAO(SQLiteDatabase db) {
		super();
		this.db = db;
	}

	/**
	 * get bean class
	 * @return bean class
	 */
	public abstract Class<T> getBeanClass();
	
	/**
	 * get table name
	 * @return table
	 */
	public abstract String getTableName();
	
	/**
	 * Check the object.class is equal to DAO.getBeanClass or not
	 * If they a equal return null,else return a OpResult<Object> that is fail
	 * @param bean 需要检查的目标对象
	 * @return OpResult，如果检查失败则返回fail类型
	 */
	private OpResult<Object> checkObject(T bean){
		OpResult<Object> result = new OpResult<>();
		if(!bean.getClass().getName().equals(this.getBeanClass().getName())){
			result.fail("faild to insert because this object is not a "+this.getBeanClass().getSimpleName()+
					" instance");
			result.setData(bean);
			return result;
		}
		return result;
	}
	
	/**
	 * 通过一个List插入多个Bean到表中。
	 * 如果不分插入成功则返回一个标记为semiSuccess的OpResult<Object>类，具体异常信息可通过message获取，如果完全插入失败则返回失败状态的OpResult<Object>
	 * @param isConatinKey 如果有主键且为自增类型是否包含进参数列表
	 * @return OpResult<Object> may be semi success
	 */
	public OpResult<Object> insert(List<T> beans, boolean isConatinKey){
		OpResult<Object> opResult = new OpResult<>();
		for (T bean : beans) {
			OpResult<Object> or = insert(bean, isConatinKey);
			if(!or.isSuccess()){
				try {
					if(opResult.isSuccess()){
						// There are some insert have done success in collection, set status to semi success
						opResult.setSemiSuccess();
						// Take message add into end of message
						opResult.setMessage((String)or.get("errMsg"));
					}else if(opResult.isSemiSuccess()){
						// Add message
						String message = (String)opResult.get("message");
						opResult.setMessage(message + "," + or.get("errMsg"));
					}else{
						// Add message
						String errMsg = (String)opResult.get("errMsg");
						opResult.setMessage(errMsg + "," + or.get("errMsg"));
					}
				} catch (Exception e) {
					logger.i("insert data to " + getTableName() + " failed!", e);
					opResult.setMessage((String)or.get("errMsg"));
				}
			}else {
				opResult.setSuccess();
			}
		}
		return opResult;
	}
	
	/**
	 * 通过Bean插入一条数据。
	 * 如果插入成功将返回一个在data中包含新插入行数的OpResult<Object>类，如果插入失败将返回一个在errMsg中包含插入异常的OpResult<Object>类,并且在data中返回插入失败的Bean对象
	 * @param bean 表对应的Bean
	 * @param isConatinKey 如果有主键且为自增类型是否包含进参数列表
	 * @return OpResult
	 */
	public OpResult<Object> insert(T bean, boolean isConatinKey){
		OpResult<Object> result = checkObject(bean);
		if(result.isFailed()){
			return result;
		}
		
		// Transform object to ContentValues
		ContentValues contentValues = BeanUtils.transformBeanToContentValues(this.getTableName(), getBeanClass(), bean, isConatinKey);
		
		try {
			long row = db.insert(this.getTableName(), null, contentValues);
			if(row == -1){
				result.fail("Faild to insert "+this.getBeanClass().getName()+
						" into "+this.getTableName());
				result.setData(bean);
				return result;
			}
			result.success(row);
		} catch (Exception e) {
			logger.i("Faild to insert " + this.getBeanClass().getName() +
					" into " + this.getTableName() + "Exception:" + e);
			result.fail("Faild to insert "+this.getBeanClass().getName()+
					" into "+this.getTableName());
			result.setData(bean);
		}
		return result;
	}
	
	/**
	 * 按条件删除记录
	 * @param whereClause 条件（可以是带 ? 参数的那种，跟后面的 whereArgs对应上就行）
	 * @param whereArgs 参数
	 * @return OpResult
	 */
	public OpResult<Object> delete(String whereClause, String[] whereArgs){
		OpResult<Object> result = new OpResult<>();
		try {
			int count = db.delete(getTableName(), whereClause, whereArgs);
			result.success();
			result.setData(count);
		} catch (Exception e) {
			logger.i("Faild to delete from " + this.getTableName() + "Exception:" + e);
			result.fail("Faild to delete from "+this.getTableName());
		}
		return result;
	}
	
	/**
	 * 删除符合Map描述的记录
	 * 如果插入成功将返回一个在data中包含受影响行数的OpResult<Object>类，如果插入失败将返回一个在errMsg中包含插入异常的OpResult<Object>类,并且在data中返回删除失败的Map对象
	 * @param map 删除条件
	 * @return OpResult
	 */
	public OpResult<Object> deleteDataByMap(HashMap<String, Object> map){
		OpResult<Object> result;
		
		// Transform map to whereClause and whereArgs
		String whereClause = "";
		List<String> whereArgs = new ArrayList<>();
		for (String key : map.keySet()) {
			try {
				whereClause += ParseTableXML.getTableFieldName(this.getTableName(), key)+"=? and ";
				whereArgs.add(String.valueOf(map.get(key)));
			} catch (Exception e) {
				logger.i("Arrange where clause \"" + key + "\" failed!", e);
			}
		}
		whereClause = whereClause.substring(0, whereClause.length()-4);
		logger.d("whereClause:" + whereClause);
		logger.d("whereArgs:" + whereArgs);
		
		result = delete(whereClause, whereArgs.toArray(new String[whereArgs.size()]));
		return result;
	}
	
	/**
	 * 利用Map更新数据
	 * @param map 要更新的字段-值集合
	 * @param whereClause 条件（可以是带 ? 参数的那种，跟后面的 whereArgs对应上就行）
	 * @param whereArgs 参数
	 * @return OpResult
	 */
	public OpResult<Object> update(HashMap<String, Object> map, String whereClause, String[] whereArgs){
		OpResult<Object> result = new OpResult<>();
		
		ContentValues values = new ContentValues();
		for (String key : map.keySet()) {
			try {
				values.put(ParseTableXML.getTableFieldName(this.getTableName(), key), String.valueOf(map.get(key)));
			} catch (Exception e) {
				logger.i("Arrange values \"" + key + "\" failed", e);
			}
		}
		
		try {
			int count = db.update(getTableName(), values, whereClause, whereArgs);
			result.success(count);
		} catch (Exception e) {
			logger.i("Faild to update to " + this.getTableName() + "Exception:" + e);
			result.fail("Faild to delete to "+this.getTableName());
			result.setData(map);
		}
		return result;
	}
	
	/**
	 * 通过map更新数据
	 * @param map 要更新的字段-值集合
	 * @param term 更新满足的whereClause-whereArgs集合
	 * @return OpResult
	 */
	public OpResult<Object> update(HashMap<String, Object> map, HashMap<String, Object> term){
		OpResult<Object> result;
		
		// Transform map to whereClause and whereArgs
		String whereClause = "";
		List<String> whereArgs = new ArrayList<>();
		for (String key : term.keySet()) {
			try {
				whereClause += ParseTableXML.getTableFieldName(this.getTableName(), key)+"=? and ";
			} catch (Exception e) {
				logger.i("Arrange values \"" + key + "\" failed", e);
			}
			whereArgs.add(String.valueOf(term.get(key)));
		}
		whereClause = whereClause.substring(0, whereClause.length()-4);
		result = update(map, whereClause, whereArgs.toArray(new String[whereArgs.size()]));
		return result;
	}
	
	/**
	 * 通过Bean更新数据
	 * @param bean 要更新对应的Bean
	 * @param whereClause 条件（可以是带 ? 参数的那种，跟后面的 whereArgs对应上就行）
	 * @param whereArgs 参数
	 * @param isConatinKey 如果有主键且为自增类型是否包含进参数列表
	 * @return 更新成功返回影响条数，失败返回更新对应bean
	 */
	public OpResult<Object> update(T bean, String whereClause, String[] whereArgs, boolean isConatinKey){
		OpResult<Object> result = checkObject(bean);
		if(result.isFailed()){
			return result;
		}
		
		ContentValues values = BeanUtils.transformBeanToContentValues(this.getTableName(), getBeanClass(), bean, isConatinKey);
		try {
			int count = db.update(getTableName(), values, whereClause, whereArgs);
			logger.d("update "+bean.toString()+" -----------"+count);
			result.success(count);
		} catch (Exception e) {
			logger.i("Faild to update to " + this.getTableName() + "Exception:" + e);
			result.fail("Faild to delete to "+this.getTableName());
			result.setData(bean);
		}
		return result;
	}
	
	/**
	 * 根据bean和map更新数据
	 * @param bean 要更新对应的Bean
	 * @param map 要更新的字段-值集合
	 * @param isConatinKey 如果有主键且为自增类型是否包含进参数列表
	 * @return OpResult
	 */
	public OpResult<Object> update(T bean, HashMap<String, Object> map, boolean isConatinKey){
		OpResult<Object> result = checkObject(bean);
		if(result.isFailed()){
			return result;
		}
		
		ContentValues values = BeanUtils.transformBeanToContentValues(this.getTableName(), getBeanClass(), bean, isConatinKey);
		// Transform map to whereClause and whereArgs
		String whereClause = "";
		List<String> whereArgs = new ArrayList<>();
		for (String key : map.keySet()) {
			whereClause += key+"=? and ";
			if(map.get(key) != null){
				whereArgs.add(String.valueOf(map.get(key)));
			}
		}
		whereClause = whereClause.substring(0, whereClause.length()-4);
		
		try {
			int count = db.update(getTableName(), values, whereClause, whereArgs.toArray(new String[whereArgs.size()]));
			result.success(count);
		} catch (Exception e) {
			logger.i("Faild to update to " + this.getTableName() + "Exception:" + e);
			result.fail("Faild to delete to "+this.getTableName());
			result.setData(bean);
		}
		return result;
	}
	
	/**
	 * 根据条件返回表数据
	 * @param columns 要返回的字段名，为null表示返回全部
	 * @param selection 筛选条件列表（可以是带 ? 参数的那种，跟后面的 selectionArgs对应上就行），数据库字段名，为null表示无条件
	 * @param selectionArgs 条件参数
	 * @param groupBy 排序方式
	 * @param having 函数结果条件子句
	 * @param orderBy 排序方式
	 * @param limit 返回最大条数
	 * @return List结果集
	 */
	public List<T> select(String[] columns, String selection,
						  String[] selectionArgs, String groupBy,
						  String having, String orderBy, String limit){
		Cursor cursor = db.query(getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		List<T> result = new ArrayList<>();
		while(cursor.moveToNext()){
			result.add(BeanUtils.getBean(getBeanClass(), cursor, getTableName()));
		}
		cursor.close();
		return result;
	}
	
	public List<T> selectAll(String[] columns){
		return select(columns, null, null, null, null, null, null);
	}
	
	public Cursor selectForCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit){
		return db.query(getTableName(), columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}
}
