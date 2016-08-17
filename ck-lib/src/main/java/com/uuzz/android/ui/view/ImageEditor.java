package com.uuzz.android.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.uuzz.android.util.log.UUZZLog;

/**
 * 用来编辑图片大小、位置的控件，与AccountImage一起使用，为其提供符合用户标准的头像文件，不支持xml配置属性
 * @author Chunk E-mail:348353610@qq.com
 * @version Created time：2015-7-17 下午3:35:49
 *
 */
public class ImageEditor extends View {

	UUZZLog logger = new UUZZLog("ImageEditor");
	
	private final int MOVE_SELECTION = 0;
	private final int CHANGE_SELECTION = 1;
	private final int MOVE_BITMAP = 2;

	//图片矩阵
	Matrix mMatrix = new Matrix();
	// 需要编辑的原图片
	private Bitmap source;
	// 编辑后的图片
	private Bitmap bitmap = null;
	// 记录图片是否是横向填充
	private boolean isHorizontal;
	//判断是否是ontouch事件
	private boolean isTouchEvent;
	//画笔
	Paint paint = new Paint();
	Path path = new Path();
	PathEffect effects = new DashPathEffect(new float[]{5,5,5,5},1);
	//绘制图片的起始坐标
	private int bitmapX;
	private int bitmapY;
	//单手指按下时坐标
	private float downX;
	private float downY;
	//两手指之间的距离
	private float oldDistance;
	private float newDistance;
	//两手指的中点坐标
	private PointF midPoint;
	//选区代表的矩形区域
	Rect selection = new Rect();
	//控件宽高
	private int viewWidth;
	private int viewHeight;
	//单根手指按下时的动作标记，与MOVE_SELECTION、CHANGE_SELECTION、MOVE_BITMAP联合使用
	private int moveAction;
	//选区半径（不包含线宽引起的误差）
	private int radius;
	//缩放选区时手指与圆心的距离
	private double oldRadius;
	private double newRadius;
	private boolean isCut;
	//初始化图片时的缩放比例
	private float mScale = 1;
	/** 标记是否是抬起了一根手指 */
	private boolean isPointerUp = false;

	public void setSource(Bitmap source){
		this.source = source;
	}
	
	public ImageEditor(Context context) {
		super(context);
		isTouchEvent = false;
		setDrawingCacheEnabled(true);
	}

	public ImageEditor(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		isTouchEvent = false;
		setDrawingCacheEnabled(true);
	}

