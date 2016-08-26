package com.ckview.mmm.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.ckview.mmm.entity.db.Statement;
import com.ckview.mmm.ui.fragment.ChooseBorrowStatementFragment;
import com.ckview.mmm.ui.fragment.ChooseIntentFragment;
import com.ckview.mmm.ui.fragment.ChooseSpenderFragment;
import com.ckview.mmm.ui.fragment.ConfirmStatementFragment;
import com.ckview.mmm.ui.fragment.InputMoneyFragment;
import com.ckview.mmm.ui.fragment.StatementTypeFragment;

public class StatementsProcessAdapter extends FragmentPagerAdapter {
    /** 流水数据，从activity赋值 */
    private Statement mStatementData;

    public StatementsProcessAdapter(FragmentManager fm, Statement mStatementData) {
        super(fm);
        this.mStatementData = mStatementData;
    }

    /**
     * 描 述：https://www.processon.com/diagraming/57b1814de4b06764c1286ff3<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/22 <br/>
     */
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case StatementTypeFragment.STATEMENTS_DIRECTION:
                return new StatementTypeFragment();
            case InputMoneyFragment.INPUT_MONEY_FRAGMENT:
                return new InputMoneyFragment();
            case ChooseIntentFragment.CHOOSE_INTENT_FRAGMENT:
                return new ChooseIntentFragment();
            case ChooseSpenderFragment.CHOOSE_SPENDER_FRAGMENT:
                switch (mStatementData.getmStatementsType()) {
                    case Statement.OUTCOME_REPAYMENT :    //还款支出
                        return new ChooseBorrowStatementFragment();
                    case Statement.OUTCOME_CREDIT_CARD_PAYMENT :    //信用卡还款支出
                        break;
                    case Statement.INCOME_REPAYMENT :    //还款收入
                        break;
                    default:
                        if(mStatementData.getmStatementsType() < Statement.OUTCOME) {
                            return new ChooseSpenderFragment();
                        } else {
                            //生成流水页面
                            return new ConfirmStatementFragment();
                        }
                }
                break;
            case ConfirmStatementFragment.CONFIRM_STATEMENT_FRAGMENT:
                return new ConfirmStatementFragment();

        }
        return null;
    }

    @Override
    public int getCount() {
        if(mStatementData.isChanged()) {
            mStatementData.setChanged(false);
            notifyDataSetChanged();
        }
        int count = 1;
        if(mStatementData.getmStatementsType() == Statement.INCOME
                || mStatementData.getmStatementsType() == Statement.OUTCOME) {    //判断是否已经选择过流水类型
            count = InputMoneyFragment.INPUT_MONEY_FRAGMENT + 1;
        }
//        if(InputMoneyFragment.checkMoney(mStatementData)) {
//            count = ChooseMoneyAccountFragment.CHOOSE_MONEY_ACCOUNT_FRAGMENT + 1;
//        }
        if(InputMoneyFragment.checkMoney(mStatementData) && mStatementData.getsMoneyAccountId() > 0) {       //已输入有效金额且已选择有效资金账户
            count = ChooseIntentFragment.CHOOSE_INTENT_FRAGMENT + 1;
        }
        if(mStatementData.getmStatementsType() != Statement.INCOME && mStatementData.getmStatementsType() != Statement.OUTCOME && count > 1) {                              //已选择有效资金账户
            count = ChooseSpenderFragment.CHOOSE_SPENDER_FRAGMENT + 1;
        }
        if(mStatementData.getsSpender() > 0) {
            count = ConfirmStatementFragment.CONFIRM_STATEMENT_FRAGMENT + 1;
        }
        return count;
    }
}
