package com.ckview.mmm.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ckview.mmm.R;
import com.ckview.mmm.entity.db.Statements;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.OnClick;
import com.uuzz.android.util.ioc.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends AbstractActivity {

    /** 收入按钮 */
    @ViewInject(R.id.btn_income)
    private Button mIncome;
    /** 支出按钮 */
    @ViewInject(R.id.btn_outcome)
    private Button mOutcome;
    /** 支出按钮 */
    @ViewInject(R.id.btn_choose_user_account)
    private Button mChooseAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /**
     * 描 述：跳转到选择账户页面<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/16 <br/>
     */
    @OnClick(R.id.btn_choose_user_account)
    private void startChooseUserAccount() {
        // TODO: 谌珂 2016/8/16  跳转到选择账户页面
    }

    /**
     * 描 述：跳转到选择资金账户界面<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/16 <br/>
     */
    @OnClick({R.id.btn_outcome, R.id.btn_income})
    private void startChooseMoneyAccount(View v) {
        // TODO: 谌珂 2016/8/16  跳转到选择资金账户界面
        Statements lStatements = new Statements();
        switch (v.getId()) {
            case R.id.btn_outcome:
//                lStatements.setmStatementsType();
                break;
            case R.id.btn_income:
                break;
        }
    }

}
