/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: CkFragment.java <br/>
 * <p/>
 * Created by 谌珂 on 2016/1/12.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uuzz.android.util.ioc.utils.InjectUtils;
import com.uuzz.android.util.log.UUZZLog;

/**
 * 项目名称：手机大管家 <br/>
 * 类  名: CkFragment <br/>
 * 类描述: <br/>
 * 实现的主要功能 <br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2016/1/12 <br/>
 * @author 谌珂 <br/>
 */
public abstract class CkFragment extends Fragment {

    /** 日志对象 */
    protected final UUZZLog logger = new UUZZLog(getClass().getSimpleName());

    protected View mRootView;

    public View getmRootView() {
        return mRootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = InjectUtils.injectContentView(this);
        logger.d("Fragment restore instance");
        InjectUtils.restoreInstance(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logger.d("Fragment save instance");
        InjectUtils.saveInstances(this, outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        InjectUtils.inject(this);
        super.onActivityCreated(savedInstanceState);
    }
}
