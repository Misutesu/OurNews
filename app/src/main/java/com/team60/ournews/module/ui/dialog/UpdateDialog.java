package com.team60.ournews.module.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;

import com.team60.ournews.R;
import com.team60.ournews.module.model.CheckUpdateResult;
import com.team60.ournews.util.ThemeUtil;

import java.text.DecimalFormat;

/**
 * Created by Misutesu on 2017/4/20 0020.
 */

public class UpdateDialog extends AlertDialog {

    public interface OnClickListener {
        void onUpdateClick();

        void onIgnoreClick();
    }

    private AppCompatTextView mTitleText;
    private AppCompatTextView mSizeText;
    private AppCompatTextView mTimeText;
    private AppCompatTextView mDescriptionText;
    private AppCompatButton mIgnoreBtn;
    private AppCompatButton mUpdateBtn;

    private OnClickListener mOnClickListener;
    private boolean isForced;

    public static UpdateDialog create(Context context, boolean isForced) {
        if (ThemeUtil.newInstance().isNightMode()) {
            return new UpdateDialog(context, R.style.NightDialogTheme, isForced);
        }
        return new UpdateDialog(context, isForced);
    }

    private UpdateDialog(@NonNull Context context, boolean isForced) {
        super(context);
        init(context, isForced);
    }

    private UpdateDialog(@NonNull Context context, @StyleRes int themeResId, boolean isForced) {
        super(context, themeResId);
        init(context, isForced);
    }

    private void init(Context context, boolean isForced) {
        this.isForced = isForced;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null);
        setView(view);
        mTitleText = (AppCompatTextView) view.findViewById(R.id.dialog_update_title_text);
        mSizeText = (AppCompatTextView) view.findViewById(R.id.dialog_update_size_text);
        mTimeText = (AppCompatTextView) view.findViewById(R.id.dialog_update_time_text);
        mDescriptionText = (AppCompatTextView) view.findViewById(R.id.dialog_update_description_text);
        mIgnoreBtn = (AppCompatButton) view.findViewById(R.id.dialog_update_ignore_btn);
        mUpdateBtn = (AppCompatButton) view.findViewById(R.id.dialog_update_update_btn);

        if (isForced) {
            mIgnoreBtn.setVisibility(View.GONE);
            mUpdateBtn.setText(context.getString(R.string.update_forced));
        }
    }

    public UpdateDialog setUpdateInfo(CheckUpdateResult result) {
        double size = (double) (result.getData().getFileSize()) / 1024 / 1024;
        DecimalFormat df = new DecimalFormat("#.00");
        mTitleText.setText(mTitleText.getText().toString() + " " + result.getData().getNowVersionName());
        mSizeText.setText(df.format(size) + " MB");
        mTimeText.setText("[ " + result.getData().getUpdateTime() + " ]");
        mDescriptionText.setText(result.getData().getDescription().replace("\\", "\n"));
        return this;
    }

    public UpdateDialog setOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        mUpdateBtn.setOnClickListener(mOnClick);
        mIgnoreBtn.setOnClickListener(mOnClick);
        return this;
    }

    private View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.dialog_update_update_btn:
                    mOnClickListener.onUpdateClick();
                    cancel();
                    break;
                case R.id.dialog_update_ignore_btn:
                    if (!isForced) mOnClickListener.onIgnoreClick();
                    cancel();
                    break;
            }
        }
    };
}
