/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.edmodo.cropper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片剪裁
 */
public class CropImageActivity extends Activity implements View.OnClickListener {

    public static final int REQUEST_CODE = 'c' + 'r' + 'o' + 'p';
    public static final String REQUEST_PATH = "path";
    public static final String RESULT_PATH = "path";

    private ImageView btnBack;
    private Button commit;
    private CropImageView cropImageView;

    private String path;

    private void assignViews() {
        btnBack = (ImageView) findViewById(R.id.btn_back);
        commit = (Button) findViewById(R.id.commit);
        cropImageView = (CropImageView) findViewById(R.id.cropImageView);
    }

    private void initViews(String path) {
        btnBack.setOnClickListener(this);
        commit.setOnClickListener(this);
        cropImageView.setAspectRatio(15, 21);
        cropImageView.setGuidelines(1);
        cropImageView.setFixedAspectRatio(true);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        if (options.outWidth > 1280 || options.outHeight > 1280) {
            options.inSampleSize = Math.max(options.outWidth, options.outHeight) / 1280;
        }
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        cropImageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        path = intent.getStringExtra(REQUEST_PATH);

        if (path != null) {
            setContentView(R.layout.activity_crop_image);
            assignViews();
            initViews(path);
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            finish();
        } else if (id == R.id.commit) {
            commit();
        }
    }

    private void commit() {
        FileOutputStream stream = null;
        try {
            Intent intent = new Intent();
            File file = new File(path);
            file = new File(file.getParent(), "crop_" + file.getName());
            String output = file.getAbsolutePath();
            intent.putExtra(RESULT_PATH, output);
            stream = new FileOutputStream(output);
            Bitmap croppedImage = cropImageView.getCroppedImage();
            croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, stream);
            croppedImage.recycle();
            setResult(RESULT_OK, intent);
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
