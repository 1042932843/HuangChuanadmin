package com.admin.huangchuan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.admin.huangchuan.R;
import com.admin.huangchuan.model.Party;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lenovo on 2017/2/27.
 */
public class PartyMemberadapter extends BaseAdapter {
    private final Context context;
    private List<Party> list;
    private HashSet<Integer> hs;
//    private List<RedFlag> olist;

    public PartyMemberadapter(Context context) {
        this.context = context;
        this.list = new ArrayList<Party>();
        this.hs = new HashSet<Integer>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Party getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class holder {
        TextView name;
        TextView title;
        TextView phone;
        TextView date;
        TextView cl;
        TextView cname;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        holder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.list_itme_partymember, null);
            holder = new holder();

            holder.name = (TextView) view.findViewById(R.id.name);
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.phone = (TextView) view.findViewById(R.id.phone);
            holder.date = (TextView) view.findViewById(R.id.date);
            holder.cl = (TextView) view.findViewById(R.id.cl);
            holder.cname = (TextView) view.findViewById(R.id.cname);
            view.setTag(holder);
        } else {
            holder = (holder) view.getTag();
        }

        Party item = list.get(position);
        if (null != item) {
            holder.title.setText(item.getDetail());
            holder.cname.setText("村镇名：" + item.getAreaName());
            holder.name.setText("姓名：" + item.getName());
            holder.phone.setText("电话：" + item.getPhone());


            if (null != item.getStatus()) {
                if (item.getStatus().equals("0")) {
                    holder.cl.setText("未处理");
                    holder.date.setText("提交日期：" + item.getCreateDate());
                    holder.cl.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                } else if (item.getStatus().equals("1")) {
                    holder.cl.setText("已处理");
                    holder.date.setText("处理日期：" + item.getResultDate());
                    holder.cl.setTextColor(context.getResources().getColor(R.color.hei));
                }
            }

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

    public boolean addAll(Collection<? extends Party> collection) {
        boolean pa = list.addAll(collection);
        return pa;
    }


    public boolean add(Party object) {
        return list.add(object);
    }

}
