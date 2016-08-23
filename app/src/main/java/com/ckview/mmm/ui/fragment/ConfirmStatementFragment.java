package com.ckview.mmm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.ckview.mmm.R;
import com.ckview.mmm.db.UserInfoDao;
import com.ckview.mmm.entity.db.UserInfo;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.OnClick;
import com.uuzz.android.util.ioc.annotation.ViewInject;

import java.util.Calendar;
import java.util.GregorianCalendar;

@ContentView(R.layout.fragment_confirm_statement)
public class ConfirmStatementFragment extends BaseStatementsFragment implements CalendarView.OnDateChangeListener {
    /** 确认流水页面 */// TODO: 谌珂 2016/8/23 以后要改成6
    public static final int CONFIRM_STATEMENT_FRAGMENT = 5;

    /** 流水时间 */
    @ViewInject(R.id.tv_time)
    private TextView time;
    /** 流水类型 */
    @ViewInject(R.id.tv_statement_type)
    private TextView statementType;
    /** 流水金额 */
    @ViewInject(R.id.tv_money)
    private TextView money;
    /** 流水描述 */
    @ViewInject(R.id.tv_desc)
    private TextView desc;
    /** 描述内容 */
    @ViewInject(R.id.et_desc)
    private EditText mDescription;
    /** 日历控件 */
    @ViewInject(R.id.cv_calendar)
    private CalendarView mCalendarView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCalendarView.setVisibility(View.GONE);
        mCalendarView.setOnDateChangeListener(this);
        time.setText(mActivity.getmStatementsData().getTime());
        statementType.setText(mActivity.getmStatementsData().getsName());
        money.setText(String.valueOf(mActivity.getmStatementsData().getsMoney()));
        desc.setVisibility(View.GONE);
    }

    /**
     * 描 述：点击流水时间弹出日历进行修改<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/23 <br/>
     */
    @OnClick(R.id.tv_time)
    private void showCalendarView() {
        mCalendarView.setVisibility(View.VISIBLE);
    }

    /**
     * 描 述：确认流水，入库<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/23 <br/>
     */
    @OnClick(R.id.btn_commit)
    private void confirm() {
        UserInfo loginUser = UserInfoDao.getInstance(mActivity).getLoginUser();
        if(loginUser == null) {
            return;
        }
        mActivity.getmStatementsData().setsUserId(loginUser.getId());
        mActivity.getmStatementsData().setsDescription(mDescription.getText().toString());
    }

    /**
     * 描 述：日历时间改变后把日历隐藏，并把时间赋值给time<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/23 <br/>
     */
    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        view.setDate(calendar.getTimeInMillis());
        mActivity.getmStatementsData().setsTimestamp(view.getDate());
        time.setText(mActivity.getmStatementsData().getTime());
        view.setVisibility(View.GONE);
    }
}
