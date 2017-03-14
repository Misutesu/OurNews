package com.team60.ournews.module.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.R;
import com.team60.ournews.module.bean.Comment;
import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.bean.OtherUser;
import com.team60.ournews.util.MyUtil;
import com.team60.ournews.util.ThemeUtil;
import com.team60.ournews.util.UiUtil;
import com.team60.ournews.widget.LikeButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.fresco.processors.ColorFilterPostprocessor;

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

        void onAvatarClick(OtherUser otherUser);

        void onLayoutClick(Comment comment);

        void onLickBtnClick(Comment comment, TextView mLikeNumText, LikeButton likeButton);
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
            ((HeaderViewHolder) holder).mCardView.setOnClickListener(mOnClickListener);
        } else if (holder instanceof FooterViewHolder) {
            mProgressBar = ((FooterViewHolder) holder).mProgressBar;
        } else if (holder instanceof NormalViewHolder) {
            position--;
            final Comment comment = comments.get(position);
            OtherUser user = comment.getUser();
            NormalViewHolder viewHolder = (NormalViewHolder) holder;
            viewHolder.mUserNameText.setText(user.getNickName());
            viewHolder.mContentText.setText(comment.getContent());
            viewHolder.mTimeText.setText(comment.getCreateTime());
            String likeNumText = comment.getLickNum() + " 赞";
            viewHolder.mLikeNumText.setText(likeNumText);

            if (comment.getIsLike() == -1 || comment.getIsLike() == 0) {
                viewHolder.mLikeBtn.noLike();
            } else {
                viewHolder.mLikeBtn.hasLike();
            }
            viewHolder.mLikeBtn.setTag(R.id.tag_like_num_text, viewHolder.mLikeNumText);
            viewHolder.mLikeBtn.setTag(R.id.tag_comment, comment);
            viewHolder.mLikeBtn.setOnLikeButtonListener(mOnLikeButtonListener);

            if (user.getPhoto().equals("NoImage")) {
                FrescoLoader.load(R.drawable.user_default_avatar)
                        .setCircle()
                        .setPostprocessor(new ColorFilterPostprocessor(ThemeUtil.getColor(context.getTheme(), R.attr.colorPrimary)))
                        .into(viewHolder.mAvatarImg);
            } else {
                FrescoLoader.load(MyUtil.getPhotoUrl(user.getPhoto()))
                        .setCircle()
                        .into(viewHolder.mAvatarImg);
            }

            viewHolder.mAvatarImg.setTag(R.id.tag_comment, comment.getUser());
            viewHolder.mAvatarImg.setOnClickListener(mOnClickListener);

            viewHolder.mLayout.setTag(comment);
            viewHolder.mLayout.setOnClickListener(mOnClickListener);

            if (comment.getChildList() != null && comment.getChildList().size() != 0) {
                if (viewHolder.mRecyclerView == null) {
                    viewHolder.getRecyclerView();
                    if (viewHolder.mAdapter == null) {
                        viewHolder.mAdapter = new CommentActivityChildRecyclerViewAdapter(context, comment.getChildList());
                        viewHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                    }
                }
                viewHolder.mCardView.setOnClickListener(mOnClickListener);
                viewHolder.mRecyclerView.setAdapter(viewHolder.mAdapter);
            }
        }
    }

    @Override
    public int getItemCount() {
        return comments.size() + 2;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_comment_header_card_view:
                    if (onItemClickListener != null)
                        onItemClickListener.onTitleClick();
                    break;
                case R.id.item_comment_avatar_img:
                    OtherUser otherUser = (OtherUser) v.getTag();
                    if (otherUser != null && onItemClickListener != null)
                        onItemClickListener.onAvatarClick(otherUser);
                    break;
                case R.id.item_comment_layout:
                    Comment comment = (Comment) v.getTag();
                    if (comment != null && onItemClickListener != null)
                        onItemClickListener.onLayoutClick(comment);
                    break;
            }
        }
    };

    private LikeButton.OnLikeButtonListener mOnLikeButtonListener = new LikeButton.OnLikeButtonListener() {
        @Override
        public void onButtonClick(View v) {
            TextView mLikeNumText = (TextView) v.getTag(R.id.tag_like_num_text);
            Comment comment = (Comment) v.getTag(R.id.tag_comment);
            if (mLikeNumText != null && comment != null && onItemClickListener != null) {
                onItemClickListener.onLickBtnClick(comment, mLikeNumText, (LikeButton) v);
            }
        }
    };

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

        private View view;

        @BindView(R.id.item_comment_layout)
        LinearLayout mLayout;
        @BindView(R.id.item_comment_avatar_img)
        SimpleDraweeView mAvatarImg;
        @BindView(R.id.item_comment_user_name_text)
        TextView mUserNameText;
        @BindView(R.id.item_comment_content_text)
        TextView mContentText;
        @BindView(R.id.item_comment_time_text)
        TextView mTimeText;
        @BindView(R.id.item_comment_like_num_text)
        TextView mLikeNumText;
        @BindView(R.id.item_comment_like_btn)
        LikeButton mLikeBtn;
        @BindView(R.id.item_comment_content_view_stub)
        ViewStub mViewStub;

        private CardView mCardView;
        private RecyclerView mRecyclerView;
        private CommentActivityChildRecyclerViewAdapter mAdapter;

        public NormalViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void getRecyclerView() {
            mViewStub.inflate();
            mCardView = (CardView) view.findViewById(R.id.item_comment_child_card_view);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.item_comment_child_recycler_view);
            mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
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
