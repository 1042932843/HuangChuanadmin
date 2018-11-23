package com.admin.huangchuan.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.huangchuan.R;
import com.admin.huangchuan.model.Volunteer;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lenovo on 2017/2/27.
 */
public class VolunteerAdapter extends BaseAdapter {
    private final Context context;
    private List<Volunteer> list;
    private HashSet<Integer> hs;
//    private List<RedFlag> olist;

    public VolunteerAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<Volunteer>();
        this.hs = new HashSet<Integer>();
//        this.olist = list;
    }

/*    public void SearchCity(String city) {
        this.list = Search(city);
        notifyDataSetChanged();
    }*/

 /*   private List<Ranks> Search(String city) {
        if (city != null && city.length() > 0) {
            ArrayList<Ranks> area = new ArrayList<Ranks>();
            for (Ranks a : this.olist) {
                if (a.getNewsTitle().indexOf(city) != -1) {
                    area.add(a);
                }
            }
            return area;
        } else {
            return this.olist;
        }

    }*/
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Volunteer getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class holder{
        TextView ranks_tiele;
        TextView status;
        TextView date;
        TextView addr;
         ImageView img;

    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        holder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.itme_list_volunteer, null);
            holder=new holder();
            holder.ranks_tiele = (TextView) view.findViewById(R.id.text);
            holder.status = (TextView) view.findViewById(R.id.status);
            holder.date = (TextView) view.findViewById(R.id.date);
            holder.addr = (TextView) view.findViewById(R.id.addr);
            holder.img = (ImageView) view.findViewById(R.id.img);
            view.setTag(holder);
        }else {
            holder=(holder)view.getTag();
        }

        Volunteer  item = list.get(position);
        holder.addr.setText(item.getAddress());
        holder.date.setText(item.getBeginDate());
        holder.ranks_tiele.setText(item.getTitle());
//        Picasso.with(context).load(item.getNewsImgUrl()).into(holder.img);
        if (null!=item.getNewsImgUrl()){
            Log.d("reg","item.getNewsImgUrl():"+item.getNewsImgUrl());
            Glide.with(context).load(item.getNewsImgUrl()).error(R.drawable.bt_nobgd).into(holder.img);
        }
        if (item.getStatus().equals("1")){
            holder.status.setText("报名中");
        }else   if (item.getStatus().equals("2")){
            holder.status.setText("截止报名");
        }else   if (item.getStatus().equals("3")){
            holder.status.setText("活动进行中");
        }else   if (item.getStatus().equals("4")){
            holder.status.setText("已结束");
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

    public boolean addAll(Collection<? extends Volunteer> collection) {
        boolean pa = list.addAll(collection);
        return pa;
    }


    public boolean add(Volunteer object) {
        return list.add(object);
    }

}
