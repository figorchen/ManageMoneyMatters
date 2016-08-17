/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: Toaster.java <br/>
 * <p/>
 * Created by 谌珂 on 2016/1/7.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 项目名称：手机大管家 <br/>
 * 类  名: Toaster <br/>
 * 类描述: <br/>
 * 实现的主要功能 <br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2016/1/7 <br/>
 * @author 谌珂 <br/>
 */
public class Toaster {

    public static void showShortToast(Context context, String content){
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 10, 10);
        toast.show();
    }

    public static void showShortToast(Context context, int resourceId){
        Toast toast = Toast.makeText(context, resourceId, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLongToast(Context context, String content){
        Toast toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER, 10, 10);
        toast.show();
    }

    public static void showLongToast(Context context, int resourceId){
        Toast toast = Toast.makeText(context, resourceId, Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER, 10, 10);
        toast.show();
    }
}
