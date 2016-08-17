/**
 * 项目名称：公用工具包
 * 文件名称: FileLog.java
 * <p/>
 * Created by 谌珂 on 2015/12/20.
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.util.log;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.uuzz.android.util.FileUtil;
import com.uuzz.android.util.TimeUtil;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 项目名称：公用工具包<br/>
 * 类  名: FileLog<br/>
 * 类描述: 把日志转到静态文件中存储<br/>
 * @author 谌珂 <br/>
 * 版    本：1.0.0<br/>
 * 修改时间： 2015/12/20
 */
public class FileLog {

    private static final String TAG = "FileLog";

    /** 日志缓冲对象 */
    private static final StringBuffer mLogCache = new StringBuffer();
    /** 日志文件的保存路径 */
    private static final String LOG_FILE_FOLDER = FileUtil.getRootFilePath() + "log/";
    /** 日志文件的保存路径 */
    private static final String LOG_FILE_PATH = LOG_FILE_FOLDER + "log.txt";
    /** 日志文件的对象 */
    private File logFile;
    /** 是否输出日志,默认为true */
    // ReleaseConfig: 谌珂 2016/3/16 是否输出文件日志
    private static final boolean IS_OUTPUT = false;
    /** 日志文件大小最大值,单位为M */
    // ReleaseConfig: 谌珂 2016/3/16 文件日志最大容量
    private static final int MAX_LOG_LENGTH = 2*1024*1024;
    /** 日志缓冲区最大值,单位为KB */
    // ReleaseConfig: 谌珂 2016/3/16 日志缓冲最大容量
    private static final int MAX_CACHE_LENGTH = 1000*1024;
    /** 子线程管理器 */
    private Handler mHandler;
    /** 锁对象 */
    private final Object lock = new Object();
    /** 是否直接写日志进文件而不整理文件大小，为了减少保存日志的时间 */
    private boolean isDirectWrite = false;

    private static final FileLog mInstance = new FileLog();

    private FileLog() {
        logFile = getLogFile();
        /* 子线程 */
        HandlerThread mHandlerThread = new HandlerThread(TAG);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    /**
     * 描 述：单例构造器<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/6/6 注释 <br/>
     */
    public static FileLog getInstance() {
        return mInstance;
    }


    /**
     * 描 述：把日志中的缓存全部写入文件,为了保证运行速度不对日志文件大小进行整理<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/22 注释 <br/>
     */
    private synchronized void flushLog(){
        isDirectWrite = true;
        synchronized (mLogCache){
            mLogCache.append(createLogHeader("tip", TAG))
                    .append("There was a serious error happened, check out above log to find out;\r\n");
            mLogCache.append("Product Model: ").append(android.os.Build.MODEL)
                    .append(", SDK Version: ").append(android.os.Build.VERSION.SDK)
                    .append(", Android Version: ").append(android.os.Build.VERSION.RELEASE ).append(";\r\n");
            writeIntoFile(true);    //同步写入文件
        }
    }

    /**
     * 描 述：把日志信息和异常存入日志缓存或静态文件中，为了保证安全，在存入之前将日志加密<br/>
     * 此方法线程安全<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/20 注释 <br/>
     * @param tag 存入日志文件的tag
     * @param level 存入日志文件的级别
     * @param msg 存入日志文件的信息
     * @param throwable 异常信息,为null时不输出
     */
    public void saveLog(String tag, String level, String msg, Throwable throwable){
        synchronized (mLogCache){
            if(!IS_OUTPUT){    //如果不让输出则直接返回
                return;
            }
            try {
                //写入日志
                String logContent = createLogHeader(level, tag) + msg + ";" + "\r\n";
                mLogCache.append(logContent);

                //写入异常
                if (throwable != null) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    throwable.printStackTrace(pw);
                    logContent = createLogHeader(level, tag) + sw.toString() + ";" + "\r\n";
                    mLogCache.append(logContent);
                }
            } catch (Exception e) {
                GlobalLog.e(TAG, "Encrypt log failed!", e);
            }

            if(TextUtils.equals(level, "Crash")){   //当日志级别为崩溃时进入同步写日志状态，仅当整个app崩溃时调用
                flushLog();              //整理手机型号系统版本，并同步写入文件
            }else{
                writeIntoFile(false);    //异步写入文件
            }
        }
    }

