package com.uuzz.android.util.net.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import com.uuzz.android.util.Common;

public class SingleHttpClient extends DefaultHttpClient {
	private static HttpClient mHttpClient;
	private static Object lock = new Object();
	
	private SingleHttpClient(){
		super();
	}

	/**
	 * 描 述：HttpClient的简单封装，返回普通的对象或者单例对象<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/1/3 注释 <br/>
	 * @param isSignle 是否采用单例模式
	 * @return HttpClient对象
     */
	public static HttpClient getInstance(boolean isSignle){
		if(!isSignle){
			HttpClient lHttpClient = new SingleHttpClient();
			lHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Common.SO_TIMEOUT);
			lHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Common.CONNECTION_TIMEOUT);
			return lHttpClient;
		} else {
			synchronized (lock) {
				if(mHttpClient == null){
					mHttpClient = new SingleHttpClient();
				}
				mHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, Common.SO_TIMEOUT);
				mHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, Common.CONNECTION_TIMEOUT);
				return mHttpClient;
			}
		}
	}
}
