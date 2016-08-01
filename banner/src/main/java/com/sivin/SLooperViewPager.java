package com.sivin;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;

/**
 * 无限轮播的ViewPager
 * Created by xiwen on 2016/4/13.
 */
public class SLooperViewPager extends ViewPager {
    private SLooperAdapter mAdapter;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    public SLooperViewPager(Context context) {
        this(context, null);
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
    public void setCurrentItem(int position, boolean smoothScroll) {
        //item的被调用者传递过来的位置是没有原始的位置，即切换位置是从0到DataSize-1之间切换
        //但是对于外层ViewPager而言，他需要的位置范围应该是映射后的位置切换，即：出去两边映射的页面
        //应该是从1到映射后的倒数第二个位置

        super.setCurrentItem(mAdapter.toLooperPosition(position), smoothScroll);
    }


    /**
     * 外层ViewPager中的item是通过内层位置映射关系得到的
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
        //上一次的偏移量
        private float mPreviousOffset = -1;
        //上一次的位置
        private float mPreviousPosition = -1;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mAdapter != null) {
                int innerPosition = mAdapter.getInnerAdapterPosition(position);

                /*
                    positionOffset =0:滚动完成，
                    position =0 :开始的边界
                    position =mAdapter.getCount()-1:结束的边界
                 */
                if (positionOffset == 0 && mPreviousOffset == 0 && (position == 0 || position == mAdapter.getCount() - 1)) {
                    //强制回到映射位置
                    setCurrentItem(innerPosition, false);
                }
                mPreviousOffset = positionOffset;

                if (mOnPageChangeListeners != null) {
                    for (int i = 0; i < mOnPageChangeListeners.size(); i++) {
                        OnPageChangeListener listener = mOnPageChangeListeners.get(i);
                        if (listener != null) {
                            //如果内层的位置没有达到最后一个，内层滚动监听器正常设置
                            if (innerPosition != mAdapter.getInnerCount() - 1) {
                                listener.onPageScrolled(innerPosition, positionOffset, positionOffsetPixels);
                            } else {
                                //如果到达最后一个位置，当偏移量达到0.5以上，这告诉监听器，这个页面已经到达内层的第一个位置
                                //否则还是最后一个位置
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
