package com.team60.ournews.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team60.ournews.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Misutesu on 2017/1/23 0023.
 */

public class SearchActivityRecyclerViewAdapter extends RecyclerView.Adapter {

    private final int NORMAL_TYPE = 1;
    private final int FOOTER_TYPE = 2;

    private Context context;
    private List<String> histories;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onTextItemClick(int position);

        void onClearBtnClick();
    }

    public SearchActivityRecyclerViewAdapter(Context context, List<String> histories) {
        this.context = context;
        this.histories = histories;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == histories.size())
            return FOOTER_TYPE;
        return NORMAL_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_TYPE)
            return new FooterViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_footer, parent, false));
        return new NormalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            NormalViewHolder viewHolder = (NormalViewHolder) holder;
            viewHolder.mText.setText(histories.get(position));
            viewHolder.mLayout.setTag(position);
            viewHolder.mLayout.setOnClickListener(mOnClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return histories.size() + 1;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_search_layout:
                    int position = (int) v.getTag();
                    if (onItemClickListener != null)
                        onItemClickListener.onTextItemClick(position);
                    break;
                case R.id.item_search_footer_text:
                    if (onItemClickListener != null)
                        onItemClickListener.onClearBtnClick();
                    break;
                default:
            }
        }
    };

    public class NormalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_search_layout)
        LinearLayout mLayout;
        @BindView(R.id.item_search_text)
        TextView mText;

        public NormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mLayout = (LinearLayout) itemView.findViewById(R.id.item_search_layout);
            mText = (TextView) itemView.findViewById(R.id.item_search_text);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_search_footer_text)
        TextView mTextView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mTextView.setOnClickListener(mOnClickListener);
        }
    }
}
