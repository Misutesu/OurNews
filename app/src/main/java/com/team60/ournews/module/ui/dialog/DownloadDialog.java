package com.team60.ournews.module.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.team60.ournews.R;
import com.team60.ournews.util.ThemeUtil;

/**
 * Created by Misutesu on 2017/4/22 0022.
 */

public class DownloadDialog extends AlertDialog {

    public interface OnClickListener {
        void onCancelClick();

        void onBackgroundClick();
    }

    private ProgressBar mProgressBar;
    private AppCompatTextView mProgressText;
    private AppCompatButton mCancelBtn;
    private AppCompatButton mBackgroundBtn;

    private boolean isForced;
    private OnClickListener mOnClickListener;

    public static DownloadDialog create(Context context) {
        if (ThemeUtil.newInstance().isNightMode()) {
            return new DownloadDialog(context, R.style.NightDialogTheme);
        }
        return new DownloadDialog(context);
    }

    private DownloadDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    private DownloadDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_download, null);
        setView(view);
        mProgressBar = (ProgressBar) view.findViewById(R.id.dialog_download_progress);
        mProgressText = (AppCompatTextView) view.findViewById(R.id.dialog_download_progress_text);
        mCancelBtn = (AppCompatButton) view.findViewById(R.id.dialog_download_cancel_btn);
        mBackgroundBtn = (AppCompatButton) view.findViewById(R.id.dialog_download_background_btn);
    }

    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
        mProgressText.setText(progress + "%");
    }

    public DownloadDialog setForced(boolean isForced) {
        this.isForced = isForced;
        if (isForced) {
            mBackgroundBtn.setVisibility(View.GONE);
        } else {
            mBackgroundBtn.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public DownloadDialog setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        mCancelBtn.setOnClickListener(mOnClick);
        mBackgroundBtn.setOnClickListener(mOnClick);
        return this;
    }

    private View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_download_cancel_btn:
                    mOnClickListener.onCancelClick();
                    cancel();
                    break;
                case R.id.dialog_download_background_btn:
                    if (!isForced) mOnClickListener.onBackgroundClick();
                    cancel();
                    break;
            }
        }
    };
}
