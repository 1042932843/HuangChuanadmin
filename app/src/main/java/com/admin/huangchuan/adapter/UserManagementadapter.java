package com.admin.huangchuan.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.admin.huangchuan.App;
import com.admin.huangchuan.R;
import com.admin.huangchuan.model.UserManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lenovo on 2017/2/27.
 */
public class UserManagementadapter extends BaseAdapter {
    private final Context context;
    private List<UserManagement> list;
    private HashSet<Integer> hs;
    //    private List<RedFlag> olist;

    public UserManagementadapter(Context context) {
        this.context = context;
        this.list = new ArrayList<UserManagement>();
        this.hs = new HashSet<Integer>();

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public UserManagement getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class holder {
        TextView sz;
        TextView phone;
        TextView areaName;
        TextView realName;
        TextView idcard;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        holder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.list_itme_usermanagement, null);
            holder = new holder();

            holder.sz = (TextView) view.findViewById(R.id.sz);
            holder.areaName = (TextView) view.findViewById(R.id.areaName);
            holder.phone = (TextView) view.findViewById(R.id.phone);
            holder.realName = (TextView) view.findViewById(R.id.realName);
            holder.idcard = (TextView) view.findViewById(R.id.idcard);
            view.setTag(holder);
        } else {
            holder = (holder) view.getTag();
        }

        final UserManagement item = list.get(position);
        if (null != item) {
            holder.realName.setText("姓名：" + item.getRealName());
            holder.areaName.setText("村镇名：" + item.getAreaName());
            holder.idcard.setText("身份证号码：" + item.getIdcard());
            holder.phone.setText("电话：" + item.getPhone());
            holder.sz.setText(String.valueOf(position + 1));
            Log.d("reg", "position:" + position);
            holder.phone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    App.me().call((Activity) context, item.getPhone(), "");
                }
            });
        }
        return view;
    }


    public HashSet<Integer> getHs() {
        return hs;
    }

    public void setHs(HashSet<Integer> hs) {
        this.hs = hs;
    }

    public void clear() {
        list.clear();
    }

    public boolean addAll(Collection<? extends UserManagement> collection) {
        boolean pa = list.addAll(collection);
        return pa;
    }


    public boolean add(UserManagement object) {
        return list.add(object);
    }

}
