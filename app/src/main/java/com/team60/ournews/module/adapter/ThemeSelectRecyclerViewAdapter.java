package com.team60.ournews.module.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.team60.ournews.R;
import com.team60.ournews.module.bean.Theme;
import com.team60.ournews.util.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Misutesu on 2016/10/19 0019.
 */

public class ThemeSelectRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<Theme> themes;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(Theme theme, int position);
    }

    public ThemeSelectRecyclerViewAdapter(Context context) {
        this.context = context;
        themes = new ArrayList<>();
        themes.add(new Theme(ContextCompat.getColor(context, R.color.colorPrimaryBlue), -1));
        themes.add(new Theme(ContextCompat.getColor(context, R.color.colorPrimaryBlueBlack), R.style.BlueBlackTheme));
        themes.add(new Theme(ContextCompat.getColor(context, R.color.colorPrimaryGreen), R.style.GreenTheme));
        themes.add(new Theme(ContextCompat.getColor(context, R.color.colorPrimaryGreenBlack), R.style.GreenBlackTheme));
        themes.add(new Theme(ContextCompat.getColor(context, R.color.colorPrimaryYellow), R.style.YellowTheme));
        themes.add(new Theme(ContextCompat.getColor(context, R.color.colorPrimaryRed), R.style.RedTheme));
        themes.add(new Theme(ContextCompat.getColor(context, R.color.colorPrimaryPink), R.style.PinkTheme));
        themes.add(new Theme(ContextCompat.getColor(context, R.color.colorPrimaryBrown), R.style.BrownTheme));
        themes.add(new Theme(ContextCompat.getColor(context, R.color.black), R.style.BlackTheme));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThemViewHolder(LayoutInflater.from(context).inflate(R.layout.item_theme, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ThemViewHolder viewHolder = (ThemViewHolder) holder;
        final Theme theme = themes.get(position);
        viewHolder.mImg.setColorFilter(theme.getColor());
        viewHolder.mLayout.setTag(R.id.tag_theme, theme);
        viewHolder.mLayout.setTag(R.id.tag_position, position);
        viewHolder.mLayout.setOnClickListener(mOnClickListener);
        if (position == ThemeUtil.newInstance().getStyleNum()) {
            viewHolder.mClickImg.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mClickImg.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.item_theme_layout:
                    Theme theme = (Theme) v.getTag(R.id.tag_theme);
                    int position = (int) v.getTag(R.id.tag_position);
                    if (theme != null && onItemClickListener != null)
                        onItemClickListener.onClick(theme, position);
                    break;
                default:
            }
        }
    };

    public class ThemViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout mLayout;
        private ImageView mImg;
        private ImageView mClickImg;

        public ThemViewHolder(View itemView) {
            super(itemView);
            mLayout = (FrameLayout) itemView.findViewById(R.id.item_theme_layout);
            mImg = (ImageView) itemView.findViewById(R.id.item_theme_img);
            mClickImg = (ImageView) itemView.findViewById(R.id.item_theme_click);
        }
    }
}
