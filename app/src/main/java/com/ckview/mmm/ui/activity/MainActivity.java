package com.ckview.mmm.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.ckview.mmm.R;
import com.ckview.mmm.db.Common;
import com.ckview.mmm.db.StatementDao;
import com.ckview.mmm.entity.db.Statement;
import com.ckview.mmm.ui.adapter.StatementAdapter;
import com.uuzz.android.util.FileUtil;
import com.uuzz.android.util.ioc.annotation.ContentView;
import com.uuzz.android.util.ioc.annotation.OnClick;
import com.uuzz.android.util.ioc.annotation.ViewInject;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

@ContentView(R.layout.activity_main)
public class MainActivity extends AbstractActivity implements Observer {
    /** 标题栏 */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;
    @ViewInject(R.id.drawer_layout)
    private DrawerLayout mDrawerLayout;
    @ViewInject(R.id.lv_listview)
    private ListView mListView;
    @ViewInject(R.id.fab)
    private FloatingActionButton fab;
    @ViewInject(R.id.v_shader)
    private View mShader;
    private ActionBarDrawerToggle drawerToggle;
    private StatementAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
        mDrawerLayout.setDrawerListener(drawerToggle);
        checkPromissions(FileUtil.createPermissions(), new MainActivity.InitFilePath());
        mAdapter = new StatementAdapter(this);
        mListView.setAdapter(mAdapter);
        StatementDao.getInstance(this).addObserver(this);
    }

    @Override
    protected void onResume() {
        StatementDao.getInstance(this).getStatements();
        super.onResume();
    }

    @Override
    public void update(Observable observable, Object data) {
        Message msg = (Message) data;
        switch (msg.what) {
            case Common.GET_ALL_STATEMENT_:
                mAdapter.setmDatas((List<Statement>) msg.obj);
                break;
        }
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
     * 描 述：点击Fab后弹出选项<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/25 <br/>
     */
    @OnClick(R.id.fab)
    private void clickFab() {
        startActivity(new Intent(this, AddStatementActivity.class));
    }

    /**
     * 描 述：跳转到选择账户页面<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/16 <br/>
     */
    @OnClick(R.id.btn_choose_user_account)
    private void startChooseUserAccount() {
        // DONE: 谌珂 2016/8/16  跳转到选择账户页面
        startActivity(new Intent(this, ChoosePayerActivity.class));
    }

    /**
     * 描 述：跳转到选择账户页面<br/>
     * 作 者：谌珂<br/>
     * 历 史: (1.0.0) 谌珂 2016/8/16 <br/>
     */
    @OnClick(R.id.btn_change_money_account)
    private void changeMoneyAccount() {
        // DONE: 谌珂 2016/8/16  跳转到选择账户页面
        startActivity(new Intent(this, ChoosePayerActivity.class));
    }
}
