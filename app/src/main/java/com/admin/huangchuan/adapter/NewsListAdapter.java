package com.admin.huangchuan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.admin.huangchuan.R;
import com.admin.huangchuan.model.NewsList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lenovo on 2017/2/27.
 */
public class NewsListAdapter extends BaseAdapter {
    private final Context context;
    private List<NewsList> list;
    private HashSet<Integer> hs;
    //    private List<RedFlag> olist;

    public NewsListAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<NewsList>();
        this.hs = new HashSet<Integer>();

    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public NewsList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class holder {
        TextView newstitle;
        TextView areaName;
        TextView createDate;
        TextView author;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        holder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.list_itme_newslist, null);
            holder = new holder();

            holder.newstitle = (TextView) view.findViewById(R.id.newstitle);
            holder.areaName = (TextView) view.findViewById(R.id.areaName);
            holder.createDate = (TextView) view.findViewById(R.id.createDate);
            holder.author = (TextView) view.findViewById(R.id.author);
            view.setTag(holder);
        } else {
            holder = (holder) view.getTag();
        }

        final NewsList item = list.get(position);
        if (null != item) {
            holder.newstitle.setText("标题：" + item.getNewstitle());
//            holder.areaName.setText("所属党支部：" + item.getAreaName());
            holder.createDate.setText("发布时间：" + item.getCreateDate());
            holder.author.setText("发布者：" + item.getAuthor());
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

    public boolean addAll(Collection<? extends NewsList> collection) {
        boolean pa = list.addAll(collection);
        return pa;
    }


    public boolean add(NewsList object) {
        return list.add(object);
    }

}
