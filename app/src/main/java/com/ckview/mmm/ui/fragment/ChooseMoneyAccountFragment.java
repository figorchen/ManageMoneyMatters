package com.ckview.mmm.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ckview.mmm.R;
import com.ckview.mmm.db.MoneyAccountDao;
import com.ckview.mmm.entity.db.MoneyAccount;
import com.ckview.mmm.ui.adapter.ChooseMoneyAccountAdapter;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.ViewInject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

@ContentView(R.layout.fragment_choose_money_account)
public class ChooseMoneyAccountFragment extends BaseStatementsFragment implements Observer, AdapterView.OnItemClickListener {
    public static final int CHOOSE_MONEY_ACCOUNT_FRAGMENT = 3;

    /** 用户列表容器 */
    @ViewInject(R.id.gv_gridview)
    private GridView mListView;
    /** 适配器 */
    private ChooseMoneyAccountAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ChooseMoneyAccountAdapter(mActivity);
        MoneyAccountDao.getInstance(mActivity).addObserver(this);
        MoneyAccountDao.getInstance(mActivity).getDisposableAccountFromUserId();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        Message msg = (Message) data;
        switch (msg.what) {
            case MoneyAccountDao.DISPOSABLE_ACCOUNT_FROM_USERID:   //获取完所有的用户信息了
                mAdapter.setmDatas((List) msg.obj);
                break;
            default:
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(id == -1) {
            return;
        }
        MoneyAccount lUserInfo = mAdapter.getItem((int) id);
        mActivity.getmStatementsData().setsUserId(lUserInfo.getId());
        mActivity.nextPage();
    }

}
