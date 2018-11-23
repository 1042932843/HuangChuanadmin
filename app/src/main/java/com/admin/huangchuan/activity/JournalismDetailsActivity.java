package com.admin.huangchuan.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.admin.huangchuan.App;
import com.admin.huangchuan.R;
import com.admin.huangchuan.View.EContent;
import com.admin.huangchuan.View.NonFocusingScrollView;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.User;
import com.admin.huangchuan.util.Constant;
import com.admin.huangchuan.util.RichEditor;
import com.admin.huangchuan.util.SizeUtil;
import com.admin.huangchuan.wx.GlideImageLoader;
import com.admin.huangchuan.wx.ImagePickerAdapter;
import com.admin.huangchuan.wx.SelectDialog;
import com.imnjh.imagepicker.activity.PhotoPickerActivity;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/8 0008.
 */
@SuppressLint("SetJavaScriptEnabled")
public class JournalismDetailsActivity extends BaseActivity implements View.OnClickListener, ImagePickerAdapter.OnRecyclerViewItemClickListener {
    public static final int REQUEST_CODE = 'j' + 'o' + 'u' + 'r' + 'n';
    public static final String REQUEST_ID = "id";
    public static final String REQUEST_TYPE = "type";
    public static final String REQUEST_CONTENT = "contentText";
    public static final String REQUEST_AREA = "showArea";
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.newsTitle)
    EditText newsTitle;
    @Bind(R.id.newsTypeName)
    TextView newsTypeName;
    @Bind(R.id.author)
    EditText author;
    @Bind(R.id.createDate)
    TextView createDate;
    @Bind(R.id.areaName_)
    TextView areaName_;
    @Bind(R.id.Qd)
    Button Qd;
    @Bind(R.id.shanchu)
    Button shanchu;
    @Bind(R.id.line)
    LinearLayout line;

    @Bind(R.id.areaName_rela)
    RelativeLayout areaName_rela;

    private Handler handler = new Handler();
    private String id;
    private String newsTypeId;
    private String type;
    private RichEditor mEditor;
    private ImagePickerAdapter adapter;
    // 请求加载系统照相机
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 102;
    private static final int REQUEST_CODE_IMAGE = 101;
    private static final int REQUEST_CODE_EDIT_LINKED = 2001;//编辑链接
    /**
     * 数据区
     */
    private List<EContent> datas;
    private String urlimg;
    private ArrayList<ImageItem> images;
    private ArrayList<String> imglist;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int tp_fh;
    private int maxImgCount = 1;
    private RelativeLayout layout_bar;
    private RelativeLayout relaR;
    private NonFocusingScrollView scrollView;
    private String newsDetail;
    private boolean falg;
    private String showArea;
    private String areaName_text;
    private String areaId;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journalismdetails);
        ButterKnife.bind(this);
        datas = new ArrayList<>();
        initViews();
        initImagePicker();
        initWidget();
        newsTypeId= getIntent().getStringExtra(REQUEST_ID);
        type = getIntent().getStringExtra(REQUEST_TYPE);
        String contentText = getIntent().getStringExtra(REQUEST_CONTENT);


        showArea = getIntent().getStringExtra(REQUEST_AREA);
        if (showArea.equals("1")) {
            areaName_rela.setVisibility(View.GONE);
        } else if (showArea.equals("2")) {
            areaName_rela.setVisibility(View.VISIBLE);
        }

        newsTypeName.setText(contentText);

        if (type.equals("1")) {
            id= getIntent().getStringExtra(REQUEST_ID);
            User user = App.me().getUser();
            if (null != user) {
                final String url = Constant.DOMAIN + "phoneAdminNewsController.do?newsDetail" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != myBinder) {
//                            line.setVisibility(View.VISIBLE);
                            myBinder.invokeMethodInMyService(url, "JournalismDetailsActivity");
                        }
                    }
                }, 1000);
            }
        }else {
            shanchu.setVisibility(View.GONE);
        }
        scrollView = (NonFocusingScrollView) findViewById(R.id.scrollView);
        relaR = (RelativeLayout) findViewById(R.id.relaR);
//        setListener();
        layout_bar = (RelativeLayout) findViewById(R.id.layout_bar);
        setAppTitle("新闻详情");
        mEditor = (RichEditor) findViewById(R.id.editor);
