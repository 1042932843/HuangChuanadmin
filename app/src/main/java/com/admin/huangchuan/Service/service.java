package com.admin.huangchuan.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.admin.huangchuan.App;
import com.admin.huangchuan.activity.LoginActivity;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.util.HttpUtil;

import org.simple.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;

/**
 * how to create a websocket connection to a server. Only the most important callbacks are overloaded.
 */
public class service extends Service {

    private Httprequest httprequest;
    public String Ke;

    public service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //返回MyBind对象
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        httprequest = new Httprequest(this);
        Log.d("reg", "1");
    }

    private void methodInMyService(String url, String Key) {
        if (null != url) {
            httprequest.http(url, Key);
        }
    }

    private void methodInMyServiceimg(String url, Map<String, String> params, String Key) {
        if (null != url) {
            httprequest.httpimg(url, params, Key);
        }
    }

    /**
     * 该类用于在onBind方法执行后返回的对象，
     * 该对象对外提供了该服务里的方法
     */
    private class MyBinder extends Binder implements IMyBinder {

        @Override
        public void invokeMethodInMyService(String url, String Key) {
            methodInMyService(url, Key);
        }

        @Override
        public void invokeMethodInMyServiceimg(String url, Map<String, String> params, String Key) {
            methodInMyServiceimg(url, params, Key);
        }


    }

    public interface IMyBinder {

        void invokeMethodInMyService(String url, String Key);

        void invokeMethodInMyServiceimg(String url, Map<String, String> params, String Key);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Bundle bundle = intent.getExtras();
//        if (bundle != null) {
//            String Key = bundle.getString("Key");
//            String Url = bundle.getString("Url");
//            Log.d("reg", "url:" + Url);
//            if (null != Url) {
//                httprequest.http(Url, Key);
//            }
//        }
        return super.onStartCommand(intent, flags, startId);
    }


    //服务摧毁时
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    class Httprequest extends HttpUtil {

        public Httprequest(Context context) {
            super(context);
        }

        public void http(String url, String Key) {
            Ke = Key;
            super.post(url);
        }

        public void httpimg(String url, Map<String, String> params, String Key) {
            Ke = Key;
            super.postComplexForm(url, params);
        }


        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, ApiMsg apiMsg) throws IOException {
            //把从接口拿到的值传给key来传值
            Log.d("reg", "Ke:" + Ke);
            Log.d("reg", "apiMsg:" + apiMsg.getMessage());
            if (apiMsg.getMessage().indexOf("请登录") != -1) {
                App.me().toast("请重新登录！");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                EventBus.getDefault().post(apiMsg, Ke);
                Log.d("reg", "接口返回值:" + apiMsg.getResult());
            }

        }
    }
}