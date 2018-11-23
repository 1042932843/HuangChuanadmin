package com.awen.camera.util;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/12/15 0015.
 */
public class BitmapUti {
    public static Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public static void setBitmap(Bitmap bitmap) {
        BitmapUti.bitmap = bitmap;
    }
}
