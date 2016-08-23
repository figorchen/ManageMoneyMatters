package com.ckview.mmm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ckview.mmm.R;
import com.ckview.mmm.entity.db.Statements;
import com.uuzz.android.util.ioc.annotation.ViewInject;
import com.uuzz.android.util.ioc.utils.InjectUtils;

public class ChooseStatementAdapter extends BaseListViewAdapter<Statements> implements AdapterView.OnItemLongClickListener, CompoundButton.OnCheckedChangeListener {
    public ChooseStatementAdapter(Context mContext) {
        super(mContext);
    }
    /** 是否显示checkbox */
    private boolean isShowCheckBox;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Statements temp = mDatas.get(position);
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_statements, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

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
        holder.checkBox.setOnCheckedChangeListener(this);

        return convertView;
    }

    /**
     * 描 述：长按某条item显示checkbox<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/23 <br/>
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if(!isShowCheckBox) {
            isShowCheckBox = true;
            notifyDataSetChanged();
        }
        return false;
    }

    /**
     * 描 述：选中的流水标记为已选中<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/23 <br/>
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mDatas.get((Integer) buttonView.getTag()).setChecked(isChecked);
    }

    class ViewHolder {
        public ViewHolder(View v) {
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
    }
}
