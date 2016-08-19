package com.ckview.mmm.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ckview.mmm.R;
import com.ckview.mmm.entity.db.MoneyAccount;
import com.uuzz.android.ui.view.CircleImageView;
import com.uuzz.android.util.ioc.annotation.ViewInject;
import com.uuzz.android.util.ioc.utils.InjectUtils;

public class ChooseMoneyAccountAdapter extends BaseListViewAdapter<MoneyAccount> {
    public ChooseMoneyAccountAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MoneyAccount temp = mDatas.get(position);
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_info, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StringBuilder sb = new StringBuilder();
        if(TextUtils.isEmpty(temp.getcBankName())) {
            sb.append(temp.getmTypeName());
        } else {
            sb.append(temp.getcBankName()).append(temp.getmTypeName());
        }
        holder.username.setText(sb.toString());

        return convertView;
    }

    class ViewHolder {
        public ViewHolder(View v) {
            InjectUtils.injectViews(v, this);
        }
        @ViewInject(R.id.civ_icon)
        private CircleImageView icon;
        @ViewInject(R.id.tv_username)
        private TextView username;
    }
}
