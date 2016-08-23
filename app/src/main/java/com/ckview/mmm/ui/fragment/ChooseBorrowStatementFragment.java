package com.ckview.mmm.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ckview.mmm.R;
import com.ckview.mmm.db.Common;
import com.ckview.mmm.db.StatementDao;
import com.ckview.mmm.ui.adapter.ChooseStatementAdapter;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.ViewInject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

@ContentView(R.layout.fragment_choose_user)
public class ChooseBorrowStatementFragment extends BaseStatementsFragment implements Observer, AdapterView.OnItemClickListener {

    /** 用户列表容器 */
    @ViewInject(R.id.lv_listview)
    private ListView mListView;
    /** 适配器 */
    private ChooseStatementAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ChooseStatementAdapter(mActivity);
        StatementDao.getInstance(mActivity).addObserver(this);
        StatementDao.getInstance(mActivity).getIncomeBorrowStatements();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(mAdapter);
    }

    @Override
    public void update(Observable observable, Object data) {
        Message msg = (Message) data;
        switch (msg.what) {
            case Common.INCOME_BORROW_STATEMENTS:   //获取完所有的用户信息了
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
        mActivity.nextPage();
    }

    @Override
    public void onDestroy() {
        StatementDao.getInstance(mActivity).deleteObserver(this);
        super.onDestroy();
    }
}
