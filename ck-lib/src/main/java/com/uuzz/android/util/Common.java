/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: Common.java <br/>
 * <p/>
 * Created by 谌珂 on 2015/12/29.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.util;

/**
 * 项目名称：手机大管家 <br/>
 * 类  名: Common
 * 类描述: 手机大管家全局使用的常量
 * @author 谌珂 <br/>
 * 实现的主要功能
 * 版    本：1.0.0
 * 修改时间：2015/12/29 
 */
public class Common {

    /** 包名,只要继承了lib下的Application就不需要在此设置 */
    public static String PACKAGE_NAME;

    /** 默认超时时间 */
    // ReleaseConfig: 谌珂 2016/3/16 配置默认超时时间
    public static final int DEFAULT_TIME_OUT = 15*1000;
    /** socket超时时间，超出则断开socket */
    public static final int SO_TIMEOUT = DEFAULT_TIME_OUT;
    /** 连接超时时间，超出则断开连接 */
    public static final int CONNECTION_TIMEOUT = DEFAULT_TIME_OUT;
    /** Http超时时间 */
    public static final int HTTP_TIMEOUT = DEFAULT_TIME_OUT;

    /** 是否输出日志 */
    // ReleaseConfig: 谌珂 2016/3/16 配置日志是否需要输出
    public static boolean isShowVerbose = true;
    public static boolean isShowDebug = true;
    public static boolean isShowInfo = true;
    public static boolean isShowWarn = true;
    public static boolean isShowError = true;

    /** 数据库xml配置文件的文件夹路径 */
    public static final String DB_FOLDER = "databases";
    /** 数据库名称 */
    public static final String DB_NAME = "data.db";
    /** 数据库初始化脚本文件称 */
    public static final String DB_INIT_SQL = "init.sql";
}
