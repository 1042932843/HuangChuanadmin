package com.admin.huangchuan.View;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.admin.huangchuan.util.LogUtil;

import java.lang.reflect.Field;

/**
 * 左右滑动分页
 */
public final class FragmentViewPager extends NoPreloadViewPager implements Handler.Callback {

    private int duration = 500;
    private Handler handler;
    private boolean playing;
    private boolean slide;
    private boolean scrollflag = true;
    private int item;
    private int pagecount=4;

    public FragmentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getScrollflag()) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    public void setScrollflag(boolean flag) {
        this.scrollflag = flag;
    }

    public boolean getScrollflag() {
//        return this.scrollflag;
        return false;

    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
//        setOffscreenPageLimit(adapter.getCount()); // 保留全部页面
        super.setAdapter(adapter);
    }

    private void init() {
        handler = new Handler(this);
        try {
            // 替换默认滑动延时
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mField.set(this, new DurationScroller(getContext()));
        } catch (Exception e) {
            LogUtil.e(FragmentViewPager.class, e.getMessage(), e);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (getScrollflag()) {
            if (playing) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        handler.removeMessages(0);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.sendEmptyMessageDelayed(0, 5000);
                        break;
                }
            }
            return (playing || slide) && super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        PagerAdapter adapter = getAdapter();
        if (adapter == null) {/*  User user = App.me().getUser();
        if (user != null && user.getType() == 2) {

        }*/
            return playing = false;
        }
        int current = getCurrentItem();

        boolean flag;
        item = current;


        if (current < adapter.getCount() - 1) {
            flag = true;
            item = current + 1;
        } else {
            flag = adapter.getCount() < 3;
            item = 0;
        }
        setCurrentItem(item, flag);
        handler.sendEmptyMessageDelayed(0, 5000);
        return true;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        if (this.playing == playing) {
            return;
        }
        this.playing = playing;
        if (playing) {
            handler.sendEmptyMessageDelayed(0, 5000);
        } else {
            handler.removeMessages(0);
        }
    }


    public void next(int poi) {
        boolean flag;
        if (item == 0 || item == pagecount) {
            flag = false;
        } else {
            flag = true;
        }
        if (poi>pagecount){
            poi=0;
        }else if (poi<=0){
            poi=pagecount;
        }

        item = poi;
        setCurrentItem(poi, flag);
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isSlide() {
        return slide;
    }

    public void setSlide(boolean slide) {
        this.slide = slide;
    }

    public class DurationScroller extends Scroller {

        public DurationScroller(Context context) {
            super(context);
        }

        public DurationScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            startScroll(startX, startY, dx, dy);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, duration);
        }

    }

}
