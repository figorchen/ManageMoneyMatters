package com.uuzz.android.ui.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.uuzz.android.R;

/**
 * 显示账户图片的控件，圆形、带外环线
 * @author Chunk E-mail:348353610@qq.com
 * @version Created time：2015-7-8 下午6:40:42
 */
public class CircleImageView extends ImageView {

//    private UUZZLog logger = new UUZZLog("CircleImageView");

	private int defaultWidth;
	private int defaultHeight;
    /** 外边框厚度 */
	private int mOutsideBorderThickness = 0;
    /** 内边框厚度 */
	private int mInsideBorderThickness = 0;
    /** 外边框与内边框之间的距离 */
	private int mOutsideBorderDistance = 0;
    /** 内边框与图片之间的距离 */
	private int mInsideBorderDistance = 0;
    /** 默认说明文字,如果为空则不画说明文字和背景 */
    private String mContent;
    /** 说明文字背景色 */
    private int mContentBackground;
    /** 说明文字颜色 */
    private int mContentColor;
    /** 说明文字大小 */
    private int mContentSize;
    private final int defaultColor = 0xFFFFFFFF;
    // 如果只有其中一个有值，则只画一个圆形边框
    /** 外边框颜色 */
    private int mOutsideBorderColor = 0;
    /** 内边框颜色 */
    private int mInsideBorderColor = 0;
	private Context mContext;

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;  
        setCustomAttributes(attrs); 
	}
	
	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;  
        setCustomAttributes(attrs); 
	}

	public CircleImageView(Context context) {
		super(context);
		mContext = context;
	}

	private void setCustomAttributes(AttributeSet attrs) {
        TypedArray a = mContext.obtainStyledAttributes(attrs,  
                R.styleable.uuzzcircleimageview);
        mOutsideBorderThickness = a.getDimensionPixelSize(  
                R.styleable.uuzzcircleimageview_outside_border_thickness, 0);
        mOutsideBorderDistance = a.getDimensionPixelSize(
                R.styleable.uuzzcircleimageview_outside_border_distance, 0);
        mOutsideBorderColor = a
                .getColor(R.styleable.uuzzcircleimageview_outside_border_color,
                        defaultColor);  
        mInsideBorderThickness = a.getDimensionPixelSize(
                R.styleable.uuzzcircleimageview_outside_border_thickness, 0);
        mInsideBorderDistance = a.getDimensionPixelSize(
                R.styleable.uuzzcircleimageview_outside_border_distance, 0);
        mInsideBorderColor = a
                .getColor(R.styleable.uuzzcircleimageview_outside_border_color,
                        defaultColor);
        mContentBackground = a
                .getColor(R.styleable.uuzzcircleimageview_content_background_color,
                        defaultColor);
        mContentColor = a
                .getColor(R.styleable.uuzzcircleimageview_content_color,
                        defaultColor);
        mContent = a.getString(R.styleable.uuzzcircleimageview_content_text);
        mContentSize = a.getDimensionPixelSize(R.styleable.uuzzcircleimageview_content_text_size, 16);
        a.recycle();
    }  

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();  
        if (drawable == null) {  
            return;  
        }  
  
        if (getWidth() == 0 || getHeight() == 0) {  
            return;
        }  
        this.measure(0, 0);  
        if (drawable.getClass() == NinePatchDrawable.class)  
            return;  
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();  
        Bitmap bitmap = b.copy(Config.ARGB_8888, true);
        if (defaultWidth == 0) {  
            defaultWidth = getWidth();  
  
        }  
        if (defaultHeight == 0) {  
            defaultHeight = getHeight();  
        }  
        // 保证重新读取图片后不会因为图片大小而改变控件宽、高的大小（针对宽、高为wrap_content布局的imageview，但会导致margin无效）  
        // if (defaultWidth != 0 && defaultHeight != 0) {  
        // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(  
        // defaultWidth, defaultHeight);  
        // setLayoutParams(params);  
        // }  
        int radius;
        if(mOutsideBorderThickness !=0
        		&& (mOutsideBorderColor & 0xFF000000) != 0){// 外边框距离、外边框厚度不为0且外边框颜色不透明
        	// 定图片半径
        	radius = (defaultWidth < defaultHeight ? defaultWidth
            		: defaultHeight) / 2 - mOutsideBorderThickness - mOutsideBorderDistance;
        	// 画外边框
        	drawCircleBorder(canvas, radius + mOutsideBorderDistance, mOutsideBorderThickness, mOutsideBorderColor);
//            logger.d("draw outside border, outsideRadius="+String.valueOf(radius + mOutsideBorderDistance)
//                    +",outsideThickness="+String.valueOf(mOutsideBorderThickness)
//                    +",outsideColor="+String.valueOf(mOutsideBorderColor));
        	// 如果内边框达到绘制要求则绘制内边框
        	if(mInsideBorderThickness !=0
            		&& (mInsideBorderColor & 0xFF000000) != 0){
        		// 画内边框
        		drawCircleBorder(canvas, radius + mInsideBorderDistance, mInsideBorderThickness, mInsideBorderColor);
//                logger.d("draw outside border, insideRadius="+String.valueOf(radius + mInsideBorderDistance)
//                        +",insideThickness="+String.valueOf(mInsideBorderThickness)
//                        +",insideColor="+String.valueOf(mInsideBorderColor));
        	}
        }else if(mInsideBorderThickness !=0
        		&& (mInsideBorderColor & 0xFF000000) != 0){
        	radius = (defaultWidth < defaultHeight ? defaultWidth
            		: defaultHeight) / 2 - mInsideBorderThickness - mInsideBorderDistance;
        	// 画内边框
        	drawCircleBorder(canvas, radius + mInsideBorderDistance, mInsideBorderThickness, mInsideBorderColor);
//            logger.d("draw outside border, insideRadius="+String.valueOf(radius + mInsideBorderDistance)
//                    +",insideThickness="+String.valueOf(mInsideBorderThickness)
//                    +",insideColor="+String.valueOf(mInsideBorderColor));
        }else{
        	radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2;
        }