	public ImageEditor(Context context, AttributeSet attrs) {
		super(context, attrs);
		isTouchEvent = false;
		setDrawingCacheEnabled(true);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		paint.setAntiAlias(true);
		viewWidth = getWidth();
		viewHeight = getHeight();
		
		//测试截取图片
		if(isCut){
			canvas.drawBitmap(bitmap, 0, 0, null);
			return;
		}
		
		// 获取包含本控件的activity
		Context context = getContext();
		Activity activity = null;
		if(context instanceof Activity){
			activity = (Activity) context;
		}
		
		// 源文件为null时直接退出activity
		if(this.source == null){
			logger.i("bitmap source is null,exit activity!");
			if(activity != null) {
				activity.finish();
				return;
			}
		}

		//1.确定选区
		//确定初始选区位置，由于需要正方形选区，长度由短边决定
		int diameter;
		if(!isTouchEvent){
			if(viewWidth >= viewHeight){
				diameter = viewHeight - 200;
				selection.set((viewWidth-diameter)/2, 100, (viewWidth+diameter)/2, viewHeight - 100);
			}else{
				diameter = viewWidth - 200;
				selection.set(100, (viewHeight-diameter)/2, viewWidth - 100, (viewHeight+diameter)/2);
			}
		}else{
			diameter = selection.right - selection.left;
		}
		radius = diameter/2;
		logger.d("selection area diameter is"+String.valueOf(diameter));
		logger.d("selection area:"+selection.toString());

		//2.根据图片与屏幕的宽高缩放图片不超过选区
		if(source.getWidth() >= source.getHeight() && source.getHeight() <= diameter) {
			mScale = (float)diameter/source.getHeight();
			isHorizontal = true;
		} else if(source.getWidth() <= source.getHeight() && source.getWidth() <= diameter) {
			mScale = (float)diameter/source.getWidth();
			isHorizontal = false;
		}
//		if((float)source.getWidth()/source.getHeight() >= (float)viewWidth/viewHeight){
//			mScale = (float)viewWidth/source.getWidth();
//			isHorizontal = true;
//		}else{
//			mScale = (float)viewHeight/source.getHeight();
//			isHorizontal = false;
//		}
		logger.d("mScale="+String.valueOf(mScale));
		if(!isTouchEvent) {
			bitmapX = 0;
			bitmapY = 0;
//			if(isHorizontal) {
				bitmapY = (int) ((canvas.getHeight() - source.getHeight()* mScale)/2);
//			} else {
				bitmapX = (int) ((canvas.getWidth() - source.getWidth()* mScale)/2);
//			}
			//重置矩阵
			mMatrix.reset();
			//设置图片位置
			mMatrix.preTranslate(bitmapX, bitmapY);
			//设置图片大小，且为队列首位
			mMatrix.preScale(mScale, mScale);
		}
		//绘制图片
		canvas.drawBitmap(source, mMatrix, paint);
		//绘制灰色蒙版
		canvas.drawARGB(190, 0, 0, 0);
		

		canvas.save();
		path.reset();
		path.addCircle(selection.left+radius, selection.top+radius, radius, Path.Direction.CW);
		path.close();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(1);
		canvas.drawPath(path, paint);
		canvas.clipPath(path, Region.Op.INTERSECT);
		canvas.drawBitmap(source, mMatrix, null);
		canvas.restore();

//		//设置选区颜色，默认为白色,6px
//		paint.setColor(Color.WHITE);
//		paint.setStrokeWidth(0);
//		paint.setStyle(Paint.Style.STROKE);
//		//绘制圆形选区,由于选区边框width影响，半径-3
//		canvas.drawCircle(selection.left+radius, selection.top+radius, radius, paint);
//		//绘制网格
//		//绘制圆形选区的外切正方形，边为虚线
//		paint.setPathEffect(effects);
//		path.reset();
//		path.addRect(selection.left, selection.top, selection.right, selection.bottom, Path.Direction.CW);
//		path.close();
//		canvas.drawPath(path, paint);
//
//		//构建选区路径
//		path.reset();
//		//半径-6
//		path.addCircle(selection.left+radius, selection.top+radius, radius-2, Path.Direction.CW);
//		path.close();
//		canvas.save();
//		//剪裁选区
//		//剪裁圆形选区
//		canvas.clipPath(path);
//		//绘制正常颜色图片
//		paint.setPathEffect(null);
//		//绘制网格线
////		paint.setStyle(Style.FILL);
////		paint.setColor(Color.argb(130, 255, 255, 255));
////		paint.setStrokeWidth(1);
//		canvas.drawBitmap(source, mMatrix, null);
////		canvas.drawLine(selection.left+diameter/3, 0, selection.left+diameter/3, selection.bottom, paint);
////		canvas.drawLine(selection.left+diameter*2/3, 0, selection.left+diameter*2/3, selection.bottom, paint);
////		canvas.drawLine(0, selection.top+diameter/3, selection.right, selection.top+diameter/3, paint);
////		canvas.drawLine(0, selection.top+diameter*2/3, selection.right, selection.top+diameter*2/3, paint);
//		canvas.restore();
		
		//绘制完成后恢复标记
		isTouchEvent = false;
	}
	
	/**
	 * 旋转图片
	 * @param degrees 旋转角度
	 */
	public void rotate(float degrees){
		Matrix matrix = new Matrix();
		matrix.setRotate(degrees);
		source = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
		invalidate();
	}

	/**
	 * 描 述：缩放图片<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/1/27 注释 <br/>
	 * @param sx x缩放倍数
	 * @param sy y缩放倍数
	 * @param px 缩放中心点x坐标
	 * @param py 缩放中心点y坐标
	 */
	private void scaleBitmap(float sx, float sy, float px, float py){
		logger.d("sx="+String.valueOf(sx)+",sy="+String.valueOf(sy)+",px="+String.valueOf(px)+",py="+String.valueOf(py));
		//临时保存矩阵
		float[] matrixValues = new float[9];
		mMatrix.getValues(matrixValues);

		mMatrix.postScale(sx, sy, px, py);
		//缩放后如果图片边界已经超出选区则还原矩阵
		if(sx*matrixValues[Matrix.MSCALE_X] > mScale*2) {
			mMatrix.setValues(matrixValues);
			return;
		}
		invalidate();
	}


