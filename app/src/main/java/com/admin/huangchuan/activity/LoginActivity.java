package com.admin.huangchuan.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.admin.huangchuan.App;
import com.admin.huangchuan.R;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.User;
import com.admin.huangchuan.util.Constant;
import com.admin.huangchuan.util.StringUtil;
import com.alibaba.fastjson.JSON;

import org.simple.eventbus.Subscriber;


/**
 * Created by Administrator on 2017/6/8 0008.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_CODE = 'l' + 'o' + 'g' + 'i' + 'n';
    private ImageView back;
    private EditText phone;
    private EditText password;
    private Button login;
    private String pw;
    private String phoneText;


    private void assignViews() {
        back = (ImageView) findViewById(R.id.back);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
//        pull = (PullToRefreshLayout) findViewById(R.id.pull);
//        webView = (CustomWebView) findViewById(R.id.webView);
    }

    private void initViews(String phone) {
        View[] views = {login, back};
        for (View view : views) {
            view.setOnClickListener(this);
        }
        this.phone.setText(phone);
        if (!TextUtils.isEmpty(phone)) {
            password.requestFocus();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 读取偏好设置: 已登录过
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", null); // 最后一次登录的手机号码
        setContentView(R.layout.activity_login);
        assignViews();
        initViews(phone);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
//                String str ="我来自android";
//                webView.loadUrl("javascript:showAndroid('"+str+"')");
                finish();
                break;
            case R.id.login:
                login(); // 提交登录
                break;
        }
    }

    // 调用登录
    private void login() {
        phoneText = phone.getText().toString();
        if (phoneText.length() == 0) {
            App.me().toast("请输入用户名");
            phone.requestFocus();
            return;
        }

       String passwordText = password.getText().toString();
        if (passwordText.length() == 0) {
            App.me().toast("请输入密码");
            password.requestFocus();
            return;
        }
//        if (!StringUtil.matchesPassword(passwordText)) {
//            App.me().toast("密码格式不正确");
//            password.requestFocus();
//            return;
//        }
        // 调动登录接口
        if (!StringUtil.matchesMd5(passwordText)) {
            // 密码md5
//            pw = StringUtil.md5(passwordText);
        }
        String url = Constant.DOMAIN + "phoneAdminLoginController.do?login" + "&userName" + "=" + phoneText + "&" + "password" + "=" + passwordText;
        myBinder.invokeMethodInMyService(url,"LoginActivity");
    }



    @Subscriber(tag = "LoginActivity")
    public void Http(ApiMsg apiMsg) {
        Log.d("reg", "登录:" + apiMsg.getResult());
        if (apiMsg.isSuccess()) {
            // 登录成功, 保存用户登录信息, 持久化
            User user = JSON.parseObject(apiMsg.getResult(), User.class);
            // 绑定登录用户
            App.me().login(user);
            // 保存偏好设置
            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("phone", phoneText);
            edit.commit();
            App.me().toast("登录成功");
            setResult(RESULT_OK); // 回调登录成功
            finish(); // 返回上一页
        }else {
            App.me().toast(apiMsg.getMessage()); // 登录不成功, 根据接口返回提示
        }
    }

}

