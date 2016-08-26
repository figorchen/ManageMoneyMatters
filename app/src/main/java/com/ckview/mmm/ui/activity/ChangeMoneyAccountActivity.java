/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: ChangeMoneyAccountActivity.java <br/>
 * <p>
 * Created by 谌珂 on 2016/8/26.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.ckview.mmm.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.ckview.mmm.R;
import com.ckview.mmm.db.Common;
import com.ckview.mmm.db.MoneyAccountDao;
import com.ckview.mmm.entity.db.MoneyAccount;
import com.ckview.mmm.ui.adapter.ChooseMoneyAccountAdapter;
import com.ckview.mmm.ui.textwatcher.MoneyTextWatcher;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.OnClick;
import com.uuzz.android.util.ioc.annotation.ViewInject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * 项目名称：手机大管家 <br/>
 * 类  名: ChangeMoneyAccountActivity <br/>
 * 类描述: <br/>
 * 实现的主要功能 <br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2016/8/26 <br/>
 * @author 谌珂 <br/>
 */
@ContentView(R.layout.activity_change_money_account)
public class ChangeMoneyAccountActivity extends AbstractActivity implements Observer, AdapterView.OnItemClickListener {
    @ViewInject(R.id.container)
    private View mContainer;
    @ViewInject(R.id.et_loan)
    private EditText mMoney;
    @ViewInject(R.id.tv_bank_name)
    private TextView mBankName;
    /** 用户列表容器 */
    @ViewInject(R.id.gv_gridview)
    private GridView mGridView;
    /** 适配器 */
    private ChooseMoneyAccountAdapter mAdapter;
    /** 当前正在编辑的资金账户 */
    private MoneyAccount mMoneyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContainer.setVisibility(View.GONE);
        mAdapter = new ChooseMoneyAccountAdapter(this);
        MoneyAccountDao.getInstance(this).addObserver(this);
        MoneyAccountDao.getInstance(this).getAllAccountFromUserId();
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        mMoney.addTextChangedListener(new MoneyTextWatcher(mMoney));
    }

    /**
     * 描 述：收集输入的金额，并跳转到下一步<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/17 <br/>
     */
    @OnClick(R.id.btn_commit)
    private void commit() {
        // DONE: 谌珂 2016/8/26 提交账户余额
        if(mMoneyAccount == null) {
            return;
        }
        mMoneyAccount.setmBalance(Double.valueOf(mMoney.getText().toString()));
        if(MoneyAccountDao.getInstance(this).updateMoneyAccount(mMoneyAccount)) {
            Snackbar.make(mMoney, R.string.change_money_account_blance_success, Snackbar.LENGTH_SHORT).show();
            mContainer.setVisibility(View.GONE);
        } else {
            Snackbar.make(mMoney, R.string.change_money_account_blance_failed, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        Message msg = (Message) data;
        switch (msg.what) {
            case Common.MONEY_ACCOUNT:   //获取完所有的用户信息了
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
        mMoneyAccount = mAdapter.getItem((int) id);
        mMoney.setText(String.valueOf(mMoneyAccount.getmBalance()));
        mBankName.setText(TextUtils.isEmpty(mMoneyAccount.getcBankName())? mMoneyAccount.getmTypeName() : mMoneyAccount.getcBankName() + mMoneyAccount.getmTypeName());
        mContainer.setVisibility(View.VISIBLE);
    }
}
