/**
 * 项目名称：手机大管家 <br/>
 * 文件名称: PermissionUtil.java <br/>
 * <p>
 * Created by 谌珂 on 2016/7/11.  <br/>
 * Copyright 2011 北京壹平台科技有限公司. All rights reserved.[版权声明]
 */
package com.uuzz.android.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.uuzz.android.R;
import com.uuzz.android.ui.activity.UUZZActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：手机大管家 <br/>
 * 类  名: PermissionUtil <br/>
 * 类描述: <br/>
 * 实现的主要功能 <br/>
 * 版    本：1.2.2 <br/>
 * 修改时间：2016/7/11 <br/>
 * @author 谌珂 <br/>
 */
public class PermissionUtil {

    /** 申请成功后需要执行的任务列表 */
    private static Map<Integer, Runnable> mAccessTasks = new HashMap<>();
    /** 被拒绝后的执行任务列表 */
    private static Map<Integer, Runnable> mDeniedTasks = new HashMap<>();

    /**
     * 描 述：验证权限是否申请通过<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/7/11 注释 <br/>
     * @param context 上下文
     * @param permissions 申请的权限数组
     * @param accessTask 申请权限成功后需要执行的任务
     */
    public static void checkPermission(@NonNull Context context, @NonNull String[]permissions, Runnable accessTask, Runnable deniedTask) {
        boolean allApplied = true;
        boolean isActivity = UUZZActivity.class.isAssignableFrom(context.getClass());
        List<String> notAppliedPermissions = null;
        for (String permission : permissions) {      //遍历每一条权限是否都已获取，如果有没获取的则申请权限
            if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, permission)) {
                allApplied = false;
                if (!isActivity) {                          //如果Context不是继承与UUZZActivity则不为申请权限做准备
                    continue;
                }
                if (notAppliedPermissions == null) {
                    notAppliedPermissions = new ArrayList<>();
                }
                notAppliedPermissions.add(permission);         //把没有申请过的权限加入列表，方便通知用户
            }
        }

        if (allApplied) {
            accessTask.run();
        } else if(isActivity) {
            // DONE: 谌珂 2016/7/11 申请权限并把任务存入 mTasks
            //根据请求权限的数据和当前系统时间戳生成特征码
            int requestCode = (Arrays.toString(permissions) +String.valueOf(System.currentTimeMillis())).hashCode() & 0x00FF;
            mAccessTasks.put(requestCode, accessTask);
            mDeniedTasks.put(requestCode, deniedTask);

            //申请获取权限
            ActivityCompat.requestPermissions((UUZZActivity)context, notAppliedPermissions.toArray(new String[notAppliedPermissions.size()]), requestCode);
        } else if(deniedTask != null) {
            deniedTask.run();
        }
    }

    /**
     * 描 述：申请权限后执行任务<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/7/12 注释 <br/>
     * @param context 上下文
     * @param requestCode 权限申请的code，详情看{@link #checkPermission}
     * @param permissions 申请的权限数组
     * @param grantResults 授权结果的数组
     */
    public static void runTask(@Nullable Context context, int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        Runnable accessTask = mAccessTasks.remove(requestCode);
        Runnable deniedTask = mDeniedTasks.remove(requestCode);
        if(accessTask == null) {
            return;
        }

        boolean allApplied = true;
        // 如果权限请求被用户打断则会接收到一个长度为0的结果数组
        if(grantResults.length == 0 || permissions.length == 0) {
            return;
        }
        for (int i = 0; i < grantResults.length; i++) {
            if(grantResults[i] == PackageManager.PERMISSION_DENIED) {        //有被拒绝的权限，修改权限申请标记并显示提示语
                allApplied = false;
                if(context == null) {
                    continue;
                }
                Toaster.showShortToast(context, getTip(context, permissions[i]));
            }
        }

        if(allApplied) {
            accessTask.run();
        } else if(deniedTask != null) {
            deniedTask.run();
        }
    }

    /**
     * 描 述：根据申请权限内容获取权限被拒绝后的提示语<br/>
     * 作 者：谌珂<br/>
     * 历 史: (版本) 谌珂 2016/7/12 注释 <br/>
     * @param context 上下文
     * @param permission 申请的权限{@link Manifest.permission}
     * @return 权限被拒绝后的提示语
     */
    private static String getTip(@NonNull Context context, String permission) {
        // DONE: 谌珂 2016/7/12 编写权限申请提示语
        Resources r = context.getResources();
        String value;
        try {
            Class resourceClass = Class.forName("com.uuzz.android.R$string");
            Field field = resourceClass.getDeclaredField(permission.replace(".", "_"));
            value = r.getString((Integer) field.get(null));
        } catch (Exception e) {
            value = "";
        }
        return String.format(r.getString(R.string.permission_denied), value);
    }
}
