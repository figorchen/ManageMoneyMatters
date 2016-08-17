package com.uuzz.android.util.log;

import android.util.Log;

import com.uuzz.android.util.Common;

public class GlobalLog {
	
	public static void e(String TAG, String paramString) {
		if (Common.isShowError){
			paramString = initLogMsg("Error", paramString);
			Log.e(TAG, paramString);
		}
	}

	public static void e(String TAG, String paramString,
						 Throwable paramThrowable) {
		if (Common.isShowError){
			paramString = initLogMsg("Error", paramString);
			Log.e(TAG, paramString, paramThrowable);
		}
	}

	public static void w(String TAG, String paramString) {
		if (Common.isShowWarn){
			paramString = initLogMsg("Warn", paramString);
			Log.w(TAG, paramString);
		}
	}

	public static void w(String TAG, String paramString,
						 Throwable paramThrowable) {
		if (Common.isShowWarn){
			paramString = initLogMsg("Warn", paramString);
			Log.w(TAG, paramString, paramThrowable);
		}
	}

	public static void i(String TAG, String paramString) {
		if (Common.isShowInfo){
			paramString = initLogMsg("Info", paramString);
			Log.i(TAG, paramString);
		}
	}

	public static void i(String TAG, String paramString,
						 Throwable paramThrowable) {
		if (Common.isShowInfo){
			paramString = initLogMsg("Info", paramString);
			Log.e(TAG, paramString, paramThrowable);
		}
	}

	public static void d(String TAG, String paramString) {
		if (Common.isShowDebug){
			paramString = initLogMsg("Debug", paramString);
			Log.d(TAG, paramString);
		}
	}

	public static void d(String TAG, String paramString,
						 Throwable paramThrowable) {
		if (Common.isShowDebug){
			paramString = initLogMsg("Debug", paramString);
			Log.d(TAG, paramString, paramThrowable);
		}
	}

	public static void v(String TAG, String paramString) {
		if (Common.isShowVerbose){
			paramString = initLogMsg("Verbose", paramString);
			Log.v(TAG, paramString);
		}
	}

	public static void v(String TAG, String paramString,
						 Throwable paramThrowable) {
		if (Common.isShowVerbose){
			paramString = initLogMsg("Verbose", paramString);
			Log.v(TAG, paramString, paramThrowable);
		}
	}

	/**
	 * 描 述：整理log消息<br/>
	 * @author:谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
	 */
	public static String initLogMsg(String level, String msg){
		return level + " : " + (msg == null ? "" : msg);
	}
}