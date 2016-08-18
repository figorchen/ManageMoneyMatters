/**
 * 项目名称：ManageMoneyMatters <br/>
 * 文件名称: AbstractActivity.java <br/>
 * Created by 谌珂 on 2016/8/16.  <br/>
 */
package com.ckview.mmm.ui.activity;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.uuzz.android.util.PermissionUtil;
import com.uuzz.android.util.ioc.utils.InjectUtils;
import com.uuzz.android.util.log.UUZZLog;

/**
 * 项目名称：ManageMoneyMatters <br/>
 * 类  名: AbstractActivity <br/>
 * 类描述: <br/>
 * 版    本：1.0.0 <br/>
 * 修改时间：2016/8/16 <br/>
 * @author 谌珂 <br/>
 */
public class AbstractActivity extends Activity {

    protected UUZZLog logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logger = new UUZZLog(getClass());
        InjectUtils.injectContentView(this);
        InjectUtils.injectViews(getWindow().getDecorView(), this);
        InjectUtils.injectEvents(this);
    }

    /**
     * 描 述：用于在activity中申请权限<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/7/12 注释 <br/>
     * @param permissions 申请的权限数组
     * @param task 申请权限成功后需要执行的任务
     */
    public void checkPromissions(@NonNull String[]permissions, Runnable task) {
        PermissionUtil.checkPermission(this, permissions, task, null);
    }

    /**
     * 描 述：用于在activity中申请权限<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/7/12 注释 <br/>
     * @param permissions 申请的权限数组
     * @param accessTask 申请权限成功后需要执行的任务
     * @param deniedTask 申请权限被拒绝后需要执行的任务
     */
    public void checkPromissions(@NonNull String[]permissions, Runnable accessTask, Runnable deniedTask) {
        PermissionUtil.checkPermission(this, permissions, accessTask, deniedTask);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.runTask(this, requestCode, permissions, grantResults);
    }
}