package com.ckview.mmm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ckview.mmm.R;
import com.ckview.mmm.entity.db.UserInfo;
import com.uuzz.android.ui.view.CircleImageView;
import com.uuzz.android.util.ioc.annotation.ViewInject;
import com.uuzz.android.util.ioc.utils.InjectUtils;

public class ChooseUserAdapter extends BaseListViewAdapter<UserInfo> {
    public ChooseUserAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserInfo temp = mDatas.get(position);
        ViewHolder holder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_user_info, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.username.setText(temp.getmUserName());

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
