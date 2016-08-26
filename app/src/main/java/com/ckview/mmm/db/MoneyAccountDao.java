package com.ckview.mmm.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Message;

import com.ckview.mmm.entity.db.MoneyAccount;
import com.ckview.mmm.entity.db.UserInfo;
import com.uuzz.android.util.database.BeanUtils;
import com.uuzz.android.util.database.DataBasesUtil;
import com.uuzz.android.util.database.OpResult;
import com.uuzz.android.util.database.dao.AbstractDAO;

import java.util.ArrayList;
import java.util.List;

public class MoneyAccountDao extends AbstractDAO<MoneyAccount> {

    /** 单例 */
    private volatile static MoneyAccountDao mInstance;

    public static MoneyAccountDao getInstance(Context context) {
        if(mInstance == null) {
            synchronized (MoneyAccountDao.class) {
                if(mInstance == null) {
                    mInstance = new MoneyAccountDao(DataBasesUtil.getDb(context));
                }
            }
        }
        return mInstance;
    }

    private MoneyAccountDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public Class<MoneyAccount> getBeanClass() {
        return MoneyAccount.class;
    }

    @Override
    public String getTableName() {
        return "m_money_account";
    }

    /**
     * 描 述：获取与用户有关联的可支配资金账户<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/19 <br/>
     */
    public void getDisposableAccountFromUserId() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                UserInfo loginUser = UserInfoDao.getInstance(null).getLoginUser();
                if(loginUser == null) {
                    return;
                }
                String sql = "select * from (select * from m_money_account left join m_money_account_type on m_money_account.m_mime_type = m_money_account_type.m_money_account_id) left join c_card_info on m_card_id = c_card_id where m_disposable = ? and m_user_id = ? order by m_use_count desc";
                Cursor cursor = db.rawQuery(sql, new String[]{"1", String.valueOf(loginUser.getId())});
                List<MoneyAccount> result = new ArrayList<>();
                while(cursor.moveToNext()){
                    result.add(BeanUtils.getBean(getBeanClass(), cursor, getTableName()));
                }
                cursor.close();

                Message msg = Message.obtain();
                msg.what = Common.DISPOSABLE_ACCOUNT;
                msg.obj = result;
                setChanged();
                notifyObservers(msg);
            }
        });
    }

    /**
     * 描 述：获取与用户有关联的资金账户<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/19 <br/>
     */
    public void getAllAccountFromUserId() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                UserInfo loginUser = UserInfoDao.getInstance(null).getLoginUser();
                if(loginUser == null) {
                    return;
                }
                String sql = "select * from (select * from m_money_account left join m_money_account_type on m_money_account.m_mime_type = m_money_account_type.m_money_account_id) left join c_card_info on m_card_id = c_card_id where m_user_id = ? order by m_use_count desc";
                Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(loginUser.getId())});
                List<MoneyAccount> result = new ArrayList<>();
                while(cursor.moveToNext()){
                    result.add(BeanUtils.getBean(getBeanClass(), cursor, getTableName()));
                }
                cursor.close();

                Message msg = Message.obtain();
                msg.what = Common.MONEY_ACCOUNT;
                msg.obj = result;
                setChanged();
                notifyObservers(msg);
            }
        });
    }

    /**
     * 描 述：修改资金账户余额和活跃度<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/24 <br/>
     */
    public void updateMoneyAccount(int accountId, double money) {
        db.execSQL("update " + getTableName()
                + " SET m_balance = m_balance + " + String.valueOf(money)
                + ", m_use_count = m_use_count + 1"
                + " where m_money_account_id = "+ String.valueOf(accountId));
    }

    /**
     * 描 述：修改资金账户余额和活跃度<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/24 <br/>
     */
    public boolean updateMoneyAccount(MoneyAccount moneyAccount) {
        OpResult<Object> update = update(moneyAccount, "m_money_account_id = ?", new String[]{String.valueOf(moneyAccount.getId())}, false);
        return update.isSuccess();
    }


}
