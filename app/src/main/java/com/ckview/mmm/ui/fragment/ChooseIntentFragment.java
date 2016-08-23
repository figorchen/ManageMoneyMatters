package com.ckview.mmm.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ckview.mmm.R;
import com.ckview.mmm.db.Common;
import com.ckview.mmm.db.StatementTypeDao;
import com.ckview.mmm.ui.adapter.ChooseIntentAdapter;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.ViewInject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

@ContentView(R.layout.fragment_choose_user)
public class ChooseIntentFragment extends BaseStatementsFragment implements Observer, AdapterView.OnItemClickListener {
    /** 选择流水意图 */
    public static final int CHOOSE_INTENT_FRAGMENT = 3;

    /** 流水意图容器 */
    @ViewInject(R.id.lv_listview)
    private ListView mListView;
    /** 适配器 */
    private ChooseIntentAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ChooseIntentAdapter(mActivity);
        StatementTypeDao.getInstance(mActivity).addObserver(this);
        StatementTypeDao.getInstance(mActivity).getIntentAsync(mActivity.getmStatementsData());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        Message msg = (Message) data;
        switch (msg.what) {
            case Common.GET_INTENT_FROM_STATEMENT:   //获取完所有的用户信息了
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
        mActivity.getmStatementsData().setmStatementsType(mAdapter.getItem((int) id).getId());
        mActivity.getmStatementsData().setsName(mAdapter.getItem((int) id).getsName());
        mActivity.nextPage();
    }

    @Override
    public void onDestroy() {
        StatementTypeDao.getInstance(mActivity).deleteObserver(this);
        super.onDestroy();
    }

}
