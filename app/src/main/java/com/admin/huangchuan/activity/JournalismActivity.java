package com.admin.huangchuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.admin.huangchuan.App;
import com.admin.huangchuan.R;
import com.admin.huangchuan.View.PullToRefreshLayout;
import com.admin.huangchuan.adapter.TreeViewAdapter;
import com.admin.huangchuan.model.ApiMsg;
import com.admin.huangchuan.model.Element;
import com.admin.huangchuan.model.User;
import com.admin.huangchuan.util.Constant;
import com.alibaba.fastjson.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by Administrator on 2018/3/6 0006.
 */
public class JournalismActivity extends BaseActivity implements PtrHandler {
    @Bind(R.id.img_back)
    ImageView imgBack;

    @Bind(R.id.imgright)
    ImageView imgright;

    @Bind(R.id.pull)
    PullToRefreshLayout pull;

    private ListView treeview;
    /**
     * 树中的元素集合
     */
    private ArrayList<Element> elements;
    /**
     * 数据源元素集合
     */
    private ArrayList<Element> elementsData;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journalism);
        ButterKnife.bind(this);
        setAppTitle("新闻管理");
        init();
        treeview = (ListView) findViewById(R.id.treeview);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(JournalismActivity.this, JournalismDetailsActivity.class);
                startActivity(intent);
            }
        });
    }


    private void init() {
        pull.setPtrHandler(this);
        pull.post(new Runnable() {
            @Override
            public void run() {
                pull.autoRefresh();
            }
        });

    }

    @Subscriber(tag = "JournalismActivity")
    public void Htt(ApiMsg apiMsg) {
        elements = new ArrayList<Element>();
        elementsData = new ArrayList<Element>();
        if (apiMsg.isSuccess()) {
            Log.d("reg", "AreaNameActivity:" + apiMsg.getResult());
            try {
                JSONArray jsonArray = new JSONArray(apiMsg.getResult());
                Log.d("reg", "jsonArray:" + jsonArray.length());
                if (jsonArray.length() <= 0) {
                    App.me().toast("暂无数据");
                }

                for (int i = 0; i < jsonArray.length(); i++) {
                    String o = jsonArray.getString(i);
                    JSONObject object = new JSONObject(o);
                    if (object.getString("level").equals("1")) {
//                        Element e = new Element(object.getString("contentText"), object.getString("level"), object.getString("id"), object.getString("parendId"), ), Boolean.getBoolean(object.getString("isExpanded")));
//                        Log.d("reg", "o:" + e.toString());
                        elements.add(JSON.parseObject(o, Element.class));
                    }
                    //添加最外层节点
                    elementsData.add(JSON.parseObject(o, Element.class));
//                    }

//                     Element e1 = new Element(object.getString("contentText"), object.getInt("level"), object.getInt("id"), object.getInt("parendId"), Boolean.getBoolean(object.getString("hasChildren")), Boolean.getBoolean(object.getString("isExpanded")));
//                    elementsData.add(e);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            final TreeViewAdapter treeViewAdapter = new TreeViewAdapter(elements, elementsData, inflater, 0);
//            TreeViewItemClickListener treeViewItemClickListener = new TreeViewItemClickListener(treeViewAdapter);
            treeview.setAdapter(treeViewAdapter);
            treeview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Element element = (Element) treeViewAdapter.getItem(i);
                    Intent intent = new Intent();
                    intent.setClass(JournalismActivity.this, NewsListActivity.class);
                    intent.putExtra(NewsListActivity.REQUEST_ID, element.getId());
                    intent.putExtra(NewsListActivity.REQUEST_AREA, element.getShowArea());
                    intent.putExtra(NewsListActivity.REQUEST_CONTENT, element.getContentText());
                    startActivityForResult(intent, NewsListActivity.REQUEST_CODE);
                }
            });
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
            final String url = Constant.DOMAIN + "phoneAdminNewsController.do?newsTypeTreeType" + "&uuid" + "=" + user.getUuid();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != myBinder) {
//                            line.setVisibility(View.VISIBLE);
                        myBinder.invokeMethodInMyService(url, "JournalismActivity");
                    }
                }
            }, 1000);
        }
        //结束后调用
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pull.refreshComplete();
            }
        }, 1000);
    }
}
