package com.admin.huangchuan.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.admin.huangchuan.App;
import com.admin.huangchuan.R;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.Petition;
import com.admin.huangchuan.model.User;
import com.admin.huangchuan.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/8 0008.
 */
public class PetitionDetailsActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_CODE = 'p' + 'e' + 't' + 'i' + 't' + 'i';
    public static final String REQUEST_ID = "id";
    private ImageView back;
    private Handler handler = new Handler();
    private List<Petition> home;
    private TextView title, applicantPhone, applicantName, detail, createDate, date;
    private EditText result;
    private Button qd, sc;
    private LinearLayout line;
    private String url;
    private String id;
    private AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petitiondetails);
        assignViews();
        initViews();
        home = new ArrayList<Petition>();
        setAppTitle("组织信访详情");
        User user = App.me().getUser();
        if (null != user) {
            id = getIntent().getStringExtra(REQUEST_ID);
            url = Constant.DOMAIN + "phoneAdminZzxfController.do?zzxfDetail" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != myBinder) {
                        line.setVisibility(View.VISIBLE);
                        myBinder.invokeMethodInMyService(url, "PetitionDetailsActivity");
                    }
                }
            }, 1000);
        }
    }

    private void assignViews() {
        back = (ImageView) findViewById(R.id.img_back);
        title = (TextView) findViewById(R.id.title);
        applicantPhone = (TextView) findViewById(R.id.applicantPhone);
        applicantName = (TextView) findViewById(R.id.applicantName);
        detail = (TextView) findViewById(R.id.detail);
        result = (EditText) findViewById(R.id.result);
        createDate = (TextView) findViewById(R.id.createDate);
        date = (TextView) findViewById(R.id.date);
        qd = (Button) findViewById(R.id.qd);
        sc = (Button) findViewById(R.id.sc);
        line = (LinearLayout) findViewById(R.id.line);
    }

    private void initViews() {
        View[] views = {back, qd, sc};
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
            case R.id.qd:
                Qd();
                break;
            case R.id.sc:
                Sc();
                break;
        }
    }


    private void Sc() {
        User user = App.me().getUser();
        if (null != user) {
            if (null != id) {
                showAlertDialog("是否确定删除?", id);
            }
        } else {
            startActivityForResult(new Intent(PetitionDetailsActivity.this, LoginActivity.class), LoginActivity.REQUEST_CODE);
        }

    }

    public void showAlertDialog(final String s, final String User_name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(PetitionDetailsActivity.this);
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
                        String url = Constant.DOMAIN + "phoneAdminZzxfController.do?deleteZzxf" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + User_name;
                        myBinder.invokeMethodInMyService(url, "Petitionshanchu");
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

    private void Qd() {

        String resultText = result.getText().toString();
        if (resultText.length() == 0) {
            App.me().toast("请输入处理结果");
            return;
        }
        User user = App.me().getUser();
        if (null != user) {
            String id = getIntent().getStringExtra(REQUEST_ID);
            final String ur = Constant.DOMAIN + "phoneAdminZzxfController.do?finishZzxf" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id + "&result" + "=" + resultText;
            if (null != myBinder) {
                myBinder.invokeMethodInMyService(ur, "Petitionchuli");
            }
        }
    }

    @Subscriber(tag = "Petitionshanchu")

    public void shanchu(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        setResult(RESULT_OK); // 回调登录成功
        finish(); // 返回上一页
    }

    @Subscriber(tag = "Petitionchuli")
    public void chuli(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        setResult(RESULT_OK); // 回调登录成功
        finish(); // 返回上一页
    }

    @Subscriber(tag = "PetitionDetailsActivity")
    public void Http(ApiMsg apiMsg) {
        Log.d("reg", "apiMsg.:" + apiMsg.getResult());
        if (apiMsg.isSuccess()) {
            try {
                JSONObject obj = new JSONObject(apiMsg.getResult());
                title.setText(obj.getString("title"));
                applicantPhone.setText(obj.getString("applicantPhone"));
                applicantName.setText(obj.getString("applicantName"));
                detail.setText(obj.getString("detail"));
                result.setText(obj.getString("result"));
                if (obj.getString("status").equals("0")) {
                    createDate.setText(obj.getString("createDate"));
                    date.setText("提交日期");
//                    createDate.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                    qd.setVisibility(View.VISIBLE);
                    result.setEnabled(true);
                } else if (obj.getString("status").equals("1")) {
                    createDate.setText(obj.getString("resultDate"));
                    date.setText("处理日期");
//                    createDate.setTextColor(this.getResources().getColor(R.color.hei));
                    qd.setVisibility(View.GONE);
                    result.setEnabled(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
