package com.pactera.banner.SivinBanner;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiwen on 2016/4/13.
 */
public class SLooperAdapter extends PagerAdapter {
    private PagerAdapter mAdapter;

    public SLooperAdapter(PagerAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getCount() {
        //如果层ViewPager中有两个或两个以上的Item的时候，则映射出边界Item，否则显示与内层个数一致
        return mAdapter.getCount() < 1 ? mAdapter.getCount() : mAdapter.getCount() + 2;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return mAdapter.isViewFromObject(view, object);
    }


    @Override
    public void startUpdate(ViewGroup container) {
        mAdapter.startUpdate(container);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return mAdapter.instantiateItem(container, getInnerAdapterPosition(position));
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        mAdapter.destroyItem(container, getInnerAdapterPosition(position), object);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mAdapter.setPrimaryItem(container, position, object);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        mAdapter.finishUpdate(container);
    }


    /**
     * 获取内层ViewPager认识的Position
     *
     * @param position 外层ViewPager识别的position
     * @return 内层viewpagerViewPager识别的Position
     */
    public int getInnerAdapterPosition(int position) {
        //viewPager真正的可用的个数
        int realCount = getInnerCount();
        //内层没有可用的Item则换回为零
        if (realCount == 0)
            return 0;
        int realPosition = (position - 1) % realCount;
        if (realPosition < 0)
            realPosition += realCount;
        return realPosition;
    }

    /**
     * @return 内层ViewPager中可用的item个数
     */
    public int getInnerCount() {
        return mAdapter.getCount();
    }

    public int toInnerPosition(int item) {
        if (getInnerCount() > 1) {
            return item + 1;
        } else return item;
    }


}
