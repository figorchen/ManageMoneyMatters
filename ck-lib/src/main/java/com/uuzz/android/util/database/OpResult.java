package com.uuzz.android.util.database;

import com.uuzz.android.util.log.UUZZLog;

import java.util.HashMap;

/**
 * 此类用来描述不能用boolean类型表达的结果
 * @author Chunk E-mail:348353610@qq.com
 * @version Created time：2015年8月14日 下午7:35:57
 *
 * @param <T> 成功后最终返回的data类型
 */
public class OpResult<T> extends HashMap<String, Object> {
	UUZZLog logger = new UUZZLog("OpResult");
	
	private static final long serialVersionUID = 1851990283284604418L;
	
	private static final int SUCCESS = 200;
	private static final int FAIL = 400;
	private static final int SEMI_SUCCESS = 300;
	
	
	/**
	 * return a OpResult witch is success
	 * @return
	 */
	public void success(){
		this.put("status", OpResult.SUCCESS);
	}
	
	/**
	 * return a OpResult witch is success and include data
	 * @return
	 */
	public void success(T data){
		this.put("status", OpResult.SUCCESS);
		this.put("data", data);
	}
	
	/**
	 * return a OpResult witch is fail and put error message into
	 * @return
	 */
	public void fail(String errMsg){
		this.put("status", OpResult.FAIL);
		this.put("errMsg", errMsg);
	}
	
	public void setSuccess(){
		this.put("status", OpResult.SUCCESS);
	}
	public void setFail(String errMsg){
		this.put("status", OpResult.FAIL);
		this.put("errMsg", errMsg);
	}
	public void setSemiSuccess(){
		this.put("status", OpResult.SEMI_SUCCESS);
	}
	public void setMessage(String msg){
		this.put("message", msg);
	}
	public String getErrMsg(){
		String str = null;
		str = (String) this.get("errMsg");
		if(str == null){
			str = "get OpResult errmsg fail";
			
		}
		return str;
	}
	public String getMessage(){
		String str = null;
		str = (String) this.get("message");
		if(str == null) {
			str =  "get OpResult message fail";
		}
		return str;
	}
	/**
	 * If is success return true
	 * @return
	 */
	public boolean isSuccess(){
		try {
			int status = (Integer) this.get("status");
			if(status == OpResult.SUCCESS){
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			logger.w("get OpResult status fail: "+e);
			return false;
		}
	}
	
	/**
	 * If is fail return true
	 * @return
	 */
	public boolean isFailed(){
		try {
			int status = (Integer) this.get("status");
			if(status == OpResult.FAIL){
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			logger.w("get OpResult status fail: "+e);
			return false;
		}
	}
	
	/**
	 * If is semi success/fail return true/false
	 * @return
	 */
	public boolean isSemiSuccess(){
		try {
			int status = (Integer) this.get("status");
			if(status == OpResult.SEMI_SUCCESS){
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			logger.w("get OpResult status fail: "+e);
			return false;
		}
	}
	
	/**
	 * return data if there are data
	 * @return
	 */
	public T getData(){
		try {
			@SuppressWarnings("unchecked")
			T obj = (T) this.get("data");
			return obj;
		} catch (Exception e) {
			logger.w("get OpResult data fail: "+e);
			return null;
		}
	}
	
	/**
	 * Put data into hash map
	 * @return
	 */
	public void setData(T data){
		this.put("data", data);
	}
}
