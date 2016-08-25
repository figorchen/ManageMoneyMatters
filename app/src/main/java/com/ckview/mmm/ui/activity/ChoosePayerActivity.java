/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: ChoosePayerActivity.java <br/>
 * <p>
 * Created by 谌珂 on 2016/8/25.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.ckview.mmm.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ckview.mmm.R;
import com.ckview.mmm.db.Common;
import com.ckview.mmm.db.UserInfoDao;
import com.ckview.mmm.ui.adapter.ChooseSpenderAdapter;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.ViewInject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 项目名称：手机大管家 <br/>
 * 类  名: ChoosePayerActivity <br/>
 * 类描述: <br/>
 * 实现的主要功能 <br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2016/8/25 <br/>
 * @author 谌珂 <br/>
 */
@ContentView(R.layout.fragment_choose_user)
public class ChoosePayerActivity extends AbstractActivity implements Observer, AdapterView.OnItemClickListener {
    @ViewInject(R.id.lv_listview)
    private ListView mListView;
    /** 适配器 */
    private ChooseSpenderAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ChooseSpenderAdapter(this);
        UserInfoDao.getInstance(this).addObserver(this);
        UserInfoDao.getInstance(this).getPayableUserAsync();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        Message msg = (Message) data;
        switch (msg.what) {
            case Common.PAYABLE_USER_INFO:   //获取完所有的用户信息了
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
        UserInfoDao.getInstance(this).changeUser(mAdapter.getItem((int) id).getId());
        finish();
    }

    @Override
    protected void onDestroy() {
        UserInfoDao.getInstance(this).deleteObserver(this);
        super.onDestroy();
    }
}
