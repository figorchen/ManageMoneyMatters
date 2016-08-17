package com.uuzz.android.ui.view.ptr;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.uuzz.android.R;

public class PtrExpandableListView extends ExpandableListView {

    /** 是否还需要刷新 */
    private boolean isLocked;
    /** 加载更多的回调事件 */
    private PtrListView.LoadMoreListener mListener;
    /** 底部视图内容 */
    private TextView lFootContent;
    /** 底部视图 */
    private View mFootView;

    public PtrExpandableListView(Context context) {
        this(context, null);
    }

    public PtrExpandableListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PtrExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PtrExpandableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
                doPullEvent();
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
        if(!isLocked && mListener != null && getFooterViewsCount() > 0) {
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
    public void setLoadMoreListener(PtrListView.LoadMoreListener mListener) {
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
