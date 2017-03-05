package com.team60.ournews.module.view;

import android.util.SparseArray;

import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.view.base.BaseView;

import java.util.List;

/**
 * Created by Misutesu on 2016/12/28 0028.
 */

public interface HomeView extends BaseView {
    void getNewsEnd();

    void getNewsSuccess(SparseArray<List<New>> news, int type);

    void getNewsError(String message);
}
