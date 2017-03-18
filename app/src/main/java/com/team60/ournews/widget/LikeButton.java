package com.team60.ournews.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team60.ournews.R;

/**
 * Created by wujiaquan on 2017/3/14.
 */

public class LikeButton extends FrameLayout {

    private Button mButton;
    private TextView mText;
    private ProgressBar mProgressBar;

    private boolean isLoad;

    private OnLikeButtonListener mOnLikeButtonListener;

    public interface OnLikeButtonListener {
        void onButtonClick(View v);
    }

    public LikeButton(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LikeButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LikeButton(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LikeButton(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_like_button, this, true);
        mButton = (Button) view.findViewById(R.id.layout_like_button);
        mText = (TextView) view.findViewById(R.id.layout_like_button_text);
        mProgressBar = (ProgressBar) view.findViewById(R.id.layout_like_button_progress);

        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLikeButtonListener != null && !isLoad) {
                    mOnLikeButtonListener.onButtonClick(LikeButton.this);
                }
            }
        });
    }

    public void setOnLikeButtonListener(OnLikeButtonListener onLikeButtonListener) {
        mOnLikeButtonListener = onLikeButtonListener;
    }

    public void noLike() {
        mText.setText("点赞");
    }

    public void hasLike() {
        mText.setText("取消点赞");
    }

    public void startLoad() {
        isLoad = true;
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(mText, "alpha", mText.getAlpha(), 0f))
                .before(ObjectAnimator.ofFloat(mProgressBar, "alpha", mProgressBar.getAlpha(), 1f));
        set.setDuration(100);
        set.start();
    }

    public void endLoad() {
        isLoad = false;
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(mProgressBar, "alpha", mProgressBar.getAlpha(), 0f))
                .before(ObjectAnimator.ofFloat(mText, "alpha", mText.getAlpha(), 1f));
        set.setDuration(100);
        set.start();
    }
}
