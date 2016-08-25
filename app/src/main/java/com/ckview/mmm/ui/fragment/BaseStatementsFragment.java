package com.ckview.mmm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ckview.mmm.ui.activity.AddStatementActivity;
import com.uuzz.android.ui.fragment.CkFragment;


public class BaseStatementsFragment extends CkFragment {
    protected AddStatementActivity mActivity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (AddStatementActivity) getActivity();
    }
}
