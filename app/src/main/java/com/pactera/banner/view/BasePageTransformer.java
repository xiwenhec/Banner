package com.pactera.banner.view;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 *
 */
public abstract class BasePageTransformer implements ViewPager.PageTransformer {

    public void transformPage(View view, float position) {
        if (position < -1.0f) {
            handleInvisiblePage(view, position);
        } else if (position <= 0.0f) {
            handleLeftPage(view, position);
        } else if (position <= 1.0f) {
            handleRightPage(view, position);
        } else {
            handleInvisiblePage(view, position);
        }
    }

    /**
     * 处理消失的页面的动画
     * @param view
     * @param position
     */
    public abstract void handleInvisiblePage(View view, float position);

    /**
     * 处理左边的动画效果
     * @param view
     * @param position
     */
    public abstract void handleLeftPage(View view, float position);

    /**
     * 处理右边的动画效果
     * @param view
     * @param position
     */
    public abstract void handleRightPage(View view, float position);

}