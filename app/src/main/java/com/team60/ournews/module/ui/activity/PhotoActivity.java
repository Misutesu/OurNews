package com.team60.ournews.module.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
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

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.team60.ournews.R;
import com.team60.ournews.listener.DownListener;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.view.base.BaseView;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.UiUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoActivity extends BaseActivity implements BaseView {

    public static final String TITLE_VALUE = "title";
    public static final String PHOTO_NAME_VALUE = "photo_name";

    @BindView(R.id.activity_photo_top_view)
    View mTopView;
    @BindView(R.id.activity_photo_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.activity_photo_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_photo_photo_view)
    PhotoView mPhotoView;
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
        init();
        setListener();
        loadPhoto();
    }

    @Override
    public void init() {
        title = getIntent().getStringExtra(TITLE_VALUE);
        photoUrl = MyUtil.getPhotoUrl(getIntent().getStringExtra(PHOTO_NAME_VALUE));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            mTopView.setLayoutParams(new AppBarLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtil.getStatusBarHeight()));

        if (title != null) mToolBar.setTitle(title);
        else mToolBar.setTitle("");
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPhotoView.enable();
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
        Glide.with(this).load(photoUrl).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.SOURCE).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                mRetryBtn.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                mProgressBar.setVisibility(View.GONE);
                isLoadSuccess = true;
                return false;
            }
        }).into(mPhotoView);
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
                    ActivityCompat.requestPermissions(PhotoActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 100);
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
            case 100:
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
        mPhotoView.buildDrawingCache();
        MyUtil.savePhoto(mPhotoView.getDrawingCache(), new DownListener() {
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
