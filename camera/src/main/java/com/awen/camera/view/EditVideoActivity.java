package com.awen.camera.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.awen.camera.R;
import com.awen.camera.util.BitmapUti;
import com.awen.camera.util.PhotoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhaoshuang on 17/2/21.
 */

public class EditVideoActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_color;
    private LinearLayout edit_text_color;

    private int[] drawableBg = new int[]{R.drawable.color1, R.drawable.color2, R.drawable.color3, R.drawable.color4, R.drawable.color5};
    private int[] drawableBg1 = new int[]{R.drawable.color1, R.drawable.color2, R.drawable.color3, R.drawable.color4, R.drawable.color5};
    private int[] colors = new int[]{R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5};
    private int[] colors1 = new int[]{R.color.color1, R.color.color2, R.color.color3, R.color.color4, R.color.color5};
    private int[] expressions = new int[]{R.drawable.expression1, R.drawable.expression2, R.drawable.expression3, R.drawable.expression4,
            R.drawable.expression5, R.drawable.expression6, R.drawable.expression7, R.drawable.expression8};
    private int dp20;
    private int dp25;
    public final static String RESULT_PHOTO_PATH = "photoPath";
    private int currentColorPosition;
    private int currentColorPosition1;
    private TuyaView tv_video;
    private ImageView iv_pen;
    private ImageView iv_icon;
    private RelativeLayout rl_expression;
    private RelativeLayout rl_touch_view;
    private RelativeLayout rl_edit_text;
    private RelativeLayout xuanzhuan_text;
    private EditText et_tag;
    private TextView tv_tag;
    private InputMethodManager manager;
    private int windowHeight;
    private int windowWidth;
    //    private String path;
    private RelativeLayout rl_tuya;
    private RelativeLayout rl_close;
    private RelativeLayout rl_title;
    private RelativeLayout rl_bottom;
    private RelativeLayout jianqie_text;
    private RelativeLayout include;
    private TextView tv_hint_delete;
    private int dp100;
    /* 剪切 */
    private static final int PHOTO_CROP_WITH_DATA = 3027;
    //    private Toolbar mToolbar;
//    private CropImageView cropImage;

    //    private ImageButton cancleBtn, okBtn;
    private Button revoleTest;
    private Bitmap bitmap;
    private int width;
    private int height;
    private int mCurrentPosition;
    private ImageView showTakePhotoImg;
    ProgressDialog dialog = null;
    int o = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_video);
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.inflateMenu(R.menu.menu_crop);
//        mToolbar.setOnMenuItemClickListener(this);
        manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        windowWidth = getWindowManager().getDefaultDisplay().getWidth();
        windowHeight = getWindowManager().getDefaultDisplay().getHeight();
        dp100 = (int) getResources().getDimension(R.dimen.dp100);

        initUI();

        Intent intent = getIntent();
        String path = intent.getStringExtra("path");

