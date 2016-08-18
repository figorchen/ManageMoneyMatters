package com.ckview.mmm.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.ckview.mmm.R;
import com.ckview.mmm.entity.db.Statements;
import com.uuzz.android.util.FileUtil;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.OnClick;
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
    private Statements mStatementsData = new Statements();

    public Statements getmStatementsData() {
        return mStatementsData;
    }

    public void setmStatementsData(Statements mStatementsData) {
        this.mStatementsData = mStatementsData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPromissions(FileUtil.createPermissions(), new InitFilePath());
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
    public void lastPage() {
        int index = mStatementsContainer.getCurrentItem() - 1;
        if(index >= 0) {
            mStatementsContainer.setCurrentItem(index, true);
        }
    }

    /**
     * 描 述：缓慢滑动到下一个Fragment<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/17 <br/>
     */
    public void nextPage() {
        int index = mStatementsContainer.getCurrentItem() + 1;
        // TODO: 谌珂 2016/8/17 确定一共几个fragment
        if(index < 50) {
            mStatementsContainer.setCurrentItem(index, true);
        }
    }

}
