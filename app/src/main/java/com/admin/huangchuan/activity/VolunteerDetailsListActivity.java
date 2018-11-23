package com.admin.huangchuan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.huangchuan.App;
import com.admin.huangchuan.R;
import com.admin.huangchuan.View.PullToRefreshLayout;
import com.admin.huangchuan.View.SwipeMenu.SwipeMenuListView;
import com.admin.huangchuan.adapter.VolunteerListAdapter;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.User;
import com.admin.huangchuan.model.VolunteerDetailsList;
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
 * Created by Administrator on 2018/4/4 0004.
 */
public class VolunteerDetailsListActivity extends BaseActivity implements View.OnClickListener, PtrHandler {
    public static final String REQUEST_ID = "id";
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.txt_title)
    TextView txtTitle;
    @Bind(R.id.list)
    SwipeMenuListView listView;
    @Bind(R.id.pull)
    PullToRefreshLayout pull;
    private VolunteerListAdapter volunteerlistAdapter;
    private Handler handler = new Handler();
    private int page;
    private boolean hasMore;
    private String id;
    private List<VolunteerDetailsList> home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteerdetailslist);
        ButterKnife.bind(this);
        initViews();
        setAppTitle("志愿者列表");
        id = getIntent().getStringExtra(REQUEST_ID);
    }

    private void initViews() {
        View[] views = {imgBack};
        for (View view : views) {
            view.setOnClickListener(this);
        }
        listView.setAdapter(volunteerlistAdapter = new VolunteerListAdapter(this));
        pull.setPtrHandler(this);
        pull.post(new Runnable() {
            @Override
            public void run() {
                pull.autoRefresh();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
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
            String url = Constant.DOMAIN + "phoneAdminVolunteerController.do?enrollvolunteerList" + "&uuid" + "=" + user.getUuid() + "&activityId" + "=" + id;
            myBinder.invokeMethodInMyService(url, "VolunteerDetailsListActivity");
            //结束后调用
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull.refreshComplete();
                }
            }, 1000);
        }
    }

    @Subscriber(tag = "VolunteerDetailsListActivity")
    public void Http(ApiMsg apiMsg) {
        if (volunteerlistAdapter == null) {
            volunteerlistAdapter = new VolunteerListAdapter(VolunteerDetailsListActivity.this);
        } else {
            volunteerlistAdapter.clear();
        }

            if (home == null) {
                home = new ArrayList<VolunteerDetailsList>();
            } else {
                home.clear();
            }
        if (apiMsg.isSuccess()) {
            Log.d("reg", "api:" + apiMsg.getResult());
            try {
                JSONObject obj = new JSONObject(apiMsg.getResult());
                JSONArray jsonArray = obj.getJSONArray("list");
                if (jsonArray.length() <= 0) {
                    App.me().toast("暂无数据");
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    String o = jsonArray.getString(i);
//                        RedFlag redFlag = JSON.parseObject(o, RedFlag.class);
                    home.add(JSON.parseObject(o, VolunteerDetailsList.class));
                }
                volunteerlistAdapter.addAll(home);
                volunteerlistAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            App.me().toast(apiMsg.getMessage());
        }
    }

}
