package com.pactera.banner.view;

import android.content.Context;
import android.widget.Scroller;


public class PageChangeDurationScroller extends Scroller {
    private int mDuration = 1000;
    public PageChangeDurationScroller(Context context) {
        super(context);
    }

    public PageChangeDurationScroller(Context context, int duration) {
        super(context);
        mDuration = duration;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}