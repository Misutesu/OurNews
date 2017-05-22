package com.team60.ournews.module.ui.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.R;
import com.team60.ournews.module.presenter.EditUserPresenter;
import com.team60.ournews.module.presenter.impl.EditUserPresenterImpl;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.module.view.EditUserView;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.ThemeUtil;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.fresco.processors.BlurPostprocessor;

public class EditUserActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener, EditUserView {

    public final static int CODE_NICK_NAME = 106;

    private final String[] selectPhotoItems = {"拍照", "从相册中选择"};
    private final String[] selectSexItems = {"男", "女"};

    @BindView(R.id.activity_edit_user_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.activity_edit_user_avatar_background_img)
    SimpleDraweeView mAvatarBackgroundImg;
    @BindView(R.id.activity_edit_user_avatar_img)
    SimpleDraweeView mAvatarImg;
    @BindView(R.id.activity_edit_user_avatar_car_view)
    CardView mAvatarCarView;
    @BindView(R.id.activity_edit_user_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.activity_edit_user_login_name_text)
    AppCompatTextView mLoginNameText;
    @BindView(R.id.activity_edit_user_name_car_view)
    CardView mLoginNameCarView;
    @BindView(R.id.activity_edit_user_nick_name_text)
    AppCompatTextView mNickNameText;
    @BindView(R.id.activity_edit_nick_name_car_view)
    CardView mNickNameCarView;
    @BindView(R.id.activity_edit_sex_name_text)
    AppCompatTextView mSexNameText;
    @BindView(R.id.activity_edit_sex_car_view)
    CardView mSexCarView;
    @BindView(R.id.activity_edit_birthday_text)
    AppCompatTextView mBirthdayText;
    @BindView(R.id.activity_edit_birthday_view)
    CardView mBirthdayCardView;
    @BindView(R.id.activity_edit_save_btn)
    AppCompatButton mSaveBtn;

    private EditUserPresenter mPresenter;

