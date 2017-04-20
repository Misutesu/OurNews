package com.team60.ournews.module.view;

import com.team60.ournews.module.model.CheckUpdateResult;
import com.team60.ournews.module.view.base.BaseView;

/**
 * Created by Misutesu on 2017/4/20 0020.
 */

public interface MainView extends BaseView {
    void hasNewVersion(CheckUpdateResult result);
}
