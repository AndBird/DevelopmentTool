package com.development.tool.test;

import com.android_development.tool.DebugUtils;

import android.app.Application;

/**
 * Created by Administrator on 2016/4/11.
 */
public class ToolBaseApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        DebugUtils.setLog(true, false);//日志开关
        DebugUtils.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DebugUtils.exitDebug();
    }
}
