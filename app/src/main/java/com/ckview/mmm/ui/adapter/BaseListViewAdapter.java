package com.ckview.mmm.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.BaseAdapter;

import java.util.List;


public abstract class BaseListViewAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mDatas;

    public BaseListViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setmDatas(List<T> mDatas) {
        this.mDatas = mDatas;
        refresh();
    }

    public List<T> getmDatas() {
        return mDatas;
    }

    /**
     * 描 述：添加新数据<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/19 <br/>
     */
    public void addmDatas(List<T> datas) {
        if(datas == null || datas.size() < 1) {
            return;
        }
        if(mDatas == null) {
            mDatas = datas;
        } else {
            mDatas.addAll(datas);
        }
        refresh();
    }

    /**
     * 描 述：如果Context是Activity的实例主线程刷新列表，否则不刷新<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/19 <br/>
     */
    protected void refresh() {
        if (Activity.class.isAssignableFrom(mContext.getClass())) {
            ((Activity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getCount() {
        if(mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
