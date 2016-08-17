package com.uuzz.android.util.net.response;

import org.apache.http.Header;

import java.io.Serializable;
/**
 * 包括http请求后返回的具体内容
 * @author Administrator
 *
 * @param <T> 最终返回的实体内容类型
 */
public class ResponseContent<T> implements Serializable {
	private static final long serialVersionUID = 4430018808969776131L;
	private int mResultCode;
	private Header[] heads;
	private T entity;

	public int getmResultCode() {
		return mResultCode;
	}
	public void setmResultCode(int mResultCode) {
		this.mResultCode = mResultCode;
	}
	public Header[] getHeads() {
		return heads;
	}
	public void setHeads(Header[] heads) {
		this.heads = heads;
	}
	public T getEntity() {
		return entity;
	}
	public void setEntity(T entity) {
		this.entity = entity;
	}
	
}
