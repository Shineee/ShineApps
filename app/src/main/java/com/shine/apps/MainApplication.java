package com.shine.apps;

import android.app.Application;
import android.content.Context;

import com.hjq.permissions.XXPermissions;

/**
 * <strong>这里写类的摘要</strong>
 * <p>
 * 这里写类的详细描述
 *
 * @author wangyang
 * @version 1.0
 * @email wangyang@meiweishenghuo.com
 * @modified 4/13/21
 * @since 4/13/21
 */

public class MainApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        // 当前项目是否已经适配了分区存储的特性
        XXPermissions.setScopedStorage(true);
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }
}
