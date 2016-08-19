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
import com.uuzz.android.util.database.dao.AbstractDAO;

import java.util.ArrayList;
import java.util.List;

public class MoneyAccountDao extends AbstractDAO<MoneyAccount> {

    public static final int DISPOSABLE_ACCOUNT_FROM_USERID = 0;

    /** 用于监听者发送通知时的标记，全部用户 */
    public static final int ALL_USER_INFO = 0;
    /** 用于监听者发送通知时的标记，可支付用户 */
    public static final int PAYABLE_USER_INFO = 1;

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
                String sql = "select * from m_money_account, m_money_account_type, c_card_info where m_disposable = ? and m_card_id = c_card_id and m_money_account.m_mime_type = m_money_account_type.m_money_account_id and m_user_id = ?";
                Cursor cursor = db.rawQuery(sql, new String[]{"1", String.valueOf(loginUser.getId())});
                List<MoneyAccount> result = new ArrayList<>();
                while(cursor.moveToNext()){
                    result.add(BeanUtils.getBean(getBeanClass(), cursor, getTableName()));
                }
                cursor.close();

                Message msg = Message.obtain();
                msg.what = DISPOSABLE_ACCOUNT_FROM_USERID;
                msg.obj = result;
                setChanged();
                notifyObservers(msg);
            }
        });
    }


}
