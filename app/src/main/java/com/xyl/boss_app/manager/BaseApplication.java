package com.xyl.boss_app.manager;

import android.app.Application;

import com.hxl.universallibrary.utils.VolleyHelper;


//import com.squareup.leakcanary.LeakCanary;


/**
 * 全局对象的封装
 *
 * @author HaoXilong
 * @data: 2015-3-13 下午1:46:15
 * @version: V1.0
 */
public class BaseApplication extends Application {
    /**
     * 全局Context
     */
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //初始化应用崩溃捕获器
        CrashHandler.getInstance().init();

        VolleyHelper.getInstance().init(this);

//        TLog.disableLog();

//        LeakCanary.install(this);
    }

    public static BaseApplication getApplication() {
        return mInstance;
    }
}
