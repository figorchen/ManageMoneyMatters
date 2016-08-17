/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: Utils.java <br/>
 * <p/>
 * Created by 谌珂 on 2015/12/30.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.uuzz.android.util.log.UUZZLog;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.UUID;

/**
 * 项目名称：手机大管家 <br/>
 * 类  名: Utils <br/>
 * 类描述: <br/>
 * 实现的主要功能 <br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2015/12/30 <br/>
 * @author 谌珂 <br/>
 */
public class Utils {

    private static final UUZZLog logger = new UUZZLog(Utils.class);

    /**
     * 描 述：获取版本号<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/30 注释 <br/>
     */
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            logger.i("cast version to integer failed!", e);
        }
        return version;
    }

    /**
     * 获取泛型类型
     * @param field 属性
     * @return
     */
    public static Class<?> getFieldTClass(Field field){
        Type genericType = field.getGenericType();
        if(genericType != null){
            // 把声明的字段类型强转为模板参数类型
            ParameterizedType parameterizedType = (ParameterizedType)genericType;
            // 拿到泛型的Class
            Class<?> fieldTClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            return fieldTClass;
        }
        return null;
    }

    /**
     * 描 述：获取应用版本<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/1/4 注释 <br/>
     * @param context 上下文
     * @return app的版本号
     * @throws Exception 获取包信息出错抛出异常
     */
    public static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

    /**
     * 描 述：检测网络是否可用<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/1/15 注释 <br/>
     * @param context 上下文
     * @return true可用，false不可用
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }


    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMWAP = 0x02;
    public static final int NETTYPE_CMNET = 0x03;
    /**
     * 描 述：获取当前网络类型<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/1/15 注释 <br/>
     * @param context 上下文
     * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
     */
    public static int getNetworkType(Context context) {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if(!TextUtils.isEmpty(extraInfo)){
                if (extraInfo.toLowerCase().equals("cmnet")) {
                    netType = NETTYPE_CMNET;
                } else {
                    netType = NETTYPE_CMWAP;
                }
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }

    /**
     * 描 述：获取机器码<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/3/1 注释 <br/>
     */
    public static String getIMEI(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = String.valueOf(tm.getDeviceId());
        tmSerial = String.valueOf(tm.getSimSerialNumber());
        androidId = String.valueOf(android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID));
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString();
    }

    /**
     * 描 述：根据drawable下的图片名称获取uri<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/3/16 注释 <br/>
     */
    public static Uri getUriFromImageName(String imageName) {
        return Uri.parse("android.resource://" + Common.PACKAGE_NAME + "/drawable/imageName");
    }
}
