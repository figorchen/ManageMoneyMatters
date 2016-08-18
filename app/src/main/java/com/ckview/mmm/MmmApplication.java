package com.ckview.mmm;

import android.os.AsyncTask;

import com.uuzz.android.UUZZApplication;
import com.uuzz.android.util.database.DataBasesUtil;

public class MmmApplication extends UUZZApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                DataBasesUtil.getDb(MmmApplication.this);
            }
        });
    }
}