//        width = intent.getIntExtra("width", 0);
//        height = intent.getIntExtra("height", 0);

        WindowManager wm = (WindowManager) this.getSystemService(
                Context.WINDOW_SERVICE);

        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        mCurrentPosition = intent.getIntExtra("position", 0);
        if (null != path) {
            int screenWidth = getScreenWidth(this);
            float ratio = (float) NativeUtil.getBitmapFromFile(path).getWidth() / (float) NativeUtil.getBitmapFromFile(path).getHeight();
            height = (int) (screenWidth / ratio);
            ViewGroup.LayoutParams params = showTakePhotoImg.getLayoutParams();
            params.height = height;
            params.width = screenWidth;
            tv_video.setLayoutParams(params);
            rl_touch_view.setLayoutParams(params);
            showTakePhotoImg.setImageBitmap(resizeBitmap(NativeUtil.getBitmapFromFile(path), width, height));
        } else {
//            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(BitmapUti.bitmap.getWidth(), BitmapUti.bitmap.getHeight());
//            param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
//            rl_tuya.setLayoutParams(param);
//            int bwidth = BitmapUti.bitmap.getWidth();
//            int bHeight = BitmapUti.bitmap.getHeight();
//            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//            int height = screenWidth * bHeight / bwidth;
//            ViewGroup.LayoutParams para = showTakePhotoImg.getLayoutParams();
//            para.height = height;
//            showTakePhotoImg.setLayoutParams(para);
            int screenWidth = getScreenWidth(this);
            float ratio = (float) BitmapUti.bitmap.getWidth() / (float) BitmapUti.bitmap.getHeight();
            height = (int) (screenWidth / ratio);
            ViewGroup.LayoutParams params = showTakePhotoImg.getLayoutParams();
            params.height = height;
            params.width = screenWidth;
            showTakePhotoImg.setLayoutParams(params);
            tv_video.setLayoutParams(params);
            rl_touch_view.setLayoutParams(params);

//            rl_touch_view.setLayoutParams(para);
//            rl_tuya.setBackgroundDrawable(new BitmapDrawable(BitmapUti.bitmap));
//            showTakePhotoImg.setBackgroundDrawable(new BitmapDrawable(BitmapUti.bitmap));
            showTakePhotoImg.setImageBitmap(BitmapUti.bitmap);
        }


        //当进行涂鸦操作时, 隐藏标题栏和底部工具栏
        tv_video.setOnTouchListener(new TuyaView.OnTouchListener() {
            @Override
            public void onDown() {
                changeMode(false);
            }

            @Override
            public void onUp() {
                changeMode(true);
            }
        });
    }


    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    private void initUI() {
        showTakePhotoImg = (ImageView) findViewById(R.id.showTakePhotoImg);
//        cropImage = (CropImageView) findViewById(R.id.cropmageView);
//        cancleBtn = (ImageButton) findViewById(R.id.btn_cancel);
//        cancleBtn.setOnClickListener(this);
//        okBtn = (ImageButton) findViewById(R.id.btn_ok);
//        okBtn.setOnClickListener(this);
//        revoleTest = (Button) findViewById(R.id.revoleTest);
//        revoleTest.setOnClickListener(this);
//        cropImage.setGuidelines(CropImageType.CROPIMAGE_GRID_ON_TOUCH);// 触摸时显示网格
//        cropImage.setFixedAspectRatio(false);// 自由剪切
        RelativeLayout rv_pen = (RelativeLayout) findViewById(R.id.rv_pen);
        RelativeLayout rv_icon = (RelativeLayout) findViewById(R.id.rv_icon);
        RelativeLayout rv_text = (RelativeLayout) findViewById(R.id.rv_text);
        ll_color = (LinearLayout) findViewById(R.id.ll_color);
        edit_text_color = (LinearLayout) findViewById(R.id.edit_text_color);
        tv_video = (TuyaView) findViewById(R.id.tv_video);
        rl_expression = (RelativeLayout) findViewById(R.id.rl_expression);
        rl_touch_view = (RelativeLayout) findViewById(R.id.rl_touch_view);
        TextView tv_close = (TextView) findViewById(R.id.tv_close);
        TextView tv_finish = (TextView) findViewById(R.id.tv_finish);
        rl_edit_text = (RelativeLayout) findViewById(R.id.rl_edit_text);
        et_tag = (EditText) findViewById(R.id.et_tag);
        tv_tag = (TextView) findViewById(R.id.tv_tag);
        TextView tv_finish_video = (TextView) findViewById(R.id.tv_finish_video);
        iv_pen = (ImageView) findViewById(R.id.iv_pen);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        rl_tuya = (RelativeLayout) findViewById(R.id.rl_tuya);
        rl_close = (RelativeLayout) findViewById(R.id.rl_close);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        jianqie_text = (RelativeLayout) findViewById(R.id.jianqie_text);
        include = (RelativeLayout) findViewById(R.id.include);
        xuanzhuan_text = (RelativeLayout) findViewById(R.id.xuanzhuan_text);
        tv_hint_delete = (TextView) findViewById(R.id.tv_hint_delete);

        RelativeLayout rl_back = (RelativeLayout) findViewById(R.id.rl_back);

        rv_pen.setOnClickListener(this);
        rv_icon.setOnClickListener(this);
        rv_text.setOnClickListener(this);

        rl_back.setOnClickListener(this);
        ll_color.setOnClickListener(this);
        xuanzhuan_text.setOnClickListener(this);
        edit_text_color.setOnClickListener(this);
        tv_close.setOnClickListener(this);
        tv_finish.setOnClickListener(this);
        tv_finish_video.setOnClickListener(this);
        rl_close.setOnClickListener(this);
        jianqie_text.setOnClickListener(this);

        initColors();
        initColors1();
        initExpression();

        et_tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_tag.setText(s.toString());
            }
        });
    }

    //更改界面模式
    private void changeMode(boolean flag) {
        if (flag) {
            rl_title.setVisibility(View.VISIBLE);
            rl_bottom.setVisibility(View.VISIBLE);
        } else {
            rl_title.setVisibility(View.GONE);
            rl_bottom.setVisibility(View.GONE);
        }
    }

