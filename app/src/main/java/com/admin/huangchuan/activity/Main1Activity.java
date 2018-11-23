package com.admin.huangchuan.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.admin.huangchuan.util.RichEditor;
import com.admin.huangchuan.R;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.util.Constant;
import com.admin.huangchuan.util.SingleFileLimitInterceptor;
import com.admin.huangchuan.util.SizeUtil;
import com.imnjh.imagepicker.SImagePicker;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@SuppressLint("SetJavaScriptEnabled")
public class Main1Activity extends BaseActivity {
    public static final int REQUEST_CODE = 'm' + 'a' + 'i' + 'n' + '1';
    public static final String REQUEST_DETAIL = "newsDetail";
    private ArrayList<String> imglist;
    private RichEditor mEditor;
    private TextView mPreview;
    private Handler handler = new Handler();
    private static final int REQUEST_CODE_EDIT_LINKED = 2001;//编辑链接
    private static final int REQUEST_CODE_IMAGE = 101;
    private TextView txt_right;
    private ImageView img_back;
    private RelativeLayout rela;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        setAppTitle("新闻内容编辑");
        mEditor = (RichEditor) findViewById(R.id.editor);
        txt_right = (TextView) findViewById(R.id.txt_right);
        rela = (RelativeLayout) findViewById(R.id.rela);
        mEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Log.d("reg", "html:" + text);


            }
        });
        txt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("Editorhtml", mEditor.getHtml().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("请输入内容");
        //mEditor.setInputEnabled(false);
        mPreview = (TextView) findViewById(R.id.preview);
        mEditor.getSettings().setJavaScriptEnabled(true);
        mEditor.getSettings().setAppCacheEnabled(true);
        mEditor.getSettings().setDatabaseEnabled(true);
        mEditor.getSettings().setDomStorageEnabled(true);
        String newsDetail = getIntent().getStringExtra(REQUEST_DETAIL);
        mEditor.setHtml(newsDetail);



        findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.undo();
            }
        });

        findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.redo();
            }
        });

        findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBold();
            }
        });

        findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setItalic();
            }
        });

        findViewById(R.id.action_subscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSubscript();
            }
        });

        findViewById(R.id.action_superscript).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setSuperscript();
            }
        });

        findViewById(R.id.action_strikethrough).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setStrikeThrough();
            }
        });

        findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setUnderline();
            }
        });

        findViewById(R.id.action_heading1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(1);
            }
        });

        findViewById(R.id.action_heading2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(2);
            }
        });

        findViewById(R.id.action_heading3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(3);
            }
        });

        findViewById(R.id.action_heading4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(4);
            }
        });

        findViewById(R.id.action_heading5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(5);
            }
        });

        findViewById(R.id.action_heading6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setHeading(6);
            }
        });

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.YELLOW);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setIndent();
            }
        });

        findViewById(R.id.action_outdent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setOutdent();
            }
        });

        findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignLeft();
            }
        });

        findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignCenter();
            }
        });

        findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setAlignRight();
            }
        });

        findViewById(R.id.action_blockquote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBlockquote();
            }
        });

        findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setBullets();
            }
        });

        findViewById(R.id.action_insert_numbers).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.setNumbers();
            }
        });

        findViewById(R.id.action_insert_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showImagePicker();


            }
        });

        findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Main1Activity.this, LinkedActivity.class), REQUEST_CODE_EDIT_LINKED);


            }
        });
        findViewById(R.id.action_insert_checkbox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.insertTodo();
            }
        });
    }

    private void showImagePicker() {
        SImagePicker.from(Main1Activity.this)
                .maxCount(1)
                .rowCount(3)
                .pickText(R.string.pick_name)
                .pickMode(SImagePicker.MODE_IMAGE)
                .fileInterceptor(new SingleFileLimitInterceptor())
                .forResult(REQUEST_CODE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE) {
            final ArrayList<String> pathList = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT_SELECTION);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        imglist = new ArrayList<String>();
                        // TODO: 2017/9/14 可以在线程中添加这段代码防止主线程卡顿
                        for (String path : pathList) {
                            imglist.add(path);
                            long size[] = SizeUtil.getBitmapSize(path);
//                mRichTextView.insertImage(path, id, size[0], size[1]);
//                mInsertedImages.put(id, path);
//                tryUpload(path, id);
                        }

                        //是否是原图
                        final boolean original = data.getBooleanExtra(PhotoPickerActivity.EXTRA_RESULT_ORIGINAL, false);

                        Map<String, String> loadParams = new HashMap<>();
                        loadParams.put("type", "png");
                        loadParams.put("model", "groupService");
                        loadParams.put("serverCode", "other");
                        for (int i = 0; i < imglist.size(); i++) {
                            String path = imglist.get(i);
                            Log.d("reg", "path:" + path);
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(path, options);
                            if (options.outWidth > 512 || options.outHeight > 512) {
                                options.inSampleSize = Math.max(options.outWidth, options.outHeight) / 512;
                            }
                            options.inPreferredConfig = Bitmap.Config.RGB_565;
                            options.inJustDecodeBounds = false;

                            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                            if (null != bitmap) {
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                                bitmap.recycle();
                                loadParams.put("photo", Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT));
                            }
                            String uploadUrl = Constant.DOMAIN + "phoneAdminNewsController.do?newsPhotoUpload";
                            myBinder.invokeMethodInMyServiceimg(uploadUrl, loadParams, "JournSc");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

        if (requestCode == REQUEST_CODE_EDIT_LINKED && resultCode == REQUEST_CODE_EDIT_LINKED) {
            String html = data.getStringExtra("html");
            String tit = data.getStringExtra("tit");
            mEditor.insertLink(html, tit);
//            tvAddLinked.setText(linkContent.getTitle());
//            Log.e(TAG, "startActivityForResult: " + linkContent);
        }


    }

    @Subscriber(tag = "JournSc")
    public void Sc(ApiMsg apiMsg) {
        if (apiMsg.isSuccess()) {
            Log.d("reg", "apiMsg:" + apiMsg.getResult());

            try {
                JSONObject obj = new JSONObject(apiMsg.getResult());

                mEditor.insertImage(obj.getString("photoUrl"), "photoUrl");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}


