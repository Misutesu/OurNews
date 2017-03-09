package com.team60.ournews.module.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.mistesu.frescoloader.FrescoLoader;
import com.mistesu.frescoloader.OnDownloadListener;
import com.team60.ournews.R;
import com.team60.ournews.listener.DownListener;
import com.team60.ournews.module.evaluator.BesselEvaluator;
import com.team60.ournews.module.evaluator.SizeEvaluator;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.view.base.BaseView;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.SkipUtil;
import com.team60.ournews.util.UiUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.photodraweeview.PhotoDraweeView;

public class PhotoActivity extends BaseActivity implements BaseView {

    private final int PERMISSION_CODE = 100;

    public static final String TITLE_VALUE = "title";
    public static final String PHOTO_NAME_VALUE = "photo_name";

    @BindView(R.id.activity_photo_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.activity_photo_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_photo_photo_view)
    PhotoDraweeView mPhotoView;
    @BindView(R.id.activity_photo_anim_img)
    SimpleDraweeView mAnimImg;
    @BindView(R.id.activity_photo_anim_layout)
    FrameLayout mAnimLayout;
    @BindView(R.id.activity_photo_background)
    View mBackground;

    private String title;
    private String photoUrl;

    private boolean isLoadSuccess = false;
    private boolean isAnimShow = false;

