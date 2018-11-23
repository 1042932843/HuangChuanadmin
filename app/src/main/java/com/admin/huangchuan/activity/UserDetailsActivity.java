package com.admin.huangchuan.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.admin.huangchuan.App;
import com.admin.huangchuan.DateTimePicker;
import com.admin.huangchuan.R;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.User;
import com.admin.huangchuan.util.Constant;
import com.admin.huangchuan.util.StringUtil;
import com.admin.huangchuan.wx.GlideImageLoader;
import com.admin.huangchuan.wx.ImagePickerAdapter;
import com.admin.huangchuan.wx.SelectDialog;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/6 0006.
 */
public class UserDetailsActivity extends BaseActivity implements View.OnClickListener, DateTimePicker.OnDateSetListener, ImagePickerAdapter.OnRecyclerViewItemClickListener {

    // 请求加载系统照相机
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final String REQUEST_ID = "id";
    public static final int REQUEST_CODE = 'u' + 's' + 'e' + 'r' + 'd';
    public static final String REQUEST_TYPE = "type";
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.xuanze)
    Button xuanze;
    @Bind(R.id.realName)
    EditText realName;
    @Bind(R.id.idcard)
    EditText idcard;
    @Bind(R.id.nan)
    RadioButton nan;
    @Bind(R.id.nv)
    RadioButton nv;
    @Bind(R.id.navBar)
    RadioGroup navBar;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.joinPartyDate)
    TextView joinPartyDate;
    @Bind(R.id.areaName)
    TextView areaName;
    @Bind(R.id.position)
    EditText position;
    @Bind(R.id.cdno)
    RadioButton cdno;
    @Bind(R.id.cd_yes)
    RadioButton cdYes;
    @Bind(R.id.roder_depat)
    RadioGroup roderDepat;
    @Bind(R.id.line)
    LinearLayout line;

    @Bind(R.id.shanchu)
    Button shanchu;

    @Bind(R.id.czmm)
    Button czmm;

    @Bind(R.id.Qd)
    Button Qd;

    private AlertDialog dialog;

    private Handler handler = new Handler();
    private String type;
    private String id;
    private DateTimePicker dateTimePicker;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 1;
    private ArrayList<ImageItem> images;
    private int tp_fh;
    private String urlimg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetails);
        setAppTitle("用户信息详情");
        ButterKnife.bind(this);
        initViews();
        initImagePicker();
        initWidget();
        User user = App.me().getUser();
        if (null != user) {
            type = getIntent().getStringExtra(REQUEST_TYPE);
            if (type.equals("1")) {
                id = getIntent().getStringExtra(REQUEST_ID);
                final String url = Constant.DOMAIN + "phoneAdminTSUserController.do?userDetail" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (null != myBinder) {
//                            line.setVisibility(View.VISIBLE);
                            shanchu.setVisibility(View.VISIBLE);
                            czmm.setVisibility(View.VISIBLE);
                            myBinder.invokeMethodInMyService(url, "UserDetailsActivity");
                        }
                    }
                }, 1000);
            } else {
                czmm.setVisibility(View.GONE);
                shanchu.setVisibility(View.GONE);
//                line.setVisibility(View.VISIBLE);
            }
        }

    }

    private void initViews() {
        View[] views = {imgBack, areaName, joinPartyDate, shanchu, czmm, Qd};
        for (View view : views) {
            view.setOnClickListener(this);
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

    @Subscriber(tag = "UserDetailsActivity")
    public void Http(ApiMsg apiMsg) {
        Log.d("reg", "VolunteerDetailsActivity:" + apiMsg.getResult());
        if (apiMsg.isSuccess()) {
            try {
                JSONObject obj = new JSONObject(apiMsg.getResult());
                joinPartyDate.setText(obj.getString("joinPartyDate"));
                joinPartyDate.setTag(obj.getString("joinPartyDate"));

                position.setText(obj.getString("position"));
                phone.setText(obj.getString("phone"));
                if (null != obj.getString("sex") && obj.getString("sex").equals("1")) {
                    nan.setChecked(true);
                    nv.setChecked(false);
                } else {
                    nan.setChecked(false);
                    nv.setChecked(true);
                }
                if (null != obj.getString("dysj") && obj.getString("dysj").equals("1")) {
                    cdno.setChecked(true);
                    cdYes.setChecked(false);
                } else {
                    cdno.setChecked(false);
                    cdYes.setChecked(true);
                }
                realName.setText(obj.getString("realName"));
                areaName.setText(obj.getString("areaName"));
                areaName.setTag(obj.getString("areaId"));
                idcard.setText(obj.getString("idcard"));
                urlimg = obj.getString("picture");
                if (null != urlimg&&!"".equals(urlimg)) {
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
                                Intent intent = new Intent(UserDetailsActivity.this, ImageGridActivity.class);
                                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                                startActivityForResult(intent, REQUEST_CODE_SELECT);
                                break;
                            case 1:
                                //打开选择,本次允许选择的数量
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                                Intent intent1 = new Intent(UserDetailsActivity.this, ImageGridActivity.class);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AreaNameActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            if (null != data) {
                areaName.setText(data.getStringExtra("content"));
                areaName.setTag(data.getStringExtra("id"));
            }
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.areaName:
                AreaName();
                break;

            case R.id.joinPartyDate:
                JoinPartyDate();
                break;

            case R.id.shanchu:
                ShanChu();
                break;

            case R.id.czmm:
                Czmm();
                break;
            case R.id.Qd:
                Qd();
                break;
        }
    }

    private void Qd() {

        String realNameText = realName.getText().toString();
        if (realNameText.length() == 0) {
            App.me().toast("请输入真实姓名");
            realName.requestFocus();
            return;
        }

        String idcardText = idcard.getText().toString();
        if (idcardText.length() == 0) {
            App.me().toast("请输入身份证号");
            idcard.requestFocus();
            return;
        }
        if (!StringUtil.matchesIdCard(idcardText)) {
            App.me().toast("身份证号码格式不正确");
            idcard.requestFocus();
            return;
        }

        int expertise_score = navBar.indexOfChild(navBar.findViewById(navBar.getCheckedRadioButtonId()));
        Log.d("reg", "性别:" + Suan(expertise_score));

        String phoneText = phone.getText().toString();
        if (phoneText.length() == 0) {
            App.me().toast("请输入手机号");
            phone.requestFocus();
            return;
        }


        if (!StringUtil.matchesPhone(phoneText)) {
            App.me().toast("手机号码格式不正确");
            phone.requestFocus();
            return;
        }


//        String joinPartyDateText = joinPartyDate.getText().toString();
        if (joinPartyDate.getTag() == null) {
            App.me().toast("请选择入党时间");
            BtnTime();
            return;
        }

        if (areaName.getTag() == null) {
            App.me().toast("请选择所在支部");
            AreaName();
            return;
        }

        String positionText = position.getText().toString();
        if (positionText.length() == 0) {
            App.me().toast("请输入职位");
            position.requestFocus();
            return;
        }


        int roderDepat_score = roderDepat.indexOfChild(roderDepat.findViewById(roderDepat.getCheckedRadioButtonId()));
        Log.d("reg", "是否第一书记:" + Suan(roderDepat_score));

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
            loadParams.put("joinPartyDate", joinPartyDate.getTag().toString());
            loadParams.put("position", positionText);
            loadParams.put("sex",String.valueOf( Suan(expertise_score)));
            loadParams.put("phone", phoneText);
            loadParams.put("areaId", areaName.getTag().toString());
            loadParams.put("areaName", areaName.getText().toString());
            loadParams.put("uuid", user.getUuid());
            loadParams.put("realName", realNameText);
            loadParams.put("idcard",idcardText);
            loadParams.put("dysj",String.valueOf( Suan(roderDepat_score)));
            if (tp_fh == 0) {
                if (selImageList != null && selImageList.size() == 1) {
                    String path = selImageList.get(0).path;
                    Log.d("reg","path:"+path);
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
                        loadParams.put("picture", Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT));
                    }
                }
            } else if (tp_fh == 1) {
                loadParams.put("picture", "0");
            } else {
                loadParams.put("picture", "");
            }
            Log.d("reg", "loadParams:" + loadParams.toString());

            if (type.equals("1")) {
                Log.d("reg", "1");
                loadParams.put("id", id);
                String uploadUrl = Constant.DOMAIN + "phoneAdminTSUserController.do?userUpdate";
                myBinder.invokeMethodInMyServiceimg(uploadUrl, loadParams, "UseGx");

            } else if (type.equals("2")) {
                Log.d("reg", "2");
                String uploadUrl = Constant.DOMAIN + "phoneAdminTSUserController.do?userAdd";
                myBinder.invokeMethodInMyServiceimg(uploadUrl, loadParams, "Usertj");
            }
        }




    }

    @Subscriber(tag = "UseGx")
    public void UseGx(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        if (apiMsg.isSuccess()) {
            setResult(RESULT_OK); // 回调登录成功
            finish(); // 返回上一页
        }
    }

    @Subscriber(tag = "Usertj")
    public void Tj(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        if (apiMsg.isSuccess()) {
            setResult(RESULT_OK); // 回调登录成功
            finish(); // 返回上一页
        }
    }
    private int Suan(int i) {
        switch (i) {
            case 0:
                return 1;
            case 1:
                return 2;
            default:
                return 1;
        }
    }


    private void Czmm() {
        if (null != id) {
            showAlertDialog("是否重置密码?", id, 2);
        }
    }


    private void ShanChu() {
        if (null != id) {
            showAlertDialog("是否确定删除?", id, 1);
        }
    }

    private void JoinPartyDate() {
        BtnTime();
    }

    private void BtnTime() {
        if (dateTimePicker == null) {
            dateTimePicker = new DateTimePicker(this, this, 1);
        }
        dateTimePicker.show();
    }

    public void showAlertDialog(final String s, final String User_name, final int type) {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserDetailsActivity.this);
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
                        if (type == 1) {
                            String url = Constant.DOMAIN + "phoneAdminTSUserController.do?deleteById" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + User_name;
                            myBinder.invokeMethodInMyService(url, "Detailsshanchu");
                        } else if (type == 2) {
                            String url = Constant.DOMAIN + "phoneAdminTSUserController.do?resetPassword" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + User_name;
                            myBinder.invokeMethodInMyService(url, "Czmm");
                        }

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

    @Subscriber(tag = "Detailsshanchu")
    public void shanchu(ApiMsg apiMsg) {
        Log.d("reg", "getMessage:" + apiMsg.getMessage());
        App.me().toast(apiMsg.getMessage());
        if (apiMsg.isSuccess()) {
            setResult(RESULT_OK); // 回调登录成功
            finish(); // 返回上一页
        }
    }

    @Subscriber(tag = "Czmm")
    public void Czmm(ApiMsg apiMsg) {
        Log.d("reg", "getMessage:" + apiMsg.getMessage());
        App.me().toast(apiMsg.getMessage());
        if (apiMsg.isSuccess()) {
            setResult(RESULT_OK); // 回调登录成功
            finish(); // 返回上一页
        }
    }

    @Override
    public void onDateSet(DateTimePicker dialog, int year, int monthOfYear, int dayOfMonth, int timeOfIndex, String t) {

        Log.d("reg", "year:" + year);
        Log.d("reg", "monthOfYear:" + monthOfYear);
        Log.d("reg", "dayOfMonth:" + dayOfMonth);
        if (year != 0) {
            String month;
            String day;
            int mon = monthOfYear + 1;
            if (mon < 10) {
                month = "0" + mon;
            } else {
                month = mon + "";
            }
            if (dayOfMonth < 10) {
                day = "0" + dayOfMonth;
            } else {
                day = dayOfMonth + "";
            }
            String date = String.format("%s-%s-%s", year, month, day);
            Log.d("reg", "date:" + date);
        /*int tim = timeOfIndex + 1;*/
            joinPartyDate.setText(date);
            joinPartyDate.setTag(date);
        } else {
            joinPartyDate.setTag(null);
        }
    }


    private void AreaName() {
        Intent intent = new Intent(this, AreaNameActivity.class);
        startActivityForResult(intent, AreaNameActivity.REQUEST_CODE);
    }

}
