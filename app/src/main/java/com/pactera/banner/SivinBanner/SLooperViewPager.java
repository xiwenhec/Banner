package com.pactera.banner.SivinBanner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiwen on 2016/4/13.
 */
public class SLooperViewPager extends ViewPager {

    private static final String TAG = SLooperViewPager.class.getSimpleName();

    private SLooperAdapter mAdapter;
    private List<OnPageChangeListener> mOnPageChangeListeners;

    public SLooperViewPager(Context context) {
        super(context);
        init(context);
    }


    public SLooperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = new SLooperAdapter(adapter);
        super.setAdapter(mAdapter);
        setCurrentItem(0, false);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(mAdapter.toInnerPosition(item), smoothScroll);
    }


    /**
     * 外层ViewPager中的item是通过内层位置映射关系得到的，这里我们不想让外界知道，
     * 当我们得到此方法得到值，我们希望和我们传入的数据集的位置是一致的
     *
     * @return 返回映射后的
     */
    @Override
    public int getCurrentItem() {
        return mAdapter.getInnerAdapterPosition(super.getCurrentItem());
    }

    @Override
    public void clearOnPageChangeListeners() {
        if (mOnPageChangeListeners != null) {
            mOnPageChangeListeners.clear();
        }
    }

    @Override
    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        if (mOnPageChangeListeners != null) {
            mOnPageChangeListeners.remove(listener);
        }
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (mOnPageChangeListeners == null) {
            mOnPageChangeListeners = new ArrayList<>();
        }
        mOnPageChangeListeners.add(listener);
    }


    private void init(Context context) {
        if (mOnPageChangeListener != null) {
            super.removeOnPageChangeListener(mOnPageChangeListener);
        }
        super.addOnPageChangeListener(mOnPageChangeListener);

    }


    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        private float mPreviousOffset = -1;
        private float mPreviousPosition = -1;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            int innerPosition = mAdapter.getInnerAdapterPosition(position);
            Log.d(TAG, "onPageScrolled: innerpoition  "+innerPosition+"   position:  "+position+" positionOffset "+positionOffset);

            //当滚动结束，而且到达边界的时候，强制映射到对应的位置
            if (positionOffset == 0 && mPreviousOffset == 0 && (position == 0 || position == mAdapter.getCount() - 1)) {


                setCurrentItem(innerPosition, false);
            }

            mPreviousOffset = positionOffset;

            if (mOnPageChangeListeners != null) {
                for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                    OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                    if (listener != null) {
                        if (innerPosition != mAdapter.getInnerCount() - 1) {
                            listener.onPageScrolled(innerPosition, positionOffset, positionOffsetPixels);
                        } else {
                            if (positionOffset > 0.5) {
                                listener.onPageScrolled(0, 0, 0);
                            } else {
                                listener.onPageScrolled(innerPosition, 0, 0);
                            }
                        }
                    }
                }
            }

        }

        @Override
        public void onPageSelected(int position) {

            int realPosition = mAdapter.getInnerAdapterPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                if (mOnPageChangeListeners != null) {
                    for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                        OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                        if (listener != null) {
                            listener.onPageSelected(realPosition);
                        }
                    }
                }
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

            if (mAdapter != null) {
                int position = SLooperViewPager.super.getCurrentItem();
                int realPosition = mAdapter.getInnerAdapterPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }
            if (mOnPageChangeListeners != null) {
                for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                    OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                    if (listener != null) {
                        listener.onPageScrollStateChanged(state);
                    }
                }
            }
        }
    };


}
