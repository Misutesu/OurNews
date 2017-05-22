package com.team60.ournews.module.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.team60.ournews.R;
import com.team60.ournews.common.Constants;
import com.team60.ournews.listener.MyObjectAnimatorListener;
import com.team60.ournews.module.ui.activity.base.BaseActivity;
import com.team60.ournews.util.SignUtil;
import com.team60.ournews.util.ThemeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FirstActivity extends BaseActivity {

    @BindView(R.id.activity_first_text)
    TextView mText;

    private AnimatorSet mAnimatorSet;
    private AlertDialog mSignErrorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);
        hideNavigationBar();
        if (Constants.SHA1.equals(SignUtil.getSHA1(this))) {
            init(savedInstanceState);
            setListener();
            mAnimatorSet.start();
        } else {
            showSignErrorDialog();
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet
                .play(ObjectAnimator.ofFloat(mText, "alpha", 1.0f, 1.0f).setDuration(500))
                .after(ObjectAnimator.ofFloat(mText, "alpha", 0.0f, 1.0f).setDuration(1000));
    }

    @Override
    public void setListener() {
        mAnimatorSet.addListener(new MyObjectAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startMain();
            }
        });
    }

    @Override
    public void showSnackBar(String message) {

    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showSignErrorDialog() {
        if (mSignErrorDialog == null) {
            mSignErrorDialog = ThemeUtil.getThemeDialogBuilder(this)
                    .setTitle(R.string.sign_error)
                    .setPositiveButton(R.string.uninstall, null)
                    .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create();
        }
        mSignErrorDialog.show();
        mSignErrorDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(FirstActivity.this, R.string.open_app_info_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
