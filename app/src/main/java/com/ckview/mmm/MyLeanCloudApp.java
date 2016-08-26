package com.ckview.mmm;

import android.os.AsyncTask;

import com.avos.avoscloud.AVOSCloud;
import com.uuzz.android.UUZZApplication;
import com.uuzz.android.util.database.DataBasesUtil;

public class MyLeanCloudApp extends UUZZApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                DataBasesUtil.getDb(MyLeanCloudApp.this);
            }
        });

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"iebF5QolJ8nhmtBQ1ueH3Uvk-gzGzoHsz","FXh8DdlEOwqfdD4o3LNi5KNX");
    }
}
