/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: PtrListView.java <br/>
 * <p/>
 * Created by 谌珂 on 2016/1/28.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.ui.view.ptr;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.uuzz.android.R;

/**
 * 项目名称：手机大管家 <br/>
 * 类  名: PtrListView <br/>
 * 类描述: 带有上拉加载和底部视图的ListView，提供上拉加载回调。如果不想启用上拉加载可调用{@link #removeFootView()}隐藏底部视图，底部视图隐藏后同时也会屏蔽上拉加载<br/>
 * 实现的主要功能 <br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2016/1/28 <br/>
 * @author 谌珂 <br/>
 */
public class PtrListView extends ListView {

    /** 是否还需要刷新 */
    private boolean isLocked;
    /** 加载更多的回调事件 */
    private LoadMoreListener mListener;
    /** 底部视图内容 */
    private TextView lFootContent;
    /** 底部视图 */
    private View mFootView;

    public PtrListView(Context context) {
        this(context, null);
    }

    public PtrListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PtrListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * 描 述：添加foot view，并给foot点击和滑动到底部时注册回调<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/1/28 注释 <br/>
     */
    private void init(Context context) {
        LayoutInflater lLayoutInflater = LayoutInflater.from(context);
        mFootView = lLayoutInflater.inflate(R.layout.foot_load_more, null);
        lFootContent = (TextView) mFootView.findViewById(R.id.tv_content);
        lFootContent.setText(getContext().getString(R.string.load_more));
        addFooterView(mFootView);
        mFootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doPullEvent();
            }
        });
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case OnScrollListener.SCROLL_STATE_IDLE:
                        doPullEvent();
                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    /**
     * 描 述：执行上拉加载事件<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/1/28 注释 <br/>
     */
    private void doPullEvent(){
        if(!isLocked && mListener != null && getFooterViewsCount() > 0 && getLastVisiblePosition() == (getCount() - 1)) {
            isLocked = true;
            lFootContent.setText(getContext().getString(R.string.loading));
            lFootContent.setTextColor(Color.parseColor("#DCDCDC"));
            mListener.onLoadMore();
        }
    }

    /**
     * 描 述：加载完成<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/1/28 注释 <br/>
     */
    public void loadComplete() {
        lFootContent.setText(getContext().getString(R.string.load_more));
        lFootContent.setTextColor(Color.parseColor("#FD5453"));
        isLocked = false;
    }

    /**
     * 描 述：设置不加载更多数据<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/1/28 注释 <br/>
     * @param locked true为不加载
     */
    public void lockLoad(boolean locked) {
        lFootContent.setText(getContext().getString(R.string.no_more));
        lFootContent.setTextColor(Color.parseColor("#DCDCDC"));
        this.isLocked = locked;
    }

    /**
     * 描 述：设置监听滚动到底部或点击到foot的回调方法<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/1/28 注释 <br/>
     */
    public void setLoadMoreListener(LoadMoreListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 描 述：加载更多的回调接口<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/1/28 注释 <br/>
     */
    public interface LoadMoreListener {
        /**
         * 描 述：加载更多<br/>
         * 作者：谌珂<br/>
         * 历 史: (版本) 谌珂 2016/1/28 注释 <br/>
         */
        void onLoadMore();
    }

    /**
     * 描 述：移除底部布局<br/>
     * 作者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/1/30 注释 <br/>
     */
    public void removeFootView() {
        removeFooterView(mFootView);
    }
}
