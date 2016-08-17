/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: ShadeView.java <br/>
 * <p/>
 * Created by 谌珂 on 2016/1/28.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 项目名称：手机大管家 <br/>
 * 类  名: ShadeView <br/>
 * 类描述: 阴影层<br/>
 * 拦截所有的touch事件，保证事件不会向下专递<br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2016/1/28 <br/>
 * @author 谌珂 <br/>
 */
public class ShadeView extends View {
    public ShadeView(Context context) {
        super(context);
    }

    public ShadeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShadeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
