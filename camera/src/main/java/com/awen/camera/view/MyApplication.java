package com.awen.camera.view;

import android.app.Application;

import java.io.File;

/**
 * Created by zhaoshuang on 17/2/8.
 */

public class MyApplication extends Application {

    public static String VIDEO_PATH =  "/sdcard/RecordedDemo/";

    @Override
    public void onCreate() {

        VIDEO_PATH += String.valueOf(System.currentTimeMillis());
        File file = new File(VIDEO_PATH);
        if(!file.exists()) file.mkdirs();

    }
}
