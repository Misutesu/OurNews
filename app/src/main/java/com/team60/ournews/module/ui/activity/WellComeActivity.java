package com.team60.ournews.module.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.team60.ournews.R;
import com.team60.ournews.listener.MyObjectAnimatorListener;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.ui.fragment.WellComeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WellComeActivity extends BaseActivity {

    @BindView(R.id.activity_well_come_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.activity_well_come_view_layout)
    LinearLayout mViewLayout;
    @BindView(R.id.activity_well_come_skip_btn)
    AppCompatButton mSkipBtn;
    @BindView(R.id.activity_well_come_next_btn)
    ImageButton mNextBtn;
    @BindView(R.id.activity_well_come_done_btn)
    AppCompatButton mDoneBtn;
    @BindView(R.id.activity_well_come_layout)
    RelativeLayout mLayout;
    @BindView(R.id.activity_well_come_bottom_layout)
    LinearLayout mBottomLayout;
    @BindView(R.id.activity_well_come_anim_circle)
    ImageView mAnimCircle;
    @BindView(R.id.activity_well_come_circle_one)
    ImageView mCircleOne;
    @BindView(R.id.activity_well_come_circle_two)
    ImageView mCircleTwo;
    @BindView(R.id.activity_well_come_circle_three)
    ImageView mCircleThree;
    @BindView(R.id.activity_well_come_circle_four)
    ImageView mCircleFour;

    private Fragment[] mFragments;
    private int[] colors;
    private ImageView[] circles;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);
        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();
    }

    @Override
    public void onBackPressed() {
        if (position != 0) mViewPager.setCurrentItem(position - 1);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        hideNavigationBar();

        mFragments = new Fragment[4];
        colors = new int[4];
        circles = new ImageView[4];
        mFragments[0] = WellComeFragment.instance(R.drawable.well_come_img_one, "测试引导页1");
        mFragments[1] = WellComeFragment.instance(R.drawable.well_come_img_two, "测试引导页2");
        mFragments[2] = WellComeFragment.instance(R.drawable.well_come_img_three, "测试引导页3");
        mFragments[3] = WellComeFragment.instance(R.drawable.well_come_img_four, "测试引导页4");
        colors[0] = ContextCompat.getColor(this, R.color.colorPrimaryBlue);
        colors[1] = ContextCompat.getColor(this, R.color.colorPrimaryGreen);
        colors[2] = ContextCompat.getColor(this, R.color.colorPrimaryRed);
        colors[3] = ContextCompat.getColor(this, R.color.colorPrimaryGreenBlack);
        circles[0] = mCircleOne;
        circles[1] = mCircleTwo;
        circles[2] = mCircleThree;
        circles[3] = mCircleFour;

        mCircleOne.post(new Runnable() {
            @Override
            public void run() {
                int[] location = new int[2];
                mCircleOne.getLocationOnScreen(location);
                mAnimCircle.setTranslationX(location[0]);
                mAnimCircle.setY(mCircleOne.getY());
            }
        });

        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }

            @Override
            public int getCount() {
                return mFragments.length;
            }
        });
    }

    @Override
    public void setListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int color = (int) new ArgbEvaluator().evaluate(positionOffset, colors[position]
                        , colors[position == 3 ? position : position + 1]);
                mLayout.setBackgroundColor(color);

                int[] location = new int[2];
                circles[position].getLocationOnScreen(location);
                float startX = location[0];
                circles[position == 3 ? position : position + 1].getLocationOnScreen(location);
                float endX = location[0];
                if (startX != endX)
                    mAnimCircle.setTranslationX(((endX - startX) * positionOffset) + startX);
            }

            @Override
            public void onPageSelected(int position) {
                WellComeActivity.this.position = position;
                AnimatorSet set = new AnimatorSet();
                if (position == mFragments.length - 1) {
                    set.playTogether(ObjectAnimator.ofFloat(mSkipBtn, "alpha", mSkipBtn.getAlpha(), 0f)
                            , ObjectAnimator.ofFloat(mNextBtn, "alpha", mNextBtn.getAlpha(), 0f)
                            , ObjectAnimator.ofFloat(mDoneBtn, "alpha", mDoneBtn.getAlpha(), 1f));
                    set.addListener(new MyObjectAnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mDoneBtn.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mNextBtn.setVisibility(View.GONE);
                            mSkipBtn.setVisibility(View.GONE);
                        }
                    });
                } else {
                    set.playTogether(ObjectAnimator.ofFloat(mSkipBtn, "alpha", mSkipBtn.getAlpha(), 1f)
                            , ObjectAnimator.ofFloat(mNextBtn, "alpha", mNextBtn.getAlpha(), 1f)
                            , ObjectAnimator.ofFloat(mDoneBtn, "alpha", mDoneBtn.getAlpha(), 0f));
                    set.addListener(new MyObjectAnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            mNextBtn.setVisibility(View.VISIBLE);
                            mSkipBtn.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mDoneBtn.setVisibility(View.GONE);
                        }
                    });
                }
                set.setDuration(250);
                set.start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void showSnackBar(String message) {

    }

    @OnClick({R.id.activity_well_come_skip_btn, R.id.activity_well_come_next_btn, R.id.activity_well_come_done_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_well_come_skip_btn:
                mViewPager.setCurrentItem(mFragments.length - 1);
                break;
            case R.id.activity_well_come_next_btn:
                if (position != mFragments.length - 1) mViewPager.setCurrentItem(position + 1);
                break;
            case R.id.activity_well_come_done_btn:
                finish();
                break;
        }
    }
}