	/**
	 * 描 述：平移图片<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/1/27 注释 <br/>
	 */
	private void translateBitmap(float dx, float dy){
		logger.d("translateBitmap:dx=" + String.valueOf(dx) + ",dy=" + String.valueOf(dy));
		mMatrix.postTranslate(dx, dy);
		invalidate();
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int pointCount = event.getPointerCount();
		if(pointCount == 1){
			//一根手指按下,处理移动图片、选框或选框边角
			switch (action) {
				case MotionEvent.ACTION_DOWN:
					downX = event.getX();
					downY = event.getY();
					//计算一次原始距离
					oldRadius = getDiatance((long) (downX-selection.left-radius), (long) (downY-selection.top-radius));
					//根据触点坐标确认动作
					confirmAction((long) (downX-selection.left-radius), (long) (downY-selection.top-radius));
					break;
				case MotionEvent.ACTION_MOVE:
					float dX = event.getX()-downX;
					float dY = event.getY()-downY;
					switch (moveAction) {
						case MOVE_SELECTION:
							//移动选区
							logger.d("move selection");
							moveSelection(dX, dY);
							break;
						case CHANGE_SELECTION:
							//改变选区大小
							logger.d("change selection");
							newRadius = getDiatance((long) (event.getX()-selection.left-radius), (long) (event.getY()-selection.top-radius));
							logger.d("old radius is "+oldRadius);
							logger.d("new radius is "+newRadius);
							scaleSelecition(0.71*(newRadius-oldRadius));
							oldRadius = newRadius;
							break;
						case MOVE_BITMAP:
							if(isPointerUp) {
								downX = event.getX();
								downY = event.getY();
								dX = event.getX()-downX;
								dY = event.getY()-downY;
								isPointerUp = false;
							}
							//移动图片
							logger.d("move bitmap");
							isTouchEvent = true;


							//判断图片移动是否会超出选区的范围，超出则不移动
	//						float[] result = isSelectionOut(dX, dY);
	//						translateBitmap(result[0], result[1]);

							//移动图片
							translateBitmap(dX, dY);
							//////////////////////


		//					if(Math.abs(dX) > 3 || Math.abs(dY) > 3){        //控制移动图片的精度
		//						translateBitmap(dX, dY);
		//					}
							break;
						default:
							break;
					}
					downX = event.getX();
					downY = event.getY();
					break;
				case MotionEvent.ACTION_UP:
					adjustBitmap();
					break;
				default:
					break;
			}
		}else if(pointCount == 2){
			//两根手指按下，处理缩放图片
			switch (action&MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_POINTER_DOWN:
					//保存两点之间距离
					oldDistance = getDistance(event);
					midPoint = getMidPoint(event);
					break;
				case MotionEvent.ACTION_POINTER_UP:
					//通知单手指移动时重新记录按下坐标
					isPointerUp = true;
					//自适应一遍选区
					adjustBitmap();
					break;
				case MotionEvent.ACTION_MOVE:
					//计算新距离
					isTouchEvent = true;
					newDistance = getDistance(event);
					float scale = newDistance/oldDistance;
					oldDistance = newDistance;
					scaleBitmap(scale, scale, midPoint.x, midPoint.y);

					break;
				default:
					break;
			}
		}
		
		return true;
	}

