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
import com.team60.ournews.module.model.Theme;

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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ThemViewHolder viewHolder = (ThemViewHolder) holder;
        final int positionTemp = position;
        final Theme theme = themes.get(positionTemp);
        viewHolder.mImg.setColorFilter(theme.getColor());
        viewHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onClick(theme, positionTemp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

    class ThemViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout mLayout;
        ImageView mImg;

        public ThemViewHolder(View itemView) {
            super(itemView);
            mLayout = (FrameLayout) itemView.findViewById(R.id.item_theme_layout);
            mImg = (ImageView) itemView.findViewById(R.id.item_theme_img);
        }
    }
}