    private Bundle mStartValues;
    private int imgWidth;
    private int imgHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.all_transparent);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        hideNavigationBar();

        init(savedInstanceState);
        setListener();

        loadPhoto();
    }

    @Override
    public void onBackPressed() {
        if (!isAnimShow) {
            isAnimShow = true;
            showEndAnim();
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mStartValues = intent.getBundleExtra(SkipUtil.VIEW_INFO);
        title = intent.getStringExtra(TITLE_VALUE);
        photoUrl = MyUtil.getPhotoUrl(intent.getStringExtra(PHOTO_NAME_VALUE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mToolBar.getLayoutParams();
            layoutParams.topMargin = UiUtil.getStatusBarHeight();
            mToolBar.setLayoutParams(layoutParams);
        }

        if (title != null) mToolBar.setTitle(title);
        else mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setListener() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void loadPhoto() {
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();
        controller.setUri(photoUrl);
        controller.setOldController(mPhotoView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || mPhotoView == null) {
                    return;
                }
                mPhotoView.update(imageInfo.getWidth(), imageInfo.getHeight());
                isLoadSuccess = true;
                imgWidth = imageInfo.getWidth();
                imgHeight = imageInfo.getHeight();

                showStartAnim();
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                Toast.makeText(PhotoActivity.this, getString(R.string.load_photo_error), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        mPhotoView.setController(controller.build());
    }

    private void showStartAnim() {
        if (mStartValues != null && imgWidth > 0 && imgHeight > 0) {
            FrescoLoader.load(photoUrl)
                    .setOnDownloadListener(new OnDownloadListener() {
                        @Override
                        public void onDownloadEnd(boolean b) {
                            int mLayoutHeight = UiUtil.getScreenWidth() * imgHeight / imgWidth;

                            float deltaX = mStartValues.getInt(SkipUtil.VIEW_X);
                            float deltaY = mStartValues.getInt(SkipUtil.VIEW_Y);

                            PointF pointFEnd;

                            Integer[] startSize
                                    = {mStartValues.getInt(SkipUtil.VIEW_WIDTH), mStartValues.getInt(SkipUtil.VIEW_HEIGHT)};

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                pointFEnd = new PointF(0, UiUtil.getStatusBarHeight() + ((UiUtil.getScreenHeight() - mLayoutHeight) / 2));
                            } else {
                                pointFEnd = new PointF(0, (UiUtil.getScreenHeight() - mLayoutHeight) / 2);
                            }

                            Integer[] endSize = {UiUtil.getScreenWidth(), mLayoutHeight};

                            ValueAnimator translationAnimator = ValueAnimator.ofObject(
                                    new BesselEvaluator(new PointF(deltaX / 4 * 3, deltaY), new PointF(0, (UiUtil.getScreenHeight() - mLayoutHeight) / 2 +
                                            (deltaY / 4))),
                                    new PointF(deltaX, deltaY), pointFEnd);

                            translationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    PointF pointF = (PointF) animation.getAnimatedValue();
                                    mAnimLayout.setTranslationX(pointF.x);
                                    mAnimLayout.setTranslationY(pointF.y);
                                }
                            });

                            ValueAnimator widthAnimator
                                    = ValueAnimator.ofObject(new SizeEvaluator(), startSize, endSize);

                            widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    Integer[] size = (Integer[]) animation.getAnimatedValue();
                                    CoordinatorLayout.LayoutParams layoutParams =
                                            (CoordinatorLayout.LayoutParams) mAnimLayout.getLayoutParams();
                                    layoutParams.width = size[0];
                                    layoutParams.height = size[1];
                                    mAnimLayout.setLayoutParams(layoutParams);
                                }
                            });

                            AnimatorSet imgSet = new AnimatorSet();
                            imgSet.playTogether(widthAnimator, translationAnimator);
                            imgSet.setDuration(400);
                            imgSet.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    mPhotoView.setAlpha(1f);
                                    mAnimLayout.setVisibility(View.GONE);

                                    int finalRadius = (int) Math.hypot(UiUtil.getScreenWidth(), UiUtil.getScreenHeight()) * 2;
                                    CoordinatorLayout.LayoutParams layoutParams
                                            = (CoordinatorLayout.LayoutParams) mBackground.getLayoutParams();
                                    layoutParams.width = finalRadius;
                                    layoutParams.height = finalRadius;
                                    mBackground.setLayoutParams(layoutParams);
                                    ObjectAnimator.ofFloat(mBackground, "scaleX", 0f, 1f).setDuration(400).start();
                                    ObjectAnimator.ofFloat(mBackground, "scaleY", 0f, 1f).setDuration(400).start();
                                    ObjectAnimator.ofFloat(mToolBar, "alpha", 0f, 1f).setDuration(250).start();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            imgSet.start();
                        }
                    })
                    .into(mAnimImg);
        } else {
            mAnimLayout.setVisibility(View.GONE);

            int finalRadius = (int) Math.hypot(UiUtil.getScreenWidth(), UiUtil.getScreenHeight()) * 2;
            CoordinatorLayout.LayoutParams layoutParams
                    = (CoordinatorLayout.LayoutParams) mBackground.getLayoutParams();
            layoutParams.width = finalRadius;
            layoutParams.height = finalRadius;
            mBackground.setLayoutParams(layoutParams);
            ObjectAnimator.ofFloat(mBackground, "scaleX", 0f, 1f).setDuration(400).start();
            ObjectAnimator.ofFloat(mBackground, "scaleY", 0f, 1f).setDuration(400).start();
            AnimatorSet set = new AnimatorSet();
            set.playTogether(ObjectAnimator.ofFloat(mBackground, "scaleX", 0f, 1f).setDuration(400)
                    , ObjectAnimator.ofFloat(mBackground, "scaleX", 0f, 1f).setDuration(400));
            set.start();
            AnimatorSet set1 = new AnimatorSet();
            set1.play(ObjectAnimator.ofFloat(mPhotoView, "alpha", 0f, 1f).setDuration(250))
                    .with(ObjectAnimator.ofFloat(mToolBar, "alpha", 0f, 1f).setDuration(250))
                    .after(set);
            set1.start();
        }
    }

    private void showEndAnim() {
        if (mStartValues != null && imgWidth > 0 && imgHeight > 0) {
            int mLayoutHeight = UiUtil.getScreenWidth() * imgHeight / imgWidth;

            float deltaX = mStartValues.getInt(SkipUtil.VIEW_X);
            float deltaY = mStartValues.getInt(SkipUtil.VIEW_Y);

            PointF pointFEnd;

            Integer[] startSize
                    = {mStartValues.getInt(SkipUtil.VIEW_WIDTH), mStartValues.getInt(SkipUtil.VIEW_HEIGHT)};

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                pointFEnd = new PointF(0, ((UiUtil.getScreenHeight() - mLayoutHeight) / 2) + UiUtil.getStatusBarHeight());
            } else {
                pointFEnd = new PointF(0, (UiUtil.getScreenHeight() - mLayoutHeight) / 2);
            }

            Integer[] endSize = {UiUtil.getScreenWidth(), mLayoutHeight};

            ValueAnimator translationAnimator = ValueAnimator.ofObject(
                    new BesselEvaluator(new PointF(0, (UiUtil.getScreenHeight() - mLayoutHeight) / 2 +
                            (deltaY / 4)), new PointF(deltaX / 4 * 3, deltaY)),
                    pointFEnd, new PointF(deltaX, deltaY));

            translationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    PointF pointF = (PointF) animation.getAnimatedValue();
                    mAnimLayout.setTranslationX(pointF.x);
                    mAnimLayout.setTranslationY(pointF.y);
                }
            });

            ValueAnimator widthAnimator
                    = ValueAnimator.ofObject(new SizeEvaluator(), endSize, startSize);

            widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Integer[] size = (Integer[]) animation.getAnimatedValue();
                    CoordinatorLayout.LayoutParams layoutParams =
                            (CoordinatorLayout.LayoutParams) mAnimLayout.getLayoutParams();
                    layoutParams.width = size[0];
                    layoutParams.height = size[1];
                    mAnimLayout.setLayoutParams(layoutParams);
                }
            });

            AnimatorSet imgSet = new AnimatorSet();
            imgSet.playTogether(widthAnimator, translationAnimator,
                    ObjectAnimator.ofFloat(mBackground, "scaleX", 1f, 0f),
                    ObjectAnimator.ofFloat(mBackground, "scaleY", 1f, 0f),
                    ObjectAnimator.ofFloat(mToolBar, "alpha", 1f, 0f));
            imgSet.setDuration(350);

            AnimatorSet set1 = new AnimatorSet();
            set1.play(ObjectAnimator.ofFloat(mAnimLayout, "alpha", 1f, 0f).setDuration(150))
                    .after(imgSet);

            set1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mPhotoView.setAlpha(0f);
                    mAnimLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    finish();
                    overridePendingTransition(0, 0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set1.start();
        } else {
            AnimatorSet set = new AnimatorSet();
            set.playTogether(ObjectAnimator.ofFloat(mBackground, "scaleX", 1f, 0f).setDuration(400),
                    ObjectAnimator.ofFloat(mBackground, "scaleY", 1f, 0f).setDuration(400),
                    ObjectAnimator.ofFloat(mToolBar, "alpha", 1f, 0f).setDuration(250),
                    ObjectAnimator.ofFloat(mPhotoView, "alpha", 1f, 0f).setDuration(250));
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    finish();
                    overridePendingTransition(0, 0);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            set.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photo_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.tool_bar_save_img).setVisible(isLoadSuccess);
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.tool_bar_save_img) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(PhotoActivity.this
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PhotoActivity.this
                            , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
                } else {
                    savePhoto();
                }
            } else {
                savePhoto();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    savePhoto();
                } else {
                    showSnackBar(getString(R.string.no_write_file_permission));
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void savePhoto() {
        MyUtil.savePhoto(photoUrl, new DownListener() {
            @Override
            public void success() {
                showSnackBar(getString(R.string.save_img_success));
            }

            @Override
            public void error() {
                showSnackBar(getString(R.string.save_img_error));
            }
        });
    }
}
