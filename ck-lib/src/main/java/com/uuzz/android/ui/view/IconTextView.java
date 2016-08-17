/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: IconTextView.java <br/>
 * <p/>
 * Created by 谌珂 on 2016/1/8.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.uuzz.android.R;


/**
 * 项目名称：手机大管家 <br/>
 * 类  名: IconTextView <br/>
 * 类描述: 显示图标的控件，可转换为显示普通文字，默认为图标<br/>
 * 图标显示时因为跟文字的大小有区别，所以有两套大小<br/>
 * 实现的主要功能 <br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2016/1/8 <br/>
 * @author 谌珂 <br/>
 */
public class IconTextView extends TextView {
    
    private final String ICON_FONT_PATH = "fonts/iconfont.ttf";

    /** 图标字体库 */
    private static Typeface iconfont;
    /** 普通文字模式 */
    public static final int MODULE_TEXT = 0;
    /** 图标模式 */
    public static final int MODULE_ICON = 1;
    /** 是否显示通知 */
    private boolean isNotice;
    /** 画通知小圆圈的画笔 */
    private Paint mPaint = new Paint();
    {
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }


    public IconTextView(Context context) {
        super(context);
        if(iconfont == null){
            iconfont = Typeface.createFromAsset(context.getAssets(), ICON_FONT_PATH);
        }
        initView();
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(iconfont == null){
            iconfont = Typeface.createFromAsset(context.getAssets(), ICON_FONT_PATH);
        }
        setTypeface(iconfont);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(iconfont == null){
            iconfont = Typeface.createFromAsset(context.getAssets(), ICON_FONT_PATH);
        }
        setTypeface(iconfont);
    }

    /**
     * 描 述：加载字体库<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/1/8 注释 <br/>
     */
    @SuppressWarnings("deprecation")
    private void initView() {
        setMinWidth(getContext().getResources().getDimensionPixelOffset(R.dimen.head_icon_min_size));
        setMinHeight(getContext().getResources().getDimensionPixelOffset(R.dimen.head_icon_min_size));
        setTextColor(getResources().getColor(R.color.white));
        setGravity(Gravity.CENTER);
        setModle(IconTextView.MODULE_ICON);
    }

    /**
     * 描 述：设置字体模式<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/1/8 注释 <br/>
     * @param modle 模式编号
     */
    public void setModle(int modle) {
        switch (modle) {
            case MODULE_TEXT :             //文字模式
                setTypeface(Typeface.DEFAULT);
                setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.second_text_size));
                break;
            case MODULE_ICON :             //图标模式
                setTypeface(iconfont);
                setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.head_icon_size));
                break;
            default:
                break;
        }
    }

    /**
     * 描 述：显示icon上的小红点<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/2/23 注释 <br/>
     */
    public void showNotice() {
        isNotice = true;
        invalidate();
    }

    /**
     * 描 述：隐藏icon上的小红点<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/2/23 注释 <br/>
     */
    public void hideNotice() {
        isNotice = false;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画红色小圆圈  半径为控件宽度1/6  位置在右上角
        if(isNotice) {
            mPaint.setColor(Color.RED);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            int radius = getWidth() / 6;
            canvas.drawCircle(getWidth() - radius, radius, radius, mPaint);
        }
    }
}
