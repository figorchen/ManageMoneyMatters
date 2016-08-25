package com.ckview.mmm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Message;

import com.ckview.mmm.entity.db.UserInfo;
import com.uuzz.android.util.database.DataBasesUtil;
import com.uuzz.android.util.database.dao.AbstractDAO;

import java.util.List;

public class UserInfoDao extends AbstractDAO<UserInfo> {

    /** 单例 */
    private volatile static UserInfoDao mInstance;

    public static UserInfoDao getInstance(Context context) {
        if(mInstance == null) {
            synchronized (UserInfoDao.class) {
                if(mInstance == null) {
                    mInstance = new UserInfoDao(DataBasesUtil.getDb(context));
                }
            }
        }
        return mInstance;
    }

    private UserInfoDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public Class<UserInfo> getBeanClass() {
        return UserInfo.class;
    }

    @Override
    public String getTableName() {
        return "u_user_account";
    }

    /**
     * 描 述：异步获取可支付用户<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/19 <br/>
     */
    public void getPayableUserAsync() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = Common.PAYABLE_USER_INFO;
                msg.obj = select(null, "u_payer = ?", new String[]{"1"}, null, null, null, null);
                setChanged();
                notifyObservers(msg);
            }
        });
    }

    /**
     * 描 述：异步获取所有用户<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/19 <br/>
     */
    public void getAllUserAsync() {
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = Common.ALL_USER_INFO;
                msg.obj = selectAll(null);
                setChanged();
                notifyObservers(msg);
            }
        });
    }

    /**
     * 描 述：获取已登录的User<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/19 <br/>
     * @return 返回user对象
     */
    public UserInfo getLoginUser() {
        List<UserInfo> select = select(null, "u_login = ?", new String[]{"1"}, null, null, null, null);
        if(select.size() != 1) {
            return null;
        }
        return select.get(0);
    }

    /**
     * 描 述：改变登录用户<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/25 <br/>
     */
    public void changeUser(int userId) {
        try {
            db.beginTransaction();
            db.execSQL("update " + getTableName() + " set u_login = 0");
            db.execSQL("update " + getTableName() + " set u_login = 1 where u_user_id = " + userId);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            logger.e("change login user failed", e);
        }finally {
            db.endTransaction();
        }

    }

}
