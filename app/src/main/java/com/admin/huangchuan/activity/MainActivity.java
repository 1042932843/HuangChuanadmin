package com.admin.huangchuan.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.huangchuan.App;
import com.admin.huangchuan.R;
import com.admin.huangchuan.View.TopMiddlePopup;
import com.admin.huangchuan.fragment.InformationStatisticsFragment;
import com.admin.huangchuan.fragment.PartyRepresentativeFragment;
import com.admin.huangchuan.listener.PgyUpdateManagerListener;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.User;
import com.admin.huangchuan.util.Constant;
import com.admin.huangchuan.util.LogUtil;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.update.PgyUpdateManager;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    //region 已启动过或重新启动时则不再显示启动页面
    // 例:小米打开相机后被activity结束,返回App时为重启
    private boolean started;
    private boolean restarted;
    private long onBackPressedTimeMillis; // 按下返回键的时间戳
    private RelativeLayout zzxf_rela, ddb_rela,ztz_rela,xx_rela,uese_rela,xw_rela;
    private Button button;
    private ImageView dl;
    public static int screenW, screenH;
    private TopMiddlePopup middlePopup;
    private TextView topLineTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getScreenPixels();
        started = savedInstanceState != null && savedInstanceState.getBoolean("started", false); // 是否启动过
        restarted = savedInstanceState != null && savedInstanceState.getBoolean("restarted", false); // 是否为重启
        if (!started) { // 如未启动过
            started = true; // 设置为已启动

        }
        //结束后调用
        try
        {
            PgyUpdateManager.register(this, new PgyUpdateManagerListener(this));
        } catch (
                Exception e
                )

        {
            PgyCrashManager.reportCaughtException(this, e);
            LogUtil.e(MainActivity.class, "检查更新失败", e);
            App.me().toast("检查更新失败");
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setAppTitle("首页");
        initView();
        assignViews();
        checkLogin();
    }

    private void checkLogin() {
        User login = App.me().getUser();
        if (null == login) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void initView() {
        dl = (ImageView) findViewById(R.id.dl);
        xx_rela = (RelativeLayout) findViewById(R.id.xx_rela);
        xw_rela = (RelativeLayout) findViewById(R.id.xw_rela);
        uese_rela = (RelativeLayout) findViewById(R.id.uese_rela);
        zzxf_rela = (RelativeLayout) findViewById(R.id.zzxf_rela);
        ddb_rela = (RelativeLayout) findViewById(R.id.ddb_rela);
        ztz_rela = (RelativeLayout) findViewById(R.id.ztz_rela);
        topLineTv = (TextView) findViewById(R.id.rule_line_tv);
        button = (Button) findViewById(R.id.tuichu);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final User user = App.me().getUser();
                if (null != user) {
                    String url = Constant.DOMAIN + "phoneAdminLoginController.do?logout" + "&uuid" + "=" + user.getUuid();
                    myBinder.invokeMethodInMyService(url, "MainActivity");
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("reg", "onResume");
        final User user = App.me().getUser();
        if (null != user) {
            button.setText("退出");
        } else {
            button.setText("登录");
        }
    }

    private void assignViews() {
        View[] views = {zzxf_rela, ddb_rela,ztz_rela,xx_rela,dl,uese_rela,xw_rela};
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onBackPressed() { // 连续按下两次返回键才退出App
        long currentTimeMillis = System.currentTimeMillis();
        if (onBackPressedTimeMillis != 0 && currentTimeMillis - onBackPressedTimeMillis < 3000) {
            super.onBackPressed();
        } else {
            App.me().toast("再按一次返回键退出");
//            boolean oo1 = AntiEmulator.CheckEmulatorBuild();
//            String s1 = String.valueOf(oo1);
//            App.me().toast("是Build："+ s1 );

        }
        onBackPressedTimeMillis = currentTimeMillis;
    }

    @Subscriber(tag = "MainActivity")
    public void Http(ApiMsg apiMsg) {
        if (apiMsg.isSuccess()) {
            App.me().logout();
            App.me().toast("退出成功");
            startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), LoginActivity.REQUEST_CODE);
        } else {
            App.me().toast(apiMsg.getMessage());
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //得到了授权
                    Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                    try {
                        PgyUpdateManager.register(this, new PgyUpdateManagerListener(this));
                    } catch (Exception e) {
                        PgyCrashManager.reportCaughtException(this, e);
                        LogUtil.e(MainActivity.class, "检查更新失败", e);
                        App.me().toast("检查更新失败");
                    }
                } else {
                    //未授权
                    Toast.makeText(this, "您已禁止自动更新", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("https://www.pgyer.com/Yl8e");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }



    @Override
    public void onClick(View view) {
        User user = App.me().getUser();
        if (null != user) {
            Intent intent = new Intent();
            switch (view.getId()) {
                case R.id.zzxf_rela:
                    intent.setClass(this, PetitionActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ddb_rela:
                    intent.setClass(this, PartyRepresentativeFragment.class);
                    startActivity(intent);
                    break;
                case R.id.ztz_rela:
                    intent.setClass(this, VolunteerActivity.class);
                    startActivity(intent);
                    break;

                case R.id.xx_rela:
                    intent.setClass(this, InformationStatisticsFragment.class);
                    startActivity(intent);
                    break;

                case R.id.dl:
                    setPopup(1);
                    middlePopup.show(topLineTv);
                    break;

                case R.id.uese_rela:
                    intent.setClass(this, UserManagementActivity.class);
                    startActivity(intent);
                    break;

                case R.id.xw_rela:
                    intent.setClass(this, JournalismActivity.class);
                    startActivity(intent);
                    break;

            }
        } else {
            switch (view.getId()) {
                case R.id.xw_rela:
                case R.id.zzxf_rela:
                case R.id.ddb_rela:
                case R.id.ztz_rela:
                case R.id.xx_rela:
                case R.id.uese_rela:
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), LoginActivity.REQUEST_CODE);
                    break;
                case R.id.dl:
                    setPopup(1);
                    middlePopup.show(topLineTv);
                    break;
            }
        }


    }

    /**
     * 设置弹窗
     *
     * @param type
     */
    private void setPopup(int type) {
        middlePopup = new TopMiddlePopup(MainActivity.this, screenW, screenH,
                onItemClickListener, getItemsName(), type);
    }

    /**
     * 设置弹窗内容
     *
     * @return
     */
    private ArrayList<String> getItemsName() {
        ArrayList<String> items = new ArrayList<String>();

        final User user = App.me().getUser();
        if (null != user) {
//            items.add("修改密码 ");
            items.add("   注   销   ");
        } else {
             items.add("  登    录  ");
        }
        return items;
    }


    /**
     * 获取屏幕的宽和高
     */
    public void getScreenPixels() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenW = metrics.widthPixels;
        screenH = metrics.heightPixels;
    }

    /**
     * 弹窗点击事件
     */
    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            System.out.println("--onItemClickListener--:");
            final User user = App.me().getUser();
            switch (position){
                case 0:
                    if (null != user) {
                        String url = Constant.DOMAIN + "phoneAdminLoginController.do?logout" + "&uuid" + "=" + user.getUuid();
                        myBinder.invokeMethodInMyService(url, "MainActivity");
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                    break;
            }
            middlePopup.dismiss();
        }
    };

    @Override
    protected void onDestroy() {
        PgyUpdateManager.unregister(); // 注销蒲公英更新监听
        super.onDestroy();
    }

}
