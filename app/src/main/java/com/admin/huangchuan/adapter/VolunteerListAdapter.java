package com.admin.huangchuan.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.admin.huangchuan.R;
import com.admin.huangchuan.model.VolunteerDetailsList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lenovo on 2017/2/27.
 */
public class VolunteerListAdapter extends BaseAdapter {
    private final Context context;
    private List<VolunteerDetailsList> list;
    private HashSet<Integer> hs;
//    private List<RedFlag> olist;

    public VolunteerListAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<VolunteerDetailsList>();
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
    public VolunteerDetailsList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class holder {
        TextView name;
        TextView phone;
        TextView createDate;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        holder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.itme_list_volunteerlist, null);
            holder = new holder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.phone = (TextView) view.findViewById(R.id.phone);
            holder.createDate = (TextView) view.findViewById(R.id.createDate);
            view.setTag(holder);
        } else {
            holder = (holder) view.getTag();
        }

        VolunteerDetailsList item = list.get(position);
        holder.createDate.setText("报名时间:" + item.getCreateDate());
        holder.phone.setText("电话:" + item.getPhone());
        holder.name.setText("名字:" + item.getName());
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

    public boolean addAll(Collection<? extends VolunteerDetailsList> collection) {
        boolean pa = list.addAll(collection);
        return pa;
    }


    public boolean add(VolunteerDetailsList object) {
        return list.add(object);
    }

}