    /**
     * 描 述：将日志缓冲存入静态文件<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param isImmediately 如果为true则直接把缓存中的日志全部写入静态文件,当且仅当系统发生异常崩溃时设置为true调用
     */
    private void writeIntoFile(boolean isImmediately){

        if(isImmediately){          //同步执行写入文件
            asyncWrite(mLogCache);
            isDirectWrite = false;
        } else {                    //异步执行写入文件
            if(mLogCache.toString().getBytes().length < MAX_CACHE_LENGTH){
                return;
            }
            StringBuffer tempStringBuffer = new StringBuffer(mLogCache);
            mHandler.post(new AsyncWrite(tempStringBuffer));
        }

        mLogCache.setLength(0);
    }

    /**
     * 描 述：真正的写入日志在这个Runnable中<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     */
    private class AsyncWrite implements Runnable {
        StringBuffer tempStringBuffer;

        AsyncWrite(StringBuffer tempStringBuffer) {
            this.tempStringBuffer = tempStringBuffer;
        }

        @Override
        public void run() {
            asyncWrite(this.tempStringBuffer);
        }
    }

    /**
     * 描 述：同步写日志<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/22 注释 <br/>
     */
    private void asyncWrite(StringBuffer sb){
        synchronized (lock){
            FileWriter writer = null;
            try {
                writer = new FileWriter(LOG_FILE_PATH, true);
                writer.write(sb.toString());
                writer.flush();
            } catch (Exception e) {
                GlobalLog.e(TAG, "write log into log.txt failed!", e);
            } finally {
                safeClose(writer);
                sb.setLength(0);
                arrangeLogFile();       //最后整理下日志文件，防止日志文件大小超过最大值
            }
        }
    }

    /**
     * 描 述：根据日志级别和标签生成日志消息头<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param level 级别
     * @param tag 标签
     * @return 当前时间+级别+标签
     */
    private String createLogHeader(String level, String tag){
        return TimeUtil.getTime(System.currentTimeMillis()) + "   " + level + "--" + tag + " : ";
    }

    /**
     * 描 述：获取日志文件<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/20 注释 <br/>
     * @return 日志文件
     */
    private File getLogFile(){
        if(logFile != null){
            return logFile;
        }
        File dir = new File(LOG_FILE_FOLDER);
        if(!dir.exists()){    //创建文件夹
            dir.mkdirs();
        }
        File file = new File(LOG_FILE_PATH);
        if(!file.exists()){   //创建文件
            try {
                file.createNewFile();
            } catch (IOException e) {
                GlobalLog.e(TAG, "create file \"log.txt\" in "+ LOG_FILE_FOLDER +" failed!", e);
            }
        }
        return file;
    }

    /**
     * 描 述：整理日志文件,当日志文件大小超过阈值时则从文件开始部分删除阈值1/4大小的内容<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/20 注释 <br/>
     */
    private void arrangeLogFile(){
        if(isDirectWrite){    //不允许整理日志大小，直接返回
            return;
        }
        if(logFile.length() < MAX_LOG_LENGTH){   //缓冲大小没达到临界值，直接返回
            return;
        }
        if(logFile == null){
            logFile = getLogFile();
        }
        GlobalLog.e(TAG, "start arrange");

        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            File tempFile = new File(LOG_FILE_FOLDER + "temp.text");
            pw = new PrintWriter(tempFile);
            br = new BufferedReader(new FileReader(logFile));
            StringBuilder sb = new StringBuilder();
            String lineStr;
            boolean isFindStart = false;
            while (br.ready() && (lineStr = br.readLine()) != null) {
                if(isDirectWrite){   //发现此状态被改变为true马上放弃当前操作并删除临时文件
                    sb.setLength(0);
                    tempFile.delete();
                    GlobalLog.e(TAG, "pause arrange");
                    return;
                }
                if(!isFindStart) {
                    sb.append(lineStr);//每次按行读出来的数据都添加进字符串
                }
                if(!isFindStart && sb.toString().getBytes().length < MAX_LOG_LENGTH/2){//如果内容没有超过阈值的1/4则跳过
                    continue;
                }
                isFindStart = true;
                pw.println(lineStr);  //把需要保存的日志写入流
                pw.flush();
            }
            sb.setLength(0);
            logFile.delete();               //删除原日志文件
            tempFile.renameTo(logFile);     //把心日志文件重新命名为日志文件名
            logFile = getLogFile();             //重新引用新的日志文件对象
            GlobalLog.e(TAG, "complete arrange"+logFile.length()/1024);
        } catch (IOException e) {
            GlobalLog.e(TAG, "read \"log.txt\" or write \"temp.txt\" failed!", e);
        } finally {
            safeClose(br);
            safeClose(pw);
        }
    }

    /**
     * 描 述：关闭流对象<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2015/12/21 注释 <br/>
     * @param closeable 需要关闭的流对象
     */
    private void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                GlobalLog.e(TAG, "close Closeable failed!", e);
            }
        }
    }
}
