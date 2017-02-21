package com.team60.ournews.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.team60.ournews.R;
import com.team60.ournews.module.model.Comment;
import com.team60.ournews.module.model.New;
import com.team60.ournews.module.model.OtherUser;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.UiUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Misutesu on 2016/12/29 0029.
 */

public class CommentActivityRecyclerViewAdapter extends RecyclerView.Adapter {

    private final int HEADER_TYPE = 0;
    private final int NORMAL_TYPE = 1;
    private final int FOOTER_TYPE = 2;

    private Context context;
    private New n;
    private List<Comment> comments;

    private ProgressBar mProgressBar;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onTitleClick();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public CommentActivityRecyclerViewAdapter(Context context, New n, List<Comment> comments) {
        this.context = context;
        this.n = n;
        this.comments = comments;
    }

    public void setLoadMore(boolean isShow) {
        if (isShow) {
            if (mProgressBar.getVisibility() == View.INVISIBLE)
                mProgressBar.setVisibility(View.VISIBLE);
        } else {
            if (mProgressBar.getVisibility() == View.VISIBLE)
                mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == comments.size() + 1) {
            return FOOTER_TYPE;
        } else if (position == 0) {
            return HEADER_TYPE;
        }
        return NORMAL_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            return new HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment_header, parent, false), n);
        } else if (viewType == FOOTER_TYPE) {
            return new FooterViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment_foorter, parent, false));
        }
        return new NormalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment_normal, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onTitleClick();
                }
            });
        } else if (holder instanceof FooterViewHolder) {
            mProgressBar = ((FooterViewHolder) holder).mProgressBar;
        } else if (holder instanceof NormalViewHolder) {
            position--;
            Comment comment = comments.get(position);
            OtherUser user = comment.getUser();
            NormalViewHolder viewHolder = (NormalViewHolder) holder;
            viewHolder.mUserNameText.setText(user.getNickName());
            viewHolder.mContentText.setText(comment.getContent());
            viewHolder.mTimeText.setText(comment.getCreateTime());
            if (!user.getPhoto().equals("NoImage")) {
                Glide.with(context).load(MyUtil.getPhotoUrl(user.getPhoto()))
                        .bitmapTransform(new CropCircleTransformation(context)).into(viewHolder.mAvatarImg);
            }
        }
    }

    @Override
    public int getItemCount() {
        return comments.size() + 2;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_comment_top_view)
        View mTopView;
        @BindView(R.id.item_comment_header_background_view)
        View mTopView1;
        @BindView(R.id.item_comment_header_card_view)
        CardView mCardView;
        @BindView(R.id.item_comment_header_title_text)
        TextView mTitleText;

        public HeaderViewHolder(View itemView, New n) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mTitleText.setText(n.getTitle());
            mCardView.post(new Runnable() {
                @Override
                public void run() {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtil.getStatusBarHeight());
                    mTopView.setLayoutParams(layoutParams);
                    RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mCardView.getHeight() / 2);
                    mTopView1.setLayoutParams(layoutParams1);
                }
            });
        }
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_comment_avatar_img)
        ImageView mAvatarImg;
        @BindView(R.id.item_comment_user_name_text)
        TextView mUserNameText;
        @BindView(R.id.item_comment_content_text)
        TextView mContentText;
        @BindView(R.id.item_comment_time_text)
        TextView mTimeText;

        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_comment_footer_progress_bar)
        ProgressBar mProgressBar;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
