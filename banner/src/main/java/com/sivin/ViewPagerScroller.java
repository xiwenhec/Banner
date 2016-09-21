package com.sivin;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Author      :Sivin(析问)
 * Email       :xiwenhec@163.com
 * Date        :2016/9/21
 * Time        :11:30
 * Version     :1.0.2
 * Description :重写viewpager滚动时间
 */

public class ViewPagerScroller extends Scroller {

    private int mPagerChangeDuration = 800;

    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mPagerChangeDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mPagerChangeDuration);
    }

    public void changScrollDuration (ViewPager viewPager,int duration) {
        mPagerChangeDuration = duration;

        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
