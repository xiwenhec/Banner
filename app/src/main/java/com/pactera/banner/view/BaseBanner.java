package com.pactera.banner.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pactera.banner.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 图片轮播的基类
 * @param <T>
 */
public abstract class BaseBanner<T> extends RelativeLayout {
    private static final String TAG = BaseBanner.class.getSimpleName();

    /**
     * 相对布局的布局参数
     */
    private static final int RMP = LayoutParams.MATCH_PARENT;
    private static final int RWC = LayoutParams.WRAP_CONTENT;

    /**
     * 线性布局的布局参数
     */
    private static final int LWC = LinearLayout.LayoutParams.WRAP_CONTENT;

    /**
     * 循环轮播的Viewpager
     */
    private BaseLoopViewPager mViewPager;

    /**
     * viewPager适配器
     */
   private PageAdapter mPageAdapter;

    /**
     * 存放轮播信息的集合
     */
    protected List<T> mData = new ArrayList<>();


    private LinearLayout mPointRealContainerLl;
    private TextView mTipTv;

    /**
     * 是否自动播放
     */
    private boolean mAutoPlayAble = true;

    /**
     * 是否正在播放
     */
    private boolean mIsAutoPlaying = false;

    /**
     * 自动播放的间隔
     */
    private int mAutoPlayInterval = 3;

    /**
     * 页面切换的时间
     */
    private int mPageChangeDuration = 800;

    /**
     * 点的layout的属性
     */
    private int mPointGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private int mPointLeftRightMargin;
    private int mPointTopBottomMargin;
    private int mPointContainerLeftRightPadding;

    /**
     * 提示文字的大小
     */
    private int mTipTextSize;

    /**
     * 提示文字的颜色
     */
    private int mTipTextColor = Color.WHITE;

    /**
     * 点的资源id
     */
    private int mPointDrawableResId = R.drawable.selector_basebanner_point;

    /**
     * 容器的背景图片
     */
    private Drawable mPointContainerBackgroundDrawable;

    /**
     * 当前的页面的位置
     */
    protected int currentPositon;

