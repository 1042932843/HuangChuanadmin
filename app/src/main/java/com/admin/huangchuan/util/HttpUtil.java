

package com.admin.huangchuan.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;

import com.admin.huangchuan.App;
import com.admin.huangchuan.activity.MainActivity;
import com.admin.huangchuan.model.ApiMsg;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.HttpException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Http请求工具类
 */
public abstract class HttpUtil implements DialogInterface.OnCancelListener {

    protected ProgressDialog dialog;
    protected Handler dialogHandler;
    protected long dialogShowTime;
    protected RequestHandle requestHandle;
    OkHttpClient okHttpClient = new OkHttpClient();
    List<String> keys;
    List<String> values;
    Context context;

    //加seesion
    public final void post(String url, Object... paramsKeyAndValue) {
        showDialog();
        keys = new ArrayList<>();
        values = new ArrayList<>();
        StringBuilder builder = null;
        if (paramsKeyAndValue != null && paramsKeyAndValue.length != 0) {
            if (paramsKeyAndValue.length % 2 == 0) {
                builder = new StringBuilder();
                for (int i = 0; i < paramsKeyAndValue.length; i += 2) {
                    Object key = paramsKeyAndValue[i];
                    Object value = paramsKeyAndValue[i + 1];
                    if (key != null && value != null) {
                        builder.append(key);
                        builder.append("=");
                        builder.append(value);
                        keys.add("" + key);
                        values.add("" + value);
                    }
                }
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (requestHandle != null) {
            requestHandle.cancel(true);
        }

//        String seesion = (String) SPUtils.get(App.me(), "session", "");
//        Log.i("info_Login", "得到了session：" + seesion);
//        FormBody.Builder bodyBuilder = new FormBody.Builder();
//        for (int i = 0; i < keys.size(); i++) {
//            bodyBuilder = bodyBuilder.add(keys.get(i), values.get(i));
//        }
//
//        FormBody body = bodyBuilder.build();


        Request request;
//        if (url.indexOf("login.do") != -1) {
//            Log.d("reg","1");
//            request = new Request.Builder()
//                    .url(url)
//                    .post(body)
//                    .build();
//        } else {
//            Log.d("reg","2");
//            if (seesion != "") {
//                request = new Request.Builder()
//                        .addHeader("cookie", seesion)
//                        .url(url)
//                        .post(body)
//                        .build();
//            } else {
//
//            }
//        }
        request = new Request.Builder()
                .url(url)
//                .post(body)
                .build();

        String send = url;
//        for (int i = 0; i < body.size(); i++) {
//            if (i == 0) {
//                send = send + "?" + body.encodedName(i) + "=" + body.encodedValue(i);
//            } else {
//                send = send + "&" + body.encodedName(i) + "=" + body.encodedValue(i);
//            }
//        }
        Log.d("reg", "send: " + send);

        Call call2 = okHttpClient.newCall(request);

        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onFinish();
                HttpUtil.this.onFailure(e);
                HttpUtil.this.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    onFinish();
                    String re = response.body().string();
                    Log.d("reg", "接口返回:" + re);

                    try {
                        ApiMsg apiMsg = JSON.parseObject(re, ApiMsg.class);
                        String state = apiMsg.getState();
                        if (state != null && state.equals("0001")) {
//                App.me().setUser(null);
                        }

                        if (apiMsg.getMessage().indexOf("请登录") != -1) {
                            App.me().logout();
                            App.me().toast("请重新登录！",dialogHandler);
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            return;
                        }


                        HttpUtil.this.onResponse(call, apiMsg);
                    } catch (Exception e) {
                        HttpUtil.this.onFailure(e);
                    }

                } catch (Exception e) {
                    Log.e("OK", "e" + e.getMessage());
                }
            }
        });
    }


    public abstract void onFailure(Call call, IOException e);

    public abstract void onResponse(Call call, ApiMsg response) throws IOException;


    public HttpUtil() {
        dialog = null;
        dialogHandler = null;
    }

    protected HttpUtil(Context context) {
        this(context, "加载中...");

    }

    protected HttpUtil(Context context, String message) {
        if (context != null) {
            this.context =context;
            dialogHandler = new Handler();
            dialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
            dialog.setMessage(message);
            dialog.setOnCancelListener(this);
            // 设置成系统级对话框
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
    }


    public void onFailure(Exception e) {
        LogUtil.e(HttpUtil.class, e.getMessage(), e);
        Looper.prepare();
        if (e instanceof HttpException) {
            App.me().toast("网络不可用",dialogHandler);
        } else if (e instanceof SocketTimeoutException) {
            App.me().toast("网络请求超时",dialogHandler);
        } else if (e instanceof JSONException) {
            App.me().toast("数据解析错误",dialogHandler);
        } else if (e instanceof NullPointerException) {
            App.me().toast("程序错误",dialogHandler);
        } else if (e instanceof ConnectException) {
            App.me().toast("服务器异常或者检查你的网络是否正常",dialogHandler);
        } else {
            App.me().toast("未知错误",dialogHandler);
        }
        Looper.loop();
    }


    public void onFinish() {
        onFinished();
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        onFinish();
    }

    public void showDialog() {
        if (dialog != null) {
            try {
                dialogShowTime = System.currentTimeMillis();
                dialog.show();
            } catch (Exception e) {
                LogUtil.e(HttpUtil.class, e.getMessage(), e);
            }
        }
    }

    public void onFinished() {
        if (dialogHandler != null && dialog != null && dialog.isShowing()) {
            dialogHandler.removeCallbacks(null);
            long delayMillis = System.currentTimeMillis() - dialogShowTime;
            if (delayMillis < 1000) {
                delayMillis = 1000;
            } else {
                delayMillis = 0;
            }
            dialogHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideDialog();
                }
            }, delayMillis);
        }
    }

    public void hideDialog() {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                LogUtil.e(HttpUtil.class, e.getMessage(), e);
            }
        }
    }


    /**
     * post表单请求提交,获取json字符串
     *
     * @param url
     * @param params
     */
    public void postComplexForm(String url, Map<String, String> params) {
        showDialog();
        FormBody.Builder builder = new FormBody.Builder();//表单对象，包含以input开始的对象，以html表单为主
        //把map集合中的参数添加到FormBody表单中.
        if (null != params && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
            RequestBody requestBody = builder.build();//创建请求体
            Request request = new Request.Builder().url(url).post(requestBody).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    onFinish();
                    HttpUtil.this.onFailure(e);
                    HttpUtil.this.onFailure(call, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (null != response && response.isSuccessful()) {
                        try {
                            onFinish();
                            String re = response.body().string();
                            Log.d("reg", "接口返回:" + re);
//                            if (null!=re){
//                                if (re.equals("请登录")){
//                                    startActivityForResult(new Intent(, LoginActivity.class), LoginActivity.REQUEST_CODE);
//                                }
//                            }

                            try {
                                ApiMsg apiMsg = JSON.parseObject(re, ApiMsg.class);


                                String state = apiMsg.getState();

                                if (apiMsg.getMessage().indexOf("请登录") != -1) {
                                    App.me().logout();
                                    App.me().toast("请重新登录！",dialogHandler);
                                    Intent intent = new Intent(context, MainActivity.class);
                                    context.startActivity(intent);
                                    return;
                                }


                                if (state != null && state.equals("0001")) {
                         //                App.me().setUser(null);
                                }

                                HttpUtil.this.onResponse(call, apiMsg);
                            } catch (Exception e) {
                                HttpUtil.this.onFailure(e);
                            }

                        } catch (Exception e) {
                            Log.e("OK", "e" + e.getMessage());
                        }
                    }
                }
            });

        }
    }

}
