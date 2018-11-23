/*
 *          Copyright (C) 2016 jarlen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.awen.camera.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.awen.camera.R;
import com.awen.camera.crop.CropImageType;
import com.awen.camera.crop.CropImageView;
import com.awen.camera.util.BitmapUti;
import com.awen.camera.util.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


/**
 * 剪切
 *
 * @author jarlen
 */
public class ImageCropActivity extends Activity implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    private Toolbar mToolbar;

    private CropImageView cropImage;

    private String mPath = null;

    private ImageButton cancleBtn, okBtn;
    private ImageButton revoleTest;
    private Bitmap bit;
    private int width;
    private int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.inflateMenu(R.menu.menu_crop);
        mToolbar.setOnMenuItemClickListener(this);

//        Intent intent=getIntent();
//        if(intent !=null)
//        {
//            byte [] bis=intent.getByteArrayExtra("camera_path");
//             bit=BitmapFactory.decodeByteArray(bis, 0, bis.length);
//        }


        cropImage = (CropImageView) findViewById(R.id.cropmageView);

        cancleBtn = (ImageButton) findViewById(R.id.btn_cancel);
        cancleBtn.setOnClickListener(this);
        okBtn = (ImageButton) findViewById(R.id.btn_ok);
        okBtn.setOnClickListener(this);
        revoleTest = (ImageButton) findViewById(R.id.revoleTest);
        revoleTest.setOnClickListener(this);
        Bitmap hh = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.crop_button);

        cropImage.setCropOverlayCornerBitmap(hh);
        WindowManager wm = (WindowManager) this.getSystemService(
                Context.WINDOW_SERVICE);

        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        int bwidth = BitmapUti.bitmap.getWidth();
        int bHeight = BitmapUti.bitmap.getHeight();
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int height = screenWidth * bHeight / bwidth;
        ViewGroup.LayoutParams para = cropImage.getLayoutParams();
        para.height = height;
        cropImage.setLayoutParams(para);

        cropImage.setImageBitmap(BitmapUti.bitmap);

        // Bitmap bit =
        // BitmapFactory.decodeResource(this.getResources(),R.drawable.hi0);

        cropImage.setGuidelines(CropImageType.CROPIMAGE_GRID_ON_TOUCH);// 触摸时显示网格

        cropImage.setFixedAspectRatio(false);// 自由剪切

    }

    public Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = w;
            int newHeight = h;
            float scaleWight = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWight, scaleHeight);
            Bitmap res = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            return res;

        } else {
            return null;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        int i = item.getItemId();
        if (i == R.id.action_freedom) {
            cropImage.setFixedAspectRatio(false);

        } else if (i == R.id.action_1_1) {
            cropImage.setFixedAspectRatio(true);
            cropImage.setAspectRatio(10, 10);

        } else if (i == R.id.action_3_2) {
            cropImage.setFixedAspectRatio(true);
            cropImage.setAspectRatio(30, 20);

        } else if (i == R.id.action_4_3) {
            cropImage.setFixedAspectRatio(true);
            cropImage.setAspectRatio(40, 30);

        } else if (i == R.id.action_16_9) {
            cropImage.setFixedAspectRatio(true);
            cropImage.setAspectRatio(160, 90);

        } else if (i == R.id.action_rotate) {
            cropImage.rotateImage(90);

        } else if (i == R.id.action_up_down) {
            cropImage.reverseImage(CropImageType.REVERSE_TYPE.UP_DOWN);

        } else if (i == R.id.action_left_right) {
            cropImage.reverseImage(CropImageType.REVERSE_TYPE.LEFT_RIGHT);

        } else if (i == R.id.action_crop) {
            Bitmap cropImageBitmap = cropImage.getCroppedImage();
            Toast.makeText(
                    this,
                    "已保存到相册；剪切大小为 " + cropImageBitmap.getWidth() + " x "
                            + cropImageBitmap.getHeight(),
                    Toast.LENGTH_SHORT).show();
            FileUtils.saveBitmapToCamera(this, cropImageBitmap, "crop.jpg");

        }
        return false;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_cancel) {
            Intent cancelData = new Intent();
            setResult(RESULT_CANCELED, cancelData);
            this.finish();

        } else if (i == R.id.btn_ok) {
            Bitmap bitmap = cropImage.getCroppedImage();
            Log.d("reg", "剪切之后:" + bitmap.getWidth() + " x " + bitmap.getHeight());
//            FileUtils.writeImage(bitmap, mPath, 100);
            Intent okData = new Intent();
//            okData.putExtra("camera_path", mPath);
            BitmapUti.setBitmap(bitmap);
            setResult(RESULT_OK, okData);
            this.finish();

        } else if (i == R.id.revoleTest) {
            BitmapUti.bitmap = rotateImage(BitmapUti.bitmap, 90);
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(BitmapUti.bitmap.getWidth(), BitmapUti.bitmap.getHeight());
            param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
            cropImage.setLayoutParams(param);
            cropImage.setImageBitmap(BitmapUti.bitmap);
        } else {
        }
    }

    /**
     * 通过给出的bitmap进行质量压缩
     *
     * @param bitmap
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap) {
        System.out.println("bitmap==" + bitmap.getByteCount());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //通过这里改变压缩类型，其有不同的结果
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
        System.out.println("bos=====" + bos.size());
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        System.out.println("bis=====" + bis.available());
        return BitmapFactory.decodeStream(bis);
    }

    /**
     * 图片旋转
     *
     * @param bit     旋转原图像
     * @param degrees 旋转度数
     * @return 旋转之后的图像
     */
    public static Bitmap rotateImage(Bitmap bit, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap tempBitmap = Bitmap.createBitmap(bit, 0, 0, bit.getWidth(),
                bit.getHeight(), matrix, true);
        return tempBitmap;
    }

}
