package com.team60.ournews.module.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.team60.ournews.R;
import com.team60.ournews.listener.DownListener;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.view.base.BaseView;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.UiUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.photodraweeview.PhotoDraweeView;

public class PhotoActivity extends BaseActivity implements BaseView {

    private final int PERMISSION_CODE = 100;

    public static final String TITLE_VALUE = "title";
    public static final String PHOTO_NAME_VALUE = "photo_name";

    @BindView(R.id.activity_photo_top_view)
    View mTopView;
    @BindView(R.id.activity_photo_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.activity_photo_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_photo_photo_view)
    PhotoDraweeView mPhotoView;
    @BindView(R.id.activity_photo_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.activity_photo_retry_btn)
    Button mRetryBtn;

    private String title;
    private String photoUrl;

    private boolean isLoadSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        hideNavigationBar();
        init(savedInstanceState);
        setListener();
        loadPhoto();
    }

    @Override
    public void init(Bundle savedInstanceState) {
        title = getIntent().getStringExtra(TITLE_VALUE);
        photoUrl = MyUtil.getPhotoUrl(getIntent().getStringExtra(PHOTO_NAME_VALUE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            mTopView.setLayoutParams(new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtil.getStatusBarHeight()));

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

        mRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                loadPhoto();
                mRetryBtn.setVisibility(View.GONE);
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
                mProgressBar.setVisibility(View.GONE);
                isLoadSuccess = true;
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                mRetryBtn.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        });
        mPhotoView.setController(controller.build());
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
