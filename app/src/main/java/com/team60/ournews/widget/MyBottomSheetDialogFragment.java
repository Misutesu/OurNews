package com.team60.ournews.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team60.ournews.R;
import com.team60.ournews.util.UiUtil;

/**
 * Created by wujiaquan on 2017/3/16.
 */

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Context mContext;
    private BottomSheetDialog mDialog;
    private View mView;

    private BottomSheetBehavior mBehavior;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ViewGroup) mView.getParent()).removeAllViews();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.layout_comment_child, null);
            initView();
        }
        mDialog.setContentView(mView);
        mBehavior = BottomSheetBehavior.from((View) mView.getParent());
        mBehavior.setHideable(true);
        mBehavior.setPeekHeight(UiUtil.getScreenHeight() * 4 / 5);
        return mDialog;
    }

    private void initView() {

    }
}
