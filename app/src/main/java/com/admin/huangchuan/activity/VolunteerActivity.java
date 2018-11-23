package com.admin.huangchuan.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import com.admin.huangchuan.adapter.VolunteerAdapter;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.User;
import com.admin.huangchuan.model.Volunteer;
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
 * Created by Administrator on 2017/6/16 0016.
 */
public class VolunteerActivity extends BaseActivity implements View.OnClickListener, PtrHandler, OnScrollLastItemListener, AdapterView.OnItemClickListener {
    @Bind(R.id.img_right)
    ImageView imgRight;
    private PullToRefreshLayout pull;
    private SwipeMenuListView listView;
    private ImageView back;
    private Handler handler = new Handler();
    private VolunteerAdapter volunteerAdapter;
    private List<Volunteer> home;
    private AlertDialog dialog;
    private int page;
    private boolean hasMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);
        ButterKnife.bind(this);
        setAppTitle("志愿者活动列表");
        assignViews();
        initViews();
    }


    private void assignViews() {
        pull = (PullToRefreshLayout) findViewById(R.id.pull);
        listView = (SwipeMenuListView) findViewById(R.id.list);
        back = (ImageView) findViewById(R.id.img_back);
        listView.setAdapter(volunteerAdapter = new VolunteerAdapter(this));
    }

    private void initViews() {
        View[] views = {back,imgRight};
        for (View view : views) {
            view.setOnClickListener(this);
        }
        listView.setOnScrollListener(new OnScrollListener(this));
        listView.setOnItemClickListener(this);
        pull.setPtrHandler(this);
        pull.post(new Runnable() {
            @Override
            public void run() {
                pull.autoRefresh();
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
                Volunteer co = volunteerAdapter.getItem(position);
                User user = App.me().getUser();
                switch (index) {
                    case 0:
                        if (null != user) {
                            if (null != co.getId()) {
                                showAlertDialog("是否确定删除?", co.getId());
                            }
                        } else {
                            startActivityForResult(new Intent(VolunteerActivity.this, LoginActivity.class), LoginActivity.REQUEST_CODE);
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void showAlertDialog(final String s, final String User_name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(VolunteerActivity.this);
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
                        myBinder.invokeMethodInMyService(url, "Volunteershanchu");
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

    @Subscriber(tag = "Volunteershanchu")
    public void shanchu(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_right:
                Intent intent = new Intent(VolunteerActivity.this, VolunteerDetailsActivity.class);
                intent.putExtra(VolunteerDetailsActivity.REQUEST_TYPE, "2");
                startActivityForResult(intent, VolunteerDetailsActivity.REQUEST_CODE);
                break;
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }


    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        User user = App.me().getUser();
        if (null != user) {
            page = 1;
            hasMore = false;
            String url = Constant.DOMAIN + "phoneAdminVolunteerController.do?volunteerActivitiesList" + "&uuid" + "=" + user.getUuid() + "&currentPage" + "=" + page + "";
            myBinder.invokeMethodInMyService(url, "VolunteerActivity");
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
    public void onScrollLastItem(AbsListView view) {
        User user = App.me().getUser();
        if (null != user) {
            String url = Constant.DOMAIN + "phoneAdminVolunteerController.do?volunteerActivitiesList" + "&uuid" + "=" + user.getUuid() + "&currentPage" + "=" + page + "";
            if (hasMore) {
                myBinder.invokeMethodInMyService(url, "VolunteerActivity");
            }

        }
    }


    @Subscriber(tag = "VolunteerActivity")
    public void Http(ApiMsg apiMsg) {
        if (volunteerAdapter == null) {
            volunteerAdapter = new VolunteerAdapter(VolunteerActivity.this);
        } else {
            volunteerAdapter.clear();
        }

        if (page == 1) {
            if (home == null) {
                home = new ArrayList<Volunteer>();
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
                    home.add(JSON.parseObject(o, Volunteer.class));
                }
                page += 1;
                hasMore = jsonArray.length() >= 10;
                volunteerAdapter.addAll(home);
                volunteerAdapter.notifyDataSetChanged();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Volunteer volunteer = home.get(position);
        Intent intent = new Intent(VolunteerActivity.this, VolunteerDetailsActivity.class);
        intent.putExtra(VolunteerDetailsActivity.REQUEST_ID, volunteer.getId());
        intent.putExtra(VolunteerDetailsActivity.REQUEST_TYPE, "1");
        startActivityForResult(intent, VolunteerDetailsActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VolunteerDetailsActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            pull.post(new Runnable() {
                @Override
                public void run() {
                    pull.autoRefresh();
                }
            });
        }
    }
}
