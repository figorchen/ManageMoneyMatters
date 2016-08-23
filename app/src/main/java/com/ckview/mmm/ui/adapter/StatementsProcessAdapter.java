package com.ckview.mmm.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.ckview.mmm.entity.db.Statements;
import com.ckview.mmm.ui.fragment.ChooseBorrowStatementFragment;
import com.ckview.mmm.ui.fragment.ChooseIntentFragment;
import com.ckview.mmm.ui.fragment.ChooseMoneyAccountFragment;
import com.ckview.mmm.ui.fragment.ChooseUserFragment;
import com.ckview.mmm.ui.fragment.ConfirmStatementFragment;
import com.ckview.mmm.ui.fragment.InputMoneyFragment;
import com.ckview.mmm.ui.fragment.StatementTypeFragment;

public class StatementsProcessAdapter extends FragmentPagerAdapter {
    /** 流水数据，从activity赋值 */
    private Statements mStatementsData;

    public StatementsProcessAdapter(FragmentManager fm, Statements mStatementsData) {
        super(fm);
        this.mStatementsData = mStatementsData;
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
            case ChooseMoneyAccountFragment.CHOOSE_MONEY_ACCOUNT_FRAGMENT:
                return new ChooseMoneyAccountFragment();
            case ChooseIntentFragment.CHOOSE_INTENT_FRAGMENT:
                return new ChooseIntentFragment();
            case ChooseUserFragment.CHOOSE_USER_FRAGMENT:
                switch (mStatementsData.getmStatementsType()) {
                    case Statements.OUTCOME_REPAYMENT :    //还款支出
                        return new ChooseBorrowStatementFragment();
                    case Statements.OUTCOME_CREDIT_CARD_PAYMENT :    //信用卡还款支出
                        break;
                    case Statements.INCOME_REPAYMENT :    //还款收入
                        break;
                    default:
                        if(mStatementsData.getmStatementsType() < Statements.OUTCOME) {
                            return new ChooseUserFragment();
                        } else {
                            //生成流水页面
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
        if(mStatementsData.isChanged()) {
            mStatementsData.setChanged(false);
            notifyDataSetChanged();
        }
        int count = 1;
        if(mStatementsData.getmStatementsType() == Statements.INCOME
                || mStatementsData.getmStatementsType() == Statements.OUTCOME) {    //判断是否已经选择过流水类型
            count = InputMoneyFragment.INPUT_MONEY_FRAGMENT + 1;
        }
        if(InputMoneyFragment.checkMoney(mStatementsData)) {                        //已输入有效金额
            count = ChooseMoneyAccountFragment.CHOOSE_MONEY_ACCOUNT_FRAGMENT + 1;
        }
        if(mStatementsData.getsMoneyAccountId() > 0) {                              //已选择有效资金账户
            count = ChooseIntentFragment.CHOOSE_INTENT_FRAGMENT + 1;
        }
        if(mStatementsData.getmStatementsType() != Statements.INCOME && mStatementsData.getmStatementsType() != Statements.OUTCOME) {                              //已选择有效资金账户
            count = ChooseUserFragment.CHOOSE_USER_FRAGMENT + 1;
        }
        if(mStatementsData.getsSpender() > 0) {
            count = ConfirmStatementFragment.CONFIRM_STATEMENT_FRAGMENT + 1;
        }
        return count;
    }
}
