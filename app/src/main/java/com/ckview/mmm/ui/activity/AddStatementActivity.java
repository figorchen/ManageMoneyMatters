package com.ckview.mmm.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.ckview.mmm.R;
import com.ckview.mmm.entity.db.Statement;
import com.ckview.mmm.ui.adapter.StatementsProcessAdapter;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.SaveInstance;
import com.uuzz.android.util.ioc.annotation.ViewInject;

@ContentView(R.layout.activity_add_statement)
public class AddStatementActivity extends AbstractActivity {

    /** 流水流程容器 */
    @ViewInject(R.id.vp_statements_container)
    private ViewPager mStatementsContainer;

    /** 流水数据实体 */
    @SaveInstance
    private Statement mStatementData = new Statement();
    /** viewpager的适配器 */
    private StatementsProcessAdapter adapter;
    /** 按下退出按钮记录时间戳 */
    private long mTimestamp;
    /** 连续按两次返回按钮退出应用的时间间隔 */
    public static final long EXIT_DELAY = 2000;

    public Statement getmStatementData() {
        return mStatementData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new StatementsProcessAdapter(getFragmentManager(), mStatementData);
        mStatementsContainer.setAdapter(adapter);
    }

    /**
     * 描 述：缓慢滑动到上一个Fragment<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/17 <br/>
     */
    public boolean lastPage() {
        adapter.notifyDataSetChanged();
        int index = mStatementsContainer.getCurrentItem() - 1;
        if(index >= 0) {
            mStatementsContainer.setCurrentItem(index, true);
        }
        return index >= 0;
    }

    /**
     * 描 述：缓慢滑动到下一个Fragment<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/17 <br/>
     */
    public void nextPage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatementsContainer.setCurrentItem(mStatementsContainer.getCurrentItem() + 1, true);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(lastPage()) {
                return true;
            } else {
                if(System.currentTimeMillis() - mTimestamp > EXIT_DELAY) {
                    mTimestamp = System.currentTimeMillis();
                    Snackbar.make(mStatementsContainer, R.string.press_again_exit, Snackbar.LENGTH_SHORT).show();
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
