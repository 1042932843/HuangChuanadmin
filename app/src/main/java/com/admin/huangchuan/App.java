package com.admin.huangchuan;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.huangchuan.model.User;
import com.admin.huangchuan.util.FrescoImageLoader;
import com.admin.huangchuan.util.LogUtil;
import com.awen.camera.CameraApplication;
import com.imnjh.imagepicker.PickerConfig;
import com.imnjh.imagepicker.SImagePicker;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.pgyersdk.Pgy;
import com.pgyersdk.activity.FeedbackActivity;
import com.pgyersdk.crash.PgyCrashManager;

import org.simple.eventbus.EventBus;

import java.util.List;



public class App extends Application {

    private static App me; // 全局公用
    // 全局公用
    public static App me() {
        return me;
    }


    // 城市代码 默认为珠海
    private int cityCode = 440400;


    // 土司提示控件 全局公用
    private Toast toast;

    // 自定义土司文本控件
    private TextView toastText;

    // 输入法管

    private InputMethodManager input;

    private ActivityManager activityManager;


/*    // 设置登录成功后的用户实体
    public void setUser(User user) {
        DbUtils db = getDb();
        try {
            db.deleteAll(User.class); // 清除全部, 仅保存一条记录
            if (user != null) {
                db.save(user); // 持久化到数据库
            }
        } catch (DbException e) {
            LogUtil.e(App.class, e.getMessage(), e);
        }
        this.user = user;
    }

    // 获取登录的用户实体 如未登录则返回空对象
    public User getUser() {
        if (user == null) {
            DbUtils db = getDb();
            try {
                user = db.findFirst(User.class);
            } catch (DbException e) {
                LogUtil.e(App.class, e.getMessage(), e);
            }
        }
        return user;
    }*/


    // 弹出全局土司, 如提示内容为空则不显示
    public void toast(CharSequence text) {
        if (null!=text){
                if (text == null || text.length() == 0) return;
                if (toast == null) {
                    toastText = new TextView(me);
                    toastText.setBackgroundResource(R.drawable.toast_background);
                    toastText.setTextColor(Color.WHITE);
                    toastText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    toast = Toast.makeText(me, null, Toast.LENGTH_LONG);
                    toast.setView(toastText);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                }

                toastText.setText(text);
                toast.show();
            }
    }

    // 弹出全局土司, 如提示内容为空则不显示
    public void toast(final CharSequence text, Handler handler) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                toast(text);
            }
        });
    }


    // 弹出全局土司, 从字符串资源中获取
    public void toast(int resId) {
        if (resId != 0) toast(me.getResources().getString(resId));
    }

    // 获取输入法管理服务
    public InputMethodManager input() {
        if (input == null) {
            input = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        }
        return input;
    }

    //    // 绑定推送服务, 重复调用则重新绑定
//    public void bindPushService() {
//        User user = getUser();
//        if (user != null) {
//            // 调用 Handler 来异步设置别名
//        }
//        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, "123"));
//    }

    /**
     * 分割 Dex 支持
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        me = this;
        Pgy.setDebug(BuildConfig.DEBUG); // 蒲公英
        PgyCrashManager.register(me = this); // 蒲公英异常捕捉
        FeedbackActivity.setBarImmersive(true); // 蒲公英反馈开启沉浸式布局 未用到该功能
        EventBus.getDefault().register(this);
        CameraApplication.init(this, true); //自定义拍照必须添加
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        SImagePicker.init(new PickerConfig.Builder().setAppContext(this)
                .setImageLoader(new FrescoImageLoader())
                .build());

        try {
        } catch (Exception e) {
            LogUtil.e(App.class, e.getMessage(), e);
        }
    }


    LiteOrm orm;

    public LiteOrm orm() {
        if (orm == null) {
            orm = LiteOrm.newSingleInstance(this, "LocalStorage.db");
            orm.setDebugged(BuildConfig.DEBUG);
        }
        return orm;
    }

    public User getUser() {
        if (user == null) {
            List<User> list = orm().query(QueryBuilder.get(User.class).limit(0, 1));
            if (list.size() > 0) {
                user = list.get(0);
            }
        }
        return user;
    }

    public void login(@NonNull User user) {
        orm().deleteAll(User.class);
        orm().save(this.user = user);
    }

    @NonNull
    User user;

    public User setUser() {
        if (user == null) {
            List<User> list = orm().query(QueryBuilder.get(User.class).limit(0, 1));
            if (list.size() > 0) {
                user = list.get(0);
            }
        }
        return user;
    }

    public void logout() {
        orm().deleteAll(User.class);
        this.user = null;
    }




    public void hideInput(Window window) {
        View view = window.getCurrentFocus();
        hideInput(view);
    }

    public void hideInput(View view) {
        if (view != null) {
            view.clearFocus();
            IBinder binder = view.getWindowToken();
            input().hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void call(Activity activity, final String phone,String ser) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(ser);
        builder.setMessage("您是否现在拨打"+phone+"?");
        builder.setPositiveButton("现在联系", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 调用系统拨号
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:400-671-3913")));

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    /*
        监听GPS
      */
    public void initGPS(final Activity activity) {
        LocationManager locationManager = (LocationManager) activity
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            App.me().toast("请打开GPS");
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("请打开GPS");
            dialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivityForResult(intent, 0); // 设置完成后返回到原来的界面

                        }
                    });
            dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();
        } else {
            // 弹出Toast
//          Toast.makeText(TrainDetailsActivity.this, "GPS is ready",
//                  Toast.LENGTH_LONG).show();
//          // 弹出对话框
//          new AlertDialog.Builder(this).setMessage("GPS is ready")
//                  .setPositiveButton("OK", null).show();
        }
    }


}
