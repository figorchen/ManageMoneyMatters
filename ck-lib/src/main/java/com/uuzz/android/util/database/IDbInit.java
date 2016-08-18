package com.uuzz.android.util.database;

import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

/**
 * 描 述：初始化数据库<br/>
 * 作 者：谌珂<br/>
 * 历 史: (版本) 谌珂 2016/2/17 注释 <br/>
 */
public interface IDbInit {
    void execSQL(SQLiteDatabase db) throws IOException;
}
