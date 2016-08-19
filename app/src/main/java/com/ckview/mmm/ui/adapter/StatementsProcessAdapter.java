package com.ckview.mmm.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.ckview.mmm.ui.fragment.ChooseMoneyAccountFragment;
import com.ckview.mmm.ui.fragment.ChooseUserFragment;
import com.ckview.mmm.ui.fragment.InputMoneyFragment;
import com.ckview.mmm.ui.fragment.StatementsDirectionFragment;

public class StatementsProcessAdapter extends FragmentPagerAdapter {
    public StatementsProcessAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case StatementsDirectionFragment.STATEMENTS_DIRECTION:
                return new StatementsDirectionFragment();
            case InputMoneyFragment.INPUT_MONEY_FRAGMENT:
                return new InputMoneyFragment();
            case ChooseUserFragment.CHOOSE_USER_FRAGMENT:
                return new ChooseUserFragment();
            case ChooseMoneyAccountFragment.CHOOSE_MONEY_ACCOUNT_FRAGMENT:
                return new ChooseMoneyAccountFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
