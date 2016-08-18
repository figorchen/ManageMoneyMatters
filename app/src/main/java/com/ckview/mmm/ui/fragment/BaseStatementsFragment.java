package com.ckview.mmm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ckview.mmm.ui.activity.MainActivity;
import com.uuzz.android.ui.fragment.CkFragment;


public class BaseStatementsFragment extends CkFragment {
    protected MainActivity mActivity;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }
}
