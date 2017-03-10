package com.team60.ournews.module.view;

import com.team60.ournews.module.bean.New;
import com.team60.ournews.module.view.base.BaseView;

import java.util.List;

/**
 * Created by wujiaquan on 2017/3/10.
 */

public interface CenterView extends BaseView {
    void onGetNewsEnd();

    void onGetNewsSuccess(List<New> news, int page);

    void onGetNewsError(String error, int page);
}
