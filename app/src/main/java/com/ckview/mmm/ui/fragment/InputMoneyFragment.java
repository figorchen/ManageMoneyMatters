package com.ckview.mmm.ui.fragment;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ckview.mmm.R;
import com.ckview.mmm.entity.db.Statements;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.OnClick;
import com.uuzz.android.util.ioc.annotation.ViewInject;

@ContentView(R.layout.fragment_input_money)
public class InputMoneyFragment extends BaseStatementsFragment {

    public static final int INPUT_MONEY_FRAGMENT = 1;

    @ViewInject(R.id.et_loan)
    private EditText mMoney;

    /**
     * 描 述：收集输入的金额，并跳转到下一步<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/17 <br/>
     */
    @OnClick(R.id.btn_commit)
    private void commit() {
        InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mMoney.getWindowToken(),0);
        double money;
        try {
            money = Double.valueOf(String.valueOf(mMoney.getText()));
        } catch (Exception e) {
            money = 0;
        }
        mActivity.getmStatementsData().setsMoney(money);
        if(!checkMoney(mActivity.getmStatementsData())) {
            Snackbar.make(mMoney, R.string.input_correct_money, Snackbar.LENGTH_SHORT).show();
            return;
        }
        mActivity.nextPage();
    }

    /**
     * 描 述：检查流水金额是否合法<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/22 <br/>
     * @param data 流水数据
     * @return 合法返回true，非法返回false
     */
    public static boolean checkMoney(Statements data) {
        return data.getsMoney() > 0;
    }
}