	/**
	 * 描 述：调整图片适应选区位置和大小<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/1/27 注释 <br/>
	 */
	private void adjustBitmap() {
		float[] matrixValues = new float[9];
		mMatrix.getValues(matrixValues);
		float scaleX = matrixValues[Matrix.MSCALE_X];
		float scaleY = matrixValues[Matrix.MSCALE_Y];

		//图片缩放后实际的边界点
		float left = matrixValues[Matrix.MTRANS_X];
		float top = matrixValues[Matrix.MTRANS_Y];
		float width = source.getWidth() * scaleX;
		float height = source.getHeight() * scaleY;
		float right = left + width;
		float bottom = top + height;

		//计算图片需要的scale，拿到宽、高和现在的倍数的最大值
		float scale = 1;
		if(width < selection.width()) {
			float tempScale = (float)selection.width()/width;
			scale = scale > tempScale ? scale : tempScale;
		}
		if(height < selection.height()) {
			float tempScale = (float)selection.height()/height;
			scale = scale > tempScale ? scale : tempScale;
		}
		isTouchEvent = true;
		//经过最终缩放
		if(scale > 1) {
			scaleBitmap(scale, scale, left + width/2, top + height/2);
		}

		float offSetX = 0;
		float offSetY = 0;
		//计算修复平移距离
		if(left > selection.left && right > selection.right) {  //平移至左边界
			offSetX = selection.left - left;
		} else if(left < selection.left && right < selection.right) { //平移至右边界
			offSetX = selection.right - right;
		}

		if(top > selection.top && bottom > selection.bottom) {  //平移至上边界
			offSetY = selection.top - top;
		} else if(top < selection.top && bottom < selection.bottom) {
			offSetY = selection.bottom - bottom;
		}

		translateBitmap(offSetX, offSetY);
	}

	/**
	 * 描 述：判断是否会缩放出选区<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/1/18 注释 <br/>
     * @return 如果会超出选区返回true
	 * @deprecated
     */
	private boolean isSelectionOut() {
		float[] matrixValues = new float[9];
		mMatrix.getValues(matrixValues);
		float scaleX = matrixValues[Matrix.MSCALE_X];
		float scaleY = matrixValues[Matrix.MSCALE_Y];

		//图片缩放后实际的边界点
		float left = matrixValues[Matrix.MTRANS_X];
		float top = matrixValues[Matrix.MTRANS_Y];
		float right = left + source.getWidth() * scaleX;
		float bottom = top + source.getHeight() * scaleY;

		return (left > selection.left || right < selection.right || top > selection.top || bottom < selection.bottom);
	}

	/**
	 * 描 述：判断是否会移动出选区<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/1/18 注释 <br/>
	 * @param dX 当前x偏移量
	 * @param dY 当前y偏移量
	 * @return 返回允许移动的实际值数组
	 * @deprecated
	 */
	private float[] isSelectionOut(float dX, float dY) {
		float[] matrixValues = new float[9];
		mMatrix.getValues(matrixValues);
		float scaleX = matrixValues[Matrix.MSCALE_X];
		float scaleY = matrixValues[Matrix.MSCALE_Y];
		float translationX = matrixValues[Matrix.MTRANS_X];
		float translationY = matrixValues[Matrix.MTRANS_Y];

		//图片移动后实际的边界点
		float left = translationX + dX;
		float top = translationY + dY;
//		float left = translationX * scaleX + bitmapX + dX;
//		float top = translationY * scaleY + bitmapY + dY;
		float right = left + source.getWidth() * scaleX;
		float bottom = top + source.getHeight() * scaleY;

		float[] result = new float[]{dX, dY};
		if(left > selection.left || right < selection.right) {
			result[0] = 0;
		}
		if(top > selection.top || bottom < selection.bottom) {
			result[1] = 0;
		}

		return result;
	}

	/**
	 * 移动选区
	 * @param dX x偏移量
	 * @param dY y偏移量
	 */
	private void moveSelection(float dX, float dY) {
		if(selection.left+dX >= 0 && selection.top+dY >= 0 && selection.right+dX <= viewWidth && selection.bottom+dY <= viewHeight){
			selection.left = (int) (selection.left+dX);
			selection.top = (int) (selection.top+dY);
			selection.right = (int) (selection.right+dX);
			selection.bottom = (int) (selection.bottom+dY);
			isTouchEvent = true;
			invalidate();
		}
	}

