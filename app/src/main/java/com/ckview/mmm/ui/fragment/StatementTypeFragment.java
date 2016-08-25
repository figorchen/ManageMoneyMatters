package com.ckview.mmm.ui.fragment;

import android.view.View;

import com.ckview.mmm.R;
import com.ckview.mmm.entity.db.Statement;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.OnClick;

@ContentView(R.layout.fragment_choose_income_or_outcome)
public class StatementTypeFragment extends BaseStatementsFragment {

    public static final int STATEMENTS_DIRECTION = 0;

    /**
     * 描 述：跳转到输入金额界面<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/16 <br/>
     */
    @OnClick({R.id.btn_outcome, R.id.btn_income})
    private void startInputMoney(View v) {
        Statement lStatement = mActivity.getmStatementData();
        switch (v.getId()) {
            case R.id.btn_outcome:
                lStatement.setmStatementsType(Statement.OUTCOME);
                break;
            case R.id.btn_income:
                lStatement.setmStatementsType(Statement.INCOME);
                break;
            default:
                throw new IllegalArgumentException();
        }
        // DONE: 谌珂 2016/8/16  跳转到输入金额界面
        mActivity.nextPage();
    }
}
