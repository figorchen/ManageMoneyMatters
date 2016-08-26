package com.ckview.mmm.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ckview.mmm.R;
import com.ckview.mmm.entity.db.Statement;
import com.uuzz.android.util.ioc.annotation.ViewInject;
import com.uuzz.android.util.ioc.utils.InjectUtils;

import java.util.List;

public class RecylerStatementAdapter extends RecyclerView.Adapter<RecylerStatementAdapter.ViewHolder> {
    public RecylerStatementAdapter(Context mContext) {
        this.mContext = mContext;
    }

    private Context mContext;
    private List<Statement> mDatas;
    private boolean isShowCheckBox;

    public void setmDatas(List<Statement> mDatas) {
        this.mDatas = mDatas;
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

    public List<Statement> getmDatas() {
        return mDatas;
    }

    /**
     * 描 述：添加新数据<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/19 <br/>
     */
    public void addmDatas(List<Statement> datas) {
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_statements, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Statement temp = mDatas.get(position);
        holder.time.setText(temp.getTime());
        holder.statementType.setText(temp.getsName());
        holder.money.setText(String.valueOf(temp.getsMoney()));
        holder.desc.setText(temp.getsDescription());
        holder.checkBox.setTag(position);
        if(isShowCheckBox) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        if(temp.getmStatementsType() < Statement.OUTCOME) {
            holder.container.setBackgroundResource(R.drawable.touchable_background_pink);
        } else {
            holder.container.setBackgroundResource(R.drawable.touchable_background_green);
        }
    }

    @Override
    public int getItemCount() {
        if(mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
            InjectUtils.injectViews(v, this);
        }
        @ViewInject(R.id.tv_time)
        private TextView time;
        @ViewInject(R.id.tv_statement_type)
        private TextView statementType;
        @ViewInject(R.id.tv_money)
        private TextView money;
        @ViewInject(R.id.tv_desc)
        private TextView desc;
        @ViewInject(R.id.cb_check)
        private CheckBox checkBox;
        @ViewInject(R.id.container)
        private View container;
    }
}
