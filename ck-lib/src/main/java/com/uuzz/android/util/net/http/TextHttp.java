package com.uuzz.android.util.net.http;

import com.uuzz.android.util.ReflectUtils;
import com.uuzz.android.util.log.UUZZLog;
import com.uuzz.android.util.net.request.BaseRequestBean;
import com.uuzz.android.util.net.response.ResponseContent;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Http文本请求类，POST方法可支持文件上传操作
 * @author Administrator
 *
 */
public class TextHttp<E> extends BaseHttp<E, String> {

	private UUZZLog logger = new UUZZLog(this.getClass());

	public static Map<String, String> fileHeadInfo = new HashMap<String, String>();
	static{
		fileHeadInfo.put("jpg", "image/jpeg");
		fileHeadInfo.put("jpeg", "image/jpeg");
		fileHeadInfo.put("png", "image/png");
		fileHeadInfo.put("gif", "image/gif");
		fileHeadInfo.put("bmp", "application/x-bmp");
	}
	
	protected HttpClient mHttpClient;

	@Override
	protected ResponseContent<String> doPost(String url, Object params, Header[] headers, String charset, int timeout, boolean isSingle) {
		if(mHttpClient == null){
			mHttpClient = SingleHttpClient.getInstance(isSingle);
		}
		HttpEntity stringEntity = null;
		MultipartEntity multipartEntity = null;
		Class<?> clazz = null;
		if(params != null){
			clazz = params.getClass();
			try {
				if(BaseRequestBean.class.isAssignableFrom(clazz)){
					multipartEntity = new MultipartEntity();
					// params是一个Bean对象
//					List<NameValuePair> parameters = ReflectUtils.transformBeanToNameValuePairs(params.getClass(), params);
//					mEntity = new UrlEncodedFormEntity(parameters, charset);
					Field[] fields = clazz.getDeclaredFields();
					File file = null;
					for (Field field : fields) {
						field.setAccessible(true);
						if(field.getType().getSimpleName().equals("File")){
							file = (File) field.get(params);
							multipartEntity.addPart(field.getName(),
									new FileBody(file, fileHeadInfo.get(file.getName().substring(file.getName().lastIndexOf(".")+1))));
						}else{
							multipartEntity.addPart(field.getName(), new StringBody(String.valueOf(field.get(params))));
						}
					}
				} else if(Map.class.isAssignableFrom(clazz)) {
					multipartEntity = new MultipartEntity();
					// params是一个Map对象
					File file = null;
					for (Entry<String, Object> param : ((HashMap<String, Object>) params).entrySet()) {
//						NameValuePair mNameValuePair = new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue()));
//						parameters.add(mNameValuePair);
						Class<?> valueClass = param.getValue().getClass();
						if(valueClass.getSimpleName().equals("File")){
							file = (File) param.getValue();
							multipartEntity.addPart(param.getKey(), 
									new FileBody(file, fileHeadInfo.get(file.getName().substring(file.getName().lastIndexOf(".")+1))));
						}else{
							multipartEntity.addPart(param.getKey(), new StringBody(String.valueOf(param.getValue())));
						}
					}
//					mEntity = new UrlEncodedFormEntity(parameters, charset);
				} else if(String.class.isAssignableFrom(clazz)) {
					// params是一个String对象
					stringEntity = new StringEntity((String) params, charset);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		try {
			logger.i("http request,url:"+url);
			HttpPost mHttpPost = new HttpPost(url);
			//添加实体到post请求中
			if(clazz != null){
				if(String.class.isAssignableFrom(clazz)){
					mHttpPost.setEntity(stringEntity);
				}else{
					mHttpPost.setEntity(multipartEntity);
				}
			}
			if(headers != null && headers.length > 0){
				mHttpPost.setHeaders(headers);
			}
			mHttpPost.addHeader("content-type", "application/x-www-form-urlencoded");
			mHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
			HttpResponse response;
			try {
				response = mHttpClient.execute(mHttpPost); //请求并返回结果
			} catch (Exception e) {
				logger.w("http time out,url:"+url, e);
				return null;
			}
			int statuCode = response.getStatusLine().getStatusCode();
			logger.i("statu code : " + statuCode);
			Header[] allHeaders = response.getAllHeaders();
			for (Header header : allHeaders) {
				logger.i(header.getName() + " : " + header.getValue());
			}
			HttpEntity entity = response.getEntity(); //拿到返回的实体
			String content = EntityUtils.toString(entity, charset); //把实体按照编码转成字符串
			ResponseContent<String> mResponseContent = new ResponseContent<String>();
			mResponseContent.setEntity(content);
			mResponseContent.setHeads(allHeaders);
			mResponseContent.setmResultCode(statuCode);
			if(statuCode == HttpURLConnection.HTTP_OK){
				return mResponseContent;
			}
			return mResponseContent;
		} catch (MalformedURLException e) {
			logger.w("URL is mistake!",e);
		} catch (IOException e) {
			logger.w("connect error!",e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ResponseContent<String> doGet(String url, Object params, Header[] headers, String charset, int timeout, boolean isSingle) {
		if(mHttpClient == null){
			mHttpClient = SingleHttpClient.getInstance(isSingle);
		}
		// 序列化参数
		if(params != null){
			if(String.class.isAssignableFrom(params.getClass())) {
				//传入参数为String
				if(!params.equals("")){
					url += "?" + params;
				}
			}else{
				HashMap<String, Object> paramMap = null;
				if(!params.getClass().isAssignableFrom(HashMap.class)){
					//传入参数不为HashMap则先转为Hashmap
					paramMap = ReflectUtils.transformBeanToHashMap(params.getClass(), params);
				}else{
					paramMap = (HashMap<String, Object>) params;
				}
				//传入参数为Hashmap
				StringBuffer sb = new StringBuffer();
				for (Entry<String, Object> param : paramMap.entrySet()) {
					try {
						sb.append(param.getKey()).append("=").append(URLEncoder.encode(String.valueOf(param.getValue()), charset)).append("&");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				if(sb.length() >= 1){
					sb.deleteCharAt(sb.length()-1);
				}
				url += "?" + sb.toString();
			}
		}
		
		// 开始请求
		try {
			logger.i("http request,url:"+url);
			HttpGet mHttpGet = new HttpGet(url);
			if(headers != null && headers.length > 0){
				mHttpGet.setHeaders(headers);
			}
			mHttpGet.addHeader("content-type", "application/x-www-form-urlencoded");
			mHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
			HttpResponse response;
			try {
				response = mHttpClient.execute(mHttpGet); //请求并返回结果
			} catch (Exception e) {
				logger.w("http time out,url:"+url, e);
				return null;
			}

			int statuCode = response.getStatusLine().getStatusCode();
			logger.i("statu code : " + statuCode);
			Header[] allHeaders = response.getAllHeaders();
			for (Header header : allHeaders) {
				logger.i(header.getName() + " : " + header.getValue());
			}
			HttpEntity entity = response.getEntity(); //拿到返回的实体
			String content = EntityUtils.toString(entity, charset); //把实体按照编码转成字符串
			
			// 结果装入OpResult
			ResponseContent<String> mResponseContent = new ResponseContent<String>();
			mResponseContent.setEntity(content);
			mResponseContent.setHeads(allHeaders);
			mResponseContent.setmResultCode(statuCode);
			return mResponseContent;
		} catch (MalformedURLException e) {
			logger.w("URL is mistake!", e);
		} catch (IOException e) {
			logger.w("connect error!", e);
		}
		return null;
	}
}