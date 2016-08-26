package com.ckview.mmm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.ckview.mmm.R;
import com.ckview.mmm.db.Common;
import com.ckview.mmm.db.MoneyAccountDao;
import com.ckview.mmm.db.UserInfoDao;
import com.ckview.mmm.entity.db.MoneyAccount;
import com.ckview.mmm.entity.db.Statement;
import com.ckview.mmm.entity.db.UserInfo;
import com.ckview.mmm.ui.adapter.ChooseMoneyAccountAdapter;
import com.ckview.mmm.ui.textwatcher.MoneyTextWatcher;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.ViewInject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

@ContentView(R.layout.activity_change_money_account)
public class InputMoneyFragment extends BaseStatementsFragment implements Observer, AdapterView.OnItemClickListener {

    public static final int INPUT_MONEY_FRAGMENT = 1;
    /** 花钱金额输入框 */
    @ViewInject(R.id.et_loan)
    private EditText mMoney;
    /** 确认按钮，用不上，需要隐藏 */
    @ViewInject(R.id.btn_commit)
    private View mCommit;

    /** 用户列表容器 */
    @ViewInject(R.id.gv_gridview)
    private GridView mGridView;
    /** 适配器 */
    private ChooseMoneyAccountAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommit.setVisibility(View.GONE);
        mMoney.addTextChangedListener(new MoneyTextWatcher(mMoney));
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
        commit();
        mActivity.nextPage();
    }

    @Override
    public void onDestroy() {
        MoneyAccountDao.getInstance(mActivity).deleteObserver(this);
        super.onDestroy();
    }

    /**
     * 描 述：收集输入的金额，并跳转到下一步<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/17 <br/>
     */
    private void commit() {
        InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mMoney.getWindowToken(),0);
        double money;
        try {
            money = Double.valueOf(String.valueOf(mMoney.getText()));
        } catch (Exception e) {
            money = 0;
        }
        mActivity.getmStatementData().setsMoney(money);
        if(!checkMoney(mActivity.getmStatementData())) {
            Snackbar.make(mMoney, R.string.input_correct_money, Snackbar.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 描 述：检查流水金额是否合法<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/22 <br/>
     * @param data 流水数据
     * @return 合法返回true，非法返回false
     */
    public static boolean checkMoney(Statement data) {
        return data.getsMoney() > 0;
    }
}
