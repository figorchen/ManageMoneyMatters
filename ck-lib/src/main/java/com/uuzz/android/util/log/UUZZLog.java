/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: UUZZLog.java <br/>
 * <p/>
 * Created by 谌珂 on 2015/12/21.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.util.log;


/**
 * 项目名称：手机大管家 <br/>
 * 类  名: UUZZLog
 * 类描述: 对Global进行一层封装，引入静态日志保存和统一的TAG管理
 * @author 谌珂 <br/>
 * 实现的主要功能
 * 版    本：1.0.0
 * 修改时间：2015/12/21 
 */
public final class UUZZLog {

    /** 输出日志的tag，通常情况下为类名 */
    private final String TAG;

    /** 屏蔽掉默认构造器 */
    private UUZZLog() {
        super();
        this.TAG = "";
    }

    public UUZZLog(Class cls) {
        this.TAG = cls.getSimpleName();
    }

    public UUZZLog(String tag) {
        this.TAG = tag;
    }

    /**
     * 描 述：输出verbose级别日志<br/>
     * @author：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param msg 日志信息
     */
    public void v(String msg){
        GlobalLog.v(TAG, msg);
        FileLog.getInstance().saveLog(TAG, "Verbose", msg, null);
    }

    /**
     * 描 述：输出verbose级别日志<br/>
     * @author：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param msg 日志信息
     * @param throwable 异常对象
     */
    public void v(String msg, Throwable throwable){
        GlobalLog.v(TAG, msg, throwable);
        FileLog.getInstance().saveLog(TAG, "Verbose", msg, throwable);
    }

    /**
     * 描 述：输出debug级别日志<br/>
     * @author：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param msg 日志信息
     */
    public void d(String msg){
        GlobalLog.d(TAG, msg);
        FileLog.getInstance().saveLog(TAG, "Debug", msg, null);
    }

    /**
     * 描 述：输出debug级别日志<br/>
     * @author：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param msg 日志信息
     * @param throwable 异常对象
     */
    public void d(String msg, Throwable throwable){
        GlobalLog.d(TAG, msg, throwable);
        FileLog.getInstance().saveLog(TAG, "Debug", msg, throwable);
    }

    /**
     * 描 述：输出info级别日志<br/>
     * @author：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param msg 日志信息
     */
    public void i(String msg){
        GlobalLog.i(TAG, msg);
        FileLog.getInstance().saveLog(TAG, "Info", msg, null);
    }

    /**
     * 描 述：输出info级别日志<br/>
     * @author：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param msg 日志信息
     * @param throwable 异常对象
     */
    public void i(String msg, Throwable throwable){
        GlobalLog.i(TAG, msg, throwable);
        FileLog.getInstance().saveLog(TAG, "Info", msg, throwable);
    }

    /**
     * 描 述：输出warn级别日志<br/>
     * @author：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param msg 日志信息
     */
    public void w(String msg){
        GlobalLog.w(TAG, msg);
        FileLog.getInstance().saveLog(TAG, "Warn", msg, null);
    }

    /**
     * 描 述：输出warn级别日志<br/>
     * @author：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param msg 日志信息
     * @param throwable 异常对象
     */
    public void w(String msg, Throwable throwable){
        GlobalLog.w(TAG, msg, throwable);
        FileLog.getInstance().saveLog(TAG, "Warn", msg, throwable);
    }

    /**
     * 描 述：输出error级别日志<br/>
     * @author：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param msg 日志信息
     */
    public void e(String msg){
        GlobalLog.e(TAG, msg);
        FileLog.getInstance().saveLog(TAG, "Error", msg, null);
    }

    /**
     * 描 述：输出error级别日志<br/>
     * @author：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param msg 日志信息
     * @param throwable 异常对象
     */
    public void e(String msg, Throwable throwable){
        GlobalLog.e(TAG, msg, throwable);
        FileLog.getInstance().saveLog(TAG, "Error", msg, throwable);
    }

}
