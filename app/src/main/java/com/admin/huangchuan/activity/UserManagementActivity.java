package com.admin.huangchuan.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.huangchuan.App;
import com.admin.huangchuan.R;
import com.admin.huangchuan.View.OnScrollLastItemListener;
import com.admin.huangchuan.View.OnScrollListener;
import com.admin.huangchuan.View.PullToRefreshLayout;
import com.admin.huangchuan.View.SwipeMenu.SwipeMenu;
import com.admin.huangchuan.View.SwipeMenu.SwipeMenuCreator;
import com.admin.huangchuan.View.SwipeMenu.SwipeMenuItem;
import com.admin.huangchuan.View.SwipeMenu.SwipeMenuListView;
import com.admin.huangchuan.adapter.UserManagementadapter;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.User;
import com.admin.huangchuan.model.UserManagement;
import com.admin.huangchuan.util.Constant;
import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2018/3/5 0005.
 */
public class UserManagementActivity extends BaseActivity implements PtrHandler, View.OnClickListener, OnScrollLastItemListener {


    @Bind(R.id.img_back)
    ImageView imgBack;

    @Bind(R.id.imgright)
    ImageView img_right;
    @Bind(R.id.list)
    SwipeMenuListView listView;
    @Bind(R.id.pull)
    PullToRefreshLayout pull;
    @Bind(R.id.search_name)
    EditText searchName;
    @Bind(R.id.search_phone)
    EditText searchPhone;
    @Bind(R.id.determine)
    Button determine;

    private int page;
    private boolean hasMore;
    private AlertDialog dialog;
    private List<UserManagement> home;
    private Handler handler = new Handler();
    private UserManagementadapter userManagementadapter;
    private  boolean falg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanagement);
        ButterKnife.bind(this);
        assignViews();
        initViews();
        setAppTitle("用户管理");
    }


    private void assignViews() {
        listView.setAdapter(userManagementadapter = new UserManagementadapter(this));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserManagement co = userManagementadapter.getItem(position);
                Intent intent = new Intent(UserManagementActivity.this, UserDetailsActivity.class);
                intent.putExtra(UserDetailsActivity.REQUEST_ID, co.getId());
                intent.putExtra(UserDetailsActivity.REQUEST_TYPE, "1");
                startActivityForResult(intent, UserDetailsActivity.REQUEST_CODE);

            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                openItem.setWidth(dp2px(90));
                openItem.setTitle("删除");
                // set item title fontsize
                openItem.setTitleSize(16);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                UserManagement co = userManagementadapter.getItem(position);
                User user = App.me().getUser();
                switch (index) {
                    case 0:
                        if (null != user) {
                            if (null != co.getIdcard()) {
                                showAlertDialog("是否确定删除?", co.getId());
                            }
                        } else {
                            startActivityForResult(new Intent(UserManagementActivity.this, LoginActivity.class), LoginActivity.REQUEST_CODE);
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void initViews() {
        View[] views = {imgBack, img_right,determine};
        for (View view : views) {
            view.setOnClickListener(this);
        }
        listView.setOnScrollListener(new OnScrollListener(this));
        pull.setPtrHandler(this);
        pull.post(new Runnable() {
            @Override
            public void run() {
                pull.autoRefresh();
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        User user = App.me().getUser();
        falg=true;
        if (null != user) {
            page = 1;
            hasMore = false;
            String url = Constant.DOMAIN + "phoneAdminTSUserController.do?userList" + "&uuid" + "=" + user.getUuid() + "&currentPage" + "=" + page + "";
            myBinder.invokeMethodInMyService(url, "UserManagementActivity");

            //结束后调用
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull.refreshComplete();
                }
            }, 1000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserDetailsActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            pull.post(new Runnable() {
                @Override
                public void run() {
                    pull.autoRefresh();
                }
            });
        }
    }

    @Subscriber(tag = "UserManagement")
    public void shanchu(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        pull.post(new Runnable() {
            @Override
            public void run() {
                pull.autoRefresh();
            }
        });
    }

    @Subscriber(tag = "UserManagementActivity")
    public void Http(ApiMsg apiMsg) {
        if (userManagementadapter == null) {
            userManagementadapter = new UserManagementadapter(UserManagementActivity.this);
        } else {
            userManagementadapter.clear();
        }

        if (page == 1) {
            if (home == null) {
                home = new ArrayList<UserManagement>();
            } else {
                home.clear();
            }
        }
        if (apiMsg.isSuccess()) {
            Log.d("reg", "api:" + apiMsg.getResult());
            try {
                JSONObject obj = new JSONObject(apiMsg.getResult());
                JSONArray jsonArray = obj.getJSONArray("list");
                Log.d("reg", "jsonArray:" + jsonArray.length());
                if (jsonArray.length() <= 0) {
                    App.me().toast("暂无数据");
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    String o = jsonArray.getString(i);
//                        RedFlag redFlag = JSON.parseObject(o, RedFlag.class);
                    home.add(JSON.parseObject(o, UserManagement.class));
                }
                page += 1;
                hasMore = jsonArray.length() >= 10;
                userManagementadapter.addAll(home);
                userManagementadapter.notifyDataSetChanged();
                Log.d("reg", "page:" + page);
                Log.d("reg", "hasMore:" + hasMore);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            App.me().toast(apiMsg.getMessage());
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.imgright:
                Img_Right();
                break;

            case R.id.determine:
                Determine();
                break;
        }
    }


    private  void  Determine(){
        User user = App.me().getUser();
        falg=false;
        if (null != user) {
            page = 1;
            hasMore = false;
            String url = Constant.DOMAIN + "phoneAdminTSUserController.do?userList" + "&uuid" + "=" + user.getUuid() + "&realName" + "=" + searchName.getText().toString() + "&phone" + "=" + searchPhone.getText().toString() +"&currentPage" + "=" + page + "";
            myBinder.invokeMethodInMyService(url, "UserManagementActivity");
            //结束后调用
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull.refreshComplete();
                }
            }, 1000);
        }

    }

    private void Img_Right() {
        Intent intent = new Intent(this, UserDetailsActivity.class);
        intent.putExtra(UserDetailsActivity.REQUEST_TYPE, "2");
        startActivityForResult(intent, UserDetailsActivity.REQUEST_CODE);
    }

    @Override
    public void onScrollLastItem(AbsListView view) {
        User user = App.me().getUser();
        if (null != user) {
            if (falg==true){
                String url = Constant.DOMAIN + "phoneAdminTSUserController.do?userList" + "&uuid" + "=" + user.getUuid() + "&currentPage" + "=" + page + "";
                if (hasMore) {
                    myBinder.invokeMethodInMyService(url, "UserManagementActivity");
                }
            }else if (falg==false){
                String url = Constant.DOMAIN + "phoneAdminTSUserController.do?userList" + "&uuid" + "=" + user.getUuid() + "&realName" + "=" + searchName.getText().toString() + "&phone" + "=" + searchPhone.getText().toString() +"&currentPage" + "=" + page + "";
                if (hasMore) {
                    myBinder.invokeMethodInMyService(url, "UserManagementActivity");
                }
            }



        }
    }

    public void showAlertDialog(final String s, final String User_name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(UserManagementActivity.this);
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
                        String url = Constant.DOMAIN + "phoneAdminTSUserController.do?deleteById" + "&uuid" + "=" + user.getUuid() + "&id" + "=" + User_name;
                        myBinder.invokeMethodInMyService(url, "UserManagement");
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

}
