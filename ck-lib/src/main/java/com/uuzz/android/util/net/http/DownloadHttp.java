package com.uuzz.android.util.net.http;

import android.text.TextUtils;

import com.uuzz.android.util.ReflectUtils;
import com.uuzz.android.util.log.UUZZLog;
import com.uuzz.android.util.net.request.BaseRequestBean;
import com.uuzz.android.util.net.response.ResponseContent;

import org.apache.http.Header;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 用于下载文件的类，在主线程中调用将不会执行，需用异步任务或子线程执行
 * @author 谌珂
 *
 */
public class DownloadHttp<E> extends BaseHttp<E, String> {

	UUZZLog logger = new UUZZLog("DownloadHttpURLConnection");

	@Override
	protected ResponseContent<String> doPost(String url,
											 E params, Header[] headers, String charset, int timeout, boolean isSignle) {
		String threadName = Thread.currentThread().getName();
		if(threadName != null && threadName.equals("main")){
			logger.e("can not start download task in main thread!");
			return null;
		}
		if(TextUtils.isEmpty(this.path)){
			logger.e("file path is blank,stop download!");
			return null;
		}
		StringBuilder sb = new StringBuilder();
		if(params != null){
			if(String.class.isAssignableFrom(params.getClass())) {
				// params是一个String对象
				sb.append(params);
			}else{
				HashMap<String, Object> parameters = null;
				if(BaseRequestBean.class.isAssignableFrom(params.getClass())){
					parameters = ReflectUtils.transformBeanToHashMap(params.getClass(), params);
				}else{
					parameters = (HashMap<String, Object>) params;
				}
				for (Entry<String, Object> param : parameters.entrySet()) {
					try {
						sb.append(param.getKey()).append("=").append(URLEncoder.encode(String.valueOf(param.getValue()), charset)).append("&");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
				if(sb.length() >= 1){
					sb.deleteCharAt(sb.length()-1);
				}
			}
		}
		byte[] entity = sb.toString().getBytes();
		
		//开始请求
		HttpURLConnection conn = null;
		try {
			URL requestURL = new URL(url);
			logger.i(url);
			//开启链接
			conn = (HttpURLConnection) requestURL.openConnection();
			conn.setRequestMethod("POST");
			//不使用gip压缩下载
			conn.setRequestProperty("Accept-Encoding", "identity");
			//禁止自动重定向
			conn.setInstanceFollowRedirects(false);
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
//			写入正文长度
//			conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
			conn.setDoOutput(true);//设置可以向外写数据
			//获取输出流，把post请求写入正文
			OutputStream ops = conn.getOutputStream();
			ops.write(entity);
			try {
				conn.connect();
			} catch (Exception e) {
				logger.e("http download time out! url:"+url, e);
				return null;
			}
			int statuCode = conn.getResponseCode();//获取返回码
			logger.i("statu code : " + statuCode);
			ResponseContent<String> mResponseContent = new ResponseContent<String>();
			mResponseContent.setEntity(path);
			int size = conn.getContentLength();
			mResponseContent.setmResultCode(statuCode);
			if(statuCode == HttpURLConnection.HTTP_OK){
				InputStream inputStream = conn.getInputStream();
				FileOutputStream mFileOutputStream = new FileOutputStream(path);
				byte[] buffer = new byte[1024];
				int len;
				double length = 0;
				while((len = inputStream.read(buffer)) != -1){
					if(size != -1 && asyncTask != null){
						length += len;
						asyncTask.updateProgress((int)((length/1024/size)*100));
					}
					mFileOutputStream.write(buffer, 0, len);
				}
				asyncTask.updateProgress(100);
				inputStream.close();
				mFileOutputStream.close();
			}
			return mResponseContent;
		} catch (MalformedURLException e) {
			logger.e("URL is mistake!", e);
		} catch (IOException e) {
			logger.e("connect error!", e);
		} finally{
			path = null;
			if(conn != null){
				conn.disconnect();
				logger.i("conn is close");
			}else{
				logger.i("conn is null");
			}
		}
		return null;
	}

	@Override
	protected ResponseContent<String> doGet(String url, E params, Header[] headers,
			String charset, int timeout, boolean isSignle) {
		String threadName = Thread.currentThread().getName();
		if(threadName != null && threadName.equals("main")){
			logger.e("can not start download task in main thread!");
			return null;
		}
		if(TextUtils.isEmpty(this.path)){
			logger.e("file path is blank,stop download!");
			return null;
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
				}
				paramMap = (HashMap<String, Object>) params;
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
		
		//开始请求
		HttpURLConnection conn = null;
		try {
			URL requestURL = new URL(url);
			logger.i(url);
			//创建连接对象
			conn = (HttpURLConnection) requestURL.openConnection();
			//开启链接
			conn = (HttpURLConnection) requestURL.openConnection();
			conn.setRequestMethod("GET");
			//不使用gip压缩下载
			conn.setRequestProperty("Accept-Encoding", "identity");
			//禁止自动重定向
			conn.setInstanceFollowRedirects(false);
			conn.setConnectTimeout(timeout);
			conn.setReadTimeout(timeout);
			logger.i(String.valueOf(timeout));
			//开始请求
			try {
				conn.connect();
			} catch (Exception e) {
				logger.e("http download time out! url:"+url, e);
				return null;
			}
			int statuCode = conn.getResponseCode();//获取返回码
			logger.i("statu code : " + statuCode);
			int size = conn.getContentLength()/1024;
			ResponseContent<String> mResponseContent = new ResponseContent<String>();
			mResponseContent.setEntity(path);
			mResponseContent.setmResultCode(statuCode);
			if(statuCode == HttpURLConnection.HTTP_OK){
				InputStream inputStream = conn.getInputStream();
				FileOutputStream mFileOutputStream = new FileOutputStream(path);
				byte[] buffer = new byte[1024];
				int len = 0;
				double length = 0;
				while((len = inputStream.read(buffer)) != -1){
					if(size != -1 && asyncTask != null){
						length += len;
						asyncTask.updateProgress((int)((length/1024/size)*100));
					}
					mFileOutputStream.write(buffer, 0, len);
				}
				asyncTask.updateProgress(100);
				inputStream.close();
				mFileOutputStream.close();
			}
			return mResponseContent;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.e("URL is mistake!");
		} catch (IOException e) {
			e.printStackTrace();
			logger.e("connect error!");
		} finally{
			path = null;
			if(conn != null){
				conn.disconnect();
				logger.i("conn is close");
			}else{
				logger.i("conn is null");
			}
		}
		return null;
	}
}
