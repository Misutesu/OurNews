package com.team60.ournews.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mistesu.frescoloader.FrescoLoader;
import com.team60.ournews.R;
import com.team60.ournews.module.model.New;
import com.team60.ournews.util.MyUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Misutesu on 2017/1/20 0020.
 */

public class TypeFragmentRecyclerViewAdapter extends RecyclerView.Adapter {

    private final int NORMAL_TYPE = 0;
    private final int FOOTER_TYPE = 1;

    private Context context;
    private List<New> news;

    private OnItemClickListener onItemClickListener;

    private ProgressBar mProgressBar;

    public interface OnItemClickListener {
        void onItemClick(View view, New n, int position);
    }

    public TypeFragmentRecyclerViewAdapter(Context context, List<New> news) {
        this.context = context;
        this.news = news;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == news.size()) {
            return FOOTER_TYPE;
        }
        return NORMAL_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_TYPE) {
            return new FooterViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment_foorter, parent, false));
        }
        return new NormalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_type_new_normal, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            final NormalViewHolder viewHolder = (NormalViewHolder) holder;
            final int positionTemp = position;
            final New n = news.get(positionTemp);
            viewHolder.mTitleText.setText(n.getTitle());
            viewHolder.mTimeText.setText(n.getCreateTime());

            FrescoLoader.load(MyUtil.getPhotoUrl(n.getCover()))
                    .setCircleRound(6)
                    .into(viewHolder.mCoverImg);

            viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(viewHolder.mCoverImg, n, positionTemp);
                }
            });
        } else if (holder instanceof FooterViewHolder) {
            mProgressBar = ((FooterViewHolder) holder).mProgressBar;
        }
    }

    @Override
    public int getItemCount() {
        return news.size() + 1;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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

    public class NormalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_type_new_layout)
        LinearLayout mLayout;
        @BindView(R.id.item_type_new_img)
        SimpleDraweeView mCoverImg;
        @BindView(R.id.item_type_new_title_text)
        TextView mTitleText;
        @BindView(R.id.item_type_new_create_time_text)
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