    private AlertDialog mPhotoDialog;
    private AlertDialog mSexDialog;
    private DatePickerDialog mDateDialog;
    private ProgressDialog mProgressDialog;

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private boolean isSelectPhoto = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTakePhoto().onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        ButterKnife.bind(this);
        init(savedInstanceState);
        setListener();
        setUserInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_NICK_NAME) {
            if (resultCode == InputActivity.CODE_EDIT) {
                mNickNameText.setText(data.getStringExtra("newText"));
            }
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mPresenter = new EditUserPresenterImpl(this, this);

        mToolBar.setTitle(getString(R.string.edit_info));
        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void setListener() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mAvatarCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPhotoDialog == null) {
                    mPhotoDialog = ThemeUtil.getThemeDialogBuilder(EditUserActivity.this)
                            .setTitle(R.string.change_avatar)
                            .setItems(selectPhotoItems, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    File file = new File(getCacheDir(), "photo");
                                    if (!file.exists())
                                        file.mkdirs();

                                    CompressConfig compressConfig = new CompressConfig.Builder()
                                            .setMaxSize(50 * 1024).setMaxPixel(1024).enableReserveRaw(false).create();
                                    CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();

                                    TakePhoto takePhoto = getTakePhoto();

                                    takePhoto.onEnableCompress(compressConfig, true);
                                    if (which == 0) {
                                        takePhoto.onPickFromCaptureWithCrop(Uri.fromFile(new File(file, "cache.png")), cropOptions);
                                    } else if (which == 1) {
                                        takePhoto.onPickFromGalleryWithCrop(Uri.fromFile(new File(file, "cache.png")), cropOptions);
                                    }
                                }
                            })
                            .create();
                }
                mPhotoDialog.show();
            }
        });

        mNickNameCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditUserActivity.this, InputActivity.class);
                intent.putExtra("text", mNickNameText.getText().toString());
                startActivityForResult(intent, CODE_NICK_NAME);
            }
        });

        mSexCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSexDialog == null)
                    mSexDialog = ThemeUtil.getThemeDialogBuilder(EditUserActivity.this)
                            .setTitle(R.string.select_sex)
                            .setItems(selectSexItems, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        mSexNameText.setText(getString(R.string.man));
                                    } else {
                                        mSexNameText.setText(getString(R.string.woman));
                                    }
                                }
                            })
                            .create();
                mSexDialog.show();
            }
        });

        mBirthdayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year;
                int month;
                int day;
                String birthday = mBirthdayText.getText().toString();
                if (!birthday.equals(getString(R.string.please_select))) {
                    birthday = birthday.replace("-", "");
                    year = Integer.valueOf(birthday.substring(0, 4));
                    month = Integer.valueOf(birthday.substring(4, 6));
                    day = Integer.valueOf(birthday.substring(6, 8));
                } else {
                    Calendar now = Calendar.getInstance();
                    year = now.get(Calendar.YEAR);
                    month = now.get(Calendar.MONTH) + 1;
                    day = now.get(Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month++;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(year).append("-");
                        if (month < 10) stringBuilder.append("0");
                        stringBuilder.append(month);
                        stringBuilder.append("-");
                        if (day < 10) stringBuilder.append("0");
                        stringBuilder.append(day);
                        mBirthdayText.setText(stringBuilder.toString());
                    }
                };
                if (ThemeUtil.newInstance().isNightMode()) {
                    mDateDialog = new DatePickerDialog(EditUserActivity.this, R.style.NightDialogTheme
                            , onDateSetListener, year, month - 1, day);
                } else {
                    mDateDialog = new DatePickerDialog(EditUserActivity.this, onDateSetListener, year, month - 1, day);
                }
                mDateDialog.show();
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sex = 0;
                if (mSexNameText.getText().toString().equals("男")) {
                    sex = 1;
                } else if (mSexNameText.getText().toString().equals("女")) {
                    sex = 2;
                }
                String nickName = mNickNameText.getText().toString();

                String birthday = mBirthdayText.getText().toString().replace("-", "");

                if (!isSelectPhoto && nickName.equals(user.getNickName())
                        && sex == user.getSex() && birthday.equals(String.valueOf(user.getBirthday()))) {
                    showSnackBar(getString(R.string.no_change));
                } else {
                    if (mProgressDialog == null) {
                        if (ThemeUtil.newInstance().isNightMode()) {
                            mProgressDialog = new ProgressDialog(EditUserActivity.this, R.style.NightDialogTheme);
                        } else {
                            mProgressDialog = new ProgressDialog(EditUserActivity.this);
                        }
                        mProgressDialog.setMessage(getString(R.string.is_saving));
                        mProgressDialog.setCancelable(false);
                    }

                    if (sex == user.getSex()) sex = -1;
                    if (nickName.equals(user.getNickName())) nickName = "";

                    String path = "";
                    if (isSelectPhoto) {
                        path = getCacheDir().getAbsolutePath() + File.separator +
                                "takephoto_cache" + File.separator +
                                "cache.png";
                    }

                    mPresenter.saveInfo(user.getId(), user.getToken(), nickName, sex, birthday, path);
                    mProgressDialog.show();
                }
            }
        });
    }

    private void setUserInfo() {
        Uri uri;
        if (!user.getPhoto().equals("NoImage")) {
            uri = FrescoLoader.getUri(MyUtil.getPhotoUrl(user.getPhoto()));
        } else {
            uri = FrescoLoader.getUri(R.drawable.user_default_avatar);
        }
        FrescoLoader.load(uri)
                .setCircle()
                .setBorder(4, Color.WHITE)
                .into(mAvatarImg);
        FrescoLoader.load(uri)
                .resize(128, 64)
                .setDurationTime(1000)
                .setPostprocessor(new BlurPostprocessor(EditUserActivity.this))
                .into(mAvatarBackgroundImg);

        mLoginNameText.setText(user.getLoginName());
        mNickNameText.setText(user.getNickName());
        String birthday;
        if (user.getBirthday() == 0 || String.valueOf(user.getBirthday()).length() != 8) {
            birthday = getString(R.string.please_select);
        } else {
            birthday = String.valueOf(user.getBirthday());
            birthday = birthday.substring(0, 4) + "-" + birthday.substring(4, 6) + "-" + birthday.substring(6, 8);
        }
        mBirthdayText.setText(birthday);
        switch (user.getSex()) {
            case 1:
                mSexNameText.setText(getString(R.string.man));
                break;
            case 2:
                mSexNameText.setText(getString(R.string.woman));
                break;
            default:
                mSexNameText.setText(getString(R.string.unknown));
        }
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {
        if (result != null && result.getImage() != null) {
            String path = result.getImage().getCompressPath();
            if (path != null) {
                File file = new File(path);
                if (file.exists()) {
                    FrescoLoader.load(file)
                            .setCircle()
                            .setBorder(4, Color.WHITE)
                            .clearImgCache()
                            .into(mAvatarImg);
                    FrescoLoader.load(file)
                            .resize(128, 64)
                            .setPostprocessor(new BlurPostprocessor(EditUserActivity.this))
                            .clearImgCache()
                            .into(mAvatarBackgroundImg);
                    isSelectPhoto = true;
                    return;
                }
            }
        }
        showSnackBar(getString(R.string.get_photo_error));
    }

    @Override
    public void takeFail(TResult result, String msg) {
        showSnackBar(getString(R.string.get_photo_error));
    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void saveEnd() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public void saveSuccess() {
        setResult(UserActivity.CODE_CHANGE_INFO);
        finish();
    }

    @Override
    public void saveError(String message) {
        showSnackBar(message);
    }
}