//        logger.d("radius="+String.valueOf(radius));
        Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, radius);
        canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius, defaultHeight
                / 2 - radius, null);

	}
	
	/** 
     * 获取裁剪后的圆形图片 
     *  
     * @param radius 
     *            半径 
     */  
    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {  
        Bitmap scaledSrcBmp;  
        int diameter = radius * 2;  
  
        // 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片  
        int bmpWidth = bmp.getWidth();  
        int bmpHeight = bmp.getHeight();  
        int squareWidth, squareHeight;
        int x, y;
        Bitmap squareBitmap;  
        if (bmpHeight > bmpWidth) {// 高大于宽  
            squareWidth = squareHeight = bmpWidth;  
            x = 0;  
            y = (bmpHeight - bmpWidth) / 2;  
            // 截取正方形图片  
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,  
                    squareHeight);  
        } else if (bmpHeight < bmpWidth) {// 宽大于高  
            squareWidth = squareHeight = bmpHeight;  
            x = (bmpWidth - bmpHeight) / 2;  
            y = 0;  
            squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,  
                    squareHeight);  
        } else {  
            squareBitmap = bmp;  
        }  
  
        //根据正方形边长放缩图片，赋值给scaledSrcBmp
        if (squareBitmap.getWidth() != diameter  
                || squareBitmap.getHeight() != diameter) {  
            scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,  
                    diameter, true);  
        } else {  
            scaledSrcBmp = squareBitmap;  
        }
        Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),  
                scaledSrcBmp.getHeight(), Config.ARGB_8888);  
        Canvas canvas = new Canvas(output);
  
        Paint paint = new Paint();  
        Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),  
                scaledSrcBmp.getHeight());  
  
        paint.setAntiAlias(true);  
        paint.setFilterBitmap(true);  
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);

        //剪裁出圆形区域
        Path path = new Path();
        path.addCircle(scaledSrcBmp.getWidth() / 2,
                scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2, Path.Direction.CCW);
        path.close();
        canvas.drawPath(path, paint);
//        canvas.clipPath(path, Region.Op.INTERSECT);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //画出图片
        canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        if(!TextUtils.isEmpty(mContent)) {          //画出说明文字和背景
            paint.setColor(mContentBackground);
            canvas.drawRect(0, scaledSrcBmp.getHeight() / 3 * 2, scaledSrcBmp.getWidth(), scaledSrcBmp.getHeight(), paint);
            //计算起始位置的x坐标
            paint.setTextSize(mContentSize);
            float lTextWidth = paint.measureText(mContent);
            //文字与背景之间的左边距
            float gap = (scaledSrcBmp.getWidth() - lTextWidth)/2;
            Paint.FontMetrics fm = paint.getFontMetrics();
            //基线的Y坐标
            float lBaseLineY = scaledSrcBmp.getHeight() / 6 - fm.descent + (fm.descent - fm.ascent) / 2 + scaledSrcBmp.getHeight() / 3 * 2;
            paint.setColor(mContentColor);
            canvas.drawText(mContent, gap, lBaseLineY, paint);
        }
        // bitmap回收(recycle导致在布局文件XML看不到效果)
        bmp.recycle();
        squareBitmap.recycle();
        scaledSrcBmp.recycle();
        return output;
    }  
	
    /** 
     * 边缘画圆 
     */  
    private void drawCircleBorder(Canvas canvas, int radius, int thickness, int color) {  
        Paint paint = new Paint();  
        /* 去锯齿 */  
        paint.setAntiAlias(true);  
        paint.setFilterBitmap(true);  
        paint.setDither(true);  
        paint.setColor(color);  
        /* 设置paint的　style　为STROKE：空心 */  
        paint.setStyle(Paint.Style.STROKE);  
        /* 设置paint的外框宽度 */  
        paint.setStrokeWidth(thickness);  
        canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius+thickness/2, paint);  
    }  
}
