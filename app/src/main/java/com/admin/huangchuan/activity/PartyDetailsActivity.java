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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.admin.huangchuan.App;
import com.admin.huangchuan.R;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.User;
import com.admin.huangchuan.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/8 0008.
 */
public class PartyDetailsActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_CODE = 'p' + 'e' + 't' + 'i' + 't' + 'i';
    public static final String REQUEST_ID = "id";
    public static final String REQUEST_TYPE = "type";
    @Bind(R.id.type)
    TextView type;
    @Bind(R.id.detail)
    TextView detail;
    @Bind(R.id.areaName)
    TextView areaName;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.date)
    TextView date;
    @Bind(R.id.createDate)
    TextView createDate;

    @Bind(R.id.ddb)
    TextView ddb;
    @Bind(R.id.ddblayout)
    LinearLayout ddblayout;
    @Bind(R.id.ddbname)
    TextView ddbname;
    @Bind(R.id.result)
    EditText result;
    private ImageView back;
    private Handler handler = new Handler();
    //    private List<PartyDetails> home;
    private Button qd, sc;
    private LinearLayout line;
    private String Detailurl;
    private String id;
    private AlertDialog dialog;
    private String part_type;
    private String finishurl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partydetails);
        ButterKnife.bind(this);
        assignViews();
        initViews();
//        home = new ArrayList<PartyDetails>();
        setAppTitle("党代表工作室详情");
        User user = App.me().getUser();
        if (null != user) {
            id = getIntent().getStringExtra(REQUEST_ID);
            part_type = getIntent().getStringExtra(REQUEST_TYPE);
            if (part_type.equals("1")) { //1是群众
                Detailurl = Constant.DOMAIN + "phoneAdminDdbgzsController.do?ddbgzsQzDetail" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id;
            } else if (part_type.equals("2")) {//2是党员
                Detailurl = Constant.DOMAIN + "phoneAdminDdbgzsController.do?ddbgzsDyDetail" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id;
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != myBinder) {
                        line.setVisibility(View.VISIBLE);
                        myBinder.invokeMethodInMyService(Detailurl, "PartyDetailsActivity");
                    }
                }
            }, 1000);
        }
    }

    private void assignViews() {
        back = (ImageView) findViewById(R.id.img_back);
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
            startActivityForResult(new Intent(PartyDetailsActivity.this, LoginActivity.class), LoginActivity.REQUEST_CODE);
        }

    }

    public void showAlertDialog(final String s, final String User_name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(PartyDetailsActivity.this);
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
                        String url = Constant.DOMAIN + "phoneAdminDdbgzsController.do?deleteDdbgzs" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + User_name;
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

            if (part_type.equals("1")) { //1是群众
                finishurl = Constant.DOMAIN + "phoneAdminDdbgzsController.do?finishDdbgzsQz" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id + "&result" + "=" + resultText;
            } else if (part_type.equals("2")) {//2是党员
                finishurl = Constant.DOMAIN + "phoneAdminDdbgzsController.do?finishDdbgzsDy" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + id + "&result" + "=" + resultText;
            }


            if (null != myBinder) {
                myBinder.invokeMethodInMyService(finishurl, "Petitionchuli");
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

    @Subscriber(tag = "PartyDetailsActivity")
    public void Http(ApiMsg apiMsg) {
        Log.d("reg", "PartyDetailsActivity.:" + apiMsg.getResult());
        if (apiMsg.isSuccess()) {
            try {
                JSONObject obj = new JSONObject(apiMsg.getResult());
                if (null != obj.getString("name")) {
                    name.setText(obj.getString("name"));
                }
                if (null != obj.getString("phone")) {
                    phone.setText(obj.getString("phone"));
                }
                if (null != obj.getString("areaName")) {
                    areaName.setText(obj.getString("areaName"));
                }

                if (null != obj.getString("ddbName")&&!"".equals(obj.getString("ddbName"))) {
                    ddblayout.setVisibility(View.VISIBLE);
                    ddb.setText("党代表姓名");
                    ddbname.setText(obj.getString("ddbName"));
                }else{
                    ddblayout.setVisibility(View.GONE);
                }


                if (null != obj.getString("detail")) {
                    detail.setText(obj.getString("detail"));
                }

                if (null != obj.getString("result")) {
                    result.setText(obj.getString("result"));
                }

                if (null != obj.getString("name")) {
                    name.setText(obj.getString("name"));
                }

                if (null != obj.getString("type")) {
                    if (obj.getString("type").equals("1")) {
                        type.setText("社情民意");
                    } else if (obj.getString("type").equals("2")) {
                        type.setText("约见党代表");
                    }
                }


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
