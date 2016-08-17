// Copyright (C) 2012-2013 UUZZ All rights reserved
package com.uuzz.android.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.uuzz.android.R;
import com.uuzz.android.util.ScreenTools;

/**
 * 圆角图片控件
 * @author Chunk E-mail:348353610@qq.com
 * @version Created time：2015-7-8 下午6:40:42
 */
public class CircularImageView extends ImageView {
	private Paint paint;
	private int roundWidth = 6;
	private int roundHeight = 6;
	private Paint paint2;
	private boolean misCircular = true;
	private String mShadeText;
	private boolean isShowShade;
//	private final float mLetterSpace = 0.1f;


	public CircularImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public CircularImageView(Context context) {
		super(context);
		init(context, null);
	}

	public void setCircular(boolean lisCircular) {
		misCircular = lisCircular;
	}

	private void init(Context context, AttributeSet attrs) {

		if (attrs != null) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.RoundAngleImageView);
			roundWidth = a.getDimensionPixelSize(
					R.styleable.RoundAngleImageView_roundWidth, roundWidth);
			roundHeight = a.getDimensionPixelSize(
					R.styleable.RoundAngleImageView_roundHeight, roundHeight);
			a.recycle();
		} else {
			float density = context.getResources().getDisplayMetrics().density;
			roundWidth = (int) (roundWidth * density);
			roundHeight = (int) (roundHeight * density);
		}

		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

		paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setFilterBitmap(true);
		paint2.setXfermode(null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawShade(canvas);
	}

	@Override
	public void draw(Canvas canvas) {
		if (!misCircular) {
			roundWidth = 0;
			roundHeight = 0;
		}
		Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Config.ARGB_8888);
		Canvas canvas2 = new Canvas(bitmap);

		super.draw(canvas2);
		drawLiftUp(canvas2);
		drawRightUp(canvas2);
		drawLiftDown(canvas2);
		drawRightDown(canvas2);
		canvas.drawBitmap(bitmap, 0, 0, paint2);
//		bitmap.recycle();
	}

	/**
	 * 描 述：画出阴影文字<br/>
	 * @author：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/11/13 注释 <br/>
	 * @param canvas
	 */
	private void drawShade(Canvas canvas) {
		if(!isShowShade){
			return;
		}
		int lHtartHeight = canvas.getHeight()/4*3;
		Paint paint = new Paint();
		paint.setColor(Color.parseColor("#99000000"));
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setXfermode(null);
		//画出阴影背景
		canvas.drawRect(0, lHtartHeight, canvas.getWidth(), canvas.getHeight(), paint);


		if(TextUtils.isEmpty(mShadeText)){
			return;
		}
		//画出文字
		paint.setColor(Color.parseColor("#FFFFFFFF"));

		//设置文字间距
//		paint.setLetterSpacing(mLetterSpace);
		//计算文字大小
		float lTextSize = ScreenTools.dip2px(this.getContext(), 8f);

		paint.setTextSize(lTextSize);
		//起始位置的x坐标
		float lTextWidth = paint.measureText(mShadeText);
		float gap = (getWidth() - lTextWidth)/2;
		Paint.FontMetrics fm = paint.getFontMetrics();
		//基线的Y坐标
		float lBaseLineY = lHtartHeight / 6 - fm.descent + (fm.descent - fm.ascent) / 2 + lHtartHeight;
		canvas.drawText(mShadeText, gap, lBaseLineY, paint);
//		float lTextWidth = paint.measureText(mShadeText);
	}

	private void drawLiftUp(Canvas canvas) {
		Path path = new Path();
		path.moveTo(0, roundHeight);
		path.lineTo(0, 0);
		path.lineTo(roundWidth, 0);
		path.arcTo(new RectF(0, 0, roundWidth * 2, roundHeight * 2), -90, -90);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void drawLiftDown(Canvas canvas) {
		Path path = new Path();
		path.moveTo(0, getHeight() - roundHeight);
		path.lineTo(0, getHeight());
		path.lineTo(roundWidth, getHeight());
		path.arcTo(new RectF(0, getHeight() - roundHeight * 2,
				0 + roundWidth * 2, getWidth()), 90, 90);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void drawRightDown(Canvas canvas) {
		Path path = new Path();
		path.moveTo(getWidth() - roundWidth, getHeight());
		path.lineTo(getWidth(), getHeight());
		path.lineTo(getWidth(), getHeight() - roundHeight);
		path.arcTo(new RectF(getWidth() - roundWidth * 2, getHeight()
				- roundHeight * 2, getWidth(), getHeight()), 0, 90);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void drawRightUp(Canvas canvas) {
		Path path = new Path();
		path.moveTo(getWidth(), roundHeight);
		path.lineTo(getWidth(), 0);
		path.lineTo(getWidth() - roundWidth, 0);
		path.arcTo(new RectF(getWidth() - roundWidth * 2, 0, getWidth(),
				0 + roundHeight * 2), -90, 90);
		path.close();
		canvas.drawPath(path, paint);
	}

	/**
	 * 描 述：设置图片底部阴影文字<br/>
	 * @author：谌珂<br/>
	 * 历 史: (版本) 谌珂 2015/11/13 注释 <br/>
	 * @param pIsShowShade 是否显示阴影区域和文字
	 * @param pShadeText  阴影区域内容
	 */
	public void setShadeContent(boolean pIsShowShade, String pShadeText){
		this.isShowShade = pIsShowShade;
		this.mShadeText = pShadeText;
	}
}
