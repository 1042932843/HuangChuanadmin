package com.admin.huangchuan.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.admin.huangchuan.App;
import com.admin.huangchuan.R;
import com.admin.huangchuan.View.vhtableview.VHTableView;
import com.admin.huangchuan.adapter.VHTableAdapter;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.OnlineEbranch;
import com.admin.huangchuan.model.User;
import com.admin.huangchuan.util.Constant;
import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/3/5 0005.
 */
public class OnlineEbranchFragment extends PagerFragment {

    private Handler handler = new Handler();
    private List<OnlineEbranch> home;
    private VHTableView vht_table;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_information, container, false);
        ButterKnife.bind(this, view);
        vht_table = (VHTableView) view.findViewById(R.id.vht_table);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        final User user = App.me().getUser();
        if (null != user) {
            //结束后调用
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String url = Constant.DOMAIN + "phoneAdminDataCountController.do?getWsezbZhenCount" + "&uuid" + "=" + user.getUuid();
                    myBinder.invokeMethodInMyService(url, "OnlineEbranchFragment");
                }
            }, 1000);
        }
    }


    @Subscriber(tag = "OnlineEbranchFragment")
    public void XinXi(ApiMsg apiMsg) {
        if (home == null) {
            home = new ArrayList<OnlineEbranch>();
        } else {
            home.clear();
        }
        if (apiMsg.isSuccess()) {
            Log.d("reg", "XinXi:" + apiMsg.getResult());
            try {
                JSONObject obj = new JSONObject(apiMsg.getResult());
                JSONArray jsonArray = obj.getJSONArray("list");
                Log.d("reg", "jsonArray:" + jsonArray.length());
                if (jsonArray.length() <= 0) {
                    App.me().toast("暂无数据");
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    String o = jsonArray.getString(i);
                    home.add(JSON.parseObject(o, OnlineEbranch.class));
                }
                //设置数据源
                ArrayList<String> titleData = new ArrayList<>();
                titleData.add("序号");
                titleData.add("乡镇、办事处");
                titleData.add("三会一课");
                titleData.add("四议两公开");
                titleData.add("党务公开");
                titleData.add("合计");

                ArrayList<ArrayList<String>> contentData = new ArrayList<>();


                for (int i = 0; i < home.size(); i++) {
                    ArrayList<String> contentRowData = new ArrayList<>();
                    if (i < 10) {
                        contentRowData.add("0" + i);
                    } else {
                        contentRowData.add(i + "");
                    }
                    contentRowData.add(home.get(i).getZhenName());
                    contentRowData.add(home.get(i).getShykCount());
                    contentRowData.add(home.get(i).getSylgkCount());
                    contentRowData.add(home.get(i).getDwgkCount());
                    contentRowData.add(home.get(i).getValue());
                    contentData.add(contentRowData);
                }

                VHTableAdapter tableAdapter = new VHTableAdapter(getActivity(), titleData, contentData);
                //vht_table.setFirstColumnIsMove(true);//设置第一列是否可移动,默认不可移动
                //vht_table.setShowTitle(false);//设置是否显示标题行,默认显示
                //一般表格都只是展示用的，所以这里没做刷新，真要刷新数据的话，重新setadaper一次吧
                vht_table.setAdapter(tableAdapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
