package com.team60.ournews.module.view;

import com.team60.ournews.module.view.base.BaseView;

/**
 * Created by Misutesu on 2016/12/27 0027.
 */

public interface EditUserView extends BaseView {
    void saveEnd();

    void saveSuccess();

    void saveError(String message);
}
