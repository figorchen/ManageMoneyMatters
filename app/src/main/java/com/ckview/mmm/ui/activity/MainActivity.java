package com.ckview.mmm.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Button;

import com.ckview.mmm.R;
import com.ckview.mmm.entity.db.Statements;
import com.ckview.mmm.ui.adapter.StatementsProcessAdapter;
import com.uuzz.android.util.FileUtil;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.OnClick;
import com.uuzz.android.util.ioc.annotation.SaveInstance;
import com.uuzz.android.util.ioc.annotation.ViewInject;

@ContentView(R.layout.activity_main)
public class MainActivity extends AbstractActivity {

    /** 选择账户按钮 */
    @ViewInject(R.id.btn_choose_user_account)
    private Button mChooseAccount;
    /** 流水流程容器 */
    @ViewInject(R.id.vp_statements_container)
    private ViewPager mStatementsContainer;
    /** 流水数据实体 */
    @SaveInstance
    private Statements mStatementsData = new Statements();
    /** viewpager的适配器 */
    private StatementsProcessAdapter adapter;
    /** 按下退出按钮记录时间戳 */
    private long mTimestamp;
    /** 连续按两次返回按钮退出应用的时间间隔 */
    public static final long EXIT_DELAY = 2000;

    public Statements getmStatementsData() {
        return mStatementsData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPromissions(FileUtil.createPermissions(), new InitFilePath());
        adapter = new StatementsProcessAdapter(getFragmentManager(), mStatementsData);
        mStatementsContainer.setAdapter(adapter);
    }

    /**
     * 描 述：初始化文件路径的任务<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/7/13 注释 <br/>
     */
    private class InitFilePath implements Runnable {
        @Override
        public void run() {
            FileUtil.initFilePath(logger, FileUtil.getRootFilePath());
        }
    }

    /**
     * 描 述：跳转到选择账户页面<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/16 <br/>
     */
    @OnClick(R.id.btn_choose_user_account)
    private void startChooseUserAccount() {
        // TODO: 谌珂 2016/8/16  跳转到选择账户页面
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
        return index > 0;
    }

    /**
     * 描 述：缓慢滑动到下一个Fragment<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/17 <br/>
     */
    public void nextPage() {
        int index = mStatementsContainer.getCurrentItem() + 1;
        // DONE: 谌珂 2016/8/17 确定一共几个fragment
        if(index < adapter.getCount()) {
            mStatementsContainer.setCurrentItem(index, true);
        }
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
