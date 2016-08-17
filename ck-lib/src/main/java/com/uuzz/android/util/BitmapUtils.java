package com.uuzz.android.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {
	private BitmapUtilListener listener;
	
	public static final int SAVE_BITMAP_IS_OK = 100;
	public static final int SAVE_BITMAP_IS_FAIL = 200;

	/**
	 * 描 述：获取缩放过的图片<br/>
	 * 作 者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/3/29 注释 <br/>
	 * @param path 图片物理路径
	 * @param context 上下文
	 * @return 已经根据屏幕缩放过的图片
	 * @throws IOException 读取文件失败抛出
	 */
	public static Bitmap getScaleBitmapByPath(String path, Context context) throws IOException {
		Bitmap bitmap;
		int w;
		InputStream is = new FileInputStream(path);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is,null,options);
		is.close();
		w = options.outWidth;
		int screenWidth = ScreenTools.getScreenPixel(context)[0];
		if(w > screenWidth){
			options.inSampleSize = w/screenWidth;
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(path, options);
		}else{
			bitmap = BitmapFactory.decodeFile(path);
		}
		return bitmap;
	}

	/**
	 * 描 述：获取缩放过的图片<br/>
	 * 作 者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/3/29 注释 <br/>
	 * @param uri 图片uri
	 * @param context 上下文
	 * @return 已经根据屏幕缩放过的图片
	 * @throws IOException 读取文件失败抛出
	 */
	public static Bitmap getScaleBitmapByUri(Uri uri, Context context) throws FileNotFoundException{
		Bitmap bitmap;
		int w;
		InputStream is = context.getContentResolver().openInputStream(uri);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(is, null, options);
		w = options.outWidth;
		int screenWidth = ScreenTools.getScreenPixel(context)[0];
		if(w > screenWidth){
			options.inSampleSize = w/screenWidth;
			options.inJustDecodeBounds = false;
			String path = getPicPathByUri(uri, context);
			bitmap = BitmapFactory.decodeFile(path, options);
		}else{
			bitmap = BitmapFactory.decodeStream(is);
		}
		return bitmap;
	}

	/**
	 * 描 述：根据Bitmap对象缩放图片<br/>
	 * 作 者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/3/29 注释 <br/>
	 * @param source 原图片
	 * @param width 图片宽度
	 * @param height 图片高度
	 * @return 生成缩放后的新图片
	 */
	public static Bitmap getScaleBitmap(Bitmap source, int width, int height) {
		return Bitmap.createScaledBitmap(source, width, height, false);
	}

	private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		String column = MediaStore.Images.Media.DATA;
		String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	private static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is Google Photos.
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	/**
	 * 描 述：URI转路径<br/>
	 * 作者：谌珂<br/>
	 * 历 史: (版本) 谌珂 2016/1/20 注释 <br/>
	 */
	public static String getPicPathByUri(Uri imageUri,Context context){
		if (context == null || imageUri == null)
			return null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
			if (isExternalStorageDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}
			} else if (isDownloadsDocument(imageUri)) {
				String id = DocumentsContract.getDocumentId(imageUri);
				Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			} else if (isMediaDocument(imageUri)) {
				String docId = DocumentsContract.getDocumentId(imageUri);
				String[] split = docId.split(":");
				String type = split[0];
				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				String selection = MediaStore.Images.Media._ID + "=?";
				String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		} // MediaStore (and general)
		else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(imageUri))
				return imageUri.getLastPathSegment();
			return getDataColumn(context, imageUri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
			return imageUri.getPath();
		}
		return null;
	}


	/**
	 * 保存图片至对应文件路径
	 * @param path 文件路径
	 * @param bitmap 要保存的bitmap
	 * @param format 保存格式 CompressFormat.JPEG、CompressFormat.PNG、CompressFormat.WEBP
	 * @param quality 图片品质，范围从0~100,0为最低品质
	 * @throws IOException
	 */
	public static void saveBitmap(String path, Bitmap bitmap, CompressFormat format, int quality) throws IOException{
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		FileOutputStream mFileOutputStream = new FileOutputStream(file);
		bitmap.compress(format, quality, mFileOutputStream);
		mFileOutputStream.flush();
		mFileOutputStream.close();
	}
	
	public void saveBitmapInBackground(String path, Bitmap bitmap, CompressFormat format, int quality, BitmapUtilListener listener){
		this.listener = listener;
		SaveBitmapAsyncTask asyncTask = new SaveBitmapAsyncTask();
		asyncTask.execute(path, bitmap, format, quality);
	}
	
	private class SaveBitmapAsyncTask extends AsyncTask<Object, Void, Integer>{

		@Override
		protected Integer doInBackground(Object... params) {
			try {
				saveBitmap((String)params[0], (Bitmap)params[1], (CompressFormat)params[2], (Integer)params[3]);
			} catch (IOException e) {
				e.printStackTrace();
				return SAVE_BITMAP_IS_FAIL;
			}
			return SAVE_BITMAP_IS_OK;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			listener.onComplete(result);
		}
		
	};
	
	public interface BitmapUtilListener{
		void onComplete(Integer resultCode);
	}
}
