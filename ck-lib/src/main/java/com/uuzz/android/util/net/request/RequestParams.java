package com.uuzz.android.util.net.request;


import android.text.TextUtils;

import com.uuzz.android.util.Common;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;


/**
 * 包含http请求所需要的所有参数
 * @author Administrator
 *
 * @param <E> http请求中的请求参数类型
 */
public class RequestParams<E> {

	public final static String UTF_8 = "UTF-8";
	public final static String GBK = "GBK";
	public final static String GB2312 = "GB2312";

	private String url;
	private E params;
	private Header[] headers;
	private String charset;
	private int timeout = 5000;
	private boolean isGet;
	private String path;
	private boolean isSignle;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	private RequestParams() {
		super();
	}

	public boolean isSignle() {
		return isSignle;
	}

	public void setSignle(boolean signle) {
		isSignle = signle;
	}

	/**
	 * 创建请求参数
	 * @param url 网络url，随意带不带"httt://"
	 * @param params 请求参数，可以是String、Bean、Map等类型
	 * @param headers 请求头，默认包含charset
	 * @param charset 编码，默认utf-8
	 * @param timeout 超时时长，填写小于1的值会转为60秒
	 * @param isGet 是否为get请求
	 * @param path 文件路径，若启用下载此路径不能为空，下载完成后自动保存在此路径
	 */
	public RequestParams(String url, E params, Header[] headers,
			String charset, int timeout, boolean isGet, String path, boolean isSignle) {
		super();
		if(TextUtils.isEmpty(charset)){
			charset = UTF_8;
		}
		if(!url.startsWith("http://")){
			url = "http://" + url;
		}
		if(timeout < 1){
			timeout = Common.HTTP_TIMEOUT;
		}
		if(!TextUtils.isEmpty(path)){
			this.path = path;
		}
		if(headers == null || headers.length == 0){
			this.headers = new Header[]{
					new Header() {
						@Override
						public String getName() {
							return "Charset";
						}

						@Override
						public String getValue() {
							return UTF_8;
						}

						@Override
						public HeaderElement[] getElements() throws ParseException {
							return new HeaderElement[0];
						}
					}
			};
		}else {
			this.headers = headers;
		}
		this.url = url;
		this.params = params;
		this.charset = charset;
		this.timeout = timeout;
		this.isGet = isGet;
		this.isSignle = isSignle;

	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		if(!url.startsWith("http://")){
			url = "http://" + url;
		}
		this.url = url;
	}
	public E getParams() {
		return params;
	}
	public void setParams(E params) {
		this.params = params;
	}
	public Header[] getHeaders() {
		return headers;
	}
	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public boolean isGet() {
		return isGet;
	}
	public void setGet(boolean isGet) {
		this.isGet = isGet;
	}
}
