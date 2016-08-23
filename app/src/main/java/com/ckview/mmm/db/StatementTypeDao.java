package com.ckview.mmm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Message;

import com.ckview.mmm.entity.db.StatementType;
import com.ckview.mmm.entity.db.Statements;
import com.uuzz.android.util.database.DataBasesUtil;
import com.uuzz.android.util.database.dao.AbstractDAO;

import java.util.List;


public class StatementTypeDao extends AbstractDAO<StatementType> {

    /** 单例 */
    private volatile static StatementTypeDao mInstance;

    public static StatementTypeDao getInstance(Context context) {
        if(mInstance == null) {
            synchronized (MoneyAccountDao.class) {
                if(mInstance == null) {
                    mInstance = new StatementTypeDao(DataBasesUtil.getDb(context));
                }
            }
        }
        return mInstance;
    }

    private StatementTypeDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public Class<StatementType> getBeanClass() {
        return StatementType.class;
    }

    @Override
    public String getTableName() {
        return "s_statements_type";
    }

    /**
     * 描 述：根据流水数据查询支出或收入类型<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/22 <br/>
     * @param data 流水数据
     */
    public void getIntentAsync(final Statements data) {
        AsyncTask.SERIAL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                List<StatementType> select = select(null, "s_type_id " + (data.getmStatementsType() == Statements.OUTCOME ? "<" : ">") + " ? and s_leaf = ?", new String[]{String.valueOf(Statements.OUTCOME), "1"}, null, null, "s_parent_id asc", null);
                Message msg = Message.obtain();
                msg.what = Common.GET_INTENT_FROM_STATEMENT;
                msg.obj = select;
                setChanged();
                notifyObservers(msg);
            }
        });
    }
}