//        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        //mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.getSettings().setJavaScriptEnabled(true);
        mEditor.getSettings().setAppCacheEnabled(true);
        mEditor.getSettings().setDatabaseEnabled(true);
        mEditor.getSettings().setDomStorageEnabled(true);
        mEditor.setFocusable(false);
        mEditor.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        mEditor.setClickable(false);
        mEditor.setPlaceholder("请输入内容");
        relaR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JournalismDetailsActivity.this, Main1Activity.class);
                intent.putExtra(Main1Activity.REQUEST_DETAIL, newsDetail);
                startActivityForResult(intent, Main1Activity.REQUEST_CODE);
            }
        });



    }


    @Subscriber(tag = "JournalismDetailsActivity")
    public void Htt(ApiMsg apiMsg) {

        if (apiMsg.isSuccess()) {
            Log.d("reg", "JournalismDetailsActivity:" + apiMsg.getResult());
            try {
                JSONObject obj = new JSONObject(apiMsg.getResult());
                newsTitle.setText(obj.getString("newsTitle"));
                author.setText(obj.getString("author"));
                newsTypeName.setText(obj.getString("newsTypeName"));
                createDate.setText(obj.getString("createDate"));
                createDate.setTag(obj.getString("createDate"));
                newsDetail = obj.getString("newsDetail");
                areaId = obj.getString("areaId");
                mEditor.setHtml(newsDetail);
                areaName_.setText(obj.getString("areaName"));
//                areaName.setText(obj.getString("areaName"));
                if (null != obj.getString("newsImgUrl") && !obj.getString("newsImgUrl").equals("")) {
//                    data_list.add(obj.getString("newsImgUrl"));
                    urlimg = obj.getString("newsImgUrl");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
//                                Log.d("reg", "图片" + String.valueOf(saveFile(mBitmap, "图片")));
                                images = new ArrayList<ImageItem>();
                                ImageItem s1 = new ImageItem();
                                images.add(s1);
                                images.get(0).path = urlimg;
                                Log.d("reg", " images.get(0).path:" + images.get(0).path);
                                selImageList.addAll(images);
                                handler.postDelayed(runnable, 1);// 打开定时器，执行操作
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    int runCount = 0;// 全局变量，用于判断是否是第一次执行
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (runCount == 1) {// 第一次执行则关闭定时执行操作
                // 在此处添加执行的代码
                adapter.setImages(selImageList);
                adapter.notifyDataSetChanged();
                handler.removeCallbacks(this);
            }
            handler.postDelayed(this, 1);
            runCount++;
        }

    };

    private void initViews() {
        View[] views = {imgBack, Qd, createDate, areaName_,shanchu};
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.Qd:
                Qd();
                break;

            case R.id.createDate:
                CreateDate();
                break;

            case R.id.areaName_:
                AreaName();
                break;

            case R.id.shanchu:
                ShanChu();
                break;
        }
    }


    private void ShanChu() {
        if (null != id) {
            showAlertDialog("是否确定删除?", id);
        }
    }

    public void showAlertDialog(final String s, final String User_name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(JournalismDetailsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_normal_layo, null);
        TextView message = (TextView) view.findViewById(R.id.title);
        message.setText(s);
        TextView positiveButton = (TextView) view.findViewById(R.id.positiveButton);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView negativeButton = (TextView) view.findViewById(R.id.negativeButton);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = App.me().getUser();
                if (null != user) {
                    if (null != User_name) {
                            String url = Constant.DOMAIN + "phoneAdminNewsController.do?deleteById" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + User_name;
                            myBinder.invokeMethodInMyService(url, "Journhanchu");


                    }
                }
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
    @Subscriber(tag = "Journhanchu")
    public void Journhanchu(ApiMsg apiMsg) {
        Log.d("reg", "getMessage:" + apiMsg.getMessage());
        App.me().toast(apiMsg.getMessage());
        if (apiMsg.isSuccess()) {
            setResult(RESULT_OK); // 回调登录成功
            finish(); // 返回上一页
        }
    }
    private void AreaName() {
        Intent intent = new Intent(this, AreaNameActivity.class);
        startActivityForResult(intent, AreaNameActivity.REQUEST_CODE);
    }


    private void CreateDate() {
        BtnTime();
    }

    private void Qd() {
        String newsTitleText = newsTitle.getText().toString();
        if (newsTitleText.length() == 0) {
            App.me().toast("请输入新闻标题");
            newsTitle.requestFocus();
            return;
        }

        if (showArea.equals("1")) {
            areaName_rela.setVisibility(View.GONE);
        } else if (showArea.equals("2")) {
            String areaName_text = areaName_.getText().toString();
            if (areaName_text.length() == 0) {
                App.me().toast("请选择所在支部");
                AreaName();
                return;
            }
        }

        String authorText = author.getText().toString();
        if (authorText.length() == 0) {
            App.me().toast("请输入发布者");
            author.requestFocus();
            return;
        }


        if (createDate.getTag() == null) {
            App.me().toast("请选择发布时间");
            BtnTime();
            return;
        }
        Log.d("reg", "selImageList.size():" + selImageList.size());
        /*if (selImageList.size() == 0) {
            App.me().toast("请选择发布图标");
            recyclerView.requestFocus();
            return;
        }*/
        String mEditorText="";
        if(mEditor.getHtml()!=null){
           mEditorText = mEditor.getHtml().toString();
        }
        if (mEditorText.length() == 0) {
            App.me().toast("请输入新闻内容");
            return;
        }


        User user = App.me().getUser();
        if (null != user) {
            Map<String, String> loadParams = new HashMap<>();
            loadParams.put("type", "png");
            loadParams.put("model", "groupService");
            loadParams.put("serverCode", "other");
            loadParams.put("newsTitle", newsTitleText);
            loadParams.put("author", authorText);
            loadParams.put("uuid", user.getUuid());
            loadParams.put("detail", mEditorText);
            if (showArea.equals("2")) {
                loadParams.put("areaName", areaName_.getText().toString());
                loadParams.put("areaId", areaId);
                Log.d("reg","areaId:"+areaId);
                Log.d("reg","areaName_.getText().toString():"+areaName_.getText().toString());
            }


            loadParams.put("createDate", createDate.getTag().toString());
            if (tp_fh == 0) {
                if (selImageList != null && selImageList.size() == 1) {
                    String path = selImageList.get(0).path;
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
                        loadParams.put("newsImgUrl", Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT));
                    }
                }
            } else if (tp_fh == 1) {
                loadParams.put("newsImgUrl", "0");
            } else {
                loadParams.put("newsImgUrl", "");
            }

            if (type.equals("1")) {
                loadParams.put("id", id);
                loadParams.put("newsTypeId", newsTypeId);
                String uploadUrl = Constant.DOMAIN + "phoneAdminNewsController.do?newsUpdate";
                myBinder.invokeMethodInMyServiceimg(uploadUrl, loadParams, "journGx");

            } else if (type.equals("2")) {
                loadParams.put("newsTypeId", newsTypeId);
                String uploadUrl = Constant.DOMAIN + "phoneAdminNewsController.do?newsAdd";
                myBinder.invokeMethodInMyServiceimg(uploadUrl, loadParams, "journtj");
            }
        }
    }

    @Subscriber(tag = "journGx")
    public void journGx(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        if (apiMsg.isSuccess()) {
            setResult(RESULT_OK); // 回调登录成功
            finish(); // 返回上一页
        }
    }

    @Subscriber(tag = "journtj")
    public void journtj(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        if (apiMsg.isSuccess()) {
            setResult(RESULT_OK); // 回调登录成功
            finish(); // 返回上一页
        }
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }

    private void initWidget() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private SelectDialog showDialog(SelectDialog.SelectDialogListener listener, List<String> names) {
        SelectDialog dialog = new SelectDialog(this, R.style
                .transparentFrameWindowStyle,
                listener, names);
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                List<String> names = new ArrayList<>();
                names.add("拍照");
                names.add("相册");
                showDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0: // 直接调起相机
                                /**
                                 * 0.4.7 目前直接调起相机不支持裁剪，如果开启裁剪后不会返回图片，请注意，后续版本会解决
                                 *
                                 * 但是当前直接依赖的版本已经解决，考虑到版本改动很少，所以这次没有上传到远程仓库
                                 *
                                 * 如果实在有所需要，请直接下载源码引用。
                                 */
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent = new Intent(JournalismDetailsActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(JournalismDetailsActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                break;
                            default:
                                break;
                        }

                    }
                }, names);


                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_STATUS, 0);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == Main1Activity.REQUEST_CODE && resultCode == RESULT_OK) {
            newsDetail = data.getStringExtra("Editorhtml");
            mEditor.setHtml(newsDetail);
        }


        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                Log.d("reg", "路径:" + images.get(0).path);
                Log.d("reg", "名字:" + images.get(0).name);
                if (images != null) {
                    tp_fh = 0;
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                Log.d("reg", "images:" + images.size());
                if (images != null) {
                    if (images.size() == 0) {
                        tp_fh = 1;
                    }
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
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
//                            String uploadUrl = Constant.DOMAIN + "phoneAdminNewsController.do?newsPhotoUpload";
//                            myBinder.invokeMethodInMyServiceimg(uploadUrl, loadParams, "JournSc");

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
        if (requestCode == AreaNameActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            if (null != data) {
                areaName_.setText(data.getStringExtra("content"));
                 areaId = data.getStringExtra("id");
            }
        }
    }


    private void BtnTime() {
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                createDate.setText(time + ":00");
                createDate.setTag(time + ":00");
            }
        }, getTodayDateTime(), "3015-12-1 17:34");

        timeSelector.show();


    }

    public static String getTodayDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault());
        return format.format(new Date());
    }
}

