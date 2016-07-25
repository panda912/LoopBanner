package com.sgb.library.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.sgb.library.R;
import com.sgb.library.indicator.ClassicIndicator;
import com.sgb.library.indicator.IPagerIndicator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by panda on 16/7/19 下午3:40.
 */
public class BannerLayout extends RelativeLayout {

    private static final long DEFAULT_INTERVAL = 3500L;
    private static final int DEFAULT_SCROLL_DURATION = 450;

    private Context mContext;

    private ViewPager mViewPager;
    private BannerPagerAdapter mAdapter;
    private IPagerIndicator mIndicator;

    private ScheduledExecutorService mLoopTimer;

    private boolean bPause;

    public BannerLayout(Context context) {
        this(context, null);
    }

    public BannerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        inflate(mContext, R.layout.layout_banner, this);
        mIndicator = (ClassicIndicator) findViewById(R.id.vpi);
        mViewPager = (ViewPager) findViewById(R.id.vp_banner);
        mViewPager.setAdapter(mAdapter = new BannerPagerAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mAdapter.getCount() - 1) {
                    mIndicator.setCurIndex(0);
                } else if (position == 0) {
                    mIndicator.setCurIndex(mAdapter.getCount() - 2 - 1);
                } else {
                    mIndicator.setCurIndex(position - 1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        break;
                    case ViewPager.SCROLL_STATE_SETTLING:
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (mAdapter.getCount() == 1) {
                            return;
                        }
                        if (mViewPager.getCurrentItem() == 0) {
                            mViewPager.setCurrentItem(mAdapter.getCount() - 2, false);
                        } else if (mViewPager.getCurrentItem() == mAdapter.getCount() - 1) {
                            mViewPager.setCurrentItem(1, false);
                        }
                        break;

                    default:
                        break;
                }
            }
        });

        setViewPagerScroller();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                bPause = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                bPause = false;
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * @param list banner item view list
     */
    public void addItemList(@NonNull ArrayList<BaseBannerItem> list) {
        if (list.isEmpty()) {
            return;
        }

        mAdapter.addItemList(list);

        if (mAdapter.getCount() == 1) {
            mViewPager.setCurrentItem(0, false);
        } else {
            mViewPager.setCurrentItem(1, false);
            mIndicator.setTotalNum(mAdapter.getCount() - 2);
        }
    }

    public void hideIndicator(boolean hide) {
        ((View) mIndicator).setVisibility(hide ? View.INVISIBLE : View.VISIBLE);
    }

    public void startLoop() {
        startLoop(DEFAULT_INTERVAL);
    }

    public void startLoop(long interval) {

        stopLoop();

        //if only one item, forbit loopping
        if (mAdapter.getCount() == 1) {
            return;
        }

        mLoopTimer = Executors.newSingleThreadScheduledExecutor();
        mLoopTimer.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!bPause) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }, interval, interval, TimeUnit.MILLISECONDS);
    }

    public void stopLoop() {
        if (mLoopTimer != null && !mLoopTimer.isShutdown()) {
            mLoopTimer.shutdown();
            mLoopTimer = null;
        }
    }

    private void setViewPagerScroller() {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
            FixedSpeedScroller scroller = new FixedSpeedScroller(getContext(), interpolator, DEFAULT_SCROLL_DURATION);
            field.set(mViewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