	/**
	 * 根据变化长度缩小选区
	 * @param d 缩放倍数
	 */
	private void scaleSelecition(double d) {
		if(selection.right - selection.left > Math.abs(2d) && selection.bottom - selection.top > Math.abs(2d)){
			selection.left = (int) ((selection.left-d >= 0) ? selection.left-d : 0);
			selection.top = (int) ((selection.top-d >= 0) ? selection.top-d : 0);
			selection.bottom = (int) ((selection.bottom+d <= viewHeight) ? selection.bottom+d : viewHeight);
			selection.right = (int) ((selection.right+d <= viewWidth) ? selection.right+d : viewWidth);
			isTouchEvent = true;
			logger.d("selection is "+selection.toString());
			invalidate();
		}
	}

	/**
	 * 根据触点坐标确认动作,现默认只返回移动图片
	 * @deprecated
	 */
	private void confirmAction(long offsetX, long offsetY) {

		/*
		* 修改为选区不能移动和缩放，所以moveAction === MOVE_BITMAP
		* */
//		//计算触点与圆心的距离
//		long sqrtLength = offsetX*offsetX+offsetY*offsetY;
//		//判断是否在选区范围内
//		if((long)(radius+20)*(radius+20) >= sqrtLength){
//			if(sqrtLength >= (long)(radius-20)*(radius-20)){
//				this.moveAction = CHANGE_SELECTION;
//			}else{
//				this.moveAction = MOVE_SELECTION;
//			}
//			return;
//		}
		
		this.moveAction = MOVE_BITMAP;
	}

	/**
	 * 计算触点到圆心的距离
	 * @param offsetX 相对于圆心的x坐标
	 * @param offsetY 相对于圆心的y坐标
	 * @return 返回点到圆心的距离
	 */
	private double getDiatance(long offsetX, long offsetY) {
		return Math.sqrt(offsetX*offsetX+offsetY*offsetY);
	}

	/**
	 * 计算两根手指之间距离
	 * @param event 消息事件
	 * @return 返回两根手指之间的距离
	 */
	@SuppressLint("FloatMath")
	private float getDistance(MotionEvent event) {
		//第一个点
		float x = event.getX();
		float y = event.getY();
		
		//第二个点
		float x2 = event.getX(1);
		float y2 = event.getY(1);
		float result = (float) Math.sqrt((x-x2)*(x-x2)+(y-y2)*(y-y2));
		logger.d("two pointer distance is "+result);
		return result;
	}
	
	/**
	 * 计算两手指中点
	 * @param event 消息事件
	 * @return 返回两根手指之间中点坐标
	 */
	private PointF getMidPoint(MotionEvent event) {
		PointF point = new PointF();
    	point.x = (event.getX()+event.getX(1))/2;
    	point.y = (event.getY()+event.getY(1))/2;
		logger.d("midPoint:x="+point.x+",y="+point.y);
    	return point;
	}
	
	/**
	 * 获取截取后的图片
	 * @param isScale 为true时返回的图片像素与选区像素一致，为false时将返回原图片中截取部分，为保证图片不失真尽量使用false
	 * @return 返回剪裁后的图片
	 */
	public Bitmap clipBitmap(boolean isScale){
		Bitmap result = Bitmap.createBitmap(selection.width(), selection.height(), Config.ARGB_8888);
		Canvas canvas = new Canvas(result);
		mMatrix.postTranslate(-selection.left, -selection.top);
		canvas.drawBitmap(source, mMatrix, null);
		
		if(!isScale){
			//把图片缩放到原来大小
			float[] matrixValues = new float[9];
			mMatrix.getValues(matrixValues);
			float scaleX = matrixValues[Matrix.MSCALE_X];
			float scaleY = matrixValues[Matrix.MSCALE_Y];
			bitmap = Bitmap.createBitmap((int)(selection.width()/scaleX), (int)(selection.height()/scaleY), Config.ARGB_8888);
			
			Canvas scaleCanvas = new Canvas(bitmap);
			mMatrix.reset();
			mMatrix.setScale(1/scaleX, 1/scaleY);
			scaleCanvas.drawBitmap(result, mMatrix, null);
			result.recycle();
		}else{
			bitmap = result;
		}
//		isCut = true;
//		invalidate();
		return bitmap;
	}

	/**
	 * 描 述：释放bitmap对象<br/>
	 * 作 者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/3/16 注释 <br/>
	 */
	public void recycleBitmap() {
		source.recycle();
	}
}
