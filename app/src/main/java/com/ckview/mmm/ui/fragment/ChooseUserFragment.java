package com.ckview.mmm.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ckview.mmm.R;
import com.ckview.mmm.db.UserInfoDao;
import com.ckview.mmm.entity.db.UserInfo;
import com.ckview.mmm.ui.adapter.ChooseUserAdapter;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.ViewInject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

@ContentView(R.layout.fragment_choose_user)
public class ChooseUserFragment extends BaseStatementsFragment implements Observer, AdapterView.OnItemClickListener {

    public static final int CHOOSE_USER_FRAGMENT = 2;

    /** 用户列表容器 */
    @ViewInject(R.id.lv_listview)
    private ListView mListView;
    /** 适配器 */
    private ChooseUserAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ChooseUserAdapter(mActivity);
        UserInfoDao.getInstance(mActivity).addObserver(this);
        UserInfoDao.getInstance(mActivity).getPayableUserAsync();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        Message msg = (Message) data;
        switch (msg.what) {
            case UserInfoDao.PAYABLE_USER_INFO:   //获取完所有的用户信息了
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
        UserInfo lUserInfo = mAdapter.getItem((int) id);
        mActivity.getmStatementsData().setsUserId(lUserInfo.getId());
        mActivity.nextPage();
    }
}
