package com.team60.ournews.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.R;
import com.team60.ournews.module.bean.CommentChild;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.ThemeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.fresco.processors.ColorFilterPostprocessor;

/**
 * Created by wujiaquan on 2017/3/13.
 */

public class CommentActivityChildRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<CommentChild> mChildList;

    public CommentActivityChildRecyclerViewAdapter(Context context, List<CommentChild> childList) {
        mContext = context;
        mChildList = childList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_comment_child_normal, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            NormalViewHolder viewHolder = (NormalViewHolder) holder;
            if (mChildList.get(position).getUser().getPhoto().equals("NoImage")) {
                FrescoLoader.load(R.drawable.user_default_avatar)
                        .setCircle()
                        .setPostprocessor(new ColorFilterPostprocessor(ThemeUtil.getColor(mContext.getTheme(), R.attr.colorPrimary)))
                        .into(viewHolder.mImg);
            } else {
                FrescoLoader.load(MyUtil.getPhotoUrl(mChildList.get(position).getUser().getPhoto()))
                        .setCircle()
                        .into(viewHolder.mImg);
            }

            viewHolder.mNameText.setText(mChildList.get(position).getUser().getNickName());
            viewHolder.mTimeText.setText(mChildList.get(position).getCreateTime());
            viewHolder.mContentText.setText(mChildList.get(position).getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mChildList == null ? 0 : mChildList.size();
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_comment_child_img)
        SimpleDraweeView mImg;
        @BindView(R.id.item_comment_child_name_text)
        TextView mNameText;
        @BindView(R.id.item_comment_child_time_text)
        TextView mTimeText;
        @BindView(R.id.item_comment_child_content_text)
        TextView mContentText;

        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
