/*
 * Copyright © 2015 珠海云集软件科技有限公司.
 * Website：http://www.YunJi123.com
 * Mail：dev@yunji123.com
 * Tel：+86-0756-8605060
 * QQ：340022641(dove)
 * Author：dove
 */

package com.admin.huangchuan.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.admin.huangchuan.fragment.PagerFragment;

/**
 * 片段分页适配器 保存和恢复状态
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final Class<?>[] classes;
    private final Fragment[] fragments;
    private String[] titles;

    public FragmentPagerAdapter(FragmentManager fm, Class<?>... classes) {
        super(fm);
        this.classes = classes;
        this.fragments = new Fragment[classes.length];
    }

//    public FragmentPagerAdapter(FragmentManager fm, Fragment... fragments) {
//        super(fm);
//        this.classes = null;
//        this.fragments = fragments;
//    }

    public void setTitles(String... titles) {
        this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments[position];
        if (fragment == null && classes != null) {
            try {
                fragments[position] = fragment = (Fragment) classes[position].newInstance();
                if (fragment instanceof PagerFragment) {
                    ((PagerFragment) fragment).setPosition(position);
                }
            } catch (Exception e) {

            }
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles != null && titles.length > position) {
            return titles[position];
        }
        return null;
    }
}