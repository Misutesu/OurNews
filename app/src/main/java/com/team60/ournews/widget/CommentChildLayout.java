package com.team60.ournews.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.R;
import com.team60.ournews.module.bean.CommentChild;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.fresco.processors.ColorFilterPostprocessor;

/**
 * Created by wujiaquan on 2017/3/16.
 */

public class CommentChildLayout extends LinearLayout {

    private List<CommentChild> list;

    private Context mContext;

    public CommentChildLayout(Context context) {
        super(context);
        init(context);
    }

    public CommentChildLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CommentChildLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CommentChildLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setOrientation(VERTICAL);
        if (list == null) list = new ArrayList<>();
    }

    public void setData(List<CommentChild> list) {
        this.list.addAll(list);
        refreshLayout();
    }

    private void refreshLayout() {
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment_child_normal, this, false);
            addView(view);
            ViewHolder viewHolder = new ViewHolder(view);
            String photo = list.get(i).getUser().getPhoto();
            if (photo.equals("NoImage")) {
                FrescoLoader.load(R.drawable.user_default_avatar)
                        .setCircle()
                        .setPostprocessor(new ColorFilterPostprocessor(ThemeUtil.getColor(mContext.getTheme(), R.attr.colorPrimary)))
                        .into(viewHolder.mImg);
            } else {
                FrescoLoader.load(MyUtil.getPhotoUrl(photo))
                        .setCircle()
                        .into(viewHolder.mImg);
            }

            viewHolder.mNameText.setText(list.get(i).getUser().getNickName());
            viewHolder.mTimeText.setText(list.get(i).getCreateTime());
            viewHolder.mContentText.setText(list.get(i).getContent());
        }
    }

    public class ViewHolder {
        @BindView(R.id.item_comment_child_img)
        SimpleDraweeView mImg;
        @BindView(R.id.item_comment_child_name_text)
        AppCompatTextView mNameText;
        @BindView(R.id.item_comment_child_time_text)
        AppCompatTextView mTimeText;
        @BindView(R.id.item_comment_child_content_text)
        AppCompatTextView mContentText;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
