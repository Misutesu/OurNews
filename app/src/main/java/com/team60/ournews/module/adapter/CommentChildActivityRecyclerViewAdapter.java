package com.team60.ournews.module.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.team60.ournews.module.bean.CommentChild;

import java.util.List;

/**
 * Created by wujiaquan on 2017/3/17.
 */

public class CommentChildActivityRecyclerViewAdapter extends RecyclerView.Adapter {

    private final int NORMAL_TYPE = 0;
    private final int FOOTER_TYPE = 1;

    private Context mContext;
    private List<CommentChild> mList;

    public CommentChildActivityRecyclerViewAdapter(Context context, List<CommentChild> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        return NORMAL_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL_TYPE)
            return null;
        if (viewType == FOOTER_TYPE)
            return null;
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalViewHolder) {
            NormalViewHolder viewHolder = (NormalViewHolder) holder;
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder viewHolder = (FooterViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder {

        public NormalViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
