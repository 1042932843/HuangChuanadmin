package com.admin.huangchuan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.admin.huangchuan.App;
import com.admin.huangchuan.R;
import com.admin.huangchuan.View.FragmentViewPager;
import com.admin.huangchuan.View.NoPreloadViewPager;
import com.admin.huangchuan.activity.BaseFragment;
import com.admin.huangchuan.adapter.FragmentPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/27 0027.
 */
public class PartyRepresentativeFragment extends BaseFragment implements View.OnClickListener, NoPreloadViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {

    private FragmentPagerAdapter pagerAdapter; // 分页适配器
    @Bind(R.id.img_back)
    ImageView imgBack;
    @Bind(R.id.navbar)
    RadioGroup navbar;
    @Bind(R.id.viewPager)
    FragmentViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partyrepresentative);
        setAppTitle("党代表工作室");
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        View[] views = {imgBack};
        for (View view : views) {
            view.setOnClickListener(this);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        pagerAdapter = new FragmentPagerAdapter(fragmentManager,
                TheMassesFragment.class, //群众
                PartyMemberFragment.class//党员
        );
        viewPager.setDuration(1000);
        viewPager.setScrollflag(false);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(pagerAdapter);
//        viewPager.addOnPageChangeListener(this);
        navbar.setOnCheckedChangeListener(this);
        viewPager.setCurrentItem(0, false);
        RadioButton navItem = (RadioButton) navbar.getChildAt(0);
        navItem.setChecked(true);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) { // 页面切换同步选项卡
        RadioButton navItem = (RadioButton) navbar.getChildAt(position);
        navItem.setChecked(true);
        Fragment fragment = pagerAdapter.getItem(position);

        if (fragment instanceof PagerFragment) {
            ((PagerFragment) fragment).onPageSelected();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {// 点击选项卡切换页面

        View navItem = group.findViewById(checkedId);
        int index = group.indexOfChild(navItem);
        viewPager.setCurrentItem(index, false);

        // 切换页面时自动隐藏输入法
        View view = getWindow().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            App.me().input().hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