    /**
     * 任务执行器
     */
    protected ScheduledExecutorService mExecutor;
    /**
     * 自动播放下一个
     */
    private Handler mAutoPlayHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            scrollToNextItem(currentPositon);
        }
    };

    public BaseBanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //初始化默认属性
        initDefaultAttrs(context);
        //初始化自定义属性
        initCustomAttrs(context, attrs);
        //初始化view
        initView(context);
        //设置页面的切换效果
        setTransitionEffect();
    }

    private void initDefaultAttrs(Context context) {
        mPointLeftRightMargin = dp2px(context, 3);
        mPointTopBottomMargin = dp2px(context, 6);
        mPointContainerLeftRightPadding = dp2px(context, 10);
        mTipTextSize = sp2px(context, 8);
        //容器的背景图片
        mPointContainerBackgroundDrawable = new ColorDrawable(Color.parseColor("#44aaaaaa"));
    }

    /**
     * 初始化自定义属性
     * @param context context
     * @param attrs   attrs
     */
    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseBanner);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BaseBanner_banner_pointDrawable) {
            //指示器点的样式资源id
            mPointDrawableResId = typedArray.getResourceId(attr, R.drawable.selector_basebanner_point);
        } else if (attr == R.styleable.BaseBanner_banner_pointContainerBackground) {
            //指示器容器背景样式
            mPointContainerBackgroundDrawable = typedArray.getDrawable(attr);

        } else if (attr == R.styleable.BaseBanner_banner_pointLeftRightMargin) {
            //指示器左右边距
            mPointLeftRightMargin = typedArray.getDimensionPixelSize(attr, mPointLeftRightMargin);
        } else if (attr == R.styleable.BaseBanner_banner_pointContainerLeftRightPadding) {
            //指示器容器的左右padding
            mPointContainerLeftRightPadding = typedArray.getDimensionPixelSize(attr, mPointContainerLeftRightPadding);
        } else if (attr == R.styleable.BaseBanner_banner_pointTopBottomMargin) {

            //指示器的上下margin
            mPointTopBottomMargin = typedArray.getDimensionPixelSize(attr, mPointTopBottomMargin);
        } else if (attr == R.styleable.BaseBanner_banner_pointGravity) {
            //指示器在容器中的位置属性
            mPointGravity = typedArray.getInt(attr, mPointGravity);
        } else if (attr == R.styleable.BaseBanner_banner_pointAutoPlayAble) {

            //是否可以自动播放
            mAutoPlayAble = typedArray.getBoolean(attr, mAutoPlayAble);
        } else if (attr == R.styleable.BaseBanner_banner_pointAutoPlayInterval) {

            //轮播的间隔
            mAutoPlayInterval = typedArray.getInteger(attr, mAutoPlayInterval);
        } else if (attr == R.styleable.BaseBanner_banner_pageChangeDuration) {

            //页面切换的持续时间
            mPageChangeDuration = typedArray.getInteger(attr, mPageChangeDuration);
        }

        else if (attr == R.styleable.BaseBanner_banner_tipTextColor) {

            //提示文字颜色
            mTipTextColor = typedArray.getColor(attr, mTipTextColor);
        } else if (attr == R.styleable.BaseBanner_banner_tipTextSize) {
            //提示文字大小
            mTipTextSize = typedArray.getDimensionPixelSize(attr, mTipTextSize);
        }



    }

    private void initView(Context context) {
        //创建viewpager，根据是否可以循环创建不听点的Viewpager
        mViewPager = new BaseLoopViewPager(context);

        addView(mViewPager, new LayoutParams(RMP, RMP));

        //设置页面切换的持续时间
        setPageChangeDuration(mPageChangeDuration);
        //创建指示器容器的相对布局
        RelativeLayout pointContainerRl = new RelativeLayout(context);
        if (Build.VERSION.SDK_INT >= 16) {
            pointContainerRl.setBackground(mPointContainerBackgroundDrawable);
        } else {
            pointContainerRl.setBackgroundDrawable(mPointContainerBackgroundDrawable);
        }
        pointContainerRl.setPadding(mPointContainerLeftRightPadding, 0, mPointContainerLeftRightPadding, 0);
        LayoutParams pointContainerLp = new LayoutParams(RMP, RWC);
        // 处理圆点在顶部还是底部
        if ((mPointGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
            pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        } else {
            pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        //相对布局中添加点的容器
        addView(pointContainerRl, pointContainerLp);
        mPointRealContainerLl = new LinearLayout(context);
        mPointRealContainerLl.setId(R.id.banner_pointContainerId);
        mPointRealContainerLl.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams pointRealContainerLp = new LayoutParams(RWC, RWC);
        pointContainerRl.addView(mPointRealContainerLl, pointRealContainerLp);

        LayoutParams tipLp = new LayoutParams(RMP, getResources().getDrawable(mPointDrawableResId).getIntrinsicHeight() + 2 * mPointTopBottomMargin);
        mTipTv = new TextView(context);
        mTipTv.setGravity(Gravity.CENTER_VERTICAL);
        mTipTv.setSingleLine(true);
        mTipTv.setEllipsize(TextUtils.TruncateAt.END);
        mTipTv.setTextColor(mTipTextColor);
        mTipTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTipTextSize);
        pointContainerRl.addView(mTipTv, tipLp);

        int horizontalGravity = mPointGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
        // 处理圆点在左边、右边还是水平居中
        if (horizontalGravity == Gravity.LEFT) {
            pointRealContainerLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            tipLp.addRule(RelativeLayout.RIGHT_OF, R.id.banner_pointContainerId);
            mTipTv.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else if (horizontalGravity == Gravity.RIGHT) {
            pointRealContainerLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            tipLp.addRule(RelativeLayout.LEFT_OF, R.id.banner_pointContainerId);
        } else {
            pointRealContainerLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            tipLp.addRule(RelativeLayout.LEFT_OF, R.id.banner_pointContainerId);
        }
    }

    /**
     * 设置页码切换过程的时间长度
     *
     * @param duration 页码切换过程的时间长度
     */
    public void setPageChangeDuration(int duration) {

    }

    /**
     * 开始滚动
     */
    public void startScroll() {
        if (mData == null) {
            return;
        }
        onTitleSlect(mTipTv, currentPositon);
        setViewPager();
        goScroll();
    }

    public void setViewPager() {
        mPageAdapter = new PageAdapter();
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOffscreenPageLimit(mData.size());
        mViewPager.addOnPageChangeListener(new ChangePointListener());
        initPoints();
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPager.addOnPageChangeListener(listener);
    }


    /**
     * 初始化点
     */
    private void initPoints() {
        mPointRealContainerLl.removeAllViews();
        mViewPager.removeAllViews();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LWC, LWC);
        lp.setMargins(mPointLeftRightMargin, mPointTopBottomMargin, mPointLeftRightMargin, mPointTopBottomMargin);
        ImageView imageView;
        for (int i = 0; i < mData.size(); i++) {
            imageView = new ImageView(getContext());
            imageView.setLayoutParams(lp);
            imageView.setImageResource(mPointDrawableResId);
            mPointRealContainerLl.addView(imageView);
        }
        if (mData.size()>0)
            mData.size();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseScroll();
                break;
            case MotionEvent.ACTION_UP:
                goScroll();
                break;
            case MotionEvent.ACTION_CANCEL:
                goScroll();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 滚动到下一个条目
     * @param position
     */
    private void scrollToNextItem(int position) {
        position++;
        mViewPager.setCurrentItem(position);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            goScroll();
        } else if (visibility == INVISIBLE) {
            pauseScroll();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pauseScroll();
    }

    private void switchToPoint(int newCurrentPoint) {
        for (int i = 0; i < mPointRealContainerLl.getChildCount(); i++) {
            mPointRealContainerLl.getChildAt(i).setEnabled(false);
        }
        mPointRealContainerLl.getChildAt(newCurrentPoint).setEnabled(true);

        if (mTipTv != null) {
            onTitleSlect(mTipTv, currentPositon);
        }
    }

    /**
     * 设置页面也换动画
     */
    public void setTransitionEffect() {
        mViewPager.setPageTransformer(true, new DefaultPageTransformer());
    }

    /**
     * 设置自定义页面切换动画
     * @param transformer
     */
    public void setPageTransformer(ViewPager.PageTransformer transformer) {
        if (transformer != null) {
            mViewPager.setPageTransformer(true, transformer);
        }
    }


    /**
     *viewPager的适配器
     */
    private final class PageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = onCreateItemView(position);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onVpItemClickListener != null) {
                        onVpItemClickListener.onItemClick(position);
                    }
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private final class ChangePointListener extends BaseLoopViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            currentPositon = position % mData.size();
            switchToPoint(currentPositon);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mTipTv != null) {
                if (positionOffset > 0.5) {
                    onTitleSlect(mTipTv, currentPositon);
                    mTipTv.setAlpha(positionOffset);
                } else {
                    mTipTv.setAlpha(1 - positionOffset);
                    onTitleSlect(mTipTv, currentPositon);
                }
            }
        }
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    private static void debug(String msg) {
        Log.i(TAG, msg);
    }

    /**
     * 重写方法，当Viewpager滚动到下一个位置的时候，设置title的内容，
     * 同时你也可以设置title的属性，例如textColor
     * 如果指示器的setIndicatorGravity设置的是center属性，则不做任何事情
     */
    public void onTitleSlect(TextView tv, int position) {
    }

    /**
     * 设置数据的集合
     * */
    public void setSource(List<T> list) {
        this.mData = list;
    }

    /**
     *
     * 方法使用状态 ：viewpager处于暂停的状态
     * 开始滚动
     * */
    public void goScroll() {
        if (!isValid()) {
            return;
        }
        if (mIsAutoPlaying) {
            return;
        }
        if (mAutoPlayAble) {
            pauseScroll();
            mExecutor = Executors.newSingleThreadScheduledExecutor();
            //command：执行线程
            //initialDelay：初始化延时
            //period：两次开始执行最小间隔时间
            //unit：计时单位
            mExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    mAutoPlayHandler.obtainMessage().sendToTarget();
                }
            }, mAutoPlayInterval, mAutoPlayInterval, TimeUnit.SECONDS);
            mIsAutoPlaying = true;
        } else {
            mIsAutoPlaying = false;
            switchToPoint(0);
        }
    }

    /**
     * 暂停滚动
     * */
    public void pauseScroll() {
        if (mExecutor != null) {
            mExecutor.shutdown();
            mExecutor = null;
        }
        Log.d(TAG, this.getClass().getSimpleName() + "--->pauseScroll()");

        mIsAutoPlaying = false;
    }

    /**
     * 判断控件是否可用
     * @return
     */
    protected boolean isValid() {
        if (mViewPager == null) {
            Log.e(TAG, "ViewPager is not exist!");
            return false;
        }

        if (mData == null || mData.size() == 0) {
            Log.e(TAG, "DataList must be not empty!");
            return false;
        }
        return true;
    }

    /**
     *  创建Viewpager item的Layout
     */
    public abstract View onCreateItemView(int position);

    private OnVpItemClickListener onVpItemClickListener;

    /**
     * 设置viewPage的Item点击监听器
     * @param listener
     */
    public void setOnItemClickListener(OnVpItemClickListener listener) {
        this.onVpItemClickListener = listener;
    }

    public interface OnVpItemClickListener {
        void onItemClick(int position);
    }

    public void notifiDataSetChange(){
        mViewPager.notifinDataSetChange();

    }
}