package com.example.test;

import android.app.Application;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by xs on 2015/1/31.
 */
public class MainApplication extends Application {
    private static final String TAG = "JPush";

    @Override
    public void onCreate() {
        Log.d(TAG, "[MainApplication] onCreate");
        super.onCreate();

        JPushInterface.setDebugMode(false); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }
}
