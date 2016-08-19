/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: DataBasesUtil.java <br/>
 * <p>
 * Created by 谌珂 on 2016/7/27.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.util.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.uuzz.android.util.Common;
import com.uuzz.android.util.FileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * 项目名称：手机大管家 <br/>
 * 类  名: DataBasesUtil <br/>
 * 类描述: <br/>
 * 实现的主要功能 <br/>
 * 版    本：1.2.2 <br/>
 * 修改时间：2016/7/27 <br/>
 * @author 谌珂 <br/>
 */
public class DataBasesUtil {
    private static SQLiteDatabase db;

    /**
     * 描 述：获取数据库<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/6/28 注释 <br/>
     */
    public static SQLiteDatabase getDb(Context context) {
        if(db == null) {
            ParseTableXML.praseXML(context, Common.DB_FOLDER);
            DbHelper helper = new DbHelper(context, Common.DB_NAME, null, 2, new InitDatabases(context));
            db = helper.getWritableDatabase();
        }
        return db;
    }

    private static class InitDatabases implements IDbInit {
        InitDatabases(Context context) {
            this.context = context;
        }

        private Context context;

        @Override
        public void execSQL(SQLiteDatabase db) {
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            try {
                inputStream = context.getAssets().open(Common.DB_FOLDER + "/" + Common.DB_INIT_SQL);

                inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String lineTxt;
                while((lineTxt = reader.readLine()) != null){
                    db.execSQL(lineTxt);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                FileUtil.closeReader(inputStreamReader);
                FileUtil.closeInputStream(inputStream);
            }
        }
    }
}
