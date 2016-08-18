package com.uuzz.android.util.net.http;

import android.os.AsyncTask;

import com.uuzz.android.util.net.request.RequestParams;
import com.uuzz.android.util.net.response.ResponseContent;

import org.apache.http.Header;


/**
 * @param <E> 传入的消息实体类型，支持String、Map、JavaBean
 * @param <T> 返回的消息实体类型
 */
public abstract class BaseHttp<E, T> {

	//文件保存路径
	protected String path = null;
	protected MyAsyncTask asyncTask;
	protected HttpRequestListener<T> listener;
	protected abstract ResponseContent<T> doPost(String url, E params, Header[] headers, String charset, int timeout, boolean isSingle);
	protected abstract ResponseContent<T> doGet(String url, E params, Header[] headers, String charset, int timeout, boolean isSingle);

	/**
	 * 描 述：请求http的入口<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/1/3 注释 <br/>
	 * @param url 请求链接
	 * @param params 请求实体对象（JavaBean、Map、String）
	 * @param headers 请求头
	 * @param charset 字符编码
	 * @param timeout 超时时间
     * @param isGet 是否为get请求
     * @return 响应体
     */
	private ResponseContent<T> doHttp(String url, E params, Header[] headers, String charset, int timeout, boolean isGet, boolean isSingle){
		if(isGet){
			return doGet(url, params, headers, charset, timeout, isSingle);
		}else{
			return doPost(url, params, headers, charset, timeout, isSingle);
		}
	}
	
	/**
	 * 同步调用http请求，结果直接返回到OpResult中
	 * @param requestParams 请求参数
	 * @return 响应参数
	 */
	public ResponseContent<T> httpRequest(RequestParams<E> requestParams){
		this.path = requestParams.getPath();
		return doHttp(requestParams.getUrl(),
				requestParams.getParams(),
				requestParams.getHeaders(),
				requestParams.getCharset(),
				requestParams.getTimeout(),
				requestParams.isGet(),
				requestParams.isSignle());
	}
	
	/**
	 * 异步调用http请求，结果将传入回调函数中
	 * @param requestParams 请求参数
	 * @param mListener 回调函数
	 */
	public AsyncTask httpRequest(RequestParams<E> requestParams,
							HttpRequestListener<T> mListener){
		listener = mListener;
		asyncTask = new MyAsyncTask();
		asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, requestParams);
		return asyncTask;
	}

	/**
	 * 描 述：异步请求任务<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/1/3 注释 <br/>
	 */
	protected class MyAsyncTask extends AsyncTask<RequestParams<E>,
			Integer, ResponseContent<T>> {

		/**
		 * 暴漏出的更新进度条方法
		 * @param progress 进度标示
         */
		public void updateProgress(int progress){
			publishProgress(progress);
		}
		
		@Override
		protected ResponseContent<T> doInBackground(RequestParams<E>... params) {
			return httpRequest(params[0]);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			listener.updateProgress(values[0]);
		}

		@Override
		protected void onPostExecute(ResponseContent<T> result) {
			super.onPostExecute(result);
			listener.doInMainThread(result);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			listener.onCancelled();
		}
	}

	/**
	 * 描 述：异步请求的毁掉方法<br/>
	 * {@link #doInMainThread(ResponseContent)} http在没被取消的情况下回调到主线程{@link #doInMainThread},如果在请求过程中http层出现问题则会返回null<br/>
	 * {@link #onCancelled()} AsyncTask被取消后回调到取消方法（主线程）{@link #onCancelled}<br/>
	 * {@link #updateProgress(int)} 进度条更新会返回一个int型整数，用来代表进度（主线程）{@link #onCancelled}<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/1/3 注释 <br/>
	 */
	public interface HttpRequestListener<T> {
		/**
		 * 当子线程中所有任务都执行完以后会切换到主线程调用此方法
		 * @param result
		 */
		void doInMainThread(ResponseContent<T> result);

		/**
		 * 当取消任务后被调用
		 */
		void onCancelled();

		/**
		 * 更新进度条时调用此方法
		 * @param progress
		 */
		void updateProgress(int progress);
	}
}
