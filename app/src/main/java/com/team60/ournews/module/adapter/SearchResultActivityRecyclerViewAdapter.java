package com.team60.ournews.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.team60.ournews.R;
import com.team60.ournews.module.model.New;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Misutesu on 2017/1/23 0023.
 */

public class SearchResultActivityRecyclerViewAdapter extends RecyclerView.Adapter {

    private final int NORMAL_TYPE = 0;
    private final int FOOTER_TYPE = 1;

    private Context context;
    private List<New> news;

    private ProgressBar mProgressBar;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public SearchResultActivityRecyclerViewAdapter(Context context, List<New> news) {
        this.context = context;
        this.news = news;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == news.size())
            return FOOTER_TYPE;
        return NORMAL_TYPE;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_TYPE)
            return new FooterViewHolder(LayoutInflater.from(context).inflate(0, parent, false));
        return new NormalViewHolder(LayoutInflater.from(context).inflate(0, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return news.size() + 1;
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_type_new_layout)
        LinearLayout mLayout;
        @BindView(R.id.item_type_new_img)
        ImageView mCoverImg;
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
