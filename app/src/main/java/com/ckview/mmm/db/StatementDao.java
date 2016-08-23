package com.ckview.mmm.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Message;

import com.ckview.mmm.entity.db.Statements;
import com.uuzz.android.util.database.BeanUtils;
import com.uuzz.android.util.database.DataBasesUtil;
import com.uuzz.android.util.database.dao.AbstractDAO;

import java.util.ArrayList;
import java.util.List;


public class StatementDao extends AbstractDAO<Statements> {

    /** 单例 */
    private volatile static StatementDao mInstance;

    public static StatementDao getInstance(Context context) {
        if(mInstance == null) {
            synchronized (MoneyAccountDao.class) {
                if(mInstance == null) {
                    mInstance = new StatementDao(DataBasesUtil.getDb(context));
                }
            }
        }
        return mInstance;
    }

    private StatementDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public Class<Statements> getBeanClass() {
        return Statements.class;
    }

    @Override
    public String getTableName() {
        return "s_statements";
    }

    /**
     * 描 述：获取借款收入的流水<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/23 <br/>
     */
    public void getIncomeBorrowStatements() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                String sql = "select * from s_statements left join s_statements_type on s_type_id = s_mime_type where s_mime_type = ? order by s_statements_id desc";
                Cursor cursor = db.rawQuery(sql, new String[]{"2003"});
                List<Statements> result = new ArrayList<>();
                while(cursor.moveToNext()){
                    result.add(BeanUtils.getBean(getBeanClass(), cursor, getTableName()));
                }
                cursor.close();
                Message msg = Message.obtain();
                msg.what = Common.INCOME_BORROW_STATEMENTS;
                msg.obj = result;
                setChanged();
                notifyObservers(msg);
            }
        });
    }
}
