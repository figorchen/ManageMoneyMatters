package com.ckview.mmm.ui.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ckview.mmm.R;
import com.ckview.mmm.db.Common;
import com.ckview.mmm.db.UserInfoDao;
import com.ckview.mmm.entity.db.UserInfo;
import com.ckview.mmm.ui.adapter.ChooseSpenderAdapter;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.ViewInject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

@ContentView(R.layout.fragment_choose_user)
public class ChooseSpenderFragment extends BaseStatementsFragment implements Observer, AdapterView.OnItemClickListener {

    public static final int CHOOSE_SPENDER_FRAGMENT = 4;

    /** 用户列表容器 */
    @ViewInject(R.id.lv_listview)
    private ListView mListView;
    /** 适配器 */
    private ChooseSpenderAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ChooseSpenderAdapter(mActivity);
        UserInfoDao.getInstance(mActivity).addObserver(this);
        UserInfoDao.getInstance(mActivity).getAllUserAsync();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        Message msg = (Message) data;
        switch (msg.what) {
            case Common.ALL_USER_INFO:   //获取完所有的用户信息了
                mAdapter.setmDatas((List) msg.obj);
                List<UserInfo> userInfos = mAdapter.getmDatas();
                for (int i = 0; i < userInfos.size(); i++) {
                    if(userInfos.get(i).getmLogin()) {
                        nextPage(userInfos.get(i));
                        break;
                    }
                }
                break;
            default:
        }
    }

    /**
     * 描 述：跳转到下一页<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/25 <br/>
     * @param userinfo 用户信息
     */
    private void nextPage(UserInfo userinfo) {
        mActivity.getmStatementData().setsSpender(userinfo.getId());
        mActivity.nextPage();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(id == -1) {
            return;
        }
        nextPage(mAdapter.getItem((int) id));
    }

    @Override
    public void onDestroy() {
        UserInfoDao.getInstance(mActivity).deleteObserver(this);
        super.onDestroy();
    }
}
