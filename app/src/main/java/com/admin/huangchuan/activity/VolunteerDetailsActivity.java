package com.admin.huangchuan.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.User;
import com.admin.huangchuan.util.Constant;
import com.admin.huangchuan.util.DateUtil;
import com.admin.huangchuan.util.OkManager;
import com.admin.huangchuan.wx.GlideImageLoader;
import com.admin.huangchuan.wx.ImagePickerAdapter;
import com.admin.huangchuan.wx.SelectDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.feezu.liuli.timeselector.TimeSelector;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
 * Created by Administrator on 2018/2/28 0028.
 */
public class VolunteerDetailsActivity extends BaseActivity implements View.OnClickListener, ImagePickerAdapter.OnRecyclerViewItemClickListener {
    public static final int REQUEST_CODE = 'v' + 'o' + 'l' + 'u' + 'n';
    public static final String REQUEST_ID = "id";
    public static final String REQUEST_TYPE = "type";
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.imgback)
    ImageView imgback;

    @Bind(R.id.title)
    EditText title;
    @Bind(R.id.beginDate)
    TextView beginDate;
    @Bind(R.id.endDate)
    TextView endDate;
    //    @Bind(R.id.newsImgUrl)
//    ImageView newsImgUrl;
    @Bind(R.id.author)
    EditText author;
    @Bind(R.id.address)
    EditText address;
    @Bind(R.id.introduce)
    EditText introduce;
    @Bind(R.id.result)
    EditText result;
    @Bind(R.id.complain)
    Button complain;

    @Bind(R.id.xuanze)
    Button xuanze;

    @Bind(R.id.shanchu_1)
    Button shanchu;

    @Bind(R.id.zyz)
    Button zyz;

    @Bind(R.id.qyeding)
    Button qyeding;

    @Bind(R.id.line)
    LinearLayout line;

    @Bind(R.id.layout_img)
    LinearLayout layout_img;

    @Bind(R.id.rela_result)
    RelativeLayout rela_result;
    @Bind(R.id.image_view)
    ImageView imageView;
    private ArrayList<String> data_list;
    private String id;
    private boolean falg;
    private int action;
    private OkManager okManager;
    private String urlimg;
    private String newurl;

    // 请求加载系统照相机
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    final Handler handler = new Handler();
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 1;
    private ArrayList<ImageItem> images;
    private List<ImageItem> home;
    private String type;
    private int status;
    private int tp_fh;
    private RecyclerView recyclerView;
    private AlertDialog dialog;
    private  boolean falg1 = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteerdetails);
        ButterKnife.bind(this);
        initViews();
        data_list = new ArrayList<>();
        setAppTitle("志愿者活动详情");
        initImagePicker();
        initWidget();
        User user = App.me().getUser();
        if (null != user) {
            type = getIntent().getStringExtra(REQUEST_TYPE);
            if (type.equals("1")) {
                id = getIntent().getStringExtra(REQUEST_ID);
                final String url = Constant.DOMAIN + "phoneAdminVolunteerController.do?volunteerActivitiesDetail" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != myBinder) {
                            line.setVisibility(View.VISIBLE);
                            myBinder.invokeMethodInMyService(url, "VolunteerDetailsActivity");
                        }
                    }
                }, 1000);
            } else {
                line.setVisibility(View.VISIBLE);
                shanchu.setVisibility(View.GONE);
            }
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

    @Subscriber(tag = "VolunteerDetailsActivity")
    public void Http(ApiMsg apiMsg) {
        Log.d("reg", "VolunteerDetailsActivity:" + apiMsg.getResult());
        if (apiMsg.isSuccess()) {
            try {
                JSONObject obj = new JSONObject(apiMsg.getResult());
                author.setText(obj.getString("author"));
                title.setText(obj.getString("title"));
                address.setText(obj.getString("address"));
                introduce.setText(obj.getString("introduce"));
                endDate.setText(obj.getString("endDate"));
                beginDate.setText(obj.getString("beginDate"));
                if (null != obj.getString("result")) {
                    result.setText(obj.getString("result"));
                }
                if (null != obj.getString("newsImgUrl") && !obj.getString("newsImgUrl").equals("")) {
//                    data_list.add(obj.getString("newsImgUrl"));
                    urlimg = obj.getString("newsImgUrl");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Bitmap mBitmap = null;
                            try {
                                mBitmap = BitmapFactory.decodeStream(getImageStream(urlimg));
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
                status = obj.getInt("status");
                adapter.setStatus(status);
                complain.setVisibility(View.VISIBLE);
                switch ((obj.getInt("status"))) {
                    case 1:
                        complain.setText("截止报名");
                        rela_result.setVisibility(View.GONE);
                        break;
                    case 2:
                        complain.setText("开始活动");
                        rela_result.setVisibility(View.GONE);
                        break;
                    case 3:
                        title.setFocusable(false);
                        beginDate.setOnClickListener(null);
                        endDate.setOnClickListener(null);
                        author.setFocusable(false);
                        address.setFocusable(false);
                        introduce.setFocusable(false);
                        qyeding.setVisibility(View.GONE);
                        rela_result.setVisibility(View.VISIBLE);
                        complain.setText("结束活动");
                        break;
                    case 4:
                        title.setFocusable(false);
                        beginDate.setOnClickListener(null);
                        endDate.setOnClickListener(null);
                        author.setFocusable(false);
                        address.setFocusable(false);
                        introduce.setFocusable(false);
                        result.setFocusable(false);
                        rela_result.setVisibility(View.VISIBLE);
                        qyeding.setVisibility(View.GONE);
                        complain.setVisibility(View.GONE);
                        break;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Subscriber(tag = "Jz")
    public void Jz(ApiMsg apiMsg) {
        H(apiMsg);
    }

    @Subscriber(tag = "Ks")
    public void Ks(ApiMsg apiMsg) {
        H(apiMsg);
    }

    @Subscriber(tag = "Js")
    public void Js(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        if (apiMsg.isSuccess()) {
            setResult(RESULT_OK); // 回调登录成功
            finish(); // 返回上一页
        }
    }


    private void H(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        User user = App.me().getUser();
        if (null != user) {
            final String url = Constant.DOMAIN + "phoneAdminVolunteerController.do?volunteerActivitiesDetail" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id;
            myBinder.invokeMethodInMyService(url, "VolunteerDetailsActivity");
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
        View[] views = {imgBack, beginDate, endDate, xuanze, imgback, qyeding, complain, shanchu,zyz};
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    /**
     * Get image from newwork
     *
     * @param path The path of image
     * @return InputStream
     * @throws Exception
     */
    public static InputStream getImageStream(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return conn.getInputStream();
        }
        return null;
    }


    /**
     * 保存文件
     *
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static File saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(Environment.getExternalStorageDirectory().getPath());
//        if (!dirFile.exists()) {
        dirFile.mkdir();
//        }
        fileName = "tp" + ".jpg";
        File myCaptureFile = new File(Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return myCaptureFile;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                if ( falg1 == true){
                    setResult(RESULT_OK); // 回调登录成功
                    finish(); // 返回上一页
                }else {
                    finish();
                }
                break;
            case R.id.imgback:
                layout_img.setVisibility(View.GONE);
                break;
            case R.id.beginDate:
                falg = false;
                BtnTime();
                break;
            case R.id.endDate:
                falg = true;
                BtnTime();
                break;
            case R.id.complain:
                User user = App.me().getUser();
                switch (status) {
                    case 1:
                        falg1 = true;
//                        complain.setText("截止报名");
//                        rela_result.setVisibility(View.GONE);
                        if (null != user) {
                            String url = Constant.DOMAIN + "phoneAdminVolunteerController.do?stopEnroll" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id;
                            myBinder.invokeMethodInMyService(url, "Jz");
                        }
                        break;
                    case 2:
                        falg1 = true;
//                        complain.setText("开始活动");
//                        rela_result.setVisibility(View.GONE);
                        if (null != user) {
                            String url = Constant.DOMAIN + "phoneAdminVolunteerController.do?beginActivity" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id;
                            myBinder.invokeMethodInMyService(url, "Ks");
                        }
                        break;
                    case 3:
                        falg1 = true;
//                        rela_result.setVisibility(View.VISIBLE);
//                        complain.setText("结束活动");
                        String resultText = result.getText().toString();
                        if (resultText.length() == 0) {
                            App.me().toast("请输入活动结果");
                            result.requestFocus();
                            return;
                        }
                        if (null != user) {
                            String url = Constant.DOMAIN + "phoneAdminVolunteerController.do?finishActivity" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id + "&result" + "=" + resultText;
                            myBinder.invokeMethodInMyService(url, "Js");
                        }
                        break;
                }
                break;
            case R.id.qyeding:
                QueDing();
                break;
            case R.id.shanchu_1:
                showAlertDialog("是否确定删除?", id);
                break;
            case R.id.zyz:
                Intent intent = new Intent(VolunteerDetailsActivity.this, VolunteerDetailsListActivity.class);
                intent.putExtra(VolunteerDetailsListActivity.REQUEST_ID, id);
                startActivity(intent);

                break;
        }
    }

    public void showAlertDialog(final String s, final String User_name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(VolunteerDetailsActivity.this);
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
                        String url = Constant.DOMAIN + "phoneAdminVolunteerController.do?deleteById" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + User_name;
                        myBinder.invokeMethodInMyService(url, "VolunteerDetailsshanchu");
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

    @Subscriber(tag = "VolunteerDetailsshanchu")
    public void shanchu(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        if (apiMsg.isSuccess()) {
            setResult(RESULT_OK); // 回调登录成功
            finish(); // 返回上一页
        }
    }


    private void QueDing() {
        String titleText = title.getText().toString();
        if (titleText.length() == 0) {
            App.me().toast("请输入活动标题");
            title.requestFocus();
            return;
        }

        String beginDateText = beginDate.getText().toString();
        if (beginDateText.length() == 0) {
            App.me().toast("请输入开始时间");
            beginDate.requestFocus();
            return;
        }

        String endDateText = endDate.getText().toString();
        if (endDateText.length() == 0) {
            App.me().toast("请输入结束时间");
            endDate.requestFocus();
            return;
        }

        String authorText = author.getText().toString();
        if (authorText.length() == 0) {
            App.me().toast("请输入发布者");
            author.requestFocus();
            return;
        }

        String addressText = address.getText().toString();
        if (addressText.length() == 0) {
            App.me().toast("请输入活动地址");
            address.requestFocus();
            return;
        }

        String introduceText = introduce.getText().toString();
        if (introduceText.length() == 0) {
            App.me().toast("请输入活动介绍");
            introduce.requestFocus();
            return;
        }


        if ((null != beginDate.getText().toString() && !beginDate.getText().toString().equals("")) && (null != endDate.getText().toString() && !endDate.getText().toString().equals(""))) {
            if (!beginDate.getText().toString().equals("开始日期") && !endDate.getText().toString().equals("结束日期")) {
                long startdate = DateUtil.getStringDate(beginDate.getText().toString());
                Log.d("reg", "开始日期:" + beginDate.getText().toString());
                Log.d("reg", "开始日期时间戳：" + startdate);
                long enddate = DateUtil.getStringDate(endDate.getText().toString());
                Log.d("reg", "结束日期时间戳：" + enddate);
                Log.d("reg", "结束日期:" + endDate.getText().toString());
                if (startdate > enddate) {
                    App.me().toast("请选择正确的开始结束时间");
                    return;
                }
            }


            User user = App.me().getUser();
            if (null != user) {
                //从资源中获取的Drawable --> Bitmap
                /***开发中使用bitmap转换成base64格式的字符串，提交给服务器***/
                //String base64 = "/9j/4AAQSkZJRgABAQAAAQABAAD";
                // 这是本地服务器的上传测试地址
                Map<String, String> loadParams = new HashMap<>();
                loadParams.put("type", "png");
                loadParams.put("model", "groupService");
                loadParams.put("serverCode", "other");
                loadParams.put("title", titleText);
                loadParams.put("beginDate", beginDateText);
                loadParams.put("endDate", endDateText);
                loadParams.put("author", authorText);
                loadParams.put("address", addressText);
                loadParams.put("introduce", introduceText);
                loadParams.put("uuid", user.getUuid());
                if (tp_fh == 0) {
                    if (selImageList != null && selImageList.size() == 1) {
                        String path = selImageList.get(0).path;
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
                            loadParams.put("photoNames", Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT));
                        }
                    }
                } else if (tp_fh == 1) {
                    loadParams.put("photoNames", "0");
                } else {
                    loadParams.put("photoNames", "");
                }
                Log.d("reg", "loadParams:" + loadParams.toString());

                if (type.equals("1")) {
                    Log.d("reg", "1");
                    loadParams.put("id", id);
                    String uploadUrl = Constant.DOMAIN + "phoneAdminVolunteerController.do?volunteerActivityUpdate";
                    myBinder.invokeMethodInMyServiceimg(uploadUrl, loadParams, "Gx");

                } else if (type.equals("2")) {
                    Log.d("reg", "2");
                    String uploadUrl = Constant.DOMAIN + "phoneAdminVolunteerController.do?volunteerActivityAdd";
                    myBinder.invokeMethodInMyServiceimg(uploadUrl, loadParams, "tj");
                }
            }
        }
    }

    private void BtnTime() {
        TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {

                if (falg == false) {
                    beginDate.setText(time + ":00");
                    beginDate.setTag(time + ":00");
                } else {
                    endDate.setText(time + ":00");
                    endDate.setTag(time + ":00");
                }
            }
        }, getTodayDateTime(), "3015-12-1 17:34");

        timeSelector.show();


    }

    public static String getTodayDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                Locale.getDefault());
        return format.format(new Date());
    }

    /**
     * 检测权限是否授权
     *
     * @return
     */
    private boolean checkPermission(Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//        if (requestCode == REQUEST_PHOTO && resultCode == RESULT_OK) {
//            List<String> paths = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//            if (paths != null && paths.size() == 1) {
//                String path = paths.get(0);
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inJustDecodeBounds = true;
//                BitmapFactory.decodeFile(path, options);
//                if (options.outWidth > 512 || options.outHeight > 512) {
//                    options.inSampleSize = Math.max(options.outWidth, options.outHeight) / 512;
//                }
//                options.inPreferredConfig = Bitmap.Config.RGB_565;
//                options.inJustDecodeBounds = false;
//
//                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
//                bitmap.recycle();
//                if (null == okManager) {
//                    okManager = OkManager.getInstance();
//                }
//                //从资源中获取的Drawable --> Bitmap
//                Resources resources = getResources();
//                /***开发中使用bitmap转换成base64格式的字符串，提交给服务器***/
//                //String base64 = "/9j/4AAQSkZJRgABAQAAAQABAAD";
//                // 这是本地服务器的上传测试地址
//                String uploadUrl = Constant.DOMAIN + "phoneAdminVolunteerController.do?volunteerPhotoUpload";
//                Map<String, String> loadParams = new HashMap<>();
//                loadParams.put("id", "-1");
//                loadParams.put("photo", Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT));
//                loadParams.put("type", "png");
//                loadParams.put("model", "groupService");
//                loadParams.put("serverCode", "other");
//                myBinder.invokeMethodInMyServiceimg(uploadUrl, loadParams, "tp");
//                //进行图片的上传
////            okManager.postComplexForm(uploadUrl, loadParams);
//
//
//                try {
//                    stream.close();
//                } catch (IOException e) {
//                    LogUtil.e(VolunteerDetailsActivity.class, e.getMessage(), e);
//                }
//
//            }
//
//        } else if (requestCode == CropImageActivity.REQUEST_CODE && resultCode == RESULT_OK) {
//
//        }
//    }

    @Subscriber(tag = "Gx")
    public void Gx(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        if (apiMsg.isSuccess()) {
            setResult(RESULT_OK); // 回调登录成功
            finish(); // 返回上一页
        }

    }

    @Subscriber(tag = "tj")
    public void Tj(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        if (apiMsg.isSuccess()) {
            setResult(RESULT_OK); // 回调登录成功
            finish(); // 返回上一页
        }
    }

    @Subscriber(tag = "tp")
    public void Htt(ApiMsg apiMsg) {
        if (apiMsg.isSuccess()) {
            Log.d("reg", "tp:" + apiMsg.getResult());
            try {
                JSONObject obj = new JSONObject(apiMsg.getResult());
                newurl = obj.getString("photoUrl");
//                Picasso.with(this).load(obj.getString("photoUrl")).into(newsImgUrl);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
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
                                Intent intent = new Intent(VolunteerDetailsActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(VolunteerDetailsActivity.this, ImageGridActivity.class);
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
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_STATUS, status);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
    }

}
