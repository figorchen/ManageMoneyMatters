// Copyright (C) 2012-2014 UUZZ All rights reserved
package com.uuzz.android.util;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 类 名: ScreenTools<br/>
 * 描 述: 屏幕相关工具类<br/>
 * 作 者: 杨禹恒<br/>
 * 创 建： 2014-12-3<br/>
 * 
 * 历 史: (版本) 作者 时间 注释 <br/>
 */
public class ScreenTools {

    /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
	
	
	/**
	 * Description:获取屏幕分辨率<br>
	 * Created by 李京蔚 on 2014年12月4日.<br>
	 * 
	 * @return int[0]=x,int[1]=y<br>
	 */
	public static int[] getScreenPixel(Context context) {
		int[] screenPixel = new int[2];
		DisplayMetrics lDisplayMetrics;
		lDisplayMetrics = context.getResources().getDisplayMetrics();
		int width = lDisplayMetrics.widthPixels > lDisplayMetrics.heightPixels ? lDisplayMetrics.heightPixels
				: lDisplayMetrics.widthPixels;
		int height = lDisplayMetrics.heightPixels > lDisplayMetrics.widthPixels ? lDisplayMetrics.heightPixels
				: lDisplayMetrics.widthPixels;
		screenPixel[0] = width;
		screenPixel[1] = height;
		return screenPixel;
	}

}
