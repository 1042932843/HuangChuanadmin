package com.admin.huangchuan.fragment;

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
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
import com.admin.huangchuan.activity.LoginActivity;
import com.admin.huangchuan.activity.PartyDetailsActivity;
import com.admin.huangchuan.adapter.PartyMemberadapter;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.Party;
import com.admin.huangchuan.model.User;
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
 * Created by Administrator on 2018/2/27 0027.
 */
public class PartyMemberFragment extends PagerFragment implements PtrHandler, View.OnClickListener, OnScrollLastItemListener {

    @Bind(R.id.list)
    SwipeMenuListView listView;
    @Bind(R.id.pull)
    PullToRefreshLayout pull;
    private int page;
    private boolean hasMore;
    private Handler handler = new Handler();
    private PartyMemberadapter partyMemberadapter;
    private List<Party> home;
    private AlertDialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_partymember, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initViews();
        listView.setAdapter(partyMemberadapter = new PartyMemberadapter(getActivity()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Party co = partyMemberadapter.getItem(position);
                Intent intent = new Intent(getActivity(), PartyDetailsActivity.class);
                intent.putExtra(PartyDetailsActivity.REQUEST_ID, co.getId());
                intent.putExtra(PartyDetailsActivity.REQUEST_TYPE, "2");
                startActivityForResult(intent, PartyDetailsActivity.REQUEST_CODE);

            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity());
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
                Party co = partyMemberadapter.getItem(position);
                User user = App.me().getUser();
                switch (index) {
                    case 0:
                        if (null != user) {
                            if (null != co.getId()) {
                                showAlertDialog("是否确定删除?", co.getId());
                            }
                        } else {
                            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.REQUEST_CODE);
                        }
                        break;
                }
                return false;
            }
        });
    }


    public void showAlertDialog(final String s, final String User_name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    @Subscriber(tag = "Petitionshanchu")
    public void shanchu(ApiMsg apiMsg) {
        App.me().toast(apiMsg.getMessage());
        pull.post(new Runnable() {
            @Override
            public void run() {
                pull.autoRefresh();
            }
        });
    }

    private void initViews() {
        View[] views = {};
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onScrollLastItem(AbsListView view) {
        User user = App.me().getUser();
        if (null != user) {
            String url = Constant.DOMAIN + "phoneAdminDdbgzsController.do?ddbgzsDyList" + "&uuid" + "=" + user.getUuid() + "&currentPage" + "=" + page + "";
            if (hasMore) {
                myBinder.invokeMethodInMyService(url, "PartyMemberFragment");
            }

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
            String url = Constant.DOMAIN + "phoneAdminDdbgzsController.do?ddbgzsDyList" + "&uuid" + "=" + user.getUuid() + "&currentPage" + "=" + page + "";
            myBinder.invokeMethodInMyService(url, "PartyMemberFragment");

            //结束后调用
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pull.refreshComplete();
                }
            }, 1000);
        }
    }
    @Subscriber(tag = "PartyMemberFragment")
    public void TheMassesFragment(ApiMsg apiMsg) {
        if (partyMemberadapter == null) {
            partyMemberadapter = new PartyMemberadapter(getActivity());
        } else {
            partyMemberadapter.clear();
        }

        if (page == 1) {
            if (home == null) {
                home = new ArrayList<Party>();
            } else {
                home.clear();
            }
        }
        if (apiMsg.isSuccess()) {
            Log.d("reg", "PartyMemberFragment:" + apiMsg.getResult());
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
                    home.add(JSON.parseObject(o, Party.class));
                }
                page += 1;
                hasMore = jsonArray.length() >= 10;
                partyMemberadapter.addAll(home);
                partyMemberadapter.notifyDataSetChanged();
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
    protected void onPageSelected(boolean firstTime) {
        super.onPageSelected(firstTime);
        pull.autoRefresh();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PartyDetailsActivity.REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            pull.post(new Runnable() {
                @Override
                public void run() {
                    pull.autoRefresh();
                }
            });
        }
    }
}
