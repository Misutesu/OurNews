package com.team60.ournews.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.R;
import com.team60.ournews.module.model.New;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misutesu on 2016/12/27 0027.
 */

public class AdvertisementView extends RelativeLayout {

    private final int TIME = 5000;

    private Context context;

    private ViewPager mViewPager;

    private PagerAdapter mAdapter;

    private ImageView[] mCircles = new ImageView[4];
    private List<SimpleDraweeView> mImageViews;
    private List<New> news;

    private Handler handler;
    private Runnable runnable;
    private boolean isTouch = false;
    private boolean isRefresh = false;

    private OnActionListener onActionListener;

    public interface OnActionListener {
        void onNewClick(New n, View view);
    }

    public AdvertisementView(Context context) {
        super(context);
        init(context);
    }

    public AdvertisementView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AdvertisementView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    public void startRefresh() {
        isRefresh = true;
    }

    public void endRefresh() {
        isRefresh = false;
    }

    private void init(final Context context) {
        this.context = context;

        View view = LayoutInflater.from(context).inflate(R.layout.layout_advertisement_view, this, true);

        mViewPager = (ViewPager) view.findViewById(R.id.advertisement_view_view_pager);

        mCircles[0] = (ImageView) view.findViewById(R.id.advertisement_view_img_1);
        mCircles[1] = (ImageView) view.findViewById(R.id.advertisement_view_img_2);
        mCircles[2] = (ImageView) view.findViewById(R.id.advertisement_view_img_3);
        mCircles[3] = (ImageView) view.findViewById(R.id.advertisement_view_img_4);

        mImageViews = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            SimpleDraweeView simpleDraweeView = new SimpleDraweeView(context);
            simpleDraweeView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mImageViews.add(simpleDraweeView);
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position %= 4;
                if (position < 0) {
                    position = position + 4;
                }

                for (int i = 0; i < 4; i++) {
                    if (i == position) {
                        mCircles[i].setColorFilter(ThemeUtil.getColor(context.getTheme(), R.attr.colorPrimary));
                    } else {
                        mCircles[i].setColorFilter(Color.WHITE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        isTouch = true;
                        if (handler != null)
                            handler.removeCallbacks(runnable);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (isTouch) {
                            isTouch = false;
                            if (handler != null)
                                handler.postDelayed(runnable, TIME);
                        }
                        break;
                }
            }
        });
    }

    public void setData(final List<New> news) {
        if (this.news == null) {
            this.news = new ArrayList<>();
            this.news.clear();
            this.news.addAll(news);

            if (mAdapter == null) {
                mAdapter = new PagerAdapter() {
                    @Override
                    public int getCount() {
                        return Integer.MAX_VALUE;
                    }

                    @Override
                    public boolean isViewFromObject(View view, Object object) {
                        return view == object;
                    }

                    @Override
                    public void destroyItem(ViewGroup container, int position, Object object) {
                    }

                    @Override
                    public Object instantiateItem(ViewGroup container, int position) {
                        position %= 4;
                        if (position < 0) {
                            position = position + 4;
                        }

                        final SimpleDraweeView mImg = mImageViews.get(position);
                        final New n = AdvertisementView.this.news.get(position);

                        ViewParent mViewParent = mImg.getParent();
                        if (mViewParent != null) {
                            ViewGroup mViewGroup = (ViewGroup) mViewParent;
                            mViewGroup.removeView(mImg);
                        }

                        FrescoLoader.load(MyUtil.getPhotoUrl(n.getCover()))
                                .into(mImg);

                        mImg.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!isRefresh) {
                                    if (onActionListener != null) {
                                        onActionListener.onNewClick(n, mImg);
                                    }
                                }
                            }
                        });
                        container.addView(mImg);
                        return mImageViews.get(position);
                    }
                };
                mViewPager.setAdapter(mAdapter);
            }

            mAdapter.notifyDataSetChanged();

            mViewPager.setCurrentItem((Integer.MAX_VALUE / 2) + 1, false);

            initHandler();

            handler.postDelayed(runnable, TIME);
        }
    }

    public void startScroll() {
        if (handler == null) {
            initHandler();
        } else {
            handler.postDelayed(runnable, TIME);
        }
    }

    public void stopScroll() {
        if (handler == null) {
            initHandler();
        } else {
            handler.removeCallbacks(runnable);
        }
    }

    private void initHandler() {
        if (handler == null) {
            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!isTouch) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    }
                    handler.postDelayed(this, TIME);
                }
            };
        }
    }
}
