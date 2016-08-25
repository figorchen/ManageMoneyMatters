package com.ckview.mmm.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ckview.mmm.R;
import com.ckview.mmm.db.Common;
import com.ckview.mmm.db.MoneyAccountDao;
import com.ckview.mmm.db.UserInfoDao;
import com.ckview.mmm.entity.db.MoneyAccount;
import com.ckview.mmm.entity.db.UserInfo;
import com.ckview.mmm.ui.adapter.ChooseMoneyAccountAdapter;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.ViewInject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

@ContentView(R.layout.fragment_choose_money_account)
public class ChooseMoneyAccountFragment extends BaseStatementsFragment implements Observer, AdapterView.OnItemClickListener {
    public static final int CHOOSE_MONEY_ACCOUNT_FRAGMENT = 2;

    /** 用户列表容器 */
    @ViewInject(R.id.gv_gridview)
    private GridView mGridView;
    /** 适配器 */
    private ChooseMoneyAccountAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ChooseMoneyAccountAdapter(mActivity);
        MoneyAccountDao.getInstance(mActivity).addObserver(this);
        MoneyAccountDao.getInstance(mActivity).getDisposableAccountFromUserId();
        setUserId();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
    }

    /**
     * 描 述：设置登录用户id<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/23 <br/>
     */
    private void setUserId() {
        UserInfo loginUser = UserInfoDao.getInstance(mActivity).getLoginUser();
        if(loginUser == null) {
            return;
        }
        mActivity.getmStatementData().setsUserId(loginUser.getId());
    }

    @Override
    public void update(Observable observable, Object data) {
        Message msg = (Message) data;
        switch (msg.what) {
            case Common.DISPOSABLE_ACCOUNT:   //获取完所有的用户信息了
                mAdapter.setmDatas((List) msg.obj);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(id == -1) {
            return;
        }
        MoneyAccount lMoneyAccount = mAdapter.getItem((int) id);
        mActivity.getmStatementData().setsMoneyAccountId(lMoneyAccount.getId());
        mActivity.nextPage();
    }

    @Override
    public void onDestroy() {
        MoneyAccountDao.getInstance(mActivity).deleteObserver(this);
        super.onDestroy();
    }

}