//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//
//        int i = item.getItemId();
//        if (i == R.id.action_freedom) {
//            cropImage.setFixedAspectRatio(false);
//
//        } else if (i == R.id.action_1_1) {
//            cropImage.setFixedAspectRatio(true);
//            cropImage.setAspectRatio(10, 10);
//
//        } else if (i == R.id.action_3_2) {
//            cropImage.setFixedAspectRatio(true);
//            cropImage.setAspectRatio(30, 20);
//
//        } else if (i == R.id.action_4_3) {
//            cropImage.setFixedAspectRatio(true);
//            cropImage.setAspectRatio(40, 30);
//
//        } else if (i == R.id.action_16_9) {
//            cropImage.setFixedAspectRatio(true);
//            cropImage.setAspectRatio(160, 90);
//
//        } else if (i == R.id.action_rotate) {
//            cropImage.rotateImage(90);
//
//        } else if (i == R.id.action_up_down) {
//            cropImage.reverseImage(CropImageType.REVERSE_TYPE.UP_DOWN);
//
//        } else if (i == R.id.action_left_right) {
//            cropImage.reverseImage(CropImageType.REVERSE_TYPE.LEFT_RIGHT);
//
//        } else if (i == R.id.action_crop) {
//            Bitmap cropImageBitmap = cropImage.getCroppedImage();
//            Toast.makeText(
//                    this,
//                    "已保存到相册；剪切大小为 " + cropImageBitmap.getWidth() + " x "
//                            + cropImageBitmap.getHeight(),
//                    Toast.LENGTH_SHORT).show();
//            FileUtils.saveBitmapToCamera(this, cropImageBitmap, "crop.jpg");
//
//        }
//        return false;
//    }

    private void initExpression() {

        int dp80 = (int) getResources().getDimension(R.dimen.dp80);
        int dp10 = (int) getResources().getDimension(R.dimen.dp10);
        for (int x = 0; x < expressions.length; x++) {
            ImageView imageView = new ImageView(this);
            imageView.setPadding(dp10, dp10, dp10, dp10);
            final int result = expressions[x];
            imageView.setImageResource(result);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(windowWidth / 4, dp80));
            imageView.setX(x % 4 * windowWidth / 4);
            imageView.setY(x / 4 * dp80);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rl_expression.setVisibility(View.GONE);
                    iv_icon.setImageResource(R.drawable.icon);
                    addExpressionToWindow(result);
                }
            });
            rl_expression.addView(imageView);
        }
    }

    /**
     * 添加表情到界面上
     */
    private void addExpressionToWindow(int result) {

        TouchView touchView = new TouchView(this);
        touchView.setBackgroundResource(result);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(dp100, dp100);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        touchView.setLayoutParams(layoutParams);

        touchView.setLimitsX(0, windowWidth);
        touchView.setLimitsY(0, windowHeight - dp100 / 2);
        touchView.setOnLimitsListener(new TouchView.OnLimitsListener() {
            @Override
            public void OnOutLimits(float x, float y) {
                tv_hint_delete.setTextColor(Color.RED);
            }

            @Override
            public void OnInnerLimits(float x, float y) {
                tv_hint_delete.setTextColor(Color.WHITE);
            }
        });
        touchView.setOnTouchListener(new TouchView.OnTouchListener() {
            @Override
            public void onDown(TouchView view, MotionEvent event) {
                tv_hint_delete.setVisibility(View.VISIBLE);
                changeMode(false);
            }

            @Override
            public void onMove(TouchView view, MotionEvent event) {

            }

            @Override
            public void onUp(TouchView view, MotionEvent event) {
                tv_hint_delete.setVisibility(View.GONE);
                changeMode(true);
                if (view.isOutLimits()) {
                    rl_touch_view.removeView(view);
                }
            }
        });

        rl_touch_view.addView(touchView);
    }

    /**
     * 添加文字到界面上
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void addTextToWindow() {

        TouchView touchView = new TouchView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tv_tag.getWidth(), tv_tag.getHeight());
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        touchView.setLayoutParams(layoutParams);
        Bitmap bitmap = Bitmap.createBitmap(tv_tag.getWidth(), tv_tag.getHeight(), Bitmap.Config.ARGB_8888);
        tv_tag.draw(new Canvas(bitmap));
        touchView.setBackground(new BitmapDrawable(bitmap));

        touchView.setLimitsX(0, windowWidth);
        touchView.setLimitsY(0, windowHeight - dp100 / 2);
        touchView.setOnLimitsListener(new TouchView.OnLimitsListener() {
            @Override
            public void OnOutLimits(float x, float y) {
                tv_hint_delete.setTextColor(Color.RED);
            }

            @Override
            public void OnInnerLimits(float x, float y) {
                tv_hint_delete.setTextColor(Color.WHITE);

            }
        });
        touchView.setOnTouchListener(new TouchView.OnTouchListener() {
            @Override
            public void onDown(TouchView view, MotionEvent event) {
                tv_hint_delete.setVisibility(View.VISIBLE);
                changeMode(false);
            }

            @Override
            public void onMove(TouchView view, MotionEvent event) {

            }

            @Override
            public void onUp(TouchView view, MotionEvent event) {
                tv_hint_delete.setVisibility(View.GONE);
                changeMode(true);
                if (view.isOutLimits()) {
                    rl_touch_view.removeView(view);
                }
            }
        });

        rl_touch_view.addView(touchView);

        et_tag.setText("");
        tv_tag.setText("");
    }

    /**
     * 初始化底部颜色选择器
     */
    private void initColors() {

        dp20 = (int) getResources().getDimension(R.dimen.dp20);
        dp25 = (int) getResources().getDimension(R.dimen.dp25);

        for (int x = 0; x < drawableBg.length; x++) {
            RelativeLayout relativeLayout = new RelativeLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            relativeLayout.setLayoutParams(layoutParams);

            View view = new View(this);
            view.setBackgroundDrawable(getResources().getDrawable(drawableBg[x]));
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(dp20, dp20);
            layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
            view.setLayoutParams(layoutParams1);
            relativeLayout.addView(view);

            final View view2 = new View(this);
            view2.setBackgroundResource(R.drawable.color_click);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(dp25, dp25);
            layoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
            view2.setLayoutParams(layoutParams2);
            if (x != 0) {
                view2.setVisibility(View.GONE);
            }
            relativeLayout.addView(view2);

            final int position = x;
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentColorPosition != position) {
                        view2.setVisibility(View.VISIBLE);
                        ViewGroup parent = (ViewGroup) v.getParent();
                        ViewGroup childView = (ViewGroup) parent.getChildAt(currentColorPosition);
                        childView.getChildAt(1).setVisibility(View.GONE);
                        tv_video.setNewPaintColor(getResources().getColor(colors[position]));
                        currentColorPosition = position;
                    }
                }
            });

            ll_color.addView(relativeLayout, x);
        }
    }

    /**
     * 初始化底部颜色选择器
     */
    private void initColors1() {

        dp20 = (int) getResources().getDimension(R.dimen.dp20);
        dp25 = (int) getResources().getDimension(R.dimen.dp25);

        for (int x = 0; x < drawableBg1.length; x++) {
            RelativeLayout relativeLayout = new RelativeLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            relativeLayout.setLayoutParams(layoutParams);

            View view = new View(this);
            view.setBackgroundDrawable(getResources().getDrawable(drawableBg1[x]));
            RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(dp20, dp20);
            layoutParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
            view.setLayoutParams(layoutParams1);
            relativeLayout.addView(view);

            final View view2 = new View(this);
            view2.setBackgroundResource(R.drawable.color_click);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(dp25, dp25);
            layoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
            view2.setLayoutParams(layoutParams2);
            if (x != 0) {
                view2.setVisibility(View.GONE);
            }
            relativeLayout.addView(view2);

            final int position = x;
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentColorPosition1 != position) {
                        view2.setVisibility(View.VISIBLE);
                        ViewGroup parent = (ViewGroup) v.getParent();
                        ViewGroup childView = (ViewGroup) parent.getChildAt(currentColorPosition1);
                        childView.getChildAt(1).setVisibility(View.GONE);
                        et_tag.setTextColor(getResources().getColor(colors1[position]));
                        tv_tag.setTextColor(getResources().getColor(colors1[position]));
                        currentColorPosition1 = position;
                    }
                }
            });

            edit_text_color.addView(relativeLayout, x);
        }
    }

    boolean isFirstShowEditText;

    /**
     * 弹出键盘
     */
    public void popupEditText() {

        isFirstShowEditText = true;
        et_tag.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isFirstShowEditText) {
                    isFirstShowEditText = false;
                    et_tag.setFocusable(true);
                    et_tag.setFocusableInTouchMode(true);
                    et_tag.requestFocus();
                    isFirstShowEditText = !manager.showSoftInput(et_tag, 0);
                }
            }
        });
    }

    /**
     * 执行文字编辑区域动画
     */
    private void startAnim(float start, float end, AnimatorListenerAdapter listenerAdapter) {

        ValueAnimator va = ValueAnimator.ofFloat(start, end).setDuration(200);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                rl_edit_text.setY(value);
            }
        });
        if (listenerAdapter != null) va.addListener(listenerAdapter);
        va.start();
    }

    /**
     * 更改画笔状态的界面
     */
    private void changePenState(boolean flag) {

        if (flag) {
            tv_video.setDrawMode(flag);
            tv_video.setNewPaintColor(getResources().getColor(colors[currentColorPosition]));
            iv_pen.setImageResource(R.drawable.pen_click);
            ll_color.setVisibility(View.VISIBLE);
        } else {
            tv_video.setDrawMode(flag);
            ll_color.setVisibility(View.GONE);
            iv_pen.setImageResource(R.drawable.pen);
        }
    }

    /**
     * 更改表情状态的界面
     */
    private void changeIconState(boolean flag) {

        if (flag) {
            iv_icon.setImageResource(R.drawable.icon_click);
            rl_expression.setVisibility(View.VISIBLE);
        } else {
            rl_expression.setVisibility(View.GONE);
            iv_icon.setImageResource(R.drawable.icon);
        }
    }

    /**
     * 更改文字输入状态的界面
     */
    private void changeTextState(boolean flag) {

        if (flag) {
            rl_edit_text.setY(windowHeight);
            edit_text_color.setVisibility(View.VISIBLE);
            rl_edit_text.setVisibility(View.VISIBLE);
            startAnim(rl_edit_text.getY(), 0, null);
            popupEditText();
        } else {
            manager.hideSoftInputFromWindow(et_tag.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            startAnim(0, windowHeight, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rl_edit_text.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


//        if (!TextUtils.isEmpty(path)) {
//            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//            ContentResolver mContentResolver = EditVideoActivity.this.getContentResolver();
//            String where = MediaStore.Images.Media.DATA + "='" + path + "'";
////删除图片
//            mContentResolver.delete(uri, where, null);
////发送广播
//            Intent  intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//            File file = new File(path);
//            uri = Uri.fromFile(file);
//            intent.setData(uri);
//            EditVideoActivity.this.sendBroadcast(intent);
//        }
    }

    /**
     * 保存图片
     */
    private void mergeImage() {

        //得到涂鸦view的bitmap图片
        Bitmap bitmap = Bitmap.createBitmap(rl_tuya.getWidth(), rl_tuya.getHeight(), Bitmap.Config.ARGB_8888);
        rl_tuya.draw(new Canvas(bitmap));
        //这步是根据视频尺寸来调整图片宽高,和视频保持一致
//        Matrix matrix = new Matrix();
//        matrix.postScale(ScreenSizeUtil.getScreenWidth() * 1f / bitmap.getWidth(), ScreenSizeUtil.getScreenHeight() * 1f / bitmap.getHeight());
//        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        String strImgPath = Environment.getExternalStorageDirectory()
                .toString() + "/Huangchuan_tuya/";
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()) + ".jpg";// 照片以格式化日期方式命名
        File ou = new File(strImgPath);
        if (!ou.exists()) {
            ou.mkdirs();
        }
        File f = new File(strImgPath, fileName);
//        if (f.exists()) {
//            f.delete();
//        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.d("reg", "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        String fi = String.valueOf(f);
        ScannerByReceiver(this, fi);//图库扫描
        intent.putExtra(RESULT_PHOTO_PATH, fi);
        intent.putExtra("Position", mCurrentPosition);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * Receiver扫描更新图库图片
     **/
    private static void ScannerByReceiver(Context context, String path) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + path)));
    }

    public void onResult(String path) {
        Log.d("reg", "path:" + path);
        setResult(RESULT_OK, new Intent().putExtra(RESULT_PHOTO_PATH, path));
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(RESULT_PHOTO_PATH, "");
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.rv_pen) {
            changePenState(!(ll_color.getVisibility() == View.VISIBLE));
            changeIconState(false);
            changeTextState(false);

        } else if (i == R.id.rv_icon) {
            changeIconState(!(rl_expression.getVisibility() == View.VISIBLE));
            changePenState(false);
            changeTextState(false);

        } else if (i == R.id.rv_text) {
            changeTextState(!(rl_edit_text.getVisibility() == View.VISIBLE));
            changePenState(false);
            changeIconState(false);

        } else if (i == R.id.rl_back) {
            tv_video.backPath();

        } else if (i == R.id.tv_close) {
            changeTextState(!(rl_edit_text.getVisibility() == View.VISIBLE));

        } else if (i == R.id.tv_finish) {
            changeTextState(!(rl_edit_text.getVisibility() == View.VISIBLE));
            if (et_tag.getText().length() > 0) {
                addTextToWindow();
            }

        } else if (i == R.id.tv_finish_video) {
            dialog = ProgressDialog.show(this, null, "页面加载中，请稍后..");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mergeImage();
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        } else if (i == R.id.rl_close) {
            onBackPressed();

        } else if (i == R.id.jianqie_text) {
//            include.setVisibility(View.VISIBLE);
            //得到涂鸦view的bitmap图片

            bitmap = Bitmap.createBitmap(rl_tuya.getWidth(), rl_tuya.getHeight(), Bitmap.Config.ARGB_4444);
            rl_tuya.draw(new Canvas(bitmap));
//            bitmap = ((BitmapDrawable) showTakePhotoImg.getDrawable()).getBitmap();

            //这步是根据视频尺寸来调整图片宽高,和视频保持一致
//            Matrix matrix = new Matrix();
//            matrix.postScale(rl_tuya.getWidth() * 1f / bitmap.getWidth(), rl_tuya.getHeight() * 1f / bitmap.getHeight());
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            Bitmap hh = BitmapFactory.decodeResource(this.getResources(), R.drawable.crop_button);
//            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
//            param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
//            cropImage.setLayoutParams(param);
//            int bwidth = bitmap.getWidth();
//            int bHeight = bitmap.getHeight();
//            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//            int height = screenWidth * bHeight / bwidth;
//            ViewGroup.LayoutParams para = cropImage.getLayoutParams();
//            para.height = height;
//            cropImage.setLayoutParams(para);
//            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
//            param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
//            cropImage.setLayoutParams(param);
//            cropImage.setCropOverlayCornerBitmap(hh);
//            cropImage.setImageBitmap(bitmap);


            // 将图片路径photoPath传到所要调试的模块
            Intent photoFrameIntent = new Intent(EditVideoActivity.this,
                    ImageCropActivity.class);
            BitmapUti.setBitmap(bitmap);
            startActivityForResult(photoFrameIntent, PHOTO_CROP_WITH_DATA);

            Log.d("reg", "剪切之前:" + bitmap.getWidth() + " x " + bitmap.getHeight());
        }
        if (i == R.id.btn_cancel) {
//            Intent cancelData = new Intent();
//            setResult(RESULT_CANCELED, cancelData);
//            this.finish();
            include.setVisibility(View.GONE);
        } else if (i == R.id.btn_ok) {
            tv_video.backPa();
            rl_touch_view.removeAllViews();
            include.setVisibility(View.GONE);
//            bitmap = cropImage.getCroppedImage();
            Log.d("reg", "剪切大小为:" + bitmap.getWidth() + " x " + bitmap.getHeight());
//            rl_tuya.setBackgroundDrawable(new BitmapDrawable((bitmap)));
            showTakePhotoImg.setImageBitmap(bitmap);
        } else if (i == R.id.xuanzhuan_text) {
//            bitmap = Bitmap.createBitmap(rl_tuya.getWidth(), rl_tuya.getHeight(), Bitmap.Config.ARGB_8888);
            bitmap = ((BitmapDrawable) showTakePhotoImg.getDrawable()).getBitmap();
            bitmap = PhotoUtils.rotateImage(bitmap, 90);
//            int bHeight = bitmap.getHeight();
//            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
//            int height = screenWidth * bHeight;
//            ViewGroup.LayoutParams para = rl_tuya.getLayoutParams();
//            para.height = height;
//            rl_tuya.setLayoutParams(para);
//            Matrix matrix = new Matrix();
//            matrix.postScale(rl_tuya.getWidth() * 1f / bitmap.getWidth(), rl_tuya.getHeight() * 1f / bitmap.getHeight());
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            WindowManager wm = (WindowManager) this.getSystemService(
                    Context.WINDOW_SERVICE);

            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();

            showTakePhotoImg.setImageBitmap(resizeBitmap(bitmap, width, height));


            int t = o++;
            if (t == 1) {
                tv_video.setRotation(90);
            } else if (t == 2) {
                tv_video.setRotation(180);
            } else if (t == 3) {
                tv_video.setRotation(270);
            } else if (t == 4) {
                tv_video.setRotation(360);
                o = 1;

            }
//            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(bitmap.getWidth(), bitmap.getHeight());
//            param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 1);
//            showTakePhotoImg.setLayoutParams(param);
//            tv_video.setLayoutParams(param);
//            rl_touch_view.setLayoutParams(param);
//            rl_tuya.setLayoutParams(param);

            Log.d("reg", "旋转宽高:" + bitmap.getWidth() + " x " + bitmap.getHeight());
            Log.d("reg", "rl_tuya宽高:" + rl_tuya.getWidth() + " x " + rl_tuya.getHeight());
            Log.d("reg", "--------------");

//            tv_video.backPa();
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PHOTO_CROP_WITH_DATA:
                tv_video.backPa();
                rl_touch_view.removeAllViews();
//                String resultPath = data.getStringExtra("camera_path");
//                Bitmap resultBitmap = BitmapFactory.decodeFile(resultPath);
//                rl_tuya.setImageBitmap(resultBitmap);
//                rl_tuya.setBackgroundDrawable(Drawable.createFromPath(resultPath));
                int bwidth = BitmapUti.bitmap.getWidth();
                int bHeight = BitmapUti.bitmap.getHeight();
                int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
                int height = screenWidth * bHeight / bwidth;
                ViewGroup.LayoutParams para = showTakePhotoImg.getLayoutParams();
                para.height = height;
                showTakePhotoImg.setLayoutParams(para);
                showTakePhotoImg.setImageBitmap(BitmapUti.bitmap);
                break;

            default:
                break;
        }

    }
}
