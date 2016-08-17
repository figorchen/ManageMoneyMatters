package com.uuzz.android.util.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.uuzz.android.util.log.UUZZLog;

import java.util.ArrayList;

/**
 * @author Chunk E-mail:348353610@qq.com
 * @version Created time：2015年8月17日 下午1:08:14
 *
 */
public class DbHelper extends SQLiteOpenHelper{

	UUZZLog logger = new UUZZLog("DbHelper");

	/** 初始化数据库接口 */
	IDbInit mIDbInit;

	public DbHelper(Context context, String name, CursorFactory factory, int version, IDbInit mIDbInit) {
		super(context, name, factory, version);
		this.mIDbInit = mIDbInit;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		ArrayList<String> sqls = ParseTableXML.getCreateTableSql();
		try{
			db.beginTransaction();
			//创建所有表
			for (String sql : sqls) {
				db.execSQL(sql);
			}
			if(mIDbInit != null) {
				mIDbInit.execSQL(db);
			}
			db.setTransactionSuccessful();
		}catch (Exception e){
			logger.i("initialize database error!", e);
		}finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

//	/**
//	 * 描 述：异步执行创建表工作<br/>
//	 * 作 者：谌珂<br/>
//	 * 历 史: (版本) 谌珂 2016/2/17 注释 <br/>
//	 */
//	private class DbAsyncTask extends AsyncTask<SQLiteDatabase, Void, Void>{
//		@Override
//		protected Void doInBackground(SQLiteDatabase... params) {
//			SQLiteDatabase db = params[0];
//			ArrayList<String> sqls = ParseTableXML.getCreateTableSql();
//			try{
//				db.beginTransaction();
//				//创建所有表
//				for (String sql : sqls) {
//					db.execSQL(sql);
//				}
//				if(mIDbInit != null) {
//					mIDbInit.execSQL(db);
//				}
//				db.setTransactionSuccessful();
//			}catch (Exception e){
//				logger.e("initialize database error!", e);
//			}finally {
//				db.endTransaction();
//			}
//			return null;
//		}
//	};

}
