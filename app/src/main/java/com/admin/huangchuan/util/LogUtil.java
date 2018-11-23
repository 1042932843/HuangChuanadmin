package com.admin.huangchuan.util;

import android.util.Log;

import com.admin.huangchuan.BuildConfig;


/**
 * 日志工具
 */
public final class LogUtil {

    private static final String TAG = "潢川";

    public static void d(Class<?> tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG + "-" + tag.getSimpleName(), msg);
        }
    }

    public static void e(Class<?> tag, String msg, Throwable e) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG + "-" + tag.getSimpleName(), msg, e);
        }
    }

    private LogUtil() {
        // 不需要被实例化
    }


}
