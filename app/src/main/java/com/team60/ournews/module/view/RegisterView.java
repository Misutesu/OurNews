package com.team60.ournews.module.view;

import com.team60.ournews.module.view.base.BaseView;

/**
 * Created by Misutesu on 2016/12/26 0026.
 */

public interface RegisterView extends BaseView {
    void registerEnd();

    void registerSuccess();

    void registerError(String message);
}
